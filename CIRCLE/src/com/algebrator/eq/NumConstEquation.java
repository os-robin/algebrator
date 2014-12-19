package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.BigDaddy;
import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public class NumConstEquation extends LeafEquation {
	
	public NumConstEquation(String display, SuperView owner) {
		super(owner);
		this.display = display;

	}
	
	public double getValue(){
		double value = Double.parseDouble(display);
		return (this.negative?-value:value);
	}
	
	@Override
	public Equation copy() {
		Equation result = new NumConstEquation(this.display, this.owner);

		result.parentheses = this.parentheses;

		return result;
	}
	
	
	@Override
	public boolean same(Equation eq){
		if (!(eq instanceof NumConstEquation))
			return false;
		NumConstEquation e = (NumConstEquation)eq;
		return getValue() == e.getValue();
	}
}
