package test;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler {
    PrintWriter out;
    Scanner in;
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        out=new PrintWriter(outToClient);
        in=new Scanner(inFromclient);
        DictionaryManager dm = new DictionaryManager();
        String[] afterSplit = in.next().split(",");
        String funcChar = afterSplit[0];
        String[] args = new String[afterSplit.length-1];
        System.arraycopy(afterSplit, 1, args, 0, afterSplit.length-1);
        if(!funcChar.equals("Q")&&!funcChar.equals("C")) {
            out.println("not a valid pattern");
            return;
        }
        if(funcChar.equals("Q"))
        {
            if(dm.query(args))
                out.println("true");
            else
                out.println("false");
        }
        else
        {
            if(dm.challenge(args))
                out.println("true");
            else
                out.println("false");
        }
        out.flush();
    }

    @Override
    public void close() {
        in.close();
        out.close();
    }
}
