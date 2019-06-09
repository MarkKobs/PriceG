package com.example.guessprice;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewContent extends AppCompatActivity {

    DatabaseHelper dbhp;

    ArrayList<Goods> listContent = new ArrayList<>();
    ListView listView;
    Button btnDel;
    DragFloatActionButton fb;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);

        listView = (ListView) findViewById(R.id.dynamicListView);
        fb=(DragFloatActionButton)findViewById(R.id.fb) ;
        dbhp = new DatabaseHelper(this);

        populateView();
        adapter= new CustomAdapter(this , R.layout.custom_listview_layout , listContent);
        listView.setAdapter(adapter);

        fb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(ListViewContent.this,AddEditGoods.class);
                startActivity(intent);
            }
        });
    }
    public void flush(){
        listContent.clear();
        populateView();
        adapter.notifyDataSetChanged();
    }
    public void onCaiRow(View v) {
        //删除功能改为->猜价格功能
        //传递id
        btnDel=(Button)v;
        int goodsID = (int)btnDel.getTag();
        Toast.makeText(this , String.valueOf(goodsID) , Toast.LENGTH_SHORT).show();

        //dbhp.deleteRow(goodsID);
        //flush();
        Intent intent = new Intent(ListViewContent.this,MainActivity.class);
        intent.putExtra("goodsID",goodsID);//把goodsID这个int值传送过去了
        startActivity(intent);
    }

    private void populateView() {
        Cursor data = dbhp.getContent();
        while(data.moveToNext()) {
            int ID = data.getInt(0);
            int price = data.getInt(1) ;
            String name = data.getString(2);
            byte[] goodsImage = data.getBlob(3);
            listContent.add(new Goods(ID , price , name , goodsImage));

        }
    }

    public class CustomAdapter extends BaseAdapter {

        private Context context;
        private int layout;
        ArrayList<Goods> content;

        public CustomAdapter(Context context , int layout , ArrayList<Goods> content) {
            this.context = context;
            this.layout = layout;
            this.content = content;
        }


        @Override
        public int getCount() {
            return content.size();
        }

        @Override
        public Object getItem(int position) {
            return content.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        private class ViewHolder {

            TextView txtGoodsName;
            ImageView imageGoods;
            Button delButton;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;

            if(row == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layout, null);
                holder = new ViewHolder();
                holder.txtGoodsName = (TextView) row.findViewById(R.id.goodsName);
                //holder.txtGoodsPrice = (TextView) row.findViewById(R.id.goodsPrice);
                holder.delButton = (Button) row.findViewById(R.id.btnCaiRow);
                holder.imageGoods = (ImageView) row.findViewById(R.id.goodsImage);
                row.setTag(holder);


            } else {

                holder = (ViewHolder) row.getTag();

            }

            final Goods dc = content.get(position);
            holder.txtGoodsName.setText(dc.getGoodsName());
            //holder.txtGoodsPrice.setText(String.valueOf(dc.getGoodsPrice()));
            holder.delButton.setTag(dc.getGoodsID());//setText 改为 setTag，存在Tag中，注意是int格式
            byte[] goodsImage = dc.getGoodsImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(goodsImage , 0 , goodsImage.length);
            holder.imageGoods.setImageBitmap(bitmap);
            return row;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        flush();
    }
}