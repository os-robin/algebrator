package com.example.circle.Actions;

import android.content.Context;
import android.content.Intent;

import com.algebrator.eq.Equation;
import com.algebrator.eq.WritingEquation;
import com.example.circle.Algebrator;
import com.example.circle.ColinView;
import com.example.circle.EmilyView;
import com.example.circle.MainActivity;


public class Solve extends Action {

    public Solve(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        if (emilyView.stupid instanceof WritingEquation)
            if (((WritingEquation) emilyView.stupid).deepLegal() && countEquals(emilyView.stupid) == 1) {

                emilyView.removeSelected();
                Equation toPass = emilyView.stupid.copy();

                Equation newEq = ((WritingEquation) toPass).convert();

                Context c = emilyView.getContext();
                ColinView colinView = new ColinView(c);
                colinView.stupid = newEq;
                Algebrator.getAlgebrator().superView = colinView;
                //((MainActivity) c).lookAt(colinView);

                Intent myIntent = new Intent(c, MainActivity.class);
                c.startActivity(myIntent);
            }
    }
}