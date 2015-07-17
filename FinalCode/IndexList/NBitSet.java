package FinalCode.IndexList;

import java.util.BitSet;
import java.util.Iterator;

/**
 * Created by chrishafley on 4/14/15.
 */
public class NBitSet extends BitSet {
    protected BitIndexIterator i_iter = new BitIndexIterator(this);
    int size = 0;

    @Override
    public void set(int index) {
        if(!super.get(index)){
            size++;
            super.set(index);
        }
    }

    @Override
    public void flip(int index) {
        if(!super.get(index))
            size++;
        else
            size--;
        super.flip(index);
    }

    @Override
    public void clear(int index) {
        if(super.get(index)) {
            size--;
            super.clear(index);
        }
    }

    public BitSet shiftRightBy(int amount, int left){
        BitSet old = this.get(left, this.size());
        this.clear(left, this.size());
        int s = 0;
        while((s = old.nextSetBit(s)) != -1){
            this.set(left + amount + s);
            s++;
        }
        return this;
    }
    public BitSet shiftBy(int dir, int amount, int side){
        int left = side;
        int right = this.size();
        if(dir < 0){
            left = 0;
            right = side + 1;
        }

        BitSet old = this.get(left, right);
        this.clear(left, right);
        int s = 0;
        while((s = old.nextSetBit(s)) != -1){
            this.set(left + amount + s);
            s++;
        }
        return this;
    }

    public BitSet shiftKillRight(int left, int right){
        return shift_kill_bit(1, left, right);
    }
    public BitSet shiftKillRight(int left){
        return shiftKillRight(left, this.length());
    }
    public BitSet shiftKillLeft(int left, int right){
        return shift_kill_bit(-1, left, right);
    }
    public BitSet shiftKillLeft(int left){
        return shiftKillLeft(left, this.length());
    }
    private BitSet shift_kill_bit(int dir, int left, int right){
        if(left < right){
            int offset = 0;
            if(dir < 0){
                offset = 1;
                this.clear(left);
            } else {
                this.clear(right);
            }

            BitSet old = this.get(left + offset, right + offset);
            this.clear(left + offset, right + offset);
            int s = 0;
            while((s = old.nextSetBit(s)) != -1){
                super.set((left + (1 - offset)) + s);
                s++;
            }
        }
        return this;
    }

    protected int get_nth_set_bit(int n){
        i_iter.start = 0;
        int c = 0;
        for(Integer i : i_iter){
            if(c == n)
                return i;
            else
                c++;
        }
        return -1;
    }
    protected int get_num_set_bits_up_to(int index){
        i_iter.start = 0;
        int c = 0;
        for(Integer i : i_iter){
            if(i == index)
                return c + 1;
            else
                c++;
        }
        return c;
    }

    public BitIndexIterator biterator(){
        return new BitIndexIterator(this);
    }
    public static class BitIndexIterator implements Iterator<Integer>, Iterable<Integer>{
        BitSet listish;
        int start = 0;
        public BitIndexIterator(BitSet notNull){
            listish = notNull;
        }

        public Iterator<Integer> iterator(){
            return this;
        }
        public boolean hasNext() {
            return listish.nextSetBit(start) != -1;
        }
        public Integer next() {
            start = listish.nextSetBit(start);
            return start++;
        }
        public void remove() {}
    }
}
