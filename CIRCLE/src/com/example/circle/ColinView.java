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
                if ((stupid.lastPoint.get(0).y + atHeight + eb.myEq.measureHeightLower()) > 0 &&
                        (stupid.lastPoint.get(0).y + atHeight - eb.myEq.measureHeightUpper()) < height) {
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

        String db1 = "";
        for (Equation e: willSelect){
            db1 += e.toString() + ",";
        }


        Log.d("resolving selected, will select: ",db1);
        Log.d("resolving selected, selected: ",(selected==null?"null":selected.toString()));

        HashSet<Equation> selectingSet = new HashSet<Equation>();

        if (selected != null) {
            // if everything we are adding is deep contained by selected we want to remove
            // otherwise we want to add what is not already contained


            boolean remove = true;
            for (Equation e:willSelect){
                if (!selected.deepContains(e)){
                    remove = false;
                    break;
                }
            }
            selectingSet.addAll(selected.getLeafs());
            if (remove){
                for (Equation e:willSelect){
                    if (selectingSet.contains(e)){
                        selectingSet.remove(e);
                    }
                }
            }else{
                int selectedSide = selected.side();
                for (Equation e:willSelect){
                    if (!selected.deepContains(e) && e.side() == selectedSide){
                        selectingSet.add(e);
                    }
                }
            }

            // now we need to deselect and flatten
            selected.setSelected(false);
            stupid.fixIntegrety();

        }else{
            selectingSet = willSelect;
        }



        String db2 = "";
        for (Equation e: selectingSet){
            db2 += e + ",";
        }
        Log.d("resolving selected, selectingSet: ",db2);

        if (selectingSet.isEmpty()) {
            //do nothing
        } else if (selectingSet.size() == 1) {
            ((Equation) selectingSet.toArray()[0]).setSelected(true);
        } else {
            selectSet(selectingSet);
        }
    }
}
