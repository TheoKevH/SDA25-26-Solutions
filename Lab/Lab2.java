// TKH

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab2 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read inputs
        int N = in.nextInteger();
        int K = in.nextInteger();

        long[] cards = new long[N];
        for (int i = 0; i < N; i++) {
            cards[i] = Integer.MAX_VALUE;
        }

        for (int i = 0; i < K; i++) {
            cards[in.nextInteger()] = in.nextInteger();
        }

        long[] result = solve(N, K, cards);
        for (int i = 0; i < result.length; i++) {
            out.print(result[i]);
            if (i < result.length - 1) {
                out.print(" ");
            }
        }
        
        // Close/flush the output
        out.close();
    }
    
    public static long[] solve(int N, int K, long[] cards) {
        
        // Approachnya disini Divide and conquer
        // (Cari efek kartu hanya dari kartu terkecil dari kiri atau kanan serta kartu itu sendiri)
        // NOTE: Solusi ini memakai waktu O(3N) atau setara dengan O(N).

        long[] left = new long[N];
        long[] right = new long[N];

        long p;

        p = Integer.MAX_VALUE;
        
        // Untuk kartu ke-i, efek dari kartu terkecil di sebelah kiri hingga tempat kartu itu sekarang adalah...
        for (int i = 0; i < N; i++) {
            p = Math.min(p + 1, cards[i]);
            left[i] = p;
        }

        p = Integer.MAX_VALUE;

        // Untuk kartu ke-i, efek dari kartu terkecil di sebelah kanan hingga tempat kartu itu sekarang adalah...
        for (int i = N - 1; i >= 0; i--) {
            p = Math.min(p + 1, cards[i]);
            right[i] = p;
        }

        
        // Instead of bikin array dan sort, buat comparison pake variable first second third aja. OR pake .sort() juga bisa
        
        long first = Long.MIN_VALUE;
        long second = Long.MIN_VALUE;
        long third = Long.MIN_VALUE;

        for (int i = 0; i < N; i++) {
            long cur;

            if (cards[i] == Integer.MAX_VALUE) {
                cur = Math.min(left[i], right[i]);
            } else {
                cur = cards[i];     // Ingat!! Kartu sihir tidak bisa berubah nilainya
            }

            if (cur > first) {
                third = second;
                second = first;
                first = cur;
            } else if (cur > second) {
                third = second;
                second = cur;
            } else if (cur > third) {
                third = cur;
            }
        }
        
        
        return new long[] { first, second, third };     // Return tiga nilai terbesar
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
    }
}
