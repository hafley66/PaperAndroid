package FinalCode;

import android.graphics.Canvas;

import FinalCode.points.Point;

/**
 * Created by chrishafley on 4/12/15.
 */
public class Entity extends LinkedMatrix implements PATH.Listener, Style.StyleListener {

    Layer marm;
    protected PATH path = new PATH();
    Style style = new Style();

    public final static int
            PATH_CHANGE = 0,
            STYLE_CHANGE = 1,
            BOUNDS_CHANGE = 2,
            MATRIX_CHANGE = 3;

    boolean dirtyPath,
            dirtyStyle;



    public Entity()
    {
        this(PaperAndroid.activePanel());
    }

    Entity(Layer panel)
    {
        marm = panel;
        if(panel != null)
            marm.add(this);
    }

    public
    void
    draw(Canvas canvas,
                     CanvasPaints paints) {
        //Assume canvas has identity matrix on its matrix stack.
        canvas.setMatrix(
                this.getCascade());
        style.setPaints(
                paints);
        canvas.drawPath(
                path.path,
                paints.stroke());
        paints.reset();
    }

    public
    void
    computeBounds(Bounds rect) {
        path.path.computeBounds(rect, true);
    }

    public
    void
    onBoundsPointMoveRequest(
            Bounds.BoundPoint bndPt,
            Point requestedPoint) {
        //get difference
        Point diff =
                requestedPoint.minusC(bndPt);
        path.offset(diff);
        onChange(BOUNDS_CHANGE);
    }

    public
    void
    onMatrixPointChange(
            MatrixPoint changed,
            Point old){
        onChange(MATRIX_CHANGE);
    }

    @Override
    public
    void
    onCreatePathPoint(PATH.PathPoint
                              createdPoint){
        dirtyPath = true;
        onChange(PATH_CHANGE);
    }

    @Override
    public
    void
    onColorPointChange(Style.ColorPoint
                               changed){
        onChange(STYLE_CHANGE);
    }

    @Override
    public
    void
    onStrokePointChange(Style.StrokePoint
                                changed){
        onChange(STYLE_CHANGE);
    }

    protected
    void
    onChange(int code){
        if(listener != null)
            listener.onPSTFChange(this, code);
    }

    Listener listener = null;

    public
    interface
            Listener
    {
        public
        void
        onPSTFChange(Entity self, int code);
    }


    @Override
    public
    void
    _actualSetDirty() {
        super._actualSetDirty();
    }
}
