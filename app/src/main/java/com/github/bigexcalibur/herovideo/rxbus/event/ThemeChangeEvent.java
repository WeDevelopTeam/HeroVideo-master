package com.github.bigexcalibur.herovideo.rxbus.event;

import android.view.View;

/**
 * Created by Xie.Zhou on 2017/1/7.
 */

public class ThemeChangeEvent {
    /**
     *  主题变化的事件
     */
    public static final int INIT_CHANGE = 1;
    public static final int GLOBLE_CHANGE = 2;
    public static final int SPECIFIC_CHANGE =3;


    public int eventType;
    public View view;

    public ThemeChangeEvent(int eventType){
        this.eventType =eventType;
    }

    public ThemeChangeEvent(int eventType ,View view){
        this.eventType = eventType;
        this.view = view;
    }

}
