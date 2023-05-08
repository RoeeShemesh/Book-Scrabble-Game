package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class IOSearcher {

    //Given word and files, search the word inside the files.
    //If the word found, returns true, else return false.
    public static Boolean search(String word,String...files) throws IOException {
        for(String fileName : files){
            File f1=new File(fileName);
            String[] words;
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while((s=br.readLine())!=null)
            {
                words=s.split(" ");
                for (String w : words)
                {
                    if (w.equals(word))
                    {
                        fr.close(); //new
                        return true;
                    }
                }
            }
            fr.close();
        }
        return false;
    }
}
