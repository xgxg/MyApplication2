package com.dr.xg.myapplication.contentProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * @author 黄冬榕
 * @date 2018/1/23
 * @description
 * @remark
 */

public class MyTest extends AndroidTestCase {

        public MyTest() {
            // TODO Auto-generated constructor stub

        }

    public void calltest() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = Uri
                .parse("content://com.dr.xg.myapplication.contentProvider.PersonContentProvider/person");
        Bundle bundle = contentResolver.call(uri, "method", null, null);
        String returnCall = bundle.getString("returnCall");
        Log.i("main", "-------------->" + returnCall);
    }

    //测试方法：向数据库中添加记录。如果之前没有数据库，则会自动创建
    public void insert() {
        // 使用内容解析者ContentResolver访问内容提供者ContentProvider
        ContentResolver contentResolver = getContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", "生命贰号");
        values.put("address", "湖北");
        // content://authorities/person
        // http://
        Uri uri = Uri
                .parse("content://com.dr.xg.myapplication.contentProvider.PersonContentProvider/person");
        contentResolver.insert(uri, values);
    }

    //测试方法：删除单条记录。如果要删除所有记录：content://com.example.contentprovidertest01.PersonContentProvider/person
    public void delete() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = Uri
                .parse("content://com.dr.xg.myapplication.contentProvider.PersonContentProvider/person/2");//删除id为1的记录
        contentResolver.delete(uri, null, null);
    }

    //测试方法：根据条件删除记录。
    public void deletes() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = Uri
                .parse("content://com.dr.xg.myapplication.contentProvider.PersonContentProvider/person");
        String where = "address=?";
        String[] where_args = { "HK" };
        contentResolver.delete(uri, where, where_args);  //第二个参数表示查询的条件"address=?"，第三个参数表示占位符中的具体内容
    }

    //方法：根据id修改记录。注：很少有批量修改的情况。
    public void update() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = Uri
                .parse("content://com.dr.xg.myapplication.contentProvider.PersonContentProvider/person/2");
        ContentValues values = new ContentValues();
        values.put("name", "李四");
        values.put("address", "上海");
        contentResolver.update(uri, values, null, null);
    }

    //方法：根据条件来修改记录。
    public void updates() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = Uri
                .parse("content://com.dr.xg.myapplication.PersonContentProvider/person/student");
        ContentValues values = new ContentValues();
        values.put("name", "王五");
        values.put("address", "深圳");
        String where = "address=?";
        String[] where_args = { "beijing" };
        contentResolver.update(uri, values, where, where_args);
    }

    //测试方法：查询所有记录。如果要查询单条记录：content://com.example.contentprovidertest01.PersonContentProvider/person/1
    public  void query() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = Uri
                .parse("content://com.dr.xg.myapplication.contentProvider.PersonContentProvider/person");
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            Log.i("MyTest",
                    "--->>"
                            + cursor.getString(cursor.getColumnIndex("name")));
        }
    }

    //测试方法：根据条件查询所有记录。
    public void querys() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = Uri
                .parse("content://com.dr.xg.myapplication.contentProvider.PersonContentProvider/person");
        String where = "address=?";
        String[] where_args = { "深圳" };
        Cursor cursor = contentResolver.query(uri, null, where, where_args,
                null);
        while (cursor.moveToNext()) {
            Log.i("main",
                    "-------------->"
                            + cursor.getString(cursor.getColumnIndex("name")));
        }
    }

    }
