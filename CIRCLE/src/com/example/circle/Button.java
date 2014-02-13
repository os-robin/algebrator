package com.example.circle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Button {
	float left;
	float right;
	float top;
	float bottom;
	Paint bgpaint;
	String text;
	Paint textpaint;
	
	public Button(float left, float right, float top, float bottom, int color, String text, Paint textpaint) {
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.bgpaint = new Paint();
		bgpaint.setColor(color);
		this.text = text;
		this.textpaint = textpaint;
	}

	
	public void draw(Canvas canvas) {
		canvas.drawRect(left, top, right, bottom, bgpaint);
		//Log.i("I drew!", left+","+ top+","+ right+","+ bottom);
		canvas.drawText(text, left+(right-left)/2, top+(bottom-top)/2, textpaint);
		//Log.i("I wrote!", text);
	}


	public void click(MotionEvent event) {
		if (event.getX()<right && event.getX()>left && event.getY()<bottom && event.getY()>top){
			bgpaint.setColor(0x00ffffff);
		}
		
	}

}
