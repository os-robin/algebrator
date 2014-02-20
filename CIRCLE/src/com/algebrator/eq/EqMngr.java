package com.algebrator.eq;

public class EqMngr {
	
	public static AddEquation expand(AddEquation a,AddEquation b){
		AddEquation result = new AddEquation();
		for (int i=0;i<a.size();i++){
			for (int j=0;j<b.size();j++){
				MultiEquation toAdd = new MultiEquation();
				toAdd.add(a.get(i));
				toAdd.add(b.get(j));
				result.add(toAdd);
			}
		}
		return result;
	}
	
	public static boolean canSwitchSides(Equation child, Equation root){
		if (child.parent.equals(root)){
			return true;
		}
		if (child.parent.isFlex()){
			if (child.parent.parent.equals(root)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean trySwitchSides(Equation child, Equation root, Equation target, int pos){
		if (!canSwitchSides(child, root)){
			return false;
		}
		switchSides(child,target,pos);
		return true;
	}

	public static void switchSides(Equation child, Equation target, int pos) {
		child.parent.remove(child);
		target.add(pos, child);
	}
}
