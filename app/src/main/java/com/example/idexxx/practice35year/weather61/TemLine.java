package com.example.idexxx.practice35year.weather61;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by idexxx on 2020/2/24.
 */

public class TemLine extends View {
    int[] maxTem = new int[7];
    int[] minTem = new int[7];
    Paint paint;
    public TemLine(Context context) {
        super( context );
        paint=new Paint(  );
    }

    public void setTemLine(int[] maxTem, int[] minTem) {
        this.maxTem = maxTem;
        this.minTem = minTem;
        postInvalidate();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        int width = getWidth() / 14;
        int height = getHeight() / 4;
        //绘制六个折矩形
        for (int i = 2; i < 14; i += 2) {
            paint.setColor( Color.parseColor( "#708090" ) );
            paint.setStrokeWidth( 2 );
            paint.setAntiAlias( true );
            canvas.drawRect( width * i, height*4, width * i + 3, 0, paint );
        }
        //绘制最高气温折线图
        for (int i = 0; i < 6; i++) {
            paint.setColor( Color.parseColor( "#8087CEFA" ) );
            paint.setStrokeWidth( 5 );
            paint.setAntiAlias( true );
            canvas.drawLine( width * ((i * 2) + 1), height*3 - maxTem[i] * 5, width * ((i + 1) * 2 + 1), height*3 - maxTem[i + 1] * 5, paint );
        }
        //绘制最高气温圆点和文字
        for (int i = 0; i < 7; i++) {
            paint.setColor( Color.parseColor( "#F5FFFA" ) );
            paint.setStrokeWidth( 5 );
            paint.setAntiAlias( true );
            canvas.drawText( String.valueOf( maxTem[i] ) + "℃", width * ((i * 2) + 1), height*3 - maxTem[i] * 5 - 20, paint );
            canvas.drawCircle( width * ((i * 2) + 1), height*3 - maxTem[i] * 5, 8, paint );
        }
        //绘制七天最低气温折线图
        for (int i = 0; i < 6; i++) {
            paint.setColor( Color.parseColor( "#8000FA9A" ) );
            paint.setStrokeWidth( 5 );
            paint.setAntiAlias( true );
            canvas.drawLine( width * ((i * 2) + 1), height*3 - minTem[i] * 5, width * ((i + 1) * 2 + 1), height*3 - minTem[i + 1] * 5, paint );
        }
        //绘制最低气温圆心与文字
        for (int i = 0; i < 7; i++) {
            paint.setColor( Color.parseColor( "#F5FFFA" ) );
            paint.setStrokeWidth( 5 );
            paint.setAntiAlias( true );
            canvas.drawText( String.valueOf( minTem[i] ) + "℃", width * ((i * 2) + 1), height*3 - minTem[i] * 5 + 20, paint );
            canvas.drawCircle( width * ((i * 2) + 1), height*3 - minTem[i] * 5, 8, paint );
        }
    }
}
