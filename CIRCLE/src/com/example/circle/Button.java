package com.example.circle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Button {
	float left;
	float right;
	float top;
	float bottom;
	int color;
	Paint bgpaint;
	String text;
	Paint textpaint;
	Action myAction;
	
	public Button(float left, float right, float top, float bottom, int color, String text, Paint textpaint) {
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.color = color;
		this.bgpaint = new Paint();
		bgpaint.setColor(color);
		this.text = text;
		this.textpaint = textpaint;
	}

	
	public void draw(Canvas canvas) {
		
		int currentColor = bgpaint.getColor();
		int currentAlpha = android.graphics.Color.alpha(currentColor);
		int red = android.graphics.Color.red(currentColor);
		int green = android.graphics.Color.green(currentColor);
		int blue = android.graphics.Color.blue(currentColor);
		
		if(currentAlpha < 255) {
			currentColor = android.graphics.Color.argb(currentAlpha+17, red, green, blue);
		}
		
		bgpaint.setColor(currentColor);
		
		canvas.drawRect(left, top, right, bottom, bgpaint);
		//Log.i("I drew!", left+","+ top+","+ right+","+ bottom);
		canvas.drawText(text, (right+left)/2, (bottom+top)/2, textpaint);
		//Log.i("I wrote!", text);
	}
	
	
	public void click(MotionEvent event) {
		if (event.getX()<right && event.getX()>left && event.getY()<bottom && event.getY()>top){
			bgpaint.setColor(color - 0xff000000);
			if (myAction != null){
				myAction.act();
			}
		}	
	}
	
	

}
