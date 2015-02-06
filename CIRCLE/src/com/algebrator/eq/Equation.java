package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.ColinView;
import com.example.circle.DragLocation;
import com.example.circle.SuperView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

abstract public class Equation extends ArrayList<Equation> {
    protected static final float PARN_HEIGHT_ADDITION = 6;
    protected static final float PARN_WIDTH_ADDITION = 24;
    private static int idBacker = 0;
    public Equation parent;
    public Paint textPaint;
    public boolean demo = false;
    public float x = 0;
    public float y = 0;
    public ArrayList<Point> lastPoint = new ArrayList<Point>();
    protected String display = "";
    protected boolean selected = false;
    protected int myWidth;
    protected int myHeight;
    public SuperView owner;
    private int id;
    private int buffer = 10;

    public boolean parenthesis() {
        // are we an add inside a * or a -
        boolean result = this instanceof AddEquation && (this.parent instanceof MultiEquation || this.parent instanceof MinusEquation);
        // are we an a the first element of a ^
        if (owner instanceof ColinView) {
            result = result || (this.parent instanceof PowerEquation && this.parent.indexOf(this) == 0 && this.size() != 0);
            //result = result || (this.parent instanceof MultiEquation && (this instanceof MinusEquation || this instanceof PlusMinusEquation));
        }
        return result;
    }

    public Equation(SuperView owner2) {
        owner = owner2;
        id = idBacker++;

        textPaint = new Paint(Algebrator.getAlgebrator().textPaint);
        // probably not needed
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setSubpixelText(true);
    }

    // we could template this in C++ can we in java?

    public void integrityCheck() {
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDisplay(int pos) {
        return display;
    }

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

    public boolean tryFlatten() {
        if (((this instanceof WritingEquation && this.parent instanceof WritingEquation) ||
                (this instanceof AddEquation && this.parent instanceof AddEquation) ||
                (this instanceof MultiEquation && this.parent instanceof MultiEquation))) {
            // add all the bits of this to it's parent
            int at = this.parent.indexOf(this);
            justRemove();
            for (Equation e : this) {
                parent.add(at++, e);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean add(Equation equation) {
        boolean result = super.add(equation);
        equation.parent = this;
        return result;
    }

    @Override
    public void add(int i, Equation equation) {
        super.add(i, equation);
        equation.parent = this;
    }

    //public abstract boolean isFlex();

    public ArrayList<EquationDis> closest(DragEquation dragging) {
        ArrayList<EquationDis> result = new ArrayList<EquationDis>();
        for (int i = 0; i < size(); i++) {
            //result.add(new EquationDis(get(i),x,y));
            result.addAll(get(i).closest(dragging));

        }
        if (this instanceof DivEquation) {
            result.add(new EquationDis(this, dragging, EquationDis.Side.left));
            result.add(new EquationDis(this, dragging, EquationDis.Side.right));
        }

        Collections.sort(result);
        return result;
    }

    public ArrayList<EquationDis> closest(float x, float y) {
        ArrayList<EquationDis> result = new ArrayList<EquationDis>();
        for (int i = 0; i < size(); i++) {
            //result.add(new EquationDis(get(i),x,y));
            result.addAll(get(i).closest(x, y));

        }
        if (this instanceof DivEquation) {
            result.add(new EquationDis(this, x, y, EquationDis.Side.left));
            result.add(new EquationDis(this, x, y, EquationDis.Side.right));
        }


        Collections.sort(result);
        return result;
    }

    public float measureWidth() {
        float totalWidth = 0;
        for (int i = 0; i < size() - 1; i++) {
            if (!(this instanceof MultiEquation) || (((MultiEquation) this).hasSign(i))) {
                totalWidth += myWidth;
            }
        }

        for (int i = 0; i < size(); i++) {
            totalWidth += get(i).measureWidth();
        }

        if (parenthesis()) {
            totalWidth += PARN_WIDTH_ADDITION;
        }

        return totalWidth;
    }

    public void draw(Canvas canvas, float x, float y) {
        this.x = x;
        this.y = y;
        //if (!demo){
        drawBkgBox(canvas, x, y);
        privateDraw(canvas, x, y);
        //}
        if (canvas == null) {
            Log.d("I updated myself: ", this.toString() + " , " + lastPoint.size());
        }
    }

    /**
     * x,y is the center of the equation to be drawn
     * if canvas is null it just updates the location but does not draw anything
     */
    protected void privateDraw(Canvas canvas, float x, float y) {
        lastPoint = new ArrayList<Point>();
        float totalWidth = measureWidth();
        float currentX = 0;
        Paint temp = getPaint();
        if (parenthesis()) {
            drawParentheses(canvas, x, y, temp);
            currentX += PARN_WIDTH_ADDITION / 2;
        }
        Rect out = new Rect();
        //we always want to center operation?
        textPaint.getTextBounds(display, 0, display.length(), out);
        float h = out.height();
        for (int i = 0; i < size(); i++) {
            float currentWidth = get(i).measureWidth();
            float currentHeight = get(i).measureHeight();
            get(i).draw(canvas,
                    x - (totalWidth / 2) + currentX + (currentWidth / 2), y);
            currentX += currentWidth;

            if (i != size() - 1) {
                if (!(this instanceof MultiEquation) || (((MultiEquation) this).hasSign(i))) {
                    Point point = new Point();
                    point.x = (int) (x - (totalWidth / 2) + currentX + (myWidth / 2));
                    point.y = (int) (y + (h / 2));
                    if (canvas != null) {
                        canvas.drawText(getDisplay(i + 1), point.x, point.y, temp);
                    }
                    lastPoint.add(point);
                    currentX += myWidth;
                } else {
                    Point point = new Point();
                    point.x = (int) (x - (totalWidth / 2) + currentX);
                    point.y = (int) (y + (h / 2));
                    lastPoint.add(point);
                }
            }
        }
    }


    public float measureHeightLower() {
        float totalHeight = myHeight / 2;

        for (int i = 0; i < size(); i++) {
            if (get(i).measureHeightLower() > totalHeight) {
                totalHeight = get(i).measureHeightLower();
            }
        }
        if (parenthesis()) {
            totalHeight += PARN_HEIGHT_ADDITION / 2f;
        }
        return totalHeight;
    }

    public float measureHeightUpper() {
        float totalHeight = myHeight / 2f;

        for (int i = 0; i < size(); i++) {
            if (get(i).measureHeightUpper() > totalHeight) {
                totalHeight = get(i).measureHeightUpper();
            }
        }
        if (parenthesis()) {
            totalHeight += PARN_HEIGHT_ADDITION / 2f;
        }
        return totalHeight;
    }

    public boolean deepContains(Equation equation) {
        Equation current = equation;
        while (true) {
            if (current.equals(this)) {
                return true;
            }
            if (current.parent == null) {
                return false;
            } else {
                current = current.parent;
            }
        }
    }

    public float measureHeight() {
        return measureHeightLower() + measureHeightUpper();
    }

    public HashSet<Equation> on(float x, float y) {
        HashSet<Equation> result = new HashSet<Equation>();
        for (int i = 0; i < lastPoint.size(); i++) {
            if (x < lastPoint.get(i).x + myWidth / 2
                    && x > lastPoint.get(i).x - myWidth / 2
                    && y < lastPoint.get(i).y + myHeight / 2
                    && y > lastPoint.get(i).y - myHeight / 2) {
                // we need to get the left
                if (this instanceof PowerEquation) {
                    result.add(get(1));
                } else {
                    Log.d("at: ", this.toString());
                    Log.d("at: ", this.lastPoint.size() + "");
                    Equation at = get(i);
                    while (at instanceof AddEquation || at instanceof MultiEquation || at instanceof MinusEquation) {
                        at = at.get(at.size() - 1);
                    }
                    result.add(at);
                    at = get(i + 1);
                    while (at instanceof AddEquation || at instanceof MultiEquation || at instanceof MinusEquation) {
                        at = at.get(0);
                    }
                    result.add(at);
                }
            }
        }
        return result;
    }

    public HashSet<Equation> OnAnyEqualsIncluded(float x, float y) {
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

    public HashSet<Equation> onAny(float x, float y) {
        HashSet<Equation> result = on(x, y);

        if (!(this instanceof EqualsEquation)) {

            if (result.size() != 0) {
                return result;
            }
        }
        for (int i = 0; i < size(); i++) {
            result = get(i).onAny(x, y);
            if (result.size() != 0) {
                return result;
            }
        }
        return result;
    }

    public boolean inBox(float x, float y) {
        float w = measureWidth();
        float h = measureHeight();
        if (x > (this.x + (w / 2) + buffer)) {
            return false;
        }
        if (x < (this.x - (w / 2) - buffer)) {
            return false;
        }
        if (y > (this.y + (h / 2) + buffer)) {
            return false;
        }
        if (y < (this.y - (h / 2) - buffer)) {
            return false;
        }
        return true;
    }

    public void tryOperator(float x, float y) {
        Object[] ons = on(x, y).toArray();
        Equation old = owner.stupid.copy();

        if (ons.length != 0) {
            ArrayList<Equation> onsList = new ArrayList<Equation>();
            String debug = "";
            for (Object o : ons) {
                Equation e = (Equation) o;
                if (o != this) {
                    int at = deepIndexOf(e);
                    Equation toAdd = get(at);
                    debug += toAdd.toString() + ",";
                    onsList.add(toAdd);
                }
            }
            Log.i("tryOperator", debug);
            if (onsList.size() != 0) {
                tryOperator(onsList);
            } else if (this instanceof MinusEquation) {
                tryOperator(onsList);
            }
        } else {
            for (Equation e : this) {
                e.tryOperator(x, y);
            }
        }

        if (owner instanceof ColinView && !(old.same(owner.stupid))) {
            ((ColinView) owner).changed = true;
        }
    }

    public void tryOperator(ArrayList<Equation> equation) {
    }

    float MIN_TEXT_SIZE = 12;

    protected float getScale(Equation e) {
        return 1f;
    }

    protected Paint getPaint() {
        Paint temp;
        if (parent != null) {
            temp = new Paint(parent.getPaint());
        } else {
            temp = new Paint(textPaint);
        }
        if (selected) {
            temp.setColor(Color.GREEN);
        }
        if (demo) {
            temp.setColor(Color.BLUE);
        }
        if (owner.selectingSet.contains(this)) {
            temp.setColor(Color.RED);
        }
        float targetTextSize = temp.getTextSize();
        if (parent != null) {
            targetTextSize *= parent.getScale(this);
            if (targetTextSize < MIN_TEXT_SIZE) {
                targetTextSize = MIN_TEXT_SIZE;
            }
        }

        temp.setTextSize(targetTextSize);

        return temp;
    }

    protected void drawBkgBox(Canvas canvas, float x, float y) {
        Rect r = new Rect((int) (x - measureWidth() / 2),
                (int) (y - measureHeightUpper()),
                (int) (x + measureWidth() / 2), (int) (y + measureHeightLower()));
        Paint p = new Paint();
        p.setAlpha(255 / 2);
        Random rand = new Random();
        p.setARGB(255 / 4, 255 / 2, 255 / 2, 255 / 2);
        if (canvas != null) {
            //canvas.drawRect(r, p);
        }

    }

    public Equation lowestCommonContainer(Equation eq) {
        // TODO slow
        Equation at = this;
        while (true) {
            if (at == null) {
                @SuppressWarnings("unused")
                int dug = 1 + 1;
            }
            if (at.deepContains(eq)) {
                return at;
            } else {
                at = at.parent;
            }
        }
    }

    protected void drawParentheses(Canvas canvas, float x, float y, Paint temp) {
        if (canvas != null) {
            Paint ptemp = new Paint(temp);
            ptemp.setStrokeWidth(3);
            float w = measureWidth();
            float h = measureHeight();
            //left side
            canvas.drawLine(x - (w / 2) + 3, y - (h / 2) + 3, x - (w / 2) + 9, y - (h / 2) + 3, ptemp);
            canvas.drawLine(x - (w / 2) + 3, y - (h / 2) + 3, x - (w / 2) + 3, y + (h / 2) - 3, ptemp);
            canvas.drawLine(x - (w / 2) + 3, y + (h / 2) - 3, x - (w / 2) + 9, y + (h / 2) - 3, ptemp);

            //right side
            canvas.drawLine(x + (w / 2) - 3, y - (h / 2) + 3, x + (w / 2) - 9, y - (h / 2) + 3, ptemp);
            canvas.drawLine(x + (w / 2) - 3, y - (h / 2) + 3, x + (w / 2) - 3, y + (h / 2) - 3, ptemp);
            canvas.drawLine(x + (w / 2) - 3, y + (h / 2) - 3, x + (w / 2) - 9, y + (h / 2) - 3, ptemp);
        }
    }


    public boolean addContain(Equation equation) {
        Equation current = equation;
        while (true) {
            if (current.equals(this)) {
                return true;
            } else if (!(current instanceof AddEquation || current.equals(equation) || current instanceof MinusEquation)) {
                return false;
            } else {
                current = current.parent;
            }
        }
    }

    public EqualsEquation getEquals() {
        Equation at = this.parent;
        while (!(at instanceof EqualsEquation)) {
            if (at instanceof WritingEquation) {
                return null;
            }
            at = at.parent;
        }
        return ((EqualsEquation) at);
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
            } else if (!(current instanceof MinusEquation || current instanceof MultiDivSuperEquation || current.equals(equation))) {
                return false;
            } else {
                current = current.parent;
            }
        }
    }

    @Override
    public boolean remove(Object e) {
        if (e instanceof Equation && this.contains(e)) {
            remove(indexOf(e));
            return true;
        }
        return false;
    }

    @Override
    public Equation remove(int pos) {
        Equation result = super.remove(pos);
        if (result != null) {
            if (this.size() == 1 && this.parent != null) {
                this.replace(get(0));
            } else if (size() == 0) {
                remove();
            }
        }
        return result;
    }

    @Override
    public Equation set(int index, Equation eq) {
        Equation result = super.set(index, eq);
        eq.parent = this;

        return result;

    }

    public boolean same(Equation eq) {
        if (!this.getClass().equals(eq.getClass())) {
            return false;
        }
        if (this.size() != eq.size()) {
            return false;
        }
        for (Equation e : eq) {
            boolean any = false;
            for (Equation ee : this) {
                if (ee.same(e)) {
                    any = true;
                    break;
                }
            }
            if (!any) {
                return false;
            }
        }
        return true;

    }

    public void replace(Equation eq) {
        if (parent != null) {
            int index = parent.indexOf(this);
            this.parent.set(index, eq);

        } else {
            owner.stupid = eq;
        }
        if (this.isSelected()) {
            eq.setSelected(true);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Equation) {
            return ((Equation) other).hashCode() == hashCode();
        }
        return false;
    }

    protected void operateRemove(ArrayList<Equation> ops) {
        for (Equation e : ops) {
            if (contains(e)) {
                e.justRemove();
            } else {
                e.remove();
            }
        }
    }

    public boolean reallyInstanceOf(Class t) {
        Equation at = this;
        while (at instanceof MinusEquation) {
            at = at.get(0);
        }
        return t.isInstance(at);
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void remove() {
        parent.remove(this);
    }

//	public boolean canPop() {
//		// we can remove if it is equals equations all the way up
//		Equation at = parent;
//		while (at instanceof AddEquation || at instanceof EqualsEquation){
//			if (at instanceof EqualsEquation){
//				return true;
//			}
//			at = at.parent;
//		}
//		
//		// or if multiDivSupers all the way up
//		// and if not like a/(b/c) can't remove b ... but we will let you
//		// if a/(b/c) you can drag c to be (a*c)/b
//		
//		at = parent;
//		while (at instanceof MultiDivSuperEquation || at instanceof EqualsEquation){
//			if (at instanceof EqualsEquation){
//				return true;
//			}
//			at = at.parent;
//		}
//		
//		return false;
//	}

    public void isDemo(boolean b) {
        if (b) {
            if (owner.dragging != null && owner.dragging.demo != null) {
                owner.dragging.demo.isDemo(false);
            }
            owner.dragging.demo = this;
            demo = true;
        } else {
            owner.dragging.demo = null;
            demo = false;
        }

    }

    public int side() {
        Equation equals = getEquals();
        if (equals != null) {
            return ((EqualsEquation) equals).side(this);
        } else {
            //find the root
            Equation at = this;
            while (at.parent != null) {
                at = at.parent;
            }
            if (at instanceof WritingEquation) {
                int ourIndex = at.deepIndexOf(this);
                int equalsIndex = -1;
                for (Equation e : at) {
                    if (e instanceof WritingLeafEquation && e.getDisplay(-1).equals("=")) {
                        equalsIndex = at.indexOf(e);
                    }
                }
                if (equalsIndex == -1) {
                    Log.e("", "it should really have an equals");
                    return 0;
                }
                if (ourIndex == equalsIndex) {
                    Log.e("", "we should really not be checking the side of the equals");
                    return 0;
                }
                return (ourIndex < equalsIndex ? 0 : 1);
            } else {
                Log.e("", "this is bad");
                return 0;
            }
        }
    }

    public void integrityCheckOuter() {
        integrityCheck();
        if (parent != null) {
            if (!parent.deepContains(this)) {
                Log.e("ic", "parent does not contain this");
            }
        } else if (!(this instanceof EqualsEquation || this instanceof WritingEquation)) {
            Log.e("ic", "has no parent");
        }
        for (Equation e : this) {
            e.integrityCheckOuter();
        }
    }

    public void fixIntegrety() {
        for (Equation e : this) {
            e.fixIntegrety();
        }
    }

    public void updateOwner(SuperView sv) {
        owner = sv;
        for (Equation e : this) {
            e.updateOwner(sv);
        }
    }

    public Equation left() {
        return next(true);
    }

    public Equation right() {
        return next(false);
    }

    private Equation next(boolean left) {
        Equation at = this;
        if (at.parent == null) {
            return null;
        }
        while (at.parent.indexOf(at) == (left ? 0 : (at.parent.size() - 1))) {
            at = at.parent;
            if (at.parent == null) {
                return null;
            }
        }
        return at.parent.get(at.parent.indexOf(at) + (left ? -1 : 1));
    }

    public Integer deepIndexOf(Equation eq) {
        Equation at = eq;
        if (at.parent == null) {
            debug();
        }
        while (at.parent != this) {
            at = at.parent;
            if (at.parent == null) {
                debug();
            }
        }
        int result = indexOf(at);
        if (result == -1) {
            Log.i("cgrrr", "oh man is that bad");
        }
        return result;
    }

    private void debug() {

    }

    // returns null on sucess
    public ArrayList<EquationDis> tryOp(Equation dragging, boolean right, Op op) {
        Log.i("try", this.hashCode() + " " + this.display);

        if (parent.indexOf(this) == -1) {
            Log.i("", "dead on arival");
        }
        boolean can = false;
        if (op == Op.ADD) {
            can = CanAdd(dragging);
        } else if (op == Op.MULTI) {
            can = canMuli(dragging);
        } else if (op == Op.DIV) {
            can = canDiv(dragging);
        } else if (op == Op.POWER) {
            can = canPower(dragging);
        }

        if (can) {
            boolean sameSide = (op == Op.ADD && side() != dragging.side());
            //peel off the minus signs
            ArrayList<MinusEquation> minusSigns = new ArrayList<MinusEquation>();
            while (this.parent instanceof MinusEquation) {
                minusSigns.add((MinusEquation) this.parent);
                this.parent.replace(this);
            }

            if (op == Op.POWER) {
                dragging.remove();
                Equation power = Operations.flip(dragging);
                Equation newEq = new PowerEquation(owner);
                Equation oldEq = this;
                oldEq.replace(newEq);
                newEq.add(this);
                newEq.add(power);
                //dragging.getAndUpdateDemo(power);
                //dragging.updateOps(dragging.demo);
            } else if (this instanceof NumConstEquation && ((NumConstEquation) this).getValue() == 0 && op == Op.ADD) {
                dragging.remove();
                dragging = update(dragging,sameSide);
                this.replace(dragging);
                return null;
            } else if (this instanceof NumConstEquation && ((NumConstEquation) this).getValue() == 1 && op == Op.MULTI) {
                dragging.remove();
                dragging = update(dragging,sameSide);
                this.replace(dragging);
                // bring back the minus signs on demo
                for (MinusEquation me : minusSigns) {
                    me.clear();
                    dragging.replace(me);
                    me.add(dragging);
                }
                return null;
            } else if ((parent instanceof AddEquation && op == Op.ADD) ||
                    (parent instanceof MultiEquation && op == Op.MULTI)) {
                if (parent.equals(dragging.parent)) {
                    dragging.justRemove();
                } else {
                    dragging.remove();
                }
                int myIndex = parent.indexOf(this);
                Log.i("", "added to existing");
                if (!right) {
                    dragging = update(dragging,sameSide);
                    parent.add(myIndex + 1, dragging);
                } else {
                    dragging = update(dragging,sameSide);
                    parent.add(myIndex, dragging);
                }
            } else {
                if ((op == Op.DIV || op == Op.MULTI) && dragging.parent instanceof EqualsEquation) {
                    dragging.replace(new NumConstEquation(1, owner));
                } else {
                    dragging.remove();
                }
                Log.i("added to new", "" + this.toString());
                Equation oldEq = this;
                Equation newEq = null;
                if (op == Op.ADD) {
                    newEq = new AddEquation(owner);
                } else if (op == Op.DIV) {
                    newEq = new DivEquation(owner);
                } else if (op == Op.MULTI) {
                    newEq = new MultiEquation(owner);
                }

                if (op != Op.DIV) {
                    oldEq.replace(newEq);
                    if (right) {
                        dragging = update(dragging,sameSide);
                        newEq.add(dragging);
                        newEq.add(oldEq);
                    } else {
                        newEq.add(oldEq);
                        dragging = update(dragging,sameSide);
                        newEq.add(dragging);
                    }
                } else {
                    oldEq.replace(newEq);
                    newEq.add(oldEq);
                    dragging = update(dragging,sameSide);
                    newEq.add(dragging);
                }
            }
            // bring back the minus signs
            Equation at = this;
            for (MinusEquation me : minusSigns) {
                me.clear();
                at.replace(me);
                me.add(at);
            }
            return null;
        }
        return new ArrayList<EquationDis>();
    }

    private Equation update(Equation dragging, boolean sameSide) {
        Equation toInsert;
        if (sameSide) {
            if (dragging instanceof MinusEquation) {
                toInsert = dragging.get(0);
            } else {
                toInsert = new MinusEquation(dragging.owner);
                toInsert.add(dragging);
            }
        } else {
            toInsert = dragging;
        }
        return toInsert;
    }

    private boolean canPower(Equation dragging) {
        if (dragging.parent instanceof PowerEquation && dragging.parent.parent instanceof EqualsEquation) {
            if (this.parent instanceof EqualsEquation && dragging.side() != this.side()) {
                return true;
            }
        }
        return false;
    }

    public void justRemove() {
        parent.justRemove(this);
    }

    public void justRemove(Equation equation) {
        super.remove(equation);
    }

    private boolean CanAdd(Equation dragging) {
        Equation lcc = lowestCommonContainer(dragging);

        // if these are in the same add block
        if (lcc instanceof AddEquation && lcc.addContain(this) && lcc.addContain(dragging)) {
            return true;
        }
        // if they are only both only adds away form equals
        if (lcc instanceof EqualsEquation) {
            EqualsEquation ee = (EqualsEquation) lcc;
            if (ee.addContain(dragging) && ee.addContain(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean canMuli(Equation dragging) {
        return canMultiDiv(dragging, true);
    }

    public boolean canDiv(Equation dragging) {
        return canMultiDiv(dragging, false);
    }

    @Override
    public String toString() {
        String internals = "";
        for (int i = 0; i < size(); i++) {
            internals += get(i).toString();
            if (i != size() - 1) {
                internals += ",";
            }
        }
        if (internals != "") {
            internals = "(" + internals + ")";
        }
        return display + internals;
    }

    private boolean canMultiDiv(Equation dragging, boolean multi) {
        boolean result = false;
        Equation lcc = lowestCommonContainer(dragging);
        // if these are in the same multi block
        if (lcc instanceof MultiDivSuperEquation && lcc.DivMultiContain(this) && lcc.DivMultiContain(dragging)) {
            MultiDivSuperEquation lccmdse = (MultiDivSuperEquation) lcc;
            result = ((lccmdse.onTop(this) == lccmdse.onTop(dragging)) == multi);
            if (!result) {
                Log.i("", "same sides, tops are wrong. multi: " + multi);
            } else {
                Log.i("", "pass. multi: " + multi);
            }
            return result;
        }
        // if they are only div/multi away form the equals
        if (lcc instanceof EqualsEquation) {
            EqualsEquation ee = (EqualsEquation) lcc;
            if (ee.DivMultiContain(dragging) && ee.DivMultiContain(this)) {

                // 1*2=_ the _ on top and multiDivContained by the equals
                boolean myTop = true;
                if (ee.get(ee.side(this)) instanceof MultiDivSuperEquation) {
                    myTop = ((MultiDivSuperEquation) ee
                            .get(ee.side(this))).onTop(this);
                }
                boolean eqTop = true;
                if (ee.get(ee.side(dragging)) instanceof MultiDivSuperEquation) {
                    eqTop = ((MultiDivSuperEquation) ee
                            .get(ee.side(dragging))).onTop(dragging);
                }
                result = ((myTop != eqTop) == multi);
                if (!result) {
                    Log.i("", "opisite sides, tops are wrong. multi: " + multi);
                } else {
                    Log.i("", "pass. multi: " + multi);
                }
                return result;
            }
        }
        Log.i("", "not div multi contained. multi: " + multi);
        return false;
    }

    public void updateLocation() {
        draw(null, x, y);
    }


    public void getDragLocations(Equation dragging, DragLocations dragLocations, ArrayList<Op> ops) {
        if (this.parent != null && !dragging.deepContains(this)) {
            if (ops.contains(Op.MULTI) && canMuli(dragging) && !(this instanceof MultiEquation)) {
                dragLocations.add(new DragLocation(Op.MULTI, dragging, this, true));
                dragLocations.add(new DragLocation(Op.MULTI, dragging, this, false));
            }
            if (ops.contains(Op.ADD) && CanAdd(dragging)&& !(this instanceof AddEquation)) {
                dragLocations.add(new DragLocation(Op.ADD, dragging, this, true));
                dragLocations.add(new DragLocation(Op.ADD, dragging, this, false));
            }
            if (ops.contains(Op.DIV) && canDiv(dragging)) {
                DragLocation newset = new DragLocation(Op.DIV, dragging, this, false);
                dragLocations.add(newset);
            }
            if (ops.contains(Op.POWER) && canPower(dragging)) {
                DragLocation newset = new DragLocation(Op.POWER, dragging, this, false);
                dragLocations.add(newset);
            }

        }
        if (dragging.equals(this)){
            // left and op
            DragLocation newset = new DragLocation(this);
            dragLocations.add(newset);
        }else {
            for (Equation e : this) {
                e.getDragLocations(dragging, dragLocations, ops);
            }
        }
    }

    public enum Op {ADD, DIV, POWER, MULTI}
}
