package com.example.circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
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
import com.algebrator.eq.MultiEquation;

public abstract class SuperView extends SurfaceView implements Runnable,
		OnTouchListener {
	public Equation selected;
	public DragEquation dragging;
	public Equation demo;
	SurfaceHolder surfaceHolder;
	Thread thread = null;
	volatile boolean running = false;
	public EqualsEquation stupid;
	int width;
	int height;
    int offsetX=0;
    int offsetY=0;

	TextPaint text = new TextPaint();

	int highlight;
	TextPaint bkg;

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
		surfaceHolder = getHolder();
		this.setOnTouchListener(this);

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

		text.setTextSize(30);
		text.setTextAlign(Align.CENTER);
		text.setColor(0xff000000);

		highlight = 0xff000000 + (int) (Math.random() * 0xffffff);

		bkg = new TextPaint();
		bkg.setTextSize(30);
		bkg.setTextAlign(Align.CENTER);
		bkg.setColor(0x00000000);

	}

	public void onResume() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void onPause() {
		boolean retry = true;
		running = false;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void run() {
		while (running) {
			if (surfaceHolder.getSurface().isValid()) {
				Canvas canvas = surfaceHolder.lockCanvas();
				myDraw(canvas);
				surfaceHolder.unlockCanvasAndPost(canvas);
				try {
					wait(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

    private String lastLog = "";

    private float friction= 0.8f;

	protected void myDraw(Canvas canvas) {
		// canvas.drawColor(0xFFFFFFFF, Mode.CLEAR);
		canvas.drawColor(0xFFFFFFFF, Mode.ADD);
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).draw(canvas);
		}
		if (dragging != null) {
			dragging.eq.draw(canvas, dragging.eq.x, dragging.eq.y);
		}

        if (slidding){
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
	}

    private void updateOffsetX(float vx) {
        float tempOffset = offsetX + vx;
        if (Math.abs(tempOffset) < (width + stupid.measureWidth())/2 - eqDragPadding ) {
            offsetX = (int)tempOffset;
        }
    }

    private void updateOffsetY(float vy) {
        offsetY += vy;
    }

    public HashSet<Equation> selectingSet = new HashSet<Equation>();
	public boolean startedInBox;
	public boolean inBox;
	public boolean startedOne;
	private long startTime;
	private long tapTime = 1000;
	private long lastTapTime;
	private long doubleTapSpacing=1000;
	private Point lastTapPoint;
	private float doubleTapDistance = 20;

    //moving the whole equation
    private float lastX;
    private float lastY;
    private float eqDragPadding = 25;

    //
    private boolean slidding = false;
    private float vx = 0;
    private float vy = 0;

	@Override
	public synchronized boolean onTouch(View view, MotionEvent event) {
		// if its one finger:
		if (event.getPointerCount() == 1) {

			// we need to know if they started in the box
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				startedInBox = stupid.inBox(event.getX(), event.getY());
				inBox = startedInBox;
				startedOne = true;
				startTime = System.currentTimeMillis();
                lastX = event.getX();
                lastY = event.getY();
                // stop stupid sliding
                slidding = false;
                vx =0;
                vy =0;
			}
			if (startedOne) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					// check if they selected anything
					if (inBox) {
						HashSet<Equation> ons = stupid.onAny(event.getX(),
								event.getY());
						selectingSet.addAll(ons);
					}

					// see if they left the box
					if (inBox && !stupid.inBox(event.getX(), event.getY())) {
						inBox = false;
						resolveSelected();
						if (selected != null) {
							//if (selected.canPop()) {
								selected.isDemo(true);
								dragging = new DragEquation(selected);
								dragging.eq.x = event.getX();
								dragging.eq.y = event.getY();
							//}
						}
					}

					// if we are dragging something move it
					if (inBox == false && dragging != null) {
						ArrayList<EquationDis> closest = stupid.closest(
								event.getX(), event.getY());

						// debug
						String whatdowehavehere = "";
						for (int i = 0; i < closest.size(); i++) {
							whatdowehavehere += closest.get(i).equation
									.hashCode()
									+ "|"
									+ closest.get(i).equation.getDisplay(0)
									+ " ";
						}
						Log.i("closest", whatdowehavehere);

						boolean found = false;
						for (int i = 0; i < closest.size() && !found; i++) {
							if (demo.deepContains(closest.get(i).equation)) {
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
							dragging.eq.negative = !dragging.eq.negative;
						}

						dragging.eq.x = event.getX();
						dragging.eq.y = event.getY();
					}

                    // if they are moving the equation
                    if (!startedInBox){
                        float dx = event.getX() -  lastX;
                        float dy = event.getY() -  lastY;
                        updateOffsetX(dx);
                        updateOffsetY(dy);
                        lastX = event.getX();
                        lastY = event.getY();

                        vx = (3f*vx + dx)/4f;
                        vx = (3f*vx + dy)/4f;
                    }
				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					// did we click anything?
					endOnePointer(event);
					long now = System.currentTimeMillis();
					long totalTime = now - startTime;
					if (totalTime < tapTime){
						long tapSpacing = now - lastTapTime;
						Point tapPoint = new Point();
						tapPoint.x = (int) event.getX();
						tapPoint.y = (int) event.getY();
						if (tapSpacing < doubleTapSpacing && dis(tapPoint,lastTapPoint)< doubleTapDistance){
							Log.i("","doubleTap! dis: " + dis(tapPoint,lastTapPoint) + " time: " +totalTime + " spacing: "+tapSpacing);
							stupid.tryOperator(event.getX(),
									event.getY());
							
							lastTapTime = 0;
						}else{
						lastTapTime = now;
						lastTapPoint = tapPoint;
						}
					}

                    // if we were dragging everything around
                    if (!startedInBox) {
                        slidding = true;
                    }
					
				}
			}
		} else if (event.getPointerCount() == 2) {
			if (startedOne) {
				endOnePointer(event);
			} else {
				startedOne = false;
			}
		}
		Log.i("",stupid.toString());
		return true;
	}

	private float dis(Point a, Point b) {
		// TODO Auto-generated method stub
		double dx = a.x -b.x;
		double dy = a.y - b.y;
		return (float) Math.sqrt((dx*dx)+(dy*dy));
	}

	private void endOnePointer(MotionEvent event) {
		if (!startedInBox) {
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).click(event);

			}
		}
		if (inBox) {
			// what did we select?
			HashSet<Equation> ons = stupid.onAny(event.getX(), event.getY());
			selectingSet.addAll(ons);
			resolveSelected();
		}

		// we are done dragging
		dragging = null;
		if (demo != null) {
			demo.isDemo(false);
		}

	}

	private void resolveSelected() {
		// now we need to figure out what we are selecting
		// find the least commond parent
		Equation lcp = null;
		for (Equation eq : selectingSet) {
			if (lcp == null) {
				lcp = eq;
			} else {
				lcp = lcp.lowestCommonContainer(eq);
			}
		}

		if (!(lcp instanceof LeafEquation) && lcp != null ) {
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
					}
					// remove the selectingSet from lcp and add it to our
					// new equation

					for (Equation eq : selectingSet) {
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
			} else {
			}
		}
		selectingSet = new HashSet<Equation>();
	}
}
