// Maaf ya guys, next lab dibuat lebih gampang deh
// Problemsetter: TKH & DAN
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Arrays;

public class Lab4 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInteger();
        int K = in.nextInteger();

        int[] values = new int[N];
        int[] weights = new int[N];

        for (int i = 0; i < N; i++) {
            values[i] = in.nextInteger();
        }
        for (int i = 0; i < N; i++) {
            weights[i] = in.nextInteger();
        }

        long result = pinjamBukuMax(values, weights, K); 
        out.println(result);

        out.close();
    }


    public static long pinjamBukuMax(int[] values, int[] weights, int K) {
        int n = values.length;

        if (n == 0) return 0;
        if (n == 1) {
            if (weights[0] <= K) {
                return values[0];
            } else {
                return 0;
            }
        }

        // case 1: exclude rak terakhir
        int[] values1 = Arrays.copyOfRange(values, 0, n - 1);
        int[] weights1 = Arrays.copyOfRange(weights, 0, n - 1);
        long maxVal1 = solveLinearKnapsack(values1, weights1, K);

        // case 2: exclude rak pertama
        int[] values2 = Arrays.copyOfRange(values, 1, n);
        int[] weights2 = Arrays.copyOfRange(weights, 1, n);
        long maxVal2 = solveLinearKnapsack(values2, weights2, K);

        return Math.max(maxVal1, maxVal2);
    }

    private static long solveLinearKnapsack(int[] books, int[] weights, int K) {
        int n = books.length;
        if (n == 0) return 0;

        long[][] dp = new long[n][K + 1];

        for (int j = weights[0]; j <= K; j++) {
            dp[0][j] = books[0];
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= K; j++) {
                long skip = dp[i - 1][j];
                long borrow = 0;
                if (j >= weights[i]) {
                    long prev = 0;
                    if (i >= 2) {
                        prev = dp[i - 2][j - weights[i]];
                    }
                    borrow = books[i] + prev;
                }
                dp[i][j] = Math.max(skip, borrow);
            }
        }
        return dp[n - 1][K];
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
