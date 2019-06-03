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

public class MainActivity extends AppCompatActivity {
    private Button guess;
    private Button playagain;
    private Button setprice;
    private EditText numinput;
    private TextView show;
    private Guess g;
    private int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        guess = (Button) findViewById(R.id.button1);
        playagain = (Button) findViewById(R.id.button2);
        setprice=(Button)findViewById(R.id.set);
        numinput = (EditText) findViewById(R.id.num);
        show = (TextView) findViewById(R.id.show);

        g = new Guess();
        num = g.getRandomNum();
        guess.setOnClickListener(new MyListener());
        playagain.setOnClickListener(new MyListener());
        setprice.setOnClickListener(new MyListener());

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
    }
    class MyListener implements android.view.View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.equals(guess)){
                if(g.play(num,Integer.parseInt(numinput.getText().toString())).equals("猜对了!")){
                    String str=numinput.getText().toString()+g.play(num,Integer.parseInt(numinput.getText().toString()));
                    show.append(Html.fromHtml("<br/><font color='#FF0000'>"+str+"</font>"));
                    //猜对了就结束 只能先按重新开始
                    setprice.setEnabled(false);
                    guess.setEnabled(false);
                }
                else{
                    show.append("\n"+numinput.getText().toString()+g.play(num,Integer.parseInt(numinput.getText().toString())));
                }
                numinput.setText("");
            }
            if(v.equals(playagain)){
                show.setText("重新开始，这是一个随机产生的价格!");
                numinput.setText("");
                num=g.getRandomNum();

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
