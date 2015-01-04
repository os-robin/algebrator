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

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Algebrator getAlgebrator(){
        return instance;
    }
}
