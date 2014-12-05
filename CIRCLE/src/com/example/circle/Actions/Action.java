package com.example.circle.Actions;

import com.example.circle.EmilyView;


/**
 * How <b>b</b>u<i>t</i>t<b><i>o</b></i>ns know what to do
 * @author Emily
 * 
 */
public abstract class Action {
	public EmilyView emilyView;
	
	public Action(EmilyView emilyView) {
		this.emilyView = emilyView;
	}
	
	abstract public void act();

}
