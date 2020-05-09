package com.example.idexxx.practice35year.Monitor;


import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.idexxx.practice35year.Application.MyApplication;
import com.example.idexxx.practice35year.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonitoFragment extends Fragment {


    private TextView city_name;
    private TextView text_date;
    private TextView uptodate;
    private PieChart pieChart;
    Timer timer;
    RequestQueue mQueue=new MyApplication().getRequestQueue();
    Handler handler;
    private SQLite sqLite;
    List<PieEntry> pieEntries;
    String[] lift_index=new String[]{"PM2.5","二氧化碳","光照强度","湿度","温度"};
    int[] max=new int[5];
    int[] min=new int[5];
    int[] avg=new int[5];
    String tabName;
    private ListView listView;

    public MonitoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_monito, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        iniView();

        //饼图单击事件
        pieChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {

                if (entry.getY()==pieEntries.get( 0 ).getValue()){

                    tabName=sqLite.TABLE_BEIJING;
                    city_name.setText( "北京" );
                    listview();

                }else if (entry.getY()==pieEntries.get( 1 ).getValue()){

                    tabName=sqLite.TABLE_CHONGQING;
                    city_name.setText( "重庆" );
                    listview();

                }else if (entry.getY()==pieEntries.get( 2 ).getValue()){

                    tabName=sqLite.TABLE_SHANGHAI;
                    city_name.setText( "上海" );
                    listview();

                }else if (entry.getY()==pieEntries.get( 3 ).getValue()){

                    tabName=sqLite.TABLE_SHENZHEN;
                    city_name.setText( "深圳" );
                    listview();

                }else if (entry.getY()==pieEntries.get( 4 ).getValue()){

                    tabName=sqLite.TABLE_XIONGAN;
                    city_name.setText( "雄安" );
                    listview();

                }

            }

            @Override
            public void onNothingSelected() {

            }
        } );
    }


    //初始化
    private void iniView() {

        city_name = getView().findViewById( R.id.City_name );
        text_date = getView().findViewById( R.id.monito_date );
        uptodate = getView().findViewById( R.id.monito_to_updata );
        pieChart = getView().findViewById( R.id.Monitor_PieChart );
        listView = getView().findViewById( R.id.Monitor_listview );
        sqLite = new SQLite( getActivity(),"Monito",null,1);
        tabName=sqLite.TABLE_BEIJING;
        city_name.setText( "北京" );

        //获取系统时间
        SimpleDateFormat simpledate=new SimpleDateFormat( "yyyy年MM月dd日 HH:mm EEEE" );
        text_date.setText( simpledate.format( new Date(  ) ) );
        //获取刷新时间


    }

    @Override
    public void onResume() {
        super.onResume();
        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage( msg );

                //更新listView
                listview();

                //设置饼图数据
                pieEntries = new ArrayList<>();
                pieEntries.add( new PieEntry(proportion( sqLite.TABLE_BEIJING ),"北京" ) );
                pieEntries.add( new PieEntry( proportion( sqLite.TABLE_CHONGQING ) ,"重庆"));
                pieEntries.add( new PieEntry( proportion( sqLite.TABLE_SHANGHAI ) ,"上海") );
                pieEntries.add( new PieEntry( proportion( sqLite.TABLE_SHENZHEN ),"深圳") );
                pieEntries.add( new PieEntry( proportion( sqLite.TABLE_XIONGAN ) ,"雄安") );

                PieDataSet pieDataSet=new PieDataSet( pieEntries,"" );
                List<Integer> colors=new ArrayList<>(  );
                colors.add( Color.parseColor( "#DB7093" ) );
                colors.add( Color.parseColor( "#8A2BE2" ) );
                colors.add( Color.parseColor( "#4169E1" ) );
                colors.add( Color.parseColor( "#008B8B" ) );
                colors.add( Color.parseColor( "#F0E68C" ) );
                pieDataSet.setColors( colors );

                PieData pieData=new PieData( pieDataSet );
                pieChart.setData( pieData );
                pieChart.setDrawHoleEnabled( false );
                pieChart.invalidate();
            }
        };


        timer = new Timer();
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                try {
                    sqLite.getWritableDatabase().insert( sqLite.TABLE_BEIJING,null,insert( BeiJing() ) );
                    sqLite.getWritableDatabase().insert( sqLite.TABLE_CHONGQING,null,insert( ChongQing() ) );
                    sqLite.getWritableDatabase().insert( sqLite.TABLE_SHANGHAI,null,insert( ShangHai() ) );
                    sqLite.getWritableDatabase().insert( sqLite.TABLE_XIONGAN,null,insert( XiongAn() ) );
                    sqLite.getWritableDatabase().insert( sqLite.TABLE_SHENZHEN,null,insert( ShenZhen() ) );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage( 0x01 );
                //
            }
        }, 0, 5000 );
    }


    //适配ListView
    private void listview(){
        //查询最大值
        Cursor  max_cursor=sqLite.getReadableDatabase().query( tabName,new String[]{"max(pm2_5)","max(co2)","max(LightIntensity)","max(humidity)","max(temperature)"},
                null,null,null,null,null );
        StringBuffer str=new StringBuffer(  );
        while (max_cursor.moveToNext()){

            max[0]=max_cursor.getInt( max_cursor.getColumnIndex( "max(pm2_5)" ) );
            max[1]=max_cursor.getInt( max_cursor.getColumnIndex( "max(co2)" ) );
            max[2]=max_cursor.getInt( max_cursor.getColumnIndex( "max(LightIntensity)" ) );
            max[3]=max_cursor.getInt( max_cursor.getColumnIndex( "max(humidity)" ) );
            max[4]=max_cursor.getInt( max_cursor.getColumnIndex( "max(temperature)" ) );

        }


        //查询最小值
        Cursor min_cursor=sqLite.getReadableDatabase().query( tabName,new String[]{"min(pm2_5)","min(co2)","min(LightIntensity)","min(humidity)","min(temperature)"},
                null,null,null,null,null );
        while (min_cursor.moveToNext()){
            min[0]=min_cursor.getInt( min_cursor.getColumnIndex( "min(pm2_5)" ) );
            min[1]=min_cursor.getInt( min_cursor.getColumnIndex( "min(co2)" ) );
            min[2]=min_cursor.getInt( min_cursor.getColumnIndex( "min(LightIntensity)" ) );
            min[3]=min_cursor.getInt( min_cursor.getColumnIndex( "min(humidity)" ) );
            min[4]=min_cursor.getInt( min_cursor.getColumnIndex( "min(temperature)" ) );
        }


        //查询平均值
        Cursor avg_cursor=sqLite.getReadableDatabase().query( tabName,new String[]{"avg(pm2_5)","avg(co2)","avg(LightIntensity)","avg(humidity)","avg(temperature)"},
                null,null,null,null,null );
        while (avg_cursor.moveToNext()){
            avg[0]=avg_cursor.getInt( avg_cursor.getColumnIndex( "avg(pm2_5)" ) );
            avg[1]=avg_cursor.getInt( avg_cursor.getColumnIndex( "avg(co2)" ) );
            avg[2]=avg_cursor.getInt( avg_cursor.getColumnIndex( "avg(LightIntensity)" ) );
            avg[3]=avg_cursor.getInt( avg_cursor.getColumnIndex( "avg(humidity)" ) );
            avg[4]=avg_cursor.getInt( avg_cursor.getColumnIndex( "avg(temperature)" ) );
        }

        max_cursor.close();
        min_cursor.close();
        avg_cursor.close();

        //ListView添加适配器
        List<Map<String,Object>> data=new ArrayList<>(  );
        data.clear();
        for (int i=0;i<5;i++){
            Map<String,Object> map=new HashMap(  );
            map.put( "lift_index",lift_index[i] );
            map.put( "max",max[i] );
            map.put( "min",min[i] );
            map.put( "avg",avg[i] );
            data.add( map );
        }
        SimpleAdapter adapter=new SimpleAdapter( getActivity(),data,R.layout.monito_item,
                new String[]{ "lift_index", "max", "min", "avg"},
                new int[]{ R.id.monito_list_name, R.id.monito_list_max, R.id.monito_list_min, R.id.monito_list_avg });
        listView.setAdapter( adapter );

    }

    //获取每个城市的PM2.5的占比
    private int proportion(String name){

        int sum = 0;

        Cursor cursor_count=sqLite.getReadableDatabase().query(name,new String[]{"pm2_5"},
                null,null,null,null,null );

        Cursor cursor_sum=sqLite.getReadableDatabase().query(name,new String[]{"sum(pm2_5)"},
                null,null,null,null,null );

        int count=cursor_count.getCount();

        while (cursor_sum.moveToNext()){

            sum=cursor_sum.getInt( cursor_sum.getColumnIndex( "sum(pm2_5)" ) );

        }

        int proportion=sum/count;

        cursor_count.close();
        cursor_sum.close();

        return proportion;
    }


    //保存的内容的方法
    private ContentValues insert(JSONObject json) throws JSONException {

        ContentValues cv=new ContentValues(  );
        cv.put( "pm2_5",json.getInt( "pm2.5" ) );
        cv.put( "co2",json.getInt( "co2" ) );
        cv.put( "LightIntensity",json.getInt( "LightIntensity" ) );
        cv.put( "humidity",json.getInt( "humidity" ) );
        cv.put( "temperature",json.getInt( "temperature" ) );

        return cv;

    }


    //模拟北京
    private JSONObject BeiJing(){

        //模拟数据
        JSONObject beijing=new JSONObject(  );
        try {
            beijing.put( "pm2.5",(int) (Math.random()*350) );
            beijing.put( "co2",(int) (Math.random()*5951) );
            beijing.put( "LightIntensity",(int) (Math.random()*2000) );
            beijing.put( "humidity",(int) (Math.random()*45) );
            beijing.put( "temperature",(int) (Math.random()*50) );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beijing;


/*        //网络请求
        final Message msg=Message.obtain();
        String url="http://localhost:8088/transportservice/action/GetSenseByName.do";
        JSONObject outJson=new JSONObject(  );
        try {
            outJson.put( "UserName", "user1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest( Request.Method.POST, url, outJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        msg.obj=jsonObject;
                        handler.sendMessage( msg );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                } );
        mQueue.add( jsonObjectRequest );
        return msg;*/
    }


    //模拟重庆
    private JSONObject ChongQing(){
        JSONObject chongqing=new JSONObject(  );
        try {
            chongqing.put( "pm2.5",(int) (Math.random()*350) );
            chongqing.put( "co2",(int) (Math.random()*5951) );
            chongqing.put( "LightIntensity",(int) (Math.random()*2000) );
            chongqing.put( "humidity",(int) (Math.random()*45) );
            chongqing.put( "temperature",(int) (Math.random()*50) );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return chongqing;

    }


    //模拟上海
    private JSONObject ShangHai(){
        JSONObject shanghai=new JSONObject(  );
        try {
            shanghai.put( "pm2.5",(int) (Math.random()*350) );
            shanghai.put( "co2",(int) (Math.random()*5951) );
            shanghai.put( "LightIntensity",(int) (Math.random()*2000) );
            shanghai.put( "humidity",(int) (Math.random()*45) );
            shanghai.put( "temperature",(int) (Math.random()*50) );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return shanghai;

    }



    //模拟深圳
    private JSONObject ShenZhen(){
        JSONObject shenzhen=new JSONObject(  );
        try {
            shenzhen.put( "pm2.5",(int) (Math.random()*350) );
            shenzhen.put( "co2",(int) (Math.random()*5951) );
            shenzhen.put( "LightIntensity",(int) (Math.random()*2000) );
            shenzhen.put( "humidity",(int) (Math.random()*45) );
            shenzhen.put( "temperature",(int) (Math.random()*50 ));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shenzhen;
    }


    //模拟雄安
    private JSONObject XiongAn(){
        JSONObject xiongan=new JSONObject(  );
        try {
            xiongan.put( "pm2.5",(int) (Math.random()*350));
            xiongan.put( "co2",(int) (Math.random()*5951) );
            xiongan.put( "LightIntensity",(int) (Math.random()*2000) );
            xiongan.put( "humidity",(int) (Math.random()*45) );
            xiongan.put( "temperature",(int) (Math.random()*50) );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return xiongan;

    }



    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        sqLite.close();
    }
}
