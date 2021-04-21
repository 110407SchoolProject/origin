package com.example.a110407_app.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.a110407_app.ui.gallery.GalleryFragment;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    String TAG =SQLiteOpenHelper.class.getSimpleName();
    String TableName;
    private static final String DATABASE_NAME = "0421TestV2";
    private static final String profileTable = "profileTable";
    private static final String profileTable2 = "profileTable2";

    public SQLiteDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
        //this.TableName = TableName;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String table1 = "CREATE TABLE IF NOT EXISTS " + profileTable + "( " +
                "Uid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Cid INTEGER," +
                "Title TEXT, " +
                "Content TEXT," +
                "Score INT," +
                "Date TEXT"+
                ");";
        db.execSQL(table1);


        String table2 = "CREATE TABLE IF NOT EXISTS " + profileTable2 + "( " +
                "Uid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserTrueName TEXT, " +
                "UserName TEXT" +
                ");";
        db.execSQL(table2);

        /*
        String table3 = "CREATE TABLE IF NOT EXISTS " + profileTable3 + "( " +
                "Uid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserTrueName TEXT, " +
                "UserName TEXT, " +
                "Password TEXT " +
                ");";
        db.execSQL(table3);

         */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if(newVersion > oldVersion){
            //db.execSQL("DROP TABLE IF EXISTS " + profileTable2);



        //}


    }

    public void checkTable(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0)
                getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                        "Uid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Cid INTEGER ," +
                        "Title TEXT, " +
                        "Content TEXT" +
                        "Score INT," +
                        "Date TEXT,"+
                        ");");
            cursor.close();
        }

    }



    //取得有多少資料表,並以陣列回傳
    public ArrayList<String> getTables(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master", null);
        ArrayList<String> tables = new ArrayList<>();
        while (cursor.moveToNext()){
            String getTab = new String (cursor.getBlob(0));
            if (getTab.contains("android_metadata")){}
            else if (getTab.contains("sqlite_sequence")){}
            else tables.add(getTab.substring(0,getTab.length()-1));

        }
        return tables;
    }

    //新增日記資料
    public void addData(String cid, String title, String content,String score, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Cid",cid);
        values.put("Title", title);
        values.put("Content", content);
        values.put("Score",score);
        values.put("Date",date);
        db.insert(profileTable, null, values);
        Log.e("a","幹你娘有執行到阿");
    }

    //新增個人資料
    public void addProfileData(String truename, String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserTrueName",truename);
        values.put("UserName", username);
        values.put("Password", password);
        db.insert(profileTable2, null, values);
        Log.e("a","幹你娘");
    }

    //顯示所有資料
    public ArrayList<HashMap<String, String>> showAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName, null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String uid = c.getString(0);
            String title = c.getString(1);
            String content = c.getString(2);
            String score = c.getString(3);
            String date = c.getString(4);

            hashMap.put("uid", uid);
            hashMap.put("Title", title);
            hashMap.put("Content", content);
            hashMap.put("Score",score);
            hashMap.put("Date",date);
            arrayList.add(hashMap);
        }
        return arrayList;

    }
    //以id搜尋特定資料

    public ArrayList<HashMap<String,String>> searchById(String getId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE _id =" + "'" + getId + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String title = c.getString(1);
            String content = c.getString(2);

            hashMap.put("id", id);
            hashMap.put("Title", title);
            hashMap.put("Content", content);
            arrayList.add(hashMap);
        }
        return arrayList;
    }


    //以興趣篩選資料
    /*
    public ArrayList<HashMap<String, String>> searchByHobby(String getHobby) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE Hobby =" + "'" + getHobby + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String id = c.getString(0);
            String title = c.getString(1);
            String content = c.getString(2);

            hashMap.put("id", id);
            hashMap.put("Title", title);
            hashMap.put("Content", content);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

     */

    //修改資料(簡單)
    /*
    public void modifyEZ(String id, String title, String content) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        db.update(TableName, values, "_id = " + id, null);
    }

     */

    //刪除全部資料
    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM"+TableName);
    }

    //以id刪除資料(簡單)
    public void deleteByIdEZ(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"_id = " + id,null);
    }


}
