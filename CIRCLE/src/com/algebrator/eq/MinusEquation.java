package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.SuperView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Colin on 1/4/2015.
 */
public class MinusEquation extends MonaryEquation {

    public MinusEquation(SuperView owner2) {
        super(owner2);

        display ="-";
    }

    @Override
    public Equation copy() {
        Equation result = new MinusEquation(this.owner);
        result.add(get(0).copy());
        return result;
    }


    @Override
    public void tryOperator(ArrayList<Equation> eqs) {
        if (parent instanceof MinusEquation){
            parent.replace(get(0));
        }else
        if (get(0) instanceof MinusEquation){
            replace(get(0).get(0));
        }else{
            if (get(0).size()>1){
                for (Equation e: get(0)){
                    Equation minus = new MinusEquation(owner);
                    e.replace(minus);
                    minus.add(e);
                }
                replace(get(0));
            }
        }
    }
}
