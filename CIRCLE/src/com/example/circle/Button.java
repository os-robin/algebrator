package com.example.circle;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Button {
	float left;
	float right;
	float top;
	float bottom;
	Paint bgpaint;
	String text;
	Paint txtpaint;
	
	public Button(float left, float right, float top, float bottom, Paint bgpaint, String text, Paint txtpaint) {
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.bgpaint = bgpaint;
		this.text = text;
		this.txtpaint = txtpaint;
	}

	
	public void draw(Canvas canvas) {
		canvas.drawRect(left, top, right, bottom, bgpaint);
		//Log.i("I drew!", left+","+ top+","+ right+","+ bottom);
		canvas.drawText(text, left+(right-left)/2, top+(bottom-top)/2, txtpaint);
		//Log.i("I wrote!", text);
	}

}
