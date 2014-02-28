package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class DivEquation extends FixEquation {
	
	public boolean isTop(Equation equation){
		return equation.equals(get(0));
	}
	
	public DivEquation(){
		display ="/";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
	}

	@Override
	public float measureWidth() {
		return vertMeasureWidth();
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		vertDraw(canvas,x,y);
	}

	@Override
	public float measureHeight() {
		return vertMeasureHeight();
	}
	
	@Override
	public Equation remove(int pos){
		if (pos ==0){
			Equation result = get(0);
			set(0, new NumConstEquation("1"));
			return result;
		}else if (pos ==1){
			parent.set(parent.indexOf(this), get(0));
			parent = null;
			set(0, new NumConstEquation("1"));
			return this;
		}
		return parent;
	}
}
