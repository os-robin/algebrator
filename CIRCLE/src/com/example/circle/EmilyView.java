package com.example.circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.algebrator.eq.AddEquation;
import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.FlexEquation;
import com.algebrator.eq.MultiEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.Actions.Action;
import com.example.circle.Actions.DecimalAction;
import com.example.circle.Actions.DeleteAction;
import com.example.circle.Actions.DivAction;
import com.example.circle.Actions.MinusAction;
import com.example.circle.Actions.NumberAction;
import com.example.circle.Actions.ParenthesesAction;
import com.example.circle.Actions.PlusAction;
import com.example.circle.Actions.Solve;
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
		stupid = new EqualsEquation(this);
		Equation x = new PlaceholderEquation(this);
		Equation y = new PlaceholderEquation(this);
		Log.e("zoom?", (x == y) + "");

		/*
		 * modified from Elliott Hughes' "Dalvik Explorer" app on
		 * http://stackoverflow
		 * .com/questions/1016896/how-to-get-screen-dimensions
		 * 
		 * WindowManager w = (WindowManager)
		 * context.getSystemService(Context.WINDOW_SERVICE); Display d =
		 * w.getDefaultDisplay(); DisplayMetrics metrics = new DisplayMetrics();
		 * d.getMetrics(metrics); // since SDK_INT = 1; int width =
		 * metrics.widthPixels; int height = metrics.heightPixels; // includes
		 * window decorations (statusbar bar/menu bar) if (Build.VERSION.SDK_INT
		 * >= 14 && Build.VERSION.SDK_INT < 17) try { width = (Integer)
		 * Display.class.getMethod("getRawWidth").invoke(d); height = (Integer)
		 * Display.class.getMethod("getRawHeight").invoke(d); } catch (Exception
		 * ignored) { } // includes window decorations (statusbar bar/menu bar)
		 * if (Build.VERSION.SDK_INT >= 17) try { Point realSize = new Point();
		 * Display.class.getMethod("getRealSize", Point.class).invoke(d,
		 * realSize); width = realSize.x; height = realSize.y; } catch
		 * (Exception ignored) { }
		 */

		for (int i = 0; i < 5; i++) {
			buttons.add(new Button(i * width / 11, (i + 1) * width / 11,
					4 * height / 6, 5 * height / 6, i + 1 + "", text, bkg,
					highlight));
			buttons.get(i).myAction = new NumberAction(this, i + 1 + "");
		}

		Button parentheses = new Button(5 * width / 11, 6 * width / 11,
				4 * height / 6, 5 * height / 6, "( )", text, bkg, highlight);
		parentheses.myAction = new ParenthesesAction(this);

		buttons.add(parentheses);
		Button times = new Button(6 * width / 11, 7 * width / 11,
				4 * height / 6, 5 * height / 6, "*", text, bkg, highlight);
		times.myAction = new TimesAction(this);
		buttons.add(times);

		Button plus = new Button(7 * width / 11, 8 * width / 11,
				4 * height / 6, 5 * height / 6, "+", text, bkg, highlight);
		plus.myAction = new PlusAction(this);
		buttons.add(plus);

		Button varX = new Button(8 * width / 11, 9 * width / 11,
				4 * height / 6, 5 * height / 6, "x", text, bkg, highlight);
		varX.myAction = new VarAction(this, varX.text);
		buttons.add(varX);

		Button varY = new Button(9 * width / 11, 10 * width / 11,
				4 * height / 6, 5 * height / 6, "y", text, bkg, highlight);
		varY.myAction = new VarAction(this, varY.text);
		buttons.add(varY);

		buttons.add(new Button(10 * width / 11, 11 * width / 11,
				4 * height / 6, 5 * height / 6, "VAR", text, bkg, highlight));

		for (int i = 0; i < 4; i++) {
			buttons.add(new Button((int) ((i + 0.5) * width / 11),
					(int) ((i + 1.5) * width / 11), 5 * height / 6, height, i
							+ 6 + "", text, bkg, highlight));
			buttons.get(i + 11).myAction = new NumberAction(this, i + 6 + "");
		}

		Button delete = new Button((int) ((9 + 0.5) * width / 11),
				(int) ((10 + 0.5) * width / 11), 5 * height / 6, height, "DEL",
				text, bkg, highlight);
		delete.myAction = new DeleteAction(this);

		buttons.add(new Button((int) ((4 + 0.5) * width / 11), (int) ((5 + 0.5)
				* width / 11), 5 * height / 6, height, "0", text, bkg,
				highlight));
		buttons.get(15).myAction = new NumberAction(this, "0");
		buttons.add(new Button((int) ((5 + 0.5) * width / 11), (int) ((6 + 0.5)
				* width / 11), 5 * height / 6, height, ".", text, bkg,
				highlight));
		buttons.get(16).myAction = new DecimalAction(this, ".");
		Button div = new Button((int) ((6 + 0.5) * width / 11),
				(int) ((7 + 0.5) * width / 11), 5 * height / 6, height, "�",
				text, bkg, highlight);
		div.myAction = new DivAction(this);
		buttons.add(div);

		Button minus = new Button((int) ((7 + 0.5) * width / 11),
				(int) ((8 + 0.5) * width / 11), 5 * height / 6, height, "-",
				text, bkg, highlight);
		minus.myAction = new MinusAction(this);
		buttons.add(minus);
		buttons.add(new Button((int) ((8 + 0.5) * width / 11), (int) ((9 + 0.5)
				* width / 11), 5 * height / 6, height, "MORE", text, bkg,
				highlight));
		buttons.add(delete);

		Button solve = new Button(0, width, 0, 1 * height / 6, "solve", text,
				bkg, highlight);
		solve.myAction = new Solve(this);
		buttons.add(solve);

		/*
		 * AddEquation add1 = new AddEquation(); add1.add(new
		 * NumConstEquation("3")); add1.add(new NumConstEquation("4"));
		 * AddEquation add2 = new AddEquation(); add2.add(new
		 * NumConstEquation("2")); add2.add(new NumConstEquation("5"));
		 * AddEquation add3 = new AddEquation(); add3.add(new
		 * NumConstEquation("1")); add3.add(new NumConstEquation("6"));
		 * stupid.add(add1); stupid.add(add2); stupid.add(add3);
		 */

		PlaceholderEquation empty1 = new PlaceholderEquation(this);
		empty1.setSelected(true);
		PlaceholderEquation empty2 = new PlaceholderEquation(this);
		stupid.add(empty1);
		stupid.add(empty2);

		buttons.get(10).myAction = new Action(this) {
			int count = 0;

			@Override
			public void act() {

				// TODO while there is room:
				// TODO allow the player to enter a var name
				// TODO it would be cool if you could swipe up or something to
				// delete these

				Button tempButton = new Button(width * (10 - count) / 11, width
						* (11 - count) / 11, 3 * height / 6, 4 * height / 6,
						"A" + count, text, bkg, highlight);
				tempButton.myAction = new VarAction(emilyView, tempButton.text);

				vars.add(tempButton);

				count++;
			}

		};

	};

	@Override
	protected void myDraw(Canvas canvas) {
		super.myDraw(canvas);

		for (int i = 0; i < vars.size(); i++) {
			vars.get(i).draw(canvas);
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
}
