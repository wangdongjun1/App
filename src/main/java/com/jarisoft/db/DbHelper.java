package com.jarisoft.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 新建一个数据库的类 db (database) 类要继承squliteOpenhelper
 * Created by shanwj on 2018/7/18.
 */

public class DbHelper extends SQLiteOpenHelper{

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        boolean b = tableIsExist(sqLiteDatabase,"user_num");
        if(!b){
            Log.e("DbCreate","create table");
            sqLiteDatabase.execSQL("create table user_num(id integer primary key autoincrement,usernum varchar(200));");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private boolean tableIsExist(SQLiteDatabase db,String tableName){
        boolean result = false;
        if (tableName==null){
            return false;
        }
        Cursor cursor = null;
        String sql = "select count(*) as c from Sqlite_master where type = 'table' and name = '"+tableName+"' ";
        cursor = db.rawQuery(sql,null);
        if(cursor.moveToNext()){
            int count = cursor.getInt(0);
            if(count>0){
                result = true;
            }
        }
        return result;
    }
}
