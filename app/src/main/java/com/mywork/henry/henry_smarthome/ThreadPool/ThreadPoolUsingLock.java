package com.mywork.henry.henry_smarthome.ThreadPool;


import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Henry on 2016/11/25.
 */

public class ThreadPoolUsingLock {
    public static final int FLEXIBLE=0;
    private customThread[] threads=null;private final ArrayList<customThread> flexiblethreads=new ArrayList<>();
    private final int defaultperiod;private final ArrayList<Runnable> taskpool=new ArrayList<>();
    private final Object lock=new Object();
    private volatile boolean HasWorkToDo =false;

    public synchronized void givetask(Runnable run){
        synchronized (taskpool) {
            taskpool.add(run);
        }
        HasWorkToDo=true;
        if (threads!=null) {
            callThreadstowork();
        }else {
            callFlexibletoWork();
        }
        //int [] afa={1,2,3,4,5,6,7};
    }

    private synchronized void callThreadstowork(){
        synchronized (taskpool) {
            for (int i = 0; i < taskpool.size(); i++) {
                if (taskpool.size()<=0){
                    break;
                }
                synchronized (lock) {
                    lock.notify();
                    //HasWorkToDo=true;
                }
                try {
                    taskpool.wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                taskpool.wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (taskpool.size()>0) {
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
                //else if (threads[i].getState()== Thread.State.TIMED_WAITING){
                //
                //}
            }
        }
        if (taskpool.size()>0){
            synchronized (lock) {
                lock.notify();
                //HasWorkToDo=true;
            }
        }
    }

    private synchronized void callFlexibletoWork(){
        synchronized (taskpool) {
            if (flexiblethreads.size() > 0) {
                for (int i = 0; i < taskpool.size(); i++) {
                    if (taskpool.size()<=0){
                        break;
                    }
                    synchronized (lock) {
                        lock.notify();
                        //HasWorkToDo=true;
                    }
                    try {
                        taskpool.wait(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    taskpool.wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (taskpool.size() > 0) {
            synchronized (flexiblethreads){
                for (customThread thread : flexiblethreads) {
                    if (thread.getState()== Thread.State.NEW){
                        synchronized (taskpool) {
                            if (taskpool.size() > 0) {
                                thread.setrun(taskpool.get(0), taskpool);
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

    ThreadPoolUsingLock(int capacity, int defaultperiod){
        this.defaultperiod=defaultperiod;
        if (capacity>FLEXIBLE){
            threads=new customThread[capacity];
            for (int i=0;i<capacity;i++){
                threads[i]=new customThread(defaultperiod,false);
            }
        }
    }

    private class customThread extends Thread{
        Runnable run;final int time;boolean cometoend=false;
        final boolean inArraylist;

        @Override
        public void run() {

            while (true){
                if (!RefreshOrQuit()){
                    break;
                }
                if (run!=null){
                    run.run();
                    run=null;
                }
                if (isInterrupted()){
                    break;
                }
            }
            doending();


        }

        void doending(){
            cometoend=true;
            if(inArraylist){
                synchronized (flexiblethreads){
                    flexiblethreads.remove(this);
                }
            }
        }

        boolean RefreshOrQuit()  {
            synchronized (taskpool) {
                if (HasWorkToDo) {
                    if (taskpool.size() > 0) {
                        setrun(taskpool.get(0), taskpool);
                        HasWorkToDo = taskpool.size() > 0;
                        return true;
                    }
                }
            }
            return Sleep(time);
        }

        boolean Sleep(int time){
            synchronized (lock){
                try {
                    lock.wait(time);
                    if (HasWorkToDo){
                        //if (taskpool.size()<=1) {
                        //    HasWorkToDo = false;
                        //}
                        run=null;
                        return true;
                    }else {
                        return false;
                    }


                } catch (InterruptedException e) {
                    Log.d("InterruptedtoEnd",Thread.currentThread().getName());
                    return false;

                }
            }
        }

        void setrun(Runnable run,ArrayList<Runnable> taskpool){
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
            while(!this.cometoend) {
                synchronized (this) {
                    System.out.println("EndingNotify");
                    this.interrupt();
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
    }
}
