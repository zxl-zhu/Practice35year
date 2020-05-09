package com.example.idexxx.practice35year.Mymessage;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.idexxx.practice35year.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMessageFragment extends Fragment {

    Handler handler;
    private Timer timer;
    private ListView listView;
    private Spinner spinner;
    List<Map<String,Object>> data=new ArrayList<>(  );
    private SimpleAdapter adapter;
    private int count=0;
    private int pm25,co2,LightIntensity,humidity,temperature;
    private int pm25_int,co2_int,LightIntensity_int,humidity_int,temperature_int;
    private PieChart pieChart;

    public MyMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_my_message, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        iniView();
        //Spinner监听器
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    data.clear();
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
    }

    private void iniView(){
        //初始化tabhost
        TabHost tabHost=getView().findViewById( R.id.mymessage_tabhoust );
        tabHost.setup();
        tabHost.addTab( tabHost.newTabSpec( "tab1" )
                .setIndicator( "我的消息" )
                .setContent( R.id.message_tab1 ) );
        tabHost.addTab( tabHost.newTabSpec( "tab2" )
                .setIndicator( "我的分析" )
                .setContent( R.id.message_tab2 ) );
        //初始化ListView
        listView = getView().findViewById( R.id.message_list );
       TextView list_tips=getView().findViewById( R.id.message_list_tips );
       listView.setEmptyView( list_tips );
        adapter = new SimpleAdapter( getActivity(),data, R.layout.item_message_tab1,
                new String[]{"Number","Type","Max","Value"} ,
                new int[]{R.id.message_item_tab1_xuhao,R.id.message_item_tab1_baojingleixing,R.id.message_item_tab1_yuzhi,R.id.message_item_tab1_dangqianzhi});
       listView.setAdapter( adapter );
       //获取Spinner
        spinner = getView().findViewById( R.id.mymessage_spinner );
        //获取PieChart
        pieChart = getView().findViewById( R.id.mymessage_piechart );
        pieChart.setNoDataText( "当前还未有报警信息" );
    }

    @Override
    public void onResume() {
        super.onResume();
        handler=new Handler(  ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage( msg );
                JSONObject json= (JSONObject) msg.obj;
                switch (spinner.getSelectedItem().toString()){
                    case "全部":
                        humidity( json );
                        temperature( json );
                        Co2( json );
                        LightIntensity( json );
                        PM25( json );
                        break;
                    case  "湿度":
                        humidity( json );
                        break;
                    case  "温度":
                        temperature( json );
                        break;
                    case "Co2":
                        Co2( json );
                        break;
                    case "光照":
                        LightIntensity( json );
                        break;
                    case "PM2.5":
                        PM25( json );
                        break;
                }
                try {
                    humidity = json.getInt( "humidity" );
                    temperature = json.getInt( "temperature" );
                    co2 = json.getInt( "co2" );
                    LightIntensity = json.getInt( "LightIntensity" );
                    pm25 = json.getInt( "pm2.5" );
                    if (humidity > 30) {
                        humidity_int++;
                    }
                    if (temperature > 30) {
                        temperature_int++;
                    }
                    if (LightIntensity > 20) {
                        LightIntensity_int++;
                    }
                    if (pm25 > 200) {
                        pm25_int++;
                    }
                    if (co2 > 50) {
                        co2_int++;
                    }
                    if (humidity_int == 0 && temperature_int == 0 && LightIntensity_int == 0 && pm25_int == 0 && co2_int == 0) {
                        pieChart.setNoDataText( "当前还未有报警信息" );
                    }else{
                        List<PieEntry> pie_data=new ArrayList<>(  );
                        pie_data.add( new PieEntry( temperature_int, "温度" ) );
                        pie_data.add( new PieEntry( humidity_int, "湿度" ) );
                        pie_data.add( new PieEntry( LightIntensity_int, "光照" ) );
                        pie_data.add( new PieEntry( co2_int, "Co2" ) );
                        pie_data.add( new PieEntry( pm25_int, "PM2.5" ) );
                        PieDataSet pieDataSet=new PieDataSet( pie_data,"" );
                        List<Integer> colors=new ArrayList<>(  );
                        colors.add( Color.parseColor( "#FFB6C1" ) );
                        colors.add( Color.parseColor( "#DA70D6" ) );
                        colors.add( Color.parseColor( "#7B68EE" ) );
                        colors.add( Color.parseColor( "#4169E1" ) );
                        colors.add( Color.parseColor( "#00CED1" ) );
                        pieDataSet.setColors( colors );
                        PieData pieData=new PieData( pieDataSet );
                        pieChart.setData( pieData );
                        pieChart.setDrawHoleEnabled( false );
                        pieChart.invalidate();
                        Description description=new Description();
                        description.setText( "警报消息统计饼图" );
                        pieChart.setDescription( description );
                    }
                }catch (JSONException e){

                }
            }

        };
        timer=new Timer(  );
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                Message msg= Message.obtain();
                JSONObject json=new JSONObject(  );
                try {
                    json.put("pm2.5",(int) (Math.random()*300));
                    json.put("temperature",(int) (Math.random()*40));
                    json.put("humidity",(int) (Math.random()*50));
                    json.put( "co2",(int) (Math.random()*90) );
                    json.put( "LightIntensity",(int)(Math.random()*40));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj=json;
                handler.sendMessage( msg );
            }
        }, 3000, 3000);

    }

    //PM2.5
    private void PM25(JSONObject jsonObject) {
        try {
            int pm25=jsonObject.getInt( "pm2.5" );
            if (pm25>200){
                count++;
                Map<String,Object> map=new HashMap<>(  );
                map.put( "Number",count );
                map.put( "Type","【PM2.5】报警" );
                map.put( "Max",200 );
                map.put( "Value",pm25 );
                ControlData(map);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Co2
    private void Co2(JSONObject jsonObject) {
        try {
            int co2 = jsonObject.getInt( "co2" );
            if (co2>50){
                count++;
                Map<String,Object> map=new HashMap<>(  );
                map.put( "Number",count);
                map.put( "Type","【Co2】报警" );
                map.put( "Max",50 );
                map.put( "Value",co2 );
                ControlData(map);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //光照强度
    private void LightIntensity(JSONObject jsonObject){
        try {
            int LightIntensity=jsonObject.getInt( "LightIntensity" );
            if (LightIntensity>20){
                count++;
                Map<String,Object> map=new HashMap<>(  );
                map.put( "Number",count);
                map.put( "Type","【光照强度】报警" );
                map.put( "Max",20 );
                map.put( "Value",LightIntensity );
                ControlData(map);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //湿度
    private void humidity(JSONObject jsonObject){
        try {
            int  humidity=jsonObject.getInt( "humidity" );
            if (humidity>30){
                count++;
                Map<String,Object> map=new HashMap<>(  );
                map.put( "Number",count );
                map.put( "Type","【湿度】报警" );
                map.put( "Max",30 );
                map.put( "Value",humidity );
                ControlData(map);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //温度
    private void temperature(JSONObject jsonObject) {
        try {
            int temperature = jsonObject.getInt( "temperature" );
            if (temperature > 30) {
                count++;
                Map<String, Object> map = new HashMap<>();
                map.put( "Number", count );
                map.put( "Type", "【温度】报警" );
                map.put( "Max", 30 );
                map.put( "Value", temperature );
                ControlData(map);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ControlData(Map map) {
        if (data.size() > 6) {
            data.remove( 0 );
            data.add( map );
        } else {
            data.add( map );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        data.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        data.clear();
    }
}
