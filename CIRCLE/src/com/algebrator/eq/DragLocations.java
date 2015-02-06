package com.algebrator.eq;

import android.util.Log;
import android.view.MotionEvent;

import com.example.circle.DragLocation;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Colin on 2/5/2015.
 */
public class DragLocations implements Iterable<DragLocation> {
    private ArrayList<DragLocation> backEnd = new ArrayList<DragLocation>();

    public void add(DragLocation dragLocation) {
        boolean pass = true;
        for (DragLocation dl:backEnd){
            if (dl.x == dragLocation.x && dl.y == dragLocation.y && dl.myStupid.same(dragLocation.myStupid)){
                pass = false;
            }else if (dl.x == dragLocation.x && dl.y == dragLocation.y){
                Log.i("dragLocations - add", "collision adding");

            }
        }
        if (pass) {
            backEnd.add(dragLocation);
        }
    }

    public DragLocation closest(MotionEvent event) {
        float min= Float.MAX_VALUE;
        DragLocation mindl = null;
        for (DragLocation dl:backEnd){
            dl.updateDis(event.getX(),event.getY());
            if (dl.dis < min){
                min = dl.dis;
                mindl = dl;
            }
        }
        return mindl;

    }

    @Override
    public Iterator<DragLocation> iterator() {
        Iterator<DragLocation> it = backEnd.iterator();
        return it;
    }

}
