import java.io.*;
import java.util.*;

public class Dictionary {

    public static void main(String[] args) throws Exception {
        Map<String, String> dictionary = new TreeMap<>();
        {
            Scanner in = new Scanner(new File("dictionary.in"));
            while (in.hasNext()) {
                dictionary.put(in.next(), in.next());
            }
            in.close();
        }
        {
            Scanner in = new Scanner(new File("text.in"));
            while (in.hasNext()) {
                System.out.println(dictionary.get(in.next()));
            }
            in.close();
        }
        
        System.out.close();
    }    
}