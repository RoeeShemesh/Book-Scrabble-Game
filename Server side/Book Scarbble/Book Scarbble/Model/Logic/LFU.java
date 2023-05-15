package test;
import java.util.LinkedHashMap;

//Least Frequently Used
public class LFU implements CacheReplacementPolicy{
    LinkedHashMap<String,Integer> wordsInCache=new LinkedHashMap<String,Integer>();
    
    //Given word, add it to the cache (with value 1), if the word is already exist inside the cache, remove it and than add it to the cache (with previous value + 1).
    public void add(String word){
        int newValue=1;
        if(wordsInCache.containsKey(word)) {
            newValue = wordsInCache.get(word) + 1;
            wordsInCache.remove(word);
        }
        wordsInCache.put(word,newValue);
    }
    
    //Returns the key with the minimum value(the word we should remove).
    public String remove() {
        String key=wordsInCache.keySet().iterator().next();
        int min = wordsInCache.get(key);
        for(String word : wordsInCache.keySet()){
            if(wordsInCache.get(word) < min) {
                min = wordsInCache.get(word);
                key = word;
            }
        }
        return key;
    }
}
