package com.mywork.henry.henry_smarthome;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.RealInterceptorChain;

/**
 * Created by Henry on 2015/5/10.
 */

public class OKHttpTool {
    private static final OkHttpClient client=new OkHttpClient();

    private static Handler handler;
    private static final MediaType Json = MediaType.parse("application/json;charset=utf-8");
    private static final MediaType Text = MediaType.parse("text/x-markdown;charset=utf-8");
    public static final int ConnectionPool=1;public static final int Dispatcher =2;
    public static final int Cache = 4;

    static {
        handler=new Handler(Looper.getMainLooper());

    }



    public OkHttpClient CustomizeOKHttpClient (TimeoutSet connectTimeout,TimeoutSet readTimeout,
                                               TimeoutSet writeTimeout,Boolean retryOnConnectionFailure
                                                ,Interceptor interceptor){
        OkHttpClient.Builder builder=client.newBuilder();
        if (connectTimeout!=null){
            builder.connectTimeout(connectTimeout.timeout,connectTimeout.unit);
        }
        if (readTimeout!=null){
            builder.readTimeout(readTimeout.timeout,readTimeout.unit);
        }
        if (writeTimeout!=null){
            builder.writeTimeout(writeTimeout.timeout,writeTimeout.unit);
        }
        if (retryOnConnectionFailure!=null){
            builder.retryOnConnectionFailure(retryOnConnectionFailure);
        }
        if (interceptor!=null){
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    class CustomRetryInterceptor implements Interceptor{
        private final int maxTry;
        @Override
        public Response intercept(Chain chain) throws IOException {
            int trytimes=1;Response response=chain.proceed(chain.request());
            while (true){

                if (response.isSuccessful()||trytimes>maxTry){
                    break;
                }
                response=chain.proceed(chain.request());
                trytimes++;
            }
            return response;
        }

        CustomRetryInterceptor (int maxTry){
            this.maxTry=maxTry;
        }
    }


    //public static OkHttpClient getClient(){
    //    if (client==null){
    //        synchronized (OKHttpTool.class) {
    //            client = new OkHttpClient();
    //        }
    //    }
    //    return client;
    //}

    public static void cancelAllConnections(int Type){
        if ((Type&1)==1){
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
        }
        Type=Type>>1;
        if ((Type&1)==1){
            client.dispatcher().cancelAll();
        }
        Type=Type>>1;
        if ((Type&1)==1){
            try {
                client.cache().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void asyncJsonString(String URL,final processString processString,final processFailure processFailure,final int ModetoProcessResponse){
        final Request request = new Request.Builder().url(URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                onFailed(processFailure);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                switch (ModetoProcessResponse){
                    case 1:
                        Log.d("ResponseMode","Case1 Happened");
                        if(response!=null&&response.isSuccessful()) {
                            onJsonStringSuccess(response.body().string(), processString);
                        }else{
                            if (response!=null) {
                                onFailed(processFailure, response.code());
                            }else{
                                onFailed(processFailure);
                            }
                        }
                        break;
                    case 2:
                        Log.d("ResponseMode","Case2 Happened");
                        String data;
                        if (response==null||!response.isSuccessful()||response.body()==null){
                            data ="No Data";
                        }else {
                            data=response.body().string();
                        }
                        onJsonStringSuccess(data, processString);
                        break;
                }
                if (response!=null) {
                    response.close();
                }
                call.cancel();
            }
        });
    }

    public static void asyncJsonObject(String URL,final processJsonObject processJsonObject,final processFailure processFailure){
        final Request request = new Request.Builder().url(URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                onFailed(processFailure);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()){
                    onJsonObjectSuccess(response.body().string(),processJsonObject);
                }else{
                    if (response!=null) {
                        onFailed(processFailure, response.code());
                    }else{
                        onFailed(processFailure);
                    }
                }
                if (response!=null) {
                    response.close();
                }
                call.cancel();
            }
        });
    }

    public static void asyncCustomPost (String URL,String content,MediaType mediatype,final processString processString,final processFailure processFailure){
        RequestBody body =RequestBody.create(mediatype,content);
        Request request=new Request.Builder().url(URL).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                processFailure.onFailure();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful()) {
                    processString.onResponse(response.body().string());
                }else{
                    if (response!=null) {
                        //processFailure.onFailure(response.code())
                        processFailure.onFailure();
                    }else{
                        processFailure.onFailure();
                    }
                }
                if (response!=null) {
                    response.close();
                }
                call.cancel();
            }
        });
    }

    public static void asyncPostFormforJsonObject (String URL,Map<String,String> parameters,final processJsonObject processJsonObject,final processFailure processFailure){
        FormBody.Builder formbudiler=new FormBody.Builder();
        if (parameters!=null&&!parameters.isEmpty()){
            for (Map.Entry<String,String> temp:parameters.entrySet()){
                if (temp.getKey()!=null&&temp.getValue()!=null) {
                    formbudiler.add(temp.getKey(), temp.getValue());
                }
            }
            RequestBody requestbody=formbudiler.build();
            Request request=new Request.Builder().post(requestbody).url(URL).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onFailed(processFailure);
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response!=null&&response.isSuccessful()){
                        onJsonObjectSuccess(response.body().string(),processJsonObject);
                    }else{
                        if (response!=null) {
                            onFailed(processFailure, response.code());
                        }else{
                            onFailed(processFailure);
                        }
                    }
                    if (response!=null) {
                        response.close();
                    }
                    call.cancel();
                }
            });
        }
    }

    public static void asyncPostFormforString (String URL, Map<String,String> parameters, final processString processString, final processFailure processFailure, final int ModetoProcessResponse){
        FormBody.Builder formbudiler=new FormBody.Builder();
        if (parameters!=null&&!parameters.isEmpty()){
            for (Map.Entry<String,String> temp:parameters.entrySet()){
                if (temp.getKey()!=null&&temp.getValue()!=null) {
                    formbudiler.add(temp.getKey(), temp.getValue());
                }
            }
            RequestBody requestbody=formbudiler.build();Request request;
            try {
                request=new Request.Builder().post(requestbody).url(URL).build();
            }catch (Exception e){
                e.printStackTrace();
                onFailed(processFailure,900);
                return;
            }
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onFailed(processFailure);
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    switch (ModetoProcessResponse){
                        case 1:
                            Log.d("ResponseMode","Case1 Happened");
                            if(response!=null&&response.isSuccessful()) {
                                onJsonStringSuccess(response.body().string(), processString);
                            }else{
                                if (response!=null) {
                                    onFailed(processFailure, response.code());
                                }else{
                                    onFailed(processFailure);
                                }
                            }
                            break;
                        case 2:
                            Log.d("ResponseMode","Case2 Happened");
                            String data;
                            if (response==null||!response.isSuccessful()||response.body()==null){
                                data ="No Data";
                            }else {
                                data=response.body().string();
                            }
                            onJsonStringSuccess(data, processString);
                            break;
                    }
                    if (response!=null) {
                        response.close();
                    }
                    call.cancel();


                }
            });
        }
    }

    public static void asyncCustomPostFormforJsonObject (String URL,Map<String,String> parameters,final processJsonObject processJsonObject,final processFailure processFailure){
        FormBody.Builder formbudiler=new FormBody.Builder();
        if (parameters!=null&&!parameters.isEmpty()){
            for (Map.Entry<String,String> temp:parameters.entrySet()){
                if (temp.getKey()!=null&&temp.getValue()!=null) {
                    formbudiler.add(temp.getKey(), temp.getValue());
                }
            }
            RequestBody requestbody=formbudiler.build();
            Request request=new Request.Builder().post(requestbody).url(URL).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    processFailure.onFailure();
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response!=null&&response.isSuccessful()){
                        try {
                            processJsonObject.onResponse(new JSONObject(response.body().string()));
                        } catch (JSONException e) {

                            Log.d("NoValidJsonString","CheckMysqlConfiguration");

                        }
                    }else{
                        if (response!=null) {
                            //processFailure.onFailure(response.code());
                            processFailure.onFailure();
                        }else{
                            processFailure.onFailure();
                        }
                    }
                    if (response!=null) {
                        response.close();
                    }
                    call.cancel();
                }
            });
        }
    }

    public static void syncJsonString(String URL,final processString processString,processFailure processFailure){
        Request request = new Request.Builder().url(URL).build();
        Response response;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()){
                onJsonStringSuccess(response.body().string(),processString);
            }
            else {
                onFailed(processFailure);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void onFailed (final processFailure processFailure,int Status){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (processFailure!=null) {
                    //processFailure.onFailure(Status);
                    processFailure.onFailure();
                }
            }
        });
    }

    private static void onFailed (final processFailure processFailure){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (processFailure!=null) {
                    processFailure.onFailure();
                }
            }
        });
    }

    private static void onJsonStringSuccess (final String Jsonvalue,final processString processString){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (processString!=null){
                    try{
                        processString.onResponse(Jsonvalue);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static void onJsonObjectSuccess (final String Jsonvalue,final processJsonObject processJsonObject){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (processJsonObject!=null){
                    try{
                        processJsonObject.onResponse(new JSONObject(Jsonvalue));
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d("NoValidJsonString","CheckMysqlConfiguration");
                    }
                }
            }
        });
    }

    private static void onbyteSuccess (final byte[] data,final processbyte processbyte){
        if (processbyte!=null) {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    try {
                        processbyte.onResponse(data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }




    public interface processFailure{
        //void onFailure(int Status);
        void onFailure();
    }

    public interface processString{
        void onResponse(String value);
    }
    public interface processJsonObject{
        void onResponse(JSONObject value);
    }
    public interface processbyte{
        void onResponse(byte[] value);
    }
    public interface processBitmap{
        void onResponse(Bitmap bitmap);
    }

    class TimeoutSet {
        long timeout; TimeUnit unit;
        TimeoutSet (long timeout, TimeUnit unit){
            this.timeout=timeout;this.unit=unit;
        }
    }

}

