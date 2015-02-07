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
    Equation startStupid;

    public DragEquation(Equation eq) {
        super();
        this.eq = eq.copy();
        // is this a good idea?
        this.eq.parent = null;
        //this.oldDemo = eq;
        this.demo = eq;

        this.startStupid = eq.owner.stupid;

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

    public boolean moved(Equation currentStupid){
        return !currentStupid.equals(startStupid);
    }
}