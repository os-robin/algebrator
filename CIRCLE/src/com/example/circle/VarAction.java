package com.example.circle;

public class VarAction extends Action {
	
	public EmilyView emilyView;
	public String var;
	
	public VarAction(EmilyView emilyView, String var) {
		super();
		this.emilyView = emilyView;
		this.var = var;
	}

	@Override
	public void act() {
		
		if (emilyView.selected instanceof PlaceholderEquation) {
			
		}
		
	}
	

}
