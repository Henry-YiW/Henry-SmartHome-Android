package com.mywork.henry.henry_smarthome.ThreadPool;

/**
 * Created by Henry on 2016/06/25.
 */

public class ExperimentalComplexThreadPool {
    private realrun[] realruns;
    private int defaultperiod;private int usednumber=0;

    public void giveTask(Runnable run){
        if(usednumber<realruns.length){
            realruns[usednumber].setrun(run);
            if (!realruns[usednumber].isStarted()){
                realruns[usednumber].start();
            }else if(realruns[usednumber].isCometoend()){
                realruns[usednumber]=new realrun(run,defaultperiod);
                realruns[usednumber].start();
            }else {
                realruns[usednumber].refresh();
            }
            usednumber++;
        }else {
            usednumber=0;
            giveTask(run);
        }

    }

    ExperimentalComplexThreadPool(int number){
        this(number,60*1000);
    }

    ExperimentalComplexThreadPool(int number, int defaultperiod){
        this.realruns=new realrun[number];
        this.defaultperiod=defaultperiod;
        for (int b=0;b<realruns.length;b++){
            realruns[b]=new realrun(null,defaultperiod);
        }
    }


    private static class realrun implements Runnable{
        Runnable run;int time;
        volatile boolean cometoend=false;volatile boolean started=false;
        Thread thread;boolean hasincomingtask=false;

        @Override
        public void run() {
            started=true;
            synchronized (this){
                hasincomingtask=false;
                if(run!=null) {
                    run.run();
                }
                try {
                    if (!hasincomingtask) {
                        this.wait(time);
                    }
                    if (hasincomingtask){
                        run();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
            cometoend=true;
            System.out.println("hasended");
        }
        realrun(Runnable run,int time){
            this.run=run;this.time=time;
            this.thread=new Thread(this);
        }
        void start(){
            thread.start();
        }
        void setrun(Runnable run){
            this.run=run;
        }
        void refresh(){
            this.hasincomingtask=true;
            this.notify();
        }
        boolean end(){
            //synchronized (this) {
            //    System.out.println("FirstNotify");
            //    this.notify();
            //}
            while(!this.cometoend) {
                synchronized (this) {
                    hasincomingtask=false;
                    System.out.println("EndingNotify");
                    this.notify();
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Thread.yield();

            }
            return true;
        }
        boolean isStarted(){
            return started;
        }
        boolean isCometoend(){
            return cometoend;
        }

    }


}
