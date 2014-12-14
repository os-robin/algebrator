package com.algebrator.eq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

abstract public class Equation extends ArrayList<Equation> {
	protected static final int DEFAULT_SIZE = 50;
	public Equation parent;
	protected String display = "";
	Paint textPaint;
	SuperView owner;
	public boolean parentheses;
	protected boolean selected = false;
	public boolean negative = false;
	private boolean demo = false;
	public float x=0;
	public float y=0;
	public ArrayList<Point> lastPoint = new ArrayList<Point>();
	protected int myWidth;
	protected int myHeight;
	

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDisplay(int pos) {
		return display;
	}

	public Equation(SuperView owner2) {
		owner = owner2;

		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xff000000);
	}

	// we could template this in C++ can we in java?
	/**
	 * makes a copy the entire equation tree below and including this node good
	 * for showing work
	 * 
	 * @return
	 */
	public abstract Equation copy();

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {

		if (selected) {
			if (owner.selected != null) {
				owner.selected.setSelected(false);
			}
			owner.selected = this;
		} else if (owner.selected.equals(this)) {
			owner.selected = null;
		}
		this.selected = selected;
	}

	@Override
	public boolean add(Equation equation) {
		boolean result = super.add(equation);
		equation.parent = this;
		return result;
	}
	
	@Override
	public void add(int i,Equation equation) {
		super.add(i,equation);
		equation.parent = this;
	}

	public boolean canMulti(Equation eq) {
		if (eq.equals(this)) {
			return false;
		}
		Equation lcc = lowestCommonContainer(eq);
		if (lcc.DivMultiContain(this) && lcc.DivMultiContain(eq)) {
			// if these are on different side
			if (lcc instanceof EqualsEquation) {
				// we need them to have different topnesses
				EqualsEquation top = (EqualsEquation) lcc;
				MultiDivSuperEquation myTop = (MultiDivSuperEquation) top
						.get(top.side(this));
				MultiDivSuperEquation eqTop = (MultiDivSuperEquation) top
						.get(top.side(eq));
				return myTop.onTop(this) != eqTop.onTop(eq);
			} else {
				// if they are on the same side they need the same topness
				MultiDivSuperEquation lccmdse = (MultiDivSuperEquation) lcc;
				return lccmdse.onTop(this) == lccmdse.onTop(eq);
			}
		}
		return false;
	}

	public boolean canDiv(Equation eq) {
		if (eq.equals(this)) {
			return false;
		}
		Equation lcc = lowestCommonContainer(eq);
		if (lcc.DivMultiContain(this) && lcc.DivMultiContain(eq)) {
			// if these are on different side
			if (lcc instanceof EqualsEquation) {
				// we need them to have the same topnesses
				EqualsEquation top = (EqualsEquation) lcc;
				MultiDivSuperEquation myTop = (MultiDivSuperEquation) top
						.get(top.side(this));
				MultiDivSuperEquation eqTop = (MultiDivSuperEquation) top
						.get(top.side(eq));
				return myTop.onTop(this) == eqTop.onTop(eq);
			} else {
				// if they are on the same side they to have different topness
				MultiDivSuperEquation lccmdse = (MultiDivSuperEquation) lcc;
				return lccmdse.onTop(this) != lccmdse.onTop(eq);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<EquationDis> closest(float x, float y) {
		ArrayList<EquationDis> result = new ArrayList<EquationDis>();
		for (int i = 0; i < size(); i++) {
				//result.add(new EquationDis(get(i),x,y));
				result.addAll(get(i).closest(x, y));

		}
		Collections.sort(result);
		return result;
	}

	public abstract boolean isFlex();

	public float measureWidth(){
		float totalWidth = (size() - 1) * myWidth;

		for (int i = 0; i < size(); i++) {
			totalWidth += get(i).measureWidth();
		}

		if (parentheses) {
			totalWidth += PARN_WIDTH_ADDITION;
		}

		return totalWidth;
	}

	public void draw(Canvas canvas, float x, float y){
		this.x=x;
		this.y=y;
		if (!demo){
			privateDraw(canvas,x,y);
		}
	}
	
	/**
	 * x,y is the center of the equation to be drawn
	 */
	protected void privateDraw(Canvas canvas, float x, float y){
		drawBkgBox(canvas, x, y);
		lastPoint = new ArrayList<Point>();
		float totalWidth = measureWidth();
		float currentX = 0;
		Paint temp = getPaint();
		if (parentheses) {
			drawParentheses(canvas, x, y, temp);
			currentX+= PARN_WIDTH_ADDITION/2;
		}
		Rect out = new Rect();
		textPaint.getTextBounds(display, 0, display.length(), out);
		float h = out.height();
		for (int i = 0; i < size(); i++) {
			float currentWidth = get(i).measureWidth();
			float currentHeight = get(i).measureHeight();
			get(i).draw(canvas,
					x - (totalWidth / 2) + currentX + (currentWidth / 2), y);
			currentX += currentWidth;
			if (i != size() - 1) {
				Point point = new Point();
				point.x = (int) (x - (totalWidth / 2) + currentX + (myWidth / 2));
				point.y = (int) (y + (h / 2));
				canvas.drawText(getDisplay(i + 1), point.x, point.y, temp);

				lastPoint.add(point);
				currentX += myWidth;
			}
		}
	}

	public boolean deepContains(Equation equation) {
		if (contains(equation)) {
			return true;
		}
		for (int i = 0; i < size(); i++) {
			if (get(i).deepContains(equation)) {
				return true;
			}
		}
		return false;
	}

	public float measureHeight(){
		float totalHeight = myHeight;

		for (int i = 0; i < size(); i++) {
			if (get(i).measureHeight() > totalHeight) {
				totalHeight = get(i).measureHeight();
			}
		}
		if (parentheses) {
			totalHeight += PARN_HEIGHT_ADDITION;
		}

		return totalHeight;
	}

	public HashSet<Equation> on(float x, float y) {
		Log.i("yo,yo", x + "," + y);
		HashSet<Equation> result = new HashSet<Equation>();
		for (int i = 0; i < lastPoint.size(); i++) {
			if (x < lastPoint.get(i).x + myWidth / 2
					&& x > lastPoint.get(i).x - myWidth / 2
					&& y < lastPoint.get(i).y + myHeight / 2
					&& y > lastPoint.get(i).y - myHeight / 2) {
				result.add(get(i));
				result.add(get(i + 1));
			}
		}
		return result;
	}

	public HashSet<Equation> onAny(float x, float y) {
		HashSet<Equation> result = on(x, y);
		if (result.size() != 0) {
			return result;
		}
		for (int i = 0; i < size(); i++) {
			result = get(i).onAny(x, y);
			if (result.size() != 0) {
				return result;
			}
		}
		return result;
	}

	protected Paint getPaint() {
		Paint temp;
		if (parent!= null){
			temp = parent.getPaint();
		}else{
			temp = textPaint;
		}
		if (selected) {
			temp = new Paint(textPaint);
			temp.setColor(Color.GREEN);
		}
		if (demo) {
			temp = new Paint(textPaint);
			temp.setColor(Color.BLUE);
		}
		if (owner.selectingSet.contains(this)) {
			temp = new Paint(textPaint);
			temp.setColor(Color.RED);
		}
		return temp;
	}

	protected void drawBkgBox(Canvas canvas, float x, float y) {
		Rect r = new Rect((int) (x - measureWidth() / 2),
				(int) (y - measureHeight() / 2),
				(int) (x + measureWidth() / 2), (int) (y + measureHeight() / 2));
		Paint p = new Paint();
		p.setAlpha(255 / 2);
		Random rand = new Random();
		p.setARGB(255 / 4, 255 / 2, 255 / 2, 255 / 2);
		canvas.drawRect(r, p);

	}

	public Equation lowestCommonContainer(Equation eq) {
		// TODO slow
		if (parent == null) {
			return this;
		}
		Equation at = parent;
		while (true) {
			if (at.deepContains(eq)) {
				return at;
			} else {
				at = at.parent;
			}
		}
	}

	protected static final float PARN_HEIGHT_ADDITION = 6;
	protected static final float PARN_WIDTH_ADDITION=24;
	protected void drawParentheses(Canvas canvas, float x, float y, Paint temp) {
		Paint ptemp = new Paint(temp);
		ptemp.setStrokeWidth(3);
		float w = measureWidth();
		float h = measureHeight();
		//left side
		canvas.drawLine(x-(w/2)+3, y-(h/2)+3, x-(w/2)+9, y-(h/2)+3, ptemp);
		canvas.drawLine(x-(w/2)+3, y-(h/2)+3, x-(w/2)+3, y+(h/2)-3, ptemp);
		canvas.drawLine(x-(w/2)+3, y+(h/2)-3, x-(w/2)+9, y+(h/2)-3, ptemp);
		
		//right side
		canvas.drawLine(x+(w/2)-3, y-(h/2)+3, x+(w/2)-9, y-(h/2)+3, ptemp);
		canvas.drawLine(x+(w/2)-3, y-(h/2)+3, x+(w/2)-3, y+(h/2)-3, ptemp);
		canvas.drawLine(x+(w/2)-3, y+(h/2)-3, x+(w/2)-9, y+(h/2)-3, ptemp);
	}

	public boolean addContain(Equation equation) {
		Equation current = equation;
		if (equation.equals(this)) {
			if (this instanceof AddEquation || this instanceof EqualsEquation) {
				return true;
			} else {
				return false;
			}
		}
		while (true) {
			if (current.parent.equals(this)) {
				return true;
			} else if (current.parent instanceof AddEquation) { // TODO or ()
				current = current.parent;
			} else {
				return false;
			}
		}
	}
	
	public EqualsEquation getEquals() {
		Equation at = this.parent;
		while (!(at instanceof EqualsEquation)){
			at = at.parent;
		}
		return ((EqualsEquation)at);
	}

	/**
	 * check to see if all the generations between an equation and this are
	 * divEquations or MultiEquations equations are considered to contain
	 * themselves
	 * 
	 * @param equation
	 * @return
	 */
	public boolean DivMultiContain(Equation equation) {
		Equation current = equation;
		if (equation.equals(this)) {
			if (this instanceof MultiDivSuperEquation
					|| this instanceof EqualsEquation) {
				return true;
			} else {
				return false;
			}
		}
		while (true) {
			if (current.parent.equals(this)) {
				return true;
			} else if (current.parent instanceof MultiDivSuperEquation) {
				current = current.parent;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean equals(Object x) {
		return this == x;
	}

	public void replace(Equation eq) {
		int index = parent.indexOf(this);
		this.parent.set(index, eq);
		eq.parent = this.parent;
		eq.setSelected(true);

		if (this instanceof LeafEquation && eq instanceof LeafEquation) {
			((LeafEquation) eq).negative = ((LeafEquation) this).negative;
			// TODO parathesis?
		}
	}

	public void remove() {
		
		if (parent instanceof EqualsEquation){
			int myIndex = parent.indexOf(this);
			parent.remove(this);
			NumConstEquation num = new NumConstEquation("0", owner);
			parent.add(myIndex,num);
		}else{
			parent.remove(this);
			if (parent.size() == 1) {
				parent.replace(parent.get(0));
			} else if (parent.size() == 0) {
				parent.remove();
			}
		}
	}

	public boolean canPop() {
		// we can remove if it is equals equations all the way up
		Equation at = parent;
		while (at instanceof AddEquation || at instanceof EqualsEquation){
			if (at instanceof EqualsEquation){
				return true;
			}
			at = at.parent;
		}
		
		// or if multiDivSupers all the way up
		// and if not like a/(b/c) can't remove b ... but we will let you
		// if a/(b/c) you can drag c to be (a*c)/b
		
		at = parent;
		while (at instanceof MultiDivSuperEquation || at instanceof EqualsEquation){
			if (at instanceof EqualsEquation){
				return true;
			}
			at = at.parent;
		}
		
		return false;
	}

	public Equation pop() {
		remove();
		return this;
	}

	public void isDemo(boolean b) {
		if (b){
			if (owner.demo !=null){
				owner.demo.isDemo(false);
			}
			owner.demo = this;
			demo = true;
		}else{
			owner.demo = null;
			demo = false;
		}
		
	}

	public int side() {
		Equation at = this.parent;
		while (!(at instanceof EqualsEquation)){
			at = at.parent;
		}
		return ((EqualsEquation)at).side(this);
	}
}
