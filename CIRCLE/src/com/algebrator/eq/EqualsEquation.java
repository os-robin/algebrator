package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class EqualsEquation extends FixEquation {
	
	public EqualsEquation() {
		display ="=";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
	}

	@Override
	public float measureWidth() {
		return horizMeasureWidth();
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		horizDraw(canvas,x,y);
		
	}

	@Override
	public float measureHeight() {
		return horizMeasureHeight();
	}

}
