package com.algebrator.eq;


import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.SuperView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.transform.Result;

public class MultiEquation extends FlexOperation implements MultiDivSuperEquation {

    @Override
    public void integrityCheck() {
        if (size() < 2) {
            Log.e("ic", "this should be at least size 2");
        }
    }

    @Override
    public Equation copy() {
        Equation result = new MultiEquation(this.owner);
        result.display = this.display;
        // pass selected?

        // copy all the kiddos and set this as their parent
        for (int i = 0; i < this.size(); i++) {
            result.add(this.get(i).copy());
            result.get(i).parent = result;
        }
        return result;
    }

    /**
     * returns true is the given equation could be "on top" if this MultiEquation was written as A/B
     *
     * @param equation - a child of this
     * @return true if the child would be on top false if the child would be on bottom or if the equation is not a child
     */
    @Override
    public boolean onTop(Equation equation) {
        boolean currentTop = true;
        Equation current = equation;
        String debug = current.hashCode() + ", ";
        while (true) {

            if (current.equals(this)) {
                return currentTop;
            }
            if (current.parent instanceof DivEquation) {
                currentTop = ((DivEquation) current.parent).onTop(current) == currentTop;
            }
            current = current.parent;
            if (current == null) {
                Log.i("bad", debug + " this is: " + this.hashCode());
            }
            debug += current.hashCode() + ",";
        }
    }

    public MultiEquation(SuperView owner) {
        super(owner);
        display = "*";

        myWidth = Algebrator.getAlgebrator().DEFAULT_SIZE;
        myHeight = Algebrator.getAlgebrator().DEFAULT_SIZE;
    }

    public void tryOperator(ArrayList<Equation> eqs) {
        //TODO handle inbeddedness
        String db ="";
        for (Equation e:eqs){
            db +=e.toString();
        }
        Log.i("",db);


        int at = Math.min(indexOf(eqs.get(0)), indexOf(eqs.get(1)));


        Equation result = null;

        operateRemove(eqs);
        // for the bottom and the top
            // find all the equations on each side

        HashSet<MultiCountData> left = new HashSet<MultiCountData>();
        Operations.findEquation(eqs.get(0), left);

        HashSet<MultiCountData> right = new HashSet<MultiCountData>();
        Operations.findEquation(eqs.get(1), right);
            // multiply && combine like terms
        HashSet<MultiCountData> fullSet = Operations.Multiply(left, right);

        if (fullSet.size() == 1) {
            MultiCountData mine = ((MultiCountData) fullSet.toArray()[0]);
            result = mine.getEquation(owner);
        } else if (fullSet.size() > 1) {
            result = new AddEquation(owner);
            for (MultiCountData e : fullSet) {
                result.add(e.getEquation(owner));
            }
        }

        add(at, result);
        if (this.size() == 1) {
            this.replace(this.get(0));
        }
    }



}

class MultiCountData {

    public HashSet<Equation> key = new HashSet<Equation>();
    public Double value = 1.0;
    public MultiCountData under;

    public MultiCountData() {
    }

    public HashSet<Equation> copyKey(){
        HashSet<Equation> result = new HashSet<Equation>();
        for (Equation e: key){
            result.add(e.copy());
        }
        return result;
    }

    public MultiCountData(MultiCountData mcd) {
        key = mcd.copyKey();
        this.value = mcd.value;
        if (mcd.under != null) {
            this.under =new MultiCountData(mcd.under);
        }
    }

    public MultiCountData(Equation e) {
        update(e);
    }

    private void update(Equation e) {
        if (e instanceof MinusEquation) {
            value *= -1;
            e = e.get(0);
        }
        if (e instanceof NumConstEquation) {
            value *= ((NumConstEquation) e).getValue();
        } else if (e instanceof MultiEquation) {
            for (Equation ee : e) {
                update(ee);
            }
        } else if (e instanceof DivEquation) {
            update(e.get(0));
            if (under == null) {
                under = new MultiCountData(e.get(1));
            } else {
                under.update(e.get(1));
            }
        } else {
            key.add(e);
        }
    }

    public boolean matches(MultiCountData mcd) {
        if (mcd ==null){
            return false;
        }
        // if everything in this is the same as something in the other
        if (key.size() != mcd.key.size()) {
            return false;
        }
        for (Equation e : key) {
            boolean any = false;
            for (Equation ee : mcd.key) {
                if (ee.same(e)) {
                    any = true;
                    break;
                }
            }
            if (!any) {
                return false;
            }
        }
        if (under != null) {
            return under.matches(mcd.under);
        }else if (mcd.under != null){
            return false;
        }
        return true;
    }

    public Equation getEquation(SuperView owner) {
        Equation top = null;
        Equation bot = null;
        Equation result = null;

        if (under != null){
            if (!(under.value ==1 && under.key.size() ==0)) {
                bot = under.getEquation(owner);
            }
        }

        if (value ==0){
            top = new NumConstEquation(0,owner);
            if (under != null){
                if (under.value !=0){
                    bot =null;
                }
            }
        }else if (key.size() == 0) {
            if (value < 0) {
                Equation minus = new MinusEquation(owner);
                minus.add(new NumConstEquation(-value, owner));
                top = minus;
            } else {
                top =new  NumConstEquation(value, owner);
            }
        } else {
            if (key.size() == 1 && Math.abs(value) == 1) {
                if (value ==1) {
                    top = (Equation) key.toArray()[0];
                }else{
                    top = new MinusEquation(owner);
                    top.add((Equation) key.toArray()[0]);
                }
            }else {
                top = new MultiEquation(owner);

                if (value != 1) {
                    if (value <0){
                        Equation inner = new NumConstEquation(-value, owner);
                        Equation minus = new MinusEquation(owner);
                        minus.add(inner);
                        top.add(minus);
                    }else {
                        top.add(new NumConstEquation(value, owner));
                    }
                }
                ArrayList<EquationCounts> ecs = new ArrayList<EquationCounts>();
                for (Equation e :key) {
                    boolean match = false;
                    for (EquationCounts ec:ecs) {
                        if (ec.add(e)){
                            match = true;
                        }
                    }
                    if (!match){
                        ecs.add(new EquationCounts(e));
                    }
                }
                // add all the equation
                for (EquationCounts ec:ecs){
                    top.add(ec.getEquation());
                }
                if (top.size() ==1){
                    top = top.get(0);
                }
            }
        }

        if (bot!=null)
        {
            result = new DivEquation(owner);
            result.add(top);
            result.add(bot);
        }else{
            result =top;
        }
        return result;
    }
}
