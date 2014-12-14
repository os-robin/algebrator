package com.example.circle.Actions;

import android.content.Context;

import com.example.circle.ColinView;
import com.example.circle.EmilyView;
import com.example.circle.MainActivity;


public class Solve extends Action {

	public Solve(EmilyView emilyView) {
		super(emilyView);
	}

	@Override
	public void act() {
		emilyView.onPause();
		Context c = emilyView.getContext();
		ColinView colinView = new ColinView(c);
		((MainActivity)c).lookAt(colinView);
		colinView.onResume();
		colinView.stupid = emilyView.stupid;

	}

}
