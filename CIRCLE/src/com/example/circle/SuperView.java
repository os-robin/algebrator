package com.example.circle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import com.algebrator.eq.AddEquation;
import com.algebrator.eq.DragEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.EquationDis;
import com.algebrator.eq.MinusEquation;
import com.algebrator.eq.MultiEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;
import com.algebrator.eq.WritingLeafEquation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public abstract class SuperView extends View implements
        OnTouchListener {//Runnable,
    public Equation selected;
    public DragEquation dragging;
    //SurfaceHolder surfaceHolder;
    Thread thread = null;
    volatile boolean running = false;
    public Equation stupid;
    int width;
    int height;
    int offsetX = 0;
    int offsetY = 0;
    protected float buttonsPercent;
    public ArrayList<Animation> animation = new ArrayList<Animation>();


    protected float buttonLine() {
        return height * buttonsPercent;
    }

    public TextPaint text = new TextPaint();

    int highlight;
    Paint bkg;

    ArrayList<Button> buttons = new ArrayList<Button>();

    public SuperView(Context context) {
        super(context);
        init(context);
    }

    public SuperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SuperView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        //surfaceHolder = getHolder();
        this.setOnTouchListener(this);

        measureScreen(context);

        text.setTextSize(Algebrator.getAlgebrator().TEXT_SIZE);
        //text.setAntiAlias(true);
        //text.setDither(true);
        //text.setSubpixelText(true);
        text.setTextAlign(Align.CENTER);
        text.setColor(0xff000000);

        highlight = Algebrator.getAlgebrator().highLight;

        bkg = new Paint();
        bkg.setTextSize(Algebrator.getAlgebrator().TEXT_SIZE);
        bkg.setTextAlign(Align.CENTER);
        bkg.setColor(Algebrator.getAlgebrator().mainColor);

        Log.i("lifeCycle", "SuperView-init");
    }

    public void measureScreen(Context context) {
        // Get the height of the whole display
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        int screenheight = metrics.heightPixels; // get screen height (dependent
        // on rotation)
        int screenwidth = metrics.widthPixels; // get screen width (dependent on
        // rotation)

        Log.i("screenheight, screenwidth", screenheight + "," + screenwidth);

        // Get the heights of status, title, decorations, etc.
        Window win = ((Activity) context).getWindow();
        Rect rect = new Rect();

        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top; // Get the height of the status bar
        int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT)
                .getTop(); // Get height occupied by decoration contents
        int titleBarHeight = contentViewTop - statusBarHeight;

        Log.i("statusBarHeight, contentViewTop, titleBarHeight",
                statusBarHeight + "," + contentViewTop + "," + titleBarHeight);

        // Get navigation bar height
        int navBarHeight = 0;
        /*
		 * Resources resources = context.getResources(); int resourceId =
		 * resources.getIdentifier("navigation_bar_height", "dimen", "android");
		 * if (resourceId > 0) { navBarHeight =
		 * resources.getDimensionPixelSize(resourceId); }
		 *
		 * Log.i("navBarHeight", navBarHeight+"");
		 */

        // Calculate actual height
        height = screenheight
                - (titleBarHeight + statusBarHeight + navBarHeight);
        width = screenwidth;

        Log.i("actual height, width", height + ", " + width);

    }

//	@Override
//	public synchronized void run() {
//		while (running) {
//			if (surfaceHolder.getSurface().isValid()) {
//				Canvas canvas = surfaceHolder.lockCanvas();
//				myDraw(canvas);
//				surfaceHolder.unlockCanvasAndPost(canvas);
//				try {
//					wait(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

    private String lastLog = "";

    private float friction = 0.9f;

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // canvas.drawColor(0xFFFFFFFF, Mode.CLEAR);
        canvas.drawColor(0xFFFFFFFF, Mode.ADD);


        if (dragging != null) {
            dragging.eq.draw(canvas, dragging.eq.x, dragging.eq.y);
        }

        if (canDrag) {
            if (lastLongTouch != null && lastLongTouch.started()) {
                if (lastLongTouch.done()) {
                    Log.i("lastLongTouch", "done");
                    animation.add(new DragStarted(this, 0x7f));
                    startDragging();
                    lastLongTouch = null;
                } else {
                    drawProgress(canvas, lastLongTouch.percent(), 0x7f);
                    Log.i("lastLongTouch", lastLongTouch.percent() + "");
                }
            }
        }

        for (int i = 0; i < animation.size(); i++) {
            animation.get(i).draw(canvas);
        }

        // keep selected on the screen
        float targetV = 4.0f;

        if (selected != null) {
            //TODO scale by dpi
            int buffer = 100;
            if (selected.x + buffer > width) {
                if (vx > -targetV) {
                    vx = -targetV;
                    slidding = true;
                }
            }


            if (selected.y + buffer > buttonLine()) {
                if (vy > -targetV) {
                    vy = -targetV;
                    slidding = true;
                }
            }

            if (selected.x - buffer < 0) {
                if (vx < targetV) {
                    vx = targetV;
                    slidding = true;
                }
            }

            if (selected.y - buffer < 0) {
                if (vy < targetV) {
                    vy = targetV;
                    slidding = true;
                }
            }
        }

        if (slidding) {
            //Log.i("",vx +","+ vy);
            vx *= friction;
            vy *= friction;
            updateOffsetX(vx);
            updateOffsetY(vy);
            if (vx == 0 && vy == 0) {
                slidding = false;
            }
        }

        stupid.draw(canvas, width / 2 + offsetX, height / 3 + offsetY);
        stupid.integrityCheckOuter();
        if (!stupid.toString().equals(lastLog)) {
            Log.i("", stupid.toString());
            lastLog = stupid.toString();
        }

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).draw(canvas);
        }

        invalidate();
    }

    public void drawProgress(Canvas canvas, float percent, float startAt) {
        Paint p = new Paint();
        p.setColor(Algebrator.getAlgebrator().highLight - 0xff000000);
        float targetAlpha = startAt;
        p.setAlpha((int) targetAlpha);
        int at = 0;
        for (int i = 0; i < 10; i++) {
            p.setAlpha((int) targetAlpha);
            canvas.drawLine(0, at, width * percent, at, p);
            float atX = ((int) (width * percent));
            while (p.getAlpha() > 1) {
                canvas.drawLine(atX, at, atX + 1, at, p);
                p.setAlpha((int) (p.getAlpha() / 1.1));
                atX++;
            }
            at++;
        }
        while (targetAlpha > 1) {
            p.setAlpha((int) targetAlpha);
            canvas.drawLine(0, at, width * percent, at, p);
            float atX = ((int) (width * percent));
            while (p.getAlpha() > 1) {
                canvas.drawLine(atX, at, atX + 1, at, p);
                p.setAlpha((int) (p.getAlpha() / 1.1));
                atX++;
            }
            targetAlpha = targetAlpha / 1.1f;
            at++;
        }
    }

    private void updateOffsetX(float vx) {
        float tempOffset = offsetX + vx;
        //TODO upadte this math it should keep history in mind too

        if (Math.abs(tempOffset) < (width + stupid.measureWidth()) / 2 - eqDragPadding) {
            offsetX = (int) tempOffset;
        }
    }

    private void updateOffsetY(float vy) {
        offsetY += vy;
    }

    public HashSet<Equation> selectingSet = new HashSet<Equation>();
    private long startTime;
    private long tapTime = 1000;
    private long lastTapTime;
    private Point lastTapPoint;
    private float doubleTapDistance = 20;

    //moving the whole equation
    private float lastX;
    private float lastY;
    private float eqDragPadding = 25;

    // TODO I think we can remove slidding
    private boolean slidding = false;
    private float vx = 0;
    private float vy = 0;

    public void updateOwner() {
        stupid.updateOwner(this);
    }

    public void removeSelected() {
        if (selected instanceof PlaceholderEquation) {
            if (!(selected.parent.size() == 1 && selected.parent != null)) {
                Equation oldEq = selected;
                selected.setSelected(false);
                oldEq.remove();
            }
        }
        if (selected != null) {
            if (!(selected.parent.size() == 1 && selected.parent != null)) {
                Equation oldEq = selected;
                selected.setSelected(false);
                oldEq.tryFlatten();
            }
        }
    }

    enum TouchMode {BUTTON, DRAG, SELECT, MOVE, ZOOM, DEAD}

    public boolean canDrag = false;

    private TouchMode myMode;

    LongTouch lastLongTouch = null;

    @Override
    public synchronized boolean onTouch(View view, MotionEvent event) {
        if (event.getPointerCount() == 1) {
            // we need to know if they started in the box
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // figure out the mode;
                if (inButtons(event)) {
                    myMode = TouchMode.BUTTON;
                } else if (stupid.inBox(event.getX(), event.getY())) {
                    myMode = TouchMode.SELECT;
                    removeSelected();
                } else {
                    myMode = TouchMode.MOVE;
                    if (selected != null) {
                        removeSelected();
                    }
                }
                startTime = System.currentTimeMillis();
                lastX = event.getX();
                lastY = event.getY();
                // stop stupid sliding
                slidding = false;
                vx = 0;
                vy = 0;
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                // check if they selected anything
                if (myMode == TouchMode.SELECT) {
                    HashSet<Equation> ons = stupid.onAny(event.getX(),
                            event.getY());

                    addToSelectingSet(ons);

                    String toLog = "";
                    for (Equation e : selectingSet) {
                        toLog += e.toString() + ",";
                    }

                    Log.i("selectingSet", toLog);

                    if (lastLongTouch == null) {
                        lastLongTouch = new LongTouch(event);
                    } else if (lastLongTouch.outside(event)) {
                        lastLongTouch = new LongTouch(event);
                    }

                    // see if they left the box
                    // TODO both version might be good
                    //if (canDrag &&  !stupid.inBox(event.getX(), event.getY())) {
                    //startDragging();
                    //}
                }

                // if we are dragging something move it
                if (myMode == TouchMode.DRAG) {
                    dragging.eq.x = event.getX();
                    dragging.eq.y = event.getY();


                    ArrayList<EquationDis> closest = stupid.closest(dragging);

                    // debug
                    String whatdowehavehere = "";
                    for (int i = 0; i < closest.size(); i++) {
                        whatdowehavehere += closest.get(i).equation
                                .hashCode()
                                + "|"
                                + closest.get(i).equation.getDisplay(0)
                                + "|"
                                + closest.get(i).dis
                                + " ";
                    }
                    Log.i("closest", dragging.demo.hashCode() + "|" + dragging.demo.toString() + " " + whatdowehavehere);

                    boolean found = false;
                    while (closest.size() != 0 && !found) {
                        EquationDis current = closest.get(0);
                        closest.remove(0);
                        if (dragging.demo.deepContains(current.equation)) {
                            found = true;
                            Log.i("drag", "no Move " + dragging.demo.toString() + " current Eq " + current.equation.toString());
                        } else {
                            if (!current.equation.deepContains(dragging.demo)) {
                                Log.i("drag", dragging.ops + "");
                                ArrayList<EquationDis> retuended = current.tryInsert(dragging);
                                if (retuended == null) {
                                    found = true;
                                } else {
                                    closest.addAll(retuended);
                                    Collections.sort(closest);
                                }

                                if (dragging.demo.parent == null) {
                                    @SuppressWarnings("unused")
                                    int dbg = 0;
                                    Log.i("weee", "I am null!");
                                }
                            }
                        }
                    }
                }

                // if they are moving the equation
                if (myMode == TouchMode.MOVE) {
                    float dx = event.getX() - lastX;
                    float dy = event.getY() - lastY;
                    updateOffsetX(dx);
                    updateOffsetY(dy);
                    lastX = event.getX();
                    lastY = event.getY();

                    vx = (3f * vx + dx) / 4f;
                    vy = (3f * vy + dy) / 4f;
                }
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                // did we click anything?
                boolean clicked = false;
                long now = System.currentTimeMillis();
                long totalTime = now - startTime;
                if (totalTime < tapTime) {
                    long tapSpacing = now - lastTapTime;
                    Point tapPoint = new Point();
                    tapPoint.x = (int) event.getX();
                    tapPoint.y = (int) event.getY();
                    if (tapSpacing < Algebrator.getAlgebrator().doubleTapSpacing && dis(tapPoint, lastTapPoint) < doubleTapDistance && myMode == TouchMode.SELECT) {
                        Log.i("", "doubleTap! dis: " + dis(tapPoint, lastTapPoint) + " time: " + totalTime + " spacing: " + tapSpacing);
                        if (canDrag) {
                            stupid.tryOperator(event.getX(),
                                    event.getY());
                            clicked = true;
                        }

                        lastTapTime = 0;
                    } else {
                        lastTapTime = now;
                        lastTapPoint = tapPoint;
                    }
                }
                if (!clicked) {
                    endOnePointer(event);
                }
                selectingSet = new HashSet<Equation>();

                // if we were dragging everything around
                if (myMode == TouchMode.MOVE) {
                    slidding = true;
                }

                lastLongTouch = null;

            }

        } else if (event.getPointerCount() == 2) {
            if (myMode != TouchMode.ZOOM) {
                endOnePointer(event);
                myMode = TouchMode.ZOOM;
            }
        } else

        {
            myMode = TouchMode.DEAD;
        }

        Log.i("", stupid.toString());
        return true;
    }

    private void startDragging() {
        if (canDrag) {
            selectSet();

            if (selected != null) {
                myMode = TouchMode.DRAG;
                // we need to take all the - signs with us
                while (selected.parent instanceof MinusEquation) {
                    selected = selected.parent;
                }
                //if (selected.canPop()) {
                dragging = new DragEquation(selected);
                selected.isDemo(true);
                //}
            } else {
                myMode = TouchMode.DEAD;
            }
        }
    }

    private void addToSelectingSet(HashSet<Equation> ons) {
        // we only want them to select equation on the same side as

        // we should not be select the = sign
        for (Equation e : ons) {
            if (e instanceof WritingLeafEquation && e.getDisplay(-1).equals("=")) {
                ons.remove(e);
            }
        }

        // if we selected anything
        if (!ons.isEmpty()) {

            // figure out what side we are looking at
            int side;
            if (!selectingSet.isEmpty()) {
                side = ((Equation) selectingSet.toArray()[0]).side();
            } else {
                side = ((Equation) ons.toArray()[0]).side();
            }

            // only add bits form the correct side
            for (Equation e : ons) {
                if (e.side() == side) {
                    selectingSet.add(e);
                }
            }
        }
    }

    protected boolean inButtons(MotionEvent event) {
        for (Button b : buttons) {
            if (b.couldClick(event)) {
                return true;
            }
        }
        return false;
    }

    private float dis(Point a, Point b) {
        // TODO Auto-generated method stub
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    private void endOnePointer(MotionEvent event) {
        if (myMode == TouchMode.BUTTON) {
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).click(event);
            }
        } else if (myMode == TouchMode.SELECT) {
            // what did we select?
            HashSet<Equation> ons = stupid.onAny(event.getX(), event.getY());
            addToSelectingSet(ons);
            resolveSelected(event);
        } else if (myMode == TouchMode.DRAG) {
            stupid.fixIntegrety();

            dragging.demo.isDemo(false);
            dragging = null;

            if (selected != null) {
                selected.setSelected(false);
            }
        }
    }

    protected void selectSet() {
        Equation lcp = null;
        // if anything in selectingSet is contained by something else remove it
        HashSet<Equation> setCopy = new HashSet<Equation>(selectingSet);
        for (Equation eq1 : setCopy) {
            for (Equation eq2 : setCopy) {
                if (!(eq1.equals(eq2)) && eq1.deepContains(eq2)) {
                    Log.i("removed", eq2.toString());
                    selectingSet.remove(eq2);
                }
            }
        }
        for (Equation eq : selectingSet) {
            if (lcp == null) {
                lcp = eq;//eq.parent
            } else { //if (!eq.parent.equals(lcp))
                lcp = lcp.lowestCommonContainer(eq);
            }
        }
        if (lcp != null && selectingSet.size() > 1) {
            // make sure they are a continous block
            ArrayList<Integer> indexs = new ArrayList<Integer>();
            for (Equation eq : selectingSet) {
                int index = lcp.deepIndexOf(eq);
                if (!indexs.contains(index)) {
                    indexs.add(index);
                }
            }
            Collections.sort(indexs);
            int min = indexs.get(0);
            if (lcp.get(min) instanceof WritingLeafEquation) {
                // the div lines in isOp could be a problem here
                // i think it's no a problem... I hope
                ((WritingLeafEquation) lcp.get(min)).isOpRight();
                if (min != 0) {
                    indexs.add(0, min - 1);
                }
            }
            int max = indexs.get(indexs.size() - 1);
            if (lcp.get(max) instanceof WritingLeafEquation) {
                // the div lines in isOp could be a problem here
                // i think it's no a problem... I hope
                ((WritingLeafEquation) lcp.get(max)).isOpLeft();
                if (max != lcp.size() - 1) {
                    indexs.add(max + 1);
                }
            }

            // if they do not make up all of lcp
            if (indexs.size() != lcp.size()) {
                // we make a new equation of the type of lcp
                Equation toSelect = null;
                if (lcp instanceof MultiEquation) {
                    toSelect = new MultiEquation(this);
                } else if (lcp instanceof AddEquation) {
                    toSelect = new AddEquation(this);
                } else if (lcp instanceof WritingEquation) {
                    toSelect = new WritingEquation(this);
                }
                //sort selected set
                ArrayList<Equation> selectedList = new ArrayList<Equation>();
                for (int i : indexs) {
                    selectedList.add(lcp.get(i));
                }
                // remove the selectingSet from lcp and add it to our
                // new equation
                for (Equation eq : selectedList) {
                    lcp.justRemove(eq);
                    toSelect.add(eq);
                }
                // insert the new equation in to lcp
                lcp.add(indexs.get(0), toSelect);
                // and select the new equation
                toSelect.setSelected(true);
            } else {
                lcp.setSelected(true);
            }
        } else {
            if (lcp != null) {
                lcp.setSelected(true);
            }
        }
        selectingSet = new HashSet<Equation>();
    }

    protected abstract void resolveSelected(MotionEvent event);

}
