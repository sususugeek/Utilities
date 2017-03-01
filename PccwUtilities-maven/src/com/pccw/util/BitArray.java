package com.pccw.util;
/**
 * Create a simple class containing an array of bits.
 * 
 * @author 01517028
 *
 */
public class BitArray{
    private long bitX64[] = null;
    private final static int BIT_SIZE = 64;
    
    public BitArray(int size) {
        bitX64 = new long[size / BIT_SIZE + (size % BIT_SIZE == 0 ? 0 : 1)];
    }

    public boolean getBit(int pos) {
        return ((bitX64[pos / BIT_SIZE] >> pos & 1l) == 1l);
    }

    public void setBit(int pos, boolean b) {
        long b8 = bitX64[pos / BIT_SIZE];
        long posBit = (long) (1l << (pos % BIT_SIZE));
        if (b) {
            b8 |= posBit;
        } else {
            b8 &= ~posBit;
        }
        bitX64[pos / BIT_SIZE] = b8;
    }

    public long getLongValueOfAllBit(){
    	long ret = 0l;
    	for(int i=0; i<bitX64.length; i++){
    		ret |= bitX64[i];
    	}
    	return ret;
    }
    
    public boolean isAllFalse(){
    	return (this.getLongValueOfAllBit() == 0l);
    }
    
    public int getStorageArraySize(){
    	return bitX64.length;
    }
    
    public int getArraySize(){
    	return bitX64.length * BIT_SIZE;
    }
}