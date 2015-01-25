package com.example.circle;

import android.view.MotionEvent;

/**
 * Created by Colin on 1/21/2015.
 */
public class LongTouch {
    // TODO scale by dpi
    private float distance=20;

    private float STARTED=100;
    private float DONE=700;

    private final float x;
    private final float y;
    private final long time;

    public LongTouch(MotionEvent event){
        this.x = event.getX();
        this.y = event.getY();
        this.time = System.currentTimeMillis();
    }

    public boolean started(){
        return  System.currentTimeMillis() - time > STARTED;
    }

    public boolean done(){
        return  System.currentTimeMillis() - time > DONE;
    }

    public boolean outside(MotionEvent event) {
        float dx = event.getX()-x;
        float dy = event.getY()-y;
        return Math.sqrt(dx*dx+dy*dy) > distance;
    }

    public float percent() {return Math.min(1,(System.currentTimeMillis() - time - STARTED)/DONE);}
}
