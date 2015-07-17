package FinalCode;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import FinalCode.points.Point;

/**
 * Wrapper for the Android.Graphics.Paint object.
 *
 * Is used to st paints at the time of drawing.
 *
 * Objects do not actually carry their own paint objects, because there are many of them.
 *
 * The different paints that exist are :
 *      1. Fill
 *      2. Stroke
 *      3. Text
 *      4. Select
 *      5. Dragging (TODO:)
 *
 * Created by chrishafley on 3/25/15.
 */
public class Style {

    private static final float
            TEXT_SIZE_DEF = 15,
            TEXT_SCALE_DEF = 1.0f,
            TEXT_SKEW_DEF = 1.0f,
            STROKE_WIDTH_DEF = 15f;


    StyleListener listener;

    /**
     * Main constructor. If src is null, then it calls the
     */
    public Style(StyleListener styleListener, Style src){
        this.listener = styleListener;
        if(src != null)
            setStyle(src);
        else {
            setStyle();
        }
    }
    public Style(Style src){
        this(null, src);
    }
    public Style(StyleListener listener){this(listener, null);}
    public Style(){this(null, null);}

    public int fillColor           = Color.WHITE;
    public int strokeColor         = Color.BLACK;
    public int selectedColor       = Color.BLUE;
    public int textColor           = Color.BLACK;
    public float strokeWidth       = STROKE_WIDTH_DEF;
    public Paint.Cap cap           = Paint.Cap.ROUND;
    public Paint.Join join = Paint.Join.ROUND;
//    public boolean strokeScaling   = true;

    public ColorPoint
            fillC = new ColorPoint(fillColor),
            strkC = new ColorPoint(strokeColor),
            slctC = new ColorPoint(selectedColor),
            textC = new ColorPoint(textColor);
    public StrokePoint
            strk = new StrokePoint(Paint.Style.STROKE),
            fill = new StrokePoint(Paint.Style.FILL),
            slct = new StrokePoint(Paint.Style.STROKE);

    /*------------------------------------------------
    *  Next iteration of styles to apply
    *  TEXT
    *-----------------------------------------------*/
    public float textSize = TEXT_SIZE_DEF;
    public float textSkew = TEXT_SKEW_DEF;
    public float textScale = TEXT_SCALE_DEF;
    public Paint.Align textAlign = Paint.Align.LEFT;
    public boolean textSubpixel = false;
    public boolean textLinear = false;
    public boolean textUnderline = false;
    public boolean textFakeBold = false;
    public boolean textStrikeThru = false;

    void paint_test(){
        Paint p = new Paint();
        p.setTextSize(15f);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextScaleX(0.5f);
        p.setTextSkewX(6.0f);
        p.setLinearText(false);
        p.setSubpixelText(false);
        p.setUnderlineText(true);
        p.setFakeBoldText(false);
        p.setStrikeThruText(false);
        p.setTypeface(Typeface.DEFAULT);
        p.setAlpha(124);
        p.setStrokeMiter(0.5f);
    }

    public void setStyle(Style source) {
        if(source != null){
            fillC.copycat(source.fillC);
            strkC.copycat(source.strkC);
            slctC.copycat(source.slctC);
            textC.copycat(source.textC);
            fill.copycat(source.fill);
            strk.copycat(source.strk);
            slct.copycat(source.slct);


            //TODO: Figure this out later.
            textAlign = source.textAlign;
            textSize = source.textSize;
            textColor = source.textColor;
            textSkew  = source.textSkew;
            textScale = source.textScale;
            textFakeBold = source.textFakeBold;
            textLinear = source.textLinear;
            textStrikeThru = source.textStrikeThru;
            textSubpixel = source.textSubpixel;
            textUnderline = source.textUnderline;

        }

    }
    public void setStyle() {
        //Meant to be called anonymously.

        Float f = 0.0f;
        String.format("%.5f\n", f);
        Float.toString(f);
    }
    public void setPaints(CanvasPaints paints) {
        Paint stroke = paints.stroke();
        Paint fill = paints.fill();
        Paint select = paints.selected();

        stroke.setColor(strokeColor);
        stroke.setStrokeWidth(strokeWidth);
        stroke.setStrokeCap(cap);
        stroke.setStrokeJoin(join);
        fill.setColor(fillColor);
        select.setColor(selectedColor);

    }

    public interface StyleListener {
        public void onColorPointChange(ColorPoint changed);
        public void onStrokePointChange(StrokePoint changed);
    }

    public class ColorPoint extends Point {
        int color;

        public ColorPoint(int defaultColor){
            this.color = defaultColor;
        }

        public int alpha(){
            return Color.alpha(color);
        }
        public int rgb(){
            return color & 0x00ffffff;
        }
        public float x(){
            return alpha();
        }
        public float y(){
            return rgb();
        }
        public int red(){
            return Color.red(rgb());
        }
        public int green(){
            return Color.green(rgb());
        }
        public int blue(){
            return Color.blue(rgb());
        }

        public void SetRed(int red){
            this.SetRGB(red, blue(), green());
        }
        public void SetGreen(int green){
            this.SetRGB(red(), green, blue());
        }
        public void SetBlue(int blue){
            this.SetRGB(red(), green(), blue);
        }
        public void SetRGB(int red, int green, int blue){
            this.SetARGB(alpha(), red, green, blue);
        }
        public void SetARGB(int alpha, int red, int green, int blue){
            SetColor(Color.argb(alpha, red, green, blue));
        }
        public void SetColor(int color){
            this.color = color;
            if(listener != null){
                listener.onColorPointChange(this);
            }
        }

        public void copycat(ColorPoint colorPoint){
            StyleListener store = listener; listener = null;
            this.SetARGB(colorPoint.alpha(), colorPoint.red(), colorPoint.green(), colorPoint.blue());
            listener = store;
        }

        public void Copycat(ColorPoint colorPoint){
            this.SetARGB(colorPoint.alpha(), colorPoint.red(), colorPoint.green(), colorPoint.blue());
        }
    }
    public class StrokePoint extends Point {
        float
                strokeWidth = STROKE_WIDTH_DEF,
                strokeMiter;
        Paint.Cap cap;
        Paint.Join join;
        Paint.Style style = Paint.Style.STROKE;
        boolean strokeScaling = true;

        public StrokePoint(Paint.Style style) {
            this.style = style;
        }

        public float x() {
            return packDiscreteValues();
        }
        public float y() {
            return packFloatValues();
        }
        private float packDiscreteValues() {
            int theCapBinary = (1 << cap.ordinal());
            int theJoinBinary = (1 << join.ordinal() + 3);
            int theScalingBinary = (1 << (strokeScaling ? 1 : 0) + 1);
            return theScalingBinary | theJoinBinary | theCapBinary;
        }
        private float packFloatValues() {
            //Pack width;
            int width = floatToIntCompressed(strokeWidth);
            int miter = floatToIntCompressed(strokeMiter);
            return Float.parseFloat(width + "." + miter);
        }


        //Must store strokewidth, miter, cap, join, scaling,
        StrokePoint setWidth(float width) {
            strokeWidth = width;
            return this;
        }
        StrokePoint setMiter(float miter) {
            strokeMiter = miter;
            return this;
        }
        StrokePoint setCap(Paint.Cap newCap) {
            cap = newCap;
            return this;
        }
        StrokePoint setJoin(Paint.Join newJoin) {
            join = newJoin;
            return this;
        }
//        StrokePoint setScaling(boolean scaling){
//            strokeScaling = scaling;
//            return this;
//        }
        void SetWidth(float width) {
            float old = strokeWidth;
            strokeWidth = width;
            broadcastPointChange(this);
        }
        void SetMiter(float miter) {
            strokeMiter = miter;
            broadcastPointChange(this);
        }
        void SetCap(Paint.Cap cap) {
            this.cap = cap;
            broadcastPointChange(this);
        }
        void SetJoin(Paint.Join join) {
            this.join = join;
            broadcastPointChange(this);
        }
/*        void SetScaling(boolean scales){
              this.strokeScaling = scales;
              broadcastPointChange(this);
          }
*/

        public void broadcastPointChange(StrokePoint changed) {
            if (listener != null)
                listener.onStrokePointChange(changed);
        }

        public void copycat(StrokePoint other){
            this.setCap(other.cap).setJoin(other.join).setWidth(other.strokeWidth).setMiter(other.strokeMiter);
        }
        public void Copycat(StrokePoint other){
            this.setCap(other.cap).setJoin(other.join).setWidth(other.strokeWidth).SetMiter(other.strokeMiter);
        }
    }
    public class TextPoint extends Point {
        //TODO: Implement when text support is to be worked on.
    }

    public static String[] getLeftRightOfFloat(float f){
        return String.format("%.5f", f).split("\\.");
    }
    public static int floatToIntCompressed(float f){
        int offset = 100000;
        String[] fS = getLeftRightOfFloat(f);
        int first = Integer.parseInt(fS[0]) * (offset);
        int second = Integer.parseInt(fS[1]);
        return first + second;
    }
    public static float decompressIntToFloat(int wasAfloat){
        String intasString = Integer.toString(wasAfloat);
        String decimalPart = intasString.substring(intasString.length() - 5, intasString.length());
        float decimalPartFloat = Integer.parseInt(decimalPart) * 10e-5f;
        float integerPart = Integer.parseInt(intasString.substring(0, intasString.length() - 5));
        return integerPart + decimalPartFloat;
    }


}
