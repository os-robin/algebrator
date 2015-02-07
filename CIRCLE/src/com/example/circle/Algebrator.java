package com.example.circle;

import android.app.Application;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

/**
 * Created by Colin on 12/30/2014.
 */
public class Algebrator extends Application {
    public static final int DEFAULT_TEXT_SIZE = 40;
    private static Algebrator instance;
    private int TEXT_SIZE= DEFAULT_TEXT_SIZE;
    public int DEFAULT_SIZE;
    public long doubleTapSpacing = 1000;
    public int mainColor;
    public int highLight;
    public TextPaint textPaint = new TextPaint();
    public Paint bkgPaint = new Paint();
    public EmilyView writeView  = null;
    public ColinView solveView  = null;

    public int getTextSize(){
        return  this.TEXT_SIZE;
    }

    public void setTextSize(int newTextSize){
        this.TEXT_SIZE = newTextSize;
        bkgPaint.setTextSize(Algebrator.getAlgebrator().TEXT_SIZE);
        textPaint.setTextSize(Algebrator.getAlgebrator().TEXT_SIZE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainColor = 0xff000000 + (int) (Math.random() * 0xffffff);
        highLight = 0xff000000 + (int) (Math.random() * 0xffffff);
        instance = this;

        bkgPaint.setTextAlign(Paint.Align.CENTER);
        bkgPaint.setAntiAlias(true);
        bkgPaint.setColor(Algebrator.getAlgebrator().mainColor);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        //Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/DejaVuSans.ttf");
        //Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/DejaVuSans-ExtraLight.ttf");
        textPaint.setTypeface(myTypeface);
        textPaint.setColor(0xff000000);
    }

    public static Algebrator getAlgebrator(){
        return instance;
    }
}
