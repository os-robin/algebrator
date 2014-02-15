package com.example.circle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Heart {
	float left;
	float right;
	float circleTop;
	float circleBottom;
	static Paint heartpaint;
	Path path = new Path();
	
	public Heart(int centerX, int centerY, int width, int color){
		super();
		this.left = centerX-width/2;
		this.right = centerX+width/2;
		this.circleTop = centerY-width/4;
		this.circleBottom = centerY+width/4;
		this.heartpaint = new Paint();
		heartpaint.setColor(color);
		heartpaint.setStyle(Paint.Style.FILL);
		
		//start at intersect of left line and left circle
		path.moveTo((float) (centerX-(width/4)*(1+Math.sin(Math.PI/4))), (float) (centerY+(width/4)*(Math.sin(Math.PI/4))));
		//line to center
		path.lineTo(centerX, centerY);
		//line to intersect of right line and right circle
		path.lineTo((float) (centerX+(width/4)*(1+Math.sin(Math.PI/4))), (float) (centerY+(width/4)*(Math.sin(Math.PI/4))));
		//line to bottom
		path.lineTo((float) centerX, (float) (centerY+width/(4*Math.sin(Math.PI/8))));
		//line back to intersect of left line and left circle
		path.lineTo((float) (centerX-(width/4)*(1+Math.sin(Math.PI/4))), (float) (centerY+(width/4)*(Math.sin(Math.PI/4))));
		//top left arc
		RectF circle1 = new RectF();
		circle1.set(left, circleTop, centerX, circleBottom);
		path.addArc(circle1, 135, 225);
		//top right arc
		RectF circle2 = new RectF();
		circle2.set(centerX, circleTop, right, circleBottom);
		path.addArc(circle2, 180, 225);
		//close path
		path.close();
	}
	
	public void draw(Canvas canvas) {
		canvas.drawPath(path, heartpaint);
	}
}
