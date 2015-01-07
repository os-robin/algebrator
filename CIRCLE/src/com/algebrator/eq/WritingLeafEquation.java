package com.algebrator.eq;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

/**
 * Created by Colin on 1/6/2015.
 */
public class WritingLeafEquation extends LeafEquation {
    public WritingLeafEquation(String display, SuperView emilyView) {
        super(emilyView);
        this.display = display;
    }

    @Override
    public Equation copy() {
        Equation result = new WritingLeafEquation(this.display, this.owner);

        result.parentheses = this.parentheses;

        return result;
    }

}
