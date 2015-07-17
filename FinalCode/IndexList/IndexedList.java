package FinalCode.IndexList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IndexedList<T> implements Iterable<T>{
    ArrayList<Index<T>> indices = new ArrayList<>();
    ArrayList<T> elements = new ArrayList<>();
    public Map<T, Index<T>> parents = new HashMap<>();
    public Index<T> root = createRoot();

    private Index<T> createRoot(){
        Index<T> root = new Index<>(this);
        root.id = 0;
        indices.add(root);
        root.name = "Root";
        return root;
    }
    public Index<T> createIndex(){
        Index<T> n = new Index<>(this);
        root.addSubIndex(n);
        n.id = indices.size();
        n.name = "ID " + n.id;
        indices.add(n);
        return n;
    }
    public Index<T> getIndex(int index){
        return indices.get(index);
    }

    Index<T> getParent(T element){
        return parents.get(element);
    }
    public void swapParents(T element, Index<T> newP){
        int oldIndx = i(element);
        Index<T> oldP = getParent(element);

        if(oldP != newP){
            oldP.clear(oldIndx);
            newP.set(oldIndx);
            parents.put(element, newP);
        }
    }

    public void move(int from,  int to){
        if(from != to){
            T fromE = get(from);
            Index<T> r = getParent(fromE);
            __list_add(to, fromE);
            if(to < from) //watch for off by one error
                __list_remove(from + 1);
            else          //inserting at to does not affect from if its inserted after from.
                __list_remove(from);
            r.set(to);
        }
    }
    public int i(T element){
        return elements.indexOf(element);
    }
    public void insert(int indx, T element){
        int oldIndx = i(element);
        if(oldIndx == -1){
            _newInsert(indx, element);
        } else if(oldIndx != indx){
            move(oldIndx, indx);
        }
    }
    private void _newInsert(int indx, T element){
        __list_add(indx, element);
        root.set(indx);
        parents.put(element, root);
    }
    public void add(T element){
        this.addToRight(element);
    }
    public void addToRight(T element){
        this.insert(elements.size(), element);
    }
    public void addToLeft(T element){
        this.insert(0, element);
    }
    public void remove(T element){
        int index;
        if((index = i(element)) != -1){
            for(Index<T> i : indices)
                i.indexes.shiftKillLeft(index);
        }
    }
    public void swap(T i, T j){
        //Swap in bitset indices of the groups.
        int ii = i(i);
        int jj = i(j);
        getParent(i).swap(ii, jj);
        getParent(j).swap(jj, ii);

        //Swap in the actual list.
        elements.set(ii, j);
        elements.set(jj, i);
    }
    public void move(T element, T other){
        this.move(element, i(other));
    }
    public void move(T element, int toIndx){
        move(i(element), toIndx);
    }

    public T get(int index){
        if(!elements.isEmpty()){

            if(index > elements.size())
                index = elements.size()-1;
            else if(index < 0)
                index = 0;
            return elements.get(index);
        }
        return null;
    }

    public boolean contains(T element){
        return i(element) != -1;
    }
    public Iterator<T> iterator(){
        return elements.iterator();
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("IndexedList : {\n");

        int i = 0;
        for(T ent : elements){
            sb.append(
                    "\t" + i++ +
                    "\t : <" +
                    ent.toString() +
                    ", " +
                    getParent(ent).name +
                     ">\n");

        }
        sb.append("}\n INDICES: {\n");
        for(Index<T> index : indices){
            sb.append(index.toString() + "\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    private void __list_add(int indx, T element){
        elements.add(indx, element);
        for(Index<T> i : indices){
            if(i.indexes.length() >= indx)
                i.indexes.shiftKillRight(indx);
        }
    }
    private void __list_remove(int indx){
        elements.remove(indx);
        for(Index<T> i : indices){
            i.indexes.shiftKillLeft(indx);
        }
    }
}
