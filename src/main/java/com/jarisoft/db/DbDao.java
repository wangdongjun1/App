package com.jarisoft.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jarisoft.entity.UserNum;

import java.util.ArrayList;
import java.util.List;

/**
 *  操作数据库的工具类
 *  单例模式：在整个应用程序中不管什么地方（类）获得的都是同一个对象实例
 * Created by shanwj on 2018/7/18.
 */

public class DbDao {

    private DbHelper dbHelper;
    private String table_name = "user_num";
    private DbDao(Context context){
        dbHelper = new DbHelper(context,"user_num.db",null,1);
    }
    //创建单列
    //BlackDao类是私有的  那么别的类就不能够调用  那么就要提供一个public static（公共的  共享的）的方法
    //方法名为getInstance 参数为上下文   返回值类型为DbDao
    //要加上一个synchronized（同步的）
    //如果同时有好多线程 同时去调用getInstance()方法  就可能会出现一些创建（new）多个DbDao的现象  所以要加上synchronized
    private static DbDao instance;
    public static synchronized DbDao getInstance(Context context){
        if(instance==null){
            instance = new DbDao(context);
        }
        return instance;
    }

    /**
     * 新增用户工号
     * @param num
     */
    public void add(String num){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usernum",num);
        db.insert(table_name,null,values);
    }

    /**
     * 删除用户工号
     * @param num
     */
    public void deleteNum(String num){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(table_name,"usernum = ?",new String[]{num});
    }

    /**
     * 获取所有数据
     * @return
     */
    public List<UserNum> getAll(){
        List<UserNum> list = new ArrayList<UserNum>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user_num order by id desc",null);
        while (cursor.moveToNext()){
            String numStr = cursor.getString(cursor.getColumnIndex("usernum"));
            UserNum userNum = new UserNum(numStr);
            list.add(userNum);
        }
        cursor.close();
        return list;
    }

    public boolean isExitByNum(String num){
        boolean b = false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from user_num where usernum = '"+num+"'" ,null);
        String sql = "select * from user_num where usernum = ?";
        Cursor cursor = db.rawQuery(sql ,new String[]{num});
        while (cursor.moveToNext()){
            String numStr = cursor.getString(cursor.getColumnIndex("usernum"));
            if(numStr.equals(num)){
                b = true;
            }
        }
        return b;
    }
}
