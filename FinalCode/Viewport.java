package FinalCode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

import FinalCode.IndexList.IndexedList;

import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Created by chrishafley on 4/22/15.
 */
public class Viewport {

    private final static long DEFAULT_FPS = 30;

    private long fps = DEFAULT_FPS;
    long lastStart = 0L;
    CanvasPaints paints = new CanvasPaints();
    IndexedList<Layer> layers = new IndexedList<>();

    protected Bitmap bitmap;
    protected Canvas canvas;


    public Viewport(long fps, float x, float y){
        bitmap = Bitmap.createBitmap((int)x, (int)y, ARGB_8888);
        canvas = new Canvas(bitmap);
        this.fps = fps;
        PaperAndroid.activeViewport = this;
        tic();
    }

    public void addLayer(Layer l){
        if(!layers.contains(l))
            layers.add(l);
    }

    public long tic(){
        lastStart = System.currentTimeMillis();
        return lastStart;
    }

    public long toc(){
        return System.currentTimeMillis() - lastStart;
    }

    public interface Layer {
        public void draw(Canvas canvas,
                         CanvasPaints paints);
    }


    public void draw(Canvas canvas){
        if(toc() > 1/fps){
            resetCanvas();
            for(Layer l : layers){
                l.draw(this.canvas, paints);
            }
            tic();
        }
        canvas.drawBitmap(this.bitmap, 0,0 ,paints.bitmap());
    }

    private void resetCanvas() {
        this.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }
}
