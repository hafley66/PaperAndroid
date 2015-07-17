package FinalCode;

import android.graphics.Matrix;
import android.graphics.RectF;

import FinalCode.points.FacadePoint;
import FinalCode.points.Point;


/**
 * Created by chrishafley on 4/4/15.
 * Specification:
 * A. SCALE:
 * 1. Change scale uniformly
 * 2. Change scale on both axes
 * B. ROTATE
 * 1. rotateTo(float)
 * 2. rotateBy(float)
 * 3. rotate(float) //defaults to rotateBy
 * C.
 */
public abstract class ActionMatrix {

    public static final int PRE = 1, SET = 2, POST = 3;
    protected Matrix affine = new Matrix();
    public Listener listener;

    public abstract void onNullPivot(MatrixPoint point);

    public ActionMatrix(Listener listener)
    {
        this.listener = listener;
    }
    public ActionMatrix() {this(null);}

    public MatrixPoint
            scale = new MatrixPoint(1, 1, PRE),
            rotation = new MatrixPoint(0, 0, SET),
            translate = new MatrixPoint(0, 0, POST);

    public class MatrixPoint extends FacadePoint {
        protected FacadePoint pivot = new FacadePoint(0,0);
        protected Point oldPoint = new Point(0,0);
        public Point applyPoint;
        public int order;

        public MatrixPoint(float x, float y, int order){
            super(x, y);
            this.owner = ActionMatrix.this;
            this.order = order;

        }

        public void test(int o){
            int a,b,c,d;

            a = o/1000;
            b = (o/100)%10;
            c = (o/10)%10;
            d = o % 10;
        }

        public void St(float x, float y){
            oldPoint.set(this.x(), this.y());
            super.st(x, y);
            if(listener != null)
                listener.onMatrixPointChange(this, oldPoint);
        }
        public void St(float single){
            this.St(single, single);
        }
        public void St(Point newPoint){
            oldPoint.set(this.x(), this.y());
            super.set(newPoint);
            if(listener != null)
                listener.onMatrixPointChange(this, oldPoint);
        }

        public MatrixPoint pivot(Point p){
            if(p != null)
                pivot.set(p);
            else
                onNullPivot(this);
            return this;
        }
        public MatrixPoint pivot(float x, float y){
            pivot.st(x, y);
            return this;
        }
        public Point pivot(){
            return pivot;
        }
        public MatrixPoint order(int order){
            this.order = order;
            return this;
        }

        public void mathSt(float x, float y) {
            St(x, y);
        }
    }

    protected void _onMatrixPointChange_internal(MatrixPoint changed, Point old){
        Point p = changed.pivot;

        if(changed == scale){
            if(scale.order == ActionMatrix.PRE)
                this.affine.preScale(scale.x(), scale.y(), p.x(), p.y());

        } else if(changed == rotation){
            if(rotation.y() != 0) //Relative increase is held in the second field.
                rotation.set(old.x + rotation.y(), 0);

            if(rotation.order == ActionMatrix.SET)
                this.affine.setRotate(rotation.x(), p.x(), p.y());
        } else if(changed == translate){
            if(translate.order == ActionMatrix.POST)
                this.affine.postTranslate(translate.x(), translate.y());
        } else {
        }
    }

    protected void _onMatrixSet(MatrixPoint changed, Point toPoint){
        _onMatrixPointChange_internal(changed, toPoint);
        if(listener != null)
            listener.onMatrixPointChange(changed, toPoint);
    }

    public void setType(int type){
        scale.type = type;
        translate.type = type;
        rotation.type = type;
    }

    public void mapRect(RectF dst, RectF src){
        affine.mapRect(dst, src);
    }
    public void mapRect(RectF rect){
        affine.mapRect(rect);
    }
    public void mapPoints(Point... src){
        if(src != null){
            float[] dst = new float[src.length * 2];
            int i = 0;
            for(Point p : src){
                dst[i]   = p.x();
                //The mythical thrice-plus operator. Could easily replace with ++i in the following index.
                dst[i+++1] = p.y();
            }
            mapPoints(dst);
            i = 0;
            for(Point p : src){
                p.set(dst[i], dst[++i]);
            }
        }
    }
    public void mapPoints(float[] src){
        affine.mapPoints(src);
    }
    public void mapPoints(float[] dst, float[] src){
        affine.mapPoints(dst, src);
    }
    public ActionMatrix pivot(Point pivot){
        scale.pivot(pivot);
        rotation.pivot(pivot);
        translate.pivot(pivot);
        return this;
    }

    public interface Listener {
        public void onMatrixPointChange(MatrixPoint changed, Point old);
//        public void onMatrixPointApplyRequest(MatrixPoint changed, Point toApply);
    }
}
