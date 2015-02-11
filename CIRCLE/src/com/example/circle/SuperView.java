package com.example.circle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
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
import com.algebrator.eq.DragLocations;
import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;
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

    public boolean disabled= false;


    protected float buttonLine() {
        return height * buttonsPercent;
    }

    int highlight;

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

        highlight = Algebrator.getAlgebrator().highLight;

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
        Algebrator.getAlgebrator().at++;

        canvas.drawColor(0xFFFFFFFF, Mode.ADD);

//        for (DragLocation dl:dragLocations){
//            float dlx = dl.x + stupid.lastPoint.get(0).x;
//            float dly = dl.y + stupid.lastPoint.get(0).y;
//            Paint temp =new Paint();
//            temp.setColor(Color.GREEN);
//            canvas.drawCircle(dlx,dly,15,temp);
//        }

        if (dragging != null) {
            dragging.eq.draw(canvas, dragging.eq.x, dragging.eq.y);
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

        if (dragging != null) {
            //TODO scale by dpi
            int buffer = 100;
            if (dragging.eq.x + buffer > width) {
                if (vx > -targetV) {
                    vx = -targetV;
                    slidding = true;
                }
            }


            if (dragging.eq.y + buffer > buttonLine()) {
                if (vy > -targetV) {
                    vy = -targetV;
                    slidding = true;
                }
            }

            if (dragging.eq.x - buffer < 0) {
                if (vx < targetV) {
                    vx = targetV;
                    slidding = true;
                }
            }

            if (dragging.eq.y - buffer < 0) {
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

        if (this instanceof EmilyView) {
            stupid.draw(canvas, width / 2 + offsetX, height / 3 + offsetY);
        }else{
            ((EqualsEquation) stupid).drawCentered(canvas, width / 2 + offsetX, height / 3 + offsetY);
        }
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
        p.setColor(Algebrator.getAlgebrator().mainColor - 0xff000000);
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


    private long startTime;
    private long lastTapTime;
    private Point lastTapPoint;


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

    protected HashSet<Equation> willSelect;


    private int disabledAlpha = 0;
    protected void onDrawAfter(Canvas canvas){
        if (disabled) {
            disabledAlpha = (int)((5f*disabledAlpha + 0x80)/6f);
            Rect r = new Rect(0, 0, width, height);
            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setAlpha(disabledAlpha);
            canvas.drawRect(r, p);
        }
    }

    @Override
    public synchronized boolean onTouch(View view, MotionEvent event) {
        if (!disabled) {
            if (event.getPointerCount() == 1) {

                // we need to know if they started in the box
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // figure out the mode;
                    if (inButtons(event)) {
                        myMode = TouchMode.BUTTON;
                    } else if (stupid.inBox(event.getX(), event.getY())) {
                        myMode = TouchMode.SELECT;
                        willSelect = stupid.onAny(event.getX(),
                                event.getY());
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
                        // if they get too far from were they started we are going to start dragging
                        //TODO scale by dpi
                        float maxMovement = 50;
                        float distance = (float) Math.sqrt((lastX - event.getX()) * (lastX - event.getX()) + (lastY - event.getY()) * (lastY - event.getY()));
                        if (maxMovement < distance) {

                            boolean pass = true;
                            if (selected != null) {
                                for (Equation e : willSelect) {
                                    if (!selected.deepContains(e)) {
                                        pass = false;
                                        break;
                                    }
                                }
                            } else {
                                resolveSelected(event);
                            }

                            if (pass) {
                                startDragging();
                            } else {
                                myMode = TouchMode.MOVE;
                            }
                        }
                    }

                    // if we are dragging something move it
                    if (myMode == TouchMode.DRAG) {
                        dragging.eq.x = event.getX();
                        dragging.eq.y = event.getY();

                        DragLocation closest = dragLocations.closest(event);

                        if (closest != null) {
                            stupid = closest.myStupid;
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
                    Point tapPoint = new Point();
                    tapPoint.x = (int) event.getX();
                    tapPoint.y = (int) event.getY();
                    long tapSpacing = now - lastTapTime;
                    if (tapSpacing < Algebrator.getAlgebrator().doubleTapSpacing && dis(tapPoint, lastTapPoint) < Algebrator.getAlgebrator().getDoubleTapDistance() && myMode == TouchMode.SELECT) {
                        Log.i("", "doubleTap! dis: " + dis(tapPoint, lastTapPoint) + " time: " + tapSpacing);
                        if (canDrag) {
                            stupid.tryOperator(event.getX(),
                                    event.getY());
                            clicked = true;
                            if (this instanceof ColinView) {
                                if (((ColinView) this).changed == false) {
                                    clicked = false;
                                } else {
                                    if (selected != null) {
                                        selected.setSelected(false);
                                    }
                                }
                            }
                        }

                        // set the lastTapTime to zero so they can not triple tap and get two double taps
                        lastTapTime = 0;
                    } else {
                        lastTapTime = now;
                    }
                    lastTapPoint = tapPoint;
                    if (!clicked) {
                        endOnePointer(event);
                    }

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
        }else{
            myMode = TouchMode.DEAD;
        }

        Log.i("", stupid.toString());
        return true;
    }

    private void updateLastTouch(MotionEvent event) {
        if (lastLongTouch == null) {
            lastLongTouch = new LongTouch(event);
        } else if (lastLongTouch.outside(event)) {
            lastLongTouch = new LongTouch(event);
        }
    }

    private DragLocations dragLocations = new DragLocations();

    private void startDragging() {
        if (canDrag) {
            if (selected != null) {
                myMode = TouchMode.DRAG;
                // we need to take all the - signs with us
                while (selected.parent instanceof MinusEquation) {
                    selected.parent.setSelected(true);
                }
                //if (selected.canPop()) {
                dragging = new DragEquation(selected);
                dragging.eq.x = lastX;
                dragging.eq.y = lastY;
                selected.isDemo(true);

                selected.setSelected(false);

                //TODO the evil thing is in here
                getDragLocations();

                Log.d("Drag Locations", "#######################");
                for (DragLocation dl:dragLocations){
                    Log.d("Drag Locations",dl.myStupid.toString());
                }
                //}

            } else {
                myMode = TouchMode.MOVE;
            }
        }
    }

    //update DragLocations
    private void getDragLocations() {
        dragLocations = new DragLocations();
        stupid.getDragLocations(dragging.demo, dragLocations, dragging.ops);
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
            resolveSelected(event);
        } else if (myMode == TouchMode.DRAG) {
            DragLocation closest = dragLocations.closest(event);

            if (closest != null) {
                closest.select();
            }


            stupid.fixIntegrety();


            dragging.demo.isDemo(false);
            dragging = null;

            if (selected != null) {
                selected.setSelected(false);
            }
        }
        stupid.updateLocation();
    }

    protected void selectSet(HashSet<Equation> selectingSet) {
        Equation lcp = null;
        // if anything in selectingSet is contained by something else remove it
        ArrayList<Equation> setCopy = new ArrayList<Equation>(selectingSet);
        for (int i1=0;i1<setCopy.size();i1++) {
            Equation eq1 =setCopy.get(i1);
            for (int i2=i1+1;i2<setCopy.size();i2++) {
                Equation eq2 = setCopy.get(i2);
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
                if (index == -1){
                    Log.e("index is -1!", lcp.toString() + " "+ eq.toString());
                }

                if (!indexs.contains(index)) {
                    indexs.add(index);
                }
            }
            Collections.sort(indexs);
            //Collections.reverse(indexs);
            int min = indexs.get(0);
            Log.d("min", min + "");
            if (lcp.get(min) instanceof WritingLeafEquation) {
                // the div lines in isOp could be a problem here
                // i think it's no a problem... I hope
                ((WritingLeafEquation) lcp.get(min)).isOpRight();
                if (min != 0) {
                    indexs.add(0, min - 1);
                }
            }
            int max = indexs.get(indexs.size() - 1);
            Log.d("max", max + "");
            if (lcp.get(max) instanceof WritingLeafEquation) {
                // the div lines in isOp could be a problem here
                // i think it's no a problem... I hope
                ((WritingLeafEquation) lcp.get(max)).isOpLeft();
                if (max != lcp.size() - 1) {
                    indexs.add(max + 1);
                }
            }

            // the we need to add the indexes between min and max
            for (int newIndex = min + 1; newIndex < max; newIndex++) {
                if (!indexs.contains(newIndex)) {
                    indexs.add(newIndex);
                }
            }
            Collections.sort(indexs);

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
