package com.mywork.henry.henry_smarthome.Settings_Item;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.mywork.henry.henry_smarthome.Data;
import com.mywork.henry.henry_smarthome.MainActivity;
import com.mywork.henry.henry_smarthome.OKHttpTool;
import com.mywork.henry.henry_smarthome.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Henry on 2016/12/9.
 */

public class Login extends Fragment {
    SetButtonListener setButtonListener;
    ChangeButtonListener changeButtonListener;
    private final int changeUserID = 5001;
    private final int changePasswordID = 5002;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings_content1,container,false);
        setButtonListener=new SetButtonListener();changeButtonListener=new ChangeButtonListener();
        EditText User= (EditText) view.findViewById(R.id.user);User.setText(Data.getLogin(true)!=null?Data.getLogin(true):getString(R.string.default_user));
        EditText Pass= (EditText) view.findViewById(R.id.password);Pass.setText(Data.getLogin(false)!=null?getString(R.string.shown_password):"");
        EditText LocalHost = (EditText) view.findViewById(R.id.LocalHost);LocalHost.setText(Data.getHostAddress(false)!=null?Data.getHostAddress(false):getString(R.string.default_localhostaddress));
        EditText InternetHost = (EditText) view.findViewById(R.id.InternetHost);InternetHost.setText(Data.getHostAddress(true)!=null?Data.getHostAddress(true):getString(R.string.default_internethostaddress));
        Button Set = (Button) view.findViewById(R.id.Set);
        Set.setOnClickListener(setButtonListener.configure(User,Pass,LocalHost,InternetHost));
        Button Change = (Button) view.findViewById(R.id.ChangeLogin);
        Change.setOnClickListener(changeButtonListener.configure(Set));
        return view;
    }

    void resetSetButton(int SetButtonId,int changeUserId,int changePasswordId){
        try {
            Button Set = (Button) getView().findViewById(SetButtonId);
            LinearLayout layout = (LinearLayout) getView();
            EditText changeUser = (EditText) getView().findViewById(changeUserId);
            EditText changePassword = (EditText) getView().findViewById(changePasswordId);
            Set.setText(getString(R.string.set));
            layout.removeView(changeUser);
            layout.removeView(changePassword);
            Set.setEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class SetButtonListener implements View.OnClickListener{
        EditText User;EditText Password; EditText changeUser;EditText changePassword;
        EditText LocalHost;EditText InternetHost;
        OKHttpTool.processString processString = new OKHttpTool.processString() {
            @Override
            public void onResponse(String value) {
                Snackbar prompt = Snackbar.make(getView(), R.string.Successfully_Changed_Login, Snackbar.LENGTH_LONG);
                prompt.getView().setBackgroundColor(MainActivity.colorPrimary.data);
                prompt.show();
                resetSetButton(R.id.Set,changeUserID,changePasswordID);
            }
        };
        OKHttpTool.processFailure processFailure = new OKHttpTool.processFailure() {
            @Override
            public void onFailure() {
                Snackbar prompt = Snackbar.make(getView(), R.string.Failed_to_Connect, Snackbar.LENGTH_LONG);
                prompt.getView().setBackgroundColor(MainActivity.colorPrimary.data);
                prompt.show();
                resetSetButton(R.id.Set,changeUserID,changePasswordID);
            }
        };

        @Override
        public void onClick(View v) {

            if (((Button) v).getText().toString().equals(getString(R.string.change_login))&&changeUser!=null&&changePassword!=null){
                Map <String,String> parameters = new HashMap<>();
                String NewAccount = changeUser.getText().toString()+":"+changePassword.getText().toString();
                parameters.put("user",Data.getLogin(true));
                parameters.put("pass",Data.getLogin(false));
                parameters.put("Refresh","ResetAccount");
                parameters.put("NewAccount",NewAccount);
                v.setEnabled(false);
                OKHttpTool.asyncPostFormforString("http://168.150.116.167:8080/debugconfig", parameters, processString,processFailure,1);
            }else {
                Map<String,String> Login = new HashMap<>();
                if (User != null && !User.getText().toString().equals(getString(R.string.default_user))) {
                    Login.put(Data.user, User.getText().toString());
                }
                if (Password != null && !Password.getText().toString().equals("")) {
                    Login.put(Data.pass, Password.getText().toString());
                }
                if (Login.isEmpty()) {
                    Log.d("Login", "NO Illegal Login Set");
                }
                Data.setLogin(Login);
            }
            Map<String,String> HostAddress = new HashMap<>();
            if (LocalHost!=null&&!LocalHost.getText().toString().equals(getString(R.string.default_localhostaddress))) {
                HostAddress.put(Data.LocalHost, LocalHost.getText().toString());
            }
            if (InternetHost!=null&&!InternetHost.getText().toString().equals(getString(R.string.default_internethostaddress))){
                HostAddress.put(Data.InternetHost, InternetHost.getText().toString());
            }
            if (HostAddress.isEmpty()) {
                Log.d("HostAddress", "NO Illegal HostAddress Set");
            }
            Data.setHostAddress(HostAddress);
        }

        private  SetButtonListener configure (EditText User, EditText Password, EditText LocalHost, EditText InternetHost){
            this.User=User;
            this.Password=Password;
            this.LocalHost=LocalHost;
            this.InternetHost=InternetHost;
            return this;
        }

        private void setChangeEditTexts(EditText changeUser, EditText changePassword){
            this.changeUser=changeUser;
            this.changePassword=changePassword;
        }


    }

    private class ChangeButtonListener implements View.OnClickListener{
        Button Set;

        @Override
        public void onClick(View v) {
            Set.setText(getString(R.string.change_login));
            int changeUserid=changeUserID;int changePassid=changePasswordID;
            EditText temp1 = (EditText) getView().findViewById(changeUserid);
            EditText temp2 = (EditText) getView().findViewById(changePassid);
            if (temp1==null&&temp2==null) {
                EditText changeUser = new EditText(getActivity());
                changeUser.setId(changeUserid);
                changeUser.setHint(R.string.change_datacenters_user);
                EditText changePass = new EditText(getActivity());
                changePass.setId(changePassid);
                changePass.setHint(R.string.change_datacenters_password);
                LinearLayout layout = (LinearLayout) getView();
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.addView(changeUser, 2, layoutParams);
                layout.addView(changePass, 3, layoutParams);
                setButtonListener.setChangeEditTexts(changeUser, changePass);
            }
        }

        private  ChangeButtonListener configure (Button Set){
            this.Set=Set;
            return this;
        }

    }

}
