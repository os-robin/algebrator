package com.algebrator.eq;

import java.util.ArrayList;
import java.util.HashSet;

import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.SuperView;

public class AddEquation extends FlexOperation {

    @Override
    public void integrityCheck(){
        if (size() < 2){
            Log.e("ic","this should be at least size 2");
        }
    }

    public boolean canFlatten(Equation a, Equation b){
        Equation lcc = a.lowestCommonContainer(b);
        return (lcc.addContain(a) && lcc.addContain(b));
    }

    public void flatten(Equation a, Equation b){
        Equation lcc = a.lowestCommonContainer(b);
        // we need to work our way up from a to lcc
        // and on each step pull everthing from the level we are at up a level
        for (Equation x: new Equation[]{a,b}) {
            Equation at = x;
            while (!at.equals(lcc)) {
                if (at instanceof AddEquation){
                    Equation oldEq = at.parent;
                    int loc = oldEq.indexOf(at);
                    at.justRemove();
                    for (Equation e : at) {
                        oldEq.add(loc++, e);
                    }
                }
                at = at.parent;
            }
        }
    }

	@Override
	public Equation copy() {
		Equation result = new AddEquation(this.owner);
		result.display = this.getDisplay(-1);
		// pass selected?

		// copy all the kiddos and set this as their parent
		for (int i = 0; i < this.size(); i++) {
			result.add(this.get(i).copy());
			result.get(i).parent = result;
		}
		return result;
	}

	public String getDisplay(int pos) {
        if (pos ==-1){
            return display;
        }
        if (get(pos) instanceof MinusEquation){
            return "-";
        }
		return display;
	}

	public AddEquation(SuperView owner) {
		super(owner);
		display = "+";
		myWidth = Algebrator.getAlgebrator().DEFAULT_SIZE;
		myHeight = Algebrator.getAlgebrator().DEFAULT_SIZE;
	}
	public void tryOperator(ArrayList<Equation> eqs) {
        //TODO handle inbeddedness
        Equation a = eqs.get(0);
        Equation b = eqs.get(1);
		int at = Math.min(indexOf(a), indexOf(b));
        if (indexOf(a)> indexOf(b)){
            Equation temp =a;
            a=b;
            b=temp;
        }

        operateRemove(eqs);

        Equation result =Operations.Add(new MultiCountData(a),new MultiCountData(b));

//		Equation result = null;
//		Equation top=null;
//		Equation bottom=null;
//		Equation left = null;
//		Equation right = null;
//
//		operateRemove(eqs);
//
//		CountData cd1 = new CountData(a);
//		CountData cd2 = new CountData(b);
//
//		// find a common demoniator and adjust both sides acordingly
//		HashSet<Equation> commonOver = cd1.over.common(cd2.over);
//		cd1.over.remainder(commonOver);
//		cd2.over.remainder(commonOver);
//
//		cd1.key.addAll(cd2.over.rem);
//		cd1.value *= cd2.over.value;
//		cd2.key.addAll(cd1.over.rem);
//		cd2.value *= cd1.over.value;
//
//		commonOver.addAll(cd1.over.rem);
//		commonOver.addAll(cd2.over.rem);
//
//		CountData bot = new CountData();
//		bot.key = commonOver;
//		bot.value = cd1.over.value * cd2.over.value;
//
//		// if there is anything on bottom set that up
//		if (bot.value != 1 || bot.key.size() != 0) {
//			bottom = bot.toEquation(owner);
//		}
//
//		HashSet<Equation> common = cd1.common(cd2);
//		// we need to figure out the bottom
//
//		cd1.remainder(common);
//		cd2.remainder(common);
//
//		top = new AddEquation(owner);
//		Equation e1 = cd1.remToEquation(owner);
//		Equation e2 = cd2.remToEquation(owner);
//
//		if ( sortaNumber(e1) && sortaNumber(e2)){
//			double addTo = getValue(e1) + getValue(e2);
//            if (addTo < 0){
//                left = new MinusEquation(owner);
//                left.add( new NumConstEquation (-addTo,owner));
//            }else {
//                left = new NumConstEquation(addTo, owner);
//            }
//		}else{
//			left = new AddEquation(owner);
//			left.add(e1);
//			left.add(e2);
//		}
//
//
//		if (common.size() != 0 && !(left instanceof NumConstEquation && ((NumConstEquation) left).getValue()==0)){
//			// add common
//			if (common.size()==1){
//				right = (Equation) common.toArray()[0];
//			}else{
//				right = new MultiEquation(owner);
//				for (Equation e: common){
//					right.add(e);
//				}
//			}
//			top = new MultiEquation(owner);
//			top.add(left);
//			top.add(right);
//		}else{
//			top = left;
//		}
//
//		if (bottom != null){
//			result = new DivEquation(owner);
//			result.add(top);
//			result.add(bottom);
//		}else{
//			result = top;
//		}
		
		add(at, result);
        if (this.size() ==1){
            this.replace(this.get(0));
        }
	}



    private CountData updateCounts(Equation e, HashSet<CountData> counts) {
		CountData cd = new CountData(e);

		boolean hasMatch = false;
		for (CountData countData : counts) {
			if (countData.matches(cd)) {
				hasMatch = true;
				countData.value += cd.value;
			}
		}
		if (!hasMatch) {
			counts.add(cd);
		}
		return cd;

	}

}

class CountData {
	public HashSet<Equation> rem;
	public Double value = 1.0;
	public HashSet<Equation> key = new HashSet<Equation>();
	public CountData over = null;

	public CountData() {
	}

	public Equation remToEquation(SuperView owner) {
		if (rem.size() == 0) {
            if (value <0){
                Equation minus = new MinusEquation(owner);
                minus.add(new NumConstEquation(-value, owner));
                return minus;
            }else{
                return new NumConstEquation(value, owner);
            }
		} else if (value == 0){
            return new NumConstEquation(0, owner);
        }else if (value == 1 && rem.size() == 1) {
			return (Equation) rem.toArray()[0];
		} else if (value == 1) {
			Equation result = new MultiEquation(owner);
			for (Equation e : rem) {
				result.add(e);
			}
			return result;
		} else {
			Equation result = new MultiEquation(owner);
			result.add(new NumConstEquation(value , owner));
			for (Equation e : rem) {
				result.add(e);
			}
			return result;
		}
	}

	public Equation toEquation(SuperView owner) {
        if (value ==0){
            return new NumConstEquation(0,owner);
        }else if (key.size() == 0) {
			return new NumConstEquation(value, owner);
		} else if (value == 1 && key.size() == 1) {
			return (Equation) key.toArray()[0];
		} else if (value == 1) {
			Equation result = new MultiEquation(owner);
			for (Equation e : key) {
				result.add(e);
			}
			return result;
		} else {
			Equation result = new MultiEquation(owner);
			result.add(new NumConstEquation(value , owner));
			for (Equation e : key) {
				result.add(e);
			}
			return result;
		}
	}

	public CountData(Equation e) {
        over = new CountData();
		updateKey(e);
	}

	public void updateKey(Equation e) {
        while (e instanceof MinusEquation){
            value *= -1;
            e = e.get(0);
        }
		if (e instanceof MultiEquation) {
			for (Equation ee : e) {
				this.updateKey(ee);
			}
		} else if (e instanceof LeafEquation) {
			if (e instanceof NumConstEquation) {
				NumConstEquation ee = (NumConstEquation) e;
				this.value *= ee.getValue();
			} else {
				this.key.add(e);
			}
		} else if (e instanceof DivEquation) {
			this.updateKey(e.get(0));
			this.over.updateKey(e.get(1));
		} else {
			this.key.add(e);
		}
	}

	/**
	 * things in key that are not in common
	 * 
	 * @param common
	 * @return
	 */
	public void remainder(HashSet<Equation> common) {
        HashSet<Equation> commonClone = new HashSet<Equation>();
		HashSet<Equation> result = new HashSet<Equation>();
		for (Equation e : key) {
			boolean pass = true;
			for (Equation e2 : commonClone) {
				if (e.same(e2)) {
					pass = false;
                    commonClone.remove(e2);
					break;
				}
			}
			if (pass) {
				result.add(e);
			}
		}
		rem = result;
	}

	/**
	 * things in this.key also in cd2.key
	 * 
	 * @param cd2
	 * @return
	 */
	public HashSet<Equation> common(CountData cd2) {
		HashSet<Equation> result = new HashSet<Equation>();
		for (Equation e : key) {
			for (Equation e2 : cd2.key) {
				if (e.same(e2)) {
					result.add(e);
					break;
				}
			}
		}
		return result;
	}

	public boolean matches(CountData cd) {
		// if everything in this is the same as something in the other
		if (key.size() != cd.key.size()) {
			return false;
		}
		for (Equation e : key) {
			boolean any = false;
			for (Equation ee : cd.key) {
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


}
