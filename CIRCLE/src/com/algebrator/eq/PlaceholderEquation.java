package com.algebrator.eq;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public class PlaceholderEquation extends LeafEquation {
	
	public PlaceholderEquation(SuperView owner) {
		super(owner);
		display = "_";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;

	}
	
	@Override
	public Equation copy() {
		Log.e("copy", "this should prolly not be called");
		
		Equation result = new PlaceholderEquation(this.owner);
		result.parentheses = this.parentheses;
		result.display = this.display;

		return result;
	}
}
