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
            if (close(dl,dragLocation) && dl.myStupid.same(dragLocation.myStupid)){
                pass = false;
            }else if (close(dl,dragLocation)){
                // push the old one "out" and the new one "in"

                dragLocation.y -=closeIs/2;
                dl.y +=closeIs/2;
            }
        }
        if (pass) {
            backEnd.add(dragLocation);
        }
    }

    //TODO scale by Dpi
    private float closeIs = 10;
    private boolean close(DragLocation dl, DragLocation dragLocation) {
        float x1= dl.x;
        float x2= dragLocation.x;
        float y1 = dl.y;
        float y2= dragLocation.y;
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))<closeIs;
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
