//WILLIAM KUREK
//CS 1501
//PROJECT 3
public class LZWmod {

    private static final int MIN_CODEWORD_WIDTH = 9; //min width
    private static final int MAX_CODEWORD_WIDTH = 16; //max width

    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^W   
    private static int W = MIN_CODEWORD_WIDTH; // codeword width

    public static void compress() {
        TST2<Integer> st = new TST2<Integer>();
        for (int i = 0; i < R; i++) {
            st.put(new StringBuilder("" + (char) i), i);
        }
        int code = R + 1;  // R is codeword for EOF
        StringBuilder s = new StringBuilder("");
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();		//read character by character
            StringBuilder s1 = new StringBuilder(s.toString() + c);
            if (!st.contains(s1) && s.length() != 0) {
                BinaryStdOut.write(st.get(s), W);
                if (code < L) {
                    st.put(s1, code);	//add s1 to symbol table
                    code++;
                } else if (W < MAX_CODEWORD_WIDTH) {	//resize table
                    W++;
                    L *= 2;
                    st.put(s1, code);
                    code++;
                }
                s = new StringBuilder(s1.substring(s1.length() - 1, s1.length()));
            } else {
                s = s1;		//scan past s in input
            }
        }

        if (st.contains(s)) {
            BinaryStdOut.write(st.get(s), W);   //last character
        }
        BinaryStdOut.write(R, W);		//end of file
        BinaryStdOut.close();

    }

    public static void expand() {

        String[] st = new String[((int) Math.pow(2, MAX_CODEWORD_WIDTH))];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++) {
            st[i] = "" + (char) i;
        }
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) {
                break;
            }
            String s = st[codeword];
            if (i == codeword) {
                s = val + val.charAt(0);   // special case hack
            }
            if (i < L) {
                st[i++] = val + s.charAt(0);
            }
            if (i >= L && W < MAX_CODEWORD_WIDTH) {	//if full, resize table  
                W++;
                L = (L * 2);
            }
            val = s;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            compress();
        } else if (args[0].equals("+")) {
            expand();
        } else {
            throw new RuntimeException("Illegal command line argument");
        }
    }

}
