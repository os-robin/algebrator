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

import java.util.ArrayList;
import java.util.Arrays;
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
		boolean result =super.onTouch(view, event);

        if (!stupid.same(history.get(0).myEq) && event.getAction() == MotionEvent.ACTION_UP){
            history.add(0, new EquationButton(stupid.copy()));
            Log.i("add to History", stupid.toString());
        }

        return result;
	}

    @Override
    protected void resolveSelected() {
        // now we need to figure out what we are selecting
        // find the least commond parent
        boolean shareParent=true;
        Equation lcp = null;
        if (selectingSet.size() ==1){
            lcp = (Equation) selectingSet.toArray()[0];
            shareParent = false;
        }else {
            for (Equation eq : selectingSet) {
                if (lcp == null) {
                    lcp = eq.parent;
                } else if (!eq.parent.equals(lcp)) {
                    shareParent = false;
                    lcp = lcp.lowestCommonContainer(eq);
                }
            }
        }

        // let's check if we can flatten
        if (!shareParent && lcp instanceof AddEquation) {
            boolean canFlatten = true;
            Object[] array = selectingSet.toArray();
            for (int i = 0; i < array.length - 1 && canFlatten; i++) {
                canFlatten = ((AddEquation) lcp).canFlatten((Equation)array[i],(Equation)array[i+1]);
            }
            if (canFlatten){
                for (int i = 0; i < array.length - 1 && canFlatten; i++) {
                    ((AddEquation) lcp).flatten((Equation)array[i],(Equation)array[i+1]);
                }
                shareParent = true;
            }
        }

        if (shareParent && lcp != null) {
            // are they all next to each other?
            int[] indexs = new int[selectingSet.size()];
            int at = 0;
            for (Equation eq : selectingSet) {
                indexs[at] = lcp.indexOf(eq);
                at++;
            }
            Arrays.sort(indexs);
            boolean pass = true;
            for (int i = 0; i < indexs.length - 1 && pass; i++) {
                if (indexs[i] + 1 != indexs[i + 1]) {
                    pass = false;
                }
            }
            if (pass) {
                // if they do not make up all of lcp
                if (indexs.length != lcp.size()) {
                    // we make a new equation of the type of lcp
                    Equation toSelect = null;
                    if (lcp instanceof MultiEquation) {
                        toSelect = new MultiEquation(this);
                    } else if (lcp instanceof AddEquation) {
                        toSelect = new AddEquation(this);
                    } else if (lcp instanceof WritingEquation){
                        toSelect = new WritingEquation(this);
                    }
                    //sort selected set
                    ArrayList<Equation> selectedList = new ArrayList<Equation>();
                    at = indexs[0];
                    while (at <= indexs[indexs.length-1]){
                        for (Equation e: selectingSet){
                            if (lcp.indexOf(e)==at){
                                selectedList.add(e);
                                at++;
                            }
                        }
                    }

                    // remove the selectingSet from lcp and add it to our
                    // new equation
                    for (Equation eq : selectedList) {
                        lcp.justRemove(eq);
                        toSelect.add(eq);
                    }
                    // insert the new equation in to lcp
                    lcp.add(indexs[0], toSelect);
                    // and select the new equation
                    toSelect.setSelected(true);
                } else {
                    lcp.setSelected(true);
                }
            }
        } else {
            if (lcp != null) {
                lcp.setSelected(true);
            }
        }

        selectingSet = new HashSet<Equation>();
    }
}
