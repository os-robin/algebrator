package com.example.circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import com.algebrator.eq.AddEquation;
import com.algebrator.eq.DragEquation;
import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.EquationDis;
import com.algebrator.eq.LeafEquation;
import com.algebrator.eq.MinusEquation;
import com.algebrator.eq.MultiEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.WritingEquation;

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

        highlight = 0xff000000 + (int) (Math.random() * 0xffffff);

        bkg = new Paint();
        bkg.setTextSize(Algebrator.getAlgebrator().TEXT_SIZE);
        bkg.setTextAlign(Align.CENTER);
        bkg.setColor(0xff000000 + (int) (Math.random() * 0xffffff));

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

    public void onResume() {
        //super.onResume();
        //running = true;
        //thread = new Thread(this);
        //thread.start();
        Log.i("lifeCycle", "SuperView-onResume");
    }

    public void onPause() {
        //boolean retry = true;
        //running = false;
        //while (retry) {
        //	try {
        //		thread.join();
        //		retry = false;
        //	} catch (InterruptedException e) {
        //		e.printStackTrace();
        //	}
        //}
        Log.i("lifeCycle", "SuperView-onPause");
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
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).draw(canvas);
        }
        if (dragging != null) {
            dragging.eq.draw(canvas, dragging.eq.x, dragging.eq.y);
        }

        if (slidding) {
            vx *= friction;
            vy *= friction;
            updateOffsetX(vx);
            updateOffsetY(vy);
        }

        stupid.draw(canvas, width / 2 + offsetX, height / 3 + offsetY);
        stupid.integrityCheckOuter();
        if (!stupid.toString().equals(lastLog)) {
            Log.i("", stupid.toString());
            lastLog = stupid.toString();
        }

        invalidate();
    }

    private void updateOffsetX(float vx) {
        float tempOffset = offsetX + vx;
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
    private long doubleTapSpacing = 1000;
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

    enum TouchMode {BUTTON, DRAG, SELECT, MOVE, ZOOM, DEAD}

    public boolean canDrag=false;

    private TouchMode myMode;

    @Override
    public synchronized boolean onTouch(View view, MotionEvent event) {
        if (event.getPointerCount() == 1) {
            // we need to know if they started in the box
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // figure out the mode;
                if (stupid.inBox(event.getX(), event.getY())) {
                    myMode = TouchMode.SELECT;
                    if (selected instanceof PlaceholderEquation){
                        if (!(selected.parent.size()==1 && selected.parent!= null)) {
                            selected.remove();
                        }
                    }
                } else if (inButtons(event)) {
                    myMode = TouchMode.BUTTON;
                } else {
                    myMode = TouchMode.MOVE;

                    if (selected != null && !(selected instanceof PlaceholderEquation)) {
                        selected.setSelected(false);
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
                    selectingSet.addAll(ons);

                    String toLog = "";
                    for (Equation e: selectingSet){
                        toLog += e.toString() + ",";
                    }

                    Log.i("selectingSet", toLog);

                    // see if they left the box
                    if (canDrag &&  !stupid.inBox(event.getX(), event.getY())) {
                        //if (!(selectingSet.size() ==1 && selectingSet.toArray()[0] instanceof  MinusEquation)){
                            resolveSelected(event);
                        //}else{
                        //    selected = (Equation)selectingSet.toArray()[0];
                        //}


                        if (selected != null) {
                            myMode = TouchMode.DRAG;
                            // we need to take all the - signs with us
                            while (selected.parent instanceof MinusEquation){
                                selected = selected.parent;
                            }
                            //if (selected.canPop()) {
                            dragging = new DragEquation(selected);
                            selected.isDemo(true);
                            dragging.eq.x = event.getX();
                            dragging.eq.y = event.getY();
                            //}
                        } else {
                            myMode = TouchMode.DEAD;
                        }
                    }
                }

                // if we are dragging something move it
                if (myMode == TouchMode.DRAG) {
                    ArrayList<EquationDis> closest = stupid.closest(
                            event.getX(), event.getY());

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
                    Log.i("closest", dragging.demo.hashCode()+ "|" + dragging.demo.toString()+" "+whatdowehavehere);

                    boolean found = false;
                    for (int i = 0; i < closest.size() && !found; i++) {
                        if (dragging.demo.deepContains(closest.get(i).equation)) {
                            found = true;
                            Log.i("drag", "no Move");
                        } else {
                            found = closest.get(i).tryInsert(dragging);

                            if (dragging.demo.parent == null) {
                                @SuppressWarnings("unused")
                                int dbg = 0;
                                Log.i("weee", "I am null!");
                            }
                        }
                    }
                    if ((dragging.add)
                            && ((dragging.eq.x < stupid.lastPoint.get(0).x && event
                            .getX() >= stupid.lastPoint.get(0).x) || (dragging.eq.x < stupid.lastPoint
                            .get(0).x && event.getX() >= stupid.lastPoint
                            .get(0).x))) {
                        //TODO handle negetives
                        //dragging.eq.negative = !dragging.eq.negative;
                    }

                    dragging.eq.x = event.getX();
                    dragging.eq.y = event.getY();
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
                endOnePointer(event);
                long now = System.currentTimeMillis();
                long totalTime = now - startTime;
                if (totalTime < tapTime) {
                    long tapSpacing = now - lastTapTime;
                    Point tapPoint = new Point();
                    tapPoint.x = (int) event.getX();
                    tapPoint.y = (int) event.getY();
                    if (tapSpacing < doubleTapSpacing && dis(tapPoint, lastTapPoint) < doubleTapDistance && myMode ==TouchMode.SELECT) {
                        Log.i("", "doubleTap! dis: " + dis(tapPoint, lastTapPoint) + " time: " + totalTime + " spacing: " + tapSpacing);
                        stupid.tryOperator(event.getX(),
                                event.getY());

                        lastTapTime = 0;
                    } else {
                        lastTapTime = now;
                        lastTapPoint = tapPoint;
                    }
                }

                // if we were dragging everything around
                if (myMode == TouchMode.MOVE) {
                    slidding = true;
                }

            }

        } else if (event.getPointerCount() == 2) {
            if (myMode != TouchMode.ZOOM) {
                endOnePointer(event);
                myMode = TouchMode.ZOOM;
            }
    }

    else

    {
        myMode = TouchMode.DEAD;
    }

    Log.i("",stupid.toString());
    return true;
}

    protected boolean inButtons(MotionEvent event){
        for (Button b : buttons){
            if (b.couldClick(event)){
                return true;
            }
        }
        return false;
    }

	private float dis(Point a, Point b) {
		// TODO Auto-generated method stub
		double dx = a.x -b.x;
		double dy = a.y - b.y;
		return (float) Math.sqrt((dx*dx)+(dy*dy));
	}

	private void endOnePointer(MotionEvent event) {
		if (myMode == TouchMode.BUTTON) {
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).click(event);
			}
		}
		else if (myMode == TouchMode.SELECT) {
			// what did we select?
			HashSet<Equation> ons = stupid.onAny(event.getX(), event.getY());
			selectingSet.addAll(ons);
			resolveSelected(event);
		}else if (myMode == TouchMode.DRAG){
            stupid.fixIntegrety();

            dragging.demo.isDemo(false);
            dragging = null;

            if (selected != null) {
                selected.setSelected(false);
            }
        }
	}

    protected abstract void resolveSelected(MotionEvent event);

}
