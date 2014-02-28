package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.BigDaddy;
import com.example.circle.EmilyView;

public class NumConstEquation extends FixEquation {
	private double value;
	
	public NumConstEquation(String display, EmilyView ev) {
		super(ev);
		this.display = display;
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		textPaint = new Paint();
		textPaint.setTextSize(BigDaddy.TEXT_SIZE);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
	}

	@Override
	public float measureWidth() {
		return singleMeasureWidth();
	}

	@Override
	public float measureHeight() {
		return singleMeasureHeight();
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		singleDraw(canvas,x,y);
	}

}
