package com.example.circle;

import com.algebrator.eq.Equation;

/**
 * Created by Colin on 2/5/2015.
 */
public class DragLocation implements Comparable<DragLocation> {
    public float x;
    public float y;
    public Equation myStupid;
    public SuperView owner;
    public boolean og = false;

    public DragLocation(Equation.Op op, Equation dragging, Equation equation, boolean right) {

        this.owner = equation.owner;

        if (dragging.equals(equation)) {
            og = true;
            myStupid = equation.owner.stupid;

            //myStupid.updateLocation();
            this.x = equation.x - myStupid.x;
            this.y = equation.y - myStupid.y;
        } else {
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
            Equation ourDragging = myAt;

            // try op with our copies
            ourEquation.tryOp(ourDragging, right, op);

            myStupid.x = 0;
            myStupid.y = 0;

            myStupid.updateLocation();
            this.x = ourDragging.x;
            this.y = ourDragging.y;
        }

    }

    public float dis = 0;

    public DragLocation(Equation equation) {
        myStupid = equation.owner.stupid;
        this.owner = equation.owner;
        og = true;

        //myStupid.updateLocation();
        this.x = equation.x - myStupid.x;
        this.y = equation.y - myStupid.y;
    }

    public void updateDis(float eventX, float eventY) {
        this.dis = (float) Math.sqrt((x + owner.stupid.x - eventX) * (x + owner.stupid.x - eventX) + (y + owner.stupid.y - eventY) * (y + owner.stupid.y - eventY));
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
}
