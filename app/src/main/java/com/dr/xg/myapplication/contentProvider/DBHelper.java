package com.dr.xg.myapplication.contentProvider;

/**
 * @author 黄冬榕
 * @date 2018/1/22
 * @description
 * @remark
 */


        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static String name = "mydb.db"; // 数据库的名字
    private static int version = 1; // 数据库的版本

    public DBHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 只能支持基本数据类型:varchar int long float boolean text blob clob
        // 建表语句执行
        String sql = "create table person(id integer primary key autoincrement,name varchar(64),address varchar(64))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql = "alter table person add sex varchar(8)";
        db.execSQL(sql);
    }

}
