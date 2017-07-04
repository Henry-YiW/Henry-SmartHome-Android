package com.mywork.henry.henry_smarthome;

import android.animation.Animator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Henry on 2016/07/18.
 */

public class Registration extends DialogFragment {
    Bundle State;volatile String AddButtonDescription;
    Handler handler=new Handler();
    RegistrationExpandableListViewAdapter registrationexpandablelistviewadapter;
    OKHttpTool.processString processString = new OKHttpTool.processString() {
        @Override
        public void onResponse(String value) {
            View view=null;
            try {
                Log.d("Body",value);
                view=getView().findViewById(R.id.AddButton);
                final TextView Count = (TextView) getView().findViewById(R.id.Count);
                synchronized ((TextView) getView().findViewById(R.id.Count)) {
                    try {
                        Count.setText(Count.getText().length() != 0 ? String.valueOf(Integer.parseInt((String) Count.getText()) + 1) : "1");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Count.setText("1");
                    }
                }
                clear();
            }catch (Exception e){
                e.printStackTrace();
            }
            resetEnabled(view,AddButtonDescription);
        }
    };

    OKHttpTool.processFailure processFailure = new OKHttpTool.processFailure() {
        @Override
        public void onFailure() {
            View view=null;
            try {
                Snackbar prompt = Snackbar.make(getView(), "Failed to Register", Snackbar.LENGTH_SHORT);
                prompt.getView().setBackgroundColor(FormatFactory.getColorWithoutAlpha(MainActivity.colorPrimary.data)+0x80_000000);
                prompt.show();
                view=getView().findViewById(R.id.AddButton);
            }catch (Exception e){
                e.printStackTrace();
            }
            resetEnabled(view,AddButtonDescription);
        }
    };

    //变量是不存在复写的，所以如果一个子类有一个和父类重名的成员变量，那类型声明就决定了那个引用调用的是这个子类本身的变量还是父类的
    //例如，如果，类型声明是父类的，那个引用会调用向上转型的子类的父类的那个变量，而如果类型声明就是那个子类，那个引用就会调用这个子类的那个变量
    //而方法是存在复写的，如果一个子类复写了父类的某个方法，那不管类型声明是它的父类还是它本身，那个引用都只会调用那个子类的复写了的方法，而不会调用父类的同名方法。
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //getView().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //getView().getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
        //getView().requestLayout();
        //setShowsDialog();
        //这个在ShowsDialog为true后就是真正的DialogFragment，而如果是False，那就是一个普通的fragment了
        //这个的原理就是用onCreateDialog生成一个generic(一般，通用的) Dialog，然后在onCreateView里生成一个view放到Dialog里面。
        //所以如果你是自己复写了onCreateDialog方法---创建了自己的带有View的dialog，就不要再复写onCreateView创建另一个view了
        //因为如果这样，就会导致你自己生成的Dialog的view被后面的view所重载(也可能被addContentView加载在原来view的后面)，你自己生成的Dialog也就变得混乱而失去了意义。
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("onCreateView","HasRun");
        final Bundle RegistrationSet = getArguments();final String RegistrationURL;final String user;final String pass;
        if (RegistrationSet==null||RegistrationSet.isEmpty()||RegistrationSet.getString("RegistrationURL")==null
                ||RegistrationSet.getString("user")==null||RegistrationSet.getString("pass")==null){
            Log.d("Warning","Not Set Configurations");
            //dismiss();
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent,null));
            return inflater.inflate(R.layout.registration_warining,container,false);
        }
        RegistrationURL=RegistrationSet.getString("RegistrationURL");user=RegistrationSet.getString("user");pass=RegistrationSet.getString("pass");
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE|Window.FEATURE_SWIPE_TO_DISMISS);
        getDialog().getWindow().setWindowAnimations(R.style.CustomDialogAnimation);
        //WindowManager.LayoutParams layoutparams = getDialog().getWindow().getAttributes();
        //layoutparams.gravity = RegistrationSet.getInt("Gravity");//Gravity.CENTER;
        //layoutparams.width = RegistrationSet.getInt("Width");//WindowManager.LayoutParams.WRAP_CONTENT;//=ViewGroup.LayoutParams.WRAP_CONTENT;
        //layoutparams.height = RegistrationSet.getInt("Height");//WindowManager.LayoutParams.WRAP_CONTENT;//=ViewGroup.LayoutParams.WRAP_CONTENT;
        //layoutparams.x = RegistrationSet.getInt("X");//看源码文档，如果Gravity没有设置为特定的值，这个是无效的。即使有效，也只是设置Gravity的Offset。
        //layoutparams.y = RegistrationSet.getInt("Y");//看源码文档，如果Gravity没有设置为特定的值，这个是无效的。即使有效，也只是设置Gravity的Offset。
        //getDialog().getWindow().setAttributes(layoutparams);
        final View view = inflater.inflate(R.layout.registration,container,false);
        view.setBackgroundColor(getResources().getColor(R.color.default_backgroundColor));
        //RegistrationSet.clear();
        EditText ID=(EditText)view.findViewById(R.id.IDText);EditText URL=(EditText)view.findViewById(R.id.URLText);
        //ID.setKeyListener(new KeyListener() {
        //    @Override
        //    public int getInputType() {
        //        return 0;
        //    }
        //
        //    @Override
        //    public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
        //        return false;
        //    }
        //
        //    @Override
        //    public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
        //        return false;
        //    }
        //
        //    @Override
        //    public boolean onKeyOther(View view, Editable text, KeyEvent event) {
        //        return false;
        //    }
        //
        //    @Override
        //    public void clearMetaKeyState(View view, Editable content, int states) {
        //
        //    }
        //});
        //ID.setOnKeyListener(new View.OnKeyListener() {
        //
        //    @Override
        //    public boolean onKey(View v, int keyCode, KeyEvent event) {
        //        return false;
        //    }
        //});
        ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                //String text=s.toString();

                String temp = "";boolean whethertochange=false;
                //Log.d("Char",String.valueOf((char)0));
                for (int i = 0; i < s.length(); i++) {
                    //Log.d("Char",String.valueOf((int)s.charAt(i)));
                    if (Character.isDigit(s.charAt(i))) {
                        temp += String.valueOf(s.charAt(i));
                    }else {
                        whethertochange=true;
                    }
                }
                if (!temp.isEmpty()) {
                    if (Integer.parseInt(temp) > 254){
                        temp="254";
                        whethertochange=true;
                    }
                }
                //Log.d("temp", temp);
                if (whethertochange){
                s.replace(0, s.length(), temp);
                }

            }
        });
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
        //URL.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        EditText text = (EditText)v;
        //        if (text.getText().toString().isEmpty()){
        //            text.setText("http://");
        //        }
        //    }
        //});
        final ExpandableListView SetRegulation = (ExpandableListView)view.findViewById(R.id.Advanced);
        Button Add = (Button)view.findViewById(R.id.AddButton);
        AddButtonDescription=Add.getContentDescription().toString();
        if (getCustomState()!=null) {
            Add.setEnabled(getCustomState().getBoolean(Add.getContentDescription()+"Enabled", true));
        }
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("user",user);
                parameters.put("pass",pass);
                String name=((EditText)view.findViewById(R.id.NameText)).getText().toString();
                String URL=((EditText)view.findViewById(R.id.URLText)).getText().toString();
                String id=((EditText)view.findViewById(R.id.IDText)).getText().toString();
                String passive=String.valueOf(((Switch)view.findViewById(R.id.PassiveSwitch)).isChecked());
                String Per=String.valueOf(((Switch)view.findViewById(R.id.PersistentSwitch)).isChecked());
                String Alertable=String.valueOf(((Switch)view.findViewById(R.id.AlertableSwitch)).isChecked());
                if (!name.isEmpty()){
                parameters.put("name",name);}
                if (!URL.isEmpty()){
                parameters.put("URL",URL);}



                if (!id.isEmpty()){
                parameters.put("id",id);}else{
                    processFailure.onFailure();
                    return;
                }
                if (!passive.isEmpty()){
                parameters.put("passive",passive);}
                if (!Per.isEmpty()){
                parameters.put("Per",Per);}
                if (!Alertable.isEmpty()){
                    parameters.put("Alertable",Alertable);}

                OKHttpTool.asyncPostFormforString(RegistrationURL, parameters,processString,processFailure,1);
                v.setEnabled(false);

            }
        });
        Button Done = (Button)view.findViewById(R.id.DoneButton);
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        List <Pair<Pair<Integer[],Integer>,List<Pair<String,Integer>>>> rawViewSet = new ArrayList<>();
        List<Pair<String,Integer>> Child1 = new ArrayList<>();
        Child1.add(new Pair<>("regulationsetting",R.layout.regulationsetting_child));//@android:drawable/arrow_down_float
        Integer[] Indicator = new Integer[]{R.id.setregulationindicator,getResources().getIdentifier("arrow_up_float","drawable","android"),getResources().getIdentifier("arrow_down_float","drawable","android")};
        rawViewSet.add(new Pair<>(new Pair<>(Indicator,R.layout.regulationsetting_group),Child1));
        int[] colorset=new int[]{MainActivity.colorPrimary.data,0xffffffff,getResources().getColor(R.color.My_Orange),getResources().getColor(R.color.holo_orange_dark)};
        if (registrationexpandablelistviewadapter==null){
            registrationexpandablelistviewadapter=new RegistrationExpandableListViewAdapter(getActivity(),rawViewSet,colorset,SetRegulation);
        }
        SetRegulation.setAdapter(registrationexpandablelistviewadapter.configure(getActivity(),rawViewSet,colorset,SetRegulation));
        SetRegulation.setGroupIndicator(null);
        ((Data.MyViewsBasedExpandableListAdapter)SetRegulation.getExpandableListAdapter()).setListViewHeight(SetRegulation,handler);

        //SetRegulation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        Log.d("View",view.toString());
        //    }
        //});
        //SetRegulation.listener

        //handler.postDelayed(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        //            View tview = (View) SetRegulation.getExpandableListAdapter().getChild(0,0);
        //            tview.setVisibility(View.INVISIBLE);
        //        }catch (Exception e){
        //            e.printStackTrace();
        //            handler.postDelayed(this,50);
        //        }
        //    }
        //},100);
        //Pair.create()
        //new Transition(getContext(),null).
        //this.setSharedElementEnterTransition();

        //handler.postDelayed(new Runnable() {
        //    @Override
        //    public void run() {
        //        Log.d("RegulationActivated",((View)SetRegulation.getExpandableListAdapter().getGroup(0)).isActivated()+"");
        //        handler.postDelayed(this,100);
        //    }
        //},100);

        return view;
    }

    public static class AddOrModifyButtonOnClickListener implements View.OnClickListener{
        String user;String pass;String RegistrationURL;View view;
        OKHttpTool.processString processString;OKHttpTool.processFailure processFailure;

        public AddOrModifyButtonOnClickListener (String user,String pass,String registrationURL,View view,
                    OKHttpTool.processString processString,OKHttpTool.processFailure processFailure){
            configure(user, pass, registrationURL, view, processString, processFailure);
        }

        public AddOrModifyButtonOnClickListener configure (String user,String pass,String registrationURL,View view,
                               OKHttpTool.processString processString,OKHttpTool.processFailure processFailure){
            this.user=user;this.pass=pass;this.RegistrationURL=registrationURL;this.view=view;
            this.processString=processString;this.processFailure=processFailure;
            return this;
        }

        @Override
        public void onClick(View v) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("user",user);
            parameters.put("pass",pass);
            String name=((EditText)view.findViewById(R.id.NameText)).getText().toString();
            String URL=((EditText)view.findViewById(R.id.URLText)).getText().toString();
            String id=((TextView)view.findViewById(R.id.IDText)).getText().toString();
            String passive=String.valueOf(view.findViewById(R.id.PassiveSwitch)!=null?((Switch)view.findViewById(R.id.PassiveSwitch)).isChecked():"");
            String Per=String.valueOf(view.findViewById(R.id.PersistentSwitch)!=null?((Switch)view.findViewById(R.id.PersistentSwitch)).isChecked():"");
            String Alertable=String.valueOf(view.findViewById(R.id.AlertableSwitch)!=null?((Switch)view.findViewById(R.id.AlertableSwitch)).isChecked():"");
            //String startMonthText = getContentDescription((ViewGroup) view,R.id.startMonth);
            //String startDayText = getContentDescription((ViewGroup) view,R.id.startDay);
            //String startHourText = getContentDescription((ViewGroup) view,R.id.startHour);
            //String startMinuteText = getContentDescription((ViewGroup) view,R.id.startMinute);
            //String endMonthText = getContentDescription((ViewGroup) view,R.id.endMonth);
            //String endDayText = getContentDescription((ViewGroup) view,R.id.endDay);
            //String endHourText = getContentDescription((ViewGroup) view,R.id.endHour);
            //String endMinuteText = getContentDescription((ViewGroup) view,R.id.endMinute);
            //Log.d("startMonthText",startMonthText);
            JSONObject Regulations = new JSONObject();
            ArrayList<View> startMonth = new ArrayList<>();findViewsById(startMonth, (ViewGroup) view,R.id.startMonth);
            ArrayList<View> startDay = new ArrayList<>();findViewsById(startDay, (ViewGroup) view,R.id.startDay);
            ArrayList<View> startHour = new ArrayList<>();findViewsById(startHour, (ViewGroup) view,R.id.startHour);
            ArrayList<View> startMinute = new ArrayList<>();findViewsById(startMinute, (ViewGroup) view,R.id.startMinute);
            ArrayList<View> endMonth = new ArrayList<>();findViewsById(endMonth, (ViewGroup) view,R.id.endMonth);
            ArrayList<View> endDay = new ArrayList<>();findViewsById(endDay, (ViewGroup) view,R.id.endDay);
            ArrayList<View> endHour = new ArrayList<>();findViewsById(endHour, (ViewGroup) view,R.id.endHour);
            ArrayList<View> endMinute = new ArrayList<>();findViewsById(endMinute, (ViewGroup) view,R.id.endMinute);
            int MaxIndex=0; ArrayList[] temps = new ArrayList[]{startMonth,startDay,startHour,startMinute,endMonth,endDay,endHour,endMinute};
            for (ArrayList temp:temps){
                MaxIndex=Math.max(MaxIndex,temp.size());
            }
            //Log.d("MaxIndex",MaxIndex+"");
            ArrayList<String> realStartMonth=processArrayListView(startMonth,MaxIndex,true);ArrayList<String> realStartDay=processArrayListView(startDay,MaxIndex,true);
            ArrayList<String> realStartHour=processArrayListView(startHour,MaxIndex,false);ArrayList<String> realStartMinute=processArrayListView(startMinute,MaxIndex,false);
            ArrayList<String> realEndMonth=processArrayListView(endMonth,MaxIndex,true);ArrayList<String> realEndDay=processArrayListView(endDay,MaxIndex,true);
            ArrayList<String> realEndHour=processArrayListView(endHour,MaxIndex,false);ArrayList<String> realEndMinute=processArrayListView(endMinute,MaxIndex,false);
            for (int i=0;i<MaxIndex;i++){
                try {
                    Regulations.accumulate("StartDate",realStartMonth.get(i)+"-"+realStartDay.get(i));
                    Regulations.accumulate("StartTime",realStartHour.get(i)+":"+realStartMinute.get(i)+":00");
                    Regulations.accumulate("EndDate",realEndMonth.get(i)+"-"+realEndDay.get(i));
                    Regulations.accumulate("EndTime",realEndHour.get(i)+":"+realEndMinute.get(i)+":00");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Regulations",Regulations.toString());
            Snackbar prompt = Snackbar.make(view,Regulations.toString(),Snackbar.LENGTH_LONG);
            prompt.getView().setBackgroundColor(MainActivity.colorPrimary.data);
            prompt.show();
            if (!name.isEmpty()){
                parameters.put("name",name);}
            if (!URL.isEmpty()){
                parameters.put("URL",URL);}
            if (!id.isEmpty()){
                parameters.put("id",id);}else{
                processFailure.onFailure();
                return;
            }
            if (!passive.isEmpty()){
                parameters.put("passive",passive);}
            if (!Per.isEmpty()){
                parameters.put("Per",Per);}
            if (!Alertable.isEmpty()){
                parameters.put("Alertable",Alertable);}
            OKHttpTool.asyncPostFormforString(RegistrationURL, parameters,processString,processFailure,1);
            //v.setEnabled(false);
        }

        private String getContentDescription (ViewGroup layout, int id){
            if (layout==null){
                return null;
            }
            View temp =layout.findViewById(id);
            if (temp!=null){
                return temp.getContentDescription()!=null?temp.getContentDescription().toString():null;
            }else {
                return null;
            }
        }

        private void findViewsById (ArrayList<View> outViews,ViewGroup layout,int id){
            if (layout==null||outViews==null){
                return;
            }
            if (layout.getId()==id) {
                outViews.add(layout);
            }
            for (int i=0;i<layout.getChildCount();i++){
                try{
                    findViewsById(outViews,(ViewGroup) layout.getChildAt(i),id);
                }catch (Exception e){
                    if (layout.getChildAt(i).getId()==id) {
                        outViews.add(layout.getChildAt(i));
                    }
                }

            }
        }

        private void findViewsByContentDescription (ArrayList<View> outViews,ViewGroup layout,String text){
            if (text==null||layout==null||outViews==null){
                return;
            }
            if (layout.getContentDescription()!=null&&layout.getContentDescription().toString().equals(text)) {
                outViews.add(layout);
            }
            for (int i=0;i<layout.getChildCount();i++){
                try{
                    findViewsByContentDescription(outViews,(ViewGroup) layout.getChildAt(i),text);
                }catch (Exception e){
                    if (layout.getChildAt(i).getContentDescription()!=null&&layout.getChildAt(i).getContentDescription().toString().equals(text)) {
                        outViews.add(layout.getChildAt(i));
                    }
                }

            }
        }

        private void findChildViewsByContentDescription (ArrayList<View> outViews,ViewGroup layout,String text){
            if (text==null||layout==null||outViews==null){
                return;
            }
            for (int i=0;i<layout.getChildCount();i++){
                try{
                    findChildViewsByContentDescription(outViews,(ViewGroup) layout.getChildAt(i),text);
                }catch (Exception e){
                    //return;
                }
                if (layout.getChildAt(i).getContentDescription()!=null&&layout.getChildAt(i).getContentDescription().toString().equals(text)) {
                    outViews.add(layout.getChildAt(i));
                }
            }
        }

        private ArrayList<String> processArrayListView(ArrayList<View> views, int MaxIndex,boolean WhetherDate){
            ArrayList<String> result=new ArrayList<>(MaxIndex);
            for (int i=0;i<MaxIndex;i++){
                if (i<views.size()){
                    result.add(getStringofValue(((NumberPicker)views.get(i)).getValue(),WhetherDate));
                }else {
                    result.add("");
                }
            }
            return result;
        }

        private String getStringofValue (int value,boolean WhetherDate){
            if (WhetherDate){
                if (value<=0){
                    return "#";
                }else {
                    return String.valueOf(value);
                }
            }else {
                if (value<=0){
                    return "#";
                }else {
                    return String.valueOf(value-1);
                }
            }
        }
    }

    public static class RegistrationExpandableListViewAdapter extends Data.MyViewsBasedExpandableListAdapter{
        int BackgroundColor1;int BackgroundColor2;int TextColor1;int TextColor2;
        Data.regulation[] Regulations;
        Handler handler=new Handler();
        AddRuleOnclickListener addruleonclicklistener = new AddRuleOnclickListener();
        NumberPicker.Formatter formatter1 = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                if (value<=0){
                    return "#";
                }else if ((value-1)/10>=1){
                    return String.valueOf(value-1);
                }else {
                    return "0"+String.valueOf(value-1);
                }
            }
        };
        NumberPicker.Formatter formatter2 = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                if (value<=0){
                    return "#";
                }else {
                    return String.valueOf(value);
                }
            }
        };

        volatile int contentheight=0;

        public RegistrationExpandableListViewAdapter(Context context, List<Pair<Pair<Integer[], Integer>, List<Pair<String, Integer>>>> rawViewSet,int [] colorset, ExpandableListView View) {
            super(context, rawViewSet, View);
            this.contentheight=0;
            setColorSet(colorset);
        }


        public Data.MyViewsBasedExpandableListAdapter configure(Context context, List<Pair<Pair<Integer[], Integer>, List<Pair<String, Integer>>>> rawViewSet, int [] colorset, ExpandableListView View) {
            super.configure(context, rawViewSet, View);
            this.contentheight=0;
            setColorSet(colorset);
            return this;

        }

        public RegistrationExpandableListViewAdapter(Context context, List<Pair<Pair<Integer[], Integer>, List<Pair<String, Integer>>>> rawViewSet, int [] colorset, ExpandableListView View, Data.regulation[] Regulations) {
            super(context, rawViewSet, View);
            this.Regulations=Regulations;
            this.contentheight=0;
            setColorSet(colorset);
        }

        public RegistrationExpandableListViewAdapter configure (Context context, List<Pair<Pair<Integer[], Integer>, List<Pair<String, Integer>>>> rawViewSet, int [] colorset, ExpandableListView View,Data.regulation[] Regulations){
            super.configure(context, rawViewSet, View);
            this.Regulations=Regulations;
            this.contentheight=0;
            setColorSet(colorset);
            return this;
        }

        private void setColorSet (int[] colorset){
            if (colorset!=null){
                for (int i=0;i<colorset.length;i++){
                    if (i==0){
                        BackgroundColor1=colorset[i];
                    }else if (i==1){
                        TextColor1=colorset[i];
                    }else if (i==2){
                        BackgroundColor2=colorset[i];
                    }else if (i==3){
                        TextColor2=colorset[i];
                    }
                }
            }
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View v=super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
            ArrayList<View> views= new ArrayList<>();
            //Log.d("getChildView","getChildView");
            //try {
            //    v.findViewsWithText(views,"Add Rule",View.FIND_VIEWS_WITH_TEXT);
            //    Button New_Rule = (Button) views.get(0);
            //}catch (Exception e){
            //    e.printStackTrace();
            //}
            Button Add_Rule = (Button) v.findViewById(R.id.add_rule);
            Add_Rule.setOnClickListener(addruleonclicklistener.configure(v));
            return v;
        }

        @Override
        public View newChildView(int groupPosition, int childPosition, ViewGroup parent) {
            //Log.d("newChildView","newChildView");
            View v = super.newChildView(groupPosition, childPosition, parent);
            if (Regulations==null) {
                initiateRuleSetter(v);
            }else {
                setRuleSetters((LinearLayout) v);
            }
            return v;
        }

        private void setRuleSetters(LinearLayout layout){
            //String [][] StartDateSet = new String[Regulations.length][];String [][] EndDateSet = new String[Regulations.length][];
            //String [][] StartTimeSet = new String[Regulations.length][];String [][] EndTimeSet = new String[Regulations.length][];
            //boolean[][] EnabledSet = new boolean[Regulations.length][];
            boolean firsted=false;
            for (Data.regulation Regulation : Regulations) {
                Regulation = Regulation.process();
                boolean EnabledOfRuleSet = Regulation.EnabledOfRuleSet;
                if (!EnabledOfRuleSet) {continue;}
                String Item = Regulation.Item;
                String[] StartDateSet = Regulation.StartDate;
                String[] EndDateSet = Regulation.EndDate;
                String[] StartTimeSet = Regulation.StartTime;
                String[] EndTimeSet = Regulation.EndTime;
                boolean[] EnabledSet = Regulation.Enabled;
                int MaxIndex = 0;int[] LengthSet = new int[]{StartDateSet.length, EndDateSet.length, StartTimeSet.length, EndTimeSet.length, EnabledSet.length};
                for (int temp : LengthSet) {
                    MaxIndex = Math.max(MaxIndex, temp);
                }
                for (int i = 0; i < MaxIndex; i++) {
                    int[] StartTimeIntSet = Data.formatData(i<StartTimeSet.length?StartTimeSet[i]:"");
                    int[] StartDateIntSet = Data.formatData(i<StartDateSet.length?StartDateSet[i]:"");
                    int[] EndTimeIntSet = Data.formatData(i<EndTimeSet.length?EndTimeSet[i]:"");
                    int[] EndDateIntSet = Data.formatData(i<EndDateSet.length?EndDateSet[i]:"");
                    boolean Enabled = i < EnabledSet.length && EnabledSet[i];
                    int startHour = StartTimeIntSet[3] + 1;int startMinute = StartTimeIntSet[4] + 1;
                    int startMonth = StartDateIntSet[1];int startDay = StartDateIntSet[2];
                    int endHour = EndTimeIntSet[3] + 1;int endMinute = EndTimeIntSet[4] + 1;
                    int endMonth = EndDateIntSet[1];int endDay = EndDateIntSet[2];
                    Log.d("Result", endHour + ":" + endMinute + "|" + startMonth + "-" + startDay);
                    View view;
                    if (!firsted) {
                        view = layout;
                    } else {
                        view = getInflater().inflate(R.layout.regulationsetting_item, layout, false);
                    }
                    NumberPicker startMonthPicker = (NumberPicker) view.findViewById(R.id.startMonth);
                    startMonthPicker.setFormatter(formatter2);
                    NumberPicker startDayPicker = (NumberPicker) view.findViewById(R.id.startDay);
                    startDayPicker.setFormatter(formatter2);
                    NumberPicker startHourPicker = (NumberPicker) view.findViewById(R.id.startHour);
                    startHourPicker.setFormatter(formatter1);
                    NumberPicker startMinutePicker = (NumberPicker) view.findViewById(R.id.startMinute);
                    startMinutePicker.setFormatter(formatter1);
                    NumberPicker endMonthPicker = (NumberPicker) view.findViewById(R.id.endMonth);
                    endMonthPicker.setFormatter(formatter2);
                    NumberPicker endDayPicker = (NumberPicker) view.findViewById(R.id.endDay);
                    endDayPicker.setFormatter(formatter2);
                    NumberPicker endHourPicker = (NumberPicker) view.findViewById(R.id.endHour);
                    endHourPicker.setFormatter(formatter1);
                    NumberPicker endMinutePicker = (NumberPicker) view.findViewById(R.id.endMinute);
                    endMinutePicker.setFormatter(formatter1);
                    startMonthPicker.setMinValue(0);
                    startMonthPicker.setMaxValue(12);
                    startMonthPicker.setValue(startMonth);
                    endMonthPicker.setMinValue(0);
                    endMonthPicker.setMaxValue(12);
                    endMonthPicker.setValue(endMonth);
                    startDayPicker.setMinValue(0);
                    startDayPicker.setMaxValue(31);
                    startDayPicker.setValue(startDay);
                    endDayPicker.setMinValue(0);
                    endDayPicker.setMaxValue(31);
                    endDayPicker.setValue(endDay);
                    startHourPicker.setMinValue(0);
                    startHourPicker.setMaxValue(24);
                    startHourPicker.setValue(startHour);
                    endHourPicker.setMinValue(0);
                    endHourPicker.setMaxValue(24);
                    endHourPicker.setValue(endHour);
                    startMinutePicker.setMinValue(0);
                    startMinutePicker.setMaxValue(60);
                    startMinutePicker.setValue(startMinute);
                    endMinutePicker.setMinValue(0);
                    endMinutePicker.setMaxValue(60);
                    endMinutePicker.setValue(endMinute);
                    Button ItemButton = (Button) view.findViewById(R.id.ItemButton);
                    ItemButtonOnclickListener itembuttononclicklistener = new ItemButtonOnclickListener();
                    ItemButton.setOnClickListener(itembuttononclicklistener);
                    setItemButtonAppearance(ItemButton, itembuttononclicklistener, convertStringTypetoIntType(Item));
                    CheckBox EnabledCheckBox = (CheckBox) view.findViewById(R.id.EnabledCheckBox);
                    EnabledCheckBox.setChecked(Enabled);
                    if (firsted) {
                        layout.addView(view, layout.getChildCount() - 1);
                    }
                    firsted = true;
                }
            }
            if (!firsted){
                initiateRuleSetter(layout);
            }
        }

        //private void setRuleSetters_old(LinearLayout layout){
        //    boolean firsted=false;
        //    if (Regulations!=null){
        //        for (JSONObject tempjson:Regulations){
        //            String Item=tempjson.optString("Item");
        //            JSONArray StartDate = tempjson.optJSONArray("StartDate");if (StartDate==null){StartDate=new JSONArray().put(tempjson.optString("StartDate"));}
        //            JSONArray StartTime = tempjson.optJSONArray("StartTime");if (StartTime==null){StartTime=new JSONArray().put(tempjson.optString("StartTime"));}
        //            JSONArray EndDate = tempjson.optJSONArray("EndDate");if (EndDate==null){EndDate=new JSONArray().put(tempjson.optString("EndDate"));}
        //            JSONArray EndTime = tempjson.optJSONArray("EndTime");if (EndTime==null){EndTime=new JSONArray().put(tempjson.optString("EndTime"));}
        //            int MaxIndex=0; JSONArray[] temps = new JSONArray[]{StartDate,StartTime,EndDate,EndTime};
        //            for (JSONArray temp:temps){
        //                MaxIndex=Math.max(MaxIndex,temp.length());
        //            }
        //            for (int i=0;i<MaxIndex;i++){
        //                int[] StartTimeIntSet = Data.formatData(StartTime.optString(i,null));
        //                int[] StartDateIntSet = Data.formatData(StartDate.optString(i,null));
        //                int[] EndTimeIntSet = Data.formatData(EndTime.optString(i,null));
        //                int[] EndDateIntSet = Data.formatData(EndDate.optString(i,null));
        //                int startHour=StartTimeIntSet[3]+1;int startMinute=StartTimeIntSet[4]+1;int startMonth=StartDateIntSet[1];int startDay=StartDateIntSet[2];
        //                int endHour=EndTimeIntSet[3]+1;int endMinute=EndTimeIntSet[4]+1;int endMonth=EndDateIntSet[1];int endDay=EndDateIntSet[2];
        //                Log.d("Result",endHour+":"+endMinute+"|"+startMonth+"-"+startDay);
        //                View view;
        //                if (!firsted) {
        //                    view=layout;
        //                }else {
        //                    view = getInflater().inflate(R.layout.regulationsetting_item, layout, false);
        //                }
        //                NumberPicker startMonthPicker= (NumberPicker) view.findViewById(R.id.startMonth);startMonthPicker.setFormatter(formatter2);
        //                NumberPicker startDayPicker= (NumberPicker) view.findViewById(R.id.startDay);startDayPicker.setFormatter(formatter2);
        //                NumberPicker startHourPicker= (NumberPicker) view.findViewById(R.id.startHour);startHourPicker.setFormatter(formatter1);
        //                NumberPicker startMinutePicker= (NumberPicker) view.findViewById(R.id.startMinute);startMinutePicker.setFormatter(formatter1);
        //                NumberPicker endMonthPicker= (NumberPicker) view.findViewById(R.id.endMonth);endMonthPicker.setFormatter(formatter2);
        //                NumberPicker endDayPicker= (NumberPicker) view.findViewById(R.id.endDay);endDayPicker.setFormatter(formatter2);
        //                NumberPicker endHourPicker= (NumberPicker) view.findViewById(R.id.endHour);endHourPicker.setFormatter(formatter1);
        //                NumberPicker endMinutePicker= (NumberPicker) view.findViewById(R.id.endMinute);endMinutePicker.setFormatter(formatter1);
        //                startMonthPicker.setMinValue(0);startMonthPicker.setMaxValue(12);startMonthPicker.setValue(startMonth);
        //                endMonthPicker.setMinValue(0);endMonthPicker.setMaxValue(12);endMonthPicker.setValue(endMonth);
        //                startDayPicker.setMinValue(0);startDayPicker.setMaxValue(31);startDayPicker.setValue(startDay);
        //                endDayPicker.setMinValue(0);endDayPicker.setMaxValue(31);endDayPicker.setValue(endDay);
        //                startHourPicker.setMinValue(0);startHourPicker.setMaxValue(24);startHourPicker.setValue(startHour);
        //                endHourPicker.setMinValue(0);endHourPicker.setMaxValue(24);endHourPicker.setValue(endHour);
        //                startMinutePicker.setMinValue(0);startMinutePicker.setMaxValue(60);startMinutePicker.setValue(startMinute);
        //                endMinutePicker.setMinValue(0);endMinutePicker.setMaxValue(60);endMinutePicker.setValue(endMinute);
        //                Button ItemButton = (Button) view.findViewById(R.id.ItemButton);
        //                ItemButtonOnclickListener itembuttononclicklistener = new ItemButtonOnclickListener();
        //                ItemButton.setOnClickListener(itembuttononclicklistener);
        //                setItemButtonAppearance(ItemButton,itembuttononclicklistener,convertStringTypetoIntType(Item));
        //                if (firsted){
        //                    layout.addView(view,layout.getChildCount()-1);
        //                }
        //                firsted=true;
        //            }
        //
        //        }
        //    }
        //}

        private int convertStringTypetoIntType (String Type){
            switch (Type){
                case "SleepTime":
                    return SleepTime;
                case "ActiveTime":
                    return ActiveTime;
                default:return SleepTime;
            }
        }

        private void initiateRuleSetter (View layout){
            Calendar now = Calendar.getInstance();
            int Hour = now.get(Calendar.HOUR_OF_DAY)+1;int Minute = now.get(Calendar.MINUTE)+1;
            int Month = now.get(Calendar.MONTH)+1;int Day = now.get(Calendar.DAY_OF_MONTH);
            NumberPicker startMonth= (NumberPicker) layout.findViewById(R.id.startMonth);startMonth.setFormatter(formatter2);
            NumberPicker startDay= (NumberPicker) layout.findViewById(R.id.startDay);startDay.setFormatter(formatter2);
            NumberPicker startHour= (NumberPicker) layout.findViewById(R.id.startHour);startHour.setFormatter(formatter1);
            NumberPicker startMinute= (NumberPicker) layout.findViewById(R.id.startMinute);startMinute.setFormatter(formatter1);
            NumberPicker endMonth= (NumberPicker) layout.findViewById(R.id.endMonth);endMonth.setFormatter(formatter2);
            NumberPicker endDay= (NumberPicker) layout.findViewById(R.id.endDay);endDay.setFormatter(formatter2);
            NumberPicker endHour= (NumberPicker) layout.findViewById(R.id.endHour);endHour.setFormatter(formatter1);
            NumberPicker endMinute= (NumberPicker) layout.findViewById(R.id.endMinute);endMinute.setFormatter(formatter1);
            startMonth.setMinValue(0);startMonth.setMaxValue(12);startMonth.setValue(Month);
            endMonth.setMinValue(0);endMonth.setMaxValue(12);endMonth.setValue(Month);
            startDay.setMinValue(0);startDay.setMaxValue(31);startDay.setValue(Day);
            endDay.setMinValue(0);endDay.setMaxValue(31);endDay.setValue(Day);
            startHour.setMinValue(0);startHour.setMaxValue(24);startHour.setValue(Hour);
            endHour.setMinValue(0);endHour.setMaxValue(24);endHour.setValue(Hour);
            startMinute.setMinValue(0);startMinute.setMaxValue(60);startMinute.setValue(Minute);
            endMinute.setMinValue(0);endMinute.setMaxValue(60);endMinute.setValue(Minute);
            Button ItemButton = (Button) layout.findViewById(R.id.ItemButton);
            ItemButtonOnclickListener itembuttononclicklistener = new ItemButtonOnclickListener();
            ItemButton.setOnClickListener(itembuttononclicklistener);
            setItemButtonAppearance(ItemButton,itembuttononclicklistener,SleepTime);
        }

        @Override
        public void setListViewHeight(ExpandableListView listView) {
            if (contentheight>0){
                getExpandablelistview().getLayoutParams().height=contentheight;
                getExpandablelistview().requestLayout();
            }else {
                super.setListViewHeight(listView);
            }
        }

        @Override
        public void setListViewHeight(ExpandableListView listView, Handler handler) {
            if (contentheight>0){
                getExpandablelistview().getLayoutParams().height=contentheight;
                getExpandablelistview().requestLayout();
            }else {
                super.setListViewHeight(listView, handler);
            }
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            super.onGroupCollapsed(groupPosition);
            super.setListViewHeight(getExpandablelistview());
        }

        static final int SleepTime=0;static final int ActiveTime=1;
        private void setItemButtonAppearance(Button button, ItemButtonOnclickListener onClickListener,int Type){
            //Log.d("Colorset","Sleep:"+BackgroundColor1+"*"+TextColor1+"|"+"Active:"+BackgroundColor2+"*"+TextColor2);
            if (button!=null){
                switch (Type){
                    case SleepTime:
                        button.setBackgroundTintList(ColorStateList.valueOf(BackgroundColor1));
                        button.setTextColor(TextColor1);
                        button.setText(R.string.sleeptime);
                        break;
                    case ActiveTime:
                        button.setBackgroundTintList(ColorStateList.valueOf(BackgroundColor2));
                        //button.setBackgroundColor(BackgroundColor2);
                        button.setTextColor(TextColor2);
                        button.setText(R.string.activetime);
                        break;
                    default:Log.d("No Such ItemButton Type","ItemButton");return;
                }
                button.setHint(String.valueOf(Type));
                if (onClickListener!=null){
                    onClickListener.setType(Type);
                }
            }
        }

        private class ItemButtonOnclickListener implements View.OnClickListener{
            private int Type =SleepTime;
            @Override
            public void onClick(View v) {
                //Type=Math.abs(Type-1);
                if (Type==SleepTime){
                    Type=ActiveTime;
                }else {
                    Type=SleepTime;
                }
                setItemButtonAppearance((Button) v, null, Type);
            }

            public void setType(int type) {
                this.Type = type;
            }

            public int getType() {
                return Type;
            }
        }

        private class AddRuleOnclickListener implements View.OnClickListener{
            View view;
            @Override
            public void onClick(final View v) {
                if (view!=null) {
                    LinearLayout layout = (LinearLayout) view;
                    final View temp = getInflater().inflate(R.layout.regulationsetting_item, layout, false);
                    initiateRuleSetter(temp);
                    layout.addView(temp,layout.getChildCount()-1);
                    //view.setBackgroundColor(0xff000000);
                    v.setEnabled(false);
                    handler.post(new Runnable() {
                        int trytimes=0;
                        @Override
                        public void run() {
                            if (temp.getHeight()!=0){
                                contentheight=getExpandablelistview().getLayoutParams().height+=temp.getHeight();
                                getExpandablelistview().requestLayout();
                                v.setEnabled(true);
                            }else if (trytimes<100){
                                handler.postDelayed(this,10);
                                trytimes++;
                            }else {
                                ((Button)v).setText(R.string.cant_add_rule);
                            }
                        }
                    });

                }
            }

            AddRuleOnclickListener configure (View view){
                this.view=view;
                return this;
            }
        }
    }



    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimator(transit, enter, nextAnim);
    }

    void clear(){
        ViewGroup layout = (ViewGroup) ((ViewGroup) getView()).getChildAt(0);
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

    void resetEnabled (View v,String Key){
        if (getCustomState()!=null){
            getCustomState().putBoolean(Key+"Enabled",true);
        }
        try {
            v.setEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("onSaveInstanceState","HasRun");
        super.onSaveInstanceState(outState);
    }

    public void customSaveInstanceState (Bundle outState){
        State=outState;
    }

    public Bundle getCustomState() {
        return State;
    }

    @Override
    public void onPause() {
        Log.d("onPause","HasRun");
        Bundle temp;
        if (getCustomState()!=null){
            temp=getCustomState();
        }else {
            temp=new Bundle();
        }
        View v=getView().findViewById(R.id.AddButton);
        temp.putBoolean(v.getContentDescription()+"Enabled",v.isEnabled());
        customSaveInstanceState(temp);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("onDestroy","HasRun");

        super.onDestroy();
    }
}
