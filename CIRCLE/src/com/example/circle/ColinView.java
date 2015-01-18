package com.example.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.algebrator.eq.AddEquation;
import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.LeafEquation;
import com.algebrator.eq.MultiEquation;
import com.algebrator.eq.WritingEquation;
import com.algebrator.eq.WritingLeafEquation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

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
	
	protected void init(Context context) {
        canDrag= true;
        buttonsPercent=1f;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHistory(canvas);
    }

    // TODO scale with dpi
    float buffer = 100;
    float fade =0.8f;
    final int MIN_ALPHA =(int)(0xff*.2);
    private void drawHistory(Canvas canvas) {
        float atHeight = - stupid.measureHeight()/2 - buffer;
        int currentAlpha = (int) (0xff *0.8f);
        for (EquationButton eb:history) {
            if (!eb.equals(history.get(0))) {
                atHeight -= eb.myEq.measureHeight()/2;
                currentAlpha = Math.max(currentAlpha,MIN_ALPHA);
                eb.targetAlpha = currentAlpha;
                eb.x = 0;
                eb.targetY = atHeight;
                eb.draw(canvas,stupid.lastPoint.get(0).x,stupid.lastPoint.get(0).y);
                atHeight -= eb.myEq.measureHeight()/2 + buffer;
                currentAlpha*=fade;
            }
        }

    }


    @Override
	public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            for (int i = 0; i < history.size(); i++) {
                history.get(i).click(event);
            }
        }

        boolean result =super.onTouch(view, event);

        if (!stupid.same(history.get(0).myEq) && event.getAction() == MotionEvent.ACTION_UP){
            history.add(0, new EquationButton(stupid.copy(),this));
            Log.i("add to History", stupid.toString());
        }



        return result;
	}

    @Override
    protected void resolveSelected(MotionEvent event) {
        // now we need to figure out what we are selecting
        // find the least commond parent
        if (selectingSet.isEmpty()){
            // do nothing
        } else if (selectingSet.size() ==1){
            ((Equation) selectingSet.toArray()[0]).setSelected(true);
            selectingSet = new HashSet<Equation>();
        }else {
            selectSet();
        }
    }
}
