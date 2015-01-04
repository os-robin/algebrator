package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public class NumConstEquation extends LeafEquation {
	
	public NumConstEquation(double number, SuperView owner) {
		super(owner);
        if (number <0){
            negative =true;
            number = -number;
        }
		this.display = number+"";
        if (display.contains(".")) {
            while (display.charAt(display.length() - 1) == '0' || display.charAt(display.length() - 1) == '.') {
                if (display.charAt(display.length() - 1) == '.'){
                    display = display.substring(0, display.length() - 1);
                    break;
                }
                display = display.substring(0, display.length() - 1);
            }
        }

	}
	
	public double getValue(){
		double value = Double.parseDouble(display);
		return (this.negative?-value:value);
	}
	
	@Override
	public Equation copy() {
		Equation result = new NumConstEquation(this.getValue(), this.owner);

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
