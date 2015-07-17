package FinalCode.points;

/**
 * A point that can be locked and tagged as dirty.
 * A locked point will not
 * Created by chrishafley on 4/12/15.
 */
public class LockingPoint extends Point {

    boolean locked = false;
    boolean dirty  = false;

    public LockingPoint(Point point) {
        super(point);
    }

    public LockingPoint(float x, float y) {
        super(x, y);
    }

    public LockingPoint() {
        super();
    }

    public float x() {
        if(dirty)
            clean();
        return this.x;
    }
    public float y() {
        if(dirty)
            clean();
        return this.y;
    }

    public LockingPoint st(float x, float y) {
        if(!locked)
            super.st(x, y);
        return this;
    }

    @Override
    public void mathSt(float x, float y) {
        if(!locked)
            super.mathSt(x, y);
    }

    public void clean(){
        dirty = false;
    }

}
