package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

import com.example.circle.ColinView;
import com.example.circle.EmilyView;
import com.example.circle.SuperView;

import java.text.DecimalFormat;

public class NumConstEquation extends LeafEquation implements LegallityCheck {

	public NumConstEquation(double number, SuperView owner) {
		super(owner);
        if (number <0){
            Log.e("","should be positive");
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

    public String getDisplaySimple(){
        return display;
    }

    @Override
    public String getDisplay(int pos){
        DecimalFormat df = new DecimalFormat();
        if (owner instanceof ColinView) {
            df.setMaximumFractionDigits(3);
        }
        String result = df.format(getValue());
        if (display.endsWith(".")){
            result+=".";
        }
        return result;
    }

    //TODO
    public boolean illegal() {
        return true;
    }
	
	public double getValue(){
		double value = Double.parseDouble(display);
		return value;
	}
	
	@Override
	public Equation copy() {
		Equation result = new NumConstEquation(this.getValue(), this.owner);

		return result;
	}
	
	
	@Override
	public boolean same(Equation eq){
		if (!(eq instanceof NumConstEquation)) {
            return false;
        }
		NumConstEquation e = (NumConstEquation)eq;
		return getValue() == e.getValue();
	}
}
