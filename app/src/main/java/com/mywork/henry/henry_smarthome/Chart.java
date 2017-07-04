package com.mywork.henry.henry_smarthome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class Chart extends ImageView {
    public static volatile boolean haswiped;
    Canvas canvasback;Paint paint = new Paint();private ArrayList<setting> settings=new ArrayList<>(100);
    setting setting; public final static int Text_Top=1;public final static int Text_Center=2;
    public final static int Text_Bottom=3;
    public Chart(Context context) {
        super(context);
    }
    public Chart(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
    }

    public Chart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Chart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            if (!settings.isEmpty()) {
                for (setting value : settings) {
                    paint.setAntiAlias(true);
                    paint.setColor(value.color);
                    paint.setStrokeCap(value.cap);
                    paint.setStrokeWidth(value.width);
                    paint.setStyle(value.style);paint.setTextSize(value.textSize);
                    paint.setShader(value.shader);paint.setTextAlign(value.textalign);
                    Paint.FontMetrics fontmetrics = paint.getFontMetrics();
                    float baselineY=0;
                    switch (value.textY){
                        case Text_Top:baselineY = -fontmetrics.top/*-fontmetrics.leading*/;break;
                        case Text_Center:baselineY=-fontmetrics.top-(fontmetrics.bottom-fontmetrics.top)/2;break;
                        case Text_Bottom:baselineY=-fontmetrics.bottom;break;
                    }
                    if (value.RotateDegree!=0){
                        canvas.save();
                        canvas.rotate(value.RotateDegree,value.rotatepX,value.rotatepY);
                    }
                    switch (value.which) {
                        case 1:
                            canvas.drawLine(value.x, value.y, value.endx, value.endy, paint);
                            break;
                        case 2:
                            canvas.drawPoint(value.x, value.y, paint);
                            break;
                        case 3:
                            canvas.drawText(value.string, value.x, value.y+baselineY, paint);
                            break;
                        case 4:
                            canvas.drawTextOnPath(value.string,value.path,value.x,value.y,paint);
                            break;
                        case 5:
                            canvas.drawArc(value.rectF,value.x,value.y,true,paint);
                            break;
                    }
                    if (value.RotateDegree!=0){
                    canvas.restore();
                    }
                }
            }
        if (setting!=null) {
            paint.setAntiAlias(true);
            paint.setColor(setting.color);
            paint.setStrokeCap(setting.cap);
            paint.setStrokeWidth(setting.width);
            paint.setStyle(setting.style);paint.setTextSize(setting.textSize);
            paint.setShader(setting.shader);paint.setTextAlign(setting.textalign);
            Paint.FontMetrics fontmetrics = paint.getFontMetrics();
            float baselineY=0;
            switch (setting.textY){
                case Text_Top:baselineY = -fontmetrics.top;break;
                case Text_Center:baselineY=-fontmetrics.top-(fontmetrics.bottom-fontmetrics.top)/2;break;
                case Text_Bottom:baselineY=-fontmetrics.bottom;break;
            }
            if (setting.RotateDegree!=0){
                canvas.save();
                canvas.rotate(setting.RotateDegree,setting.rotatepX,setting.rotatepY);
            }
            switch (setting.which) {
                case 1:
                    canvas.drawLine(setting.x, setting.y, setting.endx, setting.endy, paint);
                    break;
                case 2:
                    canvas.drawPoint(setting.x, setting.y, paint);
                    break;
                case 3:
                    canvas.drawText(setting.string, setting.x, setting.y+baselineY, paint);
                    break;
                case 4:
                    canvas.drawTextOnPath(setting.string,setting.path,setting.x,setting.y,paint);
                    break;
                case 5:
                    canvas.drawArc(setting.rectF,setting.x,setting.y,true,paint);
                    break;
            }
            if (setting.RotateDegree!=0){
                canvas.restore();
            }
        }
        //setting=null;
    }


    protected void setSimpleline(float x, float y, float endx, float endy, int color, Paint.Cap cap,
                                 float width, boolean isnew){

        if (isnew){
            this.setting=new setting(x,y,endx,endy,color,cap,width);
            invalidate();
        }
        else {
            settings.add(new setting(x, y, endx, endy, color, cap, width));
            invalidate();
        }
    }

    protected void setPoint(float x, float y, int color, Paint.Cap cap,
                             float width,Paint.Style style, Shader shader,boolean isnew){

        if (isnew){
            this.setting=new setting(x,y,color,cap,width,style,shader);
            invalidate();
        }
        else {
            settings.add(new setting(x, y, color, cap, width,style,shader));
            invalidate();
        }
    }


    protected void setLine(float x, float y, float endx, float endy, int color, Paint.Cap cap,
                                 float width, Paint.Style style, Shader shader, boolean isnew){

        if (isnew){
            this.setting=new setting(x,y,endx,endy,color,cap,width,style,shader);
            invalidate();
        }
        else {
            settings.add(new setting(x,y,endx,endy,color,cap,width,style,shader));
            invalidate();
        }
    }

    protected void setText(float x, float y, int color
                           , Paint.Style style, Shader shader, String string, Paint.Align textalign,int textY,float textSize){

        settings.add(new setting(x,y,color,style,shader,string,textalign,textY,textSize));
        invalidate();

    }

    protected void setTextonPath(float hOffset, float vOffset, int color,
                                  Paint.Style style, Shader shader, String string, Paint.Align textalign,float textSize,Path path,float RotateDegree,float rotatepX,float rotatepY){
        settings.add(new setting(hOffset,vOffset,color,style,shader,string,textalign,textSize,path,RotateDegree,rotatepX,rotatepY));
        invalidate();
    }

    protected void setArc(float startAngle, float sweepAngle,int color,float width,Paint.Style style, Shader shader,RectF rectF,float RotateDegree,float rotatepX,float rotatepY){
        settings.add(new setting(startAngle,sweepAngle,color,width,style,shader,rectF,RotateDegree,rotatepX,rotatepY));
        invalidate();
    }

    protected void setArrayDrawing (ArrayList<setting> settings){
        this.settings =settings;
        invalidate();
    }

    protected void customDrawing (float startX,float startY,float endX,float endY){


        invalidate(false);
    }

    protected void invalidate(boolean b) {
        try {
            Method invalidate = View.class.getDeclaredMethod("invalidate",boolean.class);
            invalidate.setAccessible(true);
            invalidate.invoke(this,b);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    protected ArrayList<setting> getArraydrawing (){
        return settings;
    }

    protected void clear(){
        this.setting=null;
        this.settings.clear();
    }

    protected void wipeout()  {
        if (settings!=null&&!settings.isEmpty()) {
            haswiped=false;
            //for (int t = Settings_Dialog.size()-1; t >=0; t--) {
            //    Settings_Dialog.remove(t);
            //    invalidate();
            //    try {
            //        Thread.sleep(50);
            //    } catch (InterruptedException e) {
            //        e.printStackTrace();
            //    }
            //}

        }
        haswiped=true;
    }


    public static class setting {
        float x; float y; float endx; float endy; int color; Paint.Cap cap;int textY;RectF rectF;Path path;
        float width; Paint.Style style;Shader shader;String string;Paint.Align textalign;int which;
        float textSize;float RotateDegree;float rotatepX;float rotatepY;

        setting(float x, float y, int color, Paint.Cap cap,
                float width,Paint.Style style, Shader shader){
            this(x,y,0,0,color,cap,width, style,shader,null, Paint.Align.LEFT,Text_Top,0,null,null,2,0,0,0);
        }
        setting(float x, float y, float endx, float endy, int color, Paint.Cap cap,
                float width, Paint.Style style,Shader shader){
            this(x,y,endx,endy,color,cap,width,style,shader,null, Paint.Align.LEFT,Text_Top,0,null,null,1,0,0,0);
        }
        setting(float hOffset, float vOffset, int color, Paint.Style style, Shader shader, String string, Paint.Align textalign,float textSize,Path path,float RotateDegree,float rotatepX,float rotatepY){
            this(hOffset,vOffset,0,0,color, Paint.Cap.ROUND,0,style,shader,string,textalign,0,textSize,null,path,4,RotateDegree,rotatepX,rotatepY);
        }
        setting(float x, float y, int color, Paint.Style style, Shader shader, String string, Paint.Align textalign,int textY,float textSize){
            this(x,y,0,0,color, Paint.Cap.ROUND,0,style,shader,string,textalign,textY,textSize,null,null,3,0,0,0);
        }
        setting(float x, float y, float endx, float endy, int color, Paint.Cap cap,
                float width){
            this(x,y,endx,endy,color,cap,width, Paint.Style.FILL,null,null,Paint.Align.LEFT,Text_Top,0,null,null,1,0,0,0);
        }
        setting(float startAngle, float sweepAngle,int color,float width,Paint.Style style, Shader shader,RectF rectF,float RotateDegree,float rotatepX,float rotatepY){
            this(startAngle,sweepAngle,0,0,color, Paint.Cap.ROUND,width,style,shader,null, Paint.Align.LEFT,Text_Top,0,rectF,null,5,RotateDegree,rotatepX,rotatepY);
        }

        setting(float x, float y, float endx, float endy, int color, Paint.Cap cap,
                float width, Paint.Style style, Shader shader, String string,
                Paint.Align textalign, int textY,float textSize, RectF rectF, Path path , int which,float RotateDegree,float rotatepX,float rotatepY){
            this.x=x;this.y=y;this.endx=endx;this.endy=endy;this.color=color;this.cap=cap;
            this.width=width;this.style=style;this.shader=shader;this.string=string;this.which=which;
            this.textalign =textalign;this.textY=textY;this.rectF = rectF;this.path=path;this.textSize=textSize;
            this.RotateDegree=RotateDegree;this.rotatepX=rotatepX;this.rotatepY=rotatepY;
        }

    }
}