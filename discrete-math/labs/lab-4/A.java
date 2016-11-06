/**
 * Nariman Safiulin (woofilee)
 * File: A.java
 * Created on: Nov 3, 2016
 */

import java.io.*;
import java.util.*;

public class A {
    private static final String PROBLEM_NAME = "fullham";

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(PROBLEM_NAME + ".in"));
        BufferedWriter out = new BufferedWriter(new FileWriter(PROBLEM_NAME + ".out"));

        solve(in, out);

        out.close();
        in.close();
    }

    private static void solve(BufferedReader in, BufferedWriter out) throws Exception {
        final int n = Integer.parseInt(in.readLine());
        boolean matrix[][] = new boolean[n][n];
        ArrayList<Integer> queue = new ArrayList<Integer>(n) {
            @Override
            public Integer get(int index) {
                return super.get(index % n);
            }

            @Override
            public Integer set(int index, Integer element) {
                return super.set(index % n, element);
            }
        };

        for (int i = 0; i < n; i++) {
            queue.add(i);
            String l = in.readLine();
            for (int j = 0; j < i; j++) {
                matrix[i][j] = matrix[j][i] = l.charAt(j) == '1';
            }
        }

        int head = 0, unchanged = 0;
        for (int k = 0; k < n * (n - 1); k++) {
            if (!matrix[queue.get(head)][queue.get(head + 1)]) {
                int left = head + 1, middle = head + 2;
                while (!matrix[queue.get(head)][queue.get(middle)] | !matrix[queue.get(head + 1)][queue.get(middle + 1)])
                    middle++;
                while (left <= middle)
                    Collections.swap(queue, left++, middle);
                unchanged = 0;
            } else if (++unchanged > n) break;
            head++;
        }

        // queue.forEach(v -> out.write((v + 1) + " ")); // IOException in lambda???!?!? SRSLY???!! FUCKING JAVA!!!!!!
        for (Integer v : queue) {
            out.write((v + 1) + " ");                    // good one, cycle is my favorite, yeah
        }
    }
}
