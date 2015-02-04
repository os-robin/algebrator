package com.example.circle;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.algebrator.eq.Equation;
import com.algebrator.eq.EquationDis;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
import com.example.circle.Actions.DecimalAction;
import com.example.circle.Actions.DeleteAction;
import com.example.circle.Actions.DivAction;
import com.example.circle.Actions.EqualsAction;
import com.example.circle.Actions.LeftAction;
import com.example.circle.Actions.MinusAction;
import com.example.circle.Actions.NumberAction;
import com.example.circle.Actions.ParenthesesAction;
import com.example.circle.Actions.PlusAction;
import com.example.circle.Actions.PowerAction;
import com.example.circle.Actions.RightAction;
import com.example.circle.Actions.Solve;
import com.example.circle.Actions.SqrtAction;
import com.example.circle.Actions.TimesAction;
import com.example.circle.Actions.VarAction;

public class EmilyView extends SuperView {

    /**
     * list of our buttons
     */

    /**
     * list of our passed in variables
     */
    ArrayList<Button> vars = new ArrayList<Button>();

    public EmilyView(Context context) {
        super(context);
        init(context);
    }

    public EmilyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmilyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void init(Context context) {
        //Equation yo = new PowerEquation(this);
        //yo.add(new NumConstEquation(2.0,this));
        //yo.add(new NumConstEquation(2.0,this));
        stupid = new WritingEquation(this);
        Equation empty = new PlaceholderEquation(this);
        //stupid.add(yo);
        stupid.add(empty);
        empty.setSelected(true);

        addButtons();

        //Button solve = new Button(0, 1, 0, 1f / 6f, "solve", text,
        //		bkg, highlight);
        //solve.myAction = new Solve(this);
        //buttons.add(solve);

		/*
         * AddEquation add1 = new AddEquation(); add1.add(new
		 * NumConstEquation("3")); add1.add(new NumConstEquation("4"));
		 * AddEquation add2 = new AddEquation(); add2.add(new
		 * NumConstEquation("2")); add2.add(new NumConstEquation("5"));
		 * AddEquation add3 = new AddEquation(); add3.add(new
		 * NumConstEquation("1")); add3.add(new NumConstEquation("6"));
		 * stupid.add(add1); stupid.add(add2); stupid.add(add3);
		 */

        //PlaceholderEquation empty1 = new PlaceholderEquation(this);
        //empty1.setSelected(true);
        //PlaceholderEquation empty2 = new PlaceholderEquation(this);
        //stupid.add(empty1);
        //stupid.add(empty2);

//		buttons.get(10).myAction = new Action(this) {
//			int count = 0;
//
//			@Override
//			public void act() {
//
//				// TODO while there is room:
//				// TODO allow the player to enter a var name
//				// TODO it would be cool if you could swipe up or something to
//				// delete these
//
//				Button tempButton = new Button((10f - count) / 11f, (11f - count) / 11f, 3f / 6f, 4f/ 6f,
//						"A" + count, text, bkg, highlight);
//				tempButton.myAction = new VarAction(emilyView, tempButton.text);
//
//				vars.add(tempButton);
//
//				count++;
//			}
//
//		};
        buttonsPercent=4f/6f;

    }

    private void addButtons() {

        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button("7",new NumberAction(this, "7")));
        firstRow.add(new Button("8",new NumberAction(this, "8")));
        firstRow.add(new Button("9",new NumberAction(this, "9")));
        firstRow.add(new Button("a",new VarAction(this, "a")));
        firstRow.add(new Button("b",new VarAction(this, "b")));
        firstRow.add(new Button("(",new ParenthesesAction(this,true)));
        firstRow.add(new Button(")",new ParenthesesAction(this,false)));
        firstRow.add(new Button("=",new EqualsAction(this)));
        //TODO this does not work since my font does not support this
        char[] backSpaceUnicode = { '\u232B'};
        firstRow.add(new Button("⌫",new DeleteAction(this)));

        ArrayList<Button> secondRow = new ArrayList<Button>();
        secondRow.add(new Button("4",new NumberAction(this, "4")));
        secondRow.add(new Button("5",new NumberAction(this, "5")));
        secondRow.add(new Button("6",new NumberAction(this, "6")));
        secondRow.add(new Button(".", new DecimalAction(this, ".")));
        secondRow.add(new Button("+",new PlusAction(this)));
        char[] timesUnicode = { '\u00D7'};
        secondRow.add(new Button(new String(timesUnicode),new TimesAction(this)));
        secondRow.add(new Button("^",new PowerAction(this)));


        ArrayList<Button> thridRow = new ArrayList<Button>();
        thridRow.add(new Button("1",new NumberAction(this, "1")));
        thridRow.add(new Button("2",new NumberAction(this, "2")));
        thridRow.add(new Button("3",new NumberAction(this, "3")));
        thridRow.add(new Button("0",new NumberAction(this, "0")));
        thridRow.add(new Button("-",new MinusAction(this)));
        char[] divisionUnicode = { '\u00F7'};
        thridRow.add(new Button(new String(divisionUnicode),new DivAction(this)));
        char[] sqrtUnicode = { '\u221A'};
        thridRow.add(new Button(new String(sqrtUnicode),new SqrtAction(this)));
        char[] leftUnicode = { '\u2190'};
        thridRow.add(new Button(new String(leftUnicode),new LeftAction(this)));
        char[] rightUnicode = { '\u2192'};
        thridRow.add(new Button(new String(rightUnicode),new RightAction(this)));

        addButtonsRow(firstRow,6f/9f,7f/9f);
        addButtonsRow(secondRow,0f,7f/9f,7f/9f,8f/9f);
        Button solve = new Button("Solve",new Solve(this));
        solve.setLocation(7f/9f,1f,7f/9f,8f/9f);
        buttons.add(solve);
        addButtonsRow(thridRow, 8f / 9f, 9f / 9f);

    }

    private void addButtonsRow(ArrayList<Button> row, float top , float bottum) {
        addButtonsRow(row,0,1, top ,bottum);

    }

    private void addButtonsRow(ArrayList<Button> row, float left, float right, float top , float bottum) {
        float count = row.size();
        float at = left;
        float step = (right-left)/count;

        for (float i=0;i<count;i++){
            Button b =row.get((int)i);
            b.setLocation(at, at+step, top, bottum);
            buttons.add(b);
            at +=step;
        }

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawShadow(canvas);

        for (int i = 0; i < vars.size(); i++) {
            vars.get(i).draw(canvas);
        }
    }

    private void drawShadow(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(0x404040);
        p.setAlpha(0x7f);
        int at=((int)buttonLine());
        for (int i=0;i<2;i++){
            canvas.drawLine(0,at,width,at,p);
            at--;
        }
        while (p.getAlpha() >1){
            canvas.drawLine(0,at,width,at,p);
            p.setAlpha((int)(p.getAlpha()/1.2));
            at--;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            for (int i = 0; i < vars.size(); i++) {
                vars.get(i).click(event);
            }
        }
        return super.onTouch(view, event);
    }

    @Override
    protected void resolveSelected(MotionEvent event) {
        // now we need to figure out what we are selecting
        // find the least commond parent

        if (selectingSet.size() <2){
            Equation lcp = null;
            // if it's an action up
            if (event.getAction() ==MotionEvent.ACTION_UP){
                // and it was near the left of stupid
                ArrayList<EquationDis> closest = stupid.closest(
                        event.getX(), event.getY());

               lcp = closest.get(0).equation;
                // TODO 100 to var scale by dpi
                if (Math.abs(event.getY() - lcp.y) <100) {
                    if (lcp instanceof PlaceholderEquation) {
                        lcp.setSelected(true);
                    } else {
                        boolean left = event.getX() < lcp.x;
                        // insert a Placeholder to the left of everything
                        Equation toSelect = new PlaceholderEquation(this);
                        toSelect.x = event.getX();
                        toSelect.y = event.getY();
                        // add toSelect left of lcp
                        if (lcp.parent instanceof WritingEquation) {
                            int at = lcp.parent.indexOf(lcp);
                            lcp.parent.add(at + (left ? 0 : 1), toSelect);
                            toSelect.setSelected(true);
                        } else {
                            Equation oldEq = lcp;
                            Equation holder = new WritingEquation(this);
                            oldEq.replace(holder);
                            if (left) {
                                holder.add(toSelect);
                                holder.add(oldEq);
                            } else {
                                holder.add(oldEq);
                                holder.add(toSelect);
                            }
                            toSelect.setSelected(true);
                        }
                    }
                }
            }
            selectingSet = new HashSet<Equation>();
            return;


        }else {
            selectSet();
        }
    }
    // returns the equation left of the selected
    public Equation left(){
        return selected.left();
    }

    public void insert(Equation newEq) {
        Equation l = left();
        if (l != null) {
            if (l.parent instanceof WritingEquation) {
                if (l instanceof PlaceholderEquation) {
                    // this really should not happen
                    Log.e("", "yoyo");
                    //int at = emilyView.selected.parent.indexOf(emilyView.selected);
                    //emilyView.selected.parent.add(at, new WritingLeafEquation("+", emilyView));
                } else {
                    int at = l.parent.indexOf(l);
                    l.parent.add(at + 1, newEq);
                }
            }
        }else{
            if (selected instanceof PlaceholderEquation){
                selected.parent.add(0, newEq);
            }
        }
    }

    public void insertAt(Equation addTo ,int at, Equation newEq) {

        // we just move selected and than call insert
        if (selected instanceof PlaceholderEquation){
            Equation placeHolder = selected;
            selected.remove();
            addTo.add(at,placeHolder);
            insert(newEq);
        }
    }
}
