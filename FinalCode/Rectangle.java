package FinalCode;

import android.graphics.Path;
import android.graphics.RectF;

import FinalCode.points.Point;

/**
 * Created by chrishafley on 4/22/15.
 */
public class Rectangle extends Entity {

    public Rectangle(float left, float top, float width, float height){
        path.addRect(left, top, width, height, Path.Direction.CW);
    }

    public Rectangle(Point position, Point dimensions){
        this(position.x(), position.y(), dimensions.x(), dimensions.y());
    }

    public Rectangle st(float l, float t, float r, float b){
        path.reset().addRect(l, t, r, b, Path.Direction.CW);
        return this;
    }

    public Rectangle st(RectF f){
        if(f != null)
            return this.st(f.left, f.top, f.right, f.bottom);
        else
            return this;
    }

    public Rectangle st(Bounds b){
        if(b != null){
            return this.st(b.left, b.top, b.right, b.bottom);
        }
        else
            return this;
    }

    public Rectangle st(LinkedMatrix bounded){
        if(bounded != null)
            return this.st(bounded.bounds);
        return this;
    }
}
