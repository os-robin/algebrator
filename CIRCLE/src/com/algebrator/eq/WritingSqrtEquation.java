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


    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        Paint temp = getPaint();
        PowerEquation.sqrtSignDraw(canvas, x - measureWidth()/2, y, temp ,this);

        super.privateDraw(canvas,x,y);
    }

    @Override
    public float measureHeightUpper() {
        return super.measureHeightUpper() + PowerEquation.height_addition;
    }
}
