package com.algebrator.eq;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public class PlaceholderEquation extends LeafEquation {
	
	public PlaceholderEquation(SuperView owner) {
		super(owner);
		display = "|";
		myWidth = Algebrator.getAlgebrator().DEFAULT_SIZE;
		myHeight = Algebrator.getAlgebrator().DEFAULT_SIZE;

	}
	
	@Override
	public Equation copy() {
		Log.e("copy", "this should prolly not be called");
		
		Equation result = new PlaceholderEquation(this.owner);
		result.display = this.display;

		return result;
	}
	
	
}
