package com.algebrator.eq;
import java.util.ArrayList;
import java.util.List;

abstract public class Equation extends ArrayList<Equation>{
	Equation parent;
	String display;
	public abstract boolean isFlex();
}