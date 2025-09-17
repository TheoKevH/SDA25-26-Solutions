// Kode Asdos: TKH

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab1 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int T = in.nextInteger();

        for (int j = 0; j < T; j++) {
            long N = in.nextLong();
            long K = in.nextLong();

            long ans;
            if (K % 2 == 0) {
                // K genap -> tidak ada kelipatan ganjil
                ans = 0;
            } else {
                long maks = 2 * N - 1;        

                long a = K;
                long b = 2 * K;

                long n = ((maks - a + b) / b); // n adalah banyaknya bilangan ganjil yang merupakan kelipatan K dan <= 2N-1


                long Sn = (n)*(2*a + (n-1)*b)/2; // dari hint

                ans = Sn;

            }

            out.println(ans);
        }

        out.close();
    }

    // Ini template, DO NOT CHANGE THIS!!
    // taken from https://codeforces.com/submissions/Petr
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
                    String line = reader.readLine();
                    if (line == null) return null;
                    tokenizer = new StringTokenizer(line);
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
