package com.mywork.henry.henry_smarthome.ThreadPool;


import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Henry on 2016/11/25.
 */

public class SimpleThreadPool {

    public static final int FLEXIBLE=0;
    private customThread[] threads=null;private final ArrayList<customThread> flexiblethreads=new ArrayList<>();
    private final int defaultperiod;private final ArrayList<Runnable> taskpool=new ArrayList<>();
    //private final Object lock=new Object();

    public synchronized void givetask(Runnable run){
        synchronized (taskpool){
            taskpool.add(run);
        }
        if (threads!=null) {
            callThreadstowork();
        }else {
            callFlexibletoWork();
        }
    }

    private synchronized void callThreadstowork(){
        for (customThread thread : threads) {
            if (thread.getState() == Thread.State.TIMED_WAITING) {
                if(taskpool.size()>0) {
                    thread.interrupt();
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (taskpool.size() > 0) {
            for (int i = 0; i < threads.length; i++) {
                Thread.State state = threads[i].getState();
                if (state == Thread.State.TERMINATED) {
                    threads[i] = new customThread(defaultperiod, false);
                    synchronized (taskpool) {
                        if (taskpool.size() > 0) {
                            threads[i].setrun(taskpool.get(0), taskpool);
                            threads[i].start();
                        }
                    }

                } else if (state == Thread.State.NEW) {
                    synchronized (taskpool) {
                        if (taskpool.size() > 0) {
                            threads[i].setrun(taskpool.get(0), taskpool);
                            threads[i].start();
                        }
                    }

                }
            }
        }

    }

    private synchronized void callFlexibletoWork(){
        synchronized (flexiblethreads){
            for (customThread thread : flexiblethreads) {
                if (thread.getState() == Thread.State.TIMED_WAITING) {
                    if(taskpool.size()>0) {
                        thread.interrupt();
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (taskpool.size() > 0) {
            synchronized (flexiblethreads){
                for (customThread thread : flexiblethreads) {
                    if (thread.getState()== Thread.State.NEW){
                        synchronized (taskpool) {
                            if (taskpool.size()>0) {
                                thread.setrun(taskpool.get(0),taskpool);
                                thread.start();
                            }
                        }

                    }else if (thread.getState()== Thread.State.TERMINATED){
                        flexiblethreads.remove(thread);
                    }
                }
            }
        }
        if (taskpool.size()>0){
            synchronized (flexiblethreads){
                flexiblethreads.add(new customThread(defaultperiod,true));
                callFlexibletoWork();
            }
        }
    }

    SimpleThreadPool(int capacity,int defaultperiod){
        this.defaultperiod=defaultperiod;
        if (capacity>FLEXIBLE){
            threads=new customThread[capacity];
            for (int i=0;i<capacity;i++){
                threads[i]=new customThread(defaultperiod,false);
            }
        }
    }

    private class customThread extends Thread{
        final boolean inArraylist;
        Runnable run;final int time;boolean cometoend=false;

        @Override
        public void run() {
            if (run!=null){
                run.run();
            }
            refreshorsleep();


        }

        void doending(){
            cometoend=true;
            if(inArraylist){
                synchronized (flexiblethreads){
                    flexiblethreads.remove(this);
                }
            }
        }

        void sleep(int time){
            try {
                Thread.sleep(time);
                if (taskpool.size()>0){
                    refreshorsleep();
                }else {
                    doending();
                }
            } catch (InterruptedException e) {
                Log.d("Interrupted",Thread.currentThread().getName());
                if (!cometoend){
                    refreshorsleep();
                }else {
                    doending();
                }
            }

        }

        void refreshorsleep(){
            boolean seted=false;
            synchronized (taskpool) {
                if (taskpool.size() > 0) {
                    setrun(taskpool.get(0), taskpool);
                    seted=true;

                    //return;
                }
            }
            if (seted){
                run();
            }else {
                sleep(time);
            }
        }

        void setrun(Runnable run,final ArrayList<Runnable> taskpool){
            synchronized (taskpool) {
                this.run = run;
                taskpool.remove(run);
            }
        }

        customThread(int time,boolean inArraylist){
            this.time=time;this.inArraylist=inArraylist;
        }

        boolean end(){
            //synchronized (this) {
            //    System.out.println("FirstNotify");
            //    this.interrupt();
            //}

            synchronized (this) {
                cometoend=true;
                System.out.println("EndingNotify");
                this.interrupt();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Thread.yield();


            return true;
        }
    }
}
