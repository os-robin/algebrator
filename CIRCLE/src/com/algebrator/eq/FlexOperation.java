package com.algebrator.eq;


import com.example.circle.SuperView;

/**
 * Created by Colin on 1/3/2015.
 */
public abstract class FlexOperation extends  Operation{


    public FlexOperation(SuperView owner){super(owner);}

    @Override
    public void fixIntegrety() {
        // if any of this's childern are add equation consume them
        Object[] kids = this.toArray();
        for (Object o:kids){
            Equation e = (Equation)o;
            if (e.getClass().equals(this.getClass())){
                int at=indexOf(e);
                this.justRemove(e);
                for (Equation e2: e){
                    add(at++,e2);
                }
            }
        }
        // if this is size one
        if(size()==1){
            this.replace(get(0));
        }
        for (Equation e: this){
            e.fixIntegrety();
        }
    }

//    @Override
//    public boolean add(Equation equation) {
//        if (equation.getClass().equals(this.getClass()) && !equation.demo){
//            boolean result= true;
//            for (Equation e: equation){
//                result = add(e);
//            }
//            return result;
//        }else{
//            boolean result = super.add(equation);
//            equation.parent = this;
//            return result;
//        }
//    }
//
//    @Override
//    public void add(int i,Equation equation) {
//        if (equation.getClass().equals(this.getClass()) && !equation.demo){
//            int at=i;
//            for (Equation e: equation){
//                add(at++,e);
//            }
//        }else{
//            super.add(i,equation);
//        }
//    }
}
