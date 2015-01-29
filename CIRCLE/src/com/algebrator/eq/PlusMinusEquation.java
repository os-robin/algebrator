package com.algebrator.eq;

import com.example.circle.SuperView;

/**
 * Created by Colin on 1/28/2015.
 */
public class PlusMinusEquation extends MonaryEquation {

    public PlusMinusEquation(SuperView owner2) {
        super(owner2);

        display ="\u00B1";
    }

    @Override
    public Equation copy() {
        Equation result = new PlusMinusEquation(this.owner);
        result.add(get(0).copy());
        return result;
    }
}
