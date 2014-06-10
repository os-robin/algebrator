package com.example.circle;

import com.algebrator.eq.NumConstEquation;

public class DeleteAction extends Action {

	public EmilyView emilyView;
	public String num;
	
	public DeleteAction(EmilyView emilyView){
		super();
		this.emilyView = emilyView;
	}

	@Override
	public void act() {
		if (emilyView.selected instanceof NumConstEquation){
			if (((NumConstEquation)emilyView.selected).display.length()!= 0){
				((NumConstEquation)emilyView.selected).display=(String) ((NumConstEquation)emilyView.selected).display.subSequence(0, ((NumConstEquation)emilyView.selected).display.length()-1);
			}
		}
	}
}
