package com.example.guessprice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pricelist.db";
    private static final String TB_NAME = "pricelist_table";
    private static final String COL2 = "goodsPrice";
    private static final String COL3 = "goodsName";
    private static final String COL4 = "goodsImage";

    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,3);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TB_NAME + " (goodsID INTEGER PRIMARY KEY AUTOINCREMENT , goodsPrice TEXT , goodsName TEXT , goodsImage blob)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
    public boolean addData(int goodsPrice , String goodsName , byte[] goodsImage) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL2 , goodsPrice);
        cv.put(COL3 , goodsName);
        cv.put(COL4 , goodsImage);

        long resultInsert = db.insert(TB_NAME , null , cv);
        if(resultInsert == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getContent() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TB_NAME , null);
        return data;
    }
    public void deleteRow(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TB_NAME + " WHERE goodsID = " + ID;
        db.execSQL(query);
    }
}
