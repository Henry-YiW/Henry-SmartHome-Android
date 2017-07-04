package com.mywork.henry.henry_smarthome.Settings_Item;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mywork.henry.henry_smarthome.ColorPickerDialog;
import com.mywork.henry.henry_smarthome.Data;
import com.mywork.henry.henry_smarthome.MainActivity;
import com.mywork.henry.henry_smarthome.OKHttpTool;
import com.mywork.henry.henry_smarthome.R;
import com.mywork.henry.henry_smarthome.Registration;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;


/**
 * Created by Henry on 2016/12/9.
 */


public class ApparatusConfiguration extends Fragment {
    private final int CellWidth=30;
    private myAdapter myAdapter;
    private final int space=50;
    private final myItemDecoration myItemDecoration = new myItemDecoration(space);
    Settings_Dialog Dialog;

    Handler handler = new Handler();

    private static volatile boolean OnSetting = true;
    updatedkeeper UpdateKeeper = null;
    final int sleeptime=5000;
    volatile boolean StopDaemon=false;

    volatile int columnCount;
    final int decorationOutrect=20;

    static final boolean[] Clicked= new boolean[]{true};//此项可以删除
    @Override
    public void onResume() {
        super.onResume();
        Clicked[0]=true;OnSetting=true;
        int trytimes=0;
        while (trytimes<200){
            if (UpdateKeeper==null||!UpdateKeeper.isAlive()){
                UpdateKeeper=new updatedkeeper(sleeptime);
                UpdateKeeper.setDaemon(true);
                UpdateKeeper.setName("UpdateKeeper");
                StopDaemon=false;
                UpdateKeeper.start();
                break;
            }
            trytimes++;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        StopDaemon = true;
        //Log.d("Stop","Stop");
        if (UpdateKeeper != null) {
            UpdateKeeper.interrupt();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings_content2,container,false);
        //GridLayout layout = (GridLayout) view;
        //Log.d("Padding",view.getWidth()+"");
        //int columnCount = (int) Math.floor(layout.getWidth()/CellWidth);
        //layout.setLayoutParams();GridLayout.spec

        final RecyclerView layout = (RecyclerView) view.findViewById(R.id.recyclerApparatus);

        handler.post(new Runnable() {
            int trytimes=0;
            @Override
            public void run() {
                if (getView()!=null&&getView().getWidth()!=0){
                    //final GridLayoutManager layoutmanager = new GridLayoutManager(getActivity(),3);
                    //columnCount=getView().getWidth()/((int) getResources().getDimension(R.dimen.toggle_size)+60);
                    //layoutmanager.setSpanCount(columnCount);
                    //layoutmanager.setSpanSizeLookup();
                    columnCount=getView().getWidth()/((int) getResources().getDimension(R.dimen.toggle_size)+decorationOutrect*2);
                    //columnCount=2;
                    Log.d("columnCount", String.valueOf(columnCount));
                    GridLayoutManager layoutmanager = new GridLayoutManager(getActivity(),columnCount);
                    //layoutmanager.setSpanCount(columnCount);
                    //layoutmanager.setMeasuredDimension();
                    layout.setLayoutManager(layoutmanager);
                    //layout.setRecyclerListener(new RecyclerView.RecyclerListener() {
                    //    @Override
                    //    public void onViewRecycled(RecyclerView.ViewHolder holder) {
                    //        Log.d("Recycle","Recycle");
                    //        ((myViewHolder)holder).releaseResources();
                    //    }
                    //});
                    layout.addItemDecoration(myItemDecoration);
                    layout.setHasFixedSize(false);
                    if (myAdapter==null){
                        myAdapter =new myAdapter(generateDateSet(),layout);
                    }else {
                        myAdapter.configure(generateDateSet(),layout);
                    }
                    layout.setAdapter(myAdapter);
                    //layout.setItemAnimator(new DefaultItemAnimator());
                    Clicked[0]=false;OnSetting=false;
                }else if (trytimes<100){
                    handler.postDelayed(this,10);
                    trytimes++;
                }
            }
        });

        int rootWidth =getActivity().getWindow().getAttributes().width;
        int rootHeight=getActivity().getWindow().getAttributes().height;
        //if (savedInstanceState!=null) {
        //    Dialog = (Settings_Dialog) savedInstanceState.getSerializable("Dialog");
        //}
        //if (Dialog!=null){
            //Dialog.resetPosition(rootWidth,rootHeight);
            //if (Dialog.isConfigurationchanged()){
                //Dialog.bluntlydismiss();
                if (savedInstanceState!=null&&savedInstanceState.getBoolean("Dialog.configurationchanged")) {
                    Fragment temp = getFragmentManager().findFragmentByTag("Settings_Dialog");
                    if (temp != null) {
                        getFragmentManager().beginTransaction().remove(temp).commit();
                    }
                }
                //Dialog.setConfigurationchanged(false);
            //}
        //}


        return view;
    }

    public static class Settings_Dialog extends Fragment {
        ValueAnimator animator;
        myDialogAnimatorListener animatorlistener;
        Handler handler = new Handler();
        View ItemView;
        //private final int DialogViewID=15;

        ColorTextOnclickListener colortextonclicklistener;
        ColorPickerDialog ColorPickerDialog;
        Registration.AddOrModifyButtonOnClickListener modifybuttononclicklistener;
        Registration.RegistrationExpandableListViewAdapter registrationexpandablelistviewadapter;
        //View view;

        private boolean configurationchanged=false;

        void setItemView (View ItemView){
            this.ItemView = ItemView;
        }

        OKHttpTool.processString processString = new OKHttpTool.processString() {
            @Override
            public void onResponse(String value) {

            }
        };
        OKHttpTool.processFailure processFailure = new OKHttpTool.processFailure() {
            @Override
            public void onFailure() {

            }
        };

        @Override
        public void onDestroy() {
            Log.d("onDestroy","LifeCycle");
            super.onDestroy();
        }

        @Override
        public void onViewStateRestored(Bundle savedInstanceState) {
            Log.d("onViewStateRestored","LifeCycle");
            //setInitialSavedState(null);
            if (savedInstanceState!=null) {//why always null
                Log.d("Cleared","LifeCycle");
                savedInstanceState.clear();
            }
            super.onViewStateRestored(savedInstanceState);
        }

        @Override
        public void onResume() {
            super.onResume();
            final Bundle configurations = getArguments();final String RegistrationURL;final String user;final String pass;
            if (configurations==null||configurations.isEmpty()||configurations.getString("RegistrationURL")==null
                    ||configurations.getString("user")==null||configurations.getString("pass")==null
                    ||configurations.getSerializable("Data")==null){
                Log.d("Warning","Not Set Configurations");
                //dismiss();
                //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
                //getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent,null));
                Clicked[0]=false;
                return;
            }
            RegistrationURL=configurations.getString("RegistrationURL");user=configurations.getString("user");pass=configurations.getString("pass");
            final ViewGroup view = (ViewGroup) getView();
            String id=null;String url=null;String name=null;Integer Color=null;String period=null;
            if (apparatuscfg!=null){
                id= String.valueOf(apparatuscfg.id);
                url=apparatuscfg.URL;
                name=apparatuscfg.name;
            }else if (dataset!=null){
                if (dataset.id!=null){
                    id= String.valueOf(dataset.id);
                    name=dataset.nameortime;

                }else {
                    id=null;
                    name=null;
                }
                Color=dataset.Color;
                url=dataset.URL;
                period= String.valueOf(dataset.Period);
            }

            Button done = (Button) view.findViewById(R.id.DoneButton);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            TextView ID=(TextView)view.findViewById(R.id.IDText);
            ID.setText(id);
            EditText URL=(EditText)view.findViewById(R.id.URLText);
            URL.setText(url);
            URL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        EditText text = (EditText)v;
                        if (text.getText().toString().isEmpty()){
                            text.setText("http://");
                            //Log.d("Selection",text.length()+"");
                            text.setSelection(text.length());
                        }
                    }
                }
            });
            EditText Name = (EditText) view.findViewById(R.id.NameText);
            Name.setText(name);
            EditText Period = (EditText) view.findViewById(R.id.PeriodText);
            if (apparatuscfg==null){
                TextView Title = (TextView) view.findViewById(R.id.TitleText);
                if (id!=null){
                    //View ColorSet = view.findViewById(R.id.ColorSet);
                    //ColorSet.setVisibility(View.VISIBLE);
                    Title.setText(R.string.electricity);
                    TextView ColorText = (TextView) view.findViewById(R.id.ColorText);
                    if (Color!=null) {
                        ColorText.setBackgroundColor(Color);
                        ColorText.setOnClickListener(colortextonclicklistener);
                    }
                }else {
                    if (dataset.Type==Data.Type_Temperature){
                        Title.setText(getString(R.string.temperature));
                    }else if (dataset.Type==Data.Type_Humidity){
                        Title.setText(getString(R.string.humidity));
                    }
                    View ColorSet = view.findViewById(R.id.ColorSet);
                    ColorSet.setVisibility(View.INVISIBLE);
                    Name.setVisibility(View.INVISIBLE);
                    ID.setVisibility(View.INVISIBLE);
                }
                Period.setText(period);
            }


            Button Modify = (Button)view.findViewById(R.id.ModifyButton);
            if (modifybuttononclicklistener==null){
                modifybuttononclicklistener=new Registration.AddOrModifyButtonOnClickListener(user,pass,RegistrationURL,view,processString,processFailure);
            }
            Modify.setOnClickListener(modifybuttononclicklistener.configure(user,pass,RegistrationURL,view,processString,processFailure));
            if (apparatuscfg!=null) {
                Switch Passive = (Switch) view.findViewById(R.id.PassiveSwitch);Passive.setChecked(apparatuscfg.Passive);
                Switch Persistent = (Switch) view.findViewById(R.id.PersistentSwitch);Persistent.setChecked(apparatuscfg.Persistent);
                Switch Alertable = (Switch) view.findViewById(R.id.AlertableSwitch);Alertable.setChecked(apparatuscfg.Alertable);
                final ExpandableListView SetRegulation = (ExpandableListView) view.findViewById(R.id.Advanced);
                List<Pair<Pair<Integer[], Integer>, List<Pair<String, Integer>>>> rawViewSet = new ArrayList<>();
                List<Pair<String, Integer>> Child1 = new ArrayList<>();
                Child1.add(new Pair<>("regulationsetting", R.layout.regulationsetting_child));//@android:drawable/arrow_down_float
                Integer[] Indicator = new Integer[]{R.id.setregulationindicator, getResources().getIdentifier("arrow_up_float", "drawable", "android"), getResources().getIdentifier("arrow_down_float", "drawable", "android")};
                rawViewSet.add(new Pair<>(new Pair<>(Indicator, R.layout.regulationsetting_group), Child1));
                int[] colorset = new int[]{MainActivity.colorPrimary.data, 0xffffffff, getResources().getColor(R.color.My_Orange), getResources().getColor(R.color.holo_orange_dark)};
                if (registrationexpandablelistviewadapter == null) {
                    registrationexpandablelistviewadapter = new Registration.RegistrationExpandableListViewAdapter(getActivity(), rawViewSet, colorset, SetRegulation, Regulations);
                }
                SetRegulation.setAdapter(registrationexpandablelistviewadapter.configure(getActivity(), rawViewSet, colorset, SetRegulation, Regulations));
                SetRegulation.setGroupIndicator(null);
                ((Data.MyViewsBasedExpandableListAdapter) SetRegulation.getExpandableListAdapter()).setListViewHeight(SetRegulation, handler);
            }
            handler.post(new Runnable() {
                int trytimes=0;
                @Override
                public void run() {
                    View actualsetting = view.findViewById(R.id.actualsettings);
                    if (actualsetting!=null&&actualsetting.getHeight()!=0&&actualsetting.getWidth()!=0){
                        CreateAnimator(0,false,0).start();
                    }else if (trytimes<100){
                        handler.postDelayed(this,1);
                        trytimes++;
                    }
                }
            });
        }

        @Override
        public void setInitialSavedState(SavedState state) {
            int realmIndex;
            Class Class = super.getClass();//很奇怪这里得到的不是父类的类。
            Class=getClass().getSuperclass();
            Log.d(Class.getName(),"Class");
            try {
                Field mIndex = Class.getDeclaredField("mIndex");
                mIndex.setAccessible(true);
                realmIndex=mIndex.getInt(this);
            } catch (NoSuchFieldException|IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
            Log.d("realmIndex",realmIndex+"");
            if (realmIndex >= 0) {
                throw new IllegalStateException("Fragment already active");
            }
        }

        Data.regulation[] Regulations=null;Data.apparatuscfg apparatuscfg=null;Data.dataset dataset=null;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.d("onCreateView","LifeCycle");
            final Bundle configurations = getArguments();//final String RegistrationURL;final String user;final String pass;
            if (configurations==null||configurations.isEmpty()||configurations.getString("RegistrationURL")==null
                    ||configurations.getString("user")==null||configurations.getString("pass")==null
                    ||configurations.getSerializable("Data")==null){
                Log.d("Warning","Not Set Configurations");
                //dismiss();
                //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
                //getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent,null));
                Clicked[0]=false;
                View view=inflater.inflate(R.layout.registration_warining,container,false);
                view.setBackgroundColor(0xff000000);
                return view;
            }
            //RegistrationURL=configurations.getString("RegistrationURL");user=configurations.getString("user");pass=configurations.getString("pass");
            Serializable Data = configurations.getSerializable("Data");
            if (Data instanceof Data.apparatuscfg) {
                Regulations = ((Data.apparatuscfg) Data).Regulations;
                apparatuscfg= (Data.apparatuscfg) Data;
                dataset=null;
            }else  if (Data instanceof Data.dataset){
                dataset= (Data.dataset) Data;
                apparatuscfg=null;
                Regulations=null;
            }


            //getDialog().setCanceledOnTouchOutside(false);
            //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            //getDialog().getWindow().setWindowAnimations(R.style.CustomDialogAnimation);
            //WindowManager.LayoutParams layoutparams = getDialog().getWindow().getAttributes();
            //layoutparams.gravity = RegistrationSet.getInt("Gravity");//Gravity.CENTER;
            //layoutparams.width = RegistrationSet.getInt("Width");//WindowManager.LayoutParams.WRAP_CONTENT;//=ViewGroup.LayoutParams.WRAP_CONTENT;
            //layoutparams.height = RegistrationSet.getInt("Height");//WindowManager.LayoutParams.WRAP_CONTENT;//=ViewGroup.LayoutParams.WRAP_CONTENT;
            //layoutparams.x = RegistrationSet.getInt("X");//看源码文档，如果Gravity没有设置为特定的值，这个是无效的。即使有效，也只是设置Gravity的Offset。
            //layoutparams.y = RegistrationSet.getInt("Y");//看源码文档，如果Gravity没有设置为特定的值，这个是无效的。即使有效，也只是设置Gravity的Offset。
            //getDialog().getWindow().setAttributes(layoutparams);
            //final Window window = getDialog().getWindow();
            //View view = inflater.inflate(R.layout.dialog_fragment_layout,  ((ViewGroup) window.findViewById(android.R.id.content)), false);//需要用android.R.id.content这个view

            if (colortextonclicklistener ==null){
                colortextonclicklistener = new ColorTextOnclickListener();

            }
            if (ColorPickerDialog == null){
                ColorPickerDialog=new ColorPickerDialog();
            }
            final View view;
            if (apparatuscfg!=null) {
                view = inflater.inflate(R.layout.settingsviewofapparatus, container, false);
            } else {
                view = inflater.inflate(R.layout.settingsviewofdataset, container, false);
            }
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
            //window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
            view.setAlpha(0);//view.setVisibility(View.INVISIBLE);
            //final String Temperature = "Temperature";final String Humidity = "Humidity";

            //this.view=view;
            return view;
        }

        //private class ApparatusConfigurationExpandableListViewAdapter extends Data.MyViewsBasedExpandableListAdapter{
        //
        //    ApparatusConfigurationExpandableListViewAdapter(Context context, List<Pair<Pair<Integer[], Integer>, List<Pair<String, Integer>>>> rawViewSet, ExpandableListView View) {
        //        super(context, rawViewSet, View);
        //    }
        //
        //
        //    @Override
        //    public View newChildView(int groupPosition, int childPosition, ViewGroup parent) {
        //        View v = super.newChildView(groupPosition, childPosition, parent);
        //        Calendar now = Calendar.getInstance();
        //        int Hour = now.get(Calendar.HOUR_OF_DAY);int Minute = now.get(Calendar.MINUTE);
        //        int Month = now.get(Calendar.MONTH)+1;int Day = now.get(Calendar.DAY_OF_MONTH);
        //        NumberPicker startMonth= (NumberPicker) v.findViewById(R.id.startMonth);
        //        NumberPicker startDay= (NumberPicker) v.findViewById(R.id.startDay);
        //        NumberPicker startHour= (NumberPicker) v.findViewById(R.id.startHour);
        //        NumberPicker startMinute= (NumberPicker) v.findViewById(R.id.startMinute);
        //        NumberPicker endMonth= (NumberPicker) v.findViewById(R.id.endMonth);
        //        NumberPicker endDay= (NumberPicker) v.findViewById(R.id.endDay);
        //        NumberPicker endHour= (NumberPicker) v.findViewById(R.id.endHour);
        //        NumberPicker endMinute= (NumberPicker) v.findViewById(R.id.endMinute);
        //        startMonth.setMinValue(1);startMonth.setMaxValue(12);startMonth.setValue(Month);
        //        endMonth.setMinValue(1);endMonth.setMaxValue(12);endMonth.setValue(Month);
        //        startDay.setMinValue(1);startDay.setMaxValue(31);startDay.setValue(Day);
        //        endDay.setMinValue(1);endDay.setMaxValue(31);endDay.setValue(Day);
        //        startHour.setMinValue(0);startHour.setMaxValue(24);startHour.setValue(Hour);
        //        endHour.setMinValue(0);endHour.setMaxValue(24);endHour.setValue(Hour);
        //        startMinute.setMinValue(0);startMinute.setMaxValue(59);startMinute.setValue(Minute);
        //        endMinute.setMinValue(0);endMinute.setMaxValue(59);endMinute.setValue(Minute);
        //
        //
        //        return v;
        //    }
        //}

        public Animator CreateAnimator(int transit, boolean enter, int nextAnim) {
            //Log.d("CreateAnimator","CreateAnimator");
            Bundle arguments = getArguments();
            float startX     = arguments.getFloat("startX",-1);
            float startY     = arguments.getFloat("startY",-1);
            int   startWidth = arguments.getInt("startWidth",-1);
            int   startHeight= arguments.getInt("startHeight",-1);
            int rootViewWidth=arguments.getInt("rootViewWidth",-1);
            int rootViewHeight=arguments.getInt("rootViewHeight",-1);
            View view = getView().findViewById(R.id.actualsettings);
            //float endX       = view.getX();
            //float endY       = view.getY();
            int   endWidth   = view.getWidth();//view.getMeasuredWidth()
            int   endHeight  = view.getHeight();
            float endX       = (rootViewWidth-endWidth)/2;
            float endY       = (rootViewHeight-endHeight)/2;
            if(startX<0||startY<0||startWidth<0||startHeight<0||endX<0||endY<0||endWidth<0||endHeight<0
                    ||rootViewWidth<0||rootViewHeight<0){
                return null;
            }
            //float originX = -(rootViewWidth-endWidth)/2;
            //float originY = -(rootViewHeight-endHeight)/2;
            if (animator!=null){
                animator.cancel();
            }
            animator = ValueAnimator.ofFloat(0,100);
            animator.setDuration(300);
            animator.removeAllUpdateListeners();
            if (animatorlistener==null){
                animatorlistener = new myDialogAnimatorListener(startX,startY,startWidth,startHeight,
                                                                endX,endY,endWidth,endHeight);
            }
            animator.addUpdateListener(animatorlistener.configure(startX,startY,startWidth,startHeight,
                                                                endX,endY,endWidth,endHeight));

            return animator;
        }

        @Override
        public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
            return super.onCreateAnimator(transit, enter, nextAnim);
        }

        public void dismiss() {
            Log.d("dismiss","dismiss");
            //clear();
            try {
                if (animator!=null) {
                    animatorlistener.setReverse(true);
                    animator.reverse();
                    //synchronized (Clicked){
                    //    Clicked[0]=false;
                    //}



                    //handler.postDelayed(new Runnable() {
                    //    @Override
                    //    public void run() {
                    //        animator.cancel();
                    //        getFragmentManager().beginTransaction().remove(Settings_Dialog.this).commit();
                    //    }
                    //},animator.getDuration());
                }else {
                    getFragmentManager().beginTransaction().remove(Settings_Dialog.this).commit();
                }
            }catch (Exception e){
                e.printStackTrace();
                getFragmentManager().beginTransaction().remove(Settings_Dialog.this).commit();
            }

        }

        void clear(){
            ViewGroup layout = (ViewGroup) ((ViewGroup) getView().findViewById(R.id.actualsettings)).getChildAt(0);
            if (layout!=null) {
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    if (child instanceof EditText){
                        ((EditText)child).setText("");
                    }else if (child instanceof Switch){
                        ((Switch)child).setChecked(false);
                    }else if (child instanceof ExpandableListView){
                        ((ExpandableListView)child).collapseGroup(0);
                    }
                }
            }
        }

        public void bluntlydismiss() {
            getFragmentManager().beginTransaction().remove(this).commit();
            Log.d("bluntly dismiss","dismiss");
        }

        //public void resetPosition (int rootWidth,int rootHeight){
        //    //int rootWidth = getView().getRootView().getWidth();
        //    //int rootHeight = getView().getRootView().getHeight();
        //    Log.d("getview",(getView()==null)+"");
        //    View view = this.view.findViewById(R.id.actualsettings);
        //    try{
        //
        //        view.setX((rootWidth-view.getWidth())/2);
        //        view.setY((rootHeight-view.getHeight())/2);
        //        Log.d("resetPosition","reset");
        //    }catch (Exception e){
        //        e.printStackTrace();
        //    }
        //}

        public Settings_Dialog setConfigurationchanged (boolean configurationchanged){
            this.configurationchanged=configurationchanged;
            return this;
        }

        public boolean isConfigurationchanged (){
            return configurationchanged;
        }

        private class myDialogAnimatorListener implements ValueAnimator.AnimatorUpdateListener{
            boolean reverse;
            //float originX;float originY;
            float startX;float startY;int startWidth;int startHeight;
            float endX;float endY;int endWidth;int endHeight;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //int id=DialogViewID;
                if  (getView()==null){
                    animation.cancel();
                    return;
                }
                View v = getView().findViewById(R.id.actualsettings);
                View rootview = getView();
                if (v==null){
                    return;
                }
                if (v.getVisibility()!=View.VISIBLE){
                    v.setVisibility(View.VISIBLE);
                }
                float fraction = animation.getAnimatedFraction();
                rootview.setAlpha(fraction);
                if (ItemView!=null){
                    OnSetting = (fraction != 0);
                    ItemView.setAlpha(1-fraction);
                }
                //getActivity().getWindow().getDecorView().getRootView().setAlpha(0.5f);
                v.getLayoutParams().width=Math.round(startWidth+(endWidth-startWidth)*fraction);
                v.getLayoutParams().height=Math.round(startHeight+(endHeight-startHeight)*fraction);
                v.requestLayout();
                v.setX(startX+(endX-startX)*fraction);

                v.setY(startY+(endY-startY)*fraction);

                if (fraction==0&&reverse){
                    //Log.d("hasRemovedFragment","HasRemoved");
                    clear();
                    getFragmentManager().beginTransaction().remove(Settings_Dialog.this).commit();
                    synchronized (Clicked){
                        Clicked[0]=false;
                    }
                }
            }

            myDialogAnimatorListener (float startX,float startY,int startWidth,int startHeight,float endX, float endY, int endWidth, int endHeight){
                configure(startX, startY, startWidth, startHeight, endX,  endY, endWidth, endHeight);
            }

            myDialogAnimatorListener configure (float startX,float startY,int startWidth,int startHeight,
                                                float endX, float endY, int endWidth, int endHeight){
                //this.originX=originX;this.originY=originY;
                this.startX=startX;this.startY=startY;this.startWidth=startWidth;this.startHeight=startHeight;
                this.endX=endX;this.endY=endY;this.endWidth=endWidth;this.endHeight=endHeight;
                this.reverse=false;
                return this;
            }

            void setReverse (boolean reverse){
                this.reverse=reverse;
            }
        }

        private class ColorTextOnclickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getBackground() instanceof ColorDrawable){
                    int Color = ((ColorDrawable)v.getBackground().mutate()).getColor();
                    if (ColorPickerDialog==null){
                        ColorPickerDialog = new ColorPickerDialog();
                    }
                    ColorPickerDialog.setColorView(v);
                }else {
                    try{
                        throw new IllegalArgumentException("Wrong Background Drawable--Not a ColorDrawable");
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private class myAdapter extends RecyclerView.Adapter<myViewHolder> {
        private RecyclerView recyclerView;
        ArrayList <Serializable> DataSet;
        final SparseIntArray ids = new SparseIntArray();
        private final int Temperature_ID=5000;private final int Humidity_ID=5001;
        @Override
        public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //TextView ApparatusView = new TextView(getActivity());
            //ApparatusView.setGravity(Gravity.CENTER|Gravity.BOTTOM);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apparatusviewinrecyclerview,parent,false);

            return new myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(myViewHolder holder, int position) {
            //Log.d("onBindViewHolder",holder.getItemView().toString()+"-"+position+"");
            Serializable temp = DataSet.get(position);
            holder.setData(temp);
            //if (holder.getTAG()==null){
            //    holder.setTAG(position);
            //    Log.d("Holder-New",((TextView)((ViewGroup)holder.
            //            getItemView()).getChildAt(0)).getText().toString()
            //            +"&"+holder.getTAG());
            //}else {
            //    Log.d("Holder",((TextView)((ViewGroup)holder.
            //            getItemView()).getChildAt(0)).getText().toString()
            //            +"&"+holder.getTAG());
            //}
            //TextView textview = (TextView) holder.getItemView().findViewById(R.id.test);
            TextView textview = (TextView) ((ViewGroup)holder.getItemView()).getChildAt(0);
            ////TextView textview = (TextView) holder.getItemView();
            //if (textview==null){
            //    synchronized (ids) {
            //        textview = (TextView) holder.getItemView().findViewById(ids.get(position));
            //    }
            //}
            // synchronized (ids) {
            //   ids.put(position, temp.id);
            //}
            textview.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            if (temp instanceof Data.apparatuscfg){
                Data.apparatuscfg apparatuscfg = (Data.apparatuscfg) temp;
                textview.setId(apparatuscfg.id);

                textview.setActivated(apparatuscfg.isactivated);
                textview.setText(apparatuscfg.name);
                textview.setBackground(apparatuscfg.isactivated ?
                        getResources().getDrawable(R.drawable.light_on, getActivity().getTheme()) :
                        getResources().getDrawable(R.drawable.light_off, getActivity().getTheme()));
                textview.setTextColor(apparatuscfg.Alerted?MainActivity.ColorAlerted:apparatuscfg.isactivated?MainActivity.ColorActivated:MainActivity.ColorInactivated);
                //holder.getItemView().getLayoutParams().width= (int) getResources().getDimension(R.dimen.toggle_size);
                //holder.getItemView().getLayoutParams().height= (int) getResources().getDimension(R.dimen.toggle_size_height);
                //holder.getItemView().requestLayout();
                //textview.getLayoutParams().width= (int) getResources().getDimension(R.dimen.toggle_size);
                //textview.getLayoutParams().height= (int) getResources().getDimension(R.dimen.toggle_size_height);
                //textview.requestLayout();
                //textview.setX(0);
            }else if (temp instanceof Data.dataset){
                Data.dataset dataset = (Data.dataset) temp;
                if (dataset.id!=null){
                    textview.setId(dataset.id);
                    textview.setActivated(false);
                    textview.setText(dataset.nameortime);
                    //Drawable background = getResources().getDrawable();
                    //background.setColorFilter(dataset.Color, PorterDuff.Mode.LIGHTEN);
                    //textview.setBackground(background);
                    ////textview.setBackgroundResource(R.drawable.electricity_icon_h);
                    textview.setBackgroundResource(R.drawable.electricity_icon_settings);
                    //textview.setBackgroundColor(dataset.Color-100);
                    textview.setTextColor(dataset.Color);
                }else if (dataset.Type==Data.Type_Temperature){

                    int id =Temperature_ID;
                    textview.setId(id);
                    textview.setActivated(false);
                    //textview.setText(R.string.temperature);
                    textview.setText("");
                    textview.setBackgroundResource(R.drawable.temperature_icon_settings);
                    //textview.getLayoutParams().width= (int) getResources().getDimension(R.dimen.dataset_size);
                    ////holder.getItemView().setX(holder.getItemView().getX()+20);
                    ////Log.d("Widths",holder.getItemView().getLayoutParams().width+"-"+textview.getLayoutParams().width+"");
                    ////textview.setBackgroundColor(dataset.Color-100);
                    ////textview.setTextColor(dataset.Color);
                    ////Log.d("TextLayoutParams", String.valueOf(textview.getLayoutParams().height));
                    //textview.getLayoutParams().height= (int) getResources().getDimension(R.dimen.dataset_size_height);
                    //textview.requestLayout();
                    //textview.setX(5);

                }else if (dataset.Type==Data.Type_Humidity){
                    int id =Humidity_ID;
                    textview.setId(id);
                    textview.setActivated(false);
                    //textview.setText(R.string.humidity);
                    textview.setText("");
                    textview.setBackgroundResource(R.drawable.humidity_icon_settings);
                    //textview.getLayoutParams().width= (int) getResources().getDimension(R.dimen.dataset_size);
                    //holder.getItemView().setX(holder.getItemView().getX()+20);
                    //textview.setBackgroundColor(dataset.Color-100);
                    //textview.setTextColor(dataset.Color);
                    //textview.requestLayout();
                }
            }
            //Log.d("Name&RealX", textview.getText()+"&&"+String.valueOf(textview.getX()));

        }


        @Override
        public int getItemCount() {
            return DataSet.size();
        }

        public void refresh (ArrayList<Serializable> DataSet){
            ArrayList <Serializable> oldSet = this.DataSet;
            ArrayList <Serializable> newSet = DataSet;
            this.DataSet=newSet;
            //for (int i=0;i<Math.max(newSet.size(),oldSet.size());i++){
            //    if (i<newSet.size()){
            //        if (i<oldSet.size()){
            //            notifyItemChanged(i);
            //
            //        }else {
            //            notifyItemInserted(i);
            //        }
            //    }else {
            //        notifyItemRemoved(i);
            //    }
            //}
            notifyDataSetChanged();
            requestLayout();
        }

        void requestLayout(){
            if (getView()!=null) {
                if (getView().getLayoutParams()==null){
                    return;
                }
                ////Log.d("recyclerviewWidth", String.valueOf(recyclerView.getWidth()));

                //for (int i=0;i<recyclerView.getChildCount();i++){
                //    recyclerView.getChildAt(i).getWidth()
                //}
                ////getView().getLayoutParams().height = recyclerView.getHeight()+100;
                ////Log.d("canScroll", String.valueOf(recyclerView.canScrollVertically(0)));
                ////getView().requestLayout();
                Log.d("ChildCount",recyclerView.getChildCount()+"&&"+DataSet.size());
                //recyclerView.getLayoutParams().height= (int) ((recyclerView.getChildCount()/columnCount+1)
                //        *(getResources().getDimension(R.dimen.toggle_size_height)+decorationOutrect*2));
                recyclerView.getLayoutParams().height= (int) ((DataSet.size()/columnCount+1)
                        *(getResources().getDimension(R.dimen.toggle_size_height))*0.93);
                recyclerView.requestLayout();
            }
        }

        public myAdapter configure (ArrayList<Serializable> DataSet,RecyclerView recyclerView){
            this.DataSet= DataSet;this.recyclerView=recyclerView;
            return this;
        }

        myAdapter (ArrayList<Serializable> DataSet,RecyclerView recyclerView){
            configure(DataSet, recyclerView);
        }
    }


    private class myItemDecoration extends RecyclerView.ItemDecoration{
        Drawable Frame;int space;

        myItemDecoration (Drawable Frame){
         this.Frame = Frame;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //if (parent.getChildAdapterPosition(view)%columnCount==0){
            //    //Log.d("ItemName&Position",((TextView)view).getText()+"&"+parent.getChildAdapterPosition(view));
            //    outRect.left = 20;
            //}else{
            //    outRect.left = 0;
            //} 不知道为什么这样会导致不居中！！
            outRect.left = decorationOutrect;
            outRect.right = decorationOutrect;
            outRect.bottom = decorationOutrect;
            // Add top margin only for the first item to avoid double space between items
            //Log.d("Name",parent.getChildAdapterPosition(view)+"");
            //Log.d("ItemName&Position",((TextView)((ViewGroup)view).getChildAt(0)).getText()+"&"+parent.getChildAdapterPosition(view));
            if (((float)parent.getChildAdapterPosition(view)/columnCount) < 1.0) {
                //Log.d("Name",((TextView)view).getText().toString());
                outRect.top = decorationOutrect;
            }else{
                outRect.top = 0;
            }
            //super.getItemOffsets(outRect, view, parent, state);
        }

        myItemDecoration (int space){
            this.space=space;
        }
    }

    private class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        //DialogFragment Dialog;
        private Serializable Data;
        private Integer TAG;
        myViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        View getItemView(){
            return super.itemView;
        }


        void releaseResources (){
            if (Dialog!=null){
                Dialog.dismiss();
            }
        }

        public void setData(Serializable Data) {
            this.Data = Data;
        }

        public void setTAG (int TAG){
            this.TAG = TAG;
        }

        public Integer getTAG() {
            return TAG;
        }

        public Serializable getData() {
            return Data;
        }

        @Override
        public void onClick(View v) {
            synchronized (Clicked) {
                if (!Clicked[0]) {
                    Clicked[0] = true;
                    if (Dialog == null) {
                        Dialog = new Settings_Dialog();
                    }
                    if (Dialog.isAdded()) {
                        Dialog.dismiss();
                    }
                    Bundle configurations = new Bundle();
                    View itemview = getItemView();
                    View rootview = getView();
                    configurations.putString("user", "henry");
                    configurations.putString("pass", "yiweigang");
                    configurations.putString("RegistrationURL", "fafaf");
                    int[] location = new int[2];
                    itemview.getLocationOnScreen(location);
                    configurations.putFloat("startX", location[0]);
                    configurations.putFloat("startY", location[1]);
                    configurations.putInt("startWidth", itemview.getWidth());
                    configurations.putInt("startHeight", itemview.getHeight());
                    configurations.putInt("rootViewWidth", rootview.getRootView().getWidth());
                    configurations.putInt("rootViewHeight", rootview.getRootView().getHeight());
                    //ArrayList<String> Regulations = new ArrayList<>();
                    //Regulations.add("{\"Item\":\"ActiveTime\",\"StartDate\":[\"8-9\",\"6-3\"],\"EndTime\":[\"16:25:00\",\"18:23:00\"]}");
                    //Regulations.add("{\"Item\":\"ActiveTime\",\"StartDate\":[\"8-9\",\"6-3\"],\"EndTime\":\"16:25:00\"}");
                    //Regulations.add("{\"Item\":\"SleepTime\",\"StartDate\":[\"8-9\",\"6-3\"]}");
                    //Regulations.add("{\"Item\":\"ActiveTime\",\"StartDate\":[\"8-9\",\"6-3\"],\"EndTime\":\"16:25:00\"}");
                    //configurations.putSerializable("Regulations",Regulations);
                    //Data.apparatuscfg apparatuscfg=null;
                    //ArrayList <Data.apparatuscfg> apparatuscfgs = (ArrayList<Data.apparatuscfg>) Data.getApparatuses().clone();
                    //for(Data.apparatuscfg temp:apparatuscfgs){
                    //    if (temp.id == itemview.getId()){
                    //        apparatuscfg=temp;
                    //        break;
                    //    }
                    //}
                    configurations.putSerializable("Data",getData());
                    //if (rootview!=null&&rootview.getWidth()!=0&&rootview.getHeight()!=0){
                    //    configurations.putFloat("endX",rootview.getX());
                    //    configurations.putFloat("endY",rootview.getY());
                    //    configurations.putInt("endWidth",ViewGroup.LayoutParams.WRAP_CONTENT);
                    //    configurations.putInt("endHeight",ViewGroup.LayoutParams.WRAP_CONTENT);
                    //}

                    Fragment temp = getFragmentManager().findFragmentByTag("Settings_Dialog");
                    if (temp != null) {
                        getFragmentManager().beginTransaction().remove(temp).commit();
                        Log.d("hasRemovedFragment", "Removed");
                    }
                    if (!Dialog.isAdded()) {
                        Dialog.setArguments(configurations);
                        Dialog.setItemView(itemview);

                        Fragment temp2 = getFragmentManager().findFragmentByTag("Settings_Dialog");
                        if (temp2 == null) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                            ft.add(R.id.settings, Dialog, "Settings_Dialog");
                            ft.commit();
                            //Dialog.show(getFragmentManager(),"Settings_Dialog");
                        }
                    }
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    private ArrayList<Serializable> generateDateSet (){
        final ArrayList<Serializable> DataSet = new ArrayList<Serializable>((ArrayList<Data.apparatuscfg>)Data.getApparatuses().clone());
        if (Data.getDataset(Data.Type_Temperature).size()>0){
            DataSet.add(Data.getDataset(Data.Type_Temperature).get(0));}
        if (Data.getDataset(Data.Type_Humidity).size()>0){
            DataSet.add(Data.getDataset(Data.Type_Humidity).get(0));}
        if (Data.getDataset(Data.Type_Electricity).size()>0){
            DataSet.addAll((ArrayList<Data.dataset>) Data.getDataset(Data.Type_Electricity).clone());}
        return DataSet;
    }

    private class updatedkeeper extends Thread {
        int sleeptime;int defaultsleeptime;
        @Override
        public void run() {
            Log.d("Updatekeeper","ApparatusConfigurationsStarted");
            while (!StopDaemon){
                while (OnSetting||Clicked[0]){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //Log.d("Clicked & OnSetting",String.valueOf(OnSetting)+String.valueOf(Clicked[0]));
                if (!OnSetting&&!Clicked[0]) {
                    Log.d("Updatekeeper","ApparatusConfigurationsRunning");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (myAdapter!=null)
                            myAdapter.refresh(generateDateSet());
                            //handler.postDelayed(new Runnable() {
                            //    @Override
                            //    public void run() {
                            //        myAdapter.requestLayout();
                            //    }
                            //},1);
                        }
                    });

                }
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        updatedkeeper(int sleeptime){
            this.sleeptime=sleeptime;
            this.defaultsleeptime=sleeptime;

        }

        public void setDefaultsleeptime(int defaultsleeptime) {
            this.defaultsleeptime = defaultsleeptime;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("Dialog.configurationchanged",true);
        //outState.putInt("rootWidth",getView().getWidth());
        //outState.putInt("rootHeight",getView().getHeight());
        super.onSaveInstanceState(outState);
    }

    //private class getConfiguration {
    //    private volatile int index=0;
    //    ArrayList<Data.apparatuscfg> apparatuscfgset;
    //
    //    Drawable getBackground (int id){
    //        for (Data.apparatuscfg temp:apparatuscfgset){
    //            if (temp.id==id){
    //                if (temp.isactivated){
    //                    return getResources().getDrawable(R.drawable.light_on,getActivity().getTheme());
    //                }else {
    //                    return getResources().getDrawable(R.drawable.light_off,getActivity().getTheme());
    //                }
    //            }
    //        }
    //        return null;
    //    }
    //
    //    int getId (){
    //        index++;
    //        return apparatuscfgset.get(index).id;
    //    }
    //
    //    void refresh (ArrayList<Data.apparatuscfg> apparatuscfgset){
    //        this.apparatuscfgset=apparatuscfgset;
    //    }
    //}
}
