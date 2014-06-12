package com.example.circle;

public class ParenthesesAction extends Action {
	
	public EmilyView emilyView;
	
	public ParenthesesAction(EmilyView emilyView) {
		super();
		this.emilyView = emilyView;
		
	}

	@Override
	public void act() {
		emilyView.selected.parentheses = ! emilyView.selected.parentheses;
	}

}
