package com.example.idexxx.practice35year.weather61;


import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.idexxx.practice35year.Application.MyApplication;
import com.example.idexxx.practice35year.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class weather61_Fragment extends Fragment {

    Handler handler;
    private GridView gridView;
    List<Map<String,Object>> data;

    String[] img_str=new String[]{ "xue","lei","shachen","wu","bingbao","yun","yu","yin","qing" };
    int[] wea_img= new int[]{ R.drawable.xue,R.drawable.lei,R.drawable.shachen,R.drawable.wu,R.drawable.bingbao,R.drawable.yun,
            R.drawable.yu,R.drawable.yin,R.drawable.qing };
    private RelativeLayout relayout;
    private TemLine temLine;
    int[] maxTem=new int[7];
    int[] minTem=new int[7];
    private TextView now_tem;
    private ImageButton shuaxin;
    private RelativeLayout top_re;
    private TextView date;
    private Spinner spinner_city;
    private RequestQueue mQueue=new MyApplication().getRequestQueue();


    public weather61_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_weather61, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        gridView = getView().findViewById( R.id.weather_grview );
        relayout = getView().findViewById( R.id.view );
        now_tem = getView().findViewById( R.id.now_tem );
        shuaxin = getView().findViewById( R.id.shuaxin );
        top_re = getView().findViewById( R.id.top_re );
        spinner_city = getView().findViewById( R.id.spinner_City );
        temLine = new TemLine( getActivity() );
        relayout.addView( temLine );
       volley();
        //connect();
        addtime();
        shuaxin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volley();
                // connect();
                Calendar calendar=Calendar.getInstance();
                date.setText(  calendar.get( Calendar.HOUR )+":"+calendar.get( Calendar.MINUTE )+" 刷新" );
                Toast.makeText( getActivity(), "已更新实时天气", Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    public void addtime() {
        date = new TextView( getActivity());
        RelativeLayout.LayoutParams date_style=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        date_style.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
        date_style.rightMargin=10;
        date.setLayoutParams( date_style );
        date.setTextSize( 15 );
        date.setTextColor( Color.parseColor( "#8087CEFA" ) );
        top_re.addView( date );
    }


    @Override
    public void onResume() {
        super.onResume();
        //spinner监听
        spinner_city.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                volley();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
        handler=new Handler(  ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage( msg );
                if (msg.what==-1){
                    Toast.makeText( getActivity(),"网络异常："+msg.obj,Toast.LENGTH_LONG ).show();
                    return;
                }
                String jsondata= (String) msg.obj;
                data=new ArrayList<>(  );
                data.clear();
                try {
                    JSONArray jsonArray=new JSONArray( jsondata );
                    JSONObject temjson = jsonArray.getJSONObject( 0 );
                    String tem = temjson.getString( "tem" );
                    String wea = temjson.getString( "wea" );
                    now_tem.setText( "当前天气情况：" + tem + "  " + wea );
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject( i );
                        //取最低气温与最高低温
                        maxTem[i]=tointTem( jsonObject.getString( "tem1" ) );
                        minTem[i]=tointTem( jsonObject.getString( "tem2" ) );
                        Map<String,Object> item=new HashMap<>(  );
                        //取day
                        String day=jsonObject.getString( "day" );
                        String[] dayarr=day.split( "（" );
                        String[] day1=dayarr[1].split( "）");
                        item.put( "day",day1[0] );
                        //取月日
                        String date=jsonObject.getString("date");
                        String[] datearr=date.split( "-" );
                        item.put( "date", datearr[1]+"-"+datearr[2]);
                        //取天气
                        item.put( "wea", jsonObject.getString( "wea" ));
                        //取图片
                        item.put( "wea_img",wea_img[weaimg( jsonObject.getString( "wea_img" ) )] );
                        data.add( item );
                }
                    SimpleAdapter simpleAdapter=new SimpleAdapter( getActivity(),data,R.layout.weather_item,
                            new String[]{"day","date","wea","wea_img"},new int[]{ R.id.day,R.id.week,R.id.weather_text,R.id.wea_img} );
                    gridView.setAdapter( simpleAdapter );
                    temLine.setTemLine( maxTem,minTem );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

    }

    //返回图片索引
    public int weaimg(String wea_img){
        int i;
        for (i=0;i<img_str.length;i++){
            if (wea_img.equals( img_str[i])){
                break;
            }
        }
        return i;
    }

    //气温转换为整型
    public int tointTem(String tem){
        for (int i=-30;;i++){
            String strtem=i+"℃";
            if (tem.equals( strtem )){
                return i;
            }
        }
    }

    //新请求服务器
    public void volley() {
        String city=spinner_city.getSelectedItem().toString();
        String httpURL = "https://tianqiapi.com/api?version=v1&appid=59914757&appsecret=q72Kwm1r&city=" + city;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.GET, httpURL, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Message msg=Message.obtain();
                            String data=jsonObject.getString( "data" );
                            msg.obj=data;
                            handler.sendMessage( msg );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Message msg=Message.obtain();
                        msg.what=-1;
                        handler.sendMessage( msg );
                    }
                }
        );
        mQueue.add( jsonObjectRequest );
    }


/*    //连接服务器
    public void connect(){
        new Thread( new Runnable() {
            @Override
            public void run() {
                Message msg=Message.obtain();
                try {
                    String httpURL="https://tianqiapi.com/api?version=v1&appid=59914757&appsecret=q72Kwm1r&city="+"宿迁";
                    URL url=new URL( httpURL );
                    HttpsURLConnection connection= (HttpsURLConnection) url.openConnection();
                    connection.setDoOutput( true );
                    connection.setDoInput( true );
                    connection.setRequestMethod( "GET" );
                    connection.setRequestProperty( "Charset","UTF-8" );
                    connection.setRequestProperty( "Content-type","application/json" );
                    connection.setConnectTimeout( 5000 );
                    connection.setUseCaches( false );
                    connection.connect();
                    if (connection.getResponseCode()==200){
                        BufferedReader input=new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
                        String line;
                        StringBuffer strbuf=new StringBuffer(  );
                        while ((line=input.readLine())!=null){
                            strbuf.append( line );
                        }
                        JSONObject jsob=new JSONObject( strbuf.toString() );
                        String data=jsob.getString( "data" );
                        msg.obj=data;
                        handler.sendMessage( msg );
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    msg.what=-1;
                    handler.sendMessage( msg );
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } ).start();
    }*/
}
