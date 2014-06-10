package com.algebrator.eq;

public class EqMngr {
	
	public static AddEquation expand(AddEquation a,AddEquation b){
		AddEquation result = new AddEquation(a.owner);
		for (int i=0;i<a.size();i++){
			for (int j=0;j<b.size();j++){
				MultiEquation toAdd = new MultiEquation(a.owner);
				toAdd.add(a.get(i));
				toAdd.add(b.get(j));
				result.add(toAdd);
			}
		}
		return result;
	}
}
