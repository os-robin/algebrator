package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.EmilyView;

public class PlaceholderEquation extends FixEquation {
	
	public PlaceholderEquation(EmilyView ev) {
		super(ev);
		display = "_";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
	}

	@Override
	public float measureWidth() {
		return singleMeasureWidth();
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		singleDraw(canvas, x, y);
	}

	@Override
	public float measureHeight() {
		return singleMeasureHeight();
	}
	

}