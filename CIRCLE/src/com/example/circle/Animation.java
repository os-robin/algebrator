package com.example.circle;

import android.graphics.Canvas;

/**
 * Created by Colin on 1/21/2015.
 */
public abstract class Animation {
    final SuperView owner;

    public Animation(SuperView owner){
        this.owner =owner;
    }

    public abstract void draw(Canvas canvas);

    protected void remove(){
        owner.animation.remove(this);
    }



}
