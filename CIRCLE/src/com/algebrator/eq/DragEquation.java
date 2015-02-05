package com.algebrator.eq;


import android.util.Log;

import com.algebrator.eq.Equation.Op;

import java.util.ArrayList;

public class DragEquation {
    // this is the on we draw
    public Equation eq;
    // this is the one in the equation
    public Equation demo;
    public ArrayList<Op> ops = new ArrayList<Op>();
    //public Equation oldDemo;

    // used to tell if the equation has moved
    Equation startParent = null;
    int startIndex = -1;

    public DragEquation(Equation eq) {
        super();
        this.eq = eq.copy();
        // is this a good idea?
        this.eq.parent = null;
        //this.oldDemo = eq;
        this.demo = eq;

        this.startParent = this.demo.parent.copy();
        this.startIndex = this.demo.parent.indexOf(this.demo);
        // figure out if it is add
        //TODO what happens if it is a Equals equation?
        updateOps(eq);
    }

    public void updateOps(Equation equation) {
        ops = new ArrayList<Op>();
        while (equation.parent instanceof MinusEquation) {
            equation = equation.parent;
        }
        if (equation.parent instanceof MultiDivSuperEquation || equation.parent instanceof EqualsEquation){
            ops.add(Op.MULTI);
            ops.add(Op.DIV);
        }
        if (equation.parent instanceof AddEquation || equation.parent instanceof EqualsEquation){
            ops.add(Op.ADD);
        }
        if (equation.parent instanceof PowerEquation && equation.parent.indexOf(equation) == 1){
            ops.add(Op.POWER);
        }
        if (ops.size()==0){
            Log.i("this seems bad", "");
        }
    }

    public Equation getAndUpdateDemo(Equation equation, boolean sameSide) {
        Equation toInsert;
        if (sameSide) {
            if (demo instanceof MinusEquation) {
                toInsert = demo.get(0);
            } else {
                toInsert = new MinusEquation(equation.owner);
                toInsert.add(demo);
            }
        } else {
            toInsert = demo;
        }
        return getAndUpdateDemo(toInsert);
    }

    public boolean moved(){
        if (demo.parent.same(startParent) && startIndex ==demo.parent.indexOf(demo)){
            return false;
        }
        Log.d("moved", demo.parent.toString() + " " + startParent);
        return true;
    }

    public Equation getAndUpdateDemo(Equation toInsert) {
        toInsert.isDemo(true);
        float oldX = eq.x;
        float oldY = eq.y;
        eq = demo.copy();
        eq.x = oldX;
        eq.y = oldY;
        return toInsert;
    }
}