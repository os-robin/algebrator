package com.example.circle;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Square {
	float left;
	float right;
	float top;
	float bottom;
	int a;
	Paint paint;
	
	public Square(float left, float right, float top, float bottom, int a, Paint paint) {
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.a = a;
		this.paint = paint;
	}

	
	public void draw(Canvas canvas) {
		canvas.drawRect(left, top, right, bottom, paint);
	}

}
