package com.example.idexxx.practice35year.MyCar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.idexxx.practice35year.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by idexxx on 2020/3/12.
 */

public class MyCarStartCloseAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList = new ArrayList<>();

    public MyCarStartCloseAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get( i );
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from( mContext ).inflate( R.layout.mycar_tab2_listview_item, null, false );
            viewHolder.Car_id = view.findViewById( R.id.tab2_Car_id );
            viewHolder.but_Start = view.findViewById( R.id.tab2_but_Start );
            viewHolder.but_Close = view.findViewById( R.id.tab2_but_Close );
            view.setTag( viewHolder );
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.Car_id.setText( mList.get( i ) );
        viewHolder.but_Start.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.but_Start.setBackgroundColor( Color.parseColor( "#00FF00" ) );
                viewHolder.but_Close.setBackgroundColor( Color.WHITE );
                onItemStartCloseButClickListener.StartButClick( i );
            }
        } );
        viewHolder.but_Close.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.but_Close.setBackgroundColor( Color.parseColor( "#00FF00" ) );
                viewHolder.but_Start.setBackgroundColor( Color.WHITE );
                onItemStartCloseButClickListener.CloseButClick( i );
            }
        } );
        return view;
    }

    public interface OnItemStartCloseButClickListener {
        void StartButClick(int i);
        void CloseButClick(int i);
    }

    private OnItemStartCloseButClickListener onItemStartCloseButClickListener;

    public void setOnItemStartCloseButClickListener(OnItemStartCloseButClickListener onItemStartCloseButClickListener) {
        this.onItemStartCloseButClickListener = onItemStartCloseButClickListener;
    }

    class ViewHolder {
        TextView Car_id;
        Button but_Start;
        Button but_Close;
    }
}
