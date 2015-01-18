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
        for (Equation e: mcd.key){
            key.add(e.copy());
        }
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
            if (key.size() == 1 && value == 1) {
               top =(Equation) key.toArray()[0];
            }else {
                top = new MultiEquation(owner);

                if (value != 1) {
                    top.add(new NumConstEquation(value, owner));
                }
                ArrayList<Equation> list = new ArrayList<Equation>(key);
                for (int i=0;i< list.size();i++) {
                    boolean neg = false;
                    Equation e =  list.get(i);
                    HashMap<Equation,Integer> equations = new HashMap<Equation,Integer>();
                    int v =1;
                    for (int j=i+1;j<list.size();j++){
                        Equation ee = list.get(j);
                        while (ee instanceof MinusEquation){
                            neg = !neg;
                            ee = ee.get(0);
                        }

                        if (e.same(ee)){
                            list.remove(ee);
                            j--;
                            v++;
                        }

                        //TODO (x^5)^x sneeks under our radar

                        if (ee instanceof PowerEquation && e.same(ee.get(0))){
                            list.remove(ee);
                            j--;
                            Equation power = ee.get(1);
                            boolean powerNeg = false;
                            while (power instanceof  MinusEquation){
                                power = power.get(0);
                                powerNeg = !powerNeg;
                            }

                            if (power instanceof AddEquation){
                                for (Equation eee: power){
                                    if (eee instanceof NumConstEquation){
                                        v += ((NumConstEquation) eee).getValue()*(powerNeg?-1:1);
                                    }else{
                                        boolean found = false;
                                        for (Equation key : equations.keySet()){
                                            if (key.same(eee)){
                                                equations.put(key,equations.get(eee)+1);
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (!found){
                                            equations.put(eee,1);
                                        }
                                    }
                                }

                            }
                            if (power instanceof NumConstEquation){
                                v += ((NumConstEquation) power).getValue()*(powerNeg?-1:1);
                            }else{
                                boolean found = false;
                                for (Equation key : equations.keySet()){
                                    if (key.same(power)){
                                        equations.put(key,equations.get(power)+1);
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found){
                                    equations.put(power,1);
                                }
                            }
                        }
                    }

                    if (equations.size() ==0 && v==1) {
                        top.add(e);
                    }else if (equations.size()==0){
                        Equation newEq = new PowerEquation(owner);
                        newEq.add(e);
                        newEq.add(new NumConstEquation(v,owner));
                        top.add(newEq);
                    }else if (equations.size()==1 && v==1){
                        Equation newEq = new PowerEquation(owner);
                        newEq.add(e);
                        Equation key =(Equation)equations.keySet().toArray()[0];
                        if (equations.get(key)==1){
                            newEq.add(key);
                        }else{
                            Equation multi = new MultiEquation(owner);
                            multi.add(new NumConstEquation(equations.get(key),owner));
                            multi.add(key);
                            newEq.add(multi);
                        }
                        top.add(newEq);
                    }else{
                        Equation newEq = new PowerEquation(owner);
                        newEq.add(e);
                        Equation addEq = new AddEquation(owner);
                        for (Object o:equations.keySet().toArray()){
                            Equation key = (Equation)o;
                            if (equations.get(key)==1){
                                addEq.add(key);
                            }else{
                                Equation multi = new MultiEquation(owner);
                                multi.add(new NumConstEquation(equations.get(key),owner));
                                multi.add(key);
                                addEq.add(multi);
                            }
                        }
                        top.add(newEq);
                    }
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

    public void removeCommon(MultiCountData common) {
        if (common.value != 0) {
            this.value /= common.value;
        }
        for (Equation e : common.key) {
            for (Equation ee : key) {
                if (ee.same(e)) {
                    key.remove(ee);
                }

            }
        }
    }
}
