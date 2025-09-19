// TKH
// Jujur... wordingnya bingungin, tapi intinya ini kita "reverse" proses yang dijelasin di soal

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        
        int n = in.nextInteger();
        int[] k = new int[n];
        for (int i = 0; i < n; i++) {
            k[i] = in.nextInteger();
        }

        int[] result = deckRevealedDecreasing(k);
        
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                out.print(" ");
            }
            out.print(result[i]);
        }
               
        out.close();
    }

    public static int[] deckRevealedDecreasing(int[] deck) {
        Arrays.sort(deck);  // ascending
        int n = deck.length;
        int[] res = new int[n];
        int size = 0;

        for (int i = 0; i < n; i++) {  
            if (size >= 2) {
                int last = res[size - 1];
                for (int j = size - 1; j > 0; j--) {
                    res[j] = res[j - 1];
                }
                res[0] = last;
            }

            for (int j = size; j > 0; j--) {
                res[j] = res[j - 1];
            }
            res[0] = deck[i];
            size++;
        }

        return res;
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