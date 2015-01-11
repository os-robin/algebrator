package com.algebrator.eq;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.example.circle.Algebrator;
import com.example.circle.EmilyView;
import com.example.circle.SuperView;

import android.graphics.Canvas;
import android.location.GpsStatus;
import android.util.Log;

public class MultiEquation extends FlexOperation implements MultiDivSuperEquation {

    @Override
    public void integrityCheck(){
        if (size() < 2){
            Log.e("ic","this should be at least size 2");
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
	 * @param equation - a child of this
	 * @return true if the child would be on top false if the child would be on bottom or if the equation is not a child
	 */
	@Override
	public boolean onTop(Equation equation){
		boolean currentTop= true;
		Equation current = equation;
		String debug = current.hashCode() + ", ";
		while (true){
			
			if (current.equals(this)){
				return currentTop;
			}
			if (current.parent instanceof DivEquation){
				currentTop = ((DivEquation)current.parent).onTop(current) == currentTop;
			}
			current = current.parent;
			if (current == null){
				Log.i("bad",debug + " this is: " + this.hashCode());
			}
			debug += current.hashCode()+",";
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

        int at = Math.min(indexOf(eqs.get(0)), indexOf(eqs.get(1)));

        float aSign = 1;

        Equation a = eqs.get(0);
        // we need to digg throught all t
        while (a instanceof MultiEquation || a instanceof MinusEquation){
            if (a instanceof MinusEquation){
                aSign*=-1;
            }
            a = a.get(0);
        }
        while (a instanceof  MinusEquation){
            aSign *= -1;
            a = a.parent;
        }
        if (aSign ==-1){
            if (a instanceof MinusEquation){
                a= a.get(0);
            }else {
                Equation old = a;
                a = new MinusEquation(owner);
                a.add(old);
            }
        }

        float bSign = 1;

        Equation b = eqs.get(1);
        // we need to digg throught all t
        while (b instanceof MultiEquation || b instanceof MinusEquation){
            if (b instanceof MinusEquation){
                bSign*=-1;
            }
            b = b.get(0);
        }
        while (b instanceof  MinusEquation){
            bSign *= -1;
            b = b.parent;
        }

        if (bSign ==-1){
            if (b instanceof MinusEquation){
                b= b.get(0);
            }else {
                Equation old = b;
                b = new MinusEquation(owner);
                b.add(old);
            }
        }

        Equation top = null;
        Equation bottom = null;
        Equation result;

        operateRemove(a, b);
        // for the bottom and the top
        for (OnTop onTop: new OnTop[]{OnTop.TOP,OnTop.BOT}){
            // find all the equations on each side
            HashSet<Equation> left = new HashSet<Equation>();

            findEquation(onTop,a,left);
            HashSet<Equation> right =new HashSet<Equation>();

            findEquation(onTop,b,right);
            // multiply && combine like terms
            HashSet<MultiCountData> fullSet = Multiply(left,right, (onTop==OnTop.TOP?true:false));

            Equation innerResult = null;
            if (fullSet.size()==1){
                MultiCountData mine = ((MultiCountData)fullSet.toArray()[0]);
                innerResult = mine.getEquation(owner);
            }else if (fullSet.size() >1){
                innerResult = new AddEquation(owner);
                for (MultiCountData e: fullSet){
                    innerResult.add(e.getEquation(owner));
                }
            }

            if (onTop == OnTop.TOP){
                top = innerResult;
            } else {
                bottom = innerResult;
            }
        }

        if ((top instanceof  NumConstEquation && ((NumConstEquation) top).getValue()==0)&&
                !( bottom != null && bottom instanceof NumConstEquation && ((NumConstEquation) bottom).getValue()==0)){
            result = top;
        }else if (bottom!= null && !(bottom instanceof NumConstEquation && ((NumConstEquation) bottom).getValue()==1)){
            result = new DivEquation(owner);
            result.add(top);
            result.add(bottom);
        }else{
            result = top;
        }

        add(at, result);
        if (this.size() ==1){
            this.replace(this.get(0));
        }
	}

    private HashSet<MultiCountData> Multiply(HashSet<Equation> left, HashSet<Equation> right, boolean top) {
        HashSet<MultiCountData> result = new HashSet<MultiCountData>();
        if (!top){
            if (left.size() ==0){
                left.add(new NumConstEquation(1,owner));
            }
            if (right.size() ==0){
                right.add(new NumConstEquation(1,owner));
            }
        }
        for (Equation a : right){
            for (Equation b: left){
                MultiCountData toAdd =Multiply(a.copy(),b.copy());
                boolean found = false;
                for (MultiCountData mcd: result){
                    if (mcd.matches(toAdd)){
                        found = true;
                        mcd.value += toAdd.value;
                        break;
                    }
                }
                if (!found){
                    result.add(toAdd);
                }
            }
        }
        return result;
    }

    private MultiCountData Multiply(Equation a, Equation b) {
        MultiCountData result = new MultiCountData();

        for (Equation e: new Equation[]{a,b}) {
             multiplyHelper(e,result);
        }
        return result;
    }

    private void multiplyHelper(Equation e, MultiCountData result) {
        if (sortaNumber(e)){
            result.value *= getValue(e);
        }else if (e instanceof MultiEquation){
            for (Equation ee: e){
                if (ee instanceof  MultiEquation) {
                    multiplyHelper(ee, result);
                }else{
                    result.key.add(ee);
                }
            }
        }else{
            result.key.add(e);
        }
    }

    enum OnTop{TOP,BOT,BOTH}

    private void findEquation(OnTop onTop, Equation e,HashSet<Equation> set) {
        if (e instanceof  DivEquation){
            if (onTop == onTop.TOP){
                findEquation(onTop.BOTH, e.get(0), set);
                return;
            }else if (onTop == onTop.BOT){
                findEquation(onTop.BOTH, e.get(1), set);
                return;
            }else{
                set.add(e);
                return;
            }
        }else if (e instanceof  AddEquation){
            if (onTop != onTop.BOT) {
                for (Equation ee : e) {
                    findEquation(onTop.BOTH, ee, set);
                }
            }
            return;
        }else if (e instanceof  LeafEquation || e instanceof MinusEquation){
            if (onTop != OnTop.BOT) {
                set.add(e);
            }
            return;
        }
    }
}

class MultiCountData {

    public HashSet<Equation> key = new HashSet<Equation>();
    public Double value = 1.0;
    public MultiCountData(){}
    public MultiCountData(Equation e){
        update(e);
    }

    private void update(Equation e) {
        if (e instanceof MinusEquation){
            value *= -1;
            e = e.get(0);
        }

        if (e instanceof NumConstEquation){
            value*=((NumConstEquation) e).getValue();
        }else if (e instanceof MultiEquation){
            for (Equation ee:e){
                update(ee);
            }
        }else{
            key.add(e);
        }
    }

    public boolean matches(MultiCountData mcd) {
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
        return true;
    }

    public Equation getEquation(SuperView owner) {
        if ( key.size() ==0){
            if (value <0){
                Equation minus = new MinusEquation(owner);
                minus.add(new NumConstEquation(-value, owner));
                return minus;
            }else{
                return new NumConstEquation(value, owner);
            }
        }else{
            if (key.size() ==1 && value ==1){
                return (Equation) key.toArray()[0];
            }
            Equation result = new MultiEquation(owner);
            if (value!=1){
                result.add(new NumConstEquation(value ,owner));
            }
            for (Equation e: key){
                result.add(e);
            }
            return result;
        }
    }

    public void removeCommon(MultiCountData common) {
        if (common.value != 0) {
            this.value /= common.value;
        }
        for (Equation e:common.key){
            for (Equation ee:key) {
                if (ee.same(e)) {
                    key.remove(ee);
                }

            }
        }
    }
}
