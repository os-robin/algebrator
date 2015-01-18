package com.algebrator.eq;

import com.example.circle.Algebrator;
import com.example.circle.SuperView;

import java.util.HashSet;

/**
 * Created by Colin on 1/15/2015.
 */
public class Operations {

    // **************************** MULTIPLY *****************************************

    public static HashSet<MultiCountData> Multiply(HashSet<MultiCountData> left, HashSet<MultiCountData> right) {
        HashSet<MultiCountData> result = new HashSet<MultiCountData>();
        for (MultiCountData a : right) {
            for (MultiCountData b : left) {
                MultiCountData toAdd = Multiply(a, b);
                boolean found = false;
                for (MultiCountData mcd : result) {
                    if (mcd.matches(toAdd)) {
                        found = true;
                        mcd.value += toAdd.value;
                        break;
                    }
                }
                if (!found) {
                    result.add(toAdd);
                }
            }
        }
        return result;
    }

    private static MultiCountData Multiply(MultiCountData a, MultiCountData b) {
        MultiCountData result = new MultiCountData();

        for (MultiCountData e : new MultiCountData[]{a, b}) {
            multiplyHelper(e, result);
        }
        return result;
    }

    private static void multiplyHelper(MultiCountData newMcd, MultiCountData result) {
        MultiCountData at = newMcd;
        MultiCountData target = result;
        while (at != null) {
            target.value *= at.value;
            for (Equation e : at.key) {
                target.key.add(e.copy());
            }
            if (target.under ==null){
                target.under =at.under;
                at =null;
            }else{
                at = at.under;
                target = target.under;
            }
        }
    }

    public static void findEquation(Equation e, HashSet<MultiCountData> set) {
        if (e instanceof AddEquation) {
            for (Equation ee : e) {
                findEquation(ee, set);
            }
        } else {
            set.add(new MultiCountData(e.copy()));
        }
    }

    // **************************** ADD *****************************************

    public static Equation Add(MultiCountData left, MultiCountData right) {
        //
        MultiCountData under=null;
        if (left.under != null && right.under == null){
            under = left.under;
            right = Multiply(right,under);
            left.under =null;
        }else if (left.under == null && right.under != null){
            under = right.under;
            left = Multiply(left,under);
            right.under = null;
        }else if (left.under != null && right.under != null) {
            MultiCountData common = findCommon(left.under, right.under);
            MultiCountData leftRem = remainder(left.under,common);
            MultiCountData rightRem = remainder(right.under,common);

            right = Multiply(right,leftRem);
            right.under = Multiply(right.under,leftRem);

            left = Multiply(left,rightRem);
            left.under = Multiply(left.under,rightRem);

            // at this point left.under should equals right.under
            // so we set
            under = left.under;
            left.under =null;
            right.under = null;
        }

        //TODO get owner a different way?
        SuperView owner = Algebrator.getAlgebrator().superView;

        //if under == null we actully add
        if (under == null){
            MultiCountData common = findCommon(left, right);
            common.value =1.0;
            left = remainder(left,common);
            right = remainder(right,common);
            Equation result = addHelper(left,right);
            // and multiply the result time common if there is any common
            if (common.key.size() != 0 || common.value != 1){
                Equation holder = new MultiEquation(owner);
                holder.add(result);
                holder.add(common.getEquation(owner));
                return holder;
            }else{
                return  result;
            }
        }
        //otherwise we are done
        else{
        //return (left + right ) /under


            Equation top = new AddEquation(owner);
            top.add(left.getEquation(owner));
            top.add(right.getEquation(owner));
            Equation bot = under.getEquation(owner);
            Equation result = new DivEquation(owner);
            result.add(top);
            result.add(bot);
            return  result;
        }
    }

    private static Equation addHelper(MultiCountData left, MultiCountData right) {
        SuperView owner = Algebrator.getAlgebrator().superView;
        // if they are both just numbers make a NumConst
        if (left.key.size() ==0 && right.key.size() ==0){
            double sum = right.value + left.value;
            if (sum>=0) {
                return new NumConstEquation(sum,owner);
            }else{
                Equation result = new MinusEquation(owner);
                result.add(new NumConstEquation(-sum,owner));
                return result;
            }
        }
        // otherwise make an add equation and throw them both in it
        Equation result = new AddEquation(owner);
        result.add(left.getEquation(owner));
        result.add(right.getEquation(owner));
        return result;
    }


    private static MultiCountData remainder(MultiCountData right, MultiCountData common) {
        MultiCountData localCopy = new MultiCountData(right);
        right.value /= common.value;
        HashSet<Equation>  keyCopy = common.copyKey();
        for (Equation e: keyCopy){
            for (Equation ee: localCopy.key){
                if (ee.same(e)){
                    localCopy.key.remove(ee);
                    break;
                }
            }
        }
        // what do we do with unders?
        // nothing for now

        return localCopy;
    }

    private static MultiCountData findCommon(MultiCountData left, MultiCountData right) {
        MultiCountData result = new MultiCountData();
        HashSet<Equation>  leftCopy = left.copyKey();
        HashSet<Equation>  rightCopy = right.copyKey();

        for (Equation e: leftCopy){
            for (Equation ee: rightCopy){
                if (ee.same(e)){
                    result.key.add(e);
                    rightCopy.remove(ee);
                    break;
                }
            }
        }
        // find what is common in the values
        // only if both values are ints
        if ((left.value == Math.floor(left.value)) && (left.value == Math.floor(left.value))){
            result.value = Double.valueOf(gcd((int)(Math.abs(Math.floor(left.value))),(int)Math.abs(Math.floor(right.value))));
        }
        return result;
    }

    static int gcd(int a, int b)
    {
        while(a!=0 && b!=0) // until either one of them is 0
        {
            int c = b;
            b = a%b;
            a = c;
        }
        return a+b; // either one is 0, so return the non-zero value
    }

}
