package test;
import java.io.IOException;
import java.io.*;


public class Dictionary {
    //Cache for the words that exist.
    private final CacheManager existWords;
    //Cache for the words that don't exist.
    private final CacheManager notExistWords;
    //The bloom filter cache.
    private final BloomFilter bloomFilter;
    private String[] filesNames;

    public Dictionary(String... filesNames) {
        existWords = new CacheManager(400, new LRU());
        notExistWords = new CacheManager(100, new LFU());
        bloomFilter = new BloomFilter(256, "MD5", "SHA1");
        this.filesNames = filesNames;
        for (String filename : filesNames) {
            File file = new File(filename);
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    String[]words=st.split(" ");
                    for (String word : words)
                        if(word.compareTo("")!=0)
                        bloomFilter.add(word);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Given word, search it inside existing words cache, than inside not existing words cache, than inside bloom filter cache, if not found, returns false.
    //If the word found inside the bloom filter cache, updates the other caches.
    public Boolean query(String word) {
        if (existWords.wordsInCache.contains(word))
            return true;
        if (notExistWords.wordsInCache.contains(word))
            return false;
        if (bloomFilter.contains(word)) {
            existWords.add(word);
            return true;
        }
        notExistWords.add(word);
        return false;
    }

    //Given word, search it inside the files, if not found or exception, return false.
    //If the word is found, updates the cache and returns true.
    public Boolean challenge(String word) {
        try {
            if (IOSearcher.search(word,filesNames)) {
                existWords.add(word);
                return true;
            }
            notExistWords.add(word);
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }


}
