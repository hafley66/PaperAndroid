package FinalCode;

import android.graphics.Canvas;

import java.util.ArrayList;

import FinalCode.IndexList.IndexedList;

/**
 * Created by chrishafley on 4/12/15.
 */
public class Layer implements Viewport.Layer{

    public
    ArrayList<Group> groups = new ArrayList<>();

    final
    Group root;

    protected
    IndexedList<Entity> L = new IndexedList<>();
    IndexedList<Group> G = new IndexedList<>();

    public Layer()
    {
        PaperAndroid.activePanel = this;
        root = new Group(this);
        PaperAndroid.activeViewport.addLayer(this);
    }

    public
    void
    draw(Canvas canvas, CanvasPaints paints){
        canvas.save();
        for(Entity drawable : L){
            canvas.save();
            drawable.draw(canvas, paints);
            canvas.restore();
        }
        canvas.restore();
    }

    public LinkedMatrix get(int index){
        return L.get(index);
    }
    public void add(Entity child) {
        L.add(child);
        child.setParent(this.root);
    }
    public void addToTop(Entity child){
        L.root.addToRight(child);
    }
    public void addToBottom(Entity child){
        L.addToLeft(child);
    }
    public void insert(int index, Entity element){
        L.insert(index, element);
    }
    public void swapOrder(Entity t0, Entity t1){
        L.swap(t0, t1);
    }
    public void moveTo(Entity t0, Entity t1){
        L.move(t0, t1);
    }
    public void remove(Entity t){
        L.remove(t);
    }
    public void remove(Group list){
        groups.remove(list);
    }


    //TODO: Define semantics of a group moving between layers/panels
    public void add(Group list){
        if(list != null &&
                !G.contains(list)){
            G.add(list);
            list.subLists = G.createIndex();
            list.index = L.createIndex();
        }
    }
}
