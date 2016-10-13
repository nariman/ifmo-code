import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Created by woofi on 19.12.2015.
 */
public class C {
    static class AVLTree {
        static class Node {
            int key;
            int height;
            Node left;
            Node right;

            Node(int k) {
                key = k;
                height = 1;
                left = null;
                right = null;
            }
        }

        Node head;

        AVLTree() {
            head = null;
        }

        private int getHeight(Node v) {
            return (v != null) ? v.height : 0;
        }

        private void fixHeight(Node v) {
            v.height = Math.max(getHeight(v.left), getHeight(v.right)) + 1;
        }

        private int balanceFactor(Node v) {
            return getHeight(v.right) - getHeight(v.left);
        }

        private Node rotateLeft(Node v) {
            Node r = v.right;
            v.right = r.left;
            r.left = v;
            fixHeight(v);
            fixHeight(r);
            return r;
        }

        private Node rotateRight(Node v) {
            Node l = v.left;
            v.left = l.right;
            l.right = v;
            fixHeight(v);
            fixHeight(l);
            return l;
        }

        private Node balance(Node v) {
            fixHeight(v);

            if (balanceFactor(v) == 2) {
                if (balanceFactor(v.right) < 0) {
                    v.right = rotateRight(v.right);
                }
                return rotateLeft(v);
            }
            if (balanceFactor(v) == -2) {
                if (balanceFactor(v.left) > 0) {
                    v.left = rotateLeft(v.left);
                }
                return rotateRight(v);
            }

            return v;
        }

        private Node removeRecursiveHelperFindMin(Node v) {
            return (v.left != null) ? removeRecursiveHelperFindMin(v.left) : v;
        }

        private Node removeRecursiveHelperRemoveMin(Node v) {
            if (v.left == null) {
                return v.right;
            }
            v.left = removeRecursiveHelperRemoveMin(v.left);
            return balance(v);
        }

        private Node findRecursive(Node v, int k) {
            if (v == null || k == v.key) {
                return v;
            }
            if (k < v.key) {
                return findRecursive(v.left, k);
            } else {
                return findRecursive(v.right, k);
            }
        }

        private Node insertRecursive(Node v, int k) {
            if (v == null) {
                return new Node(k);
            }

            if (k < v.key) {
                v.left = insertRecursive(v.left, k);
            } else {
                v.right = insertRecursive(v.right, k);
            }

            return balance(v);
        }

        private Node removeRecursive(Node v, int k) {
            if (v == null) {
                return null;
            }

            if (k < v.key) {
                v.left = removeRecursive(v.left, k);
            } else if (k > v.key) {
                v.right = removeRecursive(v.right, k);
            } else {
                Node l = v.left;
                Node r = v.right;
                if (r == null) {
                    return l;
                }
                Node min = removeRecursiveHelperFindMin(r);
                min.right = removeRecursiveHelperRemoveMin(r);
                min.left = l;
                return balance(min);
            }

            return balance(v);
        }

        Node find(int k) {
            return findRecursive(head, k);
        }

        Node insert(int k) {
            if (find(k) != null) {
                return head;
            }
            head = insertRecursive(head, k);
            return head;
        }

        Node remove(int k) {
            if (find(k) != null) {
                head = removeRecursive(head, k);
                return head;
            }
            return null;
        }

        Node next(int k) {
            if (head == null) {
                return null;
            }
            Node current = head;
            Node successor = null;

            while (current != null) {
                if (current.key > k) {

                    successor = current;
                    current = current.left;
                } else {
                    current = current.right;
                }
            }

            return successor;
        }

        Node prev(int k) {
            if (head == null) {
                return null;
            }
            Node current = head;
            Node successor = null;

            while (current != null) {
                if (current.key < k) {
                    successor = current;
                    current = current.right;
                } else {
                    current = current.left;
                }
            }

            return successor;
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("bst.in"));
        PrintWriter pw = new PrintWriter(new File("bst.out"));

        AVLTree tree = new AVLTree();

        AVLTree.Node t;
        while (sc.hasNext()) {
            switch (sc.next()) {
                case "insert":
                    tree.insert(sc.nextInt());
                    break;
                case "delete":
                    tree.remove(sc.nextInt());
                    break;
                case "exists":
                    if (tree.find(sc.nextInt()) != null) {
                        pw.println("true");
                    } else {
                        pw.println("false");
                    }
                    break;
                case "next":
                    t = tree.next(sc.nextInt());
                    if (t != null) {
                        pw.println(t.key);
                    } else {
                        pw.println("none");
                    }
                    break;
                case "prev":
                    t = tree.prev(sc.nextInt());
                    if (t != null) {
                        pw.println(t.key);
                    } else {
                        pw.println("none");
                    }
                    break;
            }
        }

        sc.close();
        pw.close();
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
