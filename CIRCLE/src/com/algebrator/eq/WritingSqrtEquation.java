package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.circle.SuperView;

import java.util.ArrayList;

/**
 * Created by Colin on 1/29/2015.
 */
public class WritingSqrtEquation extends WritingPraEquation {

    public WritingSqrtEquation(SuperView owner){
        super(true,owner);
    }

    @Override
    public Equation copy() {
        Equation result = new WritingSqrtEquation(this.owner);
        return result;
    }


    //TODO scale by dpi
    private float buffer = 10;
    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        Paint temp = getPaint();

        Equation match = getMatch();
        float end;
        if (match != null){
            end =match.x+(match.measureWidth()/2);
        }else{
            Equation current = this;
            while (current.right() != null){
                current =  current.right();
            }
            end = current.x + (current.measureWidth()/2);
        }


        PowerEquation.sqrtSignDraw(canvas, x - measureWidth()/2,  y, temp ,measureHeightLower(), measureHeightUpper(),end);

        super.privateDraw(canvas,x+ (PowerEquation.width_addition + buffer)/2,y);
    }

    @Override
    protected float privateMeasureHeightUpper() {
        return super.privateMeasureHeightUpper() + PowerEquation.height_addition ;
    }

    @Override
    protected float privateMeasureWidth(){
        return super.privateMeasureWidth() + buffer + PowerEquation.width_addition;
    }


}
