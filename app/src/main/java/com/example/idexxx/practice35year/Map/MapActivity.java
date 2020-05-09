package com.example.idexxx.practice35year.Map;


import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;


import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.idexxx.practice35year.R;

/**
 * Created by idexxx on 2020/2/16.
 */

public class MapActivity extends AppCompatActivity {

    MapView mapView;
    AMap aMap;
    ImageButton return_button, handover, xiaoche_location, cancelMarker;
    private BitmapDescriptor location_ic;
    private RelativeLayout relayout;
    private int width, height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_map );
        mapView = findViewById( R.id.map );
        mapView.onCreate( savedInstanceState );
        aMap = mapView.getMap();
        relayout = findViewById( R.id.demo_re );
        location_ic = BitmapDescriptorFactory.fromResource( R.drawable.mylocation_ic );
        return_button = findViewById( R.id.return_button );
        //結束Activity
        return_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        //添加按鈕
        addButton();
    }

    public void addButton() {
        Resources resources = this.getResources();
        DisplayMetrics display = resources.getDisplayMetrics();
        width = display.widthPixels;
        height = display.heightPixels;
        Drawable dingwei = getDrawable( R.drawable.dingwei );
        Drawable tuceng = getDrawable( R.drawable.tuceng );
        Drawable quxiaobiaoji = getDrawable( R.drawable.quxiaobiaoji );
        LinearLayout linearLayout = new LinearLayout( this );
        cancelMarker = new ImageButton( this );
        xiaoche_location = new ImageButton( this );
        handover = new ImageButton( this );
        xiaoche_location.setBackground( dingwei );
        handover.setBackground( tuceng );
        cancelMarker.setBackground( quxiaobiaoji );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        //設置LinarLayout屬性
        RelativeLayout.LayoutParams linear_style = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        linear_style.rightMargin = 20;
        linear_style.topMargin = height / 5;
        linear_style.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
        //設置xiaoche_location屬性
        linearLayout.setLayoutParams( linear_style );
        LinearLayout.LayoutParams location_style = new LinearLayout.LayoutParams( 110, 110 );
        xiaoche_location.setLayoutParams( location_style );
        //設置cancelMarker屬性
        LinearLayout.LayoutParams cancelMarker_style = new LinearLayout.LayoutParams( 110, 110 );
        cancelMarker_style.topMargin = 20;
        cancelMarker.setLayoutParams( cancelMarker_style );
        //設置handover屬性
        LinearLayout.LayoutParams handover_style = new LinearLayout.LayoutParams( 110, 110 );
        handover_style.topMargin = 20;
        handover.setLayoutParams( handover_style );
        //綁定視圖
        linearLayout.addView( xiaoche_location );
        linearLayout.addView( cancelMarker );
        linearLayout.addView( handover );
        relayout.addView( linearLayout );
        //顯示小車位置
        xiaoche_location.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMark( 33.961046, 118.3173, "宿遷市", "宿遷位置" );
                addMark( 33.720685, 118.415898, "屠園鄉", "位置" );
                addMark( 33.803521, 118.354023, "洋河新區", "位置" );
                addMark( 31.489146, 120.277475, "江南大學", "江南大學位置" );
            }
        } );
        //觸發菜單并切換圖層
        handover.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu( handover );
            }
        } );
        //取消標記
        cancelMarker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMap.clear();
                myLocation();
            }
        } );
    }

    public void addMark(double latitude, double longitude, String title, String snippet) {
        LatLng latLng = new LatLng( latitude, longitude );
        MarkerOptions options = new MarkerOptions();
        options.position( latLng ).title( title ).snippet( snippet );
        Marker marker = aMap.addMarker( options );
    }

    public void showPopupMenu(View view) {
        //添加菜单
        PopupMenu popupMenu=new PopupMenu( this,view );
        MenuInflater inflater=popupMenu.getMenuInflater();
        inflater.inflate( R.menu.menu_viewswich,popupMenu.getMenu() );
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.daohanitem:
                        //切换为导航视图
                        aMap.setMapType( AMap.MAP_TYPE_NAVI );
                        break;
                    case R.id.yejingitem:
                        //切换为黑夜视图
                        aMap.setMapType( AMap.MAP_TYPE_NIGHT );
                        break;
                    case R.id.biaozhunitme:
                        //切换为标准视图
                        aMap.setMapType( AMap.MAP_TYPE_NORMAL );
                        break;
                    case R.id.weixinitem:
                        //切换为卫星视图
                        aMap.setMapType( AMap.MAP_TYPE_SATELLITE );
                        break;
                    case R.id.jiaotongitem:
                        //切换为交通视图
                        aMap.setMapType( AMap.MAP_TYPE_BUS );
                        break;
                }
                return false;
            }
        } );
        popupMenu.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled( true );
        myLocation();
    }

    //定位
    public void myLocation(){
        MyLocationStyle myLocationStyle=new MyLocationStyle();
        myLocationStyle.myLocationType( MyLocationStyle.LOCATION_TYPE_LOCATE );
      /* myLocationStyle.myLocationIcon( location_ic );*/
        aMap.setMyLocationStyle( myLocationStyle );
        aMap.setMyLocationEnabled( true );
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        aMap.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        mapView.onSaveInstanceState( outState );
    }

}
