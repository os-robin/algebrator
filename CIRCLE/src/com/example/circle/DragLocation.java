package com.example.circle;

import android.util.Log;

import com.algebrator.eq.Equation;

/**
 * Created by Colin on 2/5/2015.
 */
public class DragLocation implements Comparable<DragLocation> {
    public float x;
    public float y;
    public Equation myStupid;
    public SuperView owner;
    public Equation myDemo;
    public boolean og = false;

    public DragLocation(Equation.Op op, Equation dragging, Equation equation, boolean right) {
        if (dragging.equals(equation)) {
            oGinit(equation);
        } else {
            this.owner = equation.owner;
            myStupid = equation.owner.stupid.copy();
            //let's follow the path down
            Equation at = owner.stupid;
            Equation myAt = myStupid;
            while (!at.equals(equation)) {
                int index = at.deepIndexOf(equation);
                at = at.get(index);
                myAt = myAt.get(index);
            }
            Equation ourEquation = myAt;

            at = equation.owner.stupid;
            myAt = myStupid;
            while (!at.equals(dragging)) {
                int index = at.deepIndexOf(dragging);
                at = at.get(index);
                myAt = myAt.get(index);
            }
            myDemo  = myAt;

            // try op with our copies

            myDemo = ourEquation.tryOp(myDemo, right, op);
            Log.i("tryOp", myDemo.toString() + " " +myStupid.toString());

            myStupid.x = 0;
            myStupid.y = 0;

            myStupid.updateLocation();
            Log.i("updated Loc", myDemo.toString() + " " +myStupid.toString());

            float equalsOffest = myStupid.lastPoint.get(0).x - owner.stupid.lastPoint.get(0).x - owner.stupid.x;

            this.x = myDemo.x - myStupid.lastPoint.get(0).x;//+equalsOffest;
            this.y = myDemo.y - myStupid.lastPoint.get(0).y;

            myDemo.demo = true;
        }

    }

    private void oGinit(Equation equation) {
        this.owner = equation.owner;
        og = true;
        myStupid = equation.owner.stupid;
        myDemo = equation;

        //myStupid.updateLocation();
        this.x = equation.x - myStupid.lastPoint.get(0).x;
        this.y = equation.y - myStupid.lastPoint.get(0).y;

        myDemo.demo = true;
    }

    public float dis = 0;

    public DragLocation(Equation equation) {
        oGinit(equation);
    }

    public void updateDis(float eventX, float eventY) {
        this.dis = (float) Math.sqrt((x + owner.stupid.lastPoint.get(0).x- eventX) *
                (x + owner.stupid.lastPoint.get(0).x - eventX) +
                (y + owner.stupid.lastPoint.get(0).y - eventY) *
                (y + owner.stupid.lastPoint.get(0).y - eventY));
    }

    @Override
    public int compareTo(DragLocation other) {
        float otherDis = other.dis;
        if (otherDis > dis) {
            return -1;
        } else if (otherDis < dis) {
            return 1;
        }
        return 0;
    }

    public boolean isOG() {
        return og;
    }

    public void select() {
        owner.stupid = myStupid;
        if (!isOG()){
            ((ColinView)owner).changed = true;
        }else{
            myDemo.setSelected(true);
        }
        myStupid.deDemo();
    }
}
