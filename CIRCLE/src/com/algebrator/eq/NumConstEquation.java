package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.BigDaddy;
import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public class NumConstEquation extends LeafEquation {
	private double value;
	
	public NumConstEquation(String display, SuperView owner) {
		super(owner);
		this.display = display;

	}
	
	@Override
	public Equation copy() {
		Equation result = new NumConstEquation(this.display, this.owner);

		result.parentheses = this.parentheses;

		return result;
	}
}
