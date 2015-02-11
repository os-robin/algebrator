package com.example.circle.Actions;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.algebrator.eq.Equation;
import com.algebrator.eq.WritingEquation;
import com.example.circle.Algebrator;
import com.example.circle.ColinView;
import com.example.circle.EmilyView;
import com.example.circle.SolveScreen;


public class Solve extends Action {

    public Solve(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        if (emilyView.stupid instanceof WritingEquation)
            if (((WritingEquation) emilyView.stupid).deepLegal() && countEquals(emilyView.stupid) == 1) {

                AsyncTask<Void,Void,Long> task = new AsyncTask<Void,Void,Long>(){
                    Intent myIntent;
                    Context myContext;

                    protected Long doInBackground(Void... v) {
                        emilyView.removeSelected();
                        Equation toPass = emilyView.stupid.copy();

                        Equation newEq = ((WritingEquation) toPass).convert();

                        myContext = emilyView.getContext();
                        ColinView colinView = new ColinView(myContext);
                        colinView.stupid = newEq;
                        Algebrator.getAlgebrator().solveView = colinView;
                        //((MainActivity) c).lookAt(colinView);

                        myIntent = new Intent(myContext, SolveScreen.class);
                        myContext.startActivity(myIntent);
                        return 1L;
                    }

                    protected void onProgressUpdate(Void v) {
                    }

                    protected void onPostExecute(Long v) {

                    }
                };
                emilyView.disabled = true;
                task.execute();
            }
    }
}