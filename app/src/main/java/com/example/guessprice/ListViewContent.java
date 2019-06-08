package com.example.guessprice;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);

        listView = (ListView) findViewById(R.id.dynamicListView);
        dbhp = new DatabaseHelper(this);

        populateView();
        adapter= new CustomAdapter(this , R.layout.custom_listview_layout , listContent);
        listView.setAdapter(adapter);


    }

    public void onDelRow(View v) {
        btnDel = (Button) findViewById(R.id.btnDelRow);
        int IDHeroList = Integer.parseInt(btnDel.getText().toString());
        dbhp.deleteRow(IDHeroList);

        Toast.makeText(this , "Delete this row." , Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(ListViewContent.this , MainActivity.class);
        //startActivity(intent);
        listContent.clear();
        populateView();
        adapter.notifyDataSetChanged();
    }

    private void populateView() {
        Cursor data = dbhp.getContent();
        while(data.moveToNext()) {

            int ID = data.getInt(0);
            int price = data.getInt(1) ;
            String name = data.getString(2);
            byte[] HeroImage = data.getBlob(3);
            listContent.add(new Goods(ID , price , name , HeroImage));


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

            TextView txtGoodsName , txtGoodsPrice;
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
                holder.txtGoodsPrice = (TextView) row.findViewById(R.id.goodsPrice);
                holder.delButton = (Button) row.findViewById(R.id.btnDelRow);
                holder.imageGoods = (ImageView) row.findViewById(R.id.goodsImage);
                row.setTag(holder);


            } else {

                holder = (ViewHolder) row.getTag();

            }

            final Goods dc = content.get(position);
            holder.txtGoodsName.setText("Name: " + dc.getGoodsName());
            holder.txtGoodsPrice.setText("Price: " + dc.getGoodsPrice());
            holder.delButton.setText(String.valueOf(dc.getGoodsID()));
            byte[] heroImage = dc.getGoodsImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(heroImage , 0 , heroImage.length);
            holder.imageGoods.setImageBitmap(bitmap);

            return row;
        }
    }
}