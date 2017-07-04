package com.mywork.henry.henry_smarthome;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Henry on 2017/1/7.
 */

public class CurrentAlertedLogClean extends DialogFragment {
    Bundle State;volatile String ClearButtonDescription;
    OKHttpTool.processString processString = new OKHttpTool.processString() {
        @Override
        public void onResponse(String value) {
            View view=null;
            Log.d("Body",value);
            view=getView().findViewById(R.id.Clear);
            resetEnabled(view, ClearButtonDescription);
            dismiss();
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
                view=getView().findViewById(R.id.Clear);
            }catch (Exception e){
                e.printStackTrace();
            }
            resetEnabled(view, ClearButtonDescription);
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Bundle ConfigurationsSet = getArguments();final String URL;final String user;final String pass;
        if (ConfigurationsSet==null||ConfigurationsSet.isEmpty()||ConfigurationsSet.getString("URL")==null
                ||ConfigurationsSet.getString("user")==null||ConfigurationsSet.getString("pass")==null){
            Log.d("Warning","Not Set Configurations");
            //dismiss();
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent,null));
            return inflater.inflate(R.layout.registration_warining,container);
        }
        final View view = inflater.inflate(R.layout.currentalertedlogclean,container,false);
        URL=ConfigurationsSet.getString("URL");user=ConfigurationsSet.getString("user");pass=ConfigurationsSet.getString("pass");
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE|Window.FEATURE_SWIPE_TO_DISMISS);
        //getDialog().setTitle("Whether to Clear Alerts");
        getDialog().getWindow().setWindowAnimations(R.style.CustomDialogAnimationfromTop);
        Button Clear=(Button) view.findViewById(R.id.Clear);
        ClearButtonDescription =Clear.getContentDescription().toString();
        if (getCustomState()!=null) {
            Clear.setEnabled(getCustomState().getBoolean(Clear.getContentDescription()+"Enabled", true));
        }
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("user",user);
                parameters.put("pass",pass);
                parameters.put("Delete","true");
                parameters.put("Type","CurrentAlertedLog");
                OKHttpTool.asyncPostFormforString(URL, parameters,processString,processFailure,1);
                v.setEnabled(false);
            }
        });
        Button Cancel = (Button)view.findViewById(R.id.Cancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
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
        View v=getView().findViewById(R.id.Clear);
        temp.putBoolean(v.getContentDescription()+"Enabled",v.isEnabled());
        customSaveInstanceState(temp);
        super.onPause();
    }
}
