package test;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.security.MessageDigest;
import java.lang.Math;

public class BloomFilter {
    BitSet bitArray = new BitSet();
    private final int numBits;
    private String[] algs;

    public BloomFilter(int numBits, String... algs) {
        this.numBits = numBits;
        this.algs = algs;
        bitArray.set(0, numBits, false);
    }

    //Given a word, insert it to the bloom filter.
    public void add(String word) {
        for (String alg : algs) {
            try {
                MessageDigest md = MessageDigest.getInstance(alg);
                byte[] bts = md.digest(word.getBytes());
                BigInteger big = new BigInteger(bts);
                int index = Math.abs(big.intValue() % numBits);
                if (!bitArray.get(index))
                    bitArray.flip(index);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //True - the given word is in the bloom filter.
    public boolean contains(String word) {
        for (String alg : algs) {
            try {
                MessageDigest md = MessageDigest.getInstance(alg);
                byte[] bts = md.digest(word.getBytes());
                BigInteger big = new BigInteger(bts);
                int index = Math.abs(big.intValue() % numBits);
                if (!bitArray.get(index))
                    return false;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public String toString() {
//        BigInteger toNumber=new BigInteger(bitArray.toByteArray());
//        return toNumber.toString();
        String str = "";
        for (int i = 0; i < bitArray.length(); i++) {
            if (bitArray.get(i)) {
                str += '1';
                continue;
            }
            str += '0';
        }
        return str;
    }
}

