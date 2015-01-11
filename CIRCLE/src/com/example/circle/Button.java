package com.example.circle;

import com.example.circle.Actions.Action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.MotionEvent;

public class Button {
    // in percent of width (1 = full width)
	float left;
    // in percent of width (1 = full width)
	float right;
    // in percent of height (1 = full height)
	float top;
    //  in percent of height (1 = full height)

    float canvasWidth;
    float canvasHeight;

	float bottom;
	Paint bkgPaint;
	Paint textPaint;
	int highlightColor;
	int targetBkgColor;
	String text;
	Action myAction;

    public Button (){

    }

	public Button(float left, float right, float top, float bottom, String text, Paint textPaint,Paint bkgPaint, int highlightColor) {
		super();

        this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.text = text;
		this.bkgPaint=new Paint(bkgPaint);
		targetBkgColor = bkgPaint.getColor();
		this.textPaint = new TextPaint(textPaint);
		this.highlightColor = highlightColor;
	}

	final float SCALE = 10;
	public void draw(Canvas canvas) {

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
		
		int currentColor = bkgPaint.getColor();
		int currentAlpha = android.graphics.Color.alpha(currentColor);
		int currentRed = android.graphics.Color.red(currentColor);
		int currentGreen = android.graphics.Color.green(currentColor);
		int currentBlue = android.graphics.Color.blue(currentColor);
		int targetAlpha = android.graphics.Color.alpha(targetBkgColor);
		int targetRed = android.graphics.Color.red(targetBkgColor);
		int targetGreen = android.graphics.Color.green(targetBkgColor);
		int targetBlue = android.graphics.Color.blue(targetBkgColor);
		
		//if(currentAlpha > 0) {
        int lastColor = currentColor;

			currentColor = android.graphics.Color.argb(
                    (int)(((SCALE-1)*currentAlpha+targetAlpha)/SCALE),
                    (int)(((SCALE-1)*currentRed+targetRed)/SCALE),
                    (int)(((SCALE-1)*currentGreen+targetGreen)/SCALE),
                    (int)(((SCALE-1)*currentBlue+targetBlue)/SCALE));
        if (lastColor == currentColor){
            currentColor = targetBkgColor;
        }
		//}
		
		bkgPaint.setColor(currentColor);
		
		canvas.drawRect(left(), top(), right(), bottom(), bkgPaint);
		//Log.i("I drew!", left+","+ top+","+ right+","+ bottom);
		canvas.drawText(text, (right()+left())/2, (bottom()+top())/2, textPaint);
		//Log.i("I wrote!", text);
	}

    private float top() {
        return top* canvasHeight;
    }

    private float left() {
        return left*canvasWidth;
    }

    private float bottom() {
        return bottom* canvasHeight;
    }

    private float right() {

        return right*canvasWidth;
    }

    public boolean couldClick(MotionEvent event){
          return (event.getX()<right() && event.getX()>left() && event.getY()<bottom() && event.getY()>top());
    }


    public void click(MotionEvent event) {
		if (event.getX()<right() && event.getX()>left() && event.getY()<bottom() && event.getY()>top()){
			bkgPaint.setColor(highlightColor);
			if (myAction != null){
				myAction.act();
			}
		}	
	}
	
	

}
