package com.example.guessprice;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private Button guess;
    private Button playagain;
    private Button rechoice;
    private EditText numinput;
    private TextView show;
    private TextView show2;
    private TextView middle;
    private TextView remaintime;
    private Guess g;
    private int num;
    private int time;
    private DatabaseHelper dbhp;
    private Goods goods;
    private ImageView imageView;
    private TextView goodsName;
    private View axis_back,axis_front;
    private int low,high;//low和high的默认值都为0
    private int scale;//来记录num的规模
    private double ratio;//用来存储 ratio = (width)/(scale)
    private TextView txtLow,txtHigh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhp=new DatabaseHelper(this);
        guess = (Button) findViewById(R.id.button1);
        playagain = (Button) findViewById(R.id.button2);
        rechoice=(Button)findViewById(R.id.rechoice);
        numinput = (EditText) findViewById(R.id.num);
        show = (TextView) findViewById(R.id.show);
        show2 = (TextView) findViewById(R.id.show2);
        middle = (TextView) findViewById(R.id.middle);
        remaintime = (TextView) findViewById(R.id.remaintime);

        axis_back=(View)findViewById(R.id.axis_back);
        axis_front=(View)findViewById(R.id.axis_front);

        txtLow=(TextView) findViewById(R.id.low);
        txtHigh=(TextView)findViewById(R.id.high);

        g = new Guess();
        goodsName=(TextView)findViewById(R.id.goodsName);
        imageView=(ImageView)findViewById(R.id.goodsImage);
        Intent intent = getIntent();
        int goodsID=intent.getIntExtra("goodsID",100);
        Toast.makeText(MainActivity.this,String.valueOf(goodsID),Toast.LENGTH_SHORT).show();

        goods=dbhp.getGoods(goodsID);

        //商品价格的设置
        num=goods.getGoodsPrice();
        //设置规模
        setScale();

        //商品名称的填充
        goodsName.setText(goods.getGoodsName());

        //商品图片的填充
        byte[] imageByte = goods.getGoodsImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte , 0 , imageByte.length);
        imageView.setImageBitmap(bitmap);

        guess.setOnClickListener(new MyListener());
        playagain.setOnClickListener(new MyListener());
        rechoice.setOnClickListener(new MyListener());
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
    private void setScale(){
        if(num<100){
            scale=100;
        }
        else if(num<1000){
            scale=1000;
        }else{
            scale=10000;
        }
    }
    private void setRemaintime(){
        if(num<=100){
            time=7;//初始化为5次
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
    class MyListener implements android.view.View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.equals(guess)){
                //猜数字按钮
                int N=Integer.parseInt(numinput.getText().toString());
                //对变量low和high 进行初始化，以及对后期进行更新
                //如果low和high为0，默认为0，第一猜就会将它们进行初始化

                if(N<num&&(low==0||N>low)){
                    low=N;
                }
                if(N>num&&(high==0||N<high)){
                    high=N;
                }
                if(low!=0&&high!=0){
                    //画图

                    ViewGroup.LayoutParams params=axis_front.getLayoutParams();
                    ratio=800.00/scale;
                    //控制长度
                    params.width=(int)((high-low)*ratio);
                    int translationX=(int)((low+high-scale)/2*ratio);
                    Log.d("width","width:"+params.width);
                    Log.d("translationX","translationX:"+translationX);
                    //右移 长度为：[(low+high)/2-scale/2]*radio ===> (low+high-scale)*ratio/2
                    //左移 长度为：[scale/2-(low+high)/2]*radio ===> (scale-low-high)*ratio/2
                    axis_front.setTranslationX(translationX);
                    axis_front.setLayoutParams(params);
                    txtLow.setText("区间左侧:"+low);
                    txtHigh.setText("区间右侧:"+high);
                    txtLow.setVisibility(View.VISIBLE);
                    txtHigh.setVisibility(View.VISIBLE);
                    axis_front.setVisibility(View.VISIBLE);


                }
                if(g.play(num,N).equals("猜对了!")){
                    String str=numinput.getText().toString()+g.play(num,N);
                    middle.setText(Html.fromHtml("<br/><font color='#FF0000'>"+str+"</font>"));
                    //猜对了就结束 只能先按重新开始
                    //rechoice.setEnabled(false);
                    guess.setEnabled(false);
                }
                else{
                    if(g.play(num,N).equals("猜小了")){
                        show2.append("\n"+numinput.getText().toString()+g.play(num,N));
                    }
                    else{//猜大了
                        show.append("\n"+numinput.getText().toString()+g.play(num,N));
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
                setRemaintime();//setRemaintime again
                guess.setEnabled(true);

                ViewGroup.LayoutParams params=axis_front.getLayoutParams();
                low=high=0;
                params.width=800;
                axis_front.setTranslationX(0);
                axis_front.setLayoutParams(params);
                axis_front.setVisibility(View.INVISIBLE);
                txtHigh.setVisibility(View.INVISIBLE);
                txtLow.setVisibility(View.INVISIBLE);
            }
            if(v.equals(rechoice)){
                //这个Activity 猜价格的活动就结束->ListViewContent.onResume()
                finish();
            }
        }
    }
}
