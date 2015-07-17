package FinalCode;

import android.graphics.Rect;
import android.graphics.RectF;

import FinalCode.points.Point;

/**
 * This class is an extension of the RectF class.
 * The reason for extension is to add the capability of finding each point of the underlying RectF
 *      object.
 * It is also used when an Entity is currently selected,
 *      as it defines the bounding box when it is drawn.
 *
 *
 * Created by chrishafley on 2/8/15.
 */
public abstract class Bounds extends RectF  {
    static final int
            TL = 1, T = 2, TR = 3,
            L  = 4, C = 5, R  = 6,
            BL = 7, B = 8, BR = 9;

    //public int type = -1;

    public boolean dirty = false;
    BoundPoint[] pts = init();
    Handler handler = null;

    protected abstract void computeBounds();


    public Bounds(RectF src, Handler handler){
        this.handler = handler;
        if(src != null)
            this.set(src);
    }
    public Bounds(Handler handler){this(null, handler);}
    public Bounds(RectF src){this(src, null);}
    public Bounds(float left, float top, float right, float bottom){
        this(new RectF(left, top, right, bottom));
    }
    public Bounds(){
        this(new RectF());
    }

    protected BoundPoint[] init(){
        BoundPoint[] f = new BoundPoint[10];
        for(int i = 1; i < f.length; i++){
            f[i] = new BoundPoint(i);
            f[i].owner = this;
        }
        return f;
    }
    public void set(float left, float top, float right, float bottom) {
        super.set(left, top, right, bottom);
        toNinePoints();
    }
    public void set(RectF src) {
        super.set(src);
        toNinePoints();
    }
    public void set(Rect src) {
        super.set(src);
        toNinePoints();
    }
    public boolean setIntersect(RectF a, RectF b) {
        boolean toReturn = super.setIntersect(a, b);
        toNinePoints();
        return toReturn;
    }


/*=================================================================================================*/
/*====== Primary Functions ========================================================================*/
/*=================================================================================================*/
/*   These functions deal with setting the other parts of the RectF the instant it is changed.
  *   This means finding the mid points between each RectF point
  *      Top-{Left, Right}
  *      Bottom-{Left, Right}
  *      Left (Between Top-Left and Bottom-Left)
  *      and similarly for the other sides.
  */
/*=================================================================================================*/
    protected void toNinePoints(){
        float rL = left;
        float rR = right;
        float rT = top;
        float rB = bottom;
        pts[TL].set(rL, rT);
        pts[BL].set(rL, rB);
        pts[TR].set(rR, rT);
        pts[BR].set(rR, rB);
        pts[L].set(pts[TL].mid(pts[BL]));
        pts[T].set(pts[TL].mid(pts[TR]));
        pts[R].set(pts[TR].mid(pts[BR]));
        pts[B].set(pts[BL].mid(pts[BR]));
        pts[C].set(pts[L].mid(pts[R]));
    }
    public BoundPoint center() {
        return getPts()[C];
    }
    public BoundPoint topLeft() {
        return getPts()[TL];
    }
    public BoundPoint left() {
        return getPts()[L];
    }
    public BoundPoint top() {
        return getPts()[T];
    }
    public BoundPoint topRight() {
        return getPts()[TR];
    }
    public BoundPoint right() {
        return getPts()[R];
    }
    public BoundPoint bottomLeft() {
        return getPts()[BL];
    }
    public BoundPoint bottomRight() {
        return getPts()[BR];
    }
    public BoundPoint bottom() {
        return getPts()[B];
    }

    public Point getSize(){
        return new Point(width(), height());
    }
    public BoundPoint[] getPts() {
        return pts;
    }

    public class BoundPoint extends Point {
        Point requestedPoint = new Point(0,0);
        int index;

        public BoundPoint(int index){
            this.index = index;
            this.owner = Bounds.this;
        }

        public void St(float x, float y){
            requestedPoint.set(x, y);
            handler.onBoundsPointMoveRequest(this, requestedPoint);
        }

        public void St(Point p){
            requestedPoint.set(p);
            handler.onBoundsPointMoveRequest(this, requestedPoint);
        }

        public float x(){
            makeSureIsNotDirty();
            return x;
        }
        public float y() {
            makeSureIsNotDirty();
            return y;
        }
    }

    protected void setDirty(){
        dirty = true;
    }
    protected void makeSureIsNotDirty(){
        if(dirty){
            this.computeBounds();
        }
        dirty = false;
    }

    //    ArrayList<Handler> handlers = new ArrayList<>();
    public interface Handler {
        public void onBoundsPointMoveRequest(BoundPoint bndPt, Point requestedPoint);
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public void union(Bounds rect){
        if(rect != null){
            float newLeft = Math.min(this.left().x(), rect.left().x());
            float newTop  = Math.min(this.top().y(), rect.top().y());
            float newRight= Math.max(this.right().x(), rect.right().x());
            float newBottm= Math.max(this.bottom().y(), rect.bottom().y());
            this.set(newLeft, newTop, newRight, newBottm);
        }
    }

    public void setEmpty(){
        this.set(0,0,0,0);
    }
}
