package test;
import java.util.LinkedHashSet;

//Least Recently Used.
public class LRU implements CacheReplacementPolicy{
    LinkedHashSet<String> wordsInCache=new LinkedHashSet<String>();
    
    //Given word, add it to the cache, if the word is already exist, first remove it and than add.
    public void add(String word){
        if(wordsInCache.contains(word))
                wordsInCache.remove(word);
        wordsInCache.add(word);
    }
    
    //Returns the word we should remove from the cache. 
    public String remove(){
        return wordsInCache.iterator().next();
    }
}
