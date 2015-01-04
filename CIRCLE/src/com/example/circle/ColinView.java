package com.example.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;

import java.util.ArrayList;

public class ColinView extends SuperView {
    ArrayList<EquationButton> history = new ArrayList<EquationButton>();
	
	public ColinView(Context context) {
		super(context);
		init(context);
	}

	public ColinView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ColinView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	protected void init(Context context) {}

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHistory(canvas);
    }

    // TODO scale with dpi
    float buffer = 100;
    float fade =0.7f;
    final int MIN_ALPHA =(int)(0xff*.3);
    private void drawHistory(Canvas canvas) {
        float atHeight = - stupid.measureHeight()/2 - buffer;
        int currentAlpha = (int) (0xff *0.65f);
        for (EquationButton eb:history) {
            if (!eb.equals(history.get(0))) {
                atHeight -= eb.myEq.measureHeight()/2;
                currentAlpha*=fade;
                //currentAlpha = Math.max(currentAlpha,MIN_ALPHA);
                eb.targetAlpha = currentAlpha;
                eb.x = 0;
                eb.targetY = atHeight;
                eb.draw(canvas,stupid.lastPoint.get(0).x,stupid.lastPoint.get(0).y);
                atHeight -= eb.myEq.measureHeight()/2 + buffer;
            }
        }

    }

    @Override
	public boolean onTouch(View view, MotionEvent event) {
		boolean result =super.onTouch(view, event);

        if (!stupid.same(history.get(0).myEq) && event.getAction() == MotionEvent.ACTION_UP){
            history.add(0, new EquationButton(stupid.copy()));
            Log.i("add to History", stupid.toString());
        }

        return result;
	}
}
