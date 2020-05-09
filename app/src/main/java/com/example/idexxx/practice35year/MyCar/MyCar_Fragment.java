package com.example.idexxx.practice35year.MyCar;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCar_Fragment extends Fragment {

    String[] MyCar_id = new String[]{
            "一号小车", "二号小车", "三号小车", "四号小车"
    };
    int[] Car_state = new int[]{
            R.drawable.green, R.drawable.red
    };
    int[] Car_Money = new int[4];
    Timer timer;
    Handler handler;
    private GridView tab1_GridView;
    private ListView tab2_listView;
    private ListView tab3_ListView;
    private List<Map<String, Object>> tab1_data = new ArrayList<>();
    private List<String> tab2_data = new ArrayList<>();
    private List<Map<String,Object>> tab3_data=new ArrayList<>(  );
    private TextView tab3_warning_text;
    private SimpleAdapter tab3_adapter;
    private RequestQueue mQueue=new MyApplication().getRequestQueue();
    private final int[] Car_Balance=new int[]{};
    public MyCar_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_mycar, container, false );
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        iniView();
        tab2_ListView();
        tab3_listView();
    }

    //初始化视图
    private void iniView() {
        TabHost tabHost = getView().findViewById( R.id.mycar_tabhost );
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec( "tab1" )
                .setIndicator( "我的余额" )
                .setContent( R.id.mycar_tab1 );
        TabHost.TabSpec tab2 = tabHost.newTabSpec( "tab2" )
                .setIndicator( "远程控制" )
                .setContent( R.id.mycar_tab2 );
        TabHost.TabSpec tab3 = tabHost.newTabSpec( "tab3" )
                .setIndicator( "充值记录" )
                .setContent( R.id.mycar_tab3 );
        tabHost.addTab( tab1 );
        tabHost.addTab( tab2 );
        tabHost.addTab( tab3 );
        tab1_GridView = getView().findViewById( R.id.myCar_tab1_gridView );
        tab2_listView = getView().findViewById( R.id.tab2_listView );
        tab3_ListView = getView().findViewById(R.id.tab3_listView);
        tab3_warning_text = getView().findViewById( R.id.warning_text );
    }

    //tab2_ListView适配器
    private void tab2_ListView() {

        for (int i = 0; i < MyCar_id.length; i++) {
            tab2_data.add( MyCar_id[i] );
        }
        MyCarStartCloseAdapter adapter = new MyCarStartCloseAdapter( getActivity(), tab2_data );
        tab2_listView.setAdapter( adapter );
        //监听器
        adapter.setOnItemStartCloseButClickListener( new MyCarStartCloseAdapter.OnItemStartCloseButClickListener() {
            @Override
            public void StartButClick(int i) {
                final int Car_id=i;
                Toast.makeText( getActivity(), MyCar_id[Car_id] + "启动成功", Toast.LENGTH_SHORT ).show();
/*              //调用路径 http://localhost:8088/transportservice/action/SetCarMove.do
                String url="http://localhost:8088/transportservice/action/SetCarMove.do";
                //请求入参
                JSONObject StartoutJson=new JSONObject(  );
                try {
                    StartoutJson.put("CarId",Car_id+1  );
                    StartoutJson.put( "CarAction", "Start");
                    StartoutJson.put( "UserName","user1" );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //请求服务器
                JsonObjectRequest StartRequest=new JsonObjectRequest( Request.Method.POST, url, StartoutJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Toast.makeText( getActivity(), MyCar_id[Car_id] + "启动成功", Toast.LENGTH_SHORT ).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText( getActivity(), MyCar_id[Car_id] + "启动失败", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                mQueue.add( StartRequest );*/
            }

            @Override
            public void CloseButClick(int i) {
                Toast.makeText( getActivity(), MyCar_id[i] + "关闭成功", Toast.LENGTH_SHORT ).show();
/*              //调用路径 http://localhost:8088/transportservice/action/SetCarMove.do
                String url="http://localhost:8088/transportservice/action/SetCarMove.do";
                //请求入参
                JSONObject StartoutJson=new JSONObject(  );
                try {
                    StartoutJson.put("CarId",Car_id+1  );
                    StartoutJson.put( "CarAction", "Stop");
                    StartoutJson.put( "UserName","user1" );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //请求服务器
                JsonObjectRequest StartRequest=new JsonObjectRequest( Request.Method.POST, url, StartoutJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Toast.makeText( getActivity(), MyCar_id[Car_id] + "停止成功", Toast.LENGTH_SHORT ).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText( getActivity(), MyCar_id[Car_id] + "停止失败", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                mQueue.add( StartRequest );*/
            }
        } );
    }

    //tab3_ListView添加视图
    private void tab3_listView(){
        tab3_ListView.setEmptyView( tab3_warning_text );
        tab3_adapter = new SimpleAdapter( getActivity(),tab3_data,R.layout.mycar_tab3_listview_item,
                new String[]{"Number","Car_id","Money","Date"},
                new int[]{R.id.myCar_tab3_number,R.id.myCar_tab3_CarId,R.id.myCar_tab3_Money,R.id.myCar_tab3_date});
        tab3_ListView.setAdapter( tab3_adapter );
    }

    private String MoneyState(int money) {
        if (money > 50)
            return "余额充足";
        else
            return "余额不足";
    }

    ;

    private int Stateimg(String state) {
        if (state.equals( "余额充足" ))
            return 0;
        else
            return 1;
    }

    private int Moneytoint(String money) {
        for (int i = 0; ; i++) {
            if (money.equals( i )) {
                return i;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage( msg );
                JSONObject json = (JSONObject) msg.obj;
                try {
                    Car_Money[0] = json.getInt( "1" );
                    Car_Money[1] = json.getInt( "2" );
                    Car_Money[2] = json.getInt( "3" );
                    Car_Money[3] = json.getInt( "4" );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tab1_data.clear();
                for (int i = 0; i < 4; i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put( "Car_id", MyCar_id[i] );
                    map.put( "Money_State", MoneyState( Car_Money[i] ) );
                    map.put( "Money", Car_Money[i] );
                    map.put( "img", Car_state[Stateimg( MoneyState( Car_Money[i] ) )] );
                    tab1_data.add( map );
                }
                SimpleAdapter adapter = new SimpleAdapter( getActivity(), tab1_data, R.layout.mycar_tab1_gridview_item,
                        new String[]{"Car_id", "Money_State", "Money", "img"},
                        new int[]{R.id.myCar_Car_id, R.id.myCar_state, R.id.myCar_Money, R.id.myCar_img} );
                tab1_GridView.setAdapter( adapter );
            }
        };

        tab1_GridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int Carid=i;
                View alert_view = getLayoutInflater().inflate( R.layout.mycar_recharge_dialog, null, false );
                final EditText recharge_edit = alert_view.findViewById( R.id.myCar_Recharge_Edit );
                new AlertDialog.Builder( getActivity() )
                        .setTitle( "为\"" + MyCar_id[i] + "\"小车充值" )
                        .setView( alert_view )
                        .setPositiveButton( "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (recharge_edit.getText().toString().isEmpty()) {
                                    Toast.makeText( getActivity(), "请输入金额", Toast.LENGTH_SHORT ).show();
                                } else {
/*                                    //请求入参
                                    JSONObject outJson=new JSONObject(  );
                                    try {
                                        outJson.put( "CarId",Carid );
                                        outJson.put( "Money",Moneytoint(recharge_edit.getText().toString()));
                                        outJson.put( "UserName","user1" );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //调用路径 http://localhost:8088/transportservice/action/SetCarAccountRecharge.do
                                    String url="http://localhost:8088/transportservice/action/SetCarAccountRecharge.do";
                                    //请求服务器
                                    JsonObjectRequest jsonObjectRequest =new JsonObjectRequest( Request.Method.POST, url, outJson,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject jsonObject) {
                                                    Toast.makeText( getActivity(), "充值成功：\"" + recharge_edit.getText().toString() + "\"", Toast.LENGTH_SHORT ).show();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    Toast.makeText( getActivity(), "充值失败", Toast.LENGTH_SHORT ).show();
                                                }
                                            } );
                                    mQueue.add( jsonObjectRequest );*/
                                    //获取时间
                                    SimpleDateFormat dateFormat=new SimpleDateFormat( "yyyy年MM月dd日 HH:mm:ss" );
                                    Date date=new Date( System.currentTimeMillis() );
                                    Map<String,Object> map=new HashMap<>(  );
                                    map.put( "Number", tab3_data.size()+1);
                                    map.put( "Car_id",MyCar_id[Carid] );
                                    map.put( "Money",recharge_edit.getText().toString() );
                                    map.put( "Date", dateFormat.format( date ));
                                    tab3_data.add( map );
                                    tab3_adapter.notifyDataSetChanged();
                                    Toast.makeText( getActivity(), "充值成功：\"" + recharge_edit.getText().toString() + "\"", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        } )
                        .setNegativeButton( "取消", null )
                        .create().show();
            }
        } );
        //每五秒查询一次余额
        timer=new Timer(  );
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                final JSONObject json = new JSONObject();
                try {
                    json.put( "1", Math.random() * 101 );
                    json.put( "2", Math.random() * 101 );
                    json.put( "3", Math.random() * 101 );
                    json.put( "4", Math.random() * 101 );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = json;
                handler.sendMessage( msg );

/*                //调用路径 http://localhost:8088/transportservice/action/GetCarAccountBalance.do
                String url="http://localhost:8088/transportservice/action/GetCarAccountBalance.do";
                //请求入参
                for (int i=0;i<4;i++){
                    final int Carid=i;
                    JSONObject outjson=new JSONObject(  );
                    try {
                        outjson.put( "CarId",Carid+1 );
                        outjson.put( "UserName" ,"user1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest Quety=new JsonObjectRequest( Request.Method.POST, url, outjson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    try {
                                        Car_Balance[Carid]=json.getInt( "Balance" );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText( getActivity(), "查询余额失败", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                    mQueue.add( Quety );

                }
                msg.obj=Car_Balance;
                handler.sendMessage( msg );*/
            }
        }, 0, 5000 );
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
    }
}
