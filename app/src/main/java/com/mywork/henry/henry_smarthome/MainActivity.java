package com.mywork.henry.henry_smarthome;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    Button button1;
    Button button2;
    Button tempbutton;
    Handler handler;
    viewwrapper buttonwrap1;
    viewwrapper buttonwrap2;
    FloatingActionButton fab;
    public int originalwidth ;
    public int originalheight;
    public float Ylocation;
    public final float DeltaY=5;
    AnimatorSet animatorset1;
    AnimatorSet animatorset2;
    public float fab_margin ;
    public float button2endX;
    final int targetHeight = 150;//135
    final int targetWidth = 320;
    float originalX;
    float interval=20;
    //static volatile boolean stopall=false;
    final long duration2 =500;
    final long duration1 = duration2 + 800;
    final long duration3 =50;final int times=11;
    final long durationfragment=500;
    volatile boolean interanimating =false;
    public static final float viewDelta = (float) -0.1;
    public final int isFAB=1;
    public final int isMain=2;
    public Refreshprogressbar progressbar;
    public float originalPY;float refreshEndY;
    public final int Maxprogress=120;
    volatile boolean isbuttonYseted;
    Context Maincontext;
    MainActivity Mainactivity;
    Fragment_1 fragment1;
    volatile boolean refreshinganirunning=false;
    //volatile boolean refreshing=false;
    //volatile boolean added=false;
    public static volatile boolean StopGettingData=false;
    onDemandRefresh refreshthread=new onDemandRefresh(null);
    public static volatile File fileDir;
    public volatile boolean StopDaemon=false;
    String URL="http://168.150.116.167:8080/Smart_Home/Inquire";
    String URL2="http://168.150.116.167:8080/Smart_Home/InquireData";
    String URL3="http://168.150.116.167:8080/Smart_Home/Control";
    String URL4="http://168.150.116.167:8080/Smart_Home/RegistrationPlusStateupdate";
    updatedkeeper UpdateKeeper = null;
    public static volatile boolean onDemandRefreshing=false;
    final ArrayList<Integer> ApparatusBeingControlled =new ArrayList<>(9);
    final ArrayList<CalledTogglesData> calledTogglesDatas=new ArrayList<>(9);
    onclicklistener onclicklistener =new onclicklistener();
    volatile boolean refreshDescendDone=true;
    volatile boolean refreshReturnRunning=false;
    final int sleeptime=10000;final int autorestoretime=20000;
    volatile boolean StopControlling=false;
    final ArrayList<Integer> AllAddedToggles = new ArrayList<>(9);
    public static volatile boolean whetherTooMany=false;

    public static final int ColorAlerted = 0xffff0000;
    public static final int ColorActivated=0xfffbab00;
    public static final int ColorInactivated=0xff756b64;
    volatile boolean StopRefreshView=false;
    final ArrayList<ImageView> Prompts= new ArrayList<>(3);
    final int prompt_changed_ID=5001;
    final int prompt_new_ID=5002;
    final int prompt_removed_ID=5003;
    final RefreshViews refreshViews=new RefreshViews();

    ValueAnimator SwipingButtonsAnimator;
    volatile boolean ButtonsReturned = true;
    ButtonAnimatorUpdateListener updatelistener;
    volatile boolean AnimatorStarted=true;

    ValueAnimator MainAddButtonAnimator;
    MainAddButtonAnimatorUpdateListener MainAddButtonAnimatorupdatelistener;
    volatile boolean MainAddButtonReturned = true;

    Button addbutton;
    AddButtonListener addbuttonlistener;
    float MainAddButtonOriginalX;
    float MainAddButtonOriginalYDelta;
    int MainAddButtonOriginalWidth;
    int MainAddButtonOriginalHeight;
    viewwrapper addbuttonwrap;
    DialogFragment AddDialog;

    Toolbar toolbar;
    Menu ActivityMenu;ActionMenuView ActivityMenuView;
    final ArrayList<View> toolbarViews = new ArrayList<>(8);
    MenuItemOnTouch Menu_SettingsOnTouch;
    MenuItemOnTouch Menu_RefreshOnTouch;
    public static final TypedValue colorPrimary=new TypedValue();

    final AddPromptsRunnable add_prompts = new AddPromptsRunnable();

    TextView ApplicationTitle;
    AlertButtonListener alertbuttonlistener;DialogFragment ClearDialog;
    TextView AlertPrompt;    volatile int AlertedLogNumber;
    MenuItem DisconnectionPrompt;View DisconnectionPromptView;volatile boolean Disconnected=false;
    //volatile int trytimes=0;

    volatile boolean toStartOtherActivity=false;

    AnimatorSet progressbarReturnAnimator;
    AnimatorSet progressbarDescendAnimator;
    refreshingani refreshinganiThread;

    GestureDetector gesturedetector = new GestureDetector(null,new gesturelistner());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().getDecorView();获得Activity的view。
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        ApplicationTitle=(TextView)findViewById(R.id.Title_mainactivity);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        addbutton = (Button)findViewById(R.id.MainAddButton);
        buttonwrap1 = new viewwrapper(button1);
        buttonwrap2 = new viewwrapper(button2);
        addbuttonwrap = new viewwrapper(addbutton);
        AddDialog=new Registration();
        ClearDialog=new CurrentAlertedLogClean();
        handler = new Handler();
        //用View就可以了其实不用转型
        CoordinatorLayout main = (CoordinatorLayout)findViewById(R.id.activity_main);
        main.setOnTouchListener(new mainactivityontouch());
        addbuttonlistener=new AddButtonListener();
        alertbuttonlistener =new AlertButtonListener();
        Menu_RefreshOnTouch=new MenuItemOnTouch(false,new Data.whattodonext() {
            @Override
            public void todo() {
                refreshthread.interrupt();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean enabled=false;
                        synchronized (toolbarViews){
                            for (View temp:toolbarViews){
                                if (((ActionMenuItemView) temp).getItemData().toString().equals(getString(R.string.menu_settings))){
                                    enabled=temp.isEnabled();
                                }
                            }
                        }
                        if (!refreshthread.isAlive()&&enabled) {
                            progressbarDescend(progressbar);
                            refreshthread = new onDemandRefresh(fragment1);
                            refreshthread.start();
                        }else if (refreshthread.isInterrupted()||!refreshthread.isAlive()){
                            handler.postDelayed(this,5);
                        }
                    }
                }, 10);
            }
        });
        Menu_SettingsOnTouch=new MenuItemOnTouch(true,new Data.whattodonext() {
            @Override
            public void todo() {
                Intent intent = new Intent(MainActivity.this,Settings.class);
                intent.putExtra("Disconnected",Disconnected);
                intent.putExtra("AlertedLogNumber", AlertedLogNumber);
                toStartOtherActivity=true;
                intent.putExtra("toStartOtherActivity",toStartOtherActivity);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
            }
        });
        //addbutton.setOnClickListener(addbuttonlistener);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnTouchListener(new fabontouch() );
        progressbar = (Refreshprogressbar) findViewById(R.id.progressBar);
        Maincontext=this;
        Mainactivity=this;
        AlertedLogNumber =getIntent().getIntExtra("AlertedLogNumber",0);
        Disconnected=getIntent().getBooleanExtra("Disconnected",false);
        toStartOtherActivity=getIntent().getBooleanExtra("toStartOtherActivity",false);
        //Log.d("TemperatureBinary",Integer.toBinaryString(Data.Type_Temperature));
        //Log.d("HumidityBinary",Integer.toBinaryString(Data.Type_Humidity));
        //Log.d("ElectricityBinary",Integer.toBinaryString(Data.Type_Electricity));
        //Log.d("ApparatusBinary",Integer.toBinaryString(Data.Type_apparatuscfgset));
        //progressbar.setlistener
        //button1.setOnTouchListener(new buttonontouch());
        //button2.setOnTouchListener(new buttonontouch());
        setdefaultfragment();

    }


    void initiateToolbar (final Toolbar toolbar){
        TextView Title = null;
        for (int i=0;i<toolbar.getChildCount();i++){
            //Log.d("ToolbarClass",toolbar.getChildAt(i).getClass().toString());
            final View view = toolbar.getChildAt(i);
            if (view instanceof TextView){
                //Log.d("ToolbarText",((TextView) toolbar.getChildAt(i)).getText().toString());
                //Log.d("ToolbarTextScale",String.valueOf(toolbar.getChildAt(i).getWidth())+"w:h"+String.valueOf(toolbar.getChildAt(i).getHeight()));
                Title=(TextView) view;
            }else if (view instanceof ImageButton) {
                //toolbar.getChildAt(i).setVisibility(View.INVISIBLE);
                //Log.d("Toolbar"+toolbar.getChildAt(i).getClass().toString()+"Scale",String.valueOf(toolbar.getChildAt(i).getWidth())+"w:h"+String.valueOf(toolbar.getChildAt(i).getHeight()));
                if (Title==null){
                    i=-1;
                }else {
                    final TextView temp=Title;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (view.getHeight()!=0){
                                view.setY(temp.getY()+(temp.getHeight()-view.getHeight())/2);
                                //Log.d("ToolbarActionMenuView",String.valueOf(view.getWidth())+"w:h"+String.valueOf(view.getHeight()));
                            }else{
                                handler.postDelayed(this,5);
                            }
                        }
                    },10);
                }
            }else if (view instanceof ActionMenuView){
                ActivityMenuView=(ActionMenuView) view;
                if (Title==null){
                    i=-1;
                }else {

                    final TextView temp=Title;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (view.getHeight()!=0){
                                view.setY(temp.getY()+(temp.getHeight()-view.getHeight())/2);
                                //Log.d("Toolbar",((ActionMenuItemView)((ActionMenuView)view).getChildAt(1)).getText().toString());
                                //Log.d("ToolbarActionMenuView",String.valueOf(view.getWidth())+"w:h"+String.valueOf(view.getHeight()));

                                //Log.d("Toolbar",String.valueOf(((ActionMenuView)view).getChildCount()));

                            }else{
                                handler.postDelayed(this,5);
                            }
                        }
                    },10);
                }
            }
        }

        resetMenuItemStatus();
        resetMenuItemOnTouch();

        //for (int i=0;i<menu.size();i++){
        //
        //    MenuItem item = menu.findItem(R.id.Menu_Settings);
        //    if (item!=null) {
        //        //用这个得到view需要程序以及activity的theme属于Theme.AppCompat,然后在menu.xml里设置item 的app:actionViewClass 才行。
        //        View view = MenuItemCompat.getActionView(item);
        //        //Log.d("ToolbarMenuClass",(view.getClass().toString()));
        //        //view.setBackground(getDrawable(R.drawable.transparent));
        //    }
        //
        //
        //}
    }

    void initiateCriticalSigns (final Toolbar toolbar){
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,toolbar.getHeight());
        layoutparams.addRule(RelativeLayout.CENTER_IN_PARENT);//这个不好用。
        //View view = getLayoutInflater().inflate(R.layout.toolbar_notification,toolbar);//这个可以直接把layout XML注入后面的GroupView.
        //Log.d("InflatedView",view.toString()+":"+view.getClass().toString());
        final View view = getLayoutInflater().inflate(R.layout.toolbar_notification,null);
        Log.d("DecorView",getWindow().getDecorView().toString());
        toolbar.addView(view,-1,layoutparams);//view.setZ(10);view.bringToFront();//
        //view.setVisibility(View.INVISIBLE);
        //CriticalPrompts = view;
        final TextView Alert = (TextView) toolbar.findViewById(R.id.Alert);
        Alert.setOnClickListener(alertbuttonlistener);
        //Alert.setText("903");
        //Alert.setLayoutParams(new RelativeLayout.LayoutParams(toolbar.getHeight(),toolbar.getHeight()));
        Alert.setVisibility(View.INVISIBLE);
        //Disconnection.getLayoutParams().height=toolbar.getHeight()-20;Disconnection.getLayoutParams().width=toolbar.getHeight()-20;
        //Disconnection.requestLayout();
        final View Title = ApplicationTitle;
        handler.postDelayed(new Runnable() {
            int trytimes=0;
            @Override
            public void run() {
                if (Title.getWidth()!=0&&view.getWidth()!=0) {
                    //Alert.setTextSize(toolbar.getHeight()/8);
                    view.setX(Title.getX() + (Title.getWidth() - view.getWidth()) / 2);
                    AlertPrompt=Alert;
                }else {
                    if (trytimes<100)
                        handler.postDelayed(this,5);
                    trytimes++;
                }
            }
        }, 10);
    }

    void resetMenuItemStatus(){
        synchronized (toolbarViews){
            for (View temp:toolbarViews){
                ActionMenuItemView real = (ActionMenuItemView)temp;
                real.clearAnimation();real.setChecked(false);real.setActivated(false);real.setPressed(false);real.setOnTouchListener(null);
            }
        }
    }

    void resetMenuItemOnTouch (){
        synchronized (toolbarViews){
            toolbarViews.clear();
            for (int ii=0;ii<ActivityMenuView.getChildCount();ii++){
                View temp = ActivityMenuView.getChildAt(ii);
                if (temp!=null){
                    toolbarViews.add(temp);
                }

            }
            if (!toolbarViews.isEmpty()){
                for (View temp:toolbarViews){
                    //Log.d("ToolBarViews",temp.toString());
                    ActionMenuItemView real = (ActionMenuItemView)temp;
                    real.clearAnimation();real.setChecked(false);real.setActivated(false);real.setPressed(false);real.setOnTouchListener(null);
                    if (real.getItemData().toString().equals(getString(R.string.menu_settings))){
                        //Log.d("Settings","AttachListener");
                        real.setOnTouchListener(Menu_SettingsOnTouch);
                    }else if (real.getItemData().toString().equals(getString(R.string.menu_refresh))){
                        //Log.d("Refresh","AttachListener");
                        real.setOnTouchListener(Menu_RefreshOnTouch);
                        //real.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotation_endless));
                    }
                    //else if (real.getItemData().toString().equals(getString(R.string.hasdisconnected))){
                    //    //TextView
                    //    DisconnectionPromptView=real;
                    //    DisconnectionPromptView.setVisibility(View.INVISIBLE);
                    //
                    //}
                }
            }else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetMenuItemOnTouch();
                    }
                },10);
            }
        }
    }

    private class MenuItemOnTouch implements View.OnTouchListener {
        private final Data.whattodonext whattodonext;
        private final boolean whethertoClear;
        long nowup;long nowdown;long time=2000;
        boolean longpressed=false;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    longpressed=false;
                    nowdown = System.currentTimeMillis();//SystemClock.sleep();
                    v.setActivated(true);v.setPressed(true);
                    v.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotation_endless));
                    if (System.currentTimeMillis()-nowdown>time) {

                        if (!longpressed)
                            //((ActionMenuItemView)v).onLongClick(v);
                            v.performLongClick();
                        longpressed = true;
                    }
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_MOVE:
                    //if (event.getRawX()<v.getX()-100||event.getRawX()>v.getX()+v.getWidth()+100||
                    //        event.getRawY()<v.getY()-100||event.getRawY()>v.getY()+v.getHeight()+100){
                    //
                    //}else {
                    //    break;
                    //}
                    //Log.d("Raw",event.getRawX()+":"+event.getRawY());
                    //Log.d("RawX,Y,Width,Height",toolbar.getX()+","+toolbar.getY()+","+toolbar.getWidth()+","+toolbar.getHeight());
                    //Log.d("DistenceofTimes",System.currentTimeMillis()-nowdown+"");
                    if (System.currentTimeMillis()-nowdown>time) {

                        if (!longpressed)
                            //((ActionMenuItemView)v).onLongClick(v);
                            v.performLongClick();
                        longpressed = true;
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (whethertoClear)
                        v.clearAnimation();
                    v.setActivated(false);v.setPressed(false);
                    v.performClick();
                    whattodonext.todo();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    //if(whethertoClear)
                    //    v.clearAnimation();
                    //else
                    //    if (longpressed)
                            v.clearAnimation();

                    v.setActivated(false);
                    v.setPressed(false);
                    break;
                //default:Log.d("MotionType",event.getAction()+"");v.clearAnimation();break;
            }
            return true;
        }

        MenuItemOnTouch (boolean whethertoClear,Data.whattodonext whattodonext){
            this.whattodonext = whattodonext;
            this.whethertoClear=whethertoClear;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","Paused");
        if (toStartOtherActivity) {
            ApplicationTitle.setVisibility(View.INVISIBLE);
            if (AlertPrompt != null)
                AlertPrompt.setVisibility(View.INVISIBLE);
            if (DisconnectionPrompt != null) {
                resetMenuItemStatus();
                DisconnectionPrompt.setVisible(false);
                resetMenuItemOnTouch();
                
            }
        }
    }

    @Override
    protected void onResume() {
        //if (getIntent().getStringExtra("Intention")!=null&&getIntent().getStringExtra("Intention").equals("StartMainActivity")) {
        //    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        //}else {
        //    overridePendingTransition(0, R.anim.activity_exit);
        //}
        super.onResume();
        Log.d("MainActivity","Resumed");

        int trytimes=0;
        while (trytimes<200){
            if (UpdateKeeper==null||!UpdateKeeper.isAlive()){
                UpdateKeeper=new updatedkeeper(sleeptime,autorestoretime);
                UpdateKeeper.setDaemon(true);
                UpdateKeeper.setName("UpdateKeeper");
                StopDaemon=false;
                UpdateKeeper.start();
                break;
            }
            trytimes++;
        }
        //long duration = AnimationUtils.loadAnimation(this,R.anim.rotation_endless).getDuration();
        //
        //ApplicationTitle.setVisibility(View.INVISIBLE);//不过好像MainActivity不需要这样。但前提是，在onPause或onStop里设置它为INVISIBLE。
        ////if (CriticalPrompts!=null)
        ////    CriticalPrompts.setVisibility(View.INVISIBLE);
        //handler.postDelayed(new Runnable() {
        //    @Override
        //    public void run() {
        //        ApplicationTitle.setVisibility(View.VISIBLE);
        //        if (CriticalPrompts!=null)
        //            CriticalPrompts.setVisibility(View.VISIBLE);
        //    }
        //},duration);
        if (toStartOtherActivity) {
            ApplicationTitle.setVisibility(View.INVISIBLE);
            if (AlertPrompt != null)
                AlertPrompt.setVisibility(View.INVISIBLE);
            if (DisconnectionPrompt != null) {
                resetMenuItemStatus();
                DisconnectionPrompt.setVisible(false);
                resetMenuItemOnTouch();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (AlertPrompt != null && DisconnectionPrompt != null) {
                        ApplicationTitle.setVisibility(View.VISIBLE);
                        AlertPrompt.setVisibility(AlertedLogNumber > 0 ? View.VISIBLE : View.INVISIBLE);
                        AlertPrompt.setText(String.valueOf(AlertedLogNumber));
                        resetMenuItemStatus();
                        DisconnectionPrompt.setVisible(Disconnected);
                        resetMenuItemOnTouch();
                    } else {
                        handler.postDelayed(this, 50);
                    }
                }
            }, 350);
        }

        toStartOtherActivity=false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity","Restarted");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity","Stopped");
        StopDaemon = true;
        if (UpdateKeeper != null) {
            UpdateKeeper.interrupt();
        }
        StopGettingData = true;
        if (refreshthread != null) {
            refreshthread.interrupt();
        }
        StopRefreshView = true;
    }

    class AddButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Fragment temp = fm.findFragmentByTag("AddDialog");
            if (temp !=null){
                fm.beginTransaction().remove(temp).commit();
            }
            Bundle arguments = new Bundle();
            arguments.putString("RegistrationURL",URL4);
            arguments.putString("user","henry");
            arguments.putString("pass","yiweigang");
            if (!AddDialog.isAdded()) {
                AddDialog.setArguments(arguments);
                AddDialog.show(getFragmentManager(), "AddDialog");
            }
        }
    }

    class AlertButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Fragment temp = fm.findFragmentByTag("ClearDialog");
            if (temp !=null){
                fm.beginTransaction().remove(temp).commit();
            }
            Bundle arguments = new Bundle();
            arguments.putString("URL",URL4);
            arguments.putString("user","henry");
            arguments.putString("pass","yiweigang");
            if (!ClearDialog.isAdded()) {
                ClearDialog.setArguments(arguments);
                ClearDialog.show(getFragmentManager(), "ClearDialog");
            }
        }
    }

    public class mainactivityontouch implements View.OnTouchListener{
        Fragment fragment1;
        Fragment fragment2;
        float usedX;float usedY;
        float Xdistance=-1;float distancetempX=0;
        float Ydistance=-1;float distancetempY=0;
        boolean refreshing=false;
        boolean moving=false;
        //float startv1X;float startv2X;float startpY;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            fragment1 = fm.findFragmentByTag("fragment1");
            fragment2 = fm.findFragmentByTag("fragment2");
            View view1 = fragment1.getView();
            View view2 = fragment2.getView();
            float with = getResources().getDisplayMetrics().widthPixels;
            //refreshEndY=originalPY+progressbar.getHeight()+Maxprogress;

            //Log.d("IDis",String.valueOf(R.id.my_view));
            if (event!=null) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    if (!(view1.getX()==0) &&!(view1.getX()==-with)) {
                        moving=true;//stopall=true;
                    }

                    usedX = event.getX();
                    usedY = event.getY();
                    Xdistance = 0;
                    Ydistance = 0;
                    refreshing=false;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!(view1.getX()==0) &&!(view1.getX()==-with)) {
                        moving=true;//stopall=true;
                    }
                    Ydistance = Ydistance + event.getY() - usedY;
                    Xdistance = Xdistance + event.getX() - usedX;
                    distancetempX = event.getX() - usedX;
                    distancetempY = event.getY() - usedY;
                    usedX = event.getX();
                    usedY = event.getY();
                    if (Ydistance>Math.abs(Xdistance)+10){

                        refreshing=true;
                    }
                    if (refreshing&&!refreshinganirunning){
                        //Log.d("WHATREFRESH",String.valueOf(originalPY+progressbar.getHeight()+100));
                        if (progressbar.getY()+distancetempY<refreshEndY
                                &&progressbar.getY()+distancetempY>originalPY) {
                            progressbar.setY(progressbar.getY() + distancetempY);
                            progressbar.setProgress((int)(progressbar.getY()-refreshEndY+Maxprogress));

                        }
                        else if (progressbar.getY()+distancetempY>=refreshEndY){
                            progressbar.setY(refreshEndY);
                            progressbar.setProgress(progressbar.getMax());
                        }


                    }
                    else if (!refreshing){
                        if (Math.abs(Xdistance)>10) {
                            if (event.getX() > 0 && event.getX() < getResources().getDisplayMetrics().widthPixels) {
                                if (view2.getX() + distancetempX <= getResources().getDisplayMetrics().widthPixels+viewDelta
                                        && view2.getX() + distancetempX >= 0+viewDelta) {
                                    view2.setX(view2.getX() + distancetempX);
                                }
                                if (view1.getX() + distancetempX >= -getResources().getDisplayMetrics().widthPixels
                                        && view1.getX() + distancetempX <= 0) {
                                    view1.setX(view1.getX() + distancetempX);
                                }
                            }
                        }
                    }
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP ){
                if(refreshing&&!refreshinganirunning){
                    if (progressbar.getY()+distancetempY>=refreshEndY||progressbar.getY()>=refreshEndY){

                        StopGettingData=true;
                        refreshthread.interrupt();
                        try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
                        refreshthread=new onDemandRefresh(fragment1);
                        refreshthread.start();
                        progressbar.setProgress(progressbar.getMax());
                    }
                    else {
                        progressbarReturn(progressbar, 2);
                    }
                }
                if (view1.getX() > -getResources().getDisplayMetrics().widthPixels / 2) {
                    if (!(view1.getX()==0) &&!(view1.getX()==-with)) {
                        //stopall = false;
                        fragmentanimation(1);
                        //new animationset(fab, button1, button2).start();
                        AnimateButtonsForACycle(true,updatelistener_Full,false);
                    }
                    else if (moving){
                        //buttonexpansion(fab,2,isMain);
                        AnimateButtonsForACycle(null,updatelistener_Retraction,false);
                    }
                } else if (view1.getX() < -getResources().getDisplayMetrics().widthPixels / 2) {
                    if (!(view1.getX()==0) &&!(view1.getX()==-with)) {
                        //stopall = false;
                        fragmentanimation(2);
                        //new animationset(fab, button2, button1).start();
                        AnimateButtonsForACycle(false,updatelistener_Full,false);
                    }
                    else if (moving){
                        //buttonexpansion(fab,2,isMain);
                        AnimateButtonsForACycle(null,updatelistener_Retraction,false);
                    }
                }
                moving=false;
                Xdistance = 0;
                Ydistance = 0;
                refreshing= false;
            }
            //这里返回true的话，就是触摸事件的所有信息都会交给触摸的控件的监听器来处理，而返回false则只会在ACTION_DOWN（也就是第一个触摸事件）时交给这里处理，
            //而剩下的其他的触摸事件（比如ACTION_MOVE/ACTION_UP）则不会交给控件绑定的监听器来处理，而是传递给控件下层的控件的监听器来处理。
            //return gesturedetector.onTouchEvent(event);
            return true;
        }

    }

    private class onDemandRefresh extends Thread{
        Fragment fragment;
        public void run() {
            onDemandRefreshing=true;
            handler.post(new Runnable() {
                int trytimes=0;
                @Override
                public void run() {
                    synchronized (toolbarViews){
                        ActionMenuItemView view1=null;
                        ActionMenuItemView view2=null;
                        for (View temp : toolbarViews) {
                            //Log.d("ItemData",((ActionMenuItemView) temp).getItemData().toString());
                            if (((ActionMenuItemView) temp).getItemData().toString().equals(getString(R.string.menu_refresh))) {
                                view1 = (ActionMenuItemView) temp;
                            }else if (((ActionMenuItemView) temp).getItemData().toString().equals(getString(R.string.menu_settings))){
                                view2 = (ActionMenuItemView)temp;
                            }
                        }
                        if (view1!=null&&view2!=null){
                            view1.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotation_endless));
                            view1.setEnabled(false);
                            view2.setEnabled(false);
                            view2.clearAnimation();
                        }else if (trytimes<100){
                            handler.postDelayed(this,5);
                            trytimes++;
                        }
                    }
                }
            });
            Log.d("onDemandRefreshing","Started");
            StopGettingData=false;
            refreshinganirunning=true;
            refreshReturnRunning=false;
            if (UpdateKeeper!=null){
            UpdateKeeper.interrupt();}
            if (!Data.getApparatuses().isEmpty()){
                setAlertedLogNumber(Data.getApparatuses().get(0).AlertedLogNumber);
            }
            while (!refreshDescendDone&&progressbar.getProgress()!=progressbar.getMax()){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            if (refreshinganiThread!=null){
                refreshinganiThread.interrupt();
            }
            while (true){
                if (refreshinganiThread==null||!refreshinganiThread.isAlive()){
                    refreshinganiThread=new refreshingani(800);//变量需要大于Maxprogress（现在是120）
                    refreshinganiThread.start();
                    break;
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }

            int status=refresh(URL,URL2);int tempstatus=status;

            switch (status){
                case -1:
                    Log.d("HasbeenForcedlyClosed","beenclosed");
                    StopRefreshView=true;
                    break;
                case 15:
                    Snackbar prompt = Snackbar.make(fragment.getView(), R.string.DataFetch_SUCCESSFUL, Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    prompt.getView().setBackgroundColor(colorPrimary.data);
                    prompt.show();
                    if (Thread.currentThread().isInterrupted()){break;}
                    refreshViews.run();
                    break;
                default:String msg="";FileInputStream input1=null;FileInputStream input2=null;
                    FileInputStream input2C=null;FileInputStream input3C=null;
                    FileInputStream input3=null;FileInputStream input4=null;
                    status=0;
                    if ((tempstatus&1)==0){
                        try {
                            input4=openFileInput("Electricityset");
                        } catch (FileNotFoundException e) {
                            Log.d("NoFile","Electricityset");
                            status|=Data.Type_Electricity;
                        }
                        msg+=getString(R.string.electricity)+"-";
                    }
                    tempstatus=tempstatus>>1;
                    if ((tempstatus&1)==0){
                        try {
                            input3=openFileInput("Humidityset");
                        } catch (FileNotFoundException e) {
                            Log.d("NoFile","Humidityset");
                            status|=Data.Type_Humidity;
                        }
                        try {
                            input3C=openFileInput("HumidityColorSet");
                        } catch (FileNotFoundException e) {
                            Log.d("NoFile","HumidityColorSet");
                        }
                        msg+=getString(R.string.humidity)+"-";
                    }
                    tempstatus=tempstatus>>1;
                    if ((tempstatus&1)==0){
                        try {
                            input2=openFileInput("Temperatureset");
                        } catch (FileNotFoundException e) {
                            Log.d("NoFile","Temperatureset");
                            status|=Data.Type_Temperature;
                        }
                        try {
                            input2C=openFileInput("TemperatureColorSet");
                        } catch (FileNotFoundException e) {
                            Log.d("NoFile","TemperatureColorSet");
                        }
                        msg+=getString(R.string.temperature)+"-";
                    }
                    tempstatus=tempstatus>>1;
                    if ((tempstatus&1)==0){
                        try {
                            input1=openFileInput("Apparatusset");
                        } catch (FileNotFoundException e) {
                            Log.d("NoFile","Apparatusset");
                            status|=Data.Type_apparatuscfgset;
                        }
                        msg+=getString(R.string.apparatus)+"-";
                    }else {
                        refreshViews.run();
                    }
                    Snackbar prompt2 = Snackbar.make(fragment.getView(), msg+getString(R.string.DataFetch_Failed_Try_Using_Backup_Data), Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    prompt2.getView().setBackgroundColor(colorPrimary.data);
                    prompt2.show();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    try {
                        ObjectInputStream objinput;
                        if (Thread.currentThread().isInterrupted()){break;}
                        if (input1!=null){
                            objinput = new ObjectInputStream(input1);
                            Data.setData(Data.Type_apparatuscfgset,(ArrayList) objinput.readObject());
                            objinput.close();input1.close();refreshViews.run();}
                        if (Thread.currentThread().isInterrupted()){break;}
                        if (input2!=null){
                            objinput = new ObjectInputStream(input2);
                            Data.setData(Data.Type_Temperature,(ArrayList) objinput.readObject());
                            objinput.close();input2.close();}
                        if (Thread.currentThread().isInterrupted()){break;}
                        if (input2C!=null){
                            objinput = new ObjectInputStream(input2C);
                            Data.setColorset(Data.Type_Temperature,(ArrayList<Integer>)objinput.readObject(),null);
                            objinput.close();input2C.close();}
                        if (Thread.currentThread().isInterrupted()){break;}
                        if (input3!=null){
                            objinput = new ObjectInputStream(input3);
                            Data.setData(Data.Type_Humidity,(ArrayList) objinput.readObject());
                            objinput.close();input3.close();}
                        if (Thread.currentThread().isInterrupted()){break;}
                        if (input3C!=null){
                            objinput = new ObjectInputStream(input3C);
                            Data.setColorset(Data.Type_Humidity,(ArrayList<Integer>)objinput.readObject(),null);
                            objinput.close();input3C.close();}
                        if (Thread.currentThread().isInterrupted()){break;}
                        if (input4!=null){
                            objinput = new ObjectInputStream(input4);
                            Data.setData(Data.Type_Electricity,(ArrayList) objinput.readObject());
                            objinput.close();input4.close();}
                        if (Thread.currentThread().isInterrupted()){break;}
                    } catch (OptionalDataException e) {e.printStackTrace();} catch (StreamCorruptedException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();} catch (ClassNotFoundException e) {e.printStackTrace();}
                    msg="";
                    if ((status&1)==1){
                        msg+=getString(R.string.electricity)+"-";
                    }
                    status=status>>1;
                    if ((status&1)==1){
                        msg+=getString(R.string.humidity)+"-";
                    }
                    status=status>>1;
                    if ((status&1)==1){
                        msg+=getString(R.string.temperature)+"-";
                    }
                    status=status>>1;
                    if ((status&1)==1){
                        msg+=getString(R.string.apparatus)+"-";
                    }
                    if (!msg.isEmpty()) {
                        Snackbar prompt3 = Snackbar.make(fragment.getView(), getString(R.string.No) + msg + getString(R.string.Backup_Data), Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        prompt3.getView().setBackgroundColor(colorPrimary.data);
                        prompt3.show();
                    }
                    break;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressbarReturn(progressbar, 0);
                }
            });
            handler.post(new Runnable() {
                int trytimes=0;
                @Override
                public void run() {
                    synchronized (toolbarViews){
                        ActionMenuItemView view1=null;
                        ActionMenuItemView view2=null;
                        for (View temp:toolbarViews){
                            if (((ActionMenuItemView)temp).getItemData().toString().equals(getString(R.string.menu_refresh))) {
                                view1 = (ActionMenuItemView)temp;
                            }else if (((ActionMenuItemView) temp).getItemData().toString().equals(getString(R.string.menu_settings))){
                                view2 = (ActionMenuItemView)temp;
                            }
                        }
                        if (view1!=null&&view2!=null){
                            view1.clearAnimation();
                            view1.setEnabled(true);
                            view2.setEnabled(true);
                        }else if (trytimes<100){
                            handler.postDelayed(this,5);
                            trytimes++;
                        }
                    }
                }
            });

            onDemandRefreshing=false;



        }

        onDemandRefresh(Fragment fragment){
            this.fragment=fragment;
        }
    }

    int refresh (String URL,String URL2){
        Data.Status_apparatus=Data.DefaultRefresh;Data.Status_Electricity=Data.DefaultRefresh;
        Data.Status_Humidity=Data.DefaultRefresh;Data.Status_Temperature=Data.DefaultRefresh;
        int howmany=6;Map<String,String> parameters=new HashMap<>(3);
        Data.whattodonext todowhat=new Data.whattodonext() {
            @Override
            public void todo() {
                //refreshViews.run();

            }
        };
        Data.OnsuccessProcess onsuccessProcess=new Data.OnsuccessProcess(howmany,todowhat);
        if (Thread.currentThread().isInterrupted()){return -1;}
        parameters.put("user","henry");parameters.put("pass","yiweigang");
        OKHttpTool.asyncCustomPostFormforJsonObject(URL, parameters, onsuccessProcess, new Data.OnFailed(Data.Type_apparatuscfgset,4,todowhat));
        parameters.put("Type","Temperature");
        OKHttpTool.asyncCustomPostFormforJsonObject(URL2, parameters, onsuccessProcess, new Data.OnFailed(Data.Type_Temperature,4,todowhat));
        parameters.put("Type","Humidity");
        OKHttpTool.asyncCustomPostFormforJsonObject(URL2, parameters, onsuccessProcess, new Data.OnFailed(Data.Type_Humidity,4,todowhat));
        parameters.put("Type","appliance");
        OKHttpTool.asyncCustomPostFormforJsonObject(URL2, parameters, onsuccessProcess, new Data.OnFailed(Data.Type_Electricity,4,todowhat));
        int status=0;String msg="";int tempstatus;int trytimes=0;
        int Status_apparatus;int Status_Temperature;int Status_Humidity;int Status_Electricity;
        while (true){
            if (StopGettingData){
                return -1;}
            if (Thread.currentThread().isInterrupted()){StopGettingData=true;return -1;}
            if (trytimes>3000){
                StopGettingData=true;
                if (Thread.currentThread().isInterrupted()){return -1;}
                return status;
            }
            Status_apparatus=Data.Status_apparatus;
            Status_Temperature=Data.Status_Temperature;
            Status_Humidity=Data.Status_Humidity;
            Status_Electricity=Data.Status_Electricity;
            if (Status_apparatus==Data.RefreshDone){
                status|=Data.Type_apparatuscfgset;
            }
            if (Status_Temperature==Data.RefreshDone){
                status|=Data.Type_Temperature;
            }
            if (Status_Humidity==Data.RefreshDone){
                status|=Data.Type_Humidity;
            }
            if (Status_Electricity==Data.RefreshDone){
                status|=Data.Type_Electricity;
            }
            tempstatus=status;
            if (StopGettingData){return -1;}
            msg="";
            if ((tempstatus&1)==1){
                msg+="ElectricityBeenSet ";
            }
            tempstatus=tempstatus>>1;
            if ((tempstatus&1)==1){
                msg+="HumidityBeenSet ";
            }
            tempstatus=tempstatus>>1;
            if ((tempstatus&1)==1){
                msg+="TemperatureBeenSet ";
            }
            tempstatus=tempstatus>>1;
            if ((tempstatus&1)==1){
                msg+="ApparatusBeenSet ";
            }
            Log.d("DataStatus",msg);
            if (StopGettingData){return -1;}
            if (Thread.currentThread().isInterrupted()){StopGettingData=true;return -1;}
            if (Status_apparatus!=Data.DefaultRefresh&&Status_Humidity!=Data.DefaultRefresh
                    &&Status_Temperature!=Data.DefaultRefresh&&Status_Electricity!=Data.DefaultRefresh){
                if (Thread.currentThread().isInterrupted()){StopGettingData=true;return -1;}
                return status;
            }
            //if (Data.Status_apparatus==Data.RefreshFailed&&Data.Status_Humidity==Data.RefreshFailed
            //        &&Data.Status_Temperature==Data.RefreshFailed&&Data.Status_Electricity==Data.RefreshFailed){
            //    StopGettingData=true;
            //    return 0;
            //}
            else if (status==15){
                StopGettingData=true;
                if (Thread.currentThread().isInterrupted()){StopGettingData=true;return -1;}
                return 15;
            }
            if (StopGettingData){return -1;}
            if (Thread.currentThread().isInterrupted()){StopGettingData=true;return -1;}
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                StopGettingData=true;
                return -1;
            }
            trytimes++;
        }

    }

    class refreshingani extends Thread{
        int TPR;
        public void run() {
            while (true){
                if (Thread.currentThread().isInterrupted()){return;}
                try {
                    Thread.sleep(TPR/progressbar.getMax());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                if (Thread.currentThread().isInterrupted()){return;}
                int progress= progressbar.getProgress();
                if(progressbar.getProgress()<progressbar.getMax()&&progressbar.getProgress()>0) {
                    progressbar.setProgress(progress + 1);
                }
                else {progressbar.setProgress(1);}
                if (Thread.currentThread().isInterrupted()){return;}
                if(progressbar.getProgress()==0){
                    return;
                }
            }
        }
        refreshingani(int TPR){
            this.TPR=TPR;
        }
    }

    class progresswrapper {
        int getAnimating (){
            return 0;
        }//问题可能来自于这里。没有return一个真是的now值。
        void setAnimating(int now){
            if (now>95){
                //Log.d("has Restore","restoreProgressbar");
                //refreshinganirunning=false;
                //refreshReturnRunning=false;
            }else if (now==-1){
                refreshDescendDone=true;
            }
        }
    }

    void progressbarReturn(ProgressBar v, int which) {
        if (refreshinganiThread!=null){
            refreshinganiThread.interrupt();
        }
        if (progressbarReturnAnimator!=null){
            progressbarReturnAnimator.cancel();
        }
        if (which==0) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(v, "Y", v.getY(), originalPY);
            ObjectAnimator animator2 = ObjectAnimator.ofInt(new progresswrapper(), "Animating", 0, 100);
            progressbarReturnAnimator = new AnimatorSet();
            progressbarReturnAnimator.playTogether(animator1, animator2);
            progressbarReturnAnimator.setDuration(300);
            progressbarReturnAnimator.setInterpolator(new AccelerateInterpolator());
            progressbarReturnAnimator.start();
            refreshReturnRunning=true;

        }
        else if (which==2){
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(v, "Y", v.getY(), originalPY);
            ObjectAnimator animator2 = ObjectAnimator.ofInt(v, "Progress", v.getProgress(), 0);
            progressbarReturnAnimator = new AnimatorSet();
            progressbarReturnAnimator.playTogether(animator1, animator2);
            progressbarReturnAnimator.setDuration(300);
            progressbarReturnAnimator.setInterpolator(new AccelerateInterpolator());
            progressbarReturnAnimator.start();
        }
    }

    void progressbarDescend(ProgressBar v) {
        if (progressbarDescendAnimator!=null){
            progressbarDescendAnimator.cancel();
        }
        refreshDescendDone=false;
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(v, "Y", v.getY(), refreshEndY);
        ObjectAnimator animator2 = ObjectAnimator.ofInt(v, "Progress", v.getProgress(), v.getMax());
        ObjectAnimator animator3 = ObjectAnimator.ofInt(new progresswrapper(), "Animating", 90, -1);
        progressbarDescendAnimator = new AnimatorSet();
        progressbarDescendAnimator.playTogether(animator1, animator2,animator3);
        progressbarDescendAnimator.setDuration(300);
        progressbarDescendAnimator.setInterpolator(new AccelerateInterpolator());
        progressbarDescendAnimator.start();

    }


    public class fabontouch implements View.OnTouchListener{
        boolean firsttime1=true;
        boolean firsttime2=true;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //v.setPressed(true);
                float with = getResources().getDisplayMetrics().widthPixels;
                        //stopall=true;
                Fragment fragment1 = fm.findFragmentByTag("fragment1");
                Fragment fragment2 = fm.findFragmentByTag("fragment2");
                View view1 = fragment1.getView();
                View view2 = fragment2.getView();
                if(view1.getX()==0){
                    if (!button1.isActivated()){
                        animationdrawable(button1, button2, 5);}
                }
                else if(view1.getX()==-with){
                    if (!button2.isActivated()){
                        animationdrawable(button2, button1, 5);}
                }
                v.setActivated(true);
                //buttonexpansion(v,1,isFAB);
                AnimateButtonsForACycle(null,updatelistener_Expansion,true);


            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //float originalX= fab.getX()+(fab.getWidth()-originalwidth)/2;
                //getX()是获取相对于被触摸的控件的相对X坐标。getRawX()是获取相对于屏幕的X坐标。
                if (event.getRawX()<button1.getX()+button1.getWidth() &&event.getRawX()>button1.getX()
                        &&event.getRawY()<button1.getY()+button1.getHeight()&&event.getRawY()>button1.getY()){
                    if (!button1.isActivated()) {
                        animationdrawable(button1, null, 1);
                        animationdrawable(null, button2, 5);
                        fragmentanimation(1);
                        firsttime1 = true;

                    }else {
                        animationdrawable(button1, null, 1);
                        firsttime1=true;
                    }
                }
                else if (event.getRawX()<button2.getX()+button2.getWidth() &&event.getRawX()>button2.getX()
                        &&event.getRawY()<button2.getY()+button2.getHeight()&&event.getRawY()>button2.getY()){
                    if (!button2.isActivated()) {
                        animationdrawable(button2, null, 1);
                        animationdrawable(null, button1, 5);
                        fragmentanimation(2);
                        firsttime2 = true;
                    }else {
                        animationdrawable(button2, null, 1);
                        firsttime2=true;
                    }
                }else if (event.getRawX()>addbutton.getX()&&event.getRawX()<addbutton.getX()+addbutton.getWidth()
                        &&event.getRawY()>addbutton.getY()&&event.getRawY()<addbutton.getY()+addbutton.getHeight()) {
                    addbuttonlistener.onClick(null);
                }
                //buttonexpansion(v,2,isFAB);
                AnimateButtonsForACycle(null,updatelistener_Retraction,true);
                //new checkthread(animatorset).start();
                //v.setPressed(false);
            }
            else if (event.getAction()== MotionEvent.ACTION_MOVE){
                if (event.getRawX()<button2endX - fab_margin &&event.getRawX()>button2endX - fab_margin - targetWidth
                        &&event.getRawY()<button1.getY()+button1.getHeight()&&event.getRawY()>button1.getY()){
                    if (firsttime1) {
                        //每次这些资源后面的ID都会变，所以不可能这样比较了。
                        // button1.getBackground()!=getDrawable(R.drawable.button_off_r)
                        //Log.d("the Drawable",String.valueOf(button1.getBackground()!=getDrawable(R.drawable.button_off_r)));
                        //Log.d("WhatistheBackground",String.valueOf(button1.getBackground()));
                        //Log.d("WhatistheDrawable",String.valueOf(getDrawable(R.drawable.button_off_r)));
                        if (!button1.isActivated()) {
                            animationdrawable(button1, null, 4);
                            firsttime1 = false;
                        }
                        else {
                            animationdrawable(button1, null, 2);
                            firsttime1 = false;
                        }
                    }
                }
                else if (event.getRawX()<button2endX+targetWidth &&event.getRawX()>button2endX
                        &&event.getRawY()<button2.getY()+button2.getHeight()&&event.getRawY()>button2.getY()){
                    if (firsttime2) {
                        if (!button2.isActivated()) {
                            animationdrawable(button2, null, 4);
                            firsttime2 = false;
                        }
                        else{
                            animationdrawable(button2, null, 2);
                            firsttime2 = false;
                        }
                    }
                }
                else if (!firsttime1){
                    if (!button1.isActivated()) {
                        animationdrawable(button1, null, 3);
                        firsttime1 = true;
                    }
                    else {
                        animationdrawable(button1, null, 1);
                        firsttime1 = true;
                    }
                }
                else if (!firsttime2){
                    if (!button2.isActivated()) {
                        animationdrawable(button2, null, 3);
                        firsttime2 = true;
                    }
                    else {
                        animationdrawable(button2, null, 1);
                        firsttime2 = true;
                    }
                }

                if (event.getRawX()>addbutton.getX()&&event.getRawX()<addbutton.getX()+addbutton.getWidth()
                        &&event.getRawY()>addbutton.getY()&&event.getRawY()<addbutton.getY()+addbutton.getHeight()){
                    if (!addbutton.isPressed()) {
                        addbutton.setPressed(true);
                        //addbutton.performLongClick();
                    }
                }else {
                    if (addbutton.isPressed()) {
                        addbutton.setPressed(false);
                    }
                }

            }
            return true;
        }
    }

    //public class animationset extends Thread{
    //    View fab;View v;View v2;
    //    AnimatorSet set;boolean isrunning=true;
    //    int trytimes=1;
    //    public void run() {
    //        if (stopall){return;}
    //        try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
    //       handler.post(new Runnable() {
    //           @Override
    //           public void run() {
    //               fab.setActivated(true);set =buttonexpansion(fab,1,isMain);
    //           }
    //       }) ;
    //        while (true){
    //            //try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
    //            if (isbuttonYseted) {
    //                interanimating = true;break;
    //            }
    //        }
    //        customsleep(times);
    //        if (stopall){return;}
    //        while (true){
    //            if (stopall){return;}
    //            if (trytimes>1000){
    //                return;
    //            }
    //            if (stopall){return;}
    //            try {
    //                Thread.sleep(5);
    //            } catch (InterruptedException e) {
    //                e.printStackTrace();
    //            }
    //            if (stopall){return;}
    //            if (set!=null) {
    //                if (stopall){return;}
    //                if (!set.isRunning()) {
    //
    //                    customsleep(times/10);
    //                    if (stopall){return;}
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (!v.isActivated()){
    //                                animationdrawable(v, v2, 5);
    //                            }
    //                        }
    //
    //                    });
    //                    customsleep(times);
    //                    if (stopall){return;}
    //                    break;
    //                }
    //            }
    //            trytimes++;
    //        }
    //        if (stopall){return;}
    //        if (stopall){return;}
    //        handler.post(new Runnable() {
    //            @Override
    //            public void run() {
    //                buttonexpansion(fab,2,isMain);
    //            }
    //        });
    //        interanimating=false;
    //    }
    //
    //    void customsleep (int times){
    //        for (;times>0;times--){
    //            if (stopall){return;}
    //            try {
    //                Thread.sleep(duration3);
    //            } catch (InterruptedException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //
    //    animationset (View fab,View v,View v2){
    //        this.fab = fab;
    //        this.v=v;
    //        this.v2=v2;
    //    }
    //}

    //private static class animationWrapper {
    //    float Max;
    //    float value;
    //    View v1; View v2;
    //    float segmentPer1;
    //    float segmentPer2;
    //    float segmentPer3;
    //    float startX;float endX;
    //    float startY;float endY;
    //    float startWid;float endWid;
    //    float startHei;float endHei;
    //    //private static animationWrapper animationWrapper;
    //    float getValue (){
    //        return value;
    //    }
    //    void setValue(float value){
    //        this.value=value;
    //        if (value <=Max*segmentPer1){
    //            v1.setX(startX+((endX-startX)*(value/Max*segmentPer1)));
    //            //v1.setX(v1.getX()+((endX-v1.getX())*(value/Max*segmentPer1)));
    //        }else if (value<=Max*(segmentPer1+segmentPer2)){
    //        }else {
    //            v1.setX(v1.getX()+((startX-v1.getX())*(1-(Max-value)/Max*segmentPer3)));
    //        }
    //    }
    //    void Configure (float Max,View v1,View v2,
    //                    float segmentPer1,float segmentPer2,float segmentPer3){
    //        this.Max=Max;this.v1=v1;this.v2=v2;this.segmentPer1=segmentPer1;
    //        this.segmentPer2=segmentPer2;this.segmentPer3=segmentPer3;
    //    }
    //    //static animationWrapper setConfigurations (float Max,View v1,View v2,
    //    //                        float segmentPer1,float segmentPer2,float segmentPer3){
    //    //    if (animationWrapper==null){
    //    //        animationWrapper=new animationWrapper();
    //    //    }
    //    //    animationWrapper.Configure(Max,v1,v2,segmentPer1,segmentPer2,segmentPer3);
    //    //    return animationWrapper;
    //    //}
    //}

    Animator[] AnimateButtonsForACycle (Boolean Button1Activated,int which,boolean isFab){
        if (ButtonsReturned){
            button1.setY(fab.getY() + Ylocation);
            button2.setY(fab.getY() + Ylocation);
        }
        if (MainAddButtonReturned){
            addbutton.setY(fab.getY()+MainAddButtonOriginalYDelta);
        }
        //ButtonAnimatorUpdateListener updateListener;
        if (SwipingButtonsAnimator!=null){
            SwipingButtonsAnimator.cancel();
        }
        if (MainAddButtonAnimator!=null&&isFab&&(which==updatelistener_Expansion||which==updatelistener_Retraction)){
            MainAddButtonAnimator.cancel();
        }
        //int duration_1=0;int duration_2=0;
        if (!fab.isActivated()){
            fab.setActivated(true);
            //for (int i = 0; i<((AnimationDrawable)getDrawable(R.drawable.fab_off)).getNumberOfFrames(); i++){
            //    duration_1+=((AnimationDrawable)getDrawable(R.drawable.fab_off)).getDuration(i);
            //}//Log.d("Duration",String.valueOf(duration_1));
            //for (int i = 0; i<((AnimationDrawable)getDrawable(R.drawable.fab_on)).getNumberOfFrames(); i++){
            //    duration_2+=((AnimationDrawable)getDrawable(R.drawable.fab_on)).getDuration(i);
            //}//Log.d("Duration",String.valueOf(duration_2));
        }
        if (updatelistener!=null){
            updatelistener.configure(which,button1,button2,button2endX-fab_margin-targetWidth,targetWidth,fab.getHeight(),button2endX,targetWidth,fab.getHeight(),
                    buttonwrap1,buttonwrap2,fab,Button1Activated);
        }else {
            updatelistener= new ButtonAnimatorUpdateListener(which,button1,button2,button2endX-fab_margin-targetWidth,targetWidth,fab.getHeight(),button2endX,targetWidth,fab.getHeight(),
                    buttonwrap1,buttonwrap2,fab,Button1Activated);
        }
        float Midx=0;float YDrift=0; int Width=0;int Height=0;boolean activateMainAddButtonAnimator=true;
        switch (which){
            case updatelistener_Expansion:
                //因为这里的取值都是从getX，getWidth,getHeight在额外加减的，所以就会产生，在按钮未返回时，多次按fab，导致AddButton的目标体积，越来越大，很有意思。
                Midx=addbutton.getX()-(50)/2; YDrift=-200;Width=addbutton.getWidth()+50;Height=addbutton.getHeight()+50;break;
            case updatelistener_Retraction:
                Midx=MainAddButtonOriginalX;YDrift=0;Width=MainAddButtonOriginalWidth;Height=MainAddButtonOriginalHeight;break;
            default:activateMainAddButtonAnimator=false;break;
        }
        if (!isFab){activateMainAddButtonAnimator=false;}
        if (activateMainAddButtonAnimator) {
            if (MainAddButtonAnimatorupdatelistener != null) {
                MainAddButtonAnimatorupdatelistener.configure(addbutton, addbuttonwrap, Midx, YDrift, Width, Height, fab);
            }else {
                MainAddButtonAnimatorupdatelistener = new MainAddButtonAnimatorUpdateListener(addbutton,addbuttonwrap,Midx,YDrift,Width,Height,fab);
            }
            MainAddButtonAnimator=ValueAnimator.ofFloat(1,100);
            MainAddButtonAnimator.setDuration(duration2);
            MainAddButtonAnimator.removeAllUpdateListeners();
            MainAddButtonAnimator.addUpdateListener(MainAddButtonAnimatorupdatelistener);
        }

        SwipingButtonsAnimator=ValueAnimator.ofFloat(1,100);
        SwipingButtonsAnimator.setDuration(2*duration2+duration1);
        //这里，Listeners 和 updateListeners不是从属关系，而是完全独立的。
        // 所以 removeAllListeners/addListener和removeAllUpdateListeners/addUpdateListener是完全独立的。
        //SwipingButtonsAnimator.removeAllListeners();
        SwipingButtonsAnimator.removeAllUpdateListeners();
        SwipingButtonsAnimator.addUpdateListener(updatelistener);
        //SwipingButtonsAnimator.setInterpolator(new customedLinearAccelerationInterpolator());
        final boolean finalactivateMainAddButtonAnimator=activateMainAddButtonAnimator;


        //if (AnimatorStarted){
        //    AnimatorStarted=false;
        //    handler.postDelayed(new Runnable() {
        //        @Override
        //        public void run() {
                    SwipingButtonsAnimator.start();
                    if (finalactivateMainAddButtonAnimator){
                        MainAddButtonAnimator.start();
                    }
        //            AnimatorStarted=true;
        //        }
        //    },Math.max(duration_1,duration_2));
        //}


        return new Animator[]{SwipingButtonsAnimator,MainAddButtonAnimator};
    }

    public  class customedLinearAccelerationInterpolator implements Interpolator{

        @Override
        public float getInterpolation(float input) {
            float a=(2*1f)/((float) Math.pow(1f,2));
            float result= 0.5f*a*(float)Math.pow(input,2);
            return (float) Math.pow(input,2);
        }
    }

    public class customedNodedLinearAccelerationInterpolator implements Interpolator{
        float node;
        @Override
        public float getInterpolation(float input) {
            if (input<=node){
                return (float) Math.pow((input/node),2)*node;
            }else{
                return ((1-(float)Math.pow(1-((input-node)/(1-node)),2))*(1-node))+node;
            }
        }

        customedNodedLinearAccelerationInterpolator(float node){
            this.node=node;
        }
    }

    public class customedBoundingInterpolator implements Interpolator {
        float frictionac;
        @Override
        public float getInterpolation(float input) {
            float segment1;float segment2;float segment3;
            //segment1=Math.sqrt(2/(g-t/2));segment2=0;segment3=0;


            //if (input<=segment1){
            //
            //}else if(input<=segment2){
            //
            //}else{
            //
            //}
            return  0;
        }
    }

    final int updatelistener_Expansion=0;final int updatelistener_Retraction=1;final int updatelistener_Full=2;
    public class ButtonAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener{
        int which;
        View v1; View v2;viewwrapper vw1;viewwrapper vw2;
        float MidX1; float startX1; float startY1;
        float MidX2; float startX2; float startY2;
        int Width1;int Height1;int startWidth1;int startHeight1;
        int Width2;int Height2;int startWidth2;int startHeight2;
        float initX1;float initX2;float initY1;float initY2;
        int initWidth1;int initWidth2;
        int initHeight1;int initHeight2;
        FloatingActionButton tempfab;Boolean v1Activated;
        boolean ViewsActivated =false;boolean firstExceeded=false;boolean firstSetInits=false;
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //分数整型转换成float就会变成0.0了。
            float marginfraction = ((animation.getDuration()-duration1)/2f)/(float)animation.getDuration();
            float fraction = animation.getAnimatedFraction();
            //Log.d("marginfraction",String.valueOf(marginfraction));
           // animation.getAnimatedFraction()*(endX-startX);
            float MidY1=tempfab.getY() + (tempfab.getHeight() - tempfab.getHeight()) / 2 + DeltaY;
            float MidY2=tempfab.getY() + (tempfab.getHeight() - tempfab.getHeight()) / 2 + DeltaY;

            switch (which){
                case updatelistener_Expansion:
                    //if (fraction>marginfraction+0.00001f){
                    if (fraction>marginfraction){
                        //fraction=marginfraction;
                        animation.cancel();
                        //return;
                    }
                    break;
                case updatelistener_Retraction:
                    fraction +=1-marginfraction;
                    if (!firstExceeded&&fraction>=1){
                        fraction=1;
                        firstExceeded=true;
                        animation.cancel();
                    }
                    break;
                case updatelistener_Full:break;
                default:Log.d("Warning","Wrong Which Type");return;
            }
            if (fraction <=marginfraction){
                ButtonsReturned=false;
                if (v1.getVisibility()!=View.VISIBLE){v1.setVisibility(View.VISIBLE);}
                if (v2.getVisibility()!=View.VISIBLE){v2.setVisibility(View.VISIBLE);}
                v1.setX(startX1+(fraction/marginfraction)*(MidX1 -startX1));
                v2.setX(startX2+(fraction/marginfraction)*(MidX2 -startX2));
                v1.setY(startY1+(fraction/marginfraction)*(MidY1-startY1));
                v2.setY(startY2+(fraction/marginfraction)*(MidY2-startY2));
                vw1.setWidth(startWidth1+Math.round((fraction/marginfraction)*(Width1-startWidth1)));
                vw2.setWidth(startWidth2+Math.round((fraction/marginfraction)*(Width2-startWidth2)));
                vw1.setHeight(startHeight1+Math.round((fraction/marginfraction)*(Height1-startHeight1)));
                vw2.setHeight(startHeight2+Math.round((fraction/marginfraction)*(Height2-startHeight2)));
            }else if (fraction >marginfraction && fraction <1-marginfraction){
                ButtonsReturned=false;
                if (v1Activated!=null){
                    if (!ViewsActivated){
                        if (v1Activated){
                            if (!v1.isActivated()){
                                animationdrawable(v1, v2, 5);
                            }
                        }else {
                            if (!v2.isActivated()){
                                animationdrawable(v2, v1, 5);
                            }
                        }
                        ViewsActivated =true;
                    }
                }

                v1.setX(MidX1);v2.setX(MidX2);v1.setY(MidY1);v2.setY(MidY2);
                vw1.setWidth(Width1);vw2.setWidth(Width2);vw1.setHeight(Height1);vw2.setHeight(Height2);
            }else if (fraction >=1-marginfraction && fraction <=1){
                if (fraction==1){
                    v1.setVisibility(View.INVISIBLE);v2.setVisibility(View.INVISIBLE);
                    ButtonsReturned=true;
                    //if (tempfab.isActivated()){
                    //    tempfab.setActivated(false);}
                }
                if (tempfab.isActivated()){
                    tempfab.setActivated(false);}
                float endY1 = tempfab.getY() + Ylocation; float endY2 = tempfab.getY() + Ylocation;
                if (!firstSetInits){
                    setInits();firstSetInits=true;
                }
                v1.setX(initX1 +((fraction-(1-marginfraction))/marginfraction)*(originalX-initX1));
                v2.setX(initX2 +((fraction-(1-marginfraction))/marginfraction)*(originalX-initX2));
                v1.setY(initY1+((fraction-(1-marginfraction))/marginfraction)*(endY1-initY1));
                v2.setY(initY2+((fraction-(1-marginfraction))/marginfraction)*(endY2-initY2));
                vw1.setWidth(initWidth1+Math.round(((fraction-(1-marginfraction))/marginfraction)*(originalwidth-initWidth1)));
                vw2.setWidth(initWidth2+Math.round(((fraction-(1-marginfraction))/marginfraction)*(originalwidth-initWidth2)));
                vw1.setHeight(initHeight1+Math.round(((fraction-(1-marginfraction))/marginfraction)*(originalheight-initHeight1)));
                vw2.setHeight(initHeight2+Math.round(((fraction-(1-marginfraction))/marginfraction)*(originalheight-initHeight2)));
            }
        }

        ButtonAnimatorUpdateListener (int which, View v1, View v2, float MidX1, int Width1, int Height1,
                                      float MidX2, int Width2, int Height2, viewwrapper vw1, viewwrapper vw2,
                                      FloatingActionButton tempfab,Boolean v1Activated){
            configure(which,v1,v2, MidX1,Width1,Height1, MidX2,Width2,Height2,vw1,vw2,tempfab,v1Activated);
        }

        void configure (int which, View v1,View v2, float MidX1, int Width1, int Height1,
                        float MidX2, int Width2, int Height2, viewwrapper vw1, viewwrapper vw2,
                        FloatingActionButton tempfab,Boolean v1Activated){
            this.v1=v1;this.v2=v2;this.which=which;
            this.MidX1 =MidX1;this.Width1=Width1;this.Height1=Height1;
            this.MidX2 =MidX2;this.Width2=Width2;this.Height2=Height2;
            this.startX1=v1.getX();this.startY1=v1.getY();this.startWidth1=v1.getWidth();this.startHeight1=v1.getHeight();
            this.startX2=v2.getX();this.startY2=v2.getY();this.startWidth2=v2.getWidth();this.startHeight2=v2.getHeight();
            this.vw1=vw1;this.vw2=vw2;this.tempfab=tempfab;this.v1Activated=v1Activated;
            this.ViewsActivated =false;this.firstExceeded=false;this.firstSetInits=false;

        }

        void setInits(){
            initX1=v1.getX();initX2=v2.getX();initY1=v1.getY();initY2=v2.getY();initWidth1=vw1.getWidth();
            initWidth2=vw2.getWidth();initHeight1=vw1.getHeight();initHeight2=vw2.getHeight();
        }
    }

    public class MainAddButtonAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener{
        //int which;
        FloatingActionButton tempfab;View v;viewwrapper vw;
        float startX;float startY;int startWidth;int startHeight;
        float Midx; float YDrift; int Width; int Height;

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float MidY=tempfab.getY()+MainAddButtonOriginalYDelta+YDrift;
            if (v.getVisibility()!=View.VISIBLE){v.setVisibility(View.VISIBLE);}
            v.setX(startX+animation.getAnimatedFraction()*(Midx-startX));
            v.setY(startY+animation.getAnimatedFraction()*(MidY-startY));
            vw.setWidth(startWidth+Math.round(animation.getAnimatedFraction()*(Width-startWidth)));
            vw.setHeight(startHeight+Math.round(animation.getAnimatedFraction()*(Height-startHeight)));
            if (animation.getAnimatedFraction()==1&&YDrift==0){
                if (v.getVisibility()!=View.INVISIBLE){v.setVisibility(View.INVISIBLE);}
                MainAddButtonReturned=true;
            }else {
                MainAddButtonReturned=false;
            }

            //v.setX();
        }

        MainAddButtonAnimatorUpdateListener( View v, viewwrapper vw, float Midx, float YDrift, int Width, int Height, FloatingActionButton tempfab){
            configure (v,vw, Midx,YDrift,Width,Height,tempfab);
        }
        void configure (View v,viewwrapper vw, float Midx, float YDrift, int Width, int Height, FloatingActionButton tempfab){
            this.v=v; this.vw=vw; this.tempfab=tempfab;
            this.startX=v.getX();this.startY=v.getY();this.startWidth=v.getWidth();this.startHeight=v.getHeight();
            this.Midx =Midx;this.YDrift=YDrift;this.Width=Width;this.Height=Height;
        }
    }



    public  class animatorlistener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        void  initiate (View v1, View v2){

        }
    }

    public void animationdrawable (final View v,final View v2,final int which) {
        //final AnimationDrawable vAnimationDrawable; final变量在方法体内可以先定义不赋值，然后在后面赋值，但只能赋值一次。
        AnimationDrawable vAnimationDrawable=null;
        AnimationDrawable v2AnimationDrawable=null;
            if (which==5){
                if (v!=null){
                    v.setBackground(getDrawable(R.drawable.button_onoff_r));}
                if (v2!=null){
                    v2.setBackground(getDrawable(R.drawable.button_onoff_ani));}
                if (v!=null){
                    vAnimationDrawable=((AnimationDrawable)v.getBackground());
                    vAnimationDrawable.start();}
                if (v2!=null){
                    v2AnimationDrawable=((AnimationDrawable)v2.getBackground());
                    v2AnimationDrawable.start();}

                int duration_1 = 0;int duration_2 = 0;
                if (vAnimationDrawable!=null) {
                    for (int i = 0; i < vAnimationDrawable.getNumberOfFrames(); i++) {
                        duration_1 += vAnimationDrawable.getDuration(i);
                    }
                }
                if (v2AnimationDrawable!=null){
                    for (int i = 0; i < v2AnimationDrawable.getNumberOfFrames(); i++) {
                        duration_2 += v2AnimationDrawable.getDuration(i);
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (v!=null){
                        v.setBackground(getDrawable(R.drawable.button_on));}
                        if (v2!=null){
                        v2.setBackground(getDrawable(R.drawable.button_off));}
                        if (v!=null){
                        v.setActivated(true);}
                        if (v2!=null){
                        v2.setActivated(false);}
                    }
                },Math.max(duration_1,duration_2));

            }
            else if (which==1){
                if (v!=null) {
                    v.setBackground(getDrawable(R.drawable.button_on_ani));
                    vAnimationDrawable = ((AnimationDrawable) v.getBackground());
                    vAnimationDrawable.start();
                }

                int duration_1 = 0;
                if (vAnimationDrawable!=null) {
                    for (int i = 0; i < vAnimationDrawable.getNumberOfFrames(); i++) {
                        duration_1 += vAnimationDrawable.getDuration(i);
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (v!=null) {
                            v.setBackground(getDrawable(R.drawable.button_on));
                            v.setActivated(true);
                        }
                    }
                },duration_1);

            }
            else if (which==2){
                if (v!=null) {
                    v.setBackground(getDrawable(R.drawable.button_on_r));
                    vAnimationDrawable = ((AnimationDrawable) v.getBackground());
                    vAnimationDrawable.start();
                }

                int duration_1 = 0;
                if (vAnimationDrawable!=null) {
                    for (int i = 0; i < vAnimationDrawable.getNumberOfFrames(); i++) {
                        duration_1 += vAnimationDrawable.getDuration(i);
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (v!=null) {
                            v.setBackground(getDrawable(R.drawable.button_focused));
                        }
                        //Log.d("isFocused-fromOn",String.valueOf(v.isFocused()));
                    }
                },duration_1);

            }
            else if (which==3){
                if (v!=null) {
                    v.setBackground(getDrawable(R.drawable.button_off_ani));
                    vAnimationDrawable=((AnimationDrawable) v.getBackground());
                    vAnimationDrawable.start();
                }

                int duration_1 = 0;
                if (vAnimationDrawable!=null) {
                    for (int i = 0; i < vAnimationDrawable.getNumberOfFrames(); i++) {
                        duration_1 += vAnimationDrawable.getDuration(i);
                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (v!=null) {
                            v.setBackground(getDrawable(R.drawable.button_off));
                            v.setActivated(false);
                        }
                    }
                },duration_1);

            }
            else if (which==4){
                if (v!=null) {
                    v.setBackground(getDrawable(R.drawable.button_off_r));
                    vAnimationDrawable = (AnimationDrawable) v.getBackground();
                    vAnimationDrawable.start();
                }
                //((AnimationDrawable)v.getBackground()).start();
                int duration_1 = 0;
                if (vAnimationDrawable!=null) {
                    for (int i = 0; i < vAnimationDrawable.getNumberOfFrames(); i++) {
                        duration_1 += vAnimationDrawable.getDuration(i);
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (v!=null) {
                            v.setBackground(getDrawable(R.drawable.button_focused));
                        }
                        //Log.d("isFocused-fromOff",String.valueOf(v.isFocused()));
                    }
                },duration_1);



            }
    }




    //AnimatorSet buttonexpansion (View v, int which,int who){
    //    isbuttonYseted=false;
    //    button2.setVisibility(View.VISIBLE);
    //    button1.setVisibility(View.VISIBLE);
    //    if (which==1) {
    //        if (who==isFAB) {
    //            if ((animatorset2 == null || !animatorset2.isRunning())
    //                    && (SwipingButtonsAnimator==null||!SwipingButtonsAnimator.isRunning())
    //                    &&(!interanimating)) {
    //                button2.setY(v.getY() + Ylocation);
    //                button1.setY(v.getY() + Ylocation);
    //            }
    //        }
    //        else if (who==isMain){
    //            //if ((animatorset2 == null || !animatorset2.isRunning())&&(animatorset1==null || !animatorset1.isRunning())
    //            //       &&(!interanimating)) {
    //            if ((SwipingButtonsAnimator==null||!SwipingButtonsAnimator.isRunning())&&(!interanimating)) {
    //                button2.setY(v.getY() + Ylocation);
    //                button1.setY(v.getY() + Ylocation);
    //            }
    //        }
    //        isbuttonYseted=true;
    //
    //        final float TargetY = v.getY() + (v.getHeight() - targetHeight) / 2 + DeltaY;
    //        ObjectAnimator objectanimator1 = ObjectAnimator.ofFloat(button1, "X", button1.getX(), button2endX - fab_margin - targetWidth);
    //        ObjectAnimator objectanimator2 = ObjectAnimator.ofFloat(button2, "X", button2.getX(), button2endX);
    //        ObjectAnimator objectanimator7 = ObjectAnimator.ofFloat(button1, "Y", button1.getY(), TargetY);
    //        ObjectAnimator objectanimator8 = ObjectAnimator.ofFloat(button2, "Y", button2.getY(), TargetY);
    //        ObjectAnimator objectanimator3 = ObjectAnimator.ofInt(buttonwrap1, "Width", button1.getWidth(), targetWidth);
    //        ObjectAnimator objectanimator4 = ObjectAnimator.ofInt(buttonwrap2, "Width", button2.getWidth(), targetWidth);
    //        ObjectAnimator objectanimator5 = ObjectAnimator.ofInt(buttonwrap1, "Height", button1.getHeight(), targetHeight);
    //        ObjectAnimator objectanimator6 = ObjectAnimator.ofInt(buttonwrap2, "Height", button2.getHeight(), targetHeight);
    //        if (animatorset1 != null && animatorset2 != null) {
    //            //animatorset的.end()方法其实是说让Animatorset直接跳转到最后一帧然后停下
    //            animatorset1.cancel();
    //            animatorset2.cancel();
    //            if (SwipingButtonsAnimator!=null){
    //                SwipingButtonsAnimator.cancel();
    //                ButtonsReturned=true;
    //            }
    //        }
    //
    //        animatorset1 = new AnimatorSet();
    //        animatorset1.playTogether(objectanimator1, objectanimator2,
    //                objectanimator3, objectanimator4, objectanimator5, objectanimator6, objectanimator7, objectanimator8);
    //        animatorset1.setDuration(duration2);
    //        animatorset1.setInterpolator(new AccelerateInterpolator());
    //        v.setActivated(true);
    //        animatorset1.start();
    //
    //        return animatorset1;
    //    }
    //    else if (which ==2){
    //        ObjectAnimator objectanimator2nd1 = ObjectAnimator.ofFloat(button1, "X", button1.getX(), originalX);
    //        ObjectAnimator objectanimator2nd2 = ObjectAnimator.ofFloat(button2, "X", button2.getX(), originalX);
    //        ObjectAnimator objectanimator2nd7 = ObjectAnimator.ofFloat(button1, "Y", button1.getY(), v.getY() + Ylocation);
    //        ObjectAnimator objectanimator2nd8 = ObjectAnimator.ofFloat(button2, "Y", button2.getY(), v.getY() + Ylocation);
    //        ObjectAnimator objectanimator2nd3 = ObjectAnimator.ofInt(buttonwrap1, "Width", button1.getWidth(), originalwidth);
    //        ObjectAnimator objectanimator2nd4 = ObjectAnimator.ofInt(buttonwrap2, "Width", button2.getWidth(), originalwidth);
    //        ObjectAnimator objectanimator2nd5 = ObjectAnimator.ofInt(buttonwrap1, "Height", button1.getHeight(), originalheight);
    //        ObjectAnimator objectanimator2nd6 = ObjectAnimator.ofInt(buttonwrap2, "Height", button2.getHeight(), originalheight);
    //        ObjectAnimator objectanimator2nd9 = ObjectAnimator.ofInt(buttonwrap1, "Visibility", button1.getVisibility(), View.INVISIBLE);
    //        ObjectAnimator objectanimator2nd10 = ObjectAnimator.ofInt(buttonwrap2, "Visibility", button2.getVisibility(), View.INVISIBLE);
    //        if (animatorset1 != null && animatorset2 != null) {
    //            animatorset1.cancel();
    //            animatorset2.cancel();
    //            if (SwipingButtonsAnimator!=null){
    //                SwipingButtonsAnimator.cancel();
    //                ButtonsReturned=true;
    //            }
    //        }
    //        animatorset2 = new AnimatorSet();
    //        animatorset2.playTogether(objectanimator2nd1, objectanimator2nd2,
    //                objectanimator2nd3, objectanimator2nd4, objectanimator2nd5, objectanimator2nd6,
    //                objectanimator2nd7, objectanimator2nd8, objectanimator2nd9, objectanimator2nd10);
    //        animatorset2.setDuration(duration2);
    //        animatorset2.setInterpolator(new AccelerateInterpolator());
    //        animatorset2.start();
    //        v.setActivated(false);
    //        return animatorset2;
    //    }
    //return null;}

    void fragmentanimation (int which){
        Fragment fragment1 = fm.findFragmentByTag("fragment1");
        Fragment fragment2 = fm.findFragmentByTag("fragment2");
        View view1 = fragment1.getView();
        View view2 = fragment2.getView();
        float X1= view1.getX();
        float X2=view2.getX();
        float with = getResources().getDisplayMetrics().widthPixels;
        if (which ==1){
            /*mainactivityontouch temp = new mainactivityontouch();
                       temp.mandatory=1;
                       temp.onTouch(null,null);*/
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(view1,"X",X1,0);
            ObjectAnimator animation2 = ObjectAnimator.ofFloat(view2,"X",X2,with+viewDelta);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(durationfragment);
            set.playTogether(animation1,animation2);
            set.start();
        }
        else if (which==2){
            /*mainactivityontouch temp = new mainactivityontouch();
                       temp.mandatory=2;
                       temp.onTouch(null,null);*/
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(view1,"X",X1,-with);
            ObjectAnimator animation2 = ObjectAnimator.ofFloat(view2,"X",X2,0+viewDelta);
            //animation1.start();
            //animation2.start();
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animation1,animation2);
            set.setDuration(durationfragment);
            set.start();
        }
    }





    class viewwrapper {
        View view;
        viewwrapper (View view){
            this.view = view;
        }
        int getWidth (){
           return view.getLayoutParams().width;
        }
        void setWidth (int width){
            view.getLayoutParams().width = width;
            view.requestLayout();
        }
        int getHeight (){
            return view.getLayoutParams().height;
        }
        void setHeight (int height){
            view.getLayoutParams().height = height;
            view.requestLayout();
        }
        int getVisibility (){return view.getVisibility();}
        void setVisibility (int visibility){
            if (visibility==View.INVISIBLE){
                view.setVisibility(View.INVISIBLE);

            }
            else if (visibility==View.VISIBLE){
                view.setVisibility(View.VISIBLE);
            }
        }
    }


    void setdefaultfragment (){
        getTheme().resolveAttribute(R.attr.colorPrimary,colorPrimary,true);
        fm = getFragmentManager();
        final FragmentTransaction transaction = fm.beginTransaction();
        Fragment tempfragment1=fm.findFragmentByTag("fragment1");
        Fragment tempfragment2=fm.findFragmentByTag("fragment2");
        if (tempfragment1!=null){
            transaction.remove(tempfragment1);
        }
        if (tempfragment2!=null){
            transaction.remove(tempfragment2);
        }
        fragment1 = new Fragment_1();
        transaction.add(R.id.include,fragment1,"fragment1");
        transaction.add(R.id.include,new Fragment_2(),"fragment2");
        //transaction.detach(fm.findFragmentByTag("fragment1"));
        //transaction.attach(fm.findFragmentByTag("fragment2"));
        transaction.commit();
        fm.executePendingTransactions();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (fab.getHeight()==0){
                    handler.postDelayed(this,5);
                    return;
                }
                fab_margin = getResources().getDimension(R.dimen.fab_margin)+interval;
                button2endX = fab.getX()-targetWidth-fab_margin+8;
                //Log.d("BUTTON1WIDTH&HEIGHT",String.valueOf(button1.getWidth())+":"+String.valueOf(button1.getHeight()));
                originalwidth = button1.getWidth();//(int)getResources().getDimension(R.dimen.buttonwidtht);
                originalheight= button1.getHeight();//(int)getResources().getDimension(R.dimen.buttonheight);
                Ylocation =  (fab.getHeight()-originalheight)/2+DeltaY;
                originalX= fab.getX()+(fab.getWidth()-originalwidth)/2;
                MainAddButtonOriginalYDelta=(fab.getHeight()-addbutton.getHeight())/2;
                button2.setX(originalX);
                button1.setX(originalX);
                button2.setY(fab.getY()+Ylocation);
                button1.setY(fab.getY()+Ylocation);
                button1.setActivated(true);
                addbutton.setX(fab.getX()+(fab.getWidth()-addbutton.getWidth())/2);
                addbutton.setY(fab.getY()+MainAddButtonOriginalYDelta);
                originalPY=progressbar.getY();
                refreshEndY=originalPY+progressbar.getHeight()+Maxprogress;
                MainAddButtonOriginalX=addbutton.getX();
                MainAddButtonOriginalWidth=addbutton.getWidth();
                MainAddButtonOriginalHeight=addbutton.getHeight();
                fileDir=getFilesDir();
                progressbar.setMax(Maxprogress);
                StopGettingData=true;
                refreshthread.interrupt();
                //StopDaemon=true;
                //if (UpdateKeeper!=null){
                //    UpdateKeeper.interrupt();
                //}
                //initiateToolbar(toolbar,ActivityMenu);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment1.getView()!=null&&fragment1.getView().getWidth()!=0) {
                            Log.d("onDemandRefresh","hasrunning");
                            progressbarDescend(progressbar);
                            refreshthread = new onDemandRefresh(fragment1);
                            refreshthread.start();
                            //UpdateKeeper=new updatedkeeper(sleeptime,autorestoretime);
                            //UpdateKeeper.setDaemon(true);
                            //UpdateKeeper.setName("UpdateKeeper");
                            //StopDaemon=false;
                            //UpdateKeeper.start();


                            new Thread(add_prompts).start();
                        }else {
                            handler.postDelayed(this,10);
                        }
                    }
                },10);

            }
        },5);
   }

    float getFitTextSize(float defaultSize,float width,String text){
        Paint temppaint = new Paint();float textsize=defaultSize;
        Log.d("TextContainerWidth",String.valueOf(width));
        while (true) {
            if(textsize>1) {
                temppaint.setTextSize(textsize);
                if (temppaint.measureText(text)<=width){
                    Log.d("TextWidth",String.valueOf(temppaint.measureText(text)));
                    Log.d("TextSize",String.valueOf(textsize));
                    return textsize;
                }
                textsize-=0.1f;
            }
            else {

                return 1f+0.1f;
            }
        }

    }

    private class AddPromptsRunnable implements Runnable {
        volatile Boolean continuationstatus = null;
        @Override
        public void run() {
            continuationstatus=null;
            RelativeLayout templayout = null;
            while (templayout == null) {
                try {
                    templayout = (RelativeLayout) fragment1.getView();
                } catch (Exception e) {
                }
            }
            final RelativeLayout layout = templayout;
            final boolean[] prompts_added = new boolean[1];
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AllAddedToggles.clear();
                    layout.removeAllViews();
                }
            });
            StopControlling = true;
            OKHttpTool.cancelAllConnections(OKHttpTool.Dispatcher);
            synchronized (calledTogglesDatas) {
                calledTogglesDatas.clear();
            }
            synchronized (ApparatusBeingControlled) {
                ApparatusBeingControlled.clear();
            }
            UpdateKeeper.restoreSleeptime();
            StopRefreshView = false;
            handler.post(new Runnable() {
                RelativeLayout.LayoutParams params;
                @Override
                public void run() {
                    //if (StopRefreshView){return;}
                    final ImageView prompt_changed = new ImageView(Maincontext);
                    final ImageView prompt_new = new ImageView(Maincontext);
                    final ImageView prompt_removed = new ImageView(Maincontext);
                    prompt_changed.setVisibility(View.INVISIBLE);
                    prompt_new.setVisibility(View.INVISIBLE);
                    prompt_removed.setVisibility(View.INVISIBLE);
                    prompt_changed.setId(prompt_changed_ID);
                    prompt_new.setId(prompt_new_ID);
                    prompt_removed.setId(prompt_removed_ID);
                    prompt_changed.setImageDrawable(getDrawable(R.drawable.prompt_changed));
                    prompt_new.setImageDrawable(getDrawable(R.drawable.prompt_new));
                    prompt_removed.setImageDrawable(getDrawable(R.drawable.prompt_removed));
                    Log.d("adding", "prompt_changed");
                    //prompt_changed.setY(fragment1.getView().getHeight() - fragment1.getView().getHeight() / 3 + 5);
                    //prompt_changed.setX(fragment1.getView().getWidth() - fragment1.getView().getWidth() / 11 - getResources().getDimension(R.dimen.prompt_width));
                    prompt_changed.setY(fragment1.getView().getHeight()*4/5);
                    prompt_changed.setX(20);
                    params = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.prompt_width), (int) getResources().getDimension(R.dimen.prompt_height));
                    //if (StopRefreshView){return;}
                    layout.addView(prompt_changed, params);
                    //prompt_changed.measure(0,0);
                    //handler.post(new Runnable() {
                    //    @Override
                    //    public void run() {
                    //if (StopRefreshView){return;}
                    //if (prompt_changed.getHeight()==0||prompt_changed.getWidth()==0){
                    //    //if (StopRefreshView){return;}
                    //    handler.postDelayed(this,5);
                    //    Log.d("adding","prompt_newDelayed");
                    //    return;
                    //}
                    //if (StopRefreshView){return;}
                    Log.d("adding", "prompt_new");
                    params = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.prompt_width), (int) getResources().getDimension(R.dimen.prompt_height));
                    //params.addRule(RelativeLayout.ALIGN_TOP,prompt_changed_ID);
                    //prompt_new.setY(fragment1.getView().getHeight() - fragment1.getView().getHeight() / 3 + 16);
                    prompt_new.setY(prompt_changed.getY());
                    //params.addRule(RelativeLayout.LEFT_OF,prompt_changed_ID);
                    //prompt_new.setX(prompt_changed.getX() - getResources().getDimension(R.dimen.prompt_width));
                    prompt_new.setX(prompt_changed.getX() + getResources().getDimension(R.dimen.prompt_width));
                    layout.addView(prompt_new, params);
                    //handler.post(new Runnable() {
                    //    @Override
                    //    public void run() {
                    //if (StopRefreshView){return;}
                    //if (prompt_new.getHeight()==0||prompt_new.getWidth()==0){
                    //    //if (StopRefreshView){return;}
                    //    handler.postDelayed(this,5);
                    //    Log.d("adding","prompt_removedDelayed");
                    //    return;
                    //}
                    //if (StopRefreshView){return;}
                    Log.d("adding", "prompt_removed");
                    params = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.prompt_width), (int) getResources().getDimension(R.dimen.prompt_height));
                    //params.addRule(RelativeLayout.ALIGN_TOP,prompt_changed_ID);
                    //prompt_removed.setY(fragment1.getView().getHeight() - fragment1.getView().getHeight() / 3 + 5);
                    prompt_removed.setY(prompt_new.getY());
                    //params.addRule(RelativeLayout.LEFT_OF,prompt_new_ID);
                    //prompt_removed.setX(fragment1.getView().getWidth() / 9);
                    prompt_removed.setX(prompt_new.getX()+getResources().getDimension(R.dimen.prompt_width));
                    layout.addView(prompt_removed, params);
                    Log.d("IDS", "Changed:" + String.valueOf(prompt_changed.getId()) + "-New" + String.valueOf(prompt_new.getId()) + "-Removed" + String.valueOf(prompt_removed.getId()));
                    prompts_added[0] = true;
                    //    }
                    //});
                    //    }
                    //});
                }
            });
            int trys = 0;
            while (!prompts_added[0]) {
                if (StopRefreshView) {
                    continuationstatus =false;
                    return;
                }
                if (trys > 1000) {
                    Snackbar prompt = Snackbar.make(fragment1.getView(), R.string.Your_Device_is_Too_Slow_to_Show_The_Apparatuses, Snackbar.LENGTH_LONG);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //prompt.getView().setBackgroundColor(getResources().getColor(getTheme().getResources().getIdentifier("holo_blue_light", "color", "android"),null));
                        prompt.getView().setBackgroundColor(getResources().getColor(colorPrimary.resourceId, null));
                    } else {
                        //TypedValue typedvalue = new TypedValue();
                        //getTheme().resolveAttribute(R.attr.colorPrimary,typedvalue,true);
                        //prompt.getView().setBackgroundColor(getResources().getColor(colorPrimary.resourceId));
                        prompt.getView().setBackgroundColor(colorPrimary.data);
                    }
                    prompt.show();
                    Log.d("prompts", "Wrong Showing");
                    StopRefreshView = true;
                    continuationstatus =false;
                    return;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    StopRefreshView = true;
                    e.printStackTrace();
                    continuationstatus =false;
                    return;
                }
                trys++;
            }
            continuationstatus=true;

        }

        boolean run (boolean[] status){
            run();
            while (true){
                if (this.continuationstatus !=null){
                    if (status!=null) {
                        status[0] = this.continuationstatus;
                    }
                    return this.continuationstatus;
                }
            }
        }
    }

    public class RefreshViews {
        RelativeLayout layout;
        Button usedbutton1=null;Button usedbutton2=null;Button usedbutton3=null;


        volatile boolean refreshing=true;
        volatile boolean added=false;
        volatile boolean prompts_added=false;
        public void run() {
            if (!Data.getApparatuses().isEmpty()){
                setAlertedLogNumber(Data.getApparatuses().get(0).AlertedLogNumber);
            }
            while (layout==null) {
                try {
                    layout = (RelativeLayout) fragment1.getView();
                }catch (Exception e){

                }
            }
            try {
                //boolean[] continuationstatus = new boolean[1];
                //add_prompts.run(continuationstatus);
                if (Thread.currentThread().isInterrupted()){StopGettingData=true;return;}
                if (!add_prompts.run(null)){StopRefreshView=true;return;}
                int index=1;
                int indexMax=(int)Math.floor((layout.getHeight()*0.6081082)/getResources().getDimension(R.dimen.toggle_size_height));
                //Log.d("ApparatusBounds",String.valueOf((layout.getHeight()*0.6081082)));
                //Log.d("ApparatusRatio",String.valueOf((float) (layout.getHeight()-layout.getHeight()/3-87)/layout.getHeight()));
                //Log.d("ApparatusIndex",String.valueOf((layout.getHeight()*0.6081082)/getResources().getDimension(R.dimen.toggle_size_height)));
                boolean firstSnackBarShown=false;
                for (Data.apparatuscfg temp:(ArrayList<Data.apparatuscfg>)Data.getApparatuses().clone()) {

                    if (index <= indexMax*3) {

                        final Button tempbutton = new Button(Maincontext);//因为Activity是Context的子类所以用getActivity和getContext都可以。
                        tempbutton.setId(temp.id);
                        tempbutton.setText(temp.name);
                        tempbutton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        //tempbutton.getPaint().setFakeBoldText(true);
                        tempbutton.setContentDescription(temp.URL);
                        final boolean finaltempisactivated=temp.isactivated;
                        final boolean finalAlerted = temp.Alerted;
                        Log.d("newID", String.valueOf(tempbutton.getId()));


                        tempbutton.setBackground(getDrawable(R.drawable.animation_selector));
                        tempbutton.setVisibility(View.INVISIBLE);
                        if ((index + 2) % 3 == 0) {
                            //tempbutton.setY(550);//在这里设置了X或者Y，后面的Layoutparams涉及到X或者Y的部分的设置就无效了。
                            //layout.generateLayoutParams();//这个必须传入AttributeSet才可以。
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("has1run", "true");
                                    if (StopRefreshView){return;}
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (getResources().getDimension(R.dimen.toggle_size)), (int) (getResources().getDimension(R.dimen.toggle_size_height)));
                                    if (usedbutton1 != null) {
                                        params.addRule(RelativeLayout.BELOW, usedbutton1.getId());
                                        params.addRule(RelativeLayout.ALIGN_LEFT, usedbutton1.getId());
                                    } else {
                                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                        params.topMargin = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
                                        params.leftMargin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
                                    }
                                    usedbutton1 = tempbutton;
                                    layout.addView(tempbutton, params);
                                }
                            });
                        } else if ((index + 1) % 3 == 0) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("has2run", "true");
                                    if (StopRefreshView){return;}
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (getResources().getDimension(R.dimen.toggle_size)), (int) (getResources().getDimension(R.dimen.toggle_size_height)));
                                    if (usedbutton2 != null) {
                                        params.addRule(RelativeLayout.BELOW, usedbutton2.getId());
                                        params.addRule(RelativeLayout.ALIGN_LEFT, usedbutton2.getId());
                                    } else {
                                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                        params.topMargin = (int) getResources().getDimension(R.dimen.activity_vertical_margin);

                                    }
                                    usedbutton2 = tempbutton;
                                    layout.addView(tempbutton, params);
                                }
                            });
                        } else if (index % 3 == 0) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("has3run", "true");
                                    if (StopRefreshView){return;}
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (getResources().getDimension(R.dimen.toggle_size)), (int) (getResources().getDimension(R.dimen.toggle_size_height)));
                                    if (usedbutton3 != null) {
                                        params.addRule(RelativeLayout.BELOW, usedbutton3.getId());
                                        params.addRule(RelativeLayout.ALIGN_LEFT, usedbutton3.getId());
                                    } else {
                                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                        params.topMargin = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
                                        params.rightMargin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
                                    }
                                    usedbutton3 = tempbutton;
                                    layout.addView(tempbutton, params);
                                }
                            });
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (StopRefreshView){return;}
                                if (tempbutton.getHeight() == 0&&!StopRefreshView) {
                                    handler.postDelayed(this, 5);
                                    Log.d("Posted", "Posted");
                                    return;
                                }
                                if (StopRefreshView){return;}
                                //Log.d("addedX",String.valueOf(tempbutton.getX()));
                                //Log.d("addedY",String.valueOf(tempbutton.getY()));
                                //Log.d("addedHeight",String.valueOf(tempbutton.getHeight()));
                                tempbutton.setActivated( finaltempisactivated);
                                tempbutton.setPressed(finalAlerted);
                                if (finalAlerted){
                                    tempbutton.setTextColor(ColorAlerted);
                                }else if(tempbutton.isActivated()){
                                    tempbutton.setTextColor(ColorActivated);
                                }else {
                                    tempbutton.setTextColor(ColorInactivated);
                                }
                                tempbutton.setTextSize(getFitTextSize(15,tempbutton.getWidth(),(String) tempbutton.getText()));
                                tempbutton.setAllCaps(false);
                                if (StopRefreshView){return;}
                                tempbutton.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                                tempbutton.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

                                Log.d("Posted", "ButtonAdded");
                                added = true;
                            }
                        });

                        int trytimes=0;
                        while (!added) {
                            if (Thread.currentThread().isInterrupted()){StopRefreshView=true;StopGettingData=true;return;}
                            if (StopRefreshView){return;}
                            if (trytimes>100){
                                Snackbar prompt = Snackbar.make(fragment1.getView(),R.string.Your_Device_is_Too_Slow_to_Show_The_Apparatuses,Snackbar.LENGTH_LONG);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    //prompt.getView().setBackgroundColor(getResources().getColor(getTheme().getResources().getIdentifier("holo_blue_light", "color", "android"),null));
                                    prompt.getView().setBackgroundColor(getResources().getColor(colorPrimary.resourceId,null));
                                }else {
                                    //TypedValue typedvalue = new TypedValue();
                                    //getTheme().resolveAttribute(R.attr.colorPrimary,typedvalue,true);
                                    //prompt.getView().setBackgroundColor(getResources().getColor(colorPrimary.resourceId));
                                    prompt.getView().setBackgroundColor(colorPrimary.data);
                                }
                                prompt.show();
                                Log.d("Button","Wrong Adding");
                                StopRefreshView=true;return;}
                            if (Thread.currentThread().isInterrupted()){StopRefreshView=true;StopGettingData=true;return;}
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                StopRefreshView=true;
                                e.printStackTrace();
                                return;
                            }
                            trytimes++;
                        }

                        final ImageView tempimageview = new ImageView(Maincontext);
                        Drawable drawable = getDrawable(R.drawable.light_selector);
                        //drawable.setBounds(0,0, tempimageview.getWidth(),tempimageview.getHeight());
                        //drawable.setBounds(20,20, 50,50);

                        tempimageview.setImageDrawable(drawable);
                        //tempimageview.setBackground(drawable);
                        //tempimageview.setAdjustViewBounds(true);tempimageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        tempimageview.setId(tempbutton.getId() + 1000);
                        tempimageview.setX(tempbutton.getX() + tempbutton.getWidth() / 2 - getResources().getDimension(R.dimen.imageviewwidth) / 2);
                        tempimageview.setY(tempbutton.getY() + tempbutton.getHeight() / 2 - getResources().getDimension(R.dimen.imageviewwidth) / 2 - 25);
                        tempimageview.setVisibility(View.INVISIBLE);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (StopRefreshView){return;}
                                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.imageviewwidth), (int) getResources().getDimension(R.dimen.imageviewwidth));
                                layout.addView(tempimageview, 0, params2);
                                //Log.d("addedY2",String.valueOf(tempimageview.getY()));
                                Log.d("ButtonTrueID", String.valueOf(tempbutton.getId()));
                                Log.d("ImageTrueID", String.valueOf(tempimageview.getId()));
                                if (StopRefreshView){return;}
                                tempimageview.setActivated(tempbutton.isActivated());
                                if (StopRefreshView){return;}
                                //if (isActivated) {tempimageview.setVisibility(View.VISIBLE);}
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (StopRefreshView){return;}
                                        if (tempimageview.getHeight()==0&&!StopRefreshView){
                                            handler.postDelayed(this,5);
                                            Log.d("Posted", "PostedImageview");
                                            return;
                                        }
                                        if (StopRefreshView){return;}
                                        TranslateAnimation transanimation1 = new TranslateAnimation(0, 0,
                                                -tempbutton.getY() - tempbutton.getHeight(), 0);
                                        transanimation1.setFillAfter(true);
                                        transanimation1.setDuration(1000);
                                        transanimation1.setInterpolator(new BounceInterpolator());
                                        transanimation1.setStartOffset(100);
                                        TranslateAnimation transanimation2 = new TranslateAnimation(0, 0,
                                                -tempimageview.getY() - tempimageview.getHeight(), 0);
                                        transanimation2.setFillAfter(true);
                                        transanimation2.setDuration(1000);
                                        transanimation2.setInterpolator(new BounceInterpolator());
                                        transanimation2.setStartOffset(100);

                                        //ObjectAnimator objectAnimator1=ObjectAnimator.ofFloat(tempbutton,
                                        //        "TranslationY",-tempbutton.getY() - tempbutton.getHeight(),0);
                                        //ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(tempimageview,
                                        //        "TranslationY",-tempimageview.getY() - tempimageview.getHeight(),0);
                                        //AnimatorSet set=new AnimatorSet();
                                        //if (StopRefreshView){return;}
                                        //set.playTogether(objectAnimator1,objectAnimator2);
                                        //set.setDuration(1000);
                                        //set.setStartDelay(100);
                                        //set.setInterpolator(new BounceInterpolator());
                                        //if (StopRefreshView){return;}
                                        //tempbutton.setVisibility(View.VISIBLE);
                                        //tempimageview.setVisibility(View.VISIBLE);
                                        //if (StopRefreshView){return;}
                                        //set.start();

                                        //ViewGroup.LayoutParams layoutparams=tempimageview.getLayoutParams();
                                        //layoutparams.width=(int)getResources().getDimension(R.dimen.imageviewwidth);
                                        //layoutparams.height=(int)getResources().getDimension(R.dimen.imageviewwidth);
                                        //tempimageview.requestLayout();
                                        //tempimageview.setLayoutParams(layoutparams);

                                        tempbutton.setVisibility(View.VISIBLE);
                                        tempbutton.startAnimation(transanimation1);
                                        tempimageview.setVisibility(View.VISIBLE);
                                        //tempimageview.clearAnimation();
                                        tempimageview.setAnimation(transanimation2);
                                        tempbutton.setOnClickListener(onclicklistener);
                                        AllAddedToggles.add(tempbutton.getId());
                                        Log.d("TrueTextContainerWidth",String.valueOf(getResources().getDimension(R.dimen.toggle_size)));
                                        refreshing = false;
                                    }
                                },5);
                            }
                        });

                        trytimes=0;
                        while (refreshing) {
                            if (Thread.currentThread().isInterrupted()){StopRefreshView=true;StopGettingData=true;return;}
                            if (StopRefreshView){return;}
                            if (trytimes>100){
                                Snackbar prompt = Snackbar.make(fragment1.getView(),R.string.Your_Device_is_Too_Slow_to_Show_The_Apparatuses,Snackbar.LENGTH_LONG);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    //prompt.getView().setBackgroundColor(getResources().getColor(getTheme().getResources().getIdentifier("holo_blue_light", "color", "android"),null));
                                    prompt.getView().setBackgroundColor(getResources().getColor(colorPrimary.resourceId,null));
                                }else {
                                    //TypedValue typedvalue = new TypedValue();
                                    //getTheme().resolveAttribute(R.attr.colorPrimary,typedvalue,true);
                                    //prompt.getView().setBackgroundColor(getResources().getColor(colorPrimary.resourceId));
                                    prompt.getView().setBackgroundColor(colorPrimary.data);
                                }
                                prompt.show();
                                Log.d("Button&ImageView","Wrong Showing");
                                StopRefreshView=true;return;}
                            if (Thread.currentThread().isInterrupted()){StopRefreshView=true;StopGettingData=true;return;}
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                StopRefreshView=true;
                                e.printStackTrace();
                                return;
                            }
                            trytimes++;
                        }
                        if (StopRefreshView){return;}
                    }else {
                        if (StopRefreshView){return;}

                        //Data.getApparatuses().remove(index-1);
                        //whetherTooMany=true;
                        if (!firstSnackBarShown) {
                            Log.d("ApparatusOutOfBounds", "Won't Adopt");
                            Snackbar prompt = Snackbar.make(fragment1.getView(), R.string.Apparatus_Exceed_Bounds_of_Window_Got_to_Abandon_Some, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null);
                            prompt.getView().setBackgroundColor(colorPrimary.data);
                            prompt.show();
                            firstSnackBarShown=true;
                        }
                    }

                    index++;
                    added=false;refreshing=true;
                }

                if (StopRefreshView){return;}
                if (whetherTooMany){
                    Snackbar prompt =  Snackbar.make(fragment1.getView(), R.string.Too_Many_Apparatus_Got_to_Abandon_Excessives, Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    prompt.getView().setBackgroundColor(colorPrimary.data);
                    prompt.show();
                }


            }finally {
                setDefault();
            }

        }

        void setDefault(){
            this.usedbutton1=null;
            this.usedbutton2=null;
            this.usedbutton3=null;

            this.refreshing=true;
            this.added=false;
            this.prompts_added=false;
        }
    }

    boolean WhetherInArraylistInt (ArrayList<Integer> apparatus,int id){
        synchronized (apparatus) {
            for (int temp : apparatus) {
                if (temp == id) {
                    return true;
                }
            }
            return false;
        }
    }

    int WhichInArraylist(ArrayList arrayList,Object object){
        synchronized (arrayList) {
            int TargetIndex = -1;
            for (int index = 0; index < arrayList.size(); index++) {
                if (arrayList.get(index) == object) {
                    TargetIndex = index;
                }
            }
            //Log.d("TargetSize",String.valueOf(arrayList.size()));
            //Log.d("TargetIndex",String.valueOf(TargetIndex));
            //Log.d("TargetIndexID",String.valueOf(arrayList.get(TargetIndex)));
            return TargetIndex;
        }
    }

    boolean WhetherInCalled (int id){
        synchronized (calledTogglesDatas) {
            for (CalledTogglesData temp : calledTogglesDatas) {
                if (temp.id == id) {
                    return true;
                }
            }
            return false;
        }
    }

    int WhichIndexInCalled (int id){
        synchronized (calledTogglesDatas) {
            int TargetIndex = -1;
            ArrayList<CalledTogglesData> temp =calledTogglesDatas;
            for (int index = 0; index < temp.size(); index++) {
                if (temp.get(index).id == id) {
                    TargetIndex = index;
                }
            }
            return TargetIndex;
        }
    }

    boolean WhetherMathchExpected(int id,boolean activated){
        synchronized (calledTogglesDatas) {
            int TargetIndex = WhichIndexInCalled(id);
            if (TargetIndex >= 0 && activated == calledTogglesDatas.get(TargetIndex).activated) {
                calledTogglesDatas.remove(TargetIndex);
                return true;
            } else {
                return false;
            }
        }

    }

    boolean WhetherMatchAllExpected(){
        synchronized (calledTogglesDatas) {
            int howmany = calledTogglesDatas.size();
            int howmanygot = 0;
            for (CalledTogglesData data : (ArrayList<CalledTogglesData>) calledTogglesDatas.clone()) {
                for (Data.apparatuscfg data2 : Data.getApparatuses()) {
                    if (data.id == data2.id && data.activated == data2.isactivated) {
                        howmanygot++;
                    }
                }
            }
            if (howmany == howmanygot) {
                if (calledTogglesDatas.size() == howmany) {
                    calledTogglesDatas.clear();
                    return true;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

    }

    class updatedkeeper extends Thread{
        int sleeptime;int defaultsleeptime;int autorestoretime;int anomalyduration=0;
        Map<String,String> parameters=new HashMap<>(2);
        Data.whattodonext whattodonext=new Data.whattodonext() {
            volatile boolean activatedSet=false;
            @Override
            public void todo() {
                setDisconnected(false);
                ArrayList<Integer> NotHaveApparatusSet = new ArrayList<>();
                ArrayList<Data.apparatuscfg> tempApparatusSet = (ArrayList<Data.apparatuscfg>)Data.getApparatuses().clone();
                if (!tempApparatusSet.isEmpty()){
                    setAlertedLogNumber(tempApparatusSet.get(0).AlertedLogNumber);
                }
                for (final Data.apparatuscfg temp:tempApparatusSet){
                    activatedSet=false;
                    final Button tempbutton= (Button) findViewById(temp.id);
                    final ImageView tempimageview=(ImageView)findViewById(temp.id+1000);
                    final boolean isactivated=temp.isactivated;
                    final boolean Alerted= temp.Alerted;
                    final String URL = temp.URL;
                    final String name= temp.name;

                    if (tempbutton!=null&&tempimageview!=null) {
                        if(!WhetherInArraylistInt(ApparatusBeingControlled,tempbutton.getId())) {
                            //Log.d("UnControlledToggleID",String.valueOf(tempbutton.getId()));
                            if (WhetherInCalled(tempbutton.getId())){
                                if (WhetherMathchExpected(tempbutton.getId(),isactivated)) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            tempbutton.setBackground(getDrawable(R.drawable.animation_selector));
                                            tempbutton.setActivated(isactivated);
                                            tempimageview.setActivated(isactivated);
                                            if(isactivated){
                                                tempbutton.setTextColor(ColorActivated);
                                            }else {
                                                tempbutton.setTextColor(ColorInactivated);
                                            }
                                            tempbutton.setClickable(true);
                                            activatedSet = true;
                                        }
                                    });
                                    if (calledTogglesDatas.size()==0) {
                                        restoreSleeptime();
                                    }
                                }else {activatedSet=true;}
                            }else {
                                if(tempbutton.isActivated()!=isactivated){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Log.d("RegularUpdate",String.valueOf(tempbutton.getId())+"-"+tempbutton.getText());
                                            tempbutton.setActivated(isactivated);
                                            tempimageview.setActivated(isactivated);
                                            if(isactivated){
                                                tempbutton.setTextColor(ColorActivated);
                                            }else {
                                                tempbutton.setTextColor(ColorInactivated);
                                            }
                                            tempbutton.setClickable(true);
                                            activatedSet = true;
                                        }
                                    });
                                }
                                else {
                                    tempbutton.setClickable(true);
                                    activatedSet=true;
                                }
                            }

                            while (!activatedSet) {
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else {Log.d("TouchedToggleID",String.valueOf(tempbutton.getId()));}
                        if (Alerted) {
                            //ColorStateList tempx = tempbutton.getTextColors();
                            //Log.d("Alerted","Color:"+String.valueOf(tempbutton.getCurrentTextColor()));
                            //Log.d("Alerted","Color:"+String.valueOf(tempbutton.getTextColors().getDefaultColor()));
                            //Log.d("Alerted","Color:"+String.valueOf(tempbutton.getTextColors().toString()));
                            if (tempbutton.getCurrentTextColor()!=ColorAlerted) {
                                //Log.d("Alerted","Alerted");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tempbutton.setPressed(true);
                                        tempbutton.setTextColor(ColorAlerted);
                                    }
                                });
                            }
                        }else {
                            if (tempbutton.getCurrentTextColor()==ColorAlerted){
                                //Log.d("Alerted","UnAlerted");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tempbutton.setPressed(false);
                                        tempbutton.setTextColor(tempbutton.isActivated()?ColorActivated:ColorInactivated);
                                    }
                                });
                            }
                        }
                        //Log.d("URL&Name",URL+"&"+name);
                        tempbutton.setContentDescription(URL);
                        if (!tempbutton.getText().equals(name)) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tempbutton.setText(name);
                                    Log.d("Name",name);
                                }
                            });
                        }
                    }else {
                        NotHaveApparatusSet.add(temp.id);
                    }
                }
                final ImageView prompt_removed= (ImageView)findViewById(prompt_removed_ID);
                final ImageView prompt_changed= (ImageView) findViewById(prompt_changed_ID);
                final ImageView prompt_new = (ImageView) findViewById(prompt_new_ID);
                if (prompt_removed==null||prompt_changed==null||prompt_new==null){
                    return;
                }
                if (tempApparatusSet.size()>AllAddedToggles.size()){
                    //Snackbar.make(fragment1.getView(),"There are New Apparatus Assigned, Time to updateData",Snackbar.LENGTH_LONG).show();
                    //handler.post(new Runnable() {
                    //    @Override
                    //    public void run() {
                    //        Toast toast = Toast.makeText(MainActivity.this,"There are New Apparatuses Assigned, Time to updateData",Toast.LENGTH_SHORT);
                    //        toast.setGravity(Gravity.BOTTOM,0,-100);
                    //        toast.show();
                    //    }
                    //});
                    if (prompt_new.getVisibility()!=View.VISIBLE||prompt_removed.getVisibility()!=View.INVISIBLE) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                prompt_new.setVisibility(View.VISIBLE);
                                prompt_removed.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    if (NotHaveApparatusSet.size()>(tempApparatusSet.size()-AllAddedToggles.size())){
                        if (prompt_changed.getVisibility()!=View.VISIBLE) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    prompt_changed.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }else {
                        if (prompt_changed.getVisibility()!=View.INVISIBLE) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    prompt_changed.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }

                }
                else if  (tempApparatusSet.size()==AllAddedToggles.size()){
                    //handler.post(new Runnable() {
                    //    @Override
                    //    public void run() {
                    //        Toast toast = Toast.makeText(MainActivity.this,"There are Some Changes in Apparatuses, Time to updateData",Toast.LENGTH_SHORT);
                    //        toast.setGravity(Gravity.BOTTOM,0,-100);
                    //        toast.show();
                    //    }
                    //});
                    if (prompt_new.getVisibility()!=View.INVISIBLE||prompt_removed.getVisibility()!=View.INVISIBLE) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                prompt_new.setVisibility(View.INVISIBLE);
                                prompt_removed.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    if (NotHaveApparatusSet.size()>0) {
                        if (prompt_changed.getVisibility()!=View.VISIBLE) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    prompt_changed.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }else {
                        if (prompt_changed.getVisibility()!=View.INVISIBLE) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    prompt_changed.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                }
                else {
                    //handler.post(new Runnable() {
                    //    @Override
                    //    public void run() {
                    //        Toast toast = Toast.makeText(MainActivity.this,"There are Apparatuses Removed, Time to updateData",Toast.LENGTH_SHORT);
                    //        toast.setGravity(Gravity.BOTTOM,0,-100);
                    //        toast.show();
                    //    }
                    //});
                    if (prompt_removed.getVisibility()!=View.VISIBLE||prompt_new.getVisibility()!=View.INVISIBLE) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                prompt_removed.setVisibility(View.VISIBLE);
                                prompt_new.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    if (NotHaveApparatusSet.size()>0){
                        if (prompt_changed.getVisibility()!=View.VISIBLE) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    prompt_changed.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }else {
                        if (prompt_changed.getVisibility()!=View.INVISIBLE) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    prompt_changed.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }

                }
                //handler.post(new Runnable() {
                //    @Override
                //    public void run() {
                //        prompt_changed.setVisibility(View.VISIBLE);
                //        prompt_new.setVisibility(View.VISIBLE);
                //        prompt_removed.setVisibility(View.VISIBLE);
                //    }
                //});
            }
        };
        Data.OnsuccessProcess onsuccessProcess= new Data.OnsuccessProcess(0,whattodonext);
        boolean firstrapidly=false;
        @Override
        public void run() {
            parameters.put("user","henry");parameters.put("pass","yiweigang");
            Log.d("Updatekeeper","Started");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (StopDaemon){return;}
            }
            while (!StopDaemon){
                Log.d("Updatekeeper","Running");
                setDisconnected(Disconnected);
                setAlertedLogNumber(AlertedLogNumber);
                while (onDemandRefreshing){
                    Log.d("Updatekeeper","Sleep");
                    if (StopDaemon){return;}
                    while(refreshReturnRunning){
                        refreshinganirunning = true;
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            if (StopDaemon){
                                refreshinganirunning=false;
                                refreshReturnRunning=false;
                                return;}
                        }
                        //Log.d("DistanceProgressbar",String.valueOf(Math.abs(progressbar.getY()-originalPY)));
                        if (Math.abs(progressbar.getY()-originalPY)<20) {
                            Log.d("Abnormally Restore", "restoreProgressbar");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressbar.setY(originalPY);
                                }
                            });
                            refreshinganirunning = false;
                            refreshReturnRunning = false;
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (StopDaemon){return;}
                    }
                }
                while(refreshReturnRunning){
                    refreshinganirunning = true;
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (StopDaemon){
                            refreshinganirunning=false;
                            refreshReturnRunning=false;
                            return;}
                    }
                    //Log.d("DistanceProgressbar",String.valueOf(Math.abs(progressbar.getY()-originalPY)));
                    //Log.d("Dis",String.valueOf(refreshinganirunning));
                    if (Math.abs(progressbar.getY()-originalPY)<20) {
                        Log.d("Abnormally Restore", "restoreProgressbar");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressbar.setY(originalPY);
                            }
                        });
                        refreshinganirunning = false;
                        refreshReturnRunning = false;
                        break;
                    }
                }
                if (firstrapidly){
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (StopDaemon){return;}
                    }
                }
                if (!onDemandRefreshing){
                    OKHttpTool.asyncCustomPostFormforJsonObject(URL, parameters, onsuccessProcess, new OKHttpTool.processFailure() {
                        @Override
                        public void onFailure() {
                            setDisconnected(true);Log.d("UPDateSession", "Failed");
                        }
                    });
                }
                if (firstrapidly){
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (StopDaemon){return;}
                    }
                    firstrapidly=false;
                }
                if (sleeptime!=defaultsleeptime){
                    anomalyduration+=sleeptime;
                }
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (anomalyduration>=autorestoretime){
                    restoreSleeptime();
                }
            }
        }
        updatedkeeper(int sleeptime,int autorestoretime){
            this.sleeptime=sleeptime;
            this.defaultsleeptime=sleeptime;
            this.autorestoretime=autorestoretime;
        }

        public void setSleeptime(int sleeptime) {
            this.sleeptime = sleeptime;
            this.anomalyduration=0;
            this.firstrapidly=true;
        }

        public void restoreSleeptime(){
            this.sleeptime=this.defaultsleeptime;
            this.anomalyduration=0;
            this.firstrapidly=false;
        }

        public void setAutorestoretime(int autorestoretime) {
            this.autorestoretime = autorestoretime;
        }

        public void setDefaultsleeptime(int defaultsleeptime) {
            this.defaultsleeptime = defaultsleeptime;
        }
    }

    void setDisconnected (final boolean disconnected){
        this.Disconnected=disconnected;
        if (DisconnectionPrompt !=null) {
            if (DisconnectionPrompt.isVisible()!=(disconnected))
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetMenuItemStatus();
                        DisconnectionPrompt.setVisible(disconnected);
                        resetMenuItemOnTouch();

                    }
                });
        }
    }

    void setAlertedLogNumber(final int alertednumber){
        this.AlertedLogNumber =alertednumber;
        if (AlertPrompt!=null) {
            if (AlertPrompt.getVisibility()!=(alertednumber>0 ? View.VISIBLE : View.INVISIBLE)||!AlertPrompt.getText().toString().equals(String.valueOf(alertednumber)))
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("AlertedLogNumber",String.valueOf(alertednumber));
                        AlertPrompt.setText(String.valueOf(alertednumber));
                        AlertPrompt.setVisibility(alertednumber>0 ? View.VISIBLE : View.INVISIBLE);
                    }
                });
        }
    }

    class CalledTogglesData {
        int id;boolean activated;
        CalledTogglesData(int id,boolean activated){
            this.id=id;this.activated=activated;
        }
    }

    public class onclicklistener implements View.OnClickListener{

        Map<String,String> parameters=new HashMap<>(4);
        public void onClick(View v) {
            if (!onDemandRefreshing) {
                Button toggle = (Button) v;
                ImageView imageview = (ImageView) findViewById(v.getId() + 1000);
                parameters = new HashMap<>(4);
                parameters.put("user", "henry");
                parameters.put("pass", "yiweigang");
                parameters.put("id", String.valueOf(toggle.getId()));
                parameters.put("activated", String.valueOf(!toggle.isActivated()));


                //new communication(toggle,imageview).start();
                synchronized (ApparatusBeingControlled) {
                    ApparatusBeingControlled.add(toggle.getId());
                }
                processString processString = new processString(toggle);
                StopControlling = false;toggle.setClickable(false);
                OKHttpTool.asyncPostFormforString(URL3, parameters, processString, new processFailure(processString, parameters), 1);


                imageview.setVisibility(View.VISIBLE);
                //Log.d("ToggleID",String.valueOf(v.getId()));
                //Log.d("imageviewID",String.valueOf(imageview.getId()));
                //Log.d("imageviewActivated",String.valueOf(imageview.isActivated()));
                if (toggle.isActivated()) {
                    toggle.setBackground(getResources().getDrawable(R.drawable.light_on_connecting, null));
                } else {
                    toggle.setBackground(getResources().getDrawable(R.drawable.light_off_connecting, null));
                }

                //Toast toast = Toast.makeText(MainActivity.this,"Connecting",Toast.LENGTH_SHORT);
                //toast.setGravity(Gravity.BOTTOM,0,0);
                //toast.show();
                //toggle.setClickable(false);
            }
        }
    }

    class processString implements OKHttpTool.processString{
        Button toggle;//这样也许会有内存泄漏，最好换成int id（用findviewbyid找），但是感觉上好像只会有短期的泄露，所以还是不换，暂时观察一下。
        @Override
        public void onResponse(String value) {
            Log.d("hasOnResponse",(String) toggle.getText());
            if(StopControlling){return;}
            if (toggle.isActivated()){
                if(StopControlling){return;}
                toggle.setBackground(getDrawable(R.drawable.light_on_waiting));
            }
            else {
                if(StopControlling){return;}
                toggle.setBackground(getDrawable(R.drawable.light_off_waiting));
            }
            if(StopControlling){return;}
            synchronized (calledTogglesDatas) {
                calledTogglesDatas.add(new CalledTogglesData(toggle.getId(), !toggle.isActivated()));
            }
            if(StopControlling){return;}
            Snackbar prompt = Snackbar.make(fragment1.getView(), R.string.Connection_SUCCESSFUL, Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            prompt.getView().setBackgroundColor(colorPrimary.data);
            prompt.show();
            if(StopControlling){return;}
            synchronized (ApparatusBeingControlled) {
                int index=WhichInArraylist(ApparatusBeingControlled, toggle.getId());
                if (index>=0) {
                    ApparatusBeingControlled.remove(index);
                }
            }
            UpdateKeeper.setSleeptime(1000);
            if (UpdateKeeper!=null){
                UpdateKeeper.interrupt();}

        }
        processString(Button toggle){
            this.toggle=toggle;
        }
    }

    class processFailure implements OKHttpTool.processFailure {
        int trytimes=1;processString processString;Map<String,String> parameters;
        @Override
        public void onFailure() {
            if(StopControlling){return;}
            if (trytimes<=5){
                if(StopControlling){return;}
                Log.d("ConnectionFailed","ControlFailed");
                if (!(processString.toggle.getBackground().equals(getResources().getDrawable(R.drawable.connecting,null)))){
                    if(StopControlling){return;}
                    processString.toggle.setBackground(getResources().getDrawable(R.drawable.connecting,null));
                }
                if (!(((AnimationDrawable)processString.toggle.getBackground()).isRunning())){
                    if(StopControlling){return;}
                    ((AnimationDrawable)processString.toggle.getBackground()).start();

                }
                if(StopControlling){return;}
                OKHttpTool.asyncPostFormforString(URL3,parameters,processString,this,1);
                trytimes++;
            }
            else {
                if(StopControlling){return;}
                Snackbar prompt = Snackbar.make(fragment1.getView(), R.string.Normal_Connection_Failed_Try_Directly_Connecting, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                prompt.getView().setBackgroundColor(colorPrimary.data);
                prompt.show();
                if(StopControlling){return;}
                final OKHttpTool.processFailure processFailure = new OKHttpTool.processFailure() {
                    @Override
                    public void onFailure() {
                        Log.d("TotallyNOConnection","Can't Connect");
                        synchronized (ApparatusBeingControlled) {
                            if(StopControlling){return;}
                            int index=WhichInArraylist(ApparatusBeingControlled, processString.toggle.getId());
                            if (index>=0) {
                                ApparatusBeingControlled.remove(index);
                            }
                        }
                        if (processString.toggle.isActivated()){
                            if(StopControlling){return;}
                            processString.toggle.setBackground(getResources().getDrawable(R.drawable.animation_selector,null));
                        }
                        else {
                            if(StopControlling){return;}
                            processString.toggle.setBackground(getResources().getDrawable(R.drawable.animation_selector,null));
                        }
                        if(StopControlling){return;}
                        processString.toggle.setClickable(true);
                        if(StopControlling){return;}
                        Snackbar prompt = Snackbar.make(fragment1.getView(), R.string.Totally_NO_Connection_Cant_Connect, Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        prompt.getView().setBackgroundColor(colorPrimary.data);
                        prompt.show();
                    }
                };
                if(StopControlling){return;}
                final OKHttpTool.processString finalprocess = new OKHttpTool.processString() {
                    final int id=processString.toggle.getId();
                    @Override
                    public void onResponse(String value) {
                        int Status=0; int Alerted=0;Data.apparatuscfg apparatuscfg=null;
                        try{
                            String[] body=value.toUpperCase().split(",");
                            if (body[0].charAt(6)=='N'){
                                Status=2;
                            }else if(body[0].charAt(6)=='F'){
                                Status=1;
                            }
                            if (body[1].charAt(0)=='N'){
                                Alerted=1;
                            }else if(body[1].charAt(0)=='A'){
                                Alerted=2;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        synchronized (Data.getApparatuses()){
                            for (Data.apparatuscfg temp:Data.getApparatuses()){
                                if (temp.id==id){
                                    apparatuscfg=temp;
                                    break;
                                }
                            }
                        }
                        if (apparatuscfg!=null){
                            switch (Status){
                                case 2:
                                    apparatuscfg.setIsactivated(true);
                                    if(StopControlling){break;}
                                    processString.toggle.setBackground(getResources().getDrawable(R.drawable.animation_selector,null));
                                    processString.toggle.setActivated(true);
                                    processString.toggle.setTextColor(ColorActivated);
                                    (findViewById(id+1000)).setActivated(true);
                                    processString.toggle.setClickable(true);
                                    synchronized (ApparatusBeingControlled) {
                                        int index=WhichInArraylist(ApparatusBeingControlled, id);
                                        if (index>=0) {
                                            ApparatusBeingControlled.remove(index);
                                        }
                                    }
                                    break;
                                case 1:
                                    apparatuscfg.setIsactivated(false);
                                    if(StopControlling){break;}
                                    processString.toggle.setBackground(getResources().getDrawable(R.drawable.animation_selector,null));
                                    processString.toggle.setActivated(false);
                                    processString.toggle.setTextColor(ColorInactivated);
                                    (findViewById(id+1000)).setActivated(false);
                                    processString.toggle.setClickable(true);
                                    synchronized (ApparatusBeingControlled) {
                                        int index=WhichInArraylist(ApparatusBeingControlled, id);
                                        if (index>=0) {
                                            ApparatusBeingControlled.remove(index);
                                        }
                                    }
                                    break;
                                default:
                                    processFailure.onFailure();
                                    break;
                            }
                            switch (Alerted){
                                case 2:
                                    apparatuscfg.setAlerted(true);
                                    if(StopControlling){break;}
                                    break;
                                case 1:
                                    apparatuscfg.setAlerted(false);
                                    if(StopControlling){break;}
                                    break;
                                default:break;
                            }
                            File file=new File(MainActivity.fileDir,"Apparatusset");
                            try {
                                FileOutputStream out=new FileOutputStream(file);
                                ObjectOutputStream objout = new ObjectOutputStream(out);
                                objout.writeObject(Data.getApparatuses());
                                objout.flush();
                                objout.close();
                                out.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }


                };

                try {
                    if(StopControlling){return;}
                    String URL=(String) processString.toggle.getContentDescription();
                    if (parameters.get("activated").equals("true")){
                        URL+="1";
                    }else {
                        URL+="2";
                    }
                    Log.d("DirectURLis",URL);
                    OKHttpTool.asyncJsonString(URL,finalprocess,processFailure,1);
                }catch (Exception e){
                    Log.d("DirectConnection","IllegalURL",e);
                    processFailure.onFailure();
                }

            }
        }
        processFailure(processString processString,Map<String,String> parameters){
            this.processString=processString;this.parameters=parameters;
        }
    }


   class gesturelistner implements GestureDetector.OnGestureListener {
       float usedX;
       @Override
       public boolean onDown(MotionEvent e) {


           return true;
       }

       @Override
       public void onShowPress(MotionEvent e) {

       }

       @Override
       public boolean onSingleTapUp(MotionEvent e) {
           return false;
       }

       @Override
       public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
           return false;
       }

       @Override
       public void onLongPress(MotionEvent e) {

       }

       @Override
       public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

       return  false;}
   }


    public boolean onKeyDown(int keyCode,KeyEvent event) {
        //if(keyCode==KeyEvent.KEYCODE_BACK){
        //    return true;}//不执行父类点击事件
        //return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
        //不执行父类点击事件
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    //@Override //拦截这个可以实现上面一样的屏蔽back键作用的功能，只是这个时专门针对back按下的情况。
    //public void onBackPressed() {
    //    //super.onBackPressed();
    //}

    public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActivityMenu = menu;
        DisconnectionPrompt=menu.findItem(R.id.Menu_Disconnection);
        initiateToolbar(toolbar);
        initiateCriticalSigns(toolbar);
        return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       // Handle action bar item clicks here. The action bar will
       // automatically handle clicks on the Home/Up button, so long
       // as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();

       //noinspection SimplifiableIfStatement
       if (id == R.id.Menu_Settings) {
           Intent intent = new Intent(this,Settings.class);
           //intent.putExtra("Intention","StartSettings");
           //startActivity(intent);
           //ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this,R.anim.activity_enter,R.anim.activity_exit);
           //ActivityCompat.startActivityForResult(this,intent,1,options.toBundle());
           //getWindow().setEnterTransition(new Slide().setDuration(2000));
           //getWindow().setExitTransition(new Slide().setDuration(2000));
           //ActivityOptions SceneTransition = ActivityOptions.makeSceneTransitionAnimation(this,findViewById(R.id.Title_mainactivity),"Title");
           //ActivityOptions CustomTransition = ActivityOptions.makeCustomAnimation(this,R.anim.activity_enter,R.anim.activity_exit);
           //Bundle options = new Bundle();
           //options.putAll(SceneTransition.toBundle());options.putAll(CustomTransition.toBundle());
           //startActivity(new Intent(this, Settings.class));
           //
           //overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
           return true;
       }else if (id == R.id.Menu_Refresh){

           return true;
       }

       return super.onOptionsItemSelected(item);
   }

   /**
    * A native method that is implemented by the 'native-lib' native library,
    * which is packaged with this application.
    */
   //public native String stringFromJNI();

   // Used to load the 'native-lib' library on application startup.
   //static {
   //    System.loadLibrary("native-lib");
   //}
}
