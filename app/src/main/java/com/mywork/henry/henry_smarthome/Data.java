package com.mywork.henry.henry_smarthome;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    private final static  Map<String,String> mdata = new HashMap<>();
    public final static int Inquire=0;public final static int InquireData=1;
    public final static int Control=2;public final static int RegistrationPlusStateupdate=3;
    public final static int debugconfig=4;
    public final static String LocalHost = "LocalHostAddress";public final static String InternetHost = "InternetHostAddress";
    public final static String user = "user";public final static String pass="pass";
    private final static String InquireURLfix ="/Smart_Home/Inquire";
    private final static String InquireDataURLfix="/Smart_Home/InquireData";
    private final static String ControlURLfix="/Smart_Home/Control";
    private final static String RegistrationPlusStateupdateURLfix="/Smart_Home/RegistrationPlusStateupdate";
    private final static String debugconfigURLfix="/Smart_Home/debugconfig";
    private final static String InquirePlusSetRegulations = "/Smart_Home/InquirePlusSetRegulations";
    private final static ArrayList<apparatuscfg> apparatuscfgset =new ArrayList<>();
    private final static ArrayList<configurations> configurationset = new ArrayList<>();
    private final static ArrayList<dataset> rawTemperature = new ArrayList<>();private static int Temperaturestartscale =5+-20-5;
    private final static ArrayList<dataset> rawHumidity =new ArrayList<>();    private static int Humiditystartscale =5+-20-5;
    //private static ArrayList<dataset> rawElectricity = new ArrayList<>();
    private final static ArrayList<dataset> Temperature = new ArrayList<>();
    private final static ArrayList<dataset> Humidity =new ArrayList<>();
    private final static ArrayList<dataset> Electricity = new ArrayList<>();
    public static final int Type_apparatuscfgset=8;public static final int Type_Temperature=4;
    public static final int Type_Humidity=2;public static final int Type_Electricity=1;
    public static volatile int Status_Temperature=0;public static volatile int Status_Humidity=0;
    public static volatile int Status_Electricity=0;public static volatile int Status_apparatus=0;
    public static final int RefreshDone=1;public static final int DefaultRefresh=0;public static final int RefreshFailed=-1;
    private final static ArrayList<Integer> TemperatureColorSet=new ArrayList<>();
    private final static ArrayList<Integer> HumidityColorSet=new ArrayList<>();
    public final static int frameColor=0xffffffff;
    public final static int frameColor2=0xff000000;
    public final static int TemperatureColor=0xffff9c00;
    public final static int HumidityColor=0xff00d8ff;
    public static final int DateTime=0;
    public static final int Date=1;
    public static final int Time=2;

    //public static Data get (){
    //    if (mdata==null){
    //        mdata= new Data();
    //    }
    //    return mdata;
    //}
    //private Data(){
    //
    //}

    public static void setLogin (Map<String,String> Login){
        if (Login.get(Data.user)!=null){
            Data.mdata.put(Data.user,Login.get(Data.user));
        }
        if (Login.get(Data.pass)!=null){
            Data.mdata.put(Data.pass,Login.get(Data.pass));
        }
    }

    public static String getLogin (boolean WhetherUserorPass){
        String Login;
        if (WhetherUserorPass){
            Login = Data.mdata.get(Data.user);
        }else {
            Login = Data.mdata.get(Data.pass);
        }
        if (Login==null){
            Log.d("Login","No Stored "+(WhetherUserorPass?"User":"Password"));
            return null;
        }else {
            return Login;
        }
    }

    public static void setHostAddress (Map<String,String> URLs){
        if (URLs.get(Data.LocalHost)!=null){
            Data.mdata.put(Data.LocalHost,URLs.get(Data.LocalHost).toLowerCase().contains("http://")?URLs.get(Data.LocalHost):"http://"+URLs.get(Data.LocalHost));
        }
        if (URLs.get(Data.InternetHost)!=null){
            Data.mdata.put(Data.InternetHost,URLs.get(Data.InternetHost).toLowerCase().contains("http://")?URLs.get(Data.InternetHost):"http://"+URLs.get(Data.InternetHost));
        }
    }

    public static String getHostAddress (boolean WhetherInternet){
        String HostAddress;
        if (WhetherInternet){
            HostAddress = mdata.get(Data.InternetHost);
        }else {
            HostAddress = mdata.get(Data.LocalHost);
        }
        if (HostAddress==null){
            Log.d("HostAddress","No Such Stored Host Address");
        }
        return HostAddress;
    }

    public static String getURL (int Type,boolean WhetherInternet){
        String Address;
        if (WhetherInternet){
            Address = mdata.get(Data.InternetHost);
        }else {
            Address = mdata.get(Data.LocalHost);
        }
        if (Address==null){
            Log.d("URL","No Such Stored URL");return null;
        }
        switch (Type){
            case Data.Inquire:
                return Address+Data.InquireURLfix;
            case Data.InquireData:
                return Address+Data.InquireDataURLfix;
            case Data.Control:
                return Address+Data.ControlURLfix;
            case Data.RegistrationPlusStateupdate:
                return Address+Data.RegistrationPlusStateupdateURLfix;
            case Data.debugconfig:
                return Address+Data.debugconfigURLfix;
            default:Log.d("URL","No Such Type URL");return null;
        }
    }

    private static void processData(int Type,int howmany){
        ArrayList<dataset> rawdatasetArray;
        switch (Type){
            case Type_Temperature:rawdatasetArray=rawTemperature;break;
            case Type_Humidity:rawdatasetArray=rawHumidity;break;
            //case Type_Electricity:temp=rawElectricity;break;
            default:Log.d("No Valid Data","NO");return;
        }
        if (!rawdatasetArray.isEmpty()) {
            ArrayList<int[]> datasetTimeInt = new ArrayList<>(rawdatasetArray.size());
            for (dataset datatemp : rawdatasetArray) {
                datasetTimeInt.add(formatData(datatemp.nameortime));
            }
            int maxYear = 0;
            int minYear = datasetTimeInt.get(0)[0];
            for (int[] array : datasetTimeInt) {
                maxYear = Math.max(maxYear, array[0]);
                minYear = Math.min(minYear, array[0]);
            }
            ArrayList<int[]> processedDataset = new ArrayList<>(rawdatasetArray.size());
            for (int[] array : datasetTimeInt) {
                int[] Dataset = new int[3];
                for (int index = 0; index < array[0] - minYear; index++) {
                    Dataset[1] += ((index + minYear) % 4 == 0) ? 366 : 365;
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
                ////////array[0]-=minYear;////////
            }
            for (int[] lll:processedDataset){
                //Log.d("Seconds"+Type,String.valueOf(lll[0]+"-"+lll[1]+"-"+lll[2]));
            }

            double max=0;double min=processedDataset.get(0)[0];
            for (int[] array : processedDataset) {
                max = Math.max(max, array[0]);
                min = Math.min(min, array[0]);
            }
            double range=max-min;
            double segment=range/(howmany-1);Log.d("Segment"+Type,String.valueOf(segment));
            double tempsegment=min;int Proximate=-1;int usedminIndex=0;int addingindex=0;
            Log.d("MaxSeconds"+Type,String.valueOf(max));Log.d("MinSeconds"+Type,String.valueOf(min));
            ArrayList<dataset> tempDatasetArray = new ArrayList<>();
            for (;tempsegment<=max+0.000001d;tempsegment+=segment){
                //Log.d("ProcessingData"+Type,String.valueOf(tempsegment));
                int MostProximate=0;double minvalue=Math.abs(processedDataset.get(0)[0]-tempsegment);
                for (int index=0;index<processedDataset.size();index++){
                    //Log.d(String.valueOf(Math.abs(processedDataset.get(index)[0]-tempsegment)),"gap"+Type+
                    //String.valueOf(minvalue)+String.valueOf(index));
                    if (Math.abs(processedDataset.get(index)[0]-tempsegment)<minvalue){
                        minvalue=Math.abs(processedDataset.get(index)[0]-tempsegment);
                        MostProximate=index;
                    }
                }

                dataset tempDataset;
                if (MostProximate==Proximate){

                    //int mingap= Math.abs(processedDataset.get(0)[0]-processedDataset.get(usedminIndex)[0]);
                    //上面这个不可以，因为0位置的数据可能是usedminIndex本身的数据或者是比它大的但是和它距离更近的那个数据，
                    //所以可能导致下面的判断语句不可用。

                    //int mingap= 2147483647;
                    int mingap=0;
                    int minIndex=-1;
                    boolean first=true;
                    for(int index=0;index<processedDataset.size();index++){
                        if (processedDataset.get(index)[0]<processedDataset.get(usedminIndex)[0]){
                            if (first){
                                mingap = Math.abs(processedDataset.get(index)[0]-processedDataset.get(usedminIndex)[0]);
                                minIndex=index;
                                first=false;
                            }else if (Math.abs(processedDataset.get(index)[0]-processedDataset.get(usedminIndex)[0])<mingap){
                                //Log.d("Anomally Set","Set");
                                mingap=Math.abs(processedDataset.get(index)[0]-processedDataset.get(usedminIndex)[0]);
                                minIndex=index;
                            }
                        }
                        //if (first&&processedDataset.get(index)[0]<processedDataset.get(usedminIndex)[0]){
                        //    mingap = Math.abs(processedDataset.get(0)[0]-processedDataset.get(usedminIndex)[0]);
                        //    minIndex=index;
                        //    first=false;
                        //}
                        //else if (processedDataset.get(index)[0]<processedDataset.get(usedminIndex)[0]
                        //        && Math.abs(processedDataset.get(index)[0]-processedDataset.get(usedminIndex)[0])<mingap){
                        //    mingap=Math.abs(processedDataset.get(index)[0]-processedDataset.get(usedminIndex)[0]);
                        //    minIndex=index;
                        //}

                    }
                    if (minIndex<0){minIndex=usedminIndex;}
                    try {
                        tempDataset = (dataset) rawdatasetArray.get(minIndex).clone();
                        tempDataset.setSeconds(processedDataset.get(minIndex)[0]);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                        return;
                    }
                    Log.d("AnomallyAddedData"+Type,tempDataset.nameortime);
                    tempDatasetArray.add(addingindex,tempDataset);

                    usedminIndex=minIndex;
                }
                else {
                    Log.d("MostProximate"+Type,String.valueOf(MostProximate));
                    try {
                        tempDataset = (dataset) rawdatasetArray.get(MostProximate).clone();
                        tempDataset.setSeconds(processedDataset.get(MostProximate)[0]);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                        return;
                    }
                    Log.d("NomallyAddedData"+Type,tempDataset.nameortime);
                    tempDatasetArray.add(tempDataset);addingindex=tempDatasetArray.size()-1;

                    usedminIndex=MostProximate;
                    Proximate=MostProximate;

                }
                if (segment==0){break;}

            }

            switch (Type) {
                case Type_Temperature:
                    Temperature.clear();
                    //Temperature.addAll(tempDatasetArray);
                    arrangeDataSetintoArray(tempDatasetArray,Temperature);
                    break;
                case Type_Humidity:
                    Humidity.clear();
                    arrangeDataSetintoArray(tempDatasetArray,Humidity);
                    break;
                //case Type_Electricity:Electricity.add(tempDataset);break;
                default:Log.d("No Valid Data", "NO");return;
            }
            ArrayList<dataset> tt;String name;
            switch (Type){
                case Type_Temperature:tt=Temperature;name="TemperatureName";break;
                case Type_Humidity:tt=Humidity;name="HumidityName";break;
                default:return;
            }
            for (dataset kkk:tt){
                Log.d(name,String.valueOf(kkk.nameortime));
            }



        }else{
            Log.i("NonData","So Quit");
            //throw new NullPointerException();
        }
    }

    public static void arrangeDataSetintoArray (ArrayList<dataset> DatasetArray,
                                                ArrayList<dataset> Target){
        for (dataset temp:DatasetArray){
            if (Target.size()==0||temp.getSeconds()>Target.get(Target.size()-1).getSeconds()){
                Target.add(temp);
            }else {
                for (int i=0;i<Target.size();i++){
                    if (temp.getSeconds()<=Target.get(i).getSeconds()){
                        Target.add(i,temp);
                        break;
                    }
                }
            }
        }
    }

    public static long[] convertDatetoDaysandSeconds(String Date){
        long[] Dataset = new long[3];
        int[] array=formatData(Date);
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
        Dataset[1] += array[2] - 1;
        Dataset[2] = array[3] * 3600;
        Dataset[2] += array[4] * 60;
        Dataset[2] += array[5];

        Dataset[0] = Dataset[1] * 24 * 3600 + Dataset[2];
        return Dataset;
    }

    public static int[] formatData(String data){
        //String d ="2016-10-18 22:28:23.0";
        //data=data.substring(0,data.length()-2);
        //Log.d("FormattedData",data+"/");
        if (data==null){
            return new int[]{0,0,0,-1,-1,-1};
        }
        String[] tempDate;String[] tempTime=null;int[] Time=new int[]{-1,-1,-1};int[] Date=new int[]{0,0,0};

        String[] tempString=data.split(" ");

        tempDate=tempString[0].split("-");
        //if (tempDate.length>1){
            for (int i=0;i<=Date.length-1;i++){
                try {
                    int index = i-(Date.length-tempDate.length);
                    if (index>=0){
                        Date[i]=Math.round(Float.parseFloat(tempDate[index]));
                        if (Date[i]<=0){Date[i]=1;}
                    }
                }catch (Exception e){

                }

            }
        //}
        if (tempString.length>1){
            tempTime=tempString[1].split(":");
        }
        else {
            tempTime=tempString[0].split(":");
        }
        //if (tempTime.length>1){
            for (int i=0;i<=Time.length-1;i++){
                try {
                    int index = i-(Time.length-tempTime.length);
                    if (index>=0){
                        Time[i]=Math.round(Float.parseFloat(tempTime[index]));
                        if (Time[i]<0){Time[i]=0;}
                    }
                }catch (Exception e){

                }
            }
        //}

        int[] Array=new int[Time.length+Date.length];
        for (int index=0;index<=Time.length+Date.length-1;index++){
            Array[index]=(index<=3-1)?Date[index]:Time[index-Date.length];
        }
        int sum=0;String debug="";
        for (int temp:Array){
            debug +=temp+"/";
            sum+=temp;
        }
        //Log.d("FormattedData",debug);
        //if (sum<=Array.length*-1){
        //    throw new NumberFormatException ("String doesn't fit any Format");
        //}
        return Array;
    }

    public static long convertDatetoSeconds(String Date,int Type,boolean whethertoprint){
        long[] Dataset = new long[3];
        int[] array=formatData(Date.trim());
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

    private static int[] formatData_old(String data){
        String d ="2016-10-18 22:28:23.0";
        //data=data.substring(0,data.length()-2);
        String[] tempString=data.split(" ");
        String[] tempDate=tempString[0].split("-");
        String[] tempTime=tempString[1].split(":");
        int Date[]=new int[tempDate.length];
        int Time[]=new int[tempTime.length];
        for (int index=0;index<=Date.length-1;index++){
            Date[index]=Math.round(Float.parseFloat(tempDate[index]));
            if (Date[index]<=0){Date[index]=1;}
        }
        for (int index=0;index<=Time.length-1;index++){
            Time[index]=Math.round(Float.parseFloat(tempTime[index]));
        }

        int[] Array=new int[Date.length+Time.length];
        for (int index=0;index<=Date.length+Time.length-1;index++){
            Array[index]=(index<=Date.length-1)?Date[index]:Time[index-Date.length];
        }
        return Array;
    }

    private static void clearRawData(int Type){
        switch (Type){
            case Type_Temperature:rawTemperature.clear();break;
            case Type_Humidity:rawHumidity.clear();break;
            case Type_Electricity:Electricity.clear();break;
            case Type_apparatuscfgset:apparatuscfgset.clear();break;

        }
    }

    private static <t> void addData (int Type,t dataset){
        switch (Type){
            case Type_Temperature:
                rawTemperature.add((dataset) dataset);break;
            case Type_Humidity:
                rawHumidity.add((dataset) dataset);break;
            case Type_Electricity:
                Electricity.add((dataset) dataset);break;
            case Type_apparatuscfgset:apparatuscfgset.add((apparatuscfg) dataset);break;
        }
    }

    public static  ArrayList<dataset> getDataset (int type){
        switch (type){
            case Type_Temperature:
                synchronized (Temperature) {
                    return Temperature;
                }
            case Type_Humidity:
                synchronized (Humidity) {
                    return Humidity;
                }
            case Type_Electricity:
                synchronized (Electricity) {
                    return Electricity;
                }
        }

    return null;}

    private static ArrayList<dataset> getrawDataset(int type){
        switch (type){
            case Type_Temperature:
                synchronized (rawTemperature) {
                    return rawTemperature;
                }
            case Type_Humidity:
                synchronized (rawHumidity) {
                    return rawHumidity;
                }
        }

        return null;
    }

    public static  ArrayList<apparatuscfg> getApparatuses (){
        synchronized (apparatuscfgset) {
            return apparatuscfgset;
        }
    }

    public static <o extends ArrayList> void setData(int Type,o data){
        switch (Type){
            case Type_Temperature:
                synchronized (Temperature) {
                    Temperature.clear();
                    Temperature.addAll(data);
                }
                break;
            case Type_Humidity:
                synchronized (Humidity){
                    Humidity.clear();
                    Humidity.addAll(data);
                }
                break;
            case Type_Electricity:
                synchronized (Electricity){
                    Electricity.clear();
                    Electricity.addAll(data);
                }
                break;
            case Type_apparatuscfgset:
                synchronized (apparatuscfgset){
                    apparatuscfgset.clear();
                    apparatuscfgset.addAll(data);
                }
                break;
        }
    }

    public static ArrayList<Integer> getColorset (int Type){
        switch (Type){
            case Type_Temperature:
                synchronized (TemperatureColorSet) {
                    return TemperatureColorSet;
                }
            case Type_Humidity:
                synchronized (HumidityColorSet) {
                    return HumidityColorSet;
                }
        }
        return null;
    }

    private static void addColorsetData(int Type,int color){
        switch (Type){
            case Type_Temperature:
                synchronized (TemperatureColorSet) {
                    TemperatureColorSet.add(color);
                }
                break;
            case Type_Humidity:
                synchronized (HumidityColorSet) {
                    HumidityColorSet.add(color);
                }
                break;
        }
    }

    private static void clearColorset(int Type){
        switch (Type){
            case Type_Temperature:
                synchronized (TemperatureColorSet) {
                    TemperatureColorSet.clear();
                }
                break;
            case Type_Humidity:
                synchronized (HumidityColorSet) {
                    HumidityColorSet.clear();
                }
                break;
        }
    }

    public static void setColorset(int Type,ArrayList<Integer> colorsetarray,int[] colorset){
        if (colorset!=null||colorsetarray!=null) {
            switch (Type) {
                case Type_Temperature:
                    synchronized (TemperatureColorSet) {
                        TemperatureColorSet.clear();
                        if (colorsetarray!=null) {
                            if (colorsetarray.isEmpty()){
                                TemperatureColorSet.add(frameColor);
                                break;
                            }
                            TemperatureColorSet.addAll(colorsetarray);
                        }else {
                            for (int temp:colorset){
                                TemperatureColorSet.add(temp);
                            }
                        }
                    }
                    break;
                case Type_Humidity:
                    synchronized (HumidityColorSet) {
                        HumidityColorSet.clear();
                        if (colorsetarray!=null) {
                            if (colorsetarray.isEmpty()){
                                HumidityColorSet.add(frameColor);
                                break;
                            }
                            HumidityColorSet.addAll(colorsetarray);
                        }else {
                            for (int temp:colorset){
                                HumidityColorSet.add(temp);
                            }
                        }
                    }
                    break;
            }
        }
    }

    public static int getDatasetStartscale (int Type){
        switch (Type){
            case Type_Temperature:return Temperaturestartscale;
            case Type_Humidity:return Humiditystartscale;
        }
    return 0;}

    public static void setStartscale(int Type,int startscale){
        switch (Type){
            case Type_Temperature:Data.Temperaturestartscale=startscale;break;
            case Type_Humidity:Data.Humiditystartscale=startscale;break;
        }
    }

    private static int[] processColorSet (String rawColorSet){
        String [] colorset_string;int [] colorset;
        int [] framecolorset=new int[]{frameColor};//framecolorset[0]=frameColor;
        if (rawColorSet==null||rawColorSet.isEmpty()){return framecolorset;}
        colorset_string = rawColorSet.split(",");
        colorset=new int[colorset_string.length];
        for (int i=0;i<colorset.length;i++){
            if (colorset_string[i].length()==10){
                try {
                    colorset[i] = validateStringFormatOfHex(colorset_string[i]);
                }catch (Exception e){
                    //e.printStackTrace();
                    Log.d("NumberFormatException","Not A Valid Color String");
                    colorset[i]=frameColor;
                }
            }else {
                Log.d("NumberFormatException","Length is not Right");
                colorset[i]=frameColor;
            }

        }
        return colorset;
    }

    private static int validateStringFormatOfHex(String number) throws NumberFormatException{
        //String number="0xffff1200";
        long sum=0;
        //if (number.length()<10){
        //    throw new NumberFormatException("Length is not Enough");
        //}
        number=number.substring(2,number.length());
        number=number.toUpperCase();
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
        }

        String realIntString=String.valueOf(Long.toBinaryString(sum));
        String sign;
        if (realIntString.charAt(0)=='1'){
            if (realIntString.length()==32){
                sign="-";
            }else {
                sign="";
            }
        }else {
            sign="";
        }
        if (sign.equals("")){
            return (int)sum;
        }
        realIntString=String.valueOf(Long.toBinaryString(sum-1));
        String fix="0";String realfix="";
        if (realIntString.length()<32){
            for (int ii=1;ii<=(32-realIntString.length());ii++){
                realfix+=fix;
            }
            realIntString=realfix+realIntString;
        }
        Log.d("finalIntStringRealfix",realfix);
        String inverseIntString="";
        for (int i =0;i<realIntString.length();i++){
            if (realIntString.charAt(i)=='1'){
                inverseIntString+="0";
            }else {
                inverseIntString+="1";
            }
        }
        String finalIntString=sign+inverseIntString;
        Log.d("finalIntString",finalIntString);
        int Int=Integer.parseInt(finalIntString,2);
        //System.out.println(Int);
        return Int;
    }

    public static int validateStringFormatforHex2 (String number)throws NumberFormatException{
        long sum=0;
        //if (number.length()!=10){
        //    throw new NumberFormatException("Length is not Right");
        //}
        number=number.substring(2,number.length());
        number=number.toUpperCase();
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

        }

        if(sum>4294967295L){
            return 2147483647;}
        // else if (sum<=2147483647){
        //    return (int)sum;
        //}
        //long positivizedmaximumofint = 4294967295L;
        //int Int = (int)((positivizedmaximumofint+1-sum)*(-1));

        return (int)sum;
    }

    public interface whattodonext{
        void todo();
    }

    private static void processtogetTemperatureData(JSONObject value, int howmany){
        int length;
        JSONArray nameortime;
        JSONArray data;
        int Period;
        String URL;
        try {
            nameortime = value.optJSONArray("Temperature-Time");
            if (nameortime==null){nameortime=new JSONArray().put(value.getString("Temperature-Time"));}
            length = nameortime.length();
            data = value.optJSONArray("Temperature-Degree");
            if (data==null){data=new JSONArray().put(value.getInt("Temperature-Degree"));}
            Period = value.optInt("Period");
            URL = value.optString("URL");
            Log.d("GetAllJsonArray", "YES");
            //if (length>50){
            //    length=50;
            //}
            synchronized (Temperature) {
                Data.clearRawData(Data.Type_Temperature);

                for (int i = 0; i < length; i++) {
                    Data.addData(Data.Type_Temperature, new Data.dataset(null,data.getInt(i), nameortime.getString(i), TemperatureColor,Period,URL,Data.Type_Temperature));
                }
                Data.processData(Data.Type_Temperature, howmany);

                File file=new File(MainActivity.fileDir,"Temperatureset");
                File fileColorset=null;
                try{
                    String rawColorSet = value.getString("Temperature-ColorSet");
                    setColorset(Type_Temperature,null,processColorSet(rawColorSet));
                    fileColorset=new File(MainActivity.fileDir,"TemperatureColorSet");
                }catch (Exception e){
                    Log.d("NoColorSet","Temperature");
                }

                try {
                    FileOutputStream out=new FileOutputStream(file);
                    ObjectOutputStream objout = new ObjectOutputStream(out);
                    objout.writeObject(Data.getDataset(Type_Temperature));
                    objout.flush();
                    objout.close();
                    out.close();
                    if(fileColorset!=null) {
                        out = new FileOutputStream(fileColorset);
                        objout = new ObjectOutputStream(out);
                        objout.writeObject(Data.getColorset(Type_Temperature));
                        objout.flush();
                        objout.close();
                        out.close();
                        Status_Temperature=RefreshDone;
                    }else {
                        Status_Temperature=RefreshFailed;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Status_Temperature=RefreshFailed;
        }

    }

    private static void processtogetHumidityData(JSONObject value, int howmany){
        int length;
        JSONArray nameortime;
        JSONArray data;
        int Period;
        String URL;
        try {
            nameortime = value.optJSONArray("Humidity-Time");
            if (nameortime==null){nameortime=new JSONArray().put(value.getString("Humidity-Time"));}
            length = nameortime.length();
            data = value.optJSONArray("Humidity-Degree");
            if (data==null){data=new JSONArray().put(value.getInt("Humidity-Degree"));}
            Period = value.optInt("Period");
            URL = value.optString("URL");
            Log.d("GetAllJsonArray", "YES");
            //if (length>50){
            //    length=50;
            //}
            synchronized (Humidity) {
                Data.clearRawData(Data.Type_Humidity);
                for (int i = 0; i < length; i++) {
                    Data.addData(Data.Type_Humidity, new Data.dataset(null,data.getInt(i), nameortime.getString(i), HumidityColor,Period,URL,Data.Type_Humidity));
                }
                Data.processData(Data.Type_Humidity, howmany);

                File file=new File(MainActivity.fileDir,"Humidityset");
                File fileColorset=null;
                try{
                    String rawColorSet = value.getString("Humidity-ColorSet");
                    setColorset(Type_Humidity,null,processColorSet(rawColorSet));
                    fileColorset=new File(MainActivity.fileDir,"HumidityColorSet");
                }catch (Exception e){
                    Log.d("NoColorSet","Humidity");
                }

                try {
                    FileOutputStream out=new FileOutputStream(file);
                    ObjectOutputStream objout = new ObjectOutputStream(out);
                    objout.writeObject(Data.getDataset(Type_Humidity));
                    objout.flush();
                    objout.close();
                    out.close();
                    if(fileColorset!=null) {
                        out = new FileOutputStream(fileColorset);
                        objout = new ObjectOutputStream(out);
                        objout.writeObject(Data.getColorset(Type_Humidity));
                        objout.flush();
                        objout.close();
                        out.close();
                        Status_Humidity=RefreshDone;
                    }else {
                        Status_Humidity=RefreshFailed;
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
            Status_Humidity=RefreshFailed;
        }
    }

    private static void processtogetElectricityData(JSONObject value){
        int length;
        JSONArray nameortime;
        JSONArray data;
        JSONArray Color;
        int Period;
        JSONArray URL;
        JSONArray id;
        try {
            nameortime = value.optJSONArray("appliance-name");
            if (nameortime==null){nameortime=new JSONArray().put(value.getString("appliance-name"));}
            length = nameortime.length();
            data = value.optJSONArray("KWH");
            if (data==null){data=new JSONArray().put(value.getInt("KWH"));}
            Color = value.optJSONArray("Color");
            if (Color==null){Color=new JSONArray().put(value.getString("Color"));}
            Period = value.optInt("Period");
            URL = value.optJSONArray("URL");
            if (URL==null){URL=new JSONArray().put(value.getString("URL"));}
            id = value.optJSONArray("id");
            if (id==null){id=new JSONArray().put(value.getInt("id"));}
            Log.d("GetAllJsonArray", "YES");
            synchronized (Electricity) {
                Data.clearRawData(Data.Type_Electricity);
                for (int i = 0; i < length; i++) {
                    if (Color.getString(i).length()==10) {
                        try {
                            Data.addData(Data.Type_Electricity, new Data.dataset(id.getInt(i),data.getInt(i), nameortime.getString(i), validateStringFormatOfHex(Color.getString(i)),Period,URL.getString(i),Data.Type_Electricity));
                        } catch (Exception e) {
                            Log.d("ElectricityColor", "FormatWrong");
                            Data.addData(Data.Type_Electricity, new Data.dataset(id.getInt(i),data.getInt(i), nameortime.getString(i), frameColor2,Period,URL.getString(i),Data.Type_Electricity));
                        }
                    }else {
                        Log.d("ElectricityColor","LengthWrong");
                        Data.addData(Data.Type_Electricity, new Data.dataset(id.getInt(i),data.getInt(i), nameortime.getString(i), frameColor2,Period,URL.getString(i),Data.Type_Electricity));
                    }

                }
                File file=new File(MainActivity.fileDir,"Electricityset");
                try {
                    FileOutputStream out=new FileOutputStream(file);
                    ObjectOutputStream objout = new ObjectOutputStream(out);
                    objout.writeObject(Data.getDataset(Type_Electricity));
                    objout.flush();
                    objout.close();
                    out.close();
                    Status_Electricity=RefreshDone;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Status_Electricity=RefreshFailed;
        }
    }

    private static void processtogetApparatuscfgData(JSONObject value,whattodonext todowhat){
        int length;
        JSONArray nameortime;
        JSONArray activated;
        JSONArray URL;
        JSONArray id;
        JSONArray Alerted;
        int AlertedLogNumber;
        JSONArray RegulationSet;
        regulation[] Regulations;
        JSONArray Passive;
        JSONArray Persistent;
        JSONArray Alertable;
        MainActivity.whetherTooMany=false;
        try {
            nameortime = value.optJSONArray("apparatus-name");
            if (nameortime==null){nameortime=new JSONArray().put(value.getString("apparatus-name"));}
            length = nameortime.length();

            id = value.optJSONArray("id");
            if (id==null){id=new JSONArray().put(value.getInt("id"));}
            URL=value.optJSONArray("URL");
            if (URL==null){URL=new JSONArray().put(value.getString("URL"));}
            activated=value.optJSONArray("activated");
            if (activated==null){activated=new JSONArray().put(value.getBoolean("activated"));}
            Alerted=value.optJSONArray("Alerted");
            if (Alerted==null){Alerted=new JSONArray().put(value.getBoolean("Alerted"));}
            AlertedLogNumber=value.getInt("AlertedLogNumber");
            RegulationSet = value.optJSONArray("Regulation");
            if (RegulationSet==null){RegulationSet = new JSONArray().put(value.getString("Regulation").trim());}
            Passive=value.optJSONArray("Passive");
            if (Passive==null){Passive=new JSONArray().put(value.getBoolean("Passive"));}
            Persistent=value.optJSONArray("Persistent");
            if (Persistent==null){Persistent=new JSONArray().put(value.getBoolean("Persistent"));}
            Alertable=value.optJSONArray("Alertable");
            if (Alertable==null){Alertable=new JSONArray().put(value.getBoolean("Alertable"));}
            Log.d("GetAllJsonArray", "YES");
            if (length>9){
                length=9;
                MainActivity.whetherTooMany=true;
            }
            synchronized (apparatuscfgset) {
                Data.clearRawData(Data.Type_apparatuscfgset);
                for (int i = 0; i < length; i++) {
                    Regulations=processRegulationSet(RegulationSet.getString(i));
                    Data.addData(Data.Type_apparatuscfgset, new Data.apparatuscfg(id.getInt(i), nameortime.getString(i), URL.getString(i), activated.getBoolean(i),Alerted.getBoolean(i),AlertedLogNumber,Regulations,
                            Passive.getBoolean(i),Persistent.getBoolean(i),Alertable.getBoolean(i)));
                }
                File file=new File(MainActivity.fileDir,"Apparatusset");
                try {
                    FileOutputStream out=new FileOutputStream(file);
                    ObjectOutputStream objout = new ObjectOutputStream(out);
                    objout.writeObject(Data.getApparatuses());
                    objout.flush();
                    objout.close();
                    out.close();
                    Status_apparatus=RefreshDone;
                    if (todowhat!=null) {
                        todowhat.todo();
                    }
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
            Status_apparatus=RefreshFailed;
        }
    }

    private static regulation[] processRegulationSet(String RegulationSetString){
        if (RegulationSetString!=null&&!RegulationSetString.trim().isEmpty()){
            try {
                Object RegulationSet = new JSONTokener(RegulationSetString).nextValue();
                if (RegulationSet instanceof  JSONArray){
                    JSONArray realRegulationSets = (JSONArray) RegulationSet;
                    regulation[] realRegulations = new regulation[realRegulationSets.length()];
                    for (int i=0;i<realRegulations.length;i++){
                        try{
                            JSONObject realRegulationSet = realRegulationSets.getJSONObject(i);
                            realRegulations[i] = new regulation(realRegulationSet.optString("item"),
                                    realRegulationSet.optString("rule"),realRegulationSet.optBoolean("enabledOfruleset"));
                        }catch (JSONException e){

                        }
                    }
                    return realRegulations;
                }else if (RegulationSet instanceof  JSONObject){
                    JSONObject realRegulationSet = (JSONObject) RegulationSet;
                    regulation[] realRegulations = new regulation[1];
                    realRegulations[0] = new regulation(realRegulationSet.optString("item"),
                            realRegulationSet.optString("rule"),realRegulationSet.optBoolean("enabledOfruleset"));
                    return realRegulations;
                }else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }else {
            return  null;
        }
    }


    public static class OnsuccessProcess implements OKHttpTool.processJsonObject{
        int howmany;Data.whattodonext todowhat;

        OnsuccessProcess(int howmany,Data.whattodonext todowhat){
            this.howmany=howmany;this.todowhat=todowhat;
        }
        public void onResponse(JSONObject value) {
            JSONArray Keynames = value.names();
            //Log.d("Connection Status",value.toString());
            boolean set=false;
            for (int i=0;Keynames!=null&&i<Keynames.length();i++){
                switch (Keynames.optString(i)) {
                    case "Temperature-Time":
                        processtogetTemperatureData(value,howmany);
                        set=true;
                        break;
                    case "Humidity-Time":
                        processtogetHumidityData(value,howmany);
                        set=true;
                        break;
                    case "appliance-name":
                        processtogetElectricityData(value);
                        set=true;
                        break;
                    case "apparatus-name":
                        processtogetApparatuscfgData(value,todowhat);
                        set=true;
                        break;
                    //default:
                }
                if (set){
                    break;
                }
            }
            if (!set) {
                Log.d("MysqlConfigurationError", "MysqlNameConfigurationError");
            }
            //String Keyname;
            //try {Keyname=Keynames.getString(0);} catch (JSONException e) {e.printStackTrace();return;}
            //switch (Keyname) {
            //    case "Temperature-Time":
            //        processtogetTemperatureData(value,howmany);
            //        break;
            //    case "Humidity-Time":
            //        processtogetHumidityData(value,howmany);
            //        break;
            //    case "nameortime":
            //        processtogetElectricityData(value);
            //        break;
            //    case "id":
            //        processtogetApparatuscfgData(value,todowhat);
            //        break;
            //    default:Log.d("MysqlConfigurationError","MysqlNameConfigurationError");
            //
            //}
        }
    }

    public static class OnFailed implements OKHttpTool.processFailure{
        int Type;int trytimes=1;Map<String,String> parameters=new HashMap<>(3);String fixURL;int howmany;
        whattodonext todowhat;
        OnFailed(int type,int howmany,whattodonext todowhat){
            this.Type=type;this.howmany=howmany;this.todowhat=todowhat;
            parameters.put("user","henry");parameters.put("pass","yiweigang");
            switch (Type){
                case Data.Type_Temperature:parameters.put("Type","Temperature");fixURL="Data";break;
                case Data.Type_Humidity:parameters.put("Type","Humidity");fixURL="Data";break;
                case Data.Type_Electricity:parameters.put("Type","appliance");fixURL="Data";break;
                case Data.Type_apparatuscfgset:parameters.put("Type","");fixURL="";break;
                default:Log.d("inValid RequestType","No so Quit");throw new NullPointerException();
            }
        }
        public void onFailure() {
            Log.d("hasFailure","ConnectionFailed"+"Type: "+Type);
            if (Type!=Type_Electricity&&Type!=Type_Humidity&&Type!=Type_Temperature&&Type!=Type_apparatuscfgset){
                Log.d("Illegal Data","Quit");return;
            }
            if (trytimes<=5&&!MainActivity.StopGettingData){

                OKHttpTool.asyncCustomPostFormforJsonObject("http://168.150.116.167:8080/Smart_Home/Inquire"+fixURL, parameters, new OnsuccessProcess(howmany,todowhat),this);
                trytimes++;
            }else {
                switch (Type){
                    case Type_Temperature:Status_Temperature=RefreshFailed;break;
                    case Type_Humidity:Status_Humidity=RefreshFailed;break;
                    case Type_Electricity:Status_Electricity=RefreshFailed;break;
                    case Type_apparatuscfgset:Status_apparatus=RefreshFailed;break;
                }
                trytimes=1;
            }
        }
    }



    public static class dataset implements Serializable,Cloneable {
        public Integer id;float data;public String nameortime;public int Color;public int Period;public String URL;
        public int Type; private long seconds;
        dataset (Integer id, float data, String nameortime, int Color, int Period, String URL,int Type){
            this.id=id;this.Type=Type;
            this.data=data;this.nameortime = nameortime;this.Color=Color;
            this.Period=Period;this.URL=URL;
        }

        public void setSeconds(long seconds) {
            this.seconds = seconds;
        }

        public long getSeconds() {
            return seconds;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    public static class apparatuscfg implements Serializable{
        public String name;public String URL;public int id;public boolean isactivated;public boolean Alerted;int AlertedLogNumber;
        public regulation[] Regulations;public boolean Passive;public boolean Persistent;public boolean Alertable;
        apparatuscfg(int id,String name,String URL,boolean isactivated,boolean Alerted,int AlertedLogNumber,regulation[] Regulations,
                     boolean Passive,boolean Persistent,boolean Alertable){
            this.URL =URL;this.isactivated=isactivated;
            this.id=id;this.name=name;this.Alerted=Alerted;this.AlertedLogNumber=AlertedLogNumber;
            this.Regulations=Regulations;this.Passive=Passive;this.Persistent=Persistent;this.Alertable=Alertable;
        }

        public void setAlerted(boolean alerted) {
            Alerted = alerted;
        }

        public void setIsactivated(boolean isactivated) {
            this.isactivated = isactivated;
        }
    }

    public static class regulation implements Serializable{
        public String Item;public String[] StartDate=new String[0];public String[] StartTime=new String[0];
        public String[] EndDate=new String[0];public String[] EndTime=new String[0];
        public boolean[] Enabled=new boolean[0];public boolean EnabledOfRuleSet;
        private String rawRule;private boolean processed=false;
        public regulation (String Item, String rawRule, boolean EnabledOfRuleSet){
            this.Item=Item;this.rawRule = rawRule;this.EnabledOfRuleSet = EnabledOfRuleSet;
        }

        public regulation process(){
            if (!this.processed) {
                this.processed = processRegulation(this);
            }
            return this;
        }

        public boolean isProcessed() {
            return processed;
        }

        public void setRawRule(String rawRule) {
            this.rawRule = rawRule;
            processed=false;
        }
    }

    private static boolean processRegulation(regulation regulation){
        try {
            JSONObject json = new JSONObject(regulation.rawRule);
            JSONArray StartDate=json.optJSONArray("StartDate");if (StartDate==null&&json.has("StartDate")){
                StartDate=new JSONArray().put(json.getString("StartDate"));}
            JSONArray EndDate=json.optJSONArray("EndDate");if (EndDate==null&&json.has("EndDate")){
                EndDate=new JSONArray().put(json.getString("EndDate"));}
            JSONArray StartTime=json.optJSONArray("StartTime");if (StartTime==null&&json.has("StartTime")){
                StartTime=new JSONArray().put(json.getString("StartTime"));}
            JSONArray EndTime=json.optJSONArray("EndTime");if (EndTime==null&&json.has("EndTime")){
                EndTime=new JSONArray().put(json.getString("EndTime"));}
            JSONArray Enabled=json.optJSONArray("Enabled");if (Enabled==null&&json.has("Enabled")){
                Enabled=new JSONArray().put(json.getString("Enabled"));}
            String[] realStartDate=null;String[] realEndDate=null;
            String[] realStartTime=null;String[] realEndTime=null;
            boolean[] realEnabled=null;
            for (int i=0;i<(StartDate!=null?StartDate.length():0);i++){
                if (realStartDate==null){realStartDate=new String[StartDate.length()];}
                realStartDate[i]=StartDate.getString(i);
            }
            for (int i=0;i<(EndDate!=null?EndDate.length():0);i++){
                if (realEndDate==null){realEndDate=new String[EndDate.length()];}
                realEndDate[i]=EndDate.getString(i);
            }
            for (int i=0;i<(StartTime!=null?StartTime.length():0);i++){
                if (realStartTime==null){realStartTime=new String[StartTime.length()];}
                realStartTime[i]=StartTime.getString(i);
            }
            for (int i=0;i<(EndTime!=null?EndTime.length():0);i++){
                if (realEndTime==null){realEndTime=new String[EndTime.length()];}
                realEndTime[i]=EndTime.getString(i);
            }
            for (int i=0;i<(Enabled!=null?Enabled.length():0);i++){
                if (realEnabled==null){realEnabled=new boolean[Enabled.length()];}
                realEnabled[i]=Enabled.optBoolean(i);
            }
            regulation.StartDate=realStartDate!=null?realStartDate:new String[0];
            regulation.EndDate=realEndDate!=null?realEndDate:new String[0];
            regulation.StartTime=realStartTime!=null?realStartTime:new String[0];
            regulation.EndTime=realEndTime!=null?realEndTime:new String[0];
            regulation.Enabled=realEnabled!=null?realEnabled:new boolean[0];
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static class configurations implements Serializable{
        public int id = -1;public int Type;public boolean WhetherRegulation;public  Object value;



        configurations (int id,String name,String URL,boolean Alertable,boolean Passive,boolean Persistent){
            this.Type=Data.Type_apparatuscfgset;this.WhetherRegulation=false;
            this.value=new apparatusConfigurations(id, name, URL, Alertable, Passive, Persistent);
        }

        configurations (int id,String Item,JSONObject Rule){
            this.Type=Data.Type_apparatuscfgset;this.WhetherRegulation=true;
            this.value=new apparatusRegulations(id, Item, Rule);
        }

        configurations(int Period,String name,int Color,String URL){
            this.Type=Data.Type_Electricity;this.WhetherRegulation=false;
            this.value=new ElectricityConfigurations(Period, name, Color, URL);
        }

        configurations(int Type,int Period,String URL){
            switch (Type){
                case Data.Type_Temperature:
                    this.Type=Data.Type_Temperature;
                    break;
                case Data.Type_Humidity:
                    this.Type=Data.Type_Humidity;
                    break;
                default:
                    Log.d("Configurations","No Such Type of Data Configurations");
                    return;
            }
            this.WhetherRegulation=false;
            this.value = new AtmosphericDataConfigurations(Period, URL);
        }

        private class apparatusConfigurations {
            public int id;public String name;public String URL;public boolean Alertable;public boolean Passive;public boolean Persistent;
            apparatusConfigurations (int id,String name,String URL,boolean Alertable,boolean Passive,boolean Persistent){
                this.id=id;this.name=name;this.URL=URL;this.Alertable=Alertable;this.Passive=Passive;this.Persistent=Persistent;
            }
        }
        private class apparatusRegulations {
            public int id;public String Item;public JSONObject Rule;
            apparatusRegulations(int id,String Item,JSONObject Rule){
                this.id=id;this.Item=Item;this.Rule=Rule;
            }
        }
        private class ElectricityConfigurations{
            public int Period;public String name;public String URL;public int Color;
            ElectricityConfigurations (int Period,String name,int Color,String URL){
                this.Period=Period;this.name=name;this.Color=Color;this.URL=URL;
            }
        }

        private class AtmosphericDataConfigurations{
            public int Period;public String URL;
            AtmosphericDataConfigurations (int Period,String URL){
                this.Period=Period;this.URL=URL;
            }
        }
    }

    public static class MyViewsBasedExpandableListAdapter extends BaseExpandableListAdapter {
        private   LayoutInflater inflater;
        private   Context context;
        private   ExpandableListView expandablelistview;
        private   List <Pair<Pair<Integer[],Integer>,List<Pair<String,Integer>>>> rawViewSet;
        final List <Pair<View,List<View>>> realViewSet = new ArrayList<>();

        MyViewsBasedExpandableListAdapter(Context context, List<Pair<Pair<Integer[], Integer>, List<Pair<String, Integer>>>> rawViewSet, ExpandableListView View){
            configure(context, rawViewSet, View);
        }

        MyViewsBasedExpandableListAdapter configure(Context context, List<Pair<Pair<Integer[], Integer>, List<Pair<String, Integer>>>> rawViewSet, ExpandableListView View){
            this.rawViewSet = rawViewSet;
            this.context=context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.expandablelistview=View;
            return this;
        }

        ExpandableListView getExpandablelistview() {
            return expandablelistview;
        }

        public List <Pair<Pair<Integer[],Integer>,List<Pair<String,Integer>>>> getRawViewSet() {
            return rawViewSet;
        }

        public LayoutInflater getInflater() {
            return inflater;
        }

        @Override
        public int getGroupCount() {
            return rawViewSet.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return rawViewSet.get(groupPosition).second.size();
        }

        @Override
        public View getGroup(int groupPosition) {
            synchronized (realViewSet) {
                return realViewSet.get(groupPosition).first;
            }
        }

        @Override@Deprecated
        public View getChild(int groupPosition, int childPosition) {
            synchronized (realViewSet) {
                return realViewSet.get(groupPosition).second.get(childPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View v;
            if (convertView == null) {
                v = newGroupView(groupPosition, parent);
            } else {
                v = convertView;
            }

            v=setCorrectIndicator(v,groupPosition,isExpanded);
            synchronized (realViewSet) {
                if (groupPosition >= realViewSet.size()) {
                    realViewSet.add(groupPosition, new Pair<View, List<View>>(v, null));
                } else {
                    realViewSet.set(groupPosition, new Pair<View, List<View>>(v, null));
                }
            }
            //Log.d("RegulationActivated",v.isActivated()+"");
            return v;
        }

        View setCorrectIndicator (View v,int groupPosition,boolean isExpanded){
            Integer [] rawIndicator = rawViewSet.get(groupPosition).first.first;
            View temp = null;
            if (rawIndicator!=null&&rawIndicator.length>0) {
                temp=v.findViewById(rawIndicator[0]);
            }
            if (temp!=null){
                if (isExpanded){
                    //Log.d("Regulation","HasExpanded");
                    temp.setVisibility(View.VISIBLE);
                    if (rawIndicator.length>1) {
                        if (temp instanceof ImageView) {
                            ((ImageView) temp).setImageDrawable(context.getDrawable(rawIndicator[1]));
                        } else {
                            temp.setBackground(context.getDrawable(rawIndicator[1]));
                        }
                    }
                }else {
                    if (rawIndicator.length>2){
                        if (temp instanceof ImageView){
                            ((ImageView)temp).setImageDrawable(context.getDrawable(rawIndicator[2]));
                        }else{
                            temp.setBackground(context.getDrawable(rawIndicator[2]));
                        }
                    }else {
                        temp.setVisibility(View.INVISIBLE);
                    }
                }
            }
            return v;
        }

        View newGroupView(int groupPosition, ViewGroup parent) {
            View view=inflater.inflate(rawViewSet.get(groupPosition).first.second,parent,false);

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View v;
            if (convertView == null) {
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


        public View newChildView(int groupPosition, int childPosition, ViewGroup parent) {
            return inflater.inflate(rawViewSet.get(groupPosition).second.get(childPosition).second, parent, false);
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            getGroup(groupPosition).setActivated(true);
            setListViewHeight(expandablelistview);
            super.onGroupExpanded(groupPosition);
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            getGroup(groupPosition).setActivated(false);
            setListViewHeight(expandablelistview);
            super.onGroupCollapsed(groupPosition);
        }


        public void setListViewHeight(ExpandableListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            int totalHeight = 0;
            int count = listAdapter.getCount();
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight
                    + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            //Log.d("ExpandableListViewHeigh",params.height+"");
            listView.requestLayout();
        }

        public void setListViewHeight(final ExpandableListView listView, final android.os.Handler handler){
            final ListAdapter listAdapter = listView.getAdapter();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int count = listAdapter.getCount();
                    //Log.d("Has Set ListViewHeight","True");
                    if (count<=0){
                        handler.postDelayed(this,100);
                        return;
                    }
                    int totalHeight = 0;
                    for (int i = 0; i < count; i++) {
                        View listItem = listAdapter.getView(i, null, listView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }

                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    params.height = totalHeight
                            + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
                    listView.setLayoutParams(params);
                    listView.requestLayout();
                }
            },100);
        }
    }

}
