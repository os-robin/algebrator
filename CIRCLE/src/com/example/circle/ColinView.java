package com.example.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.algebrator.eq.Equation;

import java.util.ArrayList;
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
        canDrag = true;
        buttonsPercent = 1f;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHistory(canvas);
        for (EquationButton eb:history){
            eb.tryRevert(canvas);
        }
    }

    // TODO scale with dpi
    float buffer = 100;
    float fade = 0.8f;
    final int MIN_ALPHA = (int) (0xff * .2);

    private void drawHistory(Canvas canvas) {
        float atHeight = -stupid.measureHeightUpper() - buffer;
        int currentAlpha = (int) (0xff * 0.8f);
        for (EquationButton eb : history) {
            if (!eb.equals(history.get(0))) {
                atHeight -= eb.myEq.measureHeightLower();
                currentAlpha = Math.max(currentAlpha, MIN_ALPHA);
                eb.targetAlpha = currentAlpha;
                eb.x = 0;
                eb.targetY = atHeight;
                eb.update(stupid.lastPoint.get(0).x, stupid.lastPoint.get(0).y);
                if ((stupid.lastPoint.get(0).y + atHeight + eb.myEq.measureHeightLower()) > 0) {
                    eb.draw(canvas, stupid.lastPoint.get(0).x, stupid.lastPoint.get(0).y);
                }
                atHeight -= eb.myEq.measureHeightUpper() + buffer;
                currentAlpha *= fade;
            }
        }
    }


    public boolean changed= false;
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        for (int i = 0; i < history.size(); i++) {
            history.get(i).click(event);
        }

        boolean result = super.onTouch(view, event);

        if (changed) {
            history.add(0, new EquationButton(stupid.copy(), this));
            Log.i("add to History", stupid.toString());
            changed = false;
        }

        return result;
    }

    @Override
    protected void resolveSelected(MotionEvent event) {
        // now we need to figure out what we are selecting
        // find the least commond parent
        if (selectingSet.isEmpty()) {
            // do nothing
        } else if (selectingSet.size() == 1) {
            ((Equation) selectingSet.toArray()[0]).setSelected(true);
            selectingSet = new HashSet<Equation>();
        } else {
            selectSet();
        }
    }
}
