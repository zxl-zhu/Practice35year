package com.example.idexxx.practice35year.RadarChart;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idexxx.practice35year.R;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadarFragment extends Fragment {

    int[] circle=new int[]{
            R.drawable.circle1,
            R.drawable.circle2,
            R.drawable.circle3,
            R.drawable.circle4,
            R.drawable.circle5
    };
    String[] lable=new String[]{
            "驾驶摩托车在车把上悬挂物品的",
            "拖拉机驶入大中型城市中心道路",
            "拖拉机违法规定载人",
            "拖拉机牵引多辆挂车",
            "学习驾驶人不按指定时间上盗录学习驾驶"
    };
    int[] colors = new int[]{
            Color.parseColor( "#36a9ce" ),
            Color.parseColor( "#33ff66" ),
            Color.parseColor( "#ef5aa1" ),
            Color.parseColor( "#ff0000" ),
            Color.parseColor( "#6600ff" )
    };
    private RadarChart radar;

    public RadarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_radar, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        iniView();
        setRadarData();
    }

    private void iniView() {
        radar = getView().findViewById( R.id.radarChart );
    }


    private void setRadarData() {
        List<IRadarDataSet> dataSets=new ArrayList<>(  );
        for(int i=0;i<5;i++){
            dataSets.add( createData( lable[i] ,colors[i]) );
        }
        dataSets.add( DrawCricle() );
        RadarData data=new RadarData( dataSets );
        radar.setData( data );
        YAxis yAxis=radar.getYAxis();
        yAxis.setAxisMaximum( 90f );
        XAxis xAxis=radar.getXAxis();
        xAxis.setDrawLabels( false );
    }

    private RadarDataSet createData(String lable,int Color){
        List<RadarEntry> data=new ArrayList<>(  );
        for (int i=0;i<5;i++){
            data.add( new RadarEntry( (float)(Math.random()*100+1) ) );
            //data.add( new RadarEntry(0) );
        }
        RadarDataSet dataSet=new RadarDataSet( data,lable );
        dataSet.setColor( Color );
        dataSet.setDrawValues( false );
        dataSet.setDrawFilled( true );
        dataSet.setFillColor( Color );
        dataSet.setFillAlpha( 50 );
        return dataSet;
    }

    private RadarDataSet DrawCricle(){
        List<RadarEntry> circleData=new ArrayList<>(  );
        for (int i=0;i<5;i++){
            RadarEntry radarEntry=new RadarEntry( 100f );
            radarEntry.setIcon( getResources().getDrawable( circle[i] ) );
            circleData.add( radarEntry );
        }
        RadarDataSet dataSet=new RadarDataSet( circleData,"" );
        dataSet.setDrawValues( false );
        dataSet.setColor( Color.TRANSPARENT );
        return dataSet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
