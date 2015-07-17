package FinalCode;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by chrishafley on 3/1/15.
 * TODO: Implement caching at this level
 * TODO: Consider turning into a special panel in order to support layers like those in photo shop.
 */
public class PaperAndroid {

    public static Context context;

    static Layer activePanel;
    public static int latestId = 0;
    public static boolean
            recordStateChanges = false,
            recordDOMChanges = false,
            recordBoundsChanges = false,
            recordMatrixChanges = false,
            recordStyleChanges = false;



    public static short latestPanelId = 0;

    public static Layer activePanel() {
        if(activePanel == null){
            activePanel = new Layer();
        }
        return activePanel;
    }

    public static Viewport activeViewport;
    public static Viewport activeView(){
        return activeViewport;
    }
}
