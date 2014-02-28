package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.EmilyView;

public class AddEquation extends FlexEquation {
	Equation empty;
	
	public AddEquation(EmilyView ev){
		super(ev);
		display ="+";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
		empty= new NumConstEquation("0",ev);
	}
	
	void operate(int pos1, int pos2){
		// 
	}
	
	void operate(){
		
	}
	
	boolean canOperate(){
		return false;
	}

	public float measureHeight() {
		return horizMeasureHeight();
	}

	public float measureWidth() {
		return horizMeasureWidth();
	}
	
	@Override
	public void draw(Canvas canvas, float x, float y) {
		horizDraw(canvas,x,y);		
	}
}
