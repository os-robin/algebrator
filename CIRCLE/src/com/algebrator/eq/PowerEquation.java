package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.circle.Algebrator;
import com.example.circle.SuperView;

import java.util.ArrayList;

/**
 * Created by Colin on 1/10/2015.
 */
public class PowerEquation extends Operation {

    public PowerEquation(SuperView owner){
        super(owner);

        display = "^";
        myWidth = Algebrator.getAlgebrator().DEFAULT_SIZE;
        myHeight = Algebrator.getAlgebrator().DEFAULT_SIZE;
    }

    @Override
    public Equation copy() {
        Equation result = new DivEquation(this.owner);
        result.display = this.getDisplay(-1);
        // pass selected?

        // copy all the kiddos and set this as their parent
        for (int i = 0; i < this.size(); i++) {
            result.add(this.get(i).copy());
            result.get(i).parent = result;
        }
        return result;
    }

    @Override
    public float measureWidth() {
        // TODO
//        float maxWidth = myWidth;
//
//        for (int i = 0; i < size(); i++) {
//            if (get(i).measureWidth() > maxWidth) {
//                maxWidth = get(i).measureWidth();
//            }
//        }
//        if (parenthesis()) {
//            maxWidth += PARN_WIDTH_ADDITION;
//        }
//
        return 0;
    }

    @Override
    public void privateDraw(Canvas canvas, float x, float y) {
        //TODO
//        drawBkgBox(canvas, x, y);
//        lastPoint = new ArrayList<Point>();
//        float totalHieght = measureHeight();
//        float currentY = -(totalHieght / 2) + y;
//        Paint temp = getPaint();
//        if (parenthesis()) {
//            drawParentheses(canvas, x, y, temp);
//            currentY += PARN_HEIGHT_ADDITION / 2;
//        }
//
//        for (int i = 0; i < size(); i++) {
//            float currentHieght = get(i).measureHeight();
//            get(i).draw(canvas, x, currentY + (currentHieght / 2));
//            currentY += currentHieght;
//            if (i != size() - 1) {
//                Point point = new Point();
//                point.x = (int) x;
//                point.y = (int) (currentY + (myHeight) / 2);
//                temp.setStrokeWidth(3);
//                int halfwidth = (int) ((measureWidth() - (2 * BUFFER)) / 2);
//                canvas.drawLine(point.x - halfwidth, point.y, point.x
//                        + halfwidth, point.y, temp);
//
//                lastPoint.add(point);
//                currentY += myHeight;
//            }
//        }
    }

    @Override
    public float measureHeight() {
        //TODO
//        float totalHeight = myHeight;
//
//        for (int i = 0; i < size(); i++) {
//            totalHeight += get(i).measureHeight();
//        }
//        if (parenthesis()) {
//            totalHeight += PARN_HEIGHT_ADDITION;
//        }
          return 0;
    }
}
