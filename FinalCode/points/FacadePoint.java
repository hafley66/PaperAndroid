package FinalCode.points;

/**
 * The Facade Point can take a reference to a point and report its x and y coordinates instead of its own.
 * Created by chrishafley on 4/5/15.
 */
public class FacadePoint extends LockingPoint {

    public Point facadeP;

    public FacadePoint(float x, float y){
        super(x, y);
    }
    public FacadePoint(Point point){
        super(point);
    }
    public FacadePoint() {
        super();
    }
    public float x() {
        if(facadeP != null)
            return facadeP.x();
        return this.x;
    }

    public float y() {
        if(facadeP != null)
            return facadeP.y();
        return this.y;
    }

    public FacadePoint st(float x, float y){
        this.st(x, y);
        facadeP = null;
        return this;
    }
    public FacadePoint st(float single){
        return this.st(single, single);
    }
    public FacadePoint st(Point p){
        this.facadeP = p;
        this.x = p.x();
        this.y = p.y();
        return this;
    }

}
