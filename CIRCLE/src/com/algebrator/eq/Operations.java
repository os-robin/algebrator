package com.algebrator.eq;

import com.example.circle.Algebrator;
import com.example.circle.SuperView;

import java.util.ArrayList;
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
            if (target.under == null) {
                target.under = at.under;
                at = null;
            } else {
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
        MultiCountData under = null;
        if (left.under != null && right.under == null) {
            under = left.under;
            right = Multiply(right, under);
            left.under = null;
        } else if (left.under == null && right.under != null) {
            under = right.under;
            left = Multiply(left, under);
            right.under = null;
        } else if (left.under != null && right.under != null) {
            MultiCountData common = findCommon(left.under, right.under);
            MultiCountData leftRem = remainder(left.under, common);
            MultiCountData rightRem = remainder(right.under, common);

            right = Multiply(right, leftRem);
            right.under = Multiply(right.under, leftRem);

            left = Multiply(left, rightRem);
            left.under = Multiply(left.under, rightRem);

            // at this point left.under should equals right.under
            // so we set
            under = left.under;
            left.under = null;
            right.under = null;
        }

        //TODO get owner a different way?
        SuperView owner = Algebrator.getAlgebrator().superView;

        //if under == null we actully add
        if (under == null) {
            MultiCountData common = findCommon(left, right);
            common.value = 1.0;
            left = remainder(left, common);
            right = remainder(right, common);
            Equation result = addHelper(left, right);
            // and multiply the result time common if there is any common
            if (common.key.size() != 0 || common.value != 1) {
                Equation holder = new MultiEquation(owner);
                holder.add(result);
                holder.add(common.getEquation(owner));
                return holder;
            } else {
                return result;
            }
        }
        //otherwise we are done
        else {
            //return (left + right ) /under


            Equation top = new AddEquation(owner);
            top.add(left.getEquation(owner));
            top.add(right.getEquation(owner));
            Equation bot = under.getEquation(owner);
            Equation result = new DivEquation(owner);
            result.add(top);
            result.add(bot);
            return result;
        }
    }

    private static Equation addHelper(MultiCountData left, MultiCountData right) {
        SuperView owner = Algebrator.getAlgebrator().superView;
        // if they are both just numbers make a NumConst
        if (left.key.size() == 0 && right.key.size() == 0) {
            double sum = right.value + left.value;
            if (sum >= 0) {
                return new NumConstEquation(sum, owner);
            } else {
                Equation result = new MinusEquation(owner);
                result.add(new NumConstEquation(-sum, owner));
                return result;
            }
        }
        // otherwise make an add equation and throw them both in it
        Equation result = new AddEquation(owner);
        result.add(left.getEquation(owner));
        result.add(right.getEquation(owner));
        return result;
    }


    private static MultiCountData remainder(MultiCountData left, MultiCountData common) {
        MultiCountData result = new MultiCountData();
        result.value = left.value;
        ArrayList<EquationCounts> leftCopy = new ArrayList<EquationCounts>();
        for (Equation e:left.copyKey() ){
            leftCopy.add(new EquationCounts(e));
        }
        ArrayList<EquationCounts> commonCopy = new ArrayList<EquationCounts>();
        for (Equation e:common.copyKey() ){
            commonCopy.add(new EquationCounts(e));
        }
        for (EquationCounts lft : leftCopy) {
            boolean match = false;
            for (EquationCounts com : commonCopy) {
                EquationCounts removed = removeCommon(lft,com);
                if (!lft.equals(removed)){
                    match = true;
                    result.key.add(removed.getEquation());
                    commonCopy.remove(com);
                    break;
                }
            }
            if (!match){
                result.key.add(lft.getEquation());
            }
        }

        // what do we do with unders?
        // nothing for now

        return result;
    }

    private static MultiCountData findCommon(MultiCountData left, MultiCountData right) {
        MultiCountData result = new MultiCountData();
//        HashSet<Equation> leftCopy = left.copyKey();
//        HashSet<Equation> rightCopy = right.copyKey();

        // convert everything to Equation Counts
        ArrayList<EquationCounts> leftCopy = new ArrayList<EquationCounts>();
        for (Equation e:left.copyKey() ){
            leftCopy.add(new EquationCounts(e));
        }
        ArrayList<EquationCounts> rightCopy = new ArrayList<EquationCounts>();
        for (Equation e:right.copyKey() ){
            rightCopy.add(new EquationCounts(e));
        }

        for (EquationCounts e : leftCopy) {
            for (EquationCounts ee : rightCopy) {
                EquationCounts common = findCommon(e,ee);
                if (common!=null){
                    result.key.add(common.getEquation());
                    rightCopy.remove(ee);
                    break;
                }
            }
        }
        // find what is common in the values
        // only if both values are ints
        if ((left.value == Math.floor(left.value)) && (left.value == Math.floor(left.value))) {
            result.value = Double.valueOf(gcd((int) (Math.abs(Math.floor(left.value))), (int) Math.abs(Math.floor(right.value))));
        }
        return result;
    }

    private static EquationCounts findCommon(EquationCounts left, EquationCounts right) {
        if (left.root.same(right.root)){
            EquationCounts result = new EquationCounts();
            result.root = left.root;
            // find what is common with v
            if (Math.signum(left.v)== Math.signum(right.v)) {
                result.v = Math.min(Math.abs(left.v), Math.abs(right.v))*Math.signum(left.v);
            }
            // now what do they share up top side
            for (Equation keyl : left.equations.keySet()) {
                for (Equation keyr : right.equations.keySet()) {
                    if (keyl.same(keyr)) {
                        float vl= left.equations.get(keyl);
                        float vr= right.equations.get(keyr);
                        if (Math.signum(vl)== Math.signum(vr)) {
                            float myV = Math.min(Math.abs(vl), Math.abs(vr))*Math.signum(vl);
                            result.equations.put(keyl,myV);
                        }
                    }
                }
            }
            return result;
        }else{
            return null;
        }
    }

    private static EquationCounts removeCommon(EquationCounts old, EquationCounts remove) {
        if (old.root.same(remove.root)){
            EquationCounts result = new EquationCounts();
            result.root = old.root;
            // find what is common with v
            if (Math.signum(old.v)== Math.signum(old.v)) {
                result.v = old.v -  remove.v;
            }
            // now what do they share up top side
            for (Equation keyl : old.equations.keySet()) {
                boolean foundMatch = false;

                for (Equation keyr : remove.equations.keySet()) {
                    if (keyl.same(keyr)) {
                        foundMatch = true;
                        float vl= old.equations.get(keyl);
                        float vr= remove.equations.get(keyr);
                        result.equations.put(keyl,vl-vr);
                    }
                }
                if (!foundMatch){
                    result.equations.put(keyl,old.equations.get(keyl));
                }
            }
            return result;
        }else{
            return old;
        }
    }


    static int gcd(int a, int b) {
        while (a != 0 && b != 0) // until either one of them is 0
        {
            int c = b;
            b = a % b;
            a = c;
        }
        return a + b; // either one is 0, so return the non-zero value
    }

    // **************************** DIVIDE *****************************************

    public static Equation divide(Equation a, Equation b) {
        SuperView owner = Algebrator.getAlgebrator().superView;
        Equation result = null;
        if (a instanceof AddEquation) {
            result = new AddEquation(owner);
            for (Equation e : a) {
                Equation newEq = new DivEquation(owner);
                newEq.add(e);
                newEq.add(b.copy());
                result.add(newEq);
            }
//            for (Equation e:get(0)){
//                // figure out what is common
//                MultiCountData top = new MultiCountData(e);
//                MultiCountData bot = new MultiCountData(get(1));
//                MultiCountData common = common(top,bot);
//                top.removeCommon(common);
//                bot.removeCommon(common);
//                if (!(bot.getEquation(owner) instanceof NumConstEquation && ((NumConstEquation) bot.getEquation(owner)).getValue() == 1)){
//                    Equation inner = new DivEquation(owner);
//                    inner.add(top.getEquation(owner));
//                    inner.add(bot.getEquation(owner));
//                    result.add(inner);
//                }else if (!(top.getEquation(owner) instanceof NumConstEquation && ((NumConstEquation) top.getEquation(owner)).getValue() == 0 )){
//                    result.add(top.getEquation(owner));
//                }
//            }
//            if (result.size() == 1){
//                result = result.get(0);
//            }
        } else {
            // figure out what is common
            MultiCountData top = new MultiCountData(a);
            MultiCountData bot = new MultiCountData(b);
            MultiCountData common = findCommon(top, bot);
            if (common.value != 1 || !common.key.isEmpty()) {
                if (!common.key.isEmpty()) {
                    top = remainder(top, common);
                    bot = remainder(bot, common);

                } else {
                    top.value /= common.value;
                    bot.value /= common.value;
                }
                Equation topEq = top.getEquation(owner);
                Equation botEq = bot.getEquation(owner);
                result = getResult(topEq, botEq);
            } else {
                Equation topEq = a;
                Equation botEq = b;
                if (sortaNumber(botEq) && sortaNumber(topEq)) {
                    // they are both number that can not be reduced
                    double num = getValue(topEq) / getValue(botEq);
                    if (num < 0) {
                        Equation inner = new NumConstEquation(-num, owner);
                        result = new MinusEquation(owner);
                        result.add(inner);
                    } else {
                        result = new NumConstEquation(num, owner);
                    }
                } else {
                    // if top is a * split it up
                   // if (topEq instanceof MultiEquation) {
                    //    result = new MultiEquation(owner);
                    //    for (Equation e : topEq) {
                     //       Equation newEq = getResult(e, botEq.copy());
                    //        result.add(newEq);
                    //    }
                    //}
                    // else there is nothing for us to do
                    //else {
                        result = getResult(topEq, botEq);
                    //}
                }
            }
        }
        return result;
    }

    private static Equation getResult(Equation topEq, Equation botEq) {
        SuperView owner = Algebrator.getAlgebrator().superView;
        // if the top is 0 and the bottom is not
        if ((sortaNumber(topEq) && getValue(topEq) == 0) && !((sortaNumber(botEq) && getValue(botEq) == 0))) {
            return new NumConstEquation(0, owner);
        } else
            // they are both meaningful
            if (!(sortaNumber(botEq) && Math.abs(getValue(botEq)) == 1)) {

                Equation inner = new DivEquation(owner);
                inner.add(topEq);
                inner.add(botEq);
                return inner;
            }
            // bottom is 1
            else {
                if (getValue(botEq) == -1) {
                    if (topEq instanceof MinusEquation) {
                        return topEq.get(0);
                    } else {
                        Equation inner = topEq;
                        Equation neg = new MinusEquation(owner);
                        neg.add(inner);
                        return neg;
                    }
                } else {
                    return topEq;
                }
            }
    }

    protected static double getValue(Equation e) {
        int minus = 1;
        while (e instanceof MinusEquation) {
            e = e.get(0);
            minus *= -1;
        }
        return ((NumConstEquation) e).getValue() * minus;
    }

    protected static boolean sortaNumber(Equation e) {
        while (e instanceof MinusEquation) {
            e = e.get(0);
        }
        return e instanceof NumConstEquation;
    }
}
