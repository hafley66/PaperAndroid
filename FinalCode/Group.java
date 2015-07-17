package FinalCode;

import FinalCode.IndexList.Index;
import FinalCode.points.Point;

/**
 * Created by chrishafley on 4/12/15.
 */
public class Group extends LinkedMatrix implements Entity.Listener{

    public Index<Entity> index;
    public Index<Group> subLists;

    public Group(){
        this(PaperAndroid.activePanel());
    }
    //Meant for private construction inside of the panel class.
    Group (Layer panel){
        panel.add(this);
    }

    public void onMatrixPointChange(MatrixPoint changed, Point old) {
        //tell panel!
        //not if it mark and sweep.
    }

    public void onBoundsPointMoveRequest(Bounds.BoundPoint bndPt, Point requestedPoint) {
        Point dist = requestedPoint.minusC(bndPt.addC(this.translate));
        //TODO: Proper Translation of children.
    }

    public void computeBounds(Bounds f) {
        f.setEmpty();
        for(Entity child : index){
            f.union(child.localBounds);
        }
    }

    public LinkedMatrix get(int indx){
        return index.get(indx);
    }
    public void add(Entity child){
        index.add(child);
    }
    public void addToTop(Entity child){
        index.addToRight(child);
    }
    public void addToBottom(Entity child){
        index.addToLeft(child);
    }
    public void insert(int indx, Entity child){
        index.insert(indx, child);
    }
    public void swap(Entity child0, Entity child1){
        index.swap(child0, child1);
    }
    public void setParent(LinkedMatrix parent){
        if(parent instanceof Group){
            setParent((Group)parent);
        }
    }
    public void setParent(Group list){
        super.setParent(list);
        index.setSuperIndex(list.index);
    }

    @Override
    public void setDirtyCascade() {
        this._actualSetDirty();
        for(Entity child : index){
            child.setDirtyCascade();
        }
    }

    @Override
    public void onPSTFChange(Entity self, int code) {
        //Do group caching logic here.
        this.setDirtyCascade();
    }

    public void addGroup(Group list){

    }
}
