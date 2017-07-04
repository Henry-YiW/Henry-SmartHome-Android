package com.mywork.henry.henry_smarthome;

import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Henry on 2016/10/8.
 */

public class DrawingMethods {
    public static volatile boolean stopani=false;

    static class drawPie implements Runnable {
        Chart tempchart=null;Handler handler=null;
        ArrayList<Data.dataset> dataset;int time;float startX;final int sleeptimes=10;
        final float marginY=25;int Color;String name;RectF rectf;
        final ArrayList<Chart.setting>drawingsave = new ArrayList<>(100);final float margin=80;float segmentdegree;
        float tempTextwidth;final int frameColor = 0xffffffff;float Yheightstartdrift;float startdegree;float pastdegree;float degree;
        float data;float tempdegree;final float annotationmargin=150;RectF dataRectF;float datacenterY;float centerX;
        Path path;final int frameColor2=0xfffcff00;final ArrayList<Float> linemargin=new ArrayList<>();Paint.Align align;
        float textStartX;
        public void run() {
            final Chart chart=tempchart;float Yheight;
            try {
                Yheight=chart.getHeight();
                if (stopani) {

                    return;
                }
                if (dataset.isEmpty()) {
                    return;
                }
                //handler.post(new Runnable() {@Override public void run() {startwipe=true;chart.wipeout();startwipe=false;}});
                //if (stopani){HasForcedlyClosed=true;return;}
                //while (!startwipe||!Chart.haswiped){
                //    if (stopani){HasForcedlyClosed=true;return;}
                //}
                if (stopani) {

                    return;
                }
                final float ArcDrift=getArcdrift(chart);
                Yheight-=marginY+ArcDrift;

                if (stopani) {

                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        chart.clear();
                    }
                });
                if (stopani) {

                    return;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopani) {

                    return;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopani) {

                    return;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopani) {

                    return;
                }
                int segmenttime = time / dataset.size();
                if (stopani) {

                    return;
                }
                Yheightstartdrift = 2 * ArcDrift * (getYheightStartdrift(ArcDrift,chart) - 1);
                if (stopani) {

                    return;
                }
                Yheight -= Yheightstartdrift;
                if (stopani) {

                    return;
                }
                if (startX < chart.getWidth() / 2 + chart.getX()) {
                    if (stopani) {

                        return;
                    }
                    startX = margin + linemargin.get(0);
                    int index = 1;
                    startdegree = -135;
                    textStartX = 0;
                    align = Paint.Align.LEFT;
                    for (Data.dataset temp : dataset) {
                        if (stopani) {

                            return;
                        }
                        name = temp.nameortime;
                        Paint temppaint = new Paint();
                        Color = temp.Color;
                        if (stopani) {

                            return;
                        }
                        temppaint.setTextSize(2 * ArcDrift);
                        if (stopani) {

                            return;
                        }
                        if (startX + 2 * ArcDrift + temppaint.measureText(name) - linemargin.get(index - 1) > chart.getWidth() - margin) {
                            if (stopani) {

                                return;
                            }
                            Yheight += 2 * ArcDrift;
                            startX = margin + linemargin.get(index);
                            index++;
                        }
                        if (stopani) {

                            return;
                        }
                        rectf = new RectF(startX, Yheight - ArcDrift, startX + 2 * ArcDrift, Yheight + ArcDrift);
                        if (stopani) {

                            return;
                        }
                        final int finalColor = Color;
                        final RectF finalrectf = rectf;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setArc(0, 360, finalColor, 10, Paint.Style.FILL, null, finalrectf, 0, 0, 0);

                            }
                        });
                        if (stopani) {

                            return;
                        }
                        savedrawing(new Chart.setting(0, 360, Color, 10, Paint.Style.FILL, null, rectf, 0, 0, 0));
                        startX += 2 * ArcDrift;
                        if (stopani) {

                            return;
                        }
                        tempTextwidth = temppaint.measureText(name);
                        startX += tempTextwidth;
                        if (stopani) {

                            return;
                        }
                        final float finalX = startX - tempTextwidth;
                        final float finalYheight = Yheight;
                        final String finalname = name;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setText(finalX, finalYheight, finalColor, Paint.Style.FILL, null, finalname, Paint.Align.LEFT, Chart.Text_Center, 2 * ArcDrift);
                            }
                        });
                        if (stopani) {

                            return;
                        }
                        savedrawing(new Chart.setting(startX - tempTextwidth, Yheight, Color, Paint.Style.FILL, null, name, Paint.Align.LEFT, Chart.Text_Center, 2 * ArcDrift));
                        if (stopani) {

                            return;
                        }
                        final ArrayList<Chart.setting> tempdrawingsave=(ArrayList<Chart.setting>) drawingsave.clone();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.clear();
                                chart.setArrayDrawing(tempdrawingsave);
                            }
                        });
                        if (stopani) {

                            return;
                        }
                        try {
                            Thread.sleep(segmenttime / 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (stopani) {

                            return;
                        }
                        try {
                            Thread.sleep(segmenttime / 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (stopani) {

                            return;
                        }
                    }
                } else {
                    if (stopani) {

                        return;
                    }
                    startX = chart.getWidth() - margin - linemargin.get(0);
                    int index = 1;
                    startdegree = -45;
                    textStartX = chart.getWidth();
                    align = Paint.Align.RIGHT;
                    if (stopani) {

                        return;
                    }
                    for (Data.dataset temp : dataset) {
                        if (stopani) {

                            return;
                        }
                        name = temp.nameortime;
                        Paint temppaint = new Paint();
                        Color = temp.Color;
                        if (stopani) {

                            return;
                        }
                        temppaint.setTextSize(2 * ArcDrift);
                        if (stopani) {

                            return;
                        }
                        if (startX - 2 * ArcDrift - temppaint.measureText(name) + linemargin.get(index - 1) < margin) {
                            if (stopani) {

                                return;
                            }
                            Yheight += 2 * ArcDrift;
                            startX = chart.getWidth() - margin - linemargin.get(index);
                            index++;
                        }
                        if (stopani) {

                            return;
                        }
                        tempTextwidth = temppaint.measureText(name);
                        startX -= tempTextwidth;
                        if (stopani) {

                            return;
                        }
                        final float finalX = startX + tempTextwidth;
                        final float finalYheight = Yheight;
                        final int finalColor = Color;
                        final String finalname = name;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setText(finalX, finalYheight, finalColor, Paint.Style.FILL, null, finalname, Paint.Align.RIGHT, Chart.Text_Center, 2 * ArcDrift);
                            }
                        });
                        if (stopani) {

                            return;
                        }
                        savedrawing(new Chart.setting(startX + tempTextwidth, Yheight, Color, Paint.Style.FILL, null, name, Paint.Align.RIGHT, Chart.Text_Center, 2 * ArcDrift));
                        if (stopani) {

                            return;
                        }
                        rectf = new RectF(startX - 2 * ArcDrift, Yheight - ArcDrift, startX, Yheight + ArcDrift);
                        if (stopani) {

                            return;
                        }
                        final RectF finalrectf = rectf;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setArc(0, 360, finalColor, 10, Paint.Style.FILL, null, finalrectf, 0, 0, 0);

                            }
                        });
                        if (stopani) {

                            return;
                        }
                        savedrawing(new Chart.setting(0, 360, Color, 10, Paint.Style.FILL, null, rectf, 0, 0, 0));
                        if (stopani) {

                            return;
                        }
                        final ArrayList<Chart.setting> tempdrawingsave=(ArrayList<Chart.setting>) drawingsave.clone();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.clear();
                                chart.setArrayDrawing(tempdrawingsave);
                            }
                        });
                        if (stopani) {

                            return;
                        }
                        try {
                            Thread.sleep(segmenttime / 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (stopani) {

                            return;
                        }
                        try {
                            Thread.sleep(segmenttime / 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (stopani) {

                            return;
                        }
                        startX -= 2 * ArcDrift;
                        if (stopani) {

                            return;
                        }
                    }
                }
                if (stopani) {

                    return;
                }
                final ArrayList<Chart.setting> tempdrawingsave=(ArrayList<Chart.setting>) drawingsave.clone();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        chart.clear();
                        chart.setArrayDrawing(tempdrawingsave);
                    }
                });
                if (stopani) {

                    return;
                }
                float datasum = 0;
                if (stopani) {

                    return;
                }
                for (Data.dataset temp : dataset) {
                    if (stopani) {

                        return;
                    }
                    datasum += temp.data;
                }
                if (stopani) {

                    return;
                }
                centerX = chart.getWidth() / 2;
                datacenterY = (chart.getHeight() - annotationmargin - marginY) / 2;
                if (stopani) {

                    return;
                }
                dataRectF = new RectF(centerX - datacenterY, 0, centerX + datacenterY, datacenterY * 2);
                if (stopani) {

                    return;
                }
                double radian = Math.toRadians(45);
                if (stopani) {

                    return;
                }
                path = new Path();
                path.moveTo(centerX, datacenterY);
                if (stopani) {

                    return;
                }
                path.lineTo(centerX + datacenterY, datacenterY);
                segmentdegree = sleeptimes * 360 / time;
                if (stopani) {

                    return;
                }
                float textsize = 50;
                if (textsize * dataset.size() > chart.getHeight() - annotationmargin - 50) {
                    textsize = (chart.getHeight() - annotationmargin - 50) / dataset.size();
                }
                float datatextUnit = (chart.getHeight() - annotationmargin - 50) / dataset.size();
                float datatextY = 0;
                pastdegree = 0;
                for (Data.dataset temp : dataset) {
                    if (stopani) {

                        return;
                    }
                    degree = (temp.data / datasum) * 360;
                    name = temp.nameortime;
                    data = temp.data;
                    if (stopani) {

                        return;
                    }
                    Color = temp.Color;
                    if (stopani) {

                        return;
                    }
                    drawdata(datatextY, textsize,chart);
                    if (stopani) {

                        return;
                    }
                    if (pastdegree + degree < 359.999) {
                        pastdegree = pastdegree + degree;
                    }
                    if (stopani) {

                        return;
                    }
                    datatextY += datatextUnit;
                    if (stopani) {

                        return;
                    }
                }
            }
            finally{
                setDefault();
            }
        }

        private int getYheightStartdrift(float arcdrift,final Chart chart){
            float Datawidth=margin;int linenumber=0;int index=1;linemargin.clear();
            for (Data.dataset temp: dataset) {if (stopani){return 0;}
                String measurename = temp.nameortime;
                Paint temppaint = new Paint();
                if (stopani){return 0;}
                temppaint.setTextSize(2*arcdrift);
                if (stopani){return 0;}
                if (Datawidth+2*arcdrift+temppaint.measureText(measurename)>chart.getWidth()-margin){
                    linenumber++;linemargin.add((chart.getWidth()-2*margin-(Datawidth-margin))/2);Datawidth=margin;
                }
                Datawidth += 2 * arcdrift;
                if (stopani){return 0;}
                Datawidth += temppaint.measureText(measurename);
                if (index==dataset.size()){
                    linemargin.add((chart.getWidth()-2*margin-(Datawidth-margin))/2);
                }
                index++;
            }
            return linenumber+1;}

        private void drawdata(float dataY,float textsize,final Chart chart){if (stopani){
            return;}
            tempdegree=0;
            while(true){
                if (stopani){
                    return;}
                if(tempdegree+segmentdegree<degree){
                    tempdegree+=segmentdegree;
                    if (stopani){
                        return;}
                    final float finalstartdegree=startdegree;final float finaltempdegree=tempdegree;final int finalColor=Color;
                    final RectF finaldataRectF=dataRectF;final float finalpastdegree=pastdegree;final float finalcenterX=centerX;
                    final float finaldatacenterY=datacenterY;final String finalname=name;final float finaldata=data;
                    final Path finalpath=path;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            chart.setArc(finalstartdegree,finaltempdegree,finalColor,10, Paint.Style.FILL,null,finaldataRectF,finalpastdegree,finalcenterX,finaldatacenterY);
                            chart.setTextonPath(0,16,frameColor, Paint.Style.FILL,null,finalname+"·"+String.valueOf(Math.round(finaldata))+"·KWH",
                                    Paint.Align.RIGHT,20,finalpath,finalpastdegree+finalstartdegree,finalcenterX,finaldatacenterY);
                        }
                    });
                    if (stopani){
                        return;}

                    try {
                        Thread.sleep(sleeptimes);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (stopani){
                        return;}
                }
                else {
                    Log.d("hasStop","true");
                    if (stopani){
                        return;}Paint temppaint =new Paint();
                    temppaint.setTextSize(textsize);float textwidth=temppaint.measureText(String.valueOf(Math.round(data))+"·KWH");
                    int[]colors=new int[3];colors[0]=Color;colors[1]=Color;colors[2]=frameColor2;
                    float[]positions=new float[3];positions[0]=0f;positions[1]=0.5f;positions[2]=1f;
                    if (stopani){
                        return;}
                    savedrawing(new Chart.setting(textStartX,dataY,Color, Paint.Style.FILL,new LinearGradient(0,dataY+2*textsize/5,0,dataY+textsize+2*textsize/5,colors,positions, Shader.TileMode.CLAMP),String.valueOf(Math.round(data))+"·KWH", align,Chart.Text_Top,textsize));
                    if (stopani){
                        return;}
                    savedrawing(new Chart.setting(startdegree,degree,Color,10, Paint.Style.FILL,null,dataRectF,pastdegree,centerX,datacenterY));
                    if (stopani){
                        return;}
                    savedrawing(new Chart.setting(0,16,frameColor, Paint.Style.FILL,null,name+"·"+String.valueOf(Math.round(data))+"·KWH", Paint.Align.RIGHT,20,path,pastdegree+startdegree,centerX,datacenterY));
                    if (stopani){
                        return;}
                    final ArrayList<Chart.setting> tempdrawingsave=(ArrayList<Chart.setting>) drawingsave.clone();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            chart.clear();
                            chart.setArrayDrawing(tempdrawingsave);
                        }
                    });
                    if (stopani){
                        return;}
                    try {
                        Thread.sleep(sleeptimes);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (stopani){
                        return;}
                    break;
                }
            }
        }

        //drawPie(Handler handler,Chart chart,int time,float startX, ArrayList dataset){
        //    this.configure(handler,chart,time,startX, dataset);

        //}

        public drawPie  configure (Handler handler,Chart chart,int time,float startX, ArrayList dataset){
            this.handler=handler;
            this.tempchart=chart;
            this.time=time;
            this.dataset = (ArrayList<Data.dataset>) dataset.clone();this.startX=startX;
            return this;
        }

        private void setDefault(){
            this.tempchart=null;this.handler=null;
            this.dataset.clear();this.time=0;this.startX=0;
            this.Color=0;this.name=null;this.rectf=null;
            clearsaved();this.segmentdegree=0;
            this.tempTextwidth=0;this.Yheightstartdrift=0;this.startdegree=0;this.pastdegree=0;this.degree=0;
            this.data=0;this.tempdegree=0;this.dataRectF=null;this.datacenterY=0;this.centerX=0;
            this.path=null;this.linemargin.clear();this.align=null;
            this.textStartX=0;
        }

        private float getArcdrift (final Chart chart){
            float temparcdrift=50;int trytimes=0;float usedarcdrift=0;int status=0;int usedstatus=0;boolean enough=false;
            float ArcDrift;
            if (stopani){
                return 0;}
            while (true){
                if (stopani){
                    return 0;}
                //Log.d("temparcdrift",String.valueOf(temparcdrift));
                if(usedarcdrift-temparcdrift>0){usedstatus=status;status=1;}else if(usedarcdrift-temparcdrift<0) {usedstatus=status;status=3;}
                if (stopani){
                    return 0;}
                //Log.d("continuationstatus",String.valueOf(Math.abs(usedstatus-continuationstatus)));
                //Log.d("theHeight",String.valueOf(getYheightStartdrift(temparcdrift)*2*temparcdrift));
                if (trytimes>1&&Math.abs(usedstatus-status)==2){if (stopani){
                    return 0;}enough=true;}
                if (stopani){
                    return 0;}
                if (enough&&getYheightStartdrift(temparcdrift,chart)*2*temparcdrift<annotationmargin){if (stopani){
                    return 0;}ArcDrift=temparcdrift;break;}
                else if (getYheightStartdrift(temparcdrift,chart)*2*temparcdrift>annotationmargin){if (stopani){
                    return 0;}
                    usedarcdrift=temparcdrift;
                    temparcdrift=temparcdrift-0.1f;
                }
                else if (getYheightStartdrift(temparcdrift,chart)*2*temparcdrift<140){if (stopani){
                    return 0;}
                    usedarcdrift=temparcdrift;
                    temparcdrift=temparcdrift+0.2f;
                }
                else {if (stopani){
                    return 0;}
                    ArcDrift=temparcdrift;
                    break;
                }
                if (stopani){
                    return 0;}
                trytimes++;
            }
            if (stopani){
                return 0;}
            Log.d("theHeight",String.valueOf(getYheightStartdrift(ArcDrift,chart)*2*ArcDrift));
            Log.d("theArcDrift",String.valueOf(ArcDrift));
            //Yheight-=marginY+ArcDrift;
            return ArcDrift;
        }

        private void clearsaved (){
            drawingsave.clear();
        }

        private void savedrawing (Chart.setting setting){
            drawingsave.add(setting);
        }

        public void start (Handler handler,Chart chart,int time,float startX, ArrayList dataset){
            this.configure(handler,chart,time,startX, dataset);
            run();

        }


    }



    static class drawHistogram implements Runnable{
        Chart tempchart=null;Handler handler=null;
        int time;final float margin=53;float rawtargetwidth;final int sleeptimes=48;float scaleunitY;int scaleunit;
        float targetwidth;final float targetheight=615;//targetheight = rawtargetheight
        float targetX;int Color;int Usedcolor;
        float segmentX1;float tempsegmentX1;float Yheight;
        float segmentX2;float tempsegmentX2;float tempscaleY;
        float segmentY1;float tempsegmentY1;int scale;int startscale;
        float datastartX=0;float dataX;float tempdataX;float dataY;int rawData;int UsedrawData;
        String dataTime;String UseddataTime;final int FrameColor =0xffffffff;
        ArrayList<Integer> dataColorset;ArrayList data;int FrameColor2;int FrameColor3;
        final int dataWidth=50;float useddataY;float tempdataY;float datasegmentX;float datasegmentY;Shader dataShader;
        final ArrayList<Chart.setting> drawingsave = new ArrayList<>(100);Shader dataShaderfinal;float dataYdrift;
        Chart.setting usedDataTimesetting;
        public void run() {
            final Chart chart=tempchart;
            try {
                rawtargetwidth=chart.getWidth()-margin;
                Yheight= chart.getHeight()-5;
                if (stopani) {
                    return;
                }
                //handler.post(new Runnable() {@Override public void run() {startwipe=true;chart.wipeout();startwipe=false;}});
                //if (stopani){HasForcedlyClosed=true;return;}
                //while (!startwipe||!Chart.haswiped){
                //    if (stopani){HasForcedlyClosed=true;return;}
                //}
                if (data.isEmpty()) {
                    return;
                }
                if (stopani) {
                    return;
                }
                startscale = scale + scaleunit;
                if (stopani) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        chart.clear();
                    }
                });
                if (stopani) {
                    return;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopani) {
                    return;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopani) {
                    return;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopani) {
                    return;
                }
                targetwidth = rawtargetwidth + margin;
                segmentX1 = sleeptimes * (targetwidth - targetX) / time;
                tempsegmentX1 = 0;
                segmentX2 = sleeptimes * (targetX - margin) / time;
                tempsegmentX2 = 0;
                //segmentY2 =12*(targetX-margin)/nameortime;tempsegmentY2 =0;
                if (stopani) {
                    return;
                }
                while (true) {
                    if (stopani) {
                        return;
                    }
                    if (targetX + tempsegmentX1 + segmentX1 < targetwidth &&
                            targetX - tempsegmentX2 - segmentX2 > margin) {
                        if (stopani) {
                            return;
                        }
                        tempsegmentX1 = tempsegmentX1 + segmentX1;//因为这一行离Post离得太近，所以导致Post过去的变量已经被这里改变了。所以这里不要用累计方法了，用重绘的方法好了。
                        tempsegmentX2 = tempsegmentX2 + segmentX2;//因为这一行离Post离得太近，所以导致Post过去的变量已经被这里改变了。所以这里不要用累计方法了，用重绘的方法好了。
                        if (stopani) {
                            return;
                        }
                        final float finaltargetX = targetX;
                        final float finalYheight = Yheight;
                        final float finaltempsegmentX1 = tempsegmentX1;
                        final float finaltempsegmentX2 = tempsegmentX2;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setSimpleline(finaltargetX, finalYheight, finaltargetX + finaltempsegmentX1, finalYheight, FrameColor, Paint.Cap.SQUARE, 10, false);
                                chart.setSimpleline(finaltargetX, finalYheight, finaltargetX - finaltempsegmentX2, finalYheight, FrameColor, Paint.Cap.SQUARE, 10, false);
                            }
                        });
                        if (stopani) {
                            return;
                        }
                        try {
                            Thread.sleep(sleeptimes);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (stopani) {
                            return;
                        }
                    } else {
                        if (stopani) {
                            return;
                        }
                        savedrawing(new Chart.setting(margin, Yheight, targetwidth, Yheight, FrameColor, Paint.Cap.SQUARE, 10));
                        if (stopani) {
                            return;
                        }
                        final ArrayList<Chart.setting> tempdrawingsave=(ArrayList<Chart.setting>) drawingsave.clone();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.clear();
                                chart.setArrayDrawing(tempdrawingsave);
                                //chart.setSimpleline(targetX, Yheight, targetX + tempsegmentX1 + segmentX1, Yheight, 0xff33b5e5, Paint.Cap.ROUND, 10, 1, false);
                                //chart.setSimpleline(targetX, Yheight, targetX - tempsegmentX2 - segmentX2, Yheight, 0xff33b5e5, Paint.Cap.ROUND, 10, 1, false);
                            }
                        });
                        if (stopani) {
                            return;
                        }
                        try {
                            Thread.sleep(sleeptimes);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (stopani) {
                            return;
                        }
                        Log.d("hasstop1", "true");
                        break;
                    }
                    if (stopani) {
                        return;
                    }

                }
                if (stopani) {
                    return;
                }
                segmentY1 = sleeptimes * (targetheight) / time;
                tempsegmentY1 = 0;
                tempscaleY = Yheight + scaleunitY;//这样赋值是为了防止第一次Post因为在执行前变量就被下面的“tempscaleY = tempscaleY - scaleunitY;scale=scale+scaleunit;”
                //赋值语句给修改，以至于，第一个刻度消失。
                while (true) {
                    if (stopani) {
                        return;
                    }
                    if (Yheight - tempsegmentY1 - segmentY1 > 0) {
                        if (stopani) {
                            return;
                        }
                        tempsegmentY1 = tempsegmentY1 + segmentY1;
                        if (stopani) {
                            return;
                        }
                        final float finaltargetX = targetX;
                        final float finaltempsegmentX2 = tempsegmentX2;
                        final float finalsegmentX2 = segmentX2;
                        final float finalYheight = Yheight;
                        final float finaltempsegmentY1 = tempsegmentY1;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setSimpleline(margin,
                                        finalYheight, margin,
                                        finalYheight - finaltempsegmentY1, FrameColor, Paint.Cap.SQUARE, 10, true);
                            }
                        });
                        if (stopani) {
                            return;
                        }

                        if (Yheight - tempsegmentY1 <= tempscaleY - scaleunitY) { //Keep note here!!!
                            if (stopani) {
                                return;
                            }
                            tempscaleY = tempscaleY - scaleunitY;
                            scale = scale + scaleunit;//由于这里相距post太近，所以这个语句在post后面和在post前面并没有区别。而且可能还不稳定（有可能在Post后面执行了）导致刻度丢失，所以最后把赋值语句放到Post之前。
                            if (stopani) {
                                return;
                            }
                            if ((int) (Math.abs(tempscaleY - Yheight) / scaleunitY) <= dataColorset.size() - 1) {
                                Color = dataColorset.get((int) (Math.abs(tempscaleY - Yheight) / scaleunitY));
                            } else {
                                Color = FrameColor;
                            }
                            if (stopani) {
                                return;
                            }
                            final float finaltempscaleY = tempscaleY;
                            final int finalColor = Color;
                            final int finalscale = scale;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (stopani) {
                                        return;
                                    }
                                    chart.setText(margin, finaltempscaleY - dataWidth / 2,
                                            finalColor, Paint.Style.FILL, null, String.valueOf(finalscale) + " ", Paint.Align.RIGHT, Chart.Text_Center, 33);
                                }
                            });
                            savedrawing(new Chart.setting(margin, tempscaleY - dataWidth / 2, Color, Paint.Style.FILL, null, String.valueOf(scale) + " ", Paint.Align.RIGHT, Chart.Text_Center, 33));
                            if (stopani) {
                                return;
                            }
                        }
                        if (stopani) {
                            return;
                        }
                        try {
                            Thread.sleep(sleeptimes);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (stopani) {
                            return;
                        }
                    } else {
                        if (stopani) {
                            return;
                        }
                        savedrawing(new Chart.setting(margin, Yheight, margin, 0, FrameColor, Paint.Cap.SQUARE, 10));
                        if (stopani) {
                            return;
                        }
                        final ArrayList<Chart.setting> tempdrawingsave=(ArrayList<Chart.setting>) drawingsave.clone();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.clear();
                                //chart.setPoint(50,50,FrameColor, Paint.Cap.ROUND,50,true);
                                chart.setArrayDrawing(tempdrawingsave);
                            }
                        });
                        if (stopani) {
                            return;
                        }
                        try {
                            Thread.sleep(sleeptimes);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (stopani) {
                            return;
                        }
                        Log.d("hasstop2", "true");
                        break;
                    }
                    if (stopani) {
                        return;
                    }
                }
                dataX = (rawtargetwidth) / (data.size() + 1);
                datastartX = margin;
                Log.d("DATAX", String.valueOf(dataX));
                if (stopani) {
                    return;
                }
                if (!data.isEmpty()) {
                    if (stopani) {
                        return;
                    }
                    for (Object temp : data) {
                        if (stopani) {
                            return;
                        }
                        UsedrawData = rawData;
                        if (stopani) {
                            return;
                        }
                        rawData = (int) (((Data.dataset) temp).data);
                        if (stopani) {
                            return;
                        }
                        dataY = (((Data.dataset) temp).data) * (scaleunitY / scaleunit) + (-startscale) * (scaleunitY / scaleunit);
                        if (stopani) {
                            return;
                        }
                        UseddataTime = dataTime;
                        if (stopani) {
                            return;
                        }
                        dataTime = getDataTime(((Data.dataset) temp).nameortime);
                        //dataTime = ((Data.dataset) temp).nameortime;
                        if (stopani) {
                            return;
                        }
                        datastartX = datastartX + dataX;
                        if (stopani) {
                            return;
                        }
                        drawdata(chart);
                        if (stopani) {
                            return;
                        }


                    }
                }
            }
            finally{
                setDefault();
            }
        }
        //drawHistogram(Handler handler,Chart chart,int time, float targetX, float scaleunitY, int scaleunit, int scale, ArrayList<Integer> dataColorset, ArrayList data){
        //    this.configure(handler,chart,time,targetX,scaleunitY,scaleunit,scale,dataColorset,data);
        //}

        private String getDataTime(String data){
            long[] tempdata = Data.convertDatetoDaysandSeconds(data);
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            //DateFormat format= SimpleDateFormat.getDateTimeInstance();
            //Log.d("now",format.format(new Date()));
            long[] tempnow = Data.convertDatetoDaysandSeconds(format.format(new Date()));
            long DistanceBetweenDates = Math.abs(tempnow[1]-tempdata[1]);
            String[] temp = data.split(" ");
            if (DistanceBetweenDates==0){
                temp[0]="";
            }else{
                temp[0] = String.valueOf(DistanceBetweenDates)+"<"+" ";
            }
            String [] temp2  = temp[1].split(":");
            return temp[0]+temp2[0]+":"+temp2[1];
        }

        public drawHistogram configure (Handler handler,Chart chart,int time, float targetX, float scaleunitY, int scaleunit, int scale, ArrayList<Integer> dataColorset, ArrayList data,
                                        int frameColor2,int frameColor3){
            this.handler=handler;
            this.tempchart=chart;
            this.time=time;
            this.targetX=targetX-chart.getX();
            this.scaleunitY =scaleunitY;
            this.scaleunit = scaleunit;
            this.data =(ArrayList) data.clone();this.dataColorset= (ArrayList<Integer>) dataColorset.clone();
            this.scale = scale;
            this.FrameColor2=frameColor2;
            this.FrameColor3=frameColor3;
            return this;
        }

        private void setDefault (){
            this.tempchart=null;this.handler=null;
            this.time=0;this.scaleunitY=0;this.scaleunit=0;
            this.targetwidth=0;
            this.targetX=0;this.Color=0;this.Usedcolor=0;
            this.segmentX1=0;this.tempsegmentX1=0;
            this.segmentX2=0;this.tempsegmentX2=0;this.tempscaleY=0;
            this.segmentY1=0;this.tempsegmentY1=0;this.scale=0;this.startscale=0;
            this.datastartX=0;this.dataX=0;this.tempdataX=0;this.dataY=0;this.rawData=0;this.UsedrawData=0;
            this.dataTime=null;this.UseddataTime=null;this.dataColorset.clear();this.data.clear();
            this.useddataY=0;this.tempdataY=0;this.datasegmentX=0;this.datasegmentY=0;this.dataShader=null;
            clearsaved();this.dataShaderfinal=null;this.dataYdrift=0;
            this.FrameColor2=0;this.FrameColor3=0;
            this.usedDataTimesetting =null;
        }

        public void start (Handler handler,Chart chart,int time, float targetX, float scaleunitY, int scaleunit, int scale, ArrayList<Integer> dataColorset, ArrayList data,
                           int frameColor2, int frameColor3){
            this.configure(handler,chart,time,targetX,scaleunitY,scaleunit,scale,dataColorset,data,frameColor2,frameColor3);
            run();
        }

        private void clearsaved (){
            drawingsave.clear();
        }

        private void Removedrawing(Chart.setting setting){
            drawingsave.remove(setting);
        }

        private void savedrawing (Chart.setting setting){
            drawingsave.add(setting);
        }
        private void savedrawing (int index,Chart.setting setting){
            drawingsave.add(index,setting);
        }

        private void drawdata (final Chart chart){
            if (stopani){
                return;}
            Usedcolor=Color;
            if (stopani){
                return;}
            dataYdrift=Yheight-useddataY-dataWidth/2+dataWidth/8;
            if (stopani){
                return;}
            int times=0;
            if (stopani){
                return;}
            for(int temp = 0; temp<= scale -startscale; temp=temp+scaleunit){
                if (stopani){
                    return;}
                if (dataY>=temp*(scaleunitY/scaleunit)){
                    if (stopani){
                        return;}
                    if (times<=dataColorset.size()-1){
                        dataShaderfinal = new LinearGradient(datastartX, Yheight, datastartX, Yheight - dataY - dataWidth / 2, FrameColor, dataColorset.get(times), Shader.TileMode.CLAMP);
                        Color=dataColorset.get(times);}
                    else {dataShaderfinal = new LinearGradient(datastartX, Yheight, datastartX, Yheight - dataY - dataWidth / 2, FrameColor, FrameColor, Shader.TileMode.CLAMP);
                        Color=FrameColor;}
                    times++;
                }
                else if (dataY<0){dataShaderfinal = new LinearGradient(datastartX, Yheight, datastartX, Yheight - dataY - dataWidth / 2, FrameColor, FrameColor, Shader.TileMode.CLAMP);
                    Color=FrameColor;}
                if (stopani){
                    return;}
            }
            if (stopani){
                return;}
            datasegmentX = sleeptimes*(rawtargetwidth/(data.size()+1))/(time/data.size());
            datasegmentY = sleeptimes*dataY/(time/data.size());tempdataX=0;tempdataY=0;
            while (true){
                if (stopani){
                    return;}
                if (tempdataY <dataY-datasegmentY){
                    tempdataX=tempdataX+datasegmentX;tempdataY=tempdataY+datasegmentY;
                    if (stopani){
                        return;}
                    if (datastartX > dataX+margin) {
                        if (stopani){
                            return;}
                        Log.d("HERERUNN","YESSS");
                        final float finaldatastartX=datastartX;final float finaldataX=dataX;final float finaldataYdrift=dataYdrift;
                        final float finaltempdataX=tempdataX;final float finaluseddataY=useddataY;final float finaldataY=dataY;
                        final int finalUsedcolor=Usedcolor;final int finalColor=Color;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setLine(finaldatastartX-finaldataX, finaldataYdrift,finaldatastartX-finaldataX+finaltempdataX,(((finaluseddataY-finaldataY)/finaldataX)*finaltempdataX)+finaldataYdrift, FrameColor, Paint.Cap.ROUND, dataWidth/4,Paint.Style.FILL,
                                        new LinearGradient(finaldatastartX-finaldataX, finaldataYdrift,finaldatastartX,(((finaluseddataY-finaldataY)/finaldataX)*finaldataX)+finaldataYdrift,finalUsedcolor,finalColor, Shader.TileMode.CLAMP), false);
                            }
                        });
                    }
                    if (stopani){
                        return;}
                    int Temptimes=0;
                    for(int temp = 0; temp<= scale -startscale; temp=temp+scaleunit){
                        if (tempdataY>=temp*(scaleunitY/scaleunit)){
                            if (Temptimes<=dataColorset.size()-1) {
                                dataShader = new LinearGradient(datastartX, Yheight, datastartX, Yheight - tempdataY, FrameColor, dataColorset.get(Temptimes), Shader.TileMode.CLAMP);
                            }
                            else{dataShader = new LinearGradient(datastartX, Yheight, datastartX, Yheight - tempdataY, FrameColor, FrameColor, Shader.TileMode.CLAMP);
                            }
                            Temptimes++;
                        }
                        else if (tempdataY<0){dataShader = new LinearGradient(datastartX, Yheight, datastartX, Yheight - tempdataY, FrameColor, FrameColor, Shader.TileMode.CLAMP);}
                    }
                    if (stopani){
                        return;}
                    final float finaldatastartX=datastartX;final float finalYheight=Yheight;final float finaltempdataY=tempdataY;
                    final Shader finaldataShader=dataShader;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            chart.setLine(finaldatastartX,finalYheight,finaldatastartX,finalYheight-finaltempdataY,FrameColor, Paint.Cap.BUTT,dataWidth, Paint.Style.FILL,finaldataShader,true);
                        }
                    });
                    if (stopani){
                        return;}
                    try {
                        Thread.sleep(sleeptimes);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (stopani){
                        return;}
                }
                else {
                    Log.d("hasthisrun","TRUETRUE");
                    if (stopani){
                        return;}
                    savedrawing(new Chart.setting(datastartX, Yheight-dataY, Color, Paint.Cap.ROUND, dataWidth, Paint.Style.FILL,dataShaderfinal));
                    if (stopani){
                        return;}
                    savedrawing(new Chart.setting(datastartX,Yheight,datastartX,Yheight-dataY,FrameColor, Paint.Cap.BUTT,dataWidth, Paint.Style.FILL,dataShaderfinal));
                    if (stopani){
                        return;}
                    if (datastartX > dataX+margin){
                        savedrawing(new Chart.setting(datastartX-dataX, dataYdrift,datastartX,(((useddataY-dataY)/dataX)*dataX)+dataYdrift, FrameColor, Paint.Cap.ROUND, dataWidth/4, Paint.Style.FILL,
                            new LinearGradient(datastartX-dataX, dataYdrift,datastartX,(((useddataY-dataY)/dataX)*dataX)+dataYdrift,Usedcolor,Color, Shader.TileMode.CLAMP)));
                        if (stopani){
                            return;}
                        if (Yheight-useddataY-dataWidth/2>=35) {
                            savedrawing(new Chart.setting(datastartX - dataX, Yheight - useddataY - dataWidth / 2, FrameColor2, Paint.Style.FILL, null, String.valueOf(UsedrawData) + "°C", Paint.Align.CENTER, Chart.Text_Bottom, 35));
                        }
                        else {savedrawing(new Chart.setting(datastartX - dataX,35, FrameColor2, Paint.Style.FILL, null, String.valueOf(UsedrawData) + "°C", Paint.Align.CENTER, Chart.Text_Bottom, 35));}
                        if (stopani){
                            return;}
                        float pathstartY=Yheight-useddataY+10;int realFrameColor3=FrameColor3;
                        if (pathstartY+getTextPathDrift(UseddataTime,60)>Yheight+5){
                            pathstartY=Yheight-useddataY-dataWidth/2-35-5-getTextPathDrift(UseddataTime,60);
                            realFrameColor3= FormatFactory.getColorWithoutAlpha(realFrameColor3)+0x80000000;
                            if (pathstartY<(-getTextPathDrift(UseddataTime,60))/3){
                                pathstartY=Yheight-useddataY+10;
                                realFrameColor3=FrameColor3;
                            }
                        }
                        if (Yheight-useddataY+10<60) {
                            pathstartY=60;
                        }
                        if (stopani){
                            return;}
                        Path path2 =new Path();path2.moveTo(datastartX-dataWidth/2+3-dataX,pathstartY);path2.lineTo(datastartX-dataWidth/2+3-dataX,pathstartY+getTextPathDrift(UseddataTime,60));
                        if (stopani){
                            return;}
                        Removedrawing(usedDataTimesetting);
                        savedrawing(new Chart.setting(0,0,realFrameColor3, Paint.Style.FILL,null,UseddataTime, Paint.Align.RIGHT,60,path2,0,0,0));}
                    if (stopani){
                        return;}
                    if ( Yheight-dataY-dataWidth/2>=35) {
                        savedrawing(new Chart.setting(datastartX, Yheight - dataY - dataWidth / 2, FrameColor2, Paint.Style.FILL, null, String.valueOf(rawData) + "°C", Paint.Align.CENTER, Chart.Text_Bottom, 35));
                    }
                    else {savedrawing(new Chart.setting(datastartX, 35, FrameColor2, Paint.Style.FILL, null, String.valueOf(rawData) + "°C", Paint.Align.CENTER, Chart.Text_Bottom, 35));}
                    if (stopani){
                        return;}
                    float pathstartY=Yheight-dataY+10;int realFrameColor3=FrameColor3;
                    if (pathstartY+getTextPathDrift(dataTime,60)>Yheight+5){
                        pathstartY=Yheight-dataY-dataWidth/2-35-5-getTextPathDrift(dataTime,60);
                        realFrameColor3= FormatFactory.getColorWithoutAlpha(realFrameColor3)+0x80000000;
                        if (pathstartY<(-getTextPathDrift(dataTime,60))/3){
                            pathstartY=Yheight-dataY+10;
                            realFrameColor3=FrameColor3;

                        }
                    }
                    if (Yheight-dataY+10<60) {
                        pathstartY=60;
                    }
                    if (stopani){
                        return;}
                    Path path =new Path();path.moveTo(datastartX-dataWidth/2+3,pathstartY);path.lineTo(datastartX-dataWidth/2+3,pathstartY+getTextPathDrift(dataTime,60));
                    if (stopani){
                        return;}
                    usedDataTimesetting=new Chart.setting(0,0,realFrameColor3, Paint.Style.FILL,null,dataTime, Paint.Align.RIGHT,60,path,0,0,0);
                    savedrawing(usedDataTimesetting);
                    if (stopani){
                        return;}
                    final ArrayList<Chart.setting> tempdrawingsave=(ArrayList<Chart.setting>) drawingsave.clone();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            chart.clear();
                            chart.setArrayDrawing(tempdrawingsave);
                        }
                    });
                    if (stopani){
                        return;}
                    useddataY=dataY;
                    if (stopani){
                        return;}
                    try {
                        Thread.sleep(sleeptimes);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (stopani){
                        return;}
                    break;
                }


            }
        }

        private int getTextPathDrift(String time,int textsize){
            Paint paint=new Paint();
            paint.setTextSize(textsize);//paint.setColor(0xff000000);
            //Rect measurement=new Rect();
            //paint.getTextBounds();
            return (int)Math.ceil(paint.measureText(time));
        }

    }

}
