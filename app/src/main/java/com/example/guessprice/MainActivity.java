package com.example.guessprice;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private Button guess;
    private Button playagain;
    private Button setprice;
    private EditText numinput;
    private TextView show;
    private TextView show2;
    private TextView middle;
    private TextView remaintime;
    private Guess g;
    private int num;
    private int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        guess = (Button) findViewById(R.id.button1);
        playagain = (Button) findViewById(R.id.button2);
        setprice=(Button)findViewById(R.id.set);
        numinput = (EditText) findViewById(R.id.num);
        show = (TextView) findViewById(R.id.show);
        show2 = (TextView) findViewById(R.id.show2);
        middle = (TextView) findViewById(R.id.middle);
        remaintime = (TextView) findViewById(R.id.remaintime);
        g = new Guess();
        num = g.getRandomNum();
        guess.setOnClickListener(new MyListener());
        playagain.setOnClickListener(new MyListener());
        setprice.setOnClickListener(new MyListener());
        setRemaintime();//封装函数，初始化remaintime
        numinput.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(keyCode==KeyEvent.KEYCODE_ENTER ){
                    if(event.getAction()==KeyEvent.ACTION_DOWN) {
                        guess.performClick();
                        return true;
                    }
                }
                return false;
            }
        });
    }
    private void setRemaintime(){
        if(num<=100){
            time=7;//初始化为7次
        }
        else if(num>100&&num<=1000){
            time=10;//初始化为10
        }
        else{
            time=10;//暂时也设置为10，后期如果需要修改再说
        }
        //初始化剩余次数提示
        String remain=String.valueOf(time);
        remaintime.setText(Html.fromHtml("你还有"+"<font color='#FF0000'>"+remain+"</font>"+"次机会"));
    }
    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(MainActivity.this);
        //editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle(getString(R.string.setprice)).setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        num=Integer.parseInt(editText.getText().toString());
                        Toast.makeText(MainActivity.this,
                                "设置完成价格",
                                Toast.LENGTH_LONG).show();
                    }
                }).show();
        //设置价格，不会重新刷新剩余猜价格次数
    }
    class MyListener implements android.view.View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.equals(guess)){
                if(g.play(num,Integer.parseInt(numinput.getText().toString())).equals("猜对了!")){
                    String str=numinput.getText().toString()+g.play(num,Integer.parseInt(numinput.getText().toString()));
                    middle.setText(Html.fromHtml("<br/><font color='#FF0000'>"+str+"</font>"));
                    //猜对了就结束 只能先按重新开始
                    setprice.setEnabled(false);
                    guess.setEnabled(false);
                }
                else{
                    if(g.play(num,Integer.parseInt(numinput.getText().toString())).equals("猜小了")){
                        show2.append("\n"+numinput.getText().toString()+g.play(num,Integer.parseInt(numinput.getText().toString())));
                    }
                    else{//猜大了
                        show.append("\n"+numinput.getText().toString()+g.play(num,Integer.parseInt(numinput.getText().toString())));
                    }
                }
                numinput.setText("");
                //不管猜对还是没猜对，剩余猜的次数都应该-1
                time-=1;
                remaintime.setText(Html.fromHtml("你还有"+"<font color='#FF0000'>"+time+"</font>"+"次机会"));
                if(time==0){//到了0次机会，那就不让继续猜了
                    guess.setEnabled(false);
                }
            }
            if(v.equals(playagain)){
                show.setText("");
                show2.setText("");
                middle.setText("");
                Toast.makeText(MainActivity.this,
                        "重新开始，次数已刷新!",
                        Toast.LENGTH_LONG).show();
                numinput.setText("");
                num=g.getRandomNum();//TODO 这里需要进行后续的修改，不是随机价格，而是商品固有的价格
                setRemaintime();//setRemaintime again
                setprice.setEnabled(true);
                guess.setEnabled(true);
            }
            if(v.equals(setprice)){
                showInputDialog();
                numinput.setText("");

            }
        }
    }
}
