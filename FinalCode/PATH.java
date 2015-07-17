package FinalCode;

import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;

import FinalCode.points.Point;

import static android.graphics.Path.Direction;

/**
 *  Wrapper class for the Android.Graphics.Path object.
 *   ______       ______        ________
 *  | PATH |---->| NODE |<-----| MATRIX |
 *  |______|     |______|      |________|
 *                  |
 *                __V____
 *               | STYLE |
 *               |_______|
 *
 *
 *  Calls to Path (partial implementation) act as the "model" for the parentPointer node.
 *
 *  Outside of delegating its methods to the underlying path object, this class implements
 *      the SelfAwareBounds interface.
 *
 * Created by chrishafley on 3/1/15.
 * TODO: Implement rest of the "add" path functions.
 * TODO: Create field "changeThreshold" which dictates the rate at which the path notifies the node.
 * TODO: Create field "changeCount" to count the number of changes.
 * TODO: Create function "prepareForChanges(int: amountOfChangesToIgnore)" which preallocates the path with the given int and prevents any increment to "changeCount".
 *  TODO: Consider making undo by path operations.
 *  TODO: Create edge finding algorithm?
 * TODO: Create subclass "MultiPath" which knows how to draw large paths without slowing performance.
 */
public class PATH {



    public static final char
            NullPoint = 'N',
            Offset = 'O',
            Line = 'L',
            Move = 'M',
            Reset = '~',
            Quad = 'Q',
            ControlPointOne = '<',
            ControlPointTwo = '>',
            Cubic = 'C',
            Rect = '0',
            RoundRect = '1',
            RoundRectAdvanced = '2',
            Oval = '3';

    private final PathPoint StartingPathPoint = new PathPoint(0,0, NullPoint);

    int changeCount = 0;
    Path path = new Path();

    boolean trackingPath = true;
    boolean mutablePath = false;

    ArrayList<PathPoint> points = new ArrayList<>();
    PathPoint head = StartingPathPoint;

    public PATH(Listener initial, Path toCopy){
        if(toCopy != null)
            path.set(toCopy);
        if(initial != null){
            listeners.add(initial);
        }
        points.add(head);
    }
    public PATH(Listener aNodeReborn) {
        this(aNodeReborn, null);
    }
    public PATH(Path toCopy){
        this(null, toCopy);
    }
    public PATH(){
        this(null, null);
    }

    public PATH offset(float x, float y){
        path.offset(x, y);
        return _onCreatePathPoint_internal(Offset, x, y);
    }

    public void Offset(float x, float y){
        this.offset(x, y).
                _onCreatePathPoint_internal(Offset, x, y);
    }
    public PATH offset(Point f){
        return this.offset(f.x(), f.y());
    }
    public void Offset(Point f){
        if(f != null)
            this.offset(f).
                    _onCreatePathPoint_internal(Offset, f.x(), f.y());
    }
    public PATH reset(){
        path.reset();
        return _onCreatePathPoint_internal(Reset, 0, 0);
    }

    //**************************** POINTS ****************************//
    public PATH moveTo(float x, float y){
        path.moveTo(x, y);
        return _onCreatePathPoint_internal(Move, x, y);
    }
    public void MoveTo(float x, float y){
        moveTo(x, y);
        onCreatePathPoint_external();
    }
    public PATH lineTo(float x, float y){
        path.lineTo(x, y);
        return _onCreatePathPoint_internal(Line, x, y);
    }
    public void LineTo(float x, float y){
        lineTo(x, y);
        onCreatePathPoint_external();
    }
    public PATH quadTo(float x1, float y1, float x2, float y2){
        path.quadTo(x1, y1, x2, y2);
        head = new PathPoint(x1, y1, ControlPointOne);
        return _onCreatePathPoint_internal(Quad, x2, y2);
    }
    public void QuadTo(float x1, float y1, float x2, float y2){
        quadTo(x1, y1, x2, y2);
        onCreatePathPoint_external();
    }
    public PATH cubicTo(float x1, float y1, float x2, float y2, float x3, float y3){
        path.cubicTo(x1, y1, x2, y2, x3, y3);
        head = new PathPoint(x2, y2, ControlPointOne);
        head.addComplement(new PathPoint(x1, y1, ControlPointTwo));
        return _onCreatePathPoint_internal(Cubic, x3, y3);
    }
    public void CubicTo(float x1, float y1, float x2, float y2, float x3, float y3){
        cubicTo(x1, y1, x2, y2, x3, y3);
        onCreatePathPoint_external();
    }

    //**************************** SHAPES ****************************//
    public PATH addRect(float left, float top, float right, float bottom, Direction dir) {
        path.addRect(left, top, right, bottom, dir);
        return _onCreatePathPoint_internal(Rect, left, top);
    }
    public PATH addRect(RectF rect, Direction dir) {
        path.addRect(rect.left, rect.top, rect.right, rect.bottom, dir);
        return _onCreatePathPoint_internal(Rect, rect.left, rect.top);
    }
    public PATH addRoundRect(float left, float top, float right, float bottom, float rx, float ry, Direction dir) {
        return this.addRoundRect(new RectF(left, top, right, bottom), rx, ry, dir);
    }
    public PATH addRoundRect(RectF rect, float rx, float ry, Direction dir) {
        path.addRoundRect(rect, rx, ry, dir);
        return _onCreatePathPoint_internal(RoundRect, rect.left, rect.top);
    }
    public PATH addRoundRect(float left, float top, float right, float bottom, float[] radii, Direction dir) {
        return this.addRoundRect(new RectF(left, top, right, bottom), radii, dir);
    }
    public PATH addRoundRect(RectF rect, float[] radii, Direction dir) {
        path.addRoundRect(rect, radii, dir);
        return _onCreatePathPoint_internal(RoundRectAdvanced, rect.left, rect.top);
    }
    public PATH addOval(RectF oval, Direction dir) {
        path.addOval(oval, dir);
        return _onCreatePathPoint_internal(Oval, oval.left, oval.top);
    }


    public boolean broadcast = true;
    public void stopBroadcasting(){ broadcast = false;}
    public void startBroadcasting(){ broadcast = true;}


    private PATH _onCreatePathPoint_internal(char flag, float x, float y){
        PathPoint newP;

        if(trackingPath)
        {
            newP = new PathPoint(x, y, flag);
            points.add(newP);
        }
        else
        {
            newP = points.get(0);
            newP.st(x, y);
            newP.action = flag;
            newP.complement = null;
        }
        if(flag == Quad || flag == Cubic)
            newP.addComplement(head);
        head = newP;
        return this;
    }
    public void onCreatePathPoint_external(){
        if(changeCount > 0)
        {
            changeCount--;
        } else
        {
            if(broadcast){
                int stop = listeners.size();
                for(int i = 0; i < stop; i++){
                    listeners.get(i).onCreatePathPoint(head);
                }
            }
        }
    }

    //For something like the path tool.
    //For later editing of paths and their control points.
    private void _onSetPathPoint_internal(char flag){}

    public void onSetPathPoint_external(PathPoint newP){

    }

    public void prepareForChanges(int changes){
        changeCount = changes;
        path.incReserve(changes);
    }

    ArrayList<Listener> listeners = new ArrayList<>();
    public void addListener(Listener listener){
        listeners.add(listener);
    }
    public interface Listener {
        public void onCreatePathPoint(PathPoint createdPoint);
    }

    public class PathPoint extends Point{
        char action = 0;
        PathPoint complement;

        PathPoint(float x, float y, char action){
            this.action = action;
            this.x = x;
            this.y = y;
        }

        protected void addComplement(PathPoint pp){
            PathPoint link = this;
            while(link.complement != null){
                link = link.complement;
            }
            link.complement = pp;
        }
    }
}
