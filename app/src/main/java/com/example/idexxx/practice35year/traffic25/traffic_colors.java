package com.example.idexxx.practice35year.traffic25;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by idexxx on 2020/2/6.
 */

public class traffic_colors extends View implements traffic{
    public int traffic_one, traffic_two, traffic_three;
    Paint paint;

    public traffic_colors(Context context) {
        super( context );
    }

    public void setColors(int traffic_one, int traffic_two, int traffic_three) {
        this.traffic_one=traffic_one;
        this.traffic_two=traffic_two;
        this.traffic_three=traffic_three;
        postInvalidate();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        paint = new Paint(  );
        int width=getWidth()/3;
        int height=getHeight();
        //一号道路
        switch (traffic_one) {
            case 1:
                paint.setColor( Color.parseColor( "#0ebd12" ) );
                canvas.drawRect( 0, height, width, 0, paint );
                break;
            case 2:
                paint.setColor( Color.parseColor( "#98ed1f" ) );
                canvas.drawRect( 0, height, width, 0, paint );
                break;
            case 3:
                paint.setColor( Color.parseColor( "#ffff01" ) );
                canvas.drawRect( 0, height, width, 0, paint );
                break;
            case 4:
                paint.setColor( Color.parseColor( "#ff0103" ) );
                canvas.drawRect( 0, height, width, 0, paint );
                break;
            case 5:
                paint.setColor( Color.parseColor( "#4c060e" ) );
                canvas.drawRect( 0, height, width, 0, paint );
                break;
        }
        //二号道路
        switch (traffic_two) {
            case 1:
                paint.setColor( Color.parseColor( "#0ebd12" ) );
                canvas.drawRect( width, height, width * 2, 0, paint );
                break;
            case 2:
                paint.setColor( Color.parseColor( "#98ed1f" ) );
                canvas.drawRect( width, height, width * 2, 0, paint );
                break;
            case 3:
                paint.setColor( Color.parseColor( "#ffff01" ) );
                canvas.drawRect( width, height, width * 2, 0, paint );
                break;
            case 4:
                paint.setColor( Color.parseColor( "#ff0103" ) );
                canvas.drawRect( width, height, width * 2, 0, paint );
                break;
            case 5:
                paint.setColor( Color.parseColor( "#4c060e" ) );
                canvas.drawRect( width, height, width * 2, 0, paint );
                break;
        }
        //三号道路
        switch (traffic_three) {
            case 1:
                paint.setColor( Color.parseColor( "#0ebd12" ) );
                canvas.drawRect( width * 2, height, width * 3, 0, paint );
                break;
            case 2:
                paint.setColor( Color.parseColor( "#98ed1f" ) );
                canvas.drawRect( width * 2, height, width * 3, 0, paint );
                break;
            case 3:
                paint.setColor( Color.parseColor( "#ffff01" ) );
                canvas.drawRect( width * 2, height, width * 3, 0, paint );
                break;
            case 4:
                paint.setColor( Color.parseColor( "#ff0103" ) );
                canvas.drawRect( width * 2, height, width * 3, 0, paint );
                break;
            case 5:
                paint.setColor( Color.parseColor( "#4c060e" ) );
                canvas.drawRect( width * 2, height, width * 3, 0, paint );
                break;
        }
    }

}
