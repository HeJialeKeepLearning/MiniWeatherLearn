package com.example.hejiale.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hejiale.bean.City;

import java.util.ArrayList;
import java.util.List;

public class CityDB {
    public static final String CITY_DB_NAME="city.db";
    private static final String CITY_TABLE_NAME="city";
    private SQLiteDatabase db;

    public CityDB(Context context,String path){
        db=SQLiteDatabase.openOrCreateDatabase(path,null);
    }

    public List<City> getAllCity(){
        List<City> list=new ArrayList<City>();//List<City>是一个存放任意个数个City变量的数组
        Cursor cursor=db.rawQuery("SELECT * from "+CITY_TABLE_NAME,null);
        while (cursor.moveToNext()){
            String province= cursor.getString(cursor.getColumnIndex("province"));
            String city=cursor.getString(cursor.getColumnIndex("city"));
            String number= cursor.getString(cursor.getColumnIndex("number"));
            String allPY= cursor.getString(cursor.getColumnIndex("allpy"));
            String allfirPY= cursor.getString(cursor.getColumnIndex("allfirstpy"));
            String firPY= cursor.getString(cursor.getColumnIndex("firstpy"));
            City item=new City(province,city,number,allPY,allfirPY,firPY);
            list.add(item);
        }
        return list;
    }
}
