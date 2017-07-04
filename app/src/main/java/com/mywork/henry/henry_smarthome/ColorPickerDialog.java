package com.mywork.henry.henry_smarthome;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.mywork.henry.henry_smarthome.Settings_Item.ApparatusConfiguration;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henry on 2017/1/16.
 */

public class ColorPickerDialog extends Fragment {
    View ColorView;
    ValueAnimator animator;
    myDialogAnimatorListener animatorlistener;
    Handler handler = new Handler();
    View ItemView;
    ColorPicker colorpicker;
    //private final int DialogViewID=15;

    myOnColorChangedListener myoncolorchangedlistener;
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
        Integer Color=null;
        if (ColorView!=null&&ColorView.getBackground() instanceof ColorDrawable){
            Color= ((ColorDrawable)ColorView.getBackground().mutate()).getColor();
        }
        if (Color==null){
            Log.d("Warning","Not Set Color");
            //dismiss();
            //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            //getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent,null));
            return;
        }
        final ViewGroup view = (ViewGroup) getView();
        colorpicker = (ColorPicker) view.findViewById(R.id.ColorPicker);
        colorpicker.addOpacityBar((OpacityBar) view.findViewById(R.id.OpacityBar));
        colorpicker.addSaturationBar((SaturationBar) view.findViewById(R.id.SaturationBar));
        colorpicker.addSVBar((SVBar) view.findViewById(R.id.SVBar));
        colorpicker.addValueBar((ValueBar) view.findViewById(R.id.ValueBar));
        Button Set = (Button) view.findViewById(R.id.SetColor);
        if (myoncolorchangedlistener==null){
            myoncolorchangedlistener = new myOnColorChangedListener(Set);
        }else {
            myoncolorchangedlistener.configure(Set);
        }
        colorpicker.setOnColorChangedListener(myoncolorchangedlistener);
        colorpicker.setColor(Color);
        handler.post(new Runnable() {
            int trytimes=0;
            @Override
            public void run() {
                View actualsetting = view.findViewById(R.id.colorpickerset);
                if (actualsetting!=null&&actualsetting.getHeight()!=0&&actualsetting.getWidth()!=0){
                    CreateAnimator(0,false,0).start();
                }else if (trytimes<100){
                    handler.postDelayed(this,1);
                    trytimes++;
                }
            }
        });
    }

    private class SetOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (colorpicker!=null&&ColorView!=null){
                ColorView.setBackgroundColor(colorpicker.getColor());
                dismiss();
            }
        }
    }

    private class myOnColorChangedListener implements ColorPicker.OnColorChangedListener{
        Button Set;
        @Override
        public void onColorChanged(int color) {
            Set.setBackgroundColor(color);
        }

        private myOnColorChangedListener (Button Set){
            configure(Set);
        }

        private myOnColorChangedListener configure (Button Set){
            this.Set=Set;
            return this;
        }
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("onCreateView","LifeCycle");
        final View view;
        view = inflater.inflate(R.layout.colorpicker, container, false);
        //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        //window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
        view.setAlpha(0);//view.setVisibility(View.INVISIBLE);
        //final String Temperature = "Temperature";final String Humidity = "Humidity";

        //this.view=view;
        return view;
    }

    public ColorPickerDialog setColorView (View ColorView){
        this.ColorView=ColorView;
        return this;
    }

    public Animator CreateAnimator(int transit, boolean enter, int nextAnim) {
        //Log.d("CreateAnimator","CreateAnimator");
        Bundle arguments = getArguments();
        float startX     = arguments.getFloat("startX",-1);
        float startY     = arguments.getFloat("startY",-1);
        int   startWidth = arguments.getInt("startWidth",-1);
        int   startHeight= arguments.getInt("startHeight",-1);
        int rootViewWidth=arguments.getInt("rootViewWidth",-1);
        int rootViewHeight=arguments.getInt("rootViewHeight",-1);
        View view = getView().findViewById(R.id.colorpickerset);
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
        colorpicker = null;
        ColorView=null;
        try {
            if (animator!=null) {
                animatorlistener.setReverse(true);
                animator.reverse();

            }else {
                getFragmentManager().beginTransaction().remove(ColorPickerDialog.this).commit();
            }
        }catch (Exception e){
            e.printStackTrace();
            getFragmentManager().beginTransaction().remove(ColorPickerDialog.this).commit();
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


    public ColorPickerDialog setConfigurationchanged (boolean configurationchanged){
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
                getFragmentManager().beginTransaction().remove(ColorPickerDialog.this).commit();

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
}
