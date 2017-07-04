package com.mywork.henry.henry_smarthome.test;

/**
 * Created by Henry on 2016/10/15.
 */
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mywork.henry.henry_smarthome.Data;
import com.mywork.henry.henry_smarthome.FormatFactory;
import com.mywork.henry.henry_smarthome.OKHttpTool;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.Future;


public class test extends Object{
    String data;
    public  static void ddd(){
        String url="jdbc:mysql://localhost/tomcat?user=root&password=yiweigang&useUnicode=true&characterEncoding=utf-8";
        ResultSet rs;Statement statement;Connection con;
        try {
            con=DriverManager.getConnection(url);
            statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String sql="select * from test";
            rs=statement.executeQuery(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    class ddd<e extends Drawable.Callback>{
        e l;
        <k extends Collection> void vvf(k Dat){
            //l=(e)new Object();
            l.getClass();
            ddd <? super ProgressBar> afaf=new ddd<View>();
            afaf.getClass().getGenericSuperclass();
        }
    }

    class MyClass<T> {

        private Constructor<? extends T> ctor;

        private T field;

        MyClass(Class<? extends T> impl) {
            try {
                this.ctor = impl.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public <T extends Collection> void myMethod() throws Exception {
            field = ctor.newInstance();
        }


    }
    private static final Object pp=34;
    public static void main(String[] args){
        String sdd = "192.168.1.12";
        System.out.println(sdd.substring(sdd.length()-2));
        System.out.println(sdd.substring(1));
        //System.out.println(sdd.length());
        int tempid=Integer.valueOf(sdd.substring(sdd.length()-2));
        tempid=tempid-10;
        String id = String.valueOf(tempid);
        System.out.println(id);
        String URL = "192.168.1.2";

        System.out.println(URL);
        String id2=URL.substring(URL.length()-2);
        String URL2=id2.replace(".","");
        System.out.println(URL2);
        System.out.println(id2);
        //URL2.substring(3);
        int id3=Integer.valueOf(URL2);
        System.out.println("HASOUT"+id3);
        int colorss=0xffffffff;
        System.out.println(colorss);
        String color ="0xffffffff";
        //int colors=Integer.valueOf(String.valueOf(color));
        //System.out.println(colors);

        subtest t =new subtest(true,10);
        Field temp = null;
        try {
            temp=t.getClass().getSuperclass().getDeclaredField("kkk");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("SuperClass KKK:"+temp.getInt(new subtest(false,6)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.print(t.kkk);//用这种方式可以获得一个对象的父类的同名变量的值；
        Class kk=Integer.class;
        //Class.forName("dfdf")
        subtest2<Button> sub=new subtest2<>(Button.class);
        Button button =  sub.getObject();
        int kak=1;
        //sub.toArray(kak);
        //button.setActivated(true);
        Object ksk = 33;
        String kddk="2016-10-18 22:28:23.0";
        //kddk=kddk.substring(0,kddk.length()-2);
        System.out.println(kddk);
        String[] tempString=kddk.split(" ");
        String[] tempDate=tempString[0].split("-");
        String[] tempTime=tempString[1].split(":");
        int Date[]=new int[tempDate.length];
        int Time[]=new int[tempTime.length];
        for (int index=0;index<=Date.length-1;index++){
            Date[index]= Math.round(Float.valueOf(tempDate[index]));
        }
        for (int index=0;index<=Time.length-1;index++){

            Time[index]= Math.round(Float.valueOf(tempTime[index]));
        }

        int[] Array=new int[Date.length+Time.length];
        for (int index=0;index<=Date.length+Time.length-1;index++){
            Array[index]=(index<=Date.length-1)?Date[index]:Time[index-Date.length];
        }
        for (int data:Array){
            System.out.println(data);
        }
        ArrayList<int[]> datasetTimeInt=new ArrayList<>();
        int [] a=new int[6];int [] b=new int[6];int [] c=new int[6];int [] d=new int[6];
        a[0]=2014;a[1]=8;a[2]=23;a[3]=12;a[4]=20;a[5]=42;b[0]=2013;b[1]=3;b[2]=1;b[3]=3;b[4]=0;b[5]=22;
        c[0]=2012;c[1]=1;c[2]=23;c[3]=12;c[4]=20;c[5]=42;d[0]=2017;d[1]=11;d[2]=16;d[3]=5;d[4]=23;d[5]=52;
        datasetTimeInt.add(a);datasetTimeInt.add(b);datasetTimeInt.add(c);datasetTimeInt.add(d);
        int maxYear=0;int minYear=datasetTimeInt.get(1)[0];
        for (int[] array:datasetTimeInt){
            maxYear=Math.max(maxYear,array[0]);
            minYear=Math.min(minYear,array[0]);

        }
        for (int[] array:datasetTimeInt){
            System.out.println(array[0]-minYear);
            //System.out.println(array[0]%4==0?366:365);
        }
        ArrayList<int[]> processedDataset=new ArrayList<>();
        for (int[] array:datasetTimeInt) {
            int[] Dataset = new int[3];
            for(int index=0;index<array[0]-minYear;index++){
                Dataset[1]+=((index+minYear)%4==0)? 366:365;
            }
            for (int index = 1; index <= array[1] - 1; index++) {
                if (index == 2) {
                    Dataset[1] += (array[0] % 4 == 0) ? 29 : 28;
                } else if (index <= 7 && (index - 1) % 2 == 0) {
                    Dataset[1] += 31;
                } else if (index > 7 && (index) % 2 == 0) {
                    Dataset[1] += 31;
                } else {
                    Dataset[1] += 30;
                }
            }
            Dataset[1] += array[2] - 1;
            Dataset[2] = array[3] * 3600;
            Dataset[2] += array[4] * 60;
            Dataset[2] += array[5];
            Dataset[0] = Dataset[1] * 24 * 3600 + Dataset[2];
            processedDataset.add(Dataset);
        }
        for (int [] kkk:processedDataset){
            //for(int jjj:kkk){
            //    System.out.print(jjj+"-");
            //}
            System.out.println(kkk[0]);
        }
        int max=0;int min=processedDataset.get(1)[0];
        for (int[] array : processedDataset) {
            max = Math.max(max, array[0]);
            min = Math.min(min, array[0]);
        }
        System.out.println("Max"+max);System.out.println("Min"+min);
        System.out.println("MaxYear"+maxYear);System.out.println("MinYear"+minYear);
        System.out.println("kkk");
        //SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format=SimpleDateFormat.getDateTimeInstance();
        System.out.println(Locale.getDefault());
        System.out.println(format.format(new Date()));
        System.out.println(new Date());
        System.out.println(System.currentTimeMillis());
        System.out.println(convertDatetoSeconds(format.format(new Date())));
        System.out.println(convertDatetoSeconds("2016-10-28 14:32:06"));
        System.out.println(Math.abs(convertDatetoSeconds(format.format(new Date()))-convertDatetoSeconds("2016-10-27 14:49:06")));
        System.out.println(Math.abs(convertDatetoSeconds("2016-10-1 0:0:0")-convertDatetoSeconds("2016-10-31 -47:-59:-60")));
        System.out.println(Math.abs(convertDatetoSeconds("2016-10-28 07:48:49")-convertDatetoSeconds("2016-11-25 07:48:50")));
        System.out.println(28*24*3600);
        ArrayList <Integer> kjl=new ArrayList<>();
        kjl.add(kjl.size(),3434);
        kjl.add(kjl.size(),4555);
        kjl.add(1,99999);
        kjl.add(1,252525);
        String msg="";int tempstatus=15;
        tempstatus|=Data.Type_apparatuscfgset;
        Object aa=34;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.yield();
                synchronized (pp) {
                    try {
                        pp.wait(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pp.notify();
                }
            }
        }).start();

        synchronized (pp) {
            try {
                pp.wait();
                System.out.println("Exit");
            } catch (InterruptedException e) {
                e.printStackTrace();
                main(null);
            }
        }

        final Thread[] threads=new Thread[5];
        final realrun2 [] realrun2s=new realrun2[5];
        realrun2s[0]=new realrun2(null,12000);
        threads[0]=new Thread(realrun2s[0]);
        threads[0].start();
        realrun2s[0].setrun(new Runnable() {
            @Override
            public void run() {
                System.out.println("Threadhello2");
            }
        });
        realrun2s[0].refresh();
        synchronized (realrun2s[0]) {
            realrun2s[0].notify();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (realrun2s[0]) {
                    realrun2s[0].setrun(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("ThreadOK1");
                        }
                    });
                    realrun2s[0].refresh();
                    realrun2s[0].notify();
                    //threads[0].interrupt();
                }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                synchronized (realrun2s[0]) {
                    realrun2s[0].setrun(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("ThreadOK2");
                        }
                    });
                    realrun2s[0].refresh();
                    realrun2s[0].notify();
                    //threads[0].interrupt();
                }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                synchronized (realrun2s[0]) {
                    realrun2s[0].setrun(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("ThreadOK3");
                        }
                    });
                    realrun2s[0].refresh();
                }
                realrun2s[0].end();
                    //threads[0].interrupt();



            }
        }).start();
        hellok hellok=new hello();
        hellok.run();



        System.out.println(aa.equals(pp));
        //tempstatus|=Data.Type_Humidity;
        //tempstatus|=Data.Type_Temperature;
        tempstatus|=Data.Type_Electricity;
        System.out.print("Tempstatus"+Integer.toBinaryString(tempstatus)+"-");
        if ((tempstatus&1)==1){
            msg+="ElectricityBeenSet ";
        }
        tempstatus=tempstatus>>1;
        System.out.print("Tempstatus"+Integer.toBinaryString(tempstatus)+"-");
        if ((tempstatus&1)==1){
            msg+="HumidityBeenSet ";
        }
        tempstatus=tempstatus>>1;
        System.out.print("Tempstatus"+Integer.toBinaryString(tempstatus)+"-");
        if ((tempstatus&1)==1){
            msg+="TemperatureBeenSet ";
        }
        tempstatus=tempstatus>>1;
        System.out.println("Tempstatus"+Integer.toBinaryString(tempstatus));
        if ((tempstatus&1)==1){
            msg+="ApparatusBeenSet ";
        }
        System.out.println("DataStatus: "+msg);
        cancelAllConnections(OKHttpTool.ConnectionPool);
        cancelAllConnections(OKHttpTool.Dispatcher);
        cancelAllConnections(OKHttpTool.Dispatcher |OKHttpTool.ConnectionPool);
        for (int lll:kjl){
            System.out.println(lll);
        }
        //System.out.println(kjl.get(kjl.size()-1));
        String kka="454:545d:fd";
        //byte[] temptemp=kka.getBytes();
        //boolean reading=false;boolean reading2;
        String kkanew="";
        String [] kkkannn= kka.split("\\.");
        if (kkkannn.length==1){kkkannn=kka.split(":");}
        kkanew=kkkannn[kkkannn.length-1];
        //for (int index=0;index<kka.length();index++){
        //    reading2 = kka.charAt(index) != ".".charAt(0);
        //    if (reading&&reading2){
        //        kkanew+=kka.charAt(index);
        //    }
        //    if (kka.charAt(index)==".".charAt(0)){
        //        reading=true;
        //    }
        //}
        System.out.println(kkanew);

        int kkk=3;Integer jjjj=3;
        String kkkk=(jjjj==kkk)? "dff":"";
        kkkk=(Integer.valueOf("3")==kkk)? "dff":"";
        kkkk+=33;
        String number="0xffff8aff";long sum=0;
        number=number.substring(2,number.length());
        number=number.toUpperCase();
        System.out.println(number);
        for (int i=0;i<number.length();i++){
            int multiplier=0;
            switch (number.charAt(i)){
                case '1':multiplier=1;break;
                case '2':multiplier=2;break;
                case '3':multiplier=3;break;
                case '4':multiplier=4;break;
                case '5':multiplier=5;break;
                case '6':multiplier=6;break;
                case '7':multiplier=7;break;
                case '8':multiplier=8;break;
                case '9':multiplier=9;break;
                case 'A':multiplier=10;break;
                case 'B':multiplier=11;break;
                case 'C':multiplier=12;break;
                case 'D':multiplier=13;break;
                case 'E':multiplier=14;break;
                case 'F':multiplier=15;break;
                default:new NumberFormatException();

            }
            sum+=multiplier*Math.pow(16,(number.length()-i-1));
            //System.out.print(sum);
        }
        //System.out.println();
        //System.out.println(sum);
        String realIntString=String.valueOf(Long.toBinaryString(sum-1));
        String sign;
        if (realIntString.charAt(0)=='1'){
            if (realIntString.length()==32){
                sign="-";
            }else {
                sign="";
            }
        }else {
            if (realIntString.length()==32){
                sign="+";
            }else {
                sign="";
            }
        }
        String inverseIntString="";
        for (int i =0;i<realIntString.length();i++){
            if (realIntString.charAt(i)=='1'){
                inverseIntString+="0";
            }else {
                inverseIntString+="1";
            }
        }
        String finalIntString=sign+inverseIntString;
        System.out.println(finalIntString);
        int Int=Integer.parseInt(finalIntString,2);
        System.out.println(Int);
        System.out.println(0xffff8aff);
        System.out.println(Integer.toBinaryString(1073741824));
        String ppp="jkdjkfmdfdf af a fa a fafa fafaf fafa     ";
        System.out.println(ppp.trim().replace(' ',','));
        System.out.println(validateStringFormatforHex2("0x7FFFFFF8"));
        long tempt=4294967295L;
        System.out.println((int)tempt);
        System.out.println(Integer.parseInt("-10000000000000000000000000000000",2));
        //System.out.println("EEEEEEEE:"+"".substring(2,6));
        float p=0.2f;
        System.out.println(p*12);
        System.out.println((2*1f)/((float) Math.pow(1f,2)));
        System.out.println("Degree:2342C<-!afkfkf2-->".toUpperCase().substring(0,7));
        String body="Degree:55416464c<-!afkfkf2-->".toUpperCase();
        String prefix = body.substring(0,7);char sign2=0;Integer print=null;
        if (prefix.equals("DEGREE:")){
            try{
                Integer startindex=7;Integer endindex=null;
                for (int i=7;i<body.length();i++){
                    if (!Character.isDigit(body.charAt(i))){
                        sign2=body.charAt(i);
                        endindex=i;
                        break;
                    }
                }
                String degree=body.substring(startindex, endindex);
                System.out.println(degree);
                int tempint=Integer.parseInt(degree);
                System.out.println(sign2);
                if (sign2=='C'){
                    print=tempint;
                }else if (sign2=='F'){
                    print=((tempint-32)*5)/9;
                }else if (sign2=='K'){
                    print=(int) (tempint-273.15);
                }
            }catch (Exception e){
                System.out.print("||DegreeResponse String Format Not Right||");
            }
        }
        Integer Degree=212; char UnitSign='F';
        System.out.println(print);
        if (Degree!=null){
            switch (UnitSign){
                case 'C':
                case 'c':break;
                case 'F':
                case 'f':Degree=((Degree-32)*5)/9;break;
                case 'K':
                case 'k':Degree=(int) (Degree-273.15);break;
                default:throw new NumberFormatException("Not A Supported Unit");
            }
        }
        System.out.println(Degree);
        HashMap <String,String> afa=new HashMap<>(7);
        Integer sf=45;
        sf.intValue();
        Boolean ff=true;
        ff.booleanValue();
        //try {
        //    System.out.println(test2());
        //}catch (IOException e){
        //    e.printStackTrace();
        //    System.out.println("Exceptioned");
        //    return;
        //}finally{
        //    System.out.println("Ended");
        //}
        System.out.println("Hello");
        int realFrameColor3=0xD9664455;
        System.out.println(realFrameColor3);
        System.out.println(0x02664455);
        System.out.println(FormatFactory.getColorWithoutAlpha(realFrameColor3)+0x02000000);
        final DateFormat formatt=SimpleDateFormat.getDateTimeInstance();
        //System.out.println(formatt.format("2016:01:05 01:05:05"));
        System.out.println(convertDatetoSeconds2("0002-02-02 ",Data.Date,true));
        System.out.println(convertDatetoSeconds2("0002-02-02 ",Data.Time,true));
        System.out.println(convertDatetoSeconds2("6-",Data.DateTime,true));
        System.out.println(winnowoutFirstfromDateTime("gjg"));
        System.out.println(winnowoutFirstfromDateTime("gjg afaf"));
        System.out.println(winnowoutFirstfromDateTime("gjg 01:01:01"));
        System.out.println(winnowoutFirstfromDateTime("01:01:01 01:01:01"));
        System.out.println(winnowoutFirstfromDateTime("2016-08-07 5:05:06"));
        System.out.println(convertDatetoSeconds2(winnowoutFirstfromDateTime("2016-08-07 5:05:"),Data.Date,true));
        System.out.println(convertDatetoSeconds2(winnowoutFirstfromDateTime("2016-08-07 5:05:06"),Data.Time,true));
        System.out.println(convertDatetoSeconds2(winnowoutFirstfromDateTime("2016-08-07 05:06"),Data.DateTime,true));
        System.out.println();System.out.println();
        boolean faf=false;
        System.out.println(true&faf);
        System.out.println();
        String user;String pass;
        String UserPass="faffa";
        String[] tempUserPass = UserPass.split(":");
        user=tempUserPass[0];
        if (tempUserPass.length>1){
            pass=tempUserPass[tempUserPass.length-1];
        }else {
            pass="";
        }
        System.out.println("User:"+user);System.out.println("Pass:"+pass);
    }


    public static String winnowoutFirstfromDateTime (String DateTime){
        DateTime=DateTime.trim();
        String [] tempDateTime = DateTime.split(" ");
        String sign="-";
        String [] tempFirst = tempDateTime[0].split(sign);
        if (tempFirst.length<=1){
            sign=":";
            tempFirst=tempFirst[0].split(sign);
        }
        String result="";
        for (int i=1;i<tempFirst.length;i++){
            if (i<tempFirst.length-1){
                result+=tempFirst[i]+sign;
            }else {
                result+=tempFirst[i];
            }
        }
        result +=" ";
        for (int i=1;i<tempDateTime.length;i++){
            result+=tempDateTime[i];
        }
        result = result.trim();
        if (result.isEmpty()){
            result=DateTime;
        }
        return result;
    }

    public static long convertDatetoSeconds2(String Date,int Type,boolean whethertoprint){
        long[] Dataset = new long[3];
        int[] array=formatData2(Date.trim());
        //Dataset[1]+=365*(array[0]-1)+Math.floor((array[0]-1)/4);
        for (int index = 1; index < array[0]; index++) {
            Dataset[1] += ((index) % 4 == 0) ? 366 : 365;
        }
        for (int index = 1; index <= array[1] - 1; index++) {
            if (index == 2) {
                Dataset[1] += (array[0] % 4 == 0) ? 29 : 28;
            } else if (index <= 7 && (index - 1) % 2 == 0) {
                Dataset[1] += 31;
            } else if (index > 7 && (index) % 2 == 0) {
                Dataset[1] += 31;
            } else {
                Dataset[1] += 30;
            }
        }
        if (array[2]<=0){
            Dataset[1]+=0;
        }else{
            Dataset[1] += array[2] - 1;
        }
        Dataset[2] = array[3] * 3600;
        Dataset[2] += array[4] * 60;
        Dataset[2] += array[5];
        if (whethertoprint){
            System.out.println("Days:"+Dataset[1]+"Seconds:"+Dataset[2]);
        }
        Dataset[0] = Dataset[1] * 24 * 3600 + Dataset[2];
        switch (Type){
            case Data.DateTime:return Dataset[0];
            case Data.Date:return Dataset[1];
            case Data.Time:return Dataset[2];
            default:break;
        }
        return Dataset[0];
    }

    private static int[] formatData2(String data){
        //String d ="2016-10-18 22:28:23.0";
        //data=data.substring(0,data.length()-2);
        String[] tempDate;String[] tempTime=null;int[] Time=new int[3];int[] Date=new int[3];

        String[] tempString=data.split(" ");

        tempDate=tempString[0].split("-");
        for (String lll:tempDate){
            System.out.println(lll);
        }
        if (tempDate.length>1){
            for (int i=0;i<=Date.length-1;i++){
                int index = i-(Date.length-tempDate.length);
                if (index>=0){
                    System.out.println(tempDate[index]);
                    Date[i]=Math.round(Float.parseFloat(tempDate[index]));
                }
                if (Date[i]<=0){Date[i]=1;}
            }
        }
        if (tempString.length>1){
            tempTime=tempString[1].split(":");
        }
        else {
            tempTime=tempString[0].split(":");
        }
        if (tempTime.length>1){
            for (int i=0;i<=Time.length-1;i++){
                int index = i-(Time.length-tempTime.length);
                if (index>=0){
                    Time[i]=Math.round(Float.parseFloat(tempTime[index]));
                }
            }
        }

        int[] Array=new int[Time.length+Date.length];
        for (int index=0;index<=Time.length+Date.length-1;index++){
            Array[index]=(index<=3-1)?Date[index]:Time[index-Date.length];
        }
        return Array;
    }

    //static class tesst {
    //    String lll="tesst";
    //
    //    void ljl (){
    //        System.out.println("tesst:Print");
    //    }
    //}
    //
    //static class tesst2 extends tesst{
    //    String lll="tesst2";
    //
    //    void ljl (){
    //        System.out.println("tesst2:Print");
    //    }
    //}

    private static String test2 ()throws IOException{
        try {

            String result = getResponseWithInterceptorChain();
            if (result == null) throw new IOException("Canceled");
            return result;
        } finally {
            System.out.println("Finished");
        }
    }

    private static String getResponseWithInterceptorChain (){
        throw new NullPointerException();
    }

    private static class runcore{
        void run(){

        }

    }

    private static class realrun2 implements Runnable{
        Runnable run;int time;boolean whethertorestart=false;
        boolean cometoend=false;
        @Override
        public void run() {
            synchronized (this){
                whethertorestart=false;
                if(run!=null) {
                    run.run();
                }
                try {
                    this.wait(time);
                    if (whethertorestart){
                        run();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
            cometoend=true;
            System.out.println("hasended");
        }
        realrun2(Runnable run,int time){
            this.run=run;this.time=time;
        }
        void setrun(Runnable run){
            this.run=run;
        }
        void refresh(){
            this.whethertorestart=true;
        }
        void end(){
            synchronized (this) {
                System.out.println("FirstNotify");
                this.notify();
            }
            while(!this.cometoend) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Thread.yield();
                synchronized (this) {
                    System.out.println("SecondNotify");
                    this.notify();
                }
            }

        }
    }



    public static int validateStringFormatforHex2 (String number)throws NumberFormatException{
        long sum=0;
        //if (number.length()!=10){
        //    throw new NumberFormatException("Length is not Right");
        //}
        number=number.substring(2,number.length());
        number=number.toUpperCase();
        System.out.println(number);
        for (int i=0;i<number.length();i++){
            int multiplier=0;
            switch (number.charAt(i)){
                case '0':multiplier=0;break;
                case '1':multiplier=1;break;
                case '2':multiplier=2;break;
                case '3':multiplier=3;break;
                case '4':multiplier=4;break;
                case '5':multiplier=5;break;
                case '6':multiplier=6;break;
                case '7':multiplier=7;break;
                case '8':multiplier=8;break;
                case '9':multiplier=9;break;
                case 'A':multiplier=10;break;
                case 'B':multiplier=11;break;
                case 'C':multiplier=12;break;
                case 'D':multiplier=13;break;
                case 'E':multiplier=14;break;
                case 'F':multiplier=15;break;
                default:throw new NumberFormatException("Invalid Char in the String");

            }
            sum+=multiplier*Math.pow(16,(number.length()-i-1));
            //System.out.print(sum);
        }
        //System.out.println();
        //System.out.println(sum);


        if(sum>4294967295L){
            return 2147483647;}
        // else if (sum<=2147483647){
        //    return (int)sum;
        //}
        //long positivizedmaximumofint = 4294967295L;
        //int Int = (int)((positivizedmaximumofint+1-sum)*(-1));
        //System.out.println(Int);
        return (int)sum;
    }

    private static class realrun implements Runnable{
        Runnable run;final Integer kkk=0;int period;
        @Override
        public void run() {
            run.run();
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
                run();
            }
            System.out.println("has ended");
            //synchronized (kkk){
            //    try {
            //        kkk.wait(period);
            //    } catch (InterruptedException e) {
            //        e.printStackTrace();
            //        run();
            //    }
            //}
        }
        realrun(Runnable run,int period){
            this.run=run;this.period=period;
        }
        void setrun(Runnable run){
            this.run=run;
        }
    }

    static class hellok {
        void run(){
            System.out.println("hellok");
        }
    }

    static class hello extends hellok{
        String kjj="hello";
        void run(){
            kok();
        }
        void kok(){
            System.out.println(kjj);
        }
    }

    public static void cancelAllConnections(int Type){
        if ((Type&1)==1){
            System.out.print("ConnectionPollCleaned-");
        }
        Type=Type>>1;
        if ((Type&1)==1){
            System.out.print("DispatcherCleaned-");
        }
        System.out.println();
    }
    static class supertest {
        protected int kkk=700;
        //supertest(){
        //    System.out.println("SuperCLass");
        //}
        supertest(int test){
            System.out.println("SuperCLass"+test);
        }


    }

    static class subtest extends supertest {
        protected int kkk;
        subtest(boolean bool,int kk){
            super(3);
            kkk=2;
            super.kkk=kk;

            System.out.println("SubClass"+bool);
        }


    }

    public static long convertDatetoSeconds(String Date){
        long[] Dataset = new long[3];
        int[] array=formatData(Date);
        for (int index = 1; index < array[0]; index++) {
            Dataset[1] += ((index) % 4 == 0) ? 366 : 365;
        }
        for (int index = 1; index <= array[1] - 1; index++) {
            if (index == 2) {
                Dataset[1] += (array[0] % 4 == 0) ? 29 : 28;
            } else if (index <= 7 && (index - 1) % 2 == 0) {
                Dataset[1] += 31;
            } else if (index > 7 && (index) % 2 == 0) {
                Dataset[1] += 31;
            } else {
                Dataset[1] += 30;
            }
        }
        Dataset[1] += array[2] - 1;
        Dataset[2] = array[3] * 3600;
        Dataset[2] += array[4] * 60;
        Dataset[2] += array[5];
        System.out.println("Days:"+Dataset[1]+"Seconds:"+Dataset[2]);
        Dataset[0] = Dataset[1] * 24 * 3600 + Dataset[2];
        return Dataset[0];
    }

    private static int[] formatData(String data){
        //String d ="2016-10-18 22:28:23.0";
        //data=data.substring(0,data.length()-2);
        String[] tempString=data.split(" ");
        String[] tempDate=tempString[0].split("-");
        String[] tempTime=tempString[1].split(":");
        int Date[]=new int[tempDate.length];
        int Time[]=new int[tempTime.length];
        for (int index=0;index<=Date.length-1;index++){
            Date[index]=Math.round(Float.valueOf(tempDate[index]));
            if (Date[index]<=0){Date[index]=1;}
        }
        for (int index=0;index<=Time.length-1;index++){
            Time[index]=Math.round(Float.valueOf(tempTime[index]));
        }

        int[] Array=new int[Date.length+Time.length];
        for (int index=0;index<=Date.length+Time.length-1;index++){
            Array[index]=(index<=Date.length-1)?Date[index]:Time[index-Date.length];
        }
        return Array;
    }
    static class subtest2<k extends TextView> extends supertest{
        k object;Class<? super k> rawType;
        Class<k> rawType2;
        subtest2(Class<k> classtype) {
            super (5);
            getClass();
            rawType=classtype;
        }
        k getObject(){

            Constructor construtor;
            try {
                //rawType2= (Class<k>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                construtor=rawType.getConstructor(Context.class);
                //object =(k)construtor.newInstance(new MainActivity());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
//这里用super X的话那 这个泛型就不知道该有什么方法和成员变量了，因为上面的类的方法都是不包含下面的类的也就是不包含X的，只有下面的也就是继承自X的类是一定包含X的方法和成员变量的。
        return object;}
        <l extends   Object> l toArray(Button a){
            l k;Button kkk=null;
            //k=(l)kkk;
            process(a);

        return (l)kkk;}

        <a> a process (a k){
            Button kk= (Button)k;
            kk.setX(99);
            a jj =(a) kk;
        return jj;}
    }



    static class kkllsuper extends Data {
        void dee(){
            //super.ffff();
        }
    }
}

