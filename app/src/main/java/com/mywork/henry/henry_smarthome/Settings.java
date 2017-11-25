package com.mywork.henry.henry_smarthome;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mywork.henry.henry_smarthome.Settings_Item.ApparatusConfiguration;
import com.mywork.henry.henry_smarthome.Settings_Item.DebugConfiguration;
import com.mywork.henry.henry_smarthome.Settings_Item.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Henry on 2016/09/30.
 */

public class Settings extends AppCompatActivity {
    Handler handler = new Handler();
    Toolbar toolbar;
    Menu ActivityMenu;ActionMenuView ActivityMenuView;
    final ArrayList<View> toolbarViews = new ArrayList<>(8);
    TextView ApplicationTitle;
    TextView AlertPrompt;    int AlertedLogNumber;
    MenuItem DisconnectionPrompt; View DisconnectionPromptView;boolean Disconnected=false;
    String URL4="http://168.150.116.167:8080/Smart_Home/RegistrationPlusStateupdate";
    FragmentManager fm;
    DialogFragment ClearDialog;
    AlertButtonListener alertbuttonlistener;
    volatile boolean toStartOtherActivity=false;

    TextView SettingsText;

    ApparatusConfiguration apparatusConfiguration;
    DebugConfiguration debugConfiguration;
    Login login;

    updatedkeeper UpdateKeeper = null;
    final int sleeptime=5000;
    volatile boolean StopDaemon=false;

    @Override
    protected void onPause() {
        super.onPause();
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
    protected void onStop() {
        super.onStop();
        StopDaemon = true;
        if (UpdateKeeper != null) {
            UpdateKeeper.interrupt();
        }
        Log.d("Settings","Stopped");
    }

    @Override
    protected void onResume() {
        //if (getIntent().getStringExtra("Intention")!=null&&getIntent().getStringExtra("Intention").equals("StartSettings")) {
        //    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        //}else {
        //    overridePendingTransition(0, R.anim.activity_exit);
        //}
        super.onResume();

        //long duration = AnimationUtils.loadAnimation(this,R.anim.rotation_endless).getDuration();
        //
        //ApplicationTitle.setVisibility(View.INVISIBLE);
        //if (CriticalPrompts!=null)
        //    CriticalPrompts.setVisibility(View.INVISIBLE);
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

        toStartOtherActivity=false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        toolbar = (Toolbar) findViewById(R.id.settingstoolbar);
        ApplicationTitle = (TextView)findViewById(R.id.Title_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStartOtherActivity=true;
                finishAfterTransition();
                overridePendingTransition(R.anim.activity_enter_r,R.anim.activity_exit_r);
            }
        });
        Disconnected=getIntent().getBooleanExtra("Disconnected",false);
        AlertedLogNumber =getIntent().getIntExtra("AlertedLogNumber",0);
        toStartOtherActivity=getIntent().getBooleanExtra("toStartOtherActivity",false);
        fm=getFragmentManager();
        ClearDialog=new CurrentAlertedLogClean();
        alertbuttonlistener =new AlertButtonListener();

        apparatusConfiguration=new ApparatusConfiguration();
        debugConfiguration=new DebugConfiguration();
        login=new Login();
        Bundle apparatusConfigurationSet=new Bundle();apparatusConfigurationSet.putString(null,getString(R.string.ApparatusConfiguration));
        apparatusConfigurationSet.putInt("Drawable",R.drawable.light_on);
        apparatusConfiguration.setArguments(apparatusConfigurationSet);
        Bundle debugConfigurationSet=new Bundle();debugConfigurationSet.putString(null,getString(R.string.DebugConfiguration));
        debugConfigurationSet.putInt("Drawable",R.drawable.bug);
        debugConfiguration.setArguments(debugConfigurationSet);
        Bundle loginSet=new Bundle();loginSet.putString(null,getString(R.string.Login));
        loginSet.putInt("Drawable",R.drawable.person);
        login.setArguments(loginSet);


    }



    void initiateExpandableListView (){
        ExpandableListView Settings_List = (ExpandableListView) findViewById(R.id.Setting_List);

        List <Pair<Pair<Integer[],Integer>,List<Pair<String,Integer>>>> rawViewSet = new ArrayList<>();
        List<Pair<String,Integer>> Child1 = new ArrayList<>();Child1.add(new Pair<>("settings_child1",R.layout.settings_child1));
        List<Pair<String,Integer>> Child2 = new ArrayList<>();Child2.add(new Pair<>("settings_child2",R.layout.settings_child2));
        List<Pair<String,Integer>> Child3 = new ArrayList<>();Child3.add(new Pair<>("settings_child3",R.layout.settings_child3));
        Integer[] Indicator = new Integer[]{R.id.Setting_GroupIndicator,getResources().getIdentifier("arrow_up_float","drawable","android"),getResources().getIdentifier("arrow_down_float","drawable","android")};
        rawViewSet.add(new Pair<>(new Pair<>(Indicator,R.layout.settings_group),Child1));
        rawViewSet.add(new Pair<>(new Pair<>(Indicator,R.layout.settings_group),Child2));
        rawViewSet.add(new Pair<>(new Pair<>(Indicator,R.layout.settings_group),Child3));
        List<Pair<Integer,Fragment>> rawContainerSet = new ArrayList<>();
        rawContainerSet.add(new Pair<Integer,Fragment>(R.id.Setting_Child1, login));
        rawContainerSet.add(new Pair<Integer, Fragment>(R.id.Setting_Child2,apparatusConfiguration));
        rawContainerSet.add(new Pair<>(R.id.Setting_Child3,(Fragment) debugConfiguration));
        Settings_List.setAdapter(new SettingsExpandableListViewAdapter(this,rawViewSet,rawContainerSet,Settings_List));
        Settings_List.setGroupIndicator(null);
        //Settings_List.setDividerHeight(100);
        //Settings_List.setDivider(getDrawable(R.drawable.divider_shape));
        //((Data.MyViewsBasedExpandableListAdapter)Settings_List.getExpandableListAdapter()).setListViewHeight(Settings_List,handler);
    }

    void initiateToolbar (Toolbar toolbar){
        TextView Title = null;
        for (int i=0;i<toolbar.getChildCount();i++){
            //Log.d("ToolbarClass",toolbar.getChildAt(i).getClass().toString());
            final View view = toolbar.getChildAt(i);
            if (view instanceof TextView){
                //Log.d("ToolbarText",((TextView) toolbar.getChildAt(i)).getText().toString());
                //Log.d("ToolbarTextScale",String.valueOf(toolbar.getChildAt(i).getWidth())+"w:h"+String.valueOf(toolbar.getChildAt(i).getHeight()));
                SettingsText=Title=(TextView) view;
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
                            Log.d("AddToolbarViews","TRUE");
                            if (view.getHeight()!=0){
                                view.setY(temp.getY()+(temp.getHeight()-view.getHeight())/2);
                                //Log.d("ToolbarActionMenuView",String.valueOf(view.getWidth())+"w:h"+String.valueOf(view.getHeight()));

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

        //handler.postDelayed(new Runnable() {
        //    @Override
        //    public void run() {
        //        Log.d("HandleToolbarViews","TRUE");
        //        synchronized (toolbarViews){
        //            if (!toolbarViews.isEmpty()){
        //                for (View temp:toolbarViews){
        //                    //Log.d("ToolBarViews",temp.toString());
        //                    ActionMenuItemView real = (ActionMenuItemView)temp;
        //                    if (real.getItemData().toString().equals(getString(R.string.hasdisconnected))){
        //                        //TextView
        //                        DisconnectionPromptView =real;
        //                        DisconnectionPromptView.setVisibility(View.INVISIBLE);
        //
        //                    }
        //                }
        //                //initiateCriticalSigns(toolbar);
        //            }else {
        //                handler.postDelayed(this,5);
        //            }
        //        }
        //    }
        //},10);
    }

    void initiateCriticalSigns (final Toolbar toolbar){
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,toolbar.getHeight());
        //layoutparams.addRule(RelativeLayout.CENTER_IN_PARENT);//这个不好用。
        //View view = getLayoutInflater().inflate(R.layout.toolbar_notification,toolbar);//这个可以直接把layout XML注入后面的GroupView.
        //Log.d("InflatedView",view.toString()+":"+view.getClass().toString());
        final View view = getLayoutInflater().inflate(R.layout.toolbar_notification_2,null);
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
        final View Title = findViewById(R.id.Title_settings);
        handler.postDelayed(new Runnable() {
            int trytimes=0;
            @Override
            public void run() {
                if (Title.getWidth()!=0&&view.getWidth()!=0) {
                    //Alert.setTextSize(Alert.getHeight()/8);
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
        synchronized (toolbarViews) {
            toolbarViews.clear();
            for (int ii = 0; ii < ActivityMenuView.getChildCount(); ii++) {
                View temp = ActivityMenuView.getChildAt(ii);
                if (temp != null) {
                    toolbarViews.add(temp);
                }

            }

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

    void setAlertedLogNumber(final int alertedlognumber){
        this.AlertedLogNumber =alertedlognumber;
        if (AlertPrompt!=null) {
            if (AlertPrompt.getVisibility()!=(alertedlognumber>0 ? View.VISIBLE : View.INVISIBLE)||!AlertPrompt.getText().toString().equals(String.valueOf(alertedlognumber)))
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("AlertedLogNumber",String.valueOf(alertednumber));
                        AlertPrompt.setText(String.valueOf(alertedlognumber));
                        AlertPrompt.setVisibility(alertedlognumber>0 ? View.VISIBLE : View.INVISIBLE);
                    }
                });
        }
    }

    private class updatedkeeper extends Thread {
        int sleeptime;int defaultsleeptime;
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
            }
        };
        Data.OnsuccessProcess onsuccessProcess= new Data.OnsuccessProcess(0,whattodonext);
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
                OKHttpTool.asyncCustomPostFormforJsonObject("http://168.150.116.167:8080/Smart_Home/Inquire", parameters, onsuccessProcess, new OKHttpTool.processFailure() {
                    @Override
                    public void onFailure() {
                        setDisconnected(true);Log.d("UPDateSession", "Failed");
                    }
                });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        ActivityMenu=menu;
        DisconnectionPrompt=menu.findItem(R.id.Menu_Disconnection);
        initiateToolbar(toolbar);
        initiateCriticalSigns(toolbar);
        initiateExpandableListView();
        return true;
    }

    @Override
    public void onBackPressed() {
        //getWindow().setEnterTransition(new Slide().setDuration(2000));
        //getWindow().setExitTransition(new Slide().setDuration(2000));
        toStartOtherActivity=true;
        super.onBackPressed();
        //finishAfterTransition();
        overridePendingTransition(R.anim.activity_enter_r,R.anim.activity_exit_r);


        //finishAfterTransition();
        //ActivityCompat.finishAfterTransition(this);
        //Settings.this.finish();
        //Intent intent = new Intent(this,MainActivity.class);
        //intent.putExtra("Intention","StartMainActivity");
        //startActivity(intent);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("HOME",String.valueOf(item.getActionView()+"afaf"));
        Log.d("HOME",String.valueOf(R.id.home));
        if (item.getItemId()==R.id.home){
            //ActivityCompat.finishAfterTransition(this);
            Log.d("HOME","RUNNN");

        }

        return super.onOptionsItemSelected(item);
    }



    private class SettingsExpandableListViewAdapter extends Data.MyViewsBasedExpandableListAdapter{
        private final List<Pair<Integer,Fragment>> rawContainerSet;
        SettingsExpandableListViewAdapter(Context context, List <Pair<Pair<Integer[],Integer>,List<Pair<String,Integer>>>> rawViewSet, List<Pair<Integer,Fragment>> rawContainerSet,ExpandableListView View) {
            super(context, rawViewSet, View);
            this.rawContainerSet=rawContainerSet;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);
            v=setRightTitleWithImage(v,groupPosition,isExpanded);
            v=setRightGroupViewXPosition(v);
            return v;
        }

        private View setRightGroupViewXPosition (View v){
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.Setting_Group);
            layout.setX((int)(SettingsText.getX()*0.55555));
            //Log.d("SettingsText",SettingsText.getX()+"");
            return v;
        }

        private View setRightTitleWithImage(View v,int groupPosition,boolean isExpanded) {
            TextView textview = (TextView) v.findViewById(R.id.Setting_GroupText);
            ImageView imageview = (ImageView) v.findViewById(R.id.Setting_GroupImage);
            if (groupPosition<rawContainerSet.size()&&rawContainerSet.get(groupPosition).second!=null) {
                textview.setText(rawContainerSet.get(groupPosition).second.getArguments().getString(null));
                if (rawContainerSet.get(groupPosition).second.getArguments().getInt("Drawable",-1)!=-1) {
                    imageview.setImageDrawable(getDrawable(rawContainerSet.get(groupPosition).second.getArguments().getInt("Drawable")));
                }
            }
            if (isExpanded){
                v.setBackground(getDrawable(R.drawable.frame_shape_dark));
            }else {
                v.setBackground(getDrawable(R.drawable.frame_shape));
            }

            return v;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View v;
            //Log.d("Positions","Group"+groupPosition+"Child"+childPosition);
            //if (convertView!=null) {
            //    Log.d("EntryName",convertView.getResources().getResourceEntryName(convertView.getId()).toUpperCase());
            //    Log.d("EntryName",getRawViewSet().get(groupPosition).second.get(childPosition).first.toUpperCase());
            //    Log.d("EntryID",convertView.getId()+"");
            //    Log.d("EntryID",rawContainerSet.get(groupPosition).first+"");
            //}
            if (convertView == null||convertView.getId()!=rawContainerSet.get(groupPosition).first) {
                v = newChildView(groupPosition,childPosition, parent);
                //Log.d("UseNewChildView","true@"+groupPosition+childPosition+"::"+v.toString());
            } else {
                //Log.d("UseOldChildView","true@"+groupPosition+childPosition+"::"+convertView.toString());
                v = convertView;
            }
            //if (v.getResources().getResourceEntryName(v.getId()).equals(""))
            Pair<View, List<View>> entry;
            List<View> temp;
            synchronized (realViewSet) {
                if (groupPosition >= realViewSet.size()) {
                    temp=new ArrayList<>();temp.add(v);
                    realViewSet.add(groupPosition, new Pair<View, List<View>>(null, temp));
                } else {
                    entry=realViewSet.get(groupPosition);
                    temp=entry.second;
                    if (temp==null){
                        temp=new ArrayList<>();
                        temp.add(v);
                    }else if (childPosition>=temp.size()) {
                        temp.add(childPosition,v);
                    }else {
                        temp.set(childPosition,v);
                    }
                    entry=new Pair<>(entry.first,temp);
                    realViewSet.set(groupPosition, entry);
                }
                //Log.d("Regulation","Has Set Child");
            }
            return v;
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            if (groupPosition<rawContainerSet.size()) {
                //getFragmentManager().popBackStack();
                Fragment temp = getFragmentManager().findFragmentByTag(rawContainerSet.get(groupPosition).second.getTag());
                if (temp!=null) {
                    getFragmentManager().beginTransaction().remove(rawContainerSet.get(groupPosition).second).commit();
                }
            }
            //Log.d("RealGroupPosition",groupPosition+"");
            getGroup(groupPosition).setActivated(false);
            //super.onGroupCollapsed(groupPosition);
        }

        @Override
        public void onGroupExpanded(final int groupPosition) {
            handler.post(new Runnable() {
                int trytimes=0;
                @Override
                public void run() {
                    //Log.d("This has RUnnn","True");
                    View temp;
                    if (groupPosition<rawContainerSet.size()) {
                        temp = findViewById(rawContainerSet.get(groupPosition).first);
                    }else {
                        Log.d("Illegal rawContainerSet","Illegal Length");
                        return;
                    }
                    //Fragment temp3 = getFragmentManager().findFragmentByTag(rawContainerSet.get(groupPosition).second.getTag());
                    if (temp==null){
                        //Log.d("This has Retryied","True");
                        if (trytimes<50) {
                            handler.postDelayed(this, 50);
                            trytimes++;
                        }
                        return;
                    }
                    if (groupPosition<rawContainerSet.size()) {
                        Fragment temp2 = getFragmentManager().findFragmentByTag(rawContainerSet.get(groupPosition).second.getArguments().getString(null));
                        //Log.d("FragmentExisted",String.valueOf(temp2==null));
                        //if (temp2==null) {
                        getFragmentManager().beginTransaction().replace(rawContainerSet.get(groupPosition).first, rawContainerSet.get(groupPosition).second,
                                rawContainerSet.get(groupPosition).second.getArguments().getString(null)).commit();
                        //Log.d("groupPosition",groupPosition+"");
                        //Log.d("FragmentString",rawContainerSet.get(groupPosition).second.toString());
                        //Log.d("FragmentName",rawContainerSet.get(groupPosition).second.getArguments().getString(null));
                        //}
                    }

                }
            });
            //Log.d("RealGroupPosition",groupPosition+"");
            getGroup(groupPosition).setActivated(true);
            //super.onGroupExpanded(groupPosition);
        }


    }
}
