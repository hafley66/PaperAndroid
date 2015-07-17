package FinalCode.points;


import android.graphics.PointF;

/**
 * Wrapper around the PointF class in the Android.Graphics package.
 * Goal is to provide arithmetic support between points and in general to extend the geometric
 * capabilities of the original class. This class is extended to provide substantial infrastructure
 * to the entire API. It is the means by which objects broadcast their states to any subscribing class.
 *
 * TODO: Delegate functionality of PointF and create wrapper methods. Cut out inheritance.
 *
 * Created by Chris Hafley on 2/14/15.
 */
public class Point extends PointF {

    //TODO: Legacy idea from JS Libraries, consider deprecating.
    public Object owner = null;

    //TODO: Legacy idea from old library iteration, consider deprecating
    public int type = -1;


    public Point(){super();}
    public Point(Point p){
        super(0,0);
        this.x = p.x();
        this.y = p.y();
    }
    public Point(float x, float y){
        super(0,0);
        this.x = x;
        this.y = y;
    }
    public Point(PointF p){super(p.x, p.y);}

    //GETTERS: Must always use the method and never direct access.
    public float x() {
        return this.x;
    }
    public float y() {
        return this.y;
    }


    public Point minus(Point negative){
        if(negative != null) {
            float x = this.x() - negative.x();
            float y = this.y() - negative.y();
            mathSt(x, y);
            return this;
        }
        return this;
    }
    public Point add(Point addend){
        if(addend != null) {
            float x = this.x()+ addend.x;
            float y = this.y() + addend.y;
            mathSt(x, y);
            return this;
        }
        return this;
    }
    public Point minusC(Point neg){
        return new Point(this).minus(neg);
    }
    public Point addC(Point addend){
        return new Point(this).add(addend);
    }
    protected void mathSt(float x, float y){
        this.st(x, y);
    }

    public Point mid(Point negative){
        if(negative != null) {
            float x = (this.x() + negative.x()) / 2;
            float y = (this.y() + negative.y()) / 2;
            return new Point(x, y);
        }
        return this;
    }
    public float distanceTo(Point to){
        if(to != null){
            float x = this.x()- to.x(); float y = this.y() - to.y();
            return (float)Math.sqrt(x * x + y * y);
        }
        return 0.0f;
    }
    public Point square(){
        return new Point(x * x, y * y);
    }
    public Point distanceVector(Point to){
        if(to != null) {
            float x = to.x()- this.x();
            float y = to.y() - this.y();
            return new Point(x, y);
        }
        return this;
    }
    public Point xMax(Point other){
        if(other != null){
            if(this.x()>= other.x())
                return this;
            else
                return other;
        }
        return this;
    }
    public Point yMax(Point other){
        if(other != null){
            if(this.y() >= other.y())
                return this;
            else
                return other;
        }
        return this;
    }
    public Point xMin(Point other){
        if(other != null){
            if(this.x()> other.x())
                return other;
            else
                return this;
        }
        return this;
    }
    public Point yMin(Point other){
        if(other != null){
            if(this.y() > other.y())
                return other;
            else
                return this;
        }
        return this;
    }

    //TODO: Write out some geometry and trig and finish this. Extra feature, not critical.
    public float angle(){
        float x = x(),
                y = y();
        if(x() == 0){
            if(y > 0) return 90f;
            if(y < 0) return 270f;
        } else if(y == 0){
            if(x > 0) return 0f;
            if(x < 0) return 180f;
        } else {
            double tangentRatio = y/x;
            double degrees = Math.toDegrees(Math.atan(tangentRatio));
            //Now must transform this based on quadrant that this point is in.
            if(tangentRatio < 0){
                //quad 2 or 4
//                360 + tangentRatio + (x < 0 ? -180)
            }
        }
        return 0.0f;
    }

    public boolean eq(float x, float y){
        return this.x()== x && this.y() == y;
    }
    public boolean eq(Point other){
        if(other != null) {
            return eq(other.x(), other.y());
        }
        return false;
    }
    public boolean isZero(){
        return this.x()== 0 && this.y() == 0;
    }

    public Point st(float x, float y){
        this.set(x, y);
        return this;
    }
    public Point st(Point p){
        if(p != null){
            this.st(p.x(), p.y());
        }
        return this;
    }
    public Point st(float single){
        return this.st(single, single);
    }
    public Point stX(float x){
        return this.st(x, this.y());
    }
    public Point stY(float y){
        return this.st(this.x(), y);
    }

    public void St(float x, float y){
        this.st(x, y);
    }
    public void St(Point p){
        if(p != null){
            this.St(p.x(), p.y());
        }
    }
    public void St(float single){
        this.St(single, single);
    }
    public void StX(float x){
        this.St(x, this.y());
    }
    public void StY(float y){
        this.St(this.x(), y);
    }
//    public void St(float single){
//        st(single, single);
//    }
//    public void St(Point newPoint) {
//        st(newPoint.x(), newPoint.y());
//    }
}
