package com.example.guessprice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AddEditGoods extends AppCompatActivity {
    final int GALLERY_REQUEST_CODE = 1889;
    private DatabaseHelper dbhp;
    private ImageView imagePreview;
    private Button btnAddData,btnPickImage;//两个按钮，一个是添加，一个是选择图片
    private EditText txtgoodsName, txtgoodsPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_goods);
        dbhp = new DatabaseHelper(this);

        btnAddData= (Button) findViewById(R.id.btnAddData);
        btnPickImage = (Button) findViewById(R.id.btnPickImage);
        imagePreview = (ImageView) findViewById(R.id.imageView);
        txtgoodsName = (EditText)findViewById(R.id.goodsName);
        txtgoodsPrice = (EditText) findViewById(R.id.goodsPrice);
        //saveToSDCard();
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(AddEditGoods.this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , GALLERY_REQUEST_CODE);
            }
        });
        btnAddData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int getGoodsPrice=Integer.parseInt(txtgoodsPrice.getText().toString());
                String getGoodsName=txtgoodsName.getText().toString();
                byte[] getImage=imageViewToByte(imagePreview);

                if(txtgoodsName.length()!=0&&txtgoodsPrice.length()!=0){
                    AddData(getGoodsName,getGoodsPrice,getImage);
                    txtgoodsPrice.setText("");
                    txtgoodsName.setText("");
                }
                else{
                    Toast.makeText(AddEditGoods.this,"Error:Empty",Toast.LENGTH_LONG).show();
                }
            }

            private byte[] imageViewToByte(ImageView image) {
                Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG , 100 , stream);
                byte[] byteArray = stream.toByteArray();
                return byteArray;
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {

        if(requestCode == GALLERY_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , GALLERY_REQUEST_CODE);
            } else {
                Toast.makeText(AddEditGoods.this , "You don't have the permission to access file." , Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode , permissions , grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent data) {
        if(data==null)return;
        try {
            Uri uri = data.getData();
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imagePreview.setImageBitmap(bitmap);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void AddData(String goodsName,int goodsPrice,byte[] goodsImage){
        //price name image
        boolean insertData=dbhp.addData(goodsPrice,goodsName,goodsImage);
        if(insertData==true){
            Toast.makeText(AddEditGoods.this,"插入成功!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(AddEditGoods.this,"插入失败",Toast.LENGTH_LONG).show();
        }
        finish();//增加完了，这个activity的任务就结束了

    }

    public void saveToSDCard(){
        //创建File
        try{
            File file = new File("/mnt/sdcard/Pictures/glass.jpg");
            File file2 = new File("/mnt/sdcard/Pictures/rice.jpg");
            File file3 = new File("/mnt/sdcard/Pictures/huawei.jpg");
            //文件输出流
            OutputStream os = new FileOutputStream(file);
            OutputStream os2 = new FileOutputStream(file2);
            OutputStream os3 = new FileOutputStream(file3);
            //把drawable的picture图片转换为位图
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.glass);
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.rice);
            Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.huawei);
            //把picture位图复制一份到sdcard的file位置
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,os);
            bitmap2.compress(Bitmap.CompressFormat.JPEG,50,os2);
            bitmap3.compress(Bitmap.CompressFormat.JPEG,50,os3);
            os.flush();
            os.close();
            os2.flush();
            os2.close();
            os3.flush();
            os3.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
