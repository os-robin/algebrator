package com.example.circle;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.MotionEvent;

import com.example.circle.Actions.Action;

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

    public Button() {

    }

    public Button(String text, Action myAction) {
        super();

        this.myAction = myAction;
        this.text = text;
        this.bkgPaint = new TextPaint(Algebrator.getAlgebrator().bkgPaint);
        targetBkgColor = Algebrator.getAlgebrator().mainColor;
        this.textPaint = new TextPaint(Algebrator.getAlgebrator().textPaint);
        //TODO scale by dpi
        //TODO does not work at all
    }

    public void setLocation(float left, float right, float top, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public Button(String text, Action myAction, float left, float right, float top, float bottom) {
        this(text, myAction);
        setLocation(left, right, top, bottom);
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
                (int) (((SCALE - 1) * currentAlpha + targetAlpha) / SCALE),
                (int) (((SCALE - 1) * currentRed + targetRed) / SCALE),
                (int) (((SCALE - 1) * currentGreen + targetGreen) / SCALE),
                (int) (((SCALE - 1) * currentBlue + targetBlue) / SCALE));
        if (lastColor == currentColor) {
            currentColor = targetBkgColor;
        }
        //}

        bkgPaint.setColor(currentColor);

        canvas.drawRect(left(), top(), right(), bottom(), bkgPaint);

        Rect out = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), out);
        while (out.width() > right() - left()) {
            textPaint.setTextSize(textPaint.getTextSize() - 1);
            textPaint.getTextBounds(text, 0, text.length(), out);
        }
        textPaint.getTextBounds("A", 0, "A".length(), out);
        float h = out.height();


        Paint textHighlight = new Paint();
        textHighlight.setTextSize(textPaint.getTextSize());
        textHighlight.setTypeface(textPaint.getTypeface());
        textHighlight.setTextAlign(Paint.Align.CENTER);
        //textHighlight.setAntiAlias(true);
        //TODO scale by dpi
        textHighlight.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        textHighlight.setARGB(0xff,
                (android.graphics.Color.red(bkgPaint.getColor())*2+0xff)/3,
                (android.graphics.Color.green(bkgPaint.getColor())*2+0xff)/3,
                (android.graphics.Color.blue(bkgPaint.getColor())*2+0xff)/3);

        canvas.drawText(text, (right() + left()) / 2, (bottom() + top()) / 2 + h / 2 , textHighlight);
        //textHighlight.setMaskFilter(null);

        canvas.drawText(text, (right() + left()) / 2, (bottom() + top()) / 2 + h / 2, textPaint);
    }

    private float top() {
        return top * canvasHeight;
    }

    private float left() {
        return left * canvasWidth;
    }

    private float bottom() {
        return bottom * canvasHeight;
    }

    private float right() {

        return right * canvasWidth;
    }

    public boolean couldClick(MotionEvent event) {
        return (event.getX() < right() && event.getX() > left() && event.getY() < bottom() && event.getY() > top());
    }


    public void click(MotionEvent event) {
        if (event.getX() < right() && event.getX() > left() && event.getY() < bottom() && event.getY() > top()) {
            bkgPaint.setColor(highlightColor);
            if (myAction != null) {
                myAction.act();
            }
        }
    }


}
