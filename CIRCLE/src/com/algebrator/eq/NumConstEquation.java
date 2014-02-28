package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class NumConstEquation extends LeafEquation {
	private double value;
	
	public NumConstEquation(String display) {
		this.display = display;
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
	}

}
