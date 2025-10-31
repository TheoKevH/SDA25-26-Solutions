// Just normal AVL Tree. Check repo resource soal lab tahun lalu, basically the same...
import java.io.*;
import java.util.*;

public class Lab6 {
    private static InputReader in;
    private static PrintWriter out;
    private static AVLTree avlTree = new AVLTree();
    private static int idCounter = 1;
    static String tiers = "SABCDEF";

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInteger();

        for (int i = 0; i < N; i++) {
            int tier = tiers.indexOf(in.next());
            int battlePoint = in.nextInteger();
            Pair pair = new Pair(tier, battlePoint, idCounter++);
            avlTree.root = avlTree.insert(avlTree.root, pair);
        }

        int Q = in.nextInteger();

        for (int i = 0; i < Q; i++) {
            String query = in.next();
            switch (query) {
                case "G":
                    int tier = tiers.indexOf(in.next());
                    int battle = in.nextInteger();
                    Pair addPair = new Pair(tier, battle, idCounter++);
                    avlTree.ans = null;
                    avlTree.root = avlTree.insert(avlTree.root, addPair);

                    // jika menjadi root, cetak dirinya sendiri
                    if (avlTree.root.pairChar.tier == addPair.tier &&
                        avlTree.root.pairChar.battlePoint == addPair.battlePoint) {
                        out.println(tiers.charAt(addPair.tier) + " " + addPair.battlePoint);
                    } else {
                        avlTree.ans = null;
                        avlTree.findParent(avlTree.root, addPair, null);
                        out.println(tiers.charAt(avlTree.ans.tier) + " " + avlTree.ans.battlePoint);
                    }
                    break;

                case "R":
                    int delTier = tiers.indexOf(in.next());
                    int delBattle = in.nextInteger();
                    Pair delPair = new Pair(delTier, delBattle);
                    avlTree.ans = null;
                    avlTree.root = avlTree.delete(avlTree.root, delPair);
                    if (avlTree.ans == null)
                        out.println(-1);
                    else
                        out.println(tiers.charAt(avlTree.ans.tier) + " " + avlTree.ans.battlePoint);
                    break;

                case "T":
                    if (avlTree.root == null) {
                        out.println(-1);
                        break;
                    }
                    List<Pair> top = avlTree.findTopThreeNodes(avlTree.root);
                    for (Pair p : top) {
                        out.print(tiers.charAt(p.tier) + " " + p.battlePoint + " ");
                    }
                    out.print("| ");
                    List<Pair> bottom = avlTree.findBottomThreeNodes(avlTree.root);
                    for (Pair p : bottom) {
                        out.print(tiers.charAt(p.tier) + " " + p.battlePoint + " ");
                    }
                    out.println();
                    break;
            }
        }
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInteger() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

}

class Pair {
    int id, tier, battlePoint;
    public Pair(int tier, int battlePoint, int id) {
        this.id = id; this.tier = tier; this.battlePoint = battlePoint;
    }
    public Pair(int tier, int battlePoint) {
        this.tier = tier; this.battlePoint = battlePoint;
    }
}

class Node {
    int height, size;
    Node left, right;
    Pair pairChar;
    Node(Pair pairChar) {
        this.pairChar = pairChar;
        this.height = 1; this.size = 1;
    }
}

class CharComparator implements Comparator<Pair> {
    @Override
    public int compare(Pair a, Pair b) {
        if (a.tier < b.tier) {
            return -1; // tier lebih tinggi (S kiri)
        } else if (a.tier > b.tier) {
            return 1;
        } else {
            if (a.battlePoint > b.battlePoint) {
                return -1; // BP lebih tinggi ke kiri
            } else if (a.battlePoint < b.battlePoint) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

class AVLTree {
    Node root;
    Pair ans;
    CharComparator comparator = new CharComparator();

    List<Pair> findTopThreeNodes(Node root) {
        List<Pair> res = new ArrayList<>();
        collectTop(root, res);
        return res;
    }

    private void collectTop(Node node, List<Pair> res) {
        if (node == null || res.size() >= 3) {
            return;
        }
        collectTop(node.left, res);
        if (res.size() < 3) {
            res.add(node.pairChar);
        }
        collectTop(node.right, res);
    }

    List<Pair> findBottomThreeNodes(Node root) {
        List<Pair> res = new ArrayList<>();
        collectBottom(root, res);
        return res;
    }

    private void collectBottom(Node node, List<Pair> res) {
        if (node == null || res.size() >= 3) {
            return;
        }
        collectBottom(node.right, res);
        if (res.size() < 3) {
            res.add(node.pairChar);
        }
        collectBottom(node.left, res);
    }

    int height(Node n) {
        if (n == null) {
            return 0;
        } else {
            return n.height;
        }
    }

    int size(Node n) {
        if (n == null) {
            return 0;
        } else {
            return n.size;
        }
    }

    int getBalance(Node n) {
        if (n == null) {
            return 0;
        } else {
            return height(n.left) - height(n.right);
        }
    }

    int max(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    Node insert(Node node, Pair val) {
        if (node == null) {
            return new Node(val);
        }

        int cmp = comparator.compare(val, node.pairChar);
        if (cmp < 0) {
            node.left = insert(node.left, val);
        } else if (cmp > 0) {
            node.right = insert(node.right, val);
        } else {
            return node;
        }

        node.height = 1 + max(height(node.left), height(node.right));
        node.size = 1 + size(node.left) + size(node.right);
        int bal = getBalance(node);

        if (bal > 1 && comparator.compare(val, node.left.pairChar) < 0) {
            return rotateRight(node);
        }
        if (bal < -1 && comparator.compare(val, node.right.pairChar) > 0) {
            return rotateLeft(node);
        }
        if (bal > 1 && comparator.compare(val, node.left.pairChar) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bal < -1 && comparator.compare(val, node.right.pairChar) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    Node delete(Node node, Pair val) {
        if (node == null) {
            ans = null;
            return null;
        }

        int cmp = comparator.compare(val, node.pairChar);
        if (cmp < 0) {
            node.left = delete(node.left, val);
        } else if (cmp > 0) {
            node.right = delete(node.right, val);
        } else {
            if (ans == null) {
                ans = node.pairChar;
            }
            if (node.left == null && node.right == null) {
                return null;
            } else if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                Node pred = findPredecessor(node.left);
                node.pairChar = pred.pairChar;
                node.left = delete(node.left, pred.pairChar);
            }
        }

        node.height = 1 + max(height(node.left), height(node.right));
        node.size = 1 + size(node.left) + size(node.right);
        int bal = getBalance(node);

        if (bal > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }
        if (bal > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bal < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (bal < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    Node findPredecessor(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        y.size = 1 + size(y.left) + size(y.right);
        x.size = 1 + size(x.left) + size(x.right);

        return x;
    }

    Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        x.size = 1 + size(x.left) + size(x.right);
        y.size = 1 + size(y.left) + size(y.right);

        return y;
    }

    void findParent(Node node, Pair target, Node parent) {
        if (node == null) {
            return;
        }

        int cmp = comparator.compare(target, node.pairChar);
        if (cmp == 0) {
            if (parent != null) {
                ans = parent.pairChar;
            }
            return;
        }

        if (cmp < 0) {
            findParent(node.left, target, node);
        } else {
            findParent(node.right, target, node);
        }
    }
}
