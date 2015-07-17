package FinalCode;

import android.graphics.Matrix;

import FinalCode.points.Point;


/**
 * Created by chrishafley on 4/12/15.
 */
public abstract class LinkedMatrix extends ActionMatrix implements ActionMatrix.Listener, Bounds.Handler{
    private static int
            IDS = 0;

    LinkedMatrix outer;
    LinkedMatrix inner;
    Matrix cascade = new Matrix();
    boolean dirtyCascade = false;
    boolean immediateCascade = false;
    boolean inheritCascade = true;

    public boolean visible = true;

    public int id = IDS++;

    public abstract
    void
    computeBounds(Bounds f);

    public final
    Bounds localBounds = new Bounds(this)
    {
        protected
        void
        computeBounds() {
            LinkedMatrix.this.computeBounds(this);
        }

        public
        void
        setDirty() {
            super.setDirty();
            globalBounds.setDirty();
        }
    };

    public final
    Bounds globalBounds = new Bounds(this)
    {
        protected
        void
        computeBounds() {
            localBounds.makeSureIsNotDirty();
            LinkedMatrix.this.getCascade().mapRect(this, localBounds);
        }
    };

    public final
    Bounds.BoundPoint
            localPosition = localBounds.center(),
            globalPosition = globalBounds.center();

    public Bounds bounds = localBounds;
    public Bounds.BoundPoint position = localPosition;

    public
    void
    onNullPivot(MatrixPoint point) {
        point.pivot.St(localBounds.center());
    }

    protected
    void
    _onMatrixPointChange_internal(MatrixPoint changed, Point old) {
        super._onMatrixPointChange_internal(changed, old);
        setDirtyCascade();
        if (immediateCascade)
            cascadeMatrices();
    }

    public
    void
    setDirtyCascade() {
        if (!this.dirtyCascade) {
            LinkedMatrix curr = this;
            while (curr != null) {
                curr._actualSetDirty();
                curr = curr.inner;
            }
        }
    }

    protected
    void
    _actualSetDirty(){
        dirtyCascade = true;
    }

    public LinkedMatrix
    getBeginningMatrix() {
        LinkedMatrix curr = this;
        while (curr.getOuter() != null) {
            curr = curr.getOuter();
        }
        return curr;
    }

    public
    Matrix
    getCascade() {
        if (dirtyCascade)
            cascadeMatrices();
        return cascade;
    }

    public
    void
    cascadeMatrices() {
        LinkedMatrix dirtiest = getBeginningMatrix();
        while (dirtiest.inner != null && !dirtiest.dirtyCascade)
            dirtiest = dirtiest.inner;

        while (dirtiest != null) {
            dirtiest.cascade.reset();
            if (dirtiest.outer != null && dirtiest.inheritCascade) {
                dirtiest.cascade.setConcat(dirtiest.outer.cascade, dirtiest.affine);
            } else {
                dirtiest.cascade.setConcat(dirtiest.affine, dirtiest.cascade);
            }
            dirtiest.dirtyCascade = false;
            dirtiest = dirtiest.inner;
        }
    }

    public LinkedMatrix
    getOuter(){
        if(this.outer != null)
            this.outer.inner = this;
        return this.outer;
    }

    public LinkedMatrix
    getInner() {
        return this.inner;
    }

    public
    void
    setParent(LinkedMatrix parent){
        this.outer = parent;
    }

    public
    boolean
    isVisible(){
        LinkedMatrix top = getBeginningMatrix();
        while(top != null){
           if(!top.visible)
               return false;
           top = top.getInner();
        }
        return true;
    }

    public
    void
    clean(){
        dirtyCascade = false;
    }

    public static
    void
    main(String args[]){
        Entity one = new Entity();
        Group two = new Group();
        two.add(one);
        Layer p = PaperAndroid.activePanel();
    }

}