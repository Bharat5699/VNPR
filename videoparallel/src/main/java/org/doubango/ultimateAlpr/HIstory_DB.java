package org.doubango.ultimateAlpr;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class HIstory_DB extends SQLiteOpenHelper {
    public static  final String Database_Name="History.db";
    public static  final String Table_Name="Vehicle_Status";
    public static  final String col_1="Registration_No";
    public static  final String col_2="Status";
  ;

    public HIstory_DB( Context context) {
        super(context,Database_Name , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+Table_Name+" (Registration_No text primary key,Status text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public  boolean insertData(String no,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1, no);
        contentValues.put(col_2, status);
        long result = db.insert(Table_Name, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public ArrayList getAllData(){
ArrayList arrayList=new ArrayList();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+Table_Name,null);
        res.moveToFirst();
        do{
            arrayList.add(res.getString(res.getColumnIndex(col_1)));
        }while (res.moveToNext());
        return arrayList;
    }
    public Cursor verify(String no){
        SQLiteDatabase db=this.getWritableDatabase();
        String Query = "select * from Vehicle_Status where id='"+ no +"'";
        Cursor res=db.rawQuery(Query, null);
        return res;
    }
    public boolean update(String s, String s1) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE "+Table_Name+" SET Staus = "+"'"+s1+"' "+ "WHERE Registration_No= "+"'"+s+"'");
        return true;
    }

    public ArrayList getAllInActiveData() {
        ArrayList arrayList=new ArrayList();
        SQLiteDatabase db=this.getWritableDatabase();
        String Query = "select * from Vehicle_Status where Status='INACTIVE'";
        Cursor res=db.rawQuery(Query, null);
        res.moveToFirst();
        do{
            arrayList.add(res.getString(res.getColumnIndex(col_1)));
        }while (res.moveToNext());
        return arrayList;
    }

    public ArrayList getAllActiveData() {
        ArrayList arrayList=new ArrayList();
        SQLiteDatabase db=this.getWritableDatabase();
        String Query = "select * from Vehicle_Status where Status='ACTIVE'";
        Cursor res=db.rawQuery(Query, null);
        res.moveToFirst();
        do{
            arrayList.add(res.getString(res.getColumnIndex(col_1)));
        }while (res.moveToNext());
        return arrayList;
    }
}
