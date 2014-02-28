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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float measureHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
