package com.example.guessprice;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class ListViewContent extends AppCompatActivity {
    private DatabaseHelper dbhp;
    private ArrayList<Goods> listContent = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        listView=(ListView)findViewById(R.id.dynamicListView);
        dbhp=new DatabaseHelper(this);
        populateView();
    }
    private void populateView() {
        Cursor data = dbhp.getContent();
        while(data.moveToNext()) {

            int goodsID = data.getInt(0);
            int goodsPrice = data.getInt(1);
            String goodsName = data.getString(2);
            byte[] Image = data.getBlob(3);
            listContent.add(new Goods(goodsID , goodsPrice , goodsName , Image));
        }
    }
}
