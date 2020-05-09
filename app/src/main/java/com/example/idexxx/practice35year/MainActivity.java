package com.example.idexxx.practice35year;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.idexxx.practice35year.Application.MyApplication;
import com.example.idexxx.practice35year.Map.MapActivity;
import com.example.idexxx.practice35year.Monitor.MonitoFragment;
import com.example.idexxx.practice35year.MyCar.MyCar_Fragment;
import com.example.idexxx.practice35year.Mymessage.MyMessageFragment;
import com.example.idexxx.practice35year.RadarChart.RadarFragment;
import com.example.idexxx.practice35year.traffic25.traffic_Fragment;
import com.example.idexxx.practice35year.weather61.weather61_Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private DrawerLayout dr;
    private NavigationView nav;
    private String hitokoto="雾失楼台 月迷津渡";
    Timer timer;
    private Handler handler;
    private TextView a_brief_remark;
    RequestQueue mQueue=new MyApplication().getRequestQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        a_brief_remark = findViewById( R.id.main_text );
        //设置为横屏
        if (getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        }
        //设置ActionBar
        toolbar = findViewById( R.id.toolbar);
        toolbar.setTitle("首页");
        setSupportActionBar( toolbar );
        dr = findViewById( R.id.drawer);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,dr,toolbar,R.string.app_name,R.string.app_name);
        toggle.syncState();
        dr.setDrawerListener(toggle);
        nav = findViewById( R.id.nav);
        nav.setItemIconTintList(null);
        nav.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                volley();
                Toast.makeText( MainActivity.this, hitokoto, Toast.LENGTH_LONG ).show();
                switch(item.getItemId()){
                    case R.id.item1:
                        toolbar.setTitle("路况查询");
                        toolbar.setBackgroundColor( Color.parseColor( "#707B68EE" ) );
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment,new traffic_Fragment())
                                .commit();
                        dr.closeDrawer(Gravity.START);
                        break;
                    case R.id.item2:
                        toolbar.setTitle( "天气查询" );
                        toolbar.setBackgroundColor( Color.parseColor( "#2000FFFF" ) );
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment,new weather61_Fragment())
                                .commit();
                        dr.closeDrawer(Gravity.START);
                        break;
                    case R.id.item3:
                        toolbar.setTitle( "我的座驾" );
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace( R.id.main_fragment,new MyCar_Fragment() )
                                .commit();
                        dr.closeDrawer( Gravity.START );
                        break;
                    case R.id.item4:
                        toolbar.setTitle( "我的消息" );
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace( R.id.main_fragment,new MyMessageFragment() )
                                .commit();
                        dr.closeDrawer( Gravity.START );
                        break;
                    case R.id.item5:
                        toolbar.setTitle( "违章类型分析" );
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace( R.id.main_fragment,new RadarFragment() )
                                .commit();
                        dr.closeDrawer( Gravity.START );
                        break;
                    case R.id.item6:
                        toolbar.setTitle( "环境监测" );
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace( R.id.main_fragment,new MonitoFragment() )
                                .commit();
                        dr.closeDrawer( Gravity.START );
                        break;
                    case R.id.item8:
                        break;
                    case  R.id.item7:
                        Intent intent=new Intent( MainActivity.this,MapActivity.class );
                        startActivity( intent );
                        dr.closeDrawer( Gravity.START );
                        break;
                }
                return false;
            }
        } );
    }

    private void volley(){
        /*RequestQueue mQueue=Volley.newRequestQueue( MainActivity.this );*/
        String url="https://v1.hitokoto.cn/";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest( Request.Method.GET, url,(JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Message msg=Message.obtain();
                            String content=jsonObject.getString( "hitokoto" );
                            String author=jsonObject.getString( "from" );
                            hitokoto="["+content+"] —— "+author;
                            msg.obj=content+" —— "+author;
                            handler.sendMessage( msg );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hitokoto="网络错误";
                    }
                } );
        mQueue.add( jsonObjectRequest );
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler(  ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage( msg );
                String text= (String) msg.obj;
                a_brief_remark.setText( text );
            }
        };
        //
        timer=new Timer(  );
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                volley();
            }
        },0,6000 );
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
