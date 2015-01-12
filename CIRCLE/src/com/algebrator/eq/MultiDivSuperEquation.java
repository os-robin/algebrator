package com.algebrator.eq;

public interface MultiDivSuperEquation {


    /**
     * returns true is the given equation could be "on top" if this MultiEquation was written as A/B
     *
     * @param equation - a child of this
     * @return true if the child would be on top false if the child would be on bottom or if the equation is not a child
     */
    public abstract boolean onTop(Equation equation);

}
