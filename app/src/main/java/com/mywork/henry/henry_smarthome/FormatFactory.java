package com.mywork.henry.henry_smarthome;

/**
 * Created by Henry on 2016/07/22.
 */

public class FormatFactory {

    public static int getColorWithoutAlpha (int Color){
        long tempColor;
        if (Color<0){
            tempColor= 2147483649L+Color+Integer.MAX_VALUE;
            while (tempColor>=0x100_0000){
                tempColor-=0x100_0000;
            }
            return (int)tempColor;
        }else {
            while (Color>=0x100_0000){
                Color-=0x100_0000;
            }
            return Color;
        }
    }
}
