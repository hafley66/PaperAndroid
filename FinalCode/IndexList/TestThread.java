package FinalCode.IndexList;

import java.util.BitSet;


public class TestThread {

    public static BitSet shiftRightBy(int amount, int left, BitSet shift){
        BitSet old = shift.get(left, shift.size());
        shift.clear(left, shift.size());
        int s = 0;
        while((s = old.nextSetBit(s)) != -1){
            shift.set(left + amount + s);
            s++;
        }

        return shift;
    }

	public static BitSet shiftBitSetRight(int left, int right, BitSet shift){

        BitSet old = shift.get(left, right);
        shift.clear(left, right);
        int s = 0;
        while((s = old.nextSetBit(s)) != -1){
            shift.set((left + 1) + s);
            s++;
        }
        return shift;
    }
	
	public static BitSet shiftBitSetRight(int left, BitSet m){
		return shiftBitSetRight(left, m.size(), m);
	}

	public static BitSet shiftBitSetLeft(int left, int right, BitSet shift){
		if( left < right){
			BitSet old = shift.get(left + 1, right + 1);
			shift.clear(left + 1, right + 1);
			
			int s = 0;
			while((s = old.nextSetBit(s)) != -1){
				shift.set(left + s);
				s++;
			}
		}

        return shift;
    }
	
	public static BitSet shiftBitSetLeft(int left, BitSet shift){
		return shiftBitSetLeft(left, shift.size() - 1, shift);
	}
	
	public static void main(String[] args) {
		BitSet s = new BitSet();
		s.set(1, 4);
		System.out.println(s.toString());
		shiftBitSetLeft(0, 3, s);
		shiftBitSetLeft(0, 3, s);
		System.out.println(s.toString());
		
		IndexedList<Integer> list = new IndexedList<>();
		Index<Integer> one = new Index<>(list);
		Index<Integer> two = new Index<>(list);
		
		
	}
}