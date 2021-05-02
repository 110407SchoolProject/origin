package com.example.a110407_app.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    String TAG =SQLiteOpenHelper.class.getSimpleName();
    String TableName;

    public SQLiteDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version,String TableName) {
        super(context, name, factory, version);
        this.TableName = TableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        String SQLTable = "CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Title TEXT, " +
                "Content TEXT" +
                ");";
        db.execSQL(SQLTable);
         */
        /*
        String RegisterTable = "CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userTrueName TEXT, " +
                "userName TEXT, " +
                "Password TEXT" +
                ");";
        db.execSQL(RegisterTable);

         */

        String CategoryTable = "CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Category TEXT " +
                ");";
        db.execSQL(CategoryTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //final String SQL = "DROP TABLE " + TableName;
        //db.execSQL(SQL);

        //檢查目前資料庫的版本，更新資料庫(用版本號來決定是否要更新)(只有在要在舊表新增欄位或是新增一個表的時候需要用到)
        if (newVersion > oldVersion){
            //System.out.println("有吃到");
            db.beginTransaction();
            boolean success = false;
            switch (oldVersion){
                case 9:

                    /*新增Profile資料表
                    String RegisterTable = "CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "userTrueName TEXT, " +
                            "userName TEXT, " +
                            "Password TEXT" +
                            ");";
                    db.execSQL(RegisterTable);
                     */
                    //新增Date欄位在Dairy 表裡面
                    // db.execSQL("ALTER TABLE MyDairy ADD COLUMN Date TEXT");
                    //新增Category欄位在Dairy表裡面
                    //db.execSQL("ALTER TABLE MyDairy ADD COLUMN Category TEXT");
                    //新增Score欄位在Dairy表裡面
                    //db.execSQL("ALTER TABLE MyDairy ADD COLUMN Score FLOAT");
                    //新增Category資料表

                    db.execSQL("CREATE TABLE IF NOT EXISTS CategoryTable ( " +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "Category TEXT " +
                            ")");
                    success = true;
                    break;
            }
            if (success){
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        }
        else {
            onCreate(db);
        }



    }


    //新增分類項
    public void addCategory(String category){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Category",category);
        db.insert(TableName,null,values);
    }

    //刪除分類項
    public void deleteCategory(String category){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"Category = " + category,null);
    }

    public void checkTable(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0)
                getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Title TEXT, " +
                        "Content TEXT" +
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

    //新增資料
    public void addData(String title, String content, String date,String category,String score) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        values.put("Content", content);
        values.put("Date",date);
        values.put("Category",category);
        values.put("Score",score);
        db.insert(TableName, null, values);
    }

    public void addProfileData(String usertruename, String username, String password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userTrueName",usertruename);
        values.put("userName",username);
        values.put("Password",password);
        db.insert(TableName,null,values);
    }

    //顯示所有資料
    public ArrayList<HashMap<String, String>> showAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName, null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String title = c.getString(1);
            String content = c.getString(2);
            String date = c.getString(3);
            String category = c.getString(4);
            String score = c.getString(5);

            hashMap.put("id", id);
            hashMap.put("Title", title);
            hashMap.put("Content", content);
            hashMap.put("Date",date);
            hashMap.put("Category",category);
            hashMap.put("Score",score);
            arrayList.add(hashMap);
        }
        return arrayList;

    }
    //以id搜尋特定資料
    public ArrayList<HashMap<String,String>> searchById(String getId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName + " WHERE _id =" + "'" + getId + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String title = c.getString(1);
            String content = c.getString(2);
            String date = c.getString(3);
            String category = c.getString(4);
            String score = c.getString(5);


            hashMap.put("id", id);
            hashMap.put("Title", title);
            hashMap.put("Content", content);
            hashMap.put("Date",date);
            hashMap.put("Category",category);
            hashMap.put("Score",score);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    //以分類篩選資料
    public ArrayList<HashMap<String,String>> searchByCategory(String getCategory){
        SQLiteDatabase db = getReadableDatabase();
        //Cursor c = db.rawQuery(" SELECT * FROM " + TableName
        //        + " WHERE Category =" + "'" + getCategory + "'", null);
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName + " WHERE Category =" + "'" + getCategory + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String category = c.getString(0);
            hashMap.put("Category", category);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    //將所有的category抓出來
    public ArrayList<HashMap<String,String>> selectAllCategory(){
        SQLiteDatabase db = getReadableDatabase();
        //Cursor c = db.rawQuery(" SELECT * FROM " + TableName
        //        + " WHERE Category =" + "'" + getCategory + "'", null);
        Cursor c = db.rawQuery(" SELECT Category FROM " + TableName , null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String category = c.getString(0);
            hashMap.put("Category", category);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    public ArrayList<HashMap<String,String>> selectTheLastCategory(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT Category FROM " + TableName , null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        c.moveToLast();
        HashMap<String, String> hashMap = new HashMap<>();
        String category = c.getString(0);
        hashMap.put("Category", category);
        arrayList.add(hashMap);
        return arrayList;
    }






    //以興趣篩選資料
    public ArrayList<HashMap<String, String>> searchByTitle(String getTitle) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE Title =" + "'" + getTitle + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String id = c.getString(0);
            String title = c.getString(1);
            String content = c.getString(2);
            String date = c.getString(3);
            String category = c.getString(4);
            String score = c.getString(5);

            hashMap.put("id", id);
            hashMap.put("Title", title);
            hashMap.put("Content", content);
            hashMap.put("Date",date);
            hashMap.put("Category",category);
            hashMap.put("Score",score);
            arrayList.add(hashMap);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    //修改資料(簡單)
    public void modifyEZ(String id, String title, String content,String date, String category,String score) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("Date",date);
        values.put("Category",category);
        values.put("Score",score);
        System.out.println(id+title+content);
        db.update(TableName, values, "_id = " + id, null);
    }

    //刪除全部資料
    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TableName);
    }
//    public void dropTable(){
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DROP TABLE "+TableName);
//    }

    //以id刪除資料(簡單)
    public void deleteByIdEZ(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"_id = " + id,null);
    }

    public void deleteByTitle(String Title){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"Title = " + Title,null);
    }









}
