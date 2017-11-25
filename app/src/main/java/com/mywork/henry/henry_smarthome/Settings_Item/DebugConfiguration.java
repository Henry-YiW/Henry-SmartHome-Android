package com.mywork.henry.henry_smarthome.Settings_Item;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mywork.henry.henry_smarthome.Data;
import com.mywork.henry.henry_smarthome.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Henry on 2016/12/9.
 */

public class DebugConfiguration extends Fragment {
    WebView webview;
    Handler handler=new Handler();
    Bundle webviewStateSet = new Bundle();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings_content3,container,false);
        if (webview!=null){
            webview.restoreState(savedInstanceState);
        }else {
            webview = (WebView) view.findViewById(R.id.WebView);
            webview.getSettings().setJavaScriptEnabled(true);
            //webview.getSettings().setDefaultFixedFontSize();
            webview.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
            webview.setHorizontalScrollBarEnabled(false);
            webview.getSettings().setSupportZoom(false);
            webview.getSettings().setBuiltInZoomControls(false);
            webview.setInitialScale(200);
            //webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            //webview.loadDataWithBaseURL();
            //webview.loadData();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("user", "henry");
            parameters.put("pass", "yiweigang");
            parameters.put("Refresh", "Get");
            //String tempURL = "http://henry95-home.asuscomm.com:9982/Smart_Home/debugconfig?Refresh=Get&user=henry&pass=yiweigang";
            String tempURL = "http://168.150.116.167:8080/Smart_Home/debugconfig?Refresh=Get&user=henry&pass=yiweigang";
            //webview.loadUrl(Data.getURL(Data.debugconfig,true));

            webview.setBackgroundColor(getResources().getColor(R.color.default_backgroundColor));
            webview.loadUrl(tempURL,parameters);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    return false;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.d("WebView Error", error.getDescription().toString() + error.getErrorCode() + "@" + request.getUrl().getHost());
                    }
                    if (request.isForMainFrame()) {
                        //onReceivedError(view,error.getErrorCode(), error.getDescription().toString(),request.getUrl().toString());

                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        if (webview!=null){
            webview.restoreState(webviewStateSet);
            webview.resumeTimers();
            webview.onResume();

        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getView()==null||getView().getWidth()==0||webview==null){
                    handler.postDelayed(this,10);
                }else{
                    webview.setInitialScale((int)(getView().getWidth()/8.15));
                    Log.d("View", getView().getWidth()+"");
                }
            }
        },10);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (webview!=null){
            webview.pauseTimers();
            webview.onPause();
            webview.saveState(webviewStateSet);
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        //Log.d("OnStop","Runn");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        //webview.loadUrl("about:blank");
        webview.removeAllViews();
        ViewParent parent = webview.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(webview);
        }
        webview.stopLoading();
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        webview.getSettings().setJavaScriptEnabled(false);
        webview.clearHistory();
        webview.loadUrl("about:blank");
        webview.destroy();
        webview=null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        //Log.d("OnDestroy","Runn");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        //Log.d("OnDetach","Runn");
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (webview!=null)
            webview.saveState(outState);
    }
}
