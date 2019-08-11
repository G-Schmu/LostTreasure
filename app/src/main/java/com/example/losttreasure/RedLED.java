package com.example.losttreasure;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;


public class RedLED extends View {

    private int ledoffcolor, ledoncolor;
    private Paint ledPaint;
    private Boolean ledstate;

    public RedLED (Context context, AttributeSet attrs) {
        super(context, attrs);

        ledPaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RedLED,0,0);
        try {
            ledoffcolor = a.getColor(R.styleable.RedLED_ledoffcolor, 0);
            ledoncolor = a.getColor(R.styleable.RedLED_ledoncolor, 0);
        }
        catch (Exception e) {
            Toast.makeText(context,"Couldn't load colors", Toast.LENGTH_SHORT);
        }
        finally {
            a.recycle();
        }

        ledPaint.setStyle(Style.FILL);
        ledPaint.setAntiAlias(true);
        ledPaint.setColor(Color.parseColor("#8C0303"));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewWidth = this.getMeasuredWidth()/2;
        int viewHeight = this.getMeasuredHeight()/2;

        int radius;
        if (viewWidth>viewHeight)
            radius = viewHeight-10;
        else
            radius = viewWidth-10;

        canvas.drawCircle(viewWidth,viewHeight,radius,ledPaint);
    }

    public void flashnTimes (int n) {
        for (int i = 0; i < n; i++) {

        }

    }

    public void turnOn () {
        ledPaint.setColor(Color.parseColor("#F20505"));
        invalidate();
        requestLayout();
    }

    public void turnOff () {
        ledPaint.setColor(Color.parseColor("#8C0303"));
        invalidate();
        requestLayout();
    }

    public void blinkntimes (int n, int frequency) {
        int totaltime = 2*n*frequency;
        new CountDownTimer(totaltime, frequency) {
            int interval = 0;
            public void onFinish() {
                turnOff();
            }
            public void onTick(long millisUntilFinished) {
                if (interval % 2 == 0)
                    turnOn();
                else
                    turnOff();
                interval++;
            }
        }.start();
    }

}
