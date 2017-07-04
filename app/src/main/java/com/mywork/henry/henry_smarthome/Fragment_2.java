package com.mywork.henry.henry_smarthome;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class Fragment_2 extends Fragment{
    Handler handler;Button thermometer;Button electriometer;
    Chart chart;int [] TemperatureColorset =new int[13];
    int [] ElectricityColorset =new int[11];
    volatile boolean startwipe=false;
    ArrayList<Button> Buttonset=new ArrayList<>(3);
    //Thread paintingThread=null; volatile boolean HasForcedlyClosed =false;
    final DrawingMethods.drawHistogram drawHistogram=new DrawingMethods.drawHistogram();
    final DrawingMethods.drawPie drawPie=new DrawingMethods.drawPie();
    CustomDrawingThread paintingThread;
    private final Object lock=new Object();

    @Nullable

    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_second, container, false);view.getX();
        handler = new Handler();
        chart =(Chart)view.findViewById(R.id.chart);
        view.setX(getResources().getDisplayMetrics().widthPixels+MainActivity.viewDelta);
        //Data.Temperature.clear();Data.Electricity.clear();
        //Data.Temperature.add(new Data.dataset(5,"08:00"));Data.Temperature.add(new Data.dataset(43,"12:00"));
        //Data.Temperature.add(new Data.dataset(29,"08:00"));Data.Temperature.add(new Data.dataset(30,"12:00"));
        //Data.Temperature.add(new Data.dataset(35,"08:00"));;Data.Temperature.add(new Data.dataset(-14.8f,"08:00"));Data.Temperature.add(new Data.dataset(10,"08:00"));Data.Temperature.add(new Data.dataset(15,"08:00"));
        //Data.Temperature.add(new Data.dataset(-20,"08:00"));Data.Temperature.add(new Data.dataset(65,"12:00"));
        //Data.Temperature.add(new Data.dataset(25,"08:00"));Data.Temperature.add(new Data.dataset(20,"12:00"));
        //Data.Temperature.add(new Data.dataset(-10,"08:00"));;Data.Temperature.add(new Data.dataset(-5,"08:00"));Data.Temperature.add(new Data.dataset(0,"08:00"));Data.Temperature.add(new Data.dataset(15,"08:00"));


        TemperatureColorset[0]=0xff0172fe;ElectricityColorset[0]=0xff017efe;
        TemperatureColorset[1]=0xff018afe;ElectricityColorset[1]=0xff02a2fd;
        TemperatureColorset[2]=0xff01a2fe;ElectricityColorset[2]=0xff01defe;
        TemperatureColorset[3]=0xff02b9fd;ElectricityColorset[3]=0xff00fed7;
        TemperatureColorset[4]=0xff02ddfd;ElectricityColorset[4]=0xffffba00;
        TemperatureColorset[5]=0xff02fdfa;ElectricityColorset[5]=0xfffe4e01;
        TemperatureColorset[6]=0xff00ff90;ElectricityColorset[6]=0xffff2a00;
        TemperatureColorset[7]=0xff90ff00;ElectricityColorset[7]=0xff90ff00;
        TemperatureColorset[8]=0xffcbfe00;ElectricityColorset[8]=0xffcbfe00;
        TemperatureColorset[9]=0xfffef500;ElectricityColorset[9]=0xfffef500;
        TemperatureColorset[10]=0xffff8a00;ElectricityColorset[10]=0xffff8a00;
        TemperatureColorset[11]=0xffff4e00;
        TemperatureColorset[12]=0xffff1200;

        //int i=5^6;



        //Data.Electricity.add(new Data.dataset(65,"AC"));
        //Data.Electricity.add(new Data.dataset(15,"TV"));
        //Data.Electricity.add(new Data.dataset(70,"Computer"));Data.Electricity.add(new Data.dataset(35,"Cooker"));
        //Data.Electricity.add(new Data.dataset(46,"Fridge"));
        //Data.Electricity.add(new Data.dataset(5,"Light"));Data.Electricity.add(new Data.dataset(35,"Cooker"));
        //Data.Electricity.add(new Data.dataset(35,"Cooker56"));Data.Electricity.add(new Data.dataset(35,"Cook5555er"));
        //Data.Electricity.add(new Data.dataset(35,"Co33333oker"));Data.Electricity.add(new Data.dataset(35,"Coo111ker"));
        //Data.Electricity.add(new Data.dataset(35,"Coo"));Data.Electricity.add(new Data.dataset(35,"Cooke3r"));

        //temppaint.setTextSize(12);//这是不设置字体大小时的默认字体大小；


        //Integer TemperatureColorset=44646;
        //Log.d("theinteger",String.valueOf(TemperatureColorset));

        //initiatePositions(view);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view.getHeight()==0){
                    handler.postDelayed(this,5);
                }
                else {
                    chart.getLayoutParams().height = 4 * (view.getHeight()) / 11;
                    chart.requestLayout();
                    Log.d("height",String.valueOf(chart.getHeight()));
                    electriometer = (Button)view.findViewById(R.id.electriometer) ;
                    thermometer=(Button) view.findViewById(R.id.thermometer);
                    Buttonset.add(thermometer);
                    thermometer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //HasForcedlyClosed =false;
                            if (!MainActivity.onDemandRefreshing) {
                                //DrawingMethods.stopani = true;
                                Log.d("ButtonWidth", String.valueOf(v.getWidth()));
                                Log.d("ButtonX", String.valueOf(v.getX()));
                                //Data.setStartscale(Data.Type_Temperature, 5 + -10 - 5);
                                v.setClickable(false);
                                v.setActivated(true);
                                //if (paintingThread!=null){
                                //    paintingThread.interrupt();
                                //}
                                checkPaintingThread();
                                //Data.getDataset(Data.Type_Temperature).get(0).data=25;
                                //Data.getDataset(Data.Type_Temperature).get(1).data=6;
                                //Data.getDataset(Data.Type_Temperature).get(2).data=30;
                                //0xfffffb82,0xff818181
                                paintingThread.refresh(drawHistogram.configure(handler,chart,600, thermometer.getX() + thermometer.getWidth() / 2, 50, 5, -10 - 5, Data.getColorset(Data.Type_Temperature), Data.getDataset(Data.Type_Temperature),
                                        0xfffffb82,0x76000000));
                                resetButtons(v);
                                //new Thread(new Runnable() {
                                //    int trytimes=0;
                                //    @Override
                                //    public void run() {
                                //        while (true){
                                //            if (trytimes>1000){
                                //                return;
                                //            }
                                //            if(paintingThread==null||!paintingThread.isAlive()){
                                //                break;
                                //            }
                                //            try {
                                //                Thread.sleep(5);
                                //            } catch (InterruptedException e) {
                                //                e.printStackTrace();
                                //            }
                                //            trytimes++;
                                //        }
                                //        DrawingMethods.stopani = false;
                                //        handler.post(new Runnable() {
                                //            @Override
                                //            public void run() {
                                //                for (Button temp:Buttonset){
                                //                    if (temp!=thermometer){
                                //                        temp.setClickable(true);
                                //                        temp.setActivated(false);
                                //                    }
                                //                }
                                //                thermometer.setActivated(!thermometer.isActivated());
                                //            }
                                //        });
                                //        try {
                                //            Thread.sleep(5);
                                //        } catch (InterruptedException e) {
                                //            e.printStackTrace();
                                //        }
                                //        if (DrawingMethods.stopani){return;}
                                //        drawHistogram.start(handler,chart,600, thermometer.getX() + thermometer.getWidth() / 2, 50, 5, -20 - 5, Data.getColorset(Data.Type_Temperature), Data.getDataset(Data.Type_Temperature));
                                //
                                //    }
                                //}).start();

                            }
                        }
                    });
                    Buttonset.add(electriometer);
                    electriometer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!MainActivity.onDemandRefreshing) {
                                //DrawingMethods.stopani = true;
                                v.setClickable(false);
                                v.setActivated(true);
                                //if (paintingThread!=null){
                                //  paintingThread.interrupt();
                                //}
                                checkPaintingThread();
                                paintingThread.refresh(drawPie.configure(handler,chart,600, electriometer.getX(), Data.getDataset(Data.Type_Electricity)));
                                resetButtons(v);
                                //new Thread(new Runnable() {
                                //    int trytimes=0;
                                //    @Override
                                //    public void run() {
                                //        while (true){
                                //            if (trytimes>1000){
                                //                return;
                                //            }
                                //            if(paintingThread==null||!paintingThread.isAlive()){
                                //                break;
                                //            }
                                //            try {
                                //                Thread.sleep(5);
                                //            } catch (InterruptedException e) {
                                //                e.printStackTrace();
                                //            }
                                //            trytimes++;
                                //        }
                                //        DrawingMethods.stopani = false;
                                //        handler.post(new Runnable() {
                                //            @Override
                                //            public void run() {
                                //                for (Button temp:Buttonset){
                                //                    if (temp!=electriometer){
                                //                        temp.setClickable(true);
                                //                        temp.setActivated(false);
                                //                    }
                                //                }
                                //                electriometer.setActivated(!electriometer.isActivated());
                                //            }
                                //        });
                                //
                                //        try {
                                //            Thread.sleep(5);
                                //        } catch (InterruptedException e) {
                                //            e.printStackTrace();
                                //        }
                                //        if (DrawingMethods.stopani){return;}
                                //        drawPie.start(handler,chart,600, electriometer.getX(), Data.getDataset(Data.Type_Electricity));
                                //
                                //        }
                                //
                                //}).start();

                            }

                        }
                    });

                }


            }
        },5);
        //com.mywork.henry.henry_smarthome.Chart temp = new Chart(null);
        //Chart.setting sss = temp.new setting();
        //com.mywork.henry.henry_smarthome.Chart.setting kkkk = new Chart(null).new setting();要这样才可以创建非静态内部类的实例；
        return view;
    }

    void initiatePositions(final View layout){
        final View view1 = layout.findViewById(R.id.imageView1);
        final View view2 = layout.findViewById(R.id.imageView2);
        final View view3 = layout.findViewById(R.id.imageView3);
        final View thermometer = layout.findViewById(R.id.thermometer);
        final View hygrometer = layout.findViewById(R.id.hygrometer);
        final View electriometer = layout.findViewById(R.id.electriometer);
        handler.postDelayed(new Runnable() {
            int trytimes=0;
            @Override
            public void run() {
                if (layout.getHeight()!=0&&view1.getHeight()!=0){
                    int height=layout.getHeight();
                    view1.setY(height*0.40589f);
                    view2.setY(height*0.40589f);
                    view3.setY(height*0.40589f);
                    int buttonheight= (int) (view1.getHeight()*0.6+view1.getY());
                    thermometer.setY(buttonheight);
                    hygrometer.setY(buttonheight);
                    electriometer.setY(buttonheight);
                }else if (trytimes<100){
                    handler.postDelayed(this,10);
                    trytimes++;
                }
            }
        },100);

    }

    void resetButtons(View v){
        for (Button temp:Buttonset){
            if (temp!=v){
                temp.setClickable(true);
                temp.setActivated(false);
            }
        }
    }

    boolean checkPaintingThread(){
        if (paintingThread==null||!paintingThread.isAlive()){
            paintingThread=new CustomDrawingThread();
            paintingThread.setName("paintingThread");
            paintingThread.start();
            return false;
        }else {
            return true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        //DrawingMethods.stopani=true;
        paintingThread.interrupt();
    }

    @Override
    public void onResume() {
        super.onResume();
        int trytimes=0;
        while (trytimes<200){
            if (!checkPaintingThread()){
                break;
            }
            trytimes++;
        }
    }




    class CustomDrawingThread extends Thread{
        Runnable runnable;volatile boolean runfinished=false;
        @Override
        public void run() {

            while (true){
                if (runnable != null) {
                    runfinished=false;
                    runnable.run();
                    runnable = null;
                }

                if (isInterrupted()){
                    return;
                }
                synchronized (lock){
                    try {
                        runfinished=true;
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        void refresh (Runnable runnable){
            synchronized (this){
                DrawingMethods.stopani=true;
                while (true){
                    //if(this.getState()==State.WAITING||this.getState()==State.TIMED_WAITING){
                    if(runfinished){
                        DrawingMethods.stopani=false;
                        break;
                    }
                }
                this.runnable=runnable;
                synchronized (lock){
                    lock.notify();
                }
            }

        }
    }



}
