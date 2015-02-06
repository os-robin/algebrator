
package com.example.circle;

import com.algebrator.eq.DragEquation;
import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;

/**
 * Created by Colin on 2/5/2015.
 */
public class DragLocation implements Comparable<DragLocation>{
    public float x;
    public float y;
    public Equation myStupid;
    public DragLocation(Equation.Op op, Equation dragging, Equation equation,boolean right) {
        myStupid = equation.owner.stupid.copy();

        //let's follow the path down
        Equation at = equation.owner.stupid;
        Equation myAt = myStupid;
        while (!at.equals(equation)){
            int index = at.deepIndexOf(equation);
            at = at.get(index);
            myAt = myAt.get(index);
        }
        Equation ourEquation= myAt;

        at = equation.owner.stupid;
        myAt = myStupid;
        while (!at.equals(dragging)){
            int index = at.deepIndexOf(dragging);
            at = at.get(index);
            myAt = myAt.get(index);
        }
        Equation ourDragging=myAt;

        // try op with our copies
        ourEquation.tryOp(ourDragging,right,op);
        myStupid.x = equation.owner.stupid.x;
        myStupid.y = equation.owner.stupid.y;

        myStupid.updateLocation();
        this.x = ourDragging.x;
        this.y = ourDragging.y;
    }

    public float dis=0;

    public void updateDis(float x, float y){
        this.dis = (float)Math.sqrt((myStupid.x - x)*(myStupid.x - x)+(myStupid.y - y)*(myStupid.y - y));
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

}
