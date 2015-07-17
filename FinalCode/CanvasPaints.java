package FinalCode;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by chrishafley on 3/1/15.
 *  TODO: Add method to convert 32 bit color to RGB-565
 *
 */
public class CanvasPaints {
    private static Paint nullPaint = initNullPaint();

    private static Paint initNullPaint() {
        Paint nullPaint = new Paint(Paint.DITHER_FLAG);
        nullPaint.setColor(Color.BLACK);
        nullPaint.setStrokeWidth(15f);
        nullPaint.setStyle(Paint.Style.STROKE);
        return nullPaint;
    }
    public static Paint nullPaint(){
        return nullPaint;
    }

    protected Paint selected = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
    protected Paint stroke = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
    protected Paint fill = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
    private Paint text = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
    protected Paint bitmap = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
    protected Paint background = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);

    public CanvasPaints(){
        reset();
    }

    public void reset(){
        selected.setStyle(Paint.Style.STROKE);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setColor(Color.BLACK);
        fill.setStyle(Paint.Style.FILL);
        text.setTextAlign(Paint.Align.LEFT);
        background.setColor(Color.TRANSPARENT);
//        bitmap.setColor(Color.TRANSPARENT);
    }

    public Paint selected(){
        return selected;
    }
    public Paint stroke(){
        return stroke;
    }
    public Paint fill(){
        return fill;
    }
    public Paint bitmap(){
        return bitmap;
    }
    public Paint text() {
        return text;
    }
}
