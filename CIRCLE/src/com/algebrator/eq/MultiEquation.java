package com.algebrator.eq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MultiEquation extends FlexEquation implements List<Equation>{
	ArrayList<Equation> structured;

	@Override
	public boolean add(Equation equation) {
		boolean result = super.add(equation);
		structured.add(equation);
		return result;
	}

	@Override
	public void add(int pos, Equation equation) {
		super.add(pos,equation);
		//TODO structured
		
	}

	@Override
	public boolean addAll(Collection<? extends Equation> equations) {
		Iterator<? extends Equation> itter = equations.iterator();
		while (itter.hasNext()){
			this.add(itter.next());
		}
		// TODO what does this return
		return false;
	}

	@Override
	public void clear() {
		super.clear();
		structured.clear();
	}

	@Override
	public Equation remove(int pos) {
		Equation result = this.get(pos);
		remove(result);
		// TODO is this what I want to return?
		return result;
	}

	@Override
	public boolean remove(Equation eq) {
		for (int i=0;i<structured.size();i++){
			if (structured.get(i).contains(eq)){
				structured.get(i).remove(eq);
			}
		}
		return super.remove(eq);
	}

	@Override
	public Equation set(int pos, Equation eq) {
		super.set(pos, eq);
		
		return null;
	}

	
	/** 
	 * not implemented, do not use
	 */
	@Override
	public List<Equation> subList(int arg0, int arg1) {
		System.out.println("not implemented");
		return null;
	}
	
	/** 
	 * not implemented, do not use
	 */
	@Override
	public boolean retainAll(Collection<?> arg0) {
		System.out.println("not implemented");
		return false;
	}
	
	/** 
	 * not implemented, do not use
	 */
	@Override
	public boolean removeAll(Collection<?> arg0) {
		System.out.println("not implemented");
		return false;
	}
	
	/** 
	 * not implemented, do not use
	 */
	@Override
	public boolean addAll(int arg0, Collection<? extends Equation> arg1) {
		System.out.println("not implemented");
		return false;
	}
	
	/** 
	 * not implemented, do not use
	 */
	@Override
	public boolean containsAll(Collection<?> arg0) {
		System.out.println("not implemented");
		return false;
	}
}
