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
	private int id;
	private static int idBacker=0;
	

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDisplay(int pos) {
		return display;
	}

	public Equation(SuperView owner2) {
		owner = owner2;
		id = idBacker++;

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
		Equation current = equation;
		while (true){
			if (current.equals(this)){
				return true;
			}if (current.parent == null){
				return false;
			}else{
				current = current.parent;
			}
		}
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
		Equation at = this;
		while (true) {
			if (at==null){
				@SuppressWarnings("unused")
				int dug = 1+1;
			}
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
		while (true) {
			if (current.equals(this)) {
				return true;
			} else if (!(current instanceof AddEquation || current.equals(equation))) {
				return false;
			} else {
				current = current.parent;
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
		while (true) {
			if (current.equals(this)) {
				return true;
			} else if (!(current instanceof MultiDivSuperEquation || current.equals(equation))) {
				return false;
			} else {
				current = current.parent;
			}
		}
	}

	public void replace(Equation eq) {
		int index = parent.indexOf(this);
		this.parent.set(index, eq);
		eq.parent = this.parent;
		if (this.selected){
			eq.setSelected(true);
		}

		if (this instanceof LeafEquation && eq instanceof LeafEquation) {
			((LeafEquation) eq).negative = ((LeafEquation) this).negative;
			// TODO parathesis?
		}
	}
	
	@Override
	public boolean equals(Object other){
		if (other instanceof Equation){
			return ((Equation)other).hashCode() == hashCode();
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return id;
	}

	public void remove() {
		//TODO if we kill the selected we should handle that
		
		if (parent instanceof EqualsEquation){
			int myIndex = parent.indexOf(this);
			parent.remove(this);
			//TODO this is only sort right
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
		return getEquals().side(this);
	}
	
	public enum Op {ADD,DIV,MULTI}
	
	boolean tryOp(DragEquation dragging,boolean right, Op op){
		Log.i("try",this.hashCode()+" "+ this.display);
		
		if (parent.indexOf(this) == -1){
			Log.i("","dead on arival");
		}
		boolean can = false;
		if (op == Op.ADD){
			can = CanAdd(dragging);
		}else if (op == Op.MULTI){
			can = canMuli(dragging);
		}else if (op == Op.DIV){
			can = canDiv(dragging);
		}
		
		if (can){
			if (op == Op.ADD && side() != dragging.demo.side()){
				dragging.demo.negative = !dragging.demo.negative;
			}
			dragging.demo.remove();
			if ((parent instanceof AddEquation && op == Op.ADD) || 
					(parent instanceof MultiEquation && op == Op.MULTI)){
				int myIndex = parent.indexOf(this);
				if (dragging.demo.x < x){
					parent.add(myIndex+1,dragging.demo);
				}else{
					parent.add(myIndex,dragging.demo);
				}
			}else{
				Equation oldEq = this;
				Equation newEq = null;
				if (op == Op.ADD){
					newEq = new AddEquation(owner);
				}else if (op == Op.DIV){
					newEq = new DivEquation(owner);
				}else if (op == Op.MULTI){
					newEq = new MultiEquation(owner);
				}
				
				if (oldEq.parentheses){
					oldEq.parentheses = false;
					newEq.parentheses = true;
				}
				if (op != Op.DIV){
					
					oldEq.replace(newEq);
					if (right){
						newEq.add(dragging.demo);
						newEq.add(oldEq);
					}else{
						newEq.add(oldEq);
						newEq.add(dragging.demo);
					}
				}else{
					oldEq.replace(newEq);
					newEq.add(oldEq);
					newEq.add(dragging.demo);
				}
				
			}
			return true;
		}
		if (parent instanceof EqualsEquation){
			return false;
		}
		return parent.tryOp( dragging, right, op);
	}
	
	private boolean CanAdd(DragEquation dragging) {
		Equation lcc = lowestCommonContainer(dragging.demo);
		
		// if these are in the same add block
		if (lcc instanceof AddEquation && lcc.addContain(this) && lcc.addContain(dragging.demo)){
			return true;
		}
		// if they are only both only adds away form equals
		if (lcc instanceof EqualsEquation){
			EqualsEquation ee = (EqualsEquation) lcc;
			if (ee.addContain(dragging.demo) && ee.addContain(this)){
				return true;
			}
		}
		return false;
	}
	
	public boolean canMuli(DragEquation dragging) {
		return canMultiDiv(dragging,true);
	}
	
	public boolean canDiv(DragEquation dragging) {
		return canMultiDiv(dragging,false);
	}

	private boolean canMultiDiv(DragEquation dragging, boolean multi) {
		boolean result = false;
		Equation lcc = lowestCommonContainer(dragging.demo);
		// if these are in the same multi block
		if (lcc instanceof MultiDivSuperEquation && lcc.DivMultiContain(this) && lcc.DivMultiContain(dragging.demo)){
				MultiDivSuperEquation lccmdse = (MultiDivSuperEquation) lcc;
				result = ((lccmdse.onTop(this) == lccmdse.onTop(dragging.demo)) == multi);
				if (!result){
					Log.i("","same sides, tops are wrong. multi: "+multi);
				}else{
					Log.i("","pass. multi: "+multi);
				}
				return result;
		}
		// if they are only div/multi away form the equals
		if (lcc instanceof EqualsEquation){
			EqualsEquation ee = (EqualsEquation) lcc;
			if (ee.DivMultiContain(dragging.demo) && ee.DivMultiContain(this)){
			
			// 1*2=_ the _ on top and multiDivContained by the equals	
			boolean myTop=true;
			if (ee.get(ee.side(this)) instanceof MultiDivSuperEquation){
				myTop= ((MultiDivSuperEquation) ee
					.get(ee.side(this))).onTop(this);
			}
			boolean eqTop=true;
			if (ee.get(ee.side(dragging.demo)) instanceof MultiDivSuperEquation){
				eqTop= ((MultiDivSuperEquation) ee
					.get(ee.side(dragging.demo))).onTop(dragging.demo);
			}
			result = ((myTop != eqTop) == multi);
			if (!result){
				Log.i("","opisite sides, tops are wrong. multi: "+multi);
			}else{
				Log.i("","pass. multi: "+multi);
			}
			return result;
		}
		}
		Log.i("","not div multi contained. multi: "+multi);
		return false;
	}
}
