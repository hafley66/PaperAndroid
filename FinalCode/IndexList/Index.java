package FinalCode.IndexList;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;


public class Index<T> implements Iterable<T>{
    NBitSet indexes = new NBitSet();
    public int id = 0;
    private static final long serialVersionUID = 1L;
    public IndexedList<T> list;
    Index<T> superIndex = null;
    ArrayList<Index<T>> subIndices = new ArrayList<>();
    String name;

    protected Index(IndexedList<T> notNull){
        this.list = notNull;
    }

    public int size() {
        return indexes.size;
    }
    public T get(int index){
        int i = indexes.get_nth_set_bit(index);
        return list.get(i);
    }
    public void add(T element){
        addToRight(element);
    }
    public void addToRight(T element){
        insert(size(), element);
    }
    public void addToLeft(T element){
        insert(0, element);
    }
    public void insert(int relativeIndx, T element){
        int absoluteIndx;
        if(relativeIndx >= size())
            absoluteIndx = length();
        else
            absoluteIndx = indexes.get_nth_set_bit(relativeIndx);

        list.insert(absoluteIndx, element);
        list.swapParents(element, this);
    }
    public void addAll(T[] elements){
        for(T child : elements)
            add(child);
    }
    public void remove(T element){
        if(list.getParent(element) == this){
            list.swapParents(element, list.root);
        }
    }
    public void swap(T i, T j){
        list.swap(i, j);
    }
    public void move(T element, T other){
        list.move(element, other);
    }
    public void setSuperIndex(Index<T> supur){
        if(superIndex != null){
            superIndex.subIndices.remove(this);
            superIndex.superIndex = null;
        }
        if(supur != null && supur != this && supur.superIndex != this){
            superIndex = supur;
        } else {
            superIndex = list.root;
        }
        superIndex.subIndices.add(this);
    }
    public Index<T> getSuperIndex(){
        return superIndex;
    }
    public void addSubIndex(Index<T> sub){
        sub.setSuperIndex(this);
    }
    public void removeSubIndex(Index<T> sub){
        if(sub != null && sub.superIndex == this){
            subIndices.remove(sub);
            sub.superIndex = list.root;
            sub.superIndex.subIndices.add(this);
        }
    }

    public int indexOf(T element){
        return indexes.get_num_set_bits_up_to(list.i(element));
    }
    public boolean contains(T element){
        return this.indexes.get(list.i(element));
    }
    public BitSet range(){
        BitSet other = new BitSet();
        other.or(this.indexes);
        for(Index<T> sub : subIndices)
            sub.or(other);
        return other;
    }
    public int length(){
        return indexes.length();
    }

    public boolean isSubIndexOf(Index<T> question){
        Index<T> curr = this;
        while(curr != null){
            if(curr == question)
                return true;
            curr = curr.superIndex;
        }
        return false;
    }
    public boolean isSuperIndexOf(Index<T> question){
        return question.isSubIndexOf(question);
    }

    protected void set(int indx){
        indexes.set(indx);
    }
    protected void clear(int indx){
        indexes.clear(indx);
    }
    protected boolean getB(int indx){
        return indexes.get(indx);
    }
    protected void flip(int indx){
        indexes.flip(indx);
    }
    protected void or(BitSet set){
        set.or(range());
    }
    protected void swap(int x, int y){
        indexes.flip(x);
        indexes.flip(y);
    }
    protected void shiftKillLeft(int left, int right){
        indexes.shiftKillLeft(left, right);
    }
    protected void shiftKillLeft(int left){
        indexes.shiftKillLeft(left);
    }
    protected void shiftKillRight(int left, int right){
        indexes.shiftKillRight(left, right);
    }
    protected void shiftKillRight(int i){
        indexes.shiftKillRight(i);
    }

    public Iterator<T> iterator() {
        return new IndexElementIterator<>(this);
    }
    public static class IndexElementIterator<T> implements Iterator<T> {
        Index<T> listish;
        int start = 0;

        public IndexElementIterator(Index<T> notNull) {
            listish = notNull;
        }

        @Override
        public boolean hasNext() {
            return listish.indexes.nextSetBit(start) != -1;
        }

        @Override
        public T next() {
            start = listish.indexes.nextSetBit(start);
            return listish.list.get(start++);
        }

        public void remove() {
        }
    }
    public static void main(String[] args){


        NBitSet nb = new NBitSet();
        nb.set(0);
        nb.set(1);
        nb.set(2);
        nb.set(3);
        nb.set(4);
        System.out.println(nb.get_nth_set_bit(0) + " : " + nb.get_nth_set_bit(1) + " : " + nb.get_nth_set_bit(2));

        System.out.println("LENGTH " + nb.length() + " : SIZE " + nb.size);
        nb.set(6);
        System.out.println("BEFORE SHIFT : " + nb.toString());
        nb.shiftKillRight(2);
        System.out.println("AFTER  SHIFT : " + nb.toString());

        IndexedList<Float> list = new IndexedList<>();
        Index<Float> one = list.createIndex();
        System.out.println(list.toString());
        Index<Float> two = list.createIndex();
        System.out.println(list.toString());
        one.addAll(new Float[]{1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f});
        System.out.println(list.toString());

        list.addToLeft(10f);
        list.addToRight(11f);
        System.out.println(list.toString());
        one.addToRight(10f);
        one.insert(3, 11f);
        System.out.println(list.toString());
        two.add(11f);
        one.insert(1, 10f);
        one.insert(3, 100f);
        System.out.println(list.toString());
        one.remove(10f);
        System.out.println(list.toString());
    }
    public String toString(){
        iterator();
        StringBuilder sb = new StringBuilder(name + " : {");
        if(!indexes.isEmpty()){
            for(T ele : this){
                sb.append(ele.toString());
                sb.append(", ");
            }
            if(sb.length() > ", ".length())
                sb.replace(sb.toString().length() - ", ".length(), sb.toString().length(), "");
        }
        sb.append("}");
        return sb.toString();
    }
}