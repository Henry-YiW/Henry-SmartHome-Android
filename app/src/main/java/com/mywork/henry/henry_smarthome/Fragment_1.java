package com.mywork.henry.henry_smarthome;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import java.io.IOException;
import java.lang.ref.WeakReference;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



//注意以后使用HandlerThread，这样可以更方便的使用Handler给它传递信息，因为HandlerThread有getlooper方法。
public  class Fragment_1 extends android.app.Fragment {
    //Button toggle1;ImageView imageview1;
    //Button toggle2;ImageView imageview2;
    //Button toggle3;ImageView imageview3;
    //Button testbutton;//Button tempbutton;
    //ImageView tempimageview;
    public final static  int SUCCESS_STATUS = 1;
    public final static int FAILURE_STATUS = 2;
    public final static int RESTORE = 3;
    public String[] URL;
    private String url = "http://192.168.1.10/arduino/web";
    private Handler handler = new Handler();
    public View view;
    volatile boolean refreshing=false;
    volatile boolean added=false;
    public Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_main, container, false);

        //imageview1=(ImageView)view.findViewById(R.id.imageView1);
        //imageview2=(ImageView)view.findViewById(R.id.imageView2);
        //imageview3=(ImageView)view.findViewById(R.id.imageView3);
        //imageview1.setVisibility(View.INVISIBLE);
        //imageview2.setVisibility(View.INVISIBLE);
        //imageview3.setVisibility(View.INVISIBLE);



        //imageview1.setY
        //view.setX(getResources().getDisplayMetrics().widthPixels/2);

//为什么Post runnable 不会导致内存泄露，因为runnable是放在messagequeue里的，在activity生命周期结束后messagequeue会被清空
        //所以runnable的引用也就没有了，就会被垃圾回收。 而Handler的handlemeassage的方法，是在Handler生成时随着handler
        //一同被加进looper里（甚至looper里面的引用），而在activity的生命周期结束后handler的引用（可能就是Looper）并不会
        //被Nullify 因为可能被MainThread引用着，所以handler里面的handlemassage就会一直保留着context的引用了，
        // 导致context(包括里面的view框架等)也不会被垃圾回收，导致内存泄漏
        //Runnable initrunnable = new Runnable() {
        //    @Override
        //    public void run() {
        //        if (getFragmentManager().findFragmentByTag("fragment2").getView()!=null) {
        //            Log.d("Fragment2ViewX", String.valueOf(getFragmentManager().findFragmentByTag("fragment2").getView().getX()));
        //            Log.d("Fragment2ViewExpectedX",String.valueOf(getResources().getDisplayMetrics().widthPixels+MainActivity.viewDelta));
        //        }
        //        if (imageview2.getWidth()==0 ||getFragmentManager().findFragmentByTag("fragment2").getView()==null
        //                ||getFragmentManager().findFragmentByTag("fragment2").getView().getX()!=getResources().getDisplayMetrics().widthPixels+MainActivity.viewDelta){
        //            Log.d("YLOCATION-----",String.valueOf("NOT READY"));
        //            handler.postDelayed(this,5);
        //            return;
        //        }
        //        int location1[] = new int[2];
        //        int location2[] = new int[2];
        //        int location3[] = new int[2];
        //        toggle1=(Button)view.findViewById(R.id.toggleButton);
        //        toggle1.setOnClickListener(new onclicklistener(imageview1));
        //        toggle2=(Button)view.findViewById(R.id.toggleButton2);
        //        toggle2.setOnClickListener(new onclicklistener(imageview2));
        //        toggle3=(Button)view.findViewById(R.id.toggleButton3);
        //        toggle3.setOnClickListener(new onclicklistener(imageview3));
        //        toggle1.getLocationOnScreen(location1);
        //        view.getLocationOnScreen(location2);
        //        TranslateAnimation transanimation = new TranslateAnimation(0,0, -location1[1]-getResources().getDimension(R.dimen.activity_vertical_margin),0);
        //        Log.d("YLOCATION-----",String.valueOf(location1[1]));
        //        transanimation.setFillAfter(true);
        //        transanimation.setDuration(1000);
        //        transanimation.setInterpolator(new BounceInterpolator());
        //        transanimation.setStartOffset(1000);
        //        toggle1.startAnimation(transanimation);
        //        toggle2.startAnimation(transanimation);
        //        toggle3.startAnimation(transanimation);
        //        imageview1.setX(toggle1.getX()+(toggle1.getWidth()-imageview1.getWidth())/2);
        //        imageview2.setX(toggle2.getX()+(toggle2.getWidth()-imageview2.getWidth())/2);
        //        imageview3.setX(toggle3.getX()+(toggle3.getWidth()-imageview3.getWidth())/2);
        //        imageview1.setY(toggle1.getY()+(toggle1.getHeight()-imageview1.getHeight())/2-40);
        //        imageview2.setY(toggle2.getY()+(toggle2.getHeight()-imageview2.getHeight())/2-40);
        //        imageview3.setY(toggle3.getY()+(toggle3.getHeight()-imageview3.getHeight())/2-40);
        //        imageview1.setVisibility(View.GONE);
        //        imageview2.setVisibility(View.GONE);
        //        imageview3.setVisibility(View.GONE);
        //        //testbutton=(Button)view.findViewById(R.id.testbutton);
        //        //testbutton.setOnClickListener(new testaddview());
        //        Log.d("width",String.valueOf(toggle1.getWidth()));
        //        Log.d("height",String.valueOf(toggle1.getHeight()));
        //        Log.d("theXis",String.valueOf(toggle1.getX()+toggle1.getWidth()/2));
        //        Log.d("theX3is",String.valueOf(toggle3.getX()+toggle3.getWidth()/2));
        //        Log.d("theYis",String.valueOf(toggle1.getY()+toggle1.getHeight()/2));
        //        //Log.d("toggle3description",(String) toggle3.getContentDescription());
        //        Log.d("hashashashas","dddd\"\"\ndff\rr");
        //        //int a =View.generateViewId(); int b=View.generateViewId(); int c=View.generateViewId();
        //        //Log.d("theIDS",String.valueOf(a+"+"+b+"+"+c));
        //        /*width: 270
        //          height: 300
        //          theXis: 183.0
        //          theX3is: 897.0
        //          theYis: 198.0*/
        //    }
        //};
        //handler.postDelayed(initrunnable,5);
        return view;
    }


    class Test2Thread extends Thread{
        @Override
        public void run() {
            while (true) {
                Log.d(Thread.currentThread().getName(), "Has running");
            }
        }
    }

    //public class testaddview implements View.OnClickListener{
    //    int trytimes=1;Map<String,String> parameters=new HashMap<>(3);
    //    @Override
    //    public void onClick(final View v) {
    //        int howmany=4;
    //        Data.whattodonext todowhat=new Data.whattodonext() {
    //            @Override
    //            public void todo() {
    //                new Fragment_1.testthread().start();
    //            }
    //        };
    //        Data.OnsuccessProcess onsuccessProcess=new Data.OnsuccessProcess(howmany,todowhat);
    //        parameters.put("user","henry");parameters.put("pass","yiweigang");
    //        OKHttpTool.asyncCustomPostFormforJsonObject("http://192.168.1.20:8080/Smart_Home/Inquire", parameters, onsuccessProcess, new Data.OnFailed(Data.Type_apparatuscfgset,4,todowhat));
    //        parameters.put("Type","Temperature");
    //        OKHttpTool.asyncCustomPostFormforJsonObject("http://192.168.1.20:8080/Smart_Home/InquireData", parameters, onsuccessProcess, new Data.OnFailed(Data.Type_Temperature,4,todowhat));
    //        parameters.put("Type","Humidity");
    //        OKHttpTool.asyncCustomPostFormforJsonObject("http://192.168.1.20:8080/Smart_Home/InquireData", parameters, onsuccessProcess, new Data.OnFailed(Data.Type_Humidity,4,todowhat));
    //        parameters.put("Type","appliance");
    //        OKHttpTool.asyncCustomPostFormforJsonObject("http://192.168.1.20:8080/Smart_Home/InquireData", parameters, onsuccessProcess, new Data.OnFailed(Data.Type_Electricity,4,todowhat));
    //        //Test2Thread test2Thread=new Test2Thread();
    //        //test2Thread.setDaemon(true);
    //        //test2Thread.start();
    //    }
    //}



    //public class testthread extends Thread{
    //    RelativeLayout layout =(RelativeLayout)view;Button tempbutton;
    //    ImageView tempimageview;
    //    Button usedbutton1;Button usedbutton2;Button usedbutton3;
    //    boolean isActivated;
    //    public void run() {
    //        handler.post(new Runnable() {
    //            @Override
    //            public void run() {
    //                layout.removeAllViews();
    //            }
    //        });//layout.findViewsWithText();
    //        refreshing=true;added=false;
    //        int index=1;
    //        for (Data.apparatuscfg temp:Data.getApparatuses()){
//
    //            tempbutton =new Button(getContext());//因为Activity是Context的子类所以用getActivity和getContext都可以。
    //            tempbutton.setId(temp.id);
    //            tempbutton.setText(temp.nameortime);
    //            tempbutton.setContentDescription(temp.URL);
    //            isActivated=temp.isactivated;
    //            //tempbutton.setActivated(temp.isactivated);
    //            Log.d("newID",String.valueOf(tempbutton.getId()));
    //            tempbutton.setBackground(getContext().getDrawable(R.drawable.animation_selector));
    //            tempbutton.setVisibility(View.INVISIBLE);
    //            if ((index+2)%3==0){
    //                //tempbutton.setY(550);//在这里设置了X或者Y，后面的Layoutparams涉及到X或者Y的部分的设置就无效了。
    //                //layout.generateLayoutParams();//这个必须传入AttributeSet才可以。
    //                handler.post(new Runnable() {
    //                    @Override
    //                    public void run() {
    //                        tempbutton.setActivated(isActivated);Log.d("has1run","true");
    //                        tempbutton.setTextSize(10);
    //                        tempbutton.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
    //                        tempbutton.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
    //                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(getResources().getDimension(R.dimen.toggle_size)),(int)(getResources().getDimension(R.dimen.toggle_size_height)));
    //                        if (usedbutton1!=null) {
    //                            params.addRule(RelativeLayout.BELOW, usedbutton1.getId());
    //                            params.addRule(RelativeLayout.ALIGN_LEFT, usedbutton1.getId());
    //                        }else {
    //                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    //                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    //                            params.topMargin=(int)getResources().getDimension(R.dimen.activity_vertical_margin);
    //                            params.leftMargin=(int)getResources().getDimension(R.dimen.activity_horizontal_margin);
    //                        }
    //                        usedbutton1=tempbutton;
    //                        layout.addView(tempbutton,params);
    //                    }
    //                });
    //                }
    //            else if ((index+1)%3==0){
    //                handler.post(new Runnable() {
    //                    @Override
    //                    public void run() {
    //                        tempbutton.setActivated(isActivated);Log.d("has2run","true");
    //                        tempbutton.setTextSize(15);
    //                        tempbutton.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
    //                        tempbutton.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
    //                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(getResources().getDimension(R.dimen.toggle_size)),(int)(getResources().getDimension(R.dimen.toggle_size_height)));
    //                        if(usedbutton2!=null) {
    //                            params.addRule(RelativeLayout.BELOW, usedbutton2.getId());
    //                            params.addRule(RelativeLayout.ALIGN_LEFT, usedbutton2.getId());
    //                        }else {
    //                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    //                            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    //                            params.topMargin=(int)getResources().getDimension(R.dimen.activity_vertical_margin);
//
    //                        }
    //                        usedbutton2=tempbutton;
    //                        layout.addView(tempbutton,params);
    //                    }
    //                });
    //            }
    //            else if (index%3==0){
    //                handler.post(new Runnable() {
    //                    @Override
    //                    public void run() {
    //                        tempbutton.setActivated(isActivated);Log.d("has3run","true");
    //                        tempbutton.setTextSize(20);
    //                        tempbutton.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
    //                        tempbutton.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
    //                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(getResources().getDimension(R.dimen.toggle_size)),(int)(getResources().getDimension(R.dimen.toggle_size_height)));
    //                        if (usedbutton3!=null) {
    //                            params.addRule(RelativeLayout.BELOW, usedbutton3.getId());
    //                            params.addRule(RelativeLayout.ALIGN_LEFT, usedbutton3.getId());
    //                        }else{
    //                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    //                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    //                            params.topMargin=(int)getResources().getDimension(R.dimen.activity_vertical_margin);
    //                            params.rightMargin=(int)getResources().getDimension(R.dimen.activity_horizontal_margin);
    //                        }
    //                        usedbutton3=tempbutton;
    //                        layout.addView(tempbutton,params);
    //                    }
    //                });
    //            }
    //            handler.post(new Runnable() {
    //                @Override
    //                public void run() {
    //                    if (tempbutton.getHeight()==0){
    //                        handler.postDelayed(this,5);Log.d("Posted","Posted");
    //                        return;
    //                    }
    //                    //Log.d("addedX",String.valueOf(tempbutton.getX()));
    //                    //Log.d("addedY",String.valueOf(tempbutton.getY()));
    //                    //Log.d("addedHeight",String.valueOf(tempbutton.getHeight()));
    //                    TranslateAnimation transanimation = new TranslateAnimation(0,0,
    //                            -tempbutton.getY()-tempbutton.getHeight(),0);
    //                    transanimation.setFillAfter(true);
    //                    transanimation.setDuration(1000);
    //                    transanimation.setInterpolator(new BounceInterpolator());
    //                    tempbutton.setVisibility(View.VISIBLE);
    //                    transanimation.setStartOffset(100);
    //                    tempbutton.setVisibility(View.VISIBLE);
    //                    tempbutton.startAnimation(transanimation);
    //                    added=true;
    //                }
    //            });
    //            while(!added){
    //                try {
    //                    Thread.sleep(5);
    //                } catch (InterruptedException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //            while (true) {
    //                if (tempbutton.getHeight() != 0) {
    //                    tempimageview = new ImageView(getContext());
    //                    tempimageview.setBackground(getContext().getDrawable(R.drawable.light_selector));
    //                    //tempimageview.setId(View.generateViewId());
    //                    tempimageview.setX(tempbutton.getX() + tempbutton.getWidth() / 2-getResources().getDimension(R.dimen.imageviewwidth)/2);
    //                    tempimageview.setY(tempbutton.getY() + tempbutton.getHeight() / 2-getResources().getDimension(R.dimen.imageviewwidth)/2-40);
    //                    //Log.d("addedY",String.valueOf(tempbutton.getY()));
    //                    //Log.d("addedY2",String.valueOf(tempimageview.getY()));
    //                    //tempimageview.setVisibility(View.GONE);
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.imageviewwidth), (int) getResources().getDimension(R.dimen.imageviewwidth));
    //                            layout.addView(tempimageview, 0, params2);
    //                            //Log.d("addedY2",String.valueOf(tempimageview.getY()));
    //                            Log.d("ButtonTrueID",String.valueOf(tempbutton.getId()));
    //                            Log.d("ImageTrueID",String.valueOf(tempimageview.getId()));
    //                            refreshing=false;
    //                        }
    //                    });
    //                    break;
    //                }
    //            }
    //            while(refreshing){
    //                try {
    //                    Thread.sleep(5);
    //                } catch (InterruptedException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //            index++;
    //            added=false;refreshing=true;
    //        }
//
//
    //        File file=new File(getContext().getFilesDir(),"apparatuscfgset");
    //        try {
    //            FileOutputStream out=new FileOutputStream(file);
    //            ObjectOutputStream objout = new ObjectOutputStream(out);
    //            objout.writeObject(Data.getApparatuses());
    //            objout.flush();
    //            out.close();
    //        } catch (FileNotFoundException e) {
    //            e.printStackTrace();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        Data.apparatuscfg temp;
    //        try {
    //            FileInputStream input=getContext().openFileInput("apparatuscfgset");
    //            ObjectInputStream objinput = new ObjectInputStream(input);
    //            ArrayList<Data.apparatuscfg> temparray =(ArrayList) objinput.readObject();
    //            temp=temparray.get(0);
    //        } catch (FileNotFoundException e) {
    //            e.printStackTrace();
    //        } catch (StreamCorruptedException e) {
    //            e.printStackTrace();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        } catch (ClassNotFoundException e) {
    //            e.printStackTrace();
    //        }
    //        //int id=2;
    //        //button = (Button)view.findViewById(id);
    //        ////imageview=(ImageView)view.findViewById(id);
    //        //handler.post(new Runnable() {
    //        //    @Override
    //        //    public void run() {
    //        //        button.setActivated(!button.isActivated());
    //        //        Log.d("theContentURL",(String) button.getContentDescription());
    //        //        //imageview.setX(view.getWidth()-100);imageview.setY(view.getHeight()-500);
    //        //        //button.setX(view.getWidth()-100);button.setY(view.getHeight()-500);
    //        //    }
    //        //});
//
//
    //        //RelativeLayout layout2 =(RelativeLayout)view;
    //        //Button tempbutton2 =new Button(getContext());
    //        //int id2 = View.generateViewId();
    //        //tempbutton2.setId(id);
    //        //Log.d("newID",String.valueOf(id));
    //        ////tempbutton.setBackground(getContext().getDrawable(R.drawable.animation_selector));
    //        ////tempbutton2.setVisibility(View.INVISIBLE);
    //        //tempbutton2.setActivated(true);tempbutton.setContentDescription("hello world");
    //        //tempbutton2.setY(550);//在这里设置了X或者Y，后面的Layoutparams涉及到X或者Y的部分的设置就无效了。
    //        //new RelativeLayout.LayoutParams((int)(getResources().getDimension(R.dimen.toggle_size)),(int)(getResources().getDimension(R.dimen.toggle_size_height)));
    //        //RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int)(getResources().getDimension(R.dimen.toggle_size)),(int)(getResources().getDimension(R.dimen.toggle_size_height)));
    //        ////layout.generateLayoutParams();//这个必须传入AttributeSet才可以。
    //        //params.addRule(RelativeLayout.BELOW,R.id.toggleButton);
    //        //params.addRule(RelativeLayout.ALIGN_LEFT,R.id.toggleButton);
    //        //layout.addView(tempbutton2,8,params);
    //    }
    //}

    //public class onclicklistener implements View.OnClickListener{
    //    ImageView imageview;
    //    onclicklistener (ImageView imageview){
    //        this.imageview=imageview;
    //    }
    //    public void onClick(View v) {
    //        Button toggle = (Button) v;
    //        //switch (v.getId()) {
    //        //    case R.id.toggleButton:new communication(toggle,imageview1).start();imageview1.setVisibility(View.VISIBLE);break;
    //        //    case R.id.toggleButton2:new communication(toggle,imageview2).start();imageview2.setVisibility(View.VISIBLE);break;
    //        //    case R.id.toggleButton3:new communication(toggle,imageview3).start();imageview3.setVisibility(View.VISIBLE);break;
    //        //}
    //        new communication(toggle,imageview).start();imageview.setVisibility(View.VISIBLE);
    //        if (toggle.isActivated()) {
    //            toggle.setBackground(getResources().getDrawable(R.drawable.light_on_connecting,null));
    //        }
    //        else {
    //            toggle.setBackground(getResources().getDrawable(R.drawable.light_off_connecting,null));
    //        }
    //        toggle.setClickable(false);
    //        //Toast toast = Toast.makeText(MainActivity.this,"Connecting",Toast.LENGTH_SHORT);
    //        //toast.setGravity(Gravity.BOTTOM,0,0);
    //        //toast.show();
    //        //toggle.setClickable(false);
    //    }
    //}



    //public class communication extends Thread{
    //    Button togglebutton;
    //    int trialtimes;
    //    ImageView imageview;
    //    final Runnable runnable_success = new Runnable() {
    //        @Override
    //        public void run() {
    //            Log.d("connection","success");
    //            togglebutton.setBackground(view.getResources().getDrawable(R.drawable.animation_selector,null));
    //            togglebutton.setActivated(!togglebutton.isActivated());
    //            imageview.setActivated(!imageview.isActivated());
    //            Snackbar.make(view, "Connection SUCCESSFUL", Snackbar.LENGTH_LONG)
    //                    .setAction("Action", null).show();
    //            togglebutton.setClickable(true);
    //        }
    //    };
    //    final Runnable runnable_failure = new Runnable() {
    //        @Override
    //        public void run() {
    //            Log.d("connection","failure");
    //            Snackbar.make(view, "Connection FailedApparatus", Snackbar.LENGTH_SHORT)
    //                    .setAction("Action", null).show();
    //            if (!(togglebutton.getBackground()==getResources().getDrawable(R.drawable.connecting,null))){
    //                togglebutton.setBackground(getResources().getDrawable(R.drawable.connecting,null));
    //            }
    //            if (!(((AnimationDrawable)togglebutton.getBackground()).isRunning())){
    //                ((AnimationDrawable)togglebutton.getBackground()).start();
    //            }
    //            togglebutton.setClickable(false);
    //        }
    //    };
    //    final Runnable runnable_restore = new Runnable() {
    //        @Override
    //        public void run() {
    //            if (togglebutton.isActivated()){
    //                togglebutton.setBackground(getResources().getDrawable(R.drawable.light_on,null));
    //            }
    //            else {
    //                togglebutton.setBackground(getResources().getDrawable(R.drawable.light_off,null));
    //            }
    //            togglebutton.setClickable(true);
    //        }
    //    };
    //    public void run() {
    //        OkHttpClient client = new OkHttpClient();
    //        Request request;
    //        Log.d("ON is ON","Hello");
    //        if (!togglebutton.isActivated()){
    //            request = new Request.Builder().url(url+"1").get().build();
    //            if (togglebutton.getId()==R.id.toggleButton2){
    //                request = new Request.Builder().url("http://www.baidu.com").get().build();
    //            }
    //            else if (togglebutton.getId()==R.id.toggleButton3){
    //                request = new Request.Builder().url("http://www.baidu.com").get().build();
    //            }
    //            client.newCall(request).enqueue(new Callback() {
    //                @Override
    //                public void onFailure(Call call, IOException e) {
    //                    /*Message msg = new Message();
    //                    Log.d("The Connection is", "FailedApparatus");
    //                    msg.what = FAILURE_STATUS;
    //                    msg.obj = togglebutton;
    //                    handler.sendMessage(msg);*/
    //                    handler.post(runnable_failure);
    //                    if(trialtimes <=5){
    //                        Log.d("trialtimes IS ",String.valueOf(trialtimes));
    //                        trialtimes++;
    //                        run();}
    //                    else {
    //                        /*msg = new Message();
    //                        msg.what = RESTORE;
    //                        msg.obj = togglebutton;
    //                        handler.sendMessage(msg);*/
    //                        handler.post(runnable_restore);
    //                    }
    //                    call.cancel();
    //                }
    //                @Override
    //                public void onResponse(Call call, Response response) throws IOException {
    //                    Message msg = new Message();
    //                    if (response.isSuccessful()){
    //                        /*msg.obj = togglebutton;
    //                        msg.what = SUCCESS_STATUS;
    //                        boolean TemperatureColorset = togglebutton.isActivated();
    //                        Log.d("ISCHECKED", String.valueOf(TemperatureColorset));
    //                        handler.sendMessage(msg);*/
    //                        handler.post(runnable_success);
    //                        response.close();
    //                        //trialtimes=6;
    //                    }
    //                    call.cancel();
    //                }
    //            });
    //        }
    //        else {
    //            request = new Request.Builder().url(url+"2").get().build();
    //            if (togglebutton.getId()==R.id.toggleButton2){
    //                request = new Request.Builder().url("http://www.baidu.com").get().build();
    //            }
    //            client.newCall(request).enqueue(new Callback() {
    //                @Override
    //                public void onFailure(Call call, IOException e) {
    //                    /*Message msg = new Message();
    //                    Log.d("The Connection is","FailedApparatus");
    //                    msg.what = FAILURE_STATUS;
    //                    msg.obj = togglebutton;
    //                    handler.sendMessage(msg);*/
    //                    handler.post(runnable_failure);
    //                    if(trialtimes <=5){
    //                        Log.d("trialtimes IS ",String.valueOf(trialtimes));
    //                        trialtimes++;
    //                        run();}
    //                    else {
    //                        /*msg = new Message();
    //                        msg.what = RESTORE;
    //                        msg.obj = togglebutton;
    //                        handler.sendMessage(msg);*/
    //                        handler.post(runnable_restore);
    //                    }
    //                    call.cancel();
    //                }
    //                @Override
    //                public void onResponse(Call call, Response response) throws IOException {
    //                    Message msg = new Message();
    //                    if (response.isSuccessful()){
    //                        /*msg.obj = togglebutton;
    //                        msg.what = SUCCESS_STATUS;
    //                        boolean TemperatureColorset = togglebutton.isActivated();
    //                        Log.d("ISCHECKED", String.valueOf(TemperatureColorset));
    //                        handler.sendMessage(msg);*/
    //                        handler.post(runnable_success);
    //                        response.close();
    //                        //trialtimes=6;
    //                    }
    //                    call.cancel();
    //                }
    //            });
    //        }
    //    }
    //    communication (Button toggletbutton,ImageView imageview){
    //        this.togglebutton=toggletbutton;
    //        this.imageview = imageview;
    //    }
    //}


//静态内部类其实如果里面没有静态变量或者方法的话，其实就是把这个类的空壳（名字），放到公共寄存区去了。
//而其他的非静态方法和变量则还需要通过new来实例化放到引用寄存区，其实这个类里面如果没有静态变量和方法的话，
// 写成Static只是可以让它在它的外部类没有被实例化之前就把自己的名字存在公共寄存区，可以被大家访问来实例化它的
//非静态部分，进而可以实际使用它。（否则只能先实例化它的外部类，再用实例化后的外部类去访问它，以实例化它（非静态部分）。）
    static class MyHandler extends Handler{
        private final  WeakReference<Fragment_1> mActivity;

        MyHandler(Fragment_1 fragment) {
// TODO Auto-generated constructor stub
            mActivity=new WeakReference<>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {

        }
    }

}
