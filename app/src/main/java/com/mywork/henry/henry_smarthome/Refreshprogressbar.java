package com.mywork.henry.henry_smarthome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;





public class Refreshprogressbar extends ProgressBar {
    Paint paint = new Paint();float centerX;float centerY;
    RectF rectf = new RectF();float degree;
    public Refreshprogressbar(Context context) {
        super(context);
    }
    public Refreshprogressbar(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public Refreshprogressbar(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }
    public Refreshprogressbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0xff33b5e5);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        centerX = getMeasuredWidth()/2;centerY=getMeasuredHeight()/2;
        float radius=getMeasuredWidth()/2-20;
        degree = 360*getProgress()/getMax();
        rectf.set(centerX-radius,centerY-radius,centerX+radius,centerY+radius);
        canvas.drawArc(rectf,-90,degree,false,paint);
        canvas.save();
        canvas.rotate(degree,centerX,centerY);
        paint.setStrokeWidth(3);
        double radian = Math.toRadians(50);double radian2 = Math.toRadians(50+7);
        canvas.drawLine(centerX+1,centerY-radius,centerX-5,(float)(centerY-radius-Math.tan(radian)*5),paint);
        canvas.drawLine(centerX+1,centerY-radius,centerX-5,(float)(centerY-radius+Math.tan(radian2)*5),paint);
        canvas.restore();

    }

}