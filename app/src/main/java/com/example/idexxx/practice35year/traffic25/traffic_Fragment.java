package com.example.idexxx.practice35year.traffic25;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.idexxx.practice35year.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class traffic_Fragment extends Fragment {

    private int intpm,inttemperature,inthumidity;
    private int RoadId1,RoadId2,RoadId3;
    private TextView pm;
    private TextView temperature;
    private TextView humidity;
    private Handler myHandeler;
    private Timer timer;
    private TextView number_one;
    private TextView number_two;
    private TextView number_three;
    String[] Status=new String[]{"通畅","较通畅","拥挤","堵塞","爆表"};
    private LinearLayout colors;
    private traffic_colors aview;

    public traffic_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_traffic, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        pm = getView().findViewById( R.id.traffic_pm);
        temperature = getView().findViewById( R.id.traffic_temperature);
        humidity = getView().findViewById( R.id.traffic_humidity);
        number_one = getView().findViewById( R.id.Number_one);
        number_two = getView().findViewById( R.id.Number_two);
        number_three = getView().findViewById( R.id.Number_three);
        colors=getView().findViewById(R.id.colors);
        aview=new traffic_colors( getActivity() );
        colors.addView(aview);

        myHandeler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage( msg );
                switch (msg.what) {
                    case 1:
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            intpm = json.getInt( "pm2.5" );
                            inttemperature = json.getInt( "temperature" );
                            inthumidity = json.getInt( "humidity" );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pm.setText( "PM2.5: " + intpm );
                        temperature.setText( "温    度: " + inttemperature + "°" );
                        humidity.setText( "湿    度: " + inthumidity );
                        break;
                    case 2:
                        JSONObject json= (JSONObject) msg.obj;
                        try {
                            RoadId1=json.getInt("RoadId1");
                            RoadId2=json.getInt("RoadId2");
                            RoadId3=json.getInt("RoadId3");
                            number_one.setText("一号道路:"+Status[RoadId1-1]);
                            number_two.setText("二号道路:"+Status[RoadId2-1]);
                            number_three.setText("三号道路:"+Status[RoadId3-1]);
                            aview.setColors(RoadId1,RoadId2,RoadId3);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();

        timer = new Timer();
        timer.schedule( new TimerTask() {
            @Override
            public void run(){
                try {
                    Message msg=new Message();
                    JSONObject jsonObject=new JSONObject(  );
                    jsonObject.put("pm2.5",(int) (Math.random()*300));
                    jsonObject.put("temperature",(int) (Math.random()*35));
                    jsonObject.put("humidity",(int) (Math.random()*50));
                    msg.what=1;
                    msg.obj=jsonObject;
                    myHandeler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Message msg=Message.obtain();
                    JSONObject jsonObject=new JSONObject(  );
                    jsonObject.put("RoadId1",(int)(Math.random()*5+1));
                    jsonObject.put("RoadId2",(int)(Math.random()*5+1));
                    jsonObject.put("RoadId3",(int)(Math.random()*5+1));
                    msg.what=2;
                    msg.obj=jsonObject;
                    myHandeler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


/*                try {
                    Message msg=new Message();
                    URL url=new URL("http://localhost:8088/transportservice/action/GetAllSense.do");
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Charsert","UTF-8");
                    connection.setRequestProperty("Connect-type","application/json");
                    connection.connect();
                    DataOutputStream out=new DataOutputStream(connection.getOutputStream());
                    JSONObject outJson=new JSONObject(  );
                    outJson.put("UserName","user1");
                    out.writeBytes(outJson.toString());
                    InputStreamReader input=new InputStreamReader(connection.getInputStream());
                    BufferedReader buff=new BufferedReader(input);
                    StringBuffer strbuf=new StringBuffer(  );
                    String line;
                    while((line=buff.readLine())!=null){
                        strbuf.append(line);
                    }
                    JSONObject json=new JSONObject(strbuf.toString());
                    if (json.getString("RESUFT").equals("F")){
                        msg.what=-1;
                        myHandeler.sendMessage(msg);
                        return;
                    }
                    msg.obj=json;
                    myHandeler.sendMessage(msg);
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, 0, 3000 );
    }

    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }

    @Override
    public void onStop(){
        timer.cancel();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        timer.cancel();
        super.onDestroy();
    }
}

