import java.io.*;
import java.util.*;

public class Pairs {
    static class IntPair implements Comparable<IntPair> {
        int f, s;

        IntPair(int f, int s) {
            this.f = f;
            this.s = s;
        }

        public String toString() {
            return "(" + f + ", " + s + ")";
        }

        public int compareTo(IntPair that) {
            if (this.f == that.f) {
                return Integer.compare(this.s, that.s);
            } else {
                return Integer.compare(this.f, that.f);
            }
        }

        public int hashCode() {
            return f * 1000_000_007 + s;
        }

        public boolean equals(Object o) {
            if (o instanceof IntPair) {
                IntPair that = (IntPair) o;
                return this.f == that.f && this.s == that.s;
            }
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        PrintStream out = System.out;

        Collection<Object> pairs = new HashSet<>();

        Scanner in = new Scanner(new File("pairs.in"));
        while (in.hasNext()) {
            pairs.add(new IntPair(in.nextInt(), in.nextInt()));
        }
        in.close();
        out.println(pairs);
        
        out.close();
    }    
}
