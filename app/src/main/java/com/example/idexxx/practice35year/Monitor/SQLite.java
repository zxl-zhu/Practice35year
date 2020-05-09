package com.example.idexxx.practice35year.Monitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by idexxx on 2020/4/19.
 */

public class SQLite extends SQLiteOpenHelper {

    final String CREATE_BEIJING="create table BeiJing(pm2_5 integer,co2 integer,LightIntensity integer,humidity integer,temperature integer)";
    final String CREATE_CHONGQING="create table ChongQing(pm2_5 integer,co2 integer,LightIntensity integer,humidity integer,temperature integer)";
    final String CREATE_SHANGHAI="create table ShangHai(pm2_5 integer,co2 integer,LightIntensity integer,humidity integer,temperature integer)";
    final String CREATE_XIONGAN="create table XiongAn(pm2_5 integer,co2 integer,LightIntensity integer,humidity integer,temperature integer)";
    final String CREATE_SHENZHEN="create table ShenZhen(pm2_5 integer,co2 integer,LightIntensity integer,humidity integer,temperature integer)";
    final String TABLE_BEIJING="BeiJing";
    final String TABLE_CHONGQING="ChongQing";
    final String TABLE_SHANGHAI="ShangHai";
    final String TABLE_XIONGAN="XiongAn";
    final String TABLE_SHENZHEN="ShenZhen";
    final String PM2_5="pm2_5";
    final String CO2="co2";
    final String LIGHTINTENSITY="LightIntensity";
    final String HUMIDITY="humidity";
    final String TEMPERATURE="temperature";

    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super( context, name, null, version );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( CREATE_BEIJING );
        sqLiteDatabase.execSQL( CREATE_CHONGQING );
        sqLiteDatabase.execSQL( CREATE_SHANGHAI );
        sqLiteDatabase.execSQL( CREATE_XIONGAN );
        sqLiteDatabase.execSQL( CREATE_SHENZHEN );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
