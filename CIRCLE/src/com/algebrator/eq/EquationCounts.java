package com.algebrator.eq;

import com.example.circle.SuperView;

import java.util.HashMap;

/**
 * Created by Colin_000 on 1/18/2015.
 */
public class EquationCounts {
    Equation root = null;
    HashMap<Equation,Float> equations = new HashMap<Equation,Float>();
    float v =0;
    boolean neg = false;

    public EquationCounts(Equation e) {
        while (e instanceof MinusEquation){
            neg = !neg;
            e = e.get(0);
        }
        if (e instanceof  PowerEquation){
            root = e.get(0);
            // add all the top stuff
            Equation power = e.get(1);
            if (power instanceof AddEquation){
                for (Equation ee:power){
                    update(ee);
                }
            }else{
                update(e.get(1));
            }
        }else{
            if (neg){
                Equation newEq = new MinusEquation(e.owner);
                newEq.add(e);
                e = newEq;
                neg = false;
            }
            root = e;
            v=1;
        }
    }

    public EquationCounts() { }

    private void update(Equation e) {
        boolean innerNeg = false;
        while (e instanceof MinusEquation){
            innerNeg = !innerNeg;
            e = e.get(0);
        }
        if (e instanceof NumConstEquation){
            v+= ((NumConstEquation) e).getValue()*(innerNeg?-1:1);
        }else{
            boolean hasMatch =false;
            if (innerNeg){
                Equation newEq = new MinusEquation(e.owner);
                newEq.add(e);
                e = newEq;
            }
            for (Equation ee: equations.keySet()){
                if (ee.same(e)){
                    equations.put(ee,equations.get(ee)+1);
                    hasMatch = true;
                    break;
                }
            }
            if (!hasMatch){
                equations.put(e,1f);
            }
        }
    }


    public boolean add(Equation e) {
        boolean innerNeg = false;
        while (e instanceof MinusEquation){
            innerNeg = !innerNeg;
            e = e.get(0);
        }
        if (e instanceof  PowerEquation && e.get(0).same(root)){
            Equation power = e.get(1);
            if (power instanceof AddEquation){
                for (Equation ee:power){
                    update(ee);
                }
            }else{
                update(e.get(1));
            }
            return true;
        }else if (e.same(root)){
            v++;
            return true;
        }
        return false;
    }

    public Equation getEquation() {
        SuperView owner = root.owner;
        if (equations.size()==0){
            if ( v==1) {
                return root;
            }else if (v==0){
                return new NumConstEquation(1, owner);
            }else {
                Equation newEq = new PowerEquation(owner);
                newEq.add(root);
                newEq.add(new NumConstEquation(v, owner));
                return newEq;
            }
        }else if (equations.size()==1 && v==1){
            Equation newEq = new PowerEquation(owner);
            newEq.add(root);
            Equation key =(Equation)equations.keySet().toArray()[0];
            if (equations.get(key)==0){
                newEq = new NumConstEquation(1,owner);
            }else if (equations.get(key)==1){
                newEq.add(key);
            }else{
                Equation multi = new MultiEquation(owner);
                Equation num;
               if (equations.get(key)<0){
                   Equation inner= new NumConstEquation(-equations.get(key),owner);
                   num = new MinusEquation(owner);
                   num.add(inner);
               }else{
                   num= new NumConstEquation(equations.get(key),owner);
               }
                multi.add(num);
                multi.add(key);
                newEq.add(multi);
            }
            return newEq;
        }else{
            Equation newEq = new PowerEquation(owner);
            newEq.add(root);
            Equation addEq = new AddEquation(owner);
            for (Object o:equations.keySet().toArray()){
                Equation key = (Equation)o;
                if (equations.get(key)==0){
                }else  if (equations.get(key)==1){
                    addEq.add(key);
                }else{
                    Equation multi = new MultiEquation(owner);
                    Equation num;

                    if (equations.get(key)<0){
                        Equation inner= new NumConstEquation(-equations.get(key),owner);
                        num = new MinusEquation(owner);
                        num.add(inner);
                    }else{
                        num= new NumConstEquation(equations.get(key),owner);
                    }
                    multi.add(num);
                    multi.add(key);
                    addEq.add(multi);
                }
            }
            addEq.add(new NumConstEquation(v,owner));
            if (addEq.size()==0) {
                newEq = new NumConstEquation(1,owner);
            }else if (addEq.size()==1){
                newEq = addEq.get(0);
            }else{
                newEq.add(addEq);
            }
            return newEq;
        }
    }
}
