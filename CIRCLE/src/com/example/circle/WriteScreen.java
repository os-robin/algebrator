package com.example.circle;

import android.os.Bundle;
import android.view.ViewGroup;

import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;

/**
 * Created by Colin on 2/6/2015.
 */
public class WriteScreen
        extends SuperScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume(){
        super.onResume();

        if (Algebrator.getAlgebrator().writeView == null) {
            myView = new EmilyView(this);
            Algebrator.getAlgebrator().writeView = (EmilyView)myView;
        }else{
            myView = Algebrator.getAlgebrator().writeView;
            if(myView.getParent() != null) {
                ((ViewGroup) myView.getParent()).removeView(myView);
            }
        }
        myView.disabled = false;

        if (myView.selected == null){
            Equation toAdd = new PlaceholderEquation(myView);
            myView.selected = toAdd;
            myView.stupid.add(toAdd);
        }


        lookAt(myView);
    }
}
