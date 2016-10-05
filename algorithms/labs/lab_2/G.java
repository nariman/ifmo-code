import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by woofi on 14.11.2015.
 */
public class G {
    static class Queue {
        ArrayList<Integer> queue = new ArrayList<>();
        int cnt;
        int pnt;

        Queue() {
            cnt = 0;
            pnt = 0;
        }

        String iterate(int enter, int patient) {
            while (cnt != 0 && queue.get(pnt) <= enter) {
                //System.out.println(cnt + ":___: " + queue.get(pnt) + " :___: " + +enter + " " + patient);
                cnt--;
                pnt++;
            }

            if (cnt == 0) {
                queue.add(enter + 20);
                cnt++;
                return (enter + 20) / 60 + " " + (enter + 20) % 60;
            }

            if (cnt <= patient) {
                //System.out.println(cnt + ": " + enter + " " + patient);
                int t = enter + (queue.get(pnt) - enter) + cnt * 20;
                queue.add(t);
                cnt++;
                return t / 60 + " " + t % 60;
            } else {
                //queue.add(enter);
                return enter / 60 + " " + enter % 60;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("saloon.in"));
        PrintWriter pw = new PrintWriter("saloon.out");

        Queue q = new Queue();
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            pw.println(q.iterate(sc.nextInt() * 60 + sc.nextInt(), sc.nextInt()));
        }

        pw.close();
        sc.close();
    }

    static class Scanner {
        BufferedReader br;
        StringTokenizer t;

        Scanner(File file) throws Exception {
            br = new BufferedReader(new FileReader(file));
            t = new StringTokenizer("");
        }

        boolean hasNext() throws Exception {
            while (!t.hasMoreTokens()) {
                String line = br.readLine();
                if (line == null)
                    return false;
                t = new StringTokenizer(line);
            }
            return true;
        }

        String next() throws Exception {
            if (hasNext()) {
                return t.nextToken();
            }
            return null;
        }

        int nextInt() throws Exception {
            return Integer.parseInt(next());
        }

        double nextDouble() throws Exception {
            return Double.parseDouble(next());
        }

        void close() throws Exception {
            br.close();
        }
    }
}
