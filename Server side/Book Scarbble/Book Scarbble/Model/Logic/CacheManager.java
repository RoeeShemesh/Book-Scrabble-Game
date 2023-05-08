package test;
import java.util.HashSet;

public class CacheManager {
    //LRU or LFU.
    private final CacheReplacementPolicy crp;
    //The maximum size od the cache.
    private final int size;
    HashSet<String> wordsInCache=new HashSet<String>();

    public CacheManager(int size,CacheReplacementPolicy crp){
        this.size=size;
        this.crp=crp;
    }

    //Returns true if the word is inside the cache, if not return false.
    public boolean query(String word){
        return wordsInCache.contains(word);
    }
	
    //Given word, updates the crp, add it to the cache, remove other word from cache when needed.
    public void add(String word){
        crp.add(word);
        wordsInCache.add(word);
        if (wordsInCache.size()>size)
            wordsInCache.remove(crp.remove());
    }

}
