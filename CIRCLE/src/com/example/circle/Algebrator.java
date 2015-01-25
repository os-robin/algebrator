package com.example.circle;

import android.app.Application;

/**
 * Created by Colin on 12/30/2014.
 */
public class Algebrator extends Application {
    private static Algebrator instance;
    public int TEXT_SIZE;
    public SuperView superView = null;
    public int DEFAULT_SIZE;
    public long doubleTapSpacing = 1000;
    public int mainColor;
    public int highLight;

    @Override
    public void onCreate() {
        super.onCreate();
        mainColor = 0xff000000 + (int) (Math.random() * 0xffffff);
        highLight = 0xff000000 + (int) (Math.random() * 0xffffff);
        instance = this;
    }

    public static Algebrator getAlgebrator(){
        return instance;
    }
}
