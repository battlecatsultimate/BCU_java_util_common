package common.io;

import common.util.Data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.BiFunction;

strictfp class ISAStream extends InputStream implements InStream {

    private static class FileTracer {

        private RandomAccessFile raf;
        private int ind;

        protected FileTracer(File f) throws FileNotFoundException {
            raf = new RandomAccessFile(f, "r");
            ind = 0;
        }

        public int read() throws IOException {
            ind++;
            return raf.read();
        }

        public int read(byte b[], int off, int len) throws IOException {
            ind += len;
            return raf.read(b, off, len);
        }

        public void seek(int pos) throws IOException {
            if (pos != ind)
                raf.seek(ind = pos);
        }

        public void close() throws IOException {
            raf.close();
        }
    }

    private FileTracer raf;
    private int ind, len;

    ISAStream(File f) throws Exception {
        raf = new FileTracer(f);
        len = DataIO.readInt(raf::read) + 4;
        ind = 4;
    }

    ISAStream(FileTracer raf, int ind, int len) {
        this.raf = raf;
        this.ind = ind;
        this.len = len;
    }

    @Override
    public boolean end() {
        return ind == len;
    }

    @Override
    public int nextByte() {
        check(1);
        return Data.err(() -> raf.read());
    }

    @Override
    public double nextDouble() {
        return check(8, DataIO::toDouble);
    }

    @Override
    public float nextFloat() {
        return check(4, DataIO::toFloat);
    }

    @Override
    public int nextInt() {
        return check(4, DataIO::toInt);
    }

    @Override
    public long nextLong() {
        return check(8, DataIO::toLong);
    }

    @Override
    public int nextShort() {
        return check(2, DataIO::toShort);
    }

    @Override
    public int read() throws IOException {
        if (ind == len)
            return -1;
        ind++;
        return raf.read();
    }

    @Override
    public int read(byte[] bs, int off, int rlen) throws IOException {
        if (rlen + ind > len)
            rlen = len - ind;
        if (ind == len)
            return -1;
        ind += rlen;
        return raf.read(bs, off, rlen);
    }

    @Override
    public InStream subStream() {
        int n = nextInt();
        ISAStream is = new ISAStream(raf, ind, ind + n);
        ind += n;
        return is;
    }

    @Override
    public OutStream translate() {
        // TODO Auto-generated method stub
        return null;
    }

    private void check(int size) {
        Data.err(() -> raf.seek(ind));
        ind += size;
        if (ind > len) {
            System.out.println(Arrays.deepToString(Thread.currentThread().getStackTrace()));
            System.out.println("error: overread");
        }
    }

    private <T> T check(int size, BiFunction<int[], Integer, T> func) {
        check(size);
        return Data.err(() -> DataIO.readData(raf::read, size, func));
    }

    @Override
    public void close() throws IOException {
        raf.close();
    }

    @Override
    public byte[] nextBytesB() {
        int len = nextInt();
        byte[] ints = new byte[len];
        for (int i = 0; i < len; i++)
            ints[i] = (byte) nextByte();
        return ints;
    }

    @Override
    public double[] nextDoubles() {
        int len = nextInt();
        double[] ints = new double[len];
        for (int i = 0; i < len; i++)
            ints[i] = nextDouble();
        return ints;
    }

    @Override
    public int[] nextIntsB() {
        int len = nextInt();
        int[] ints = new int[len];
        for (int i = 0; i < len; i++)
            ints[i] = nextInt();
        return ints;
    }

    @Override
    public int[][] nextIntsBB() {
        int len = nextInt();
        int[][] ints = new int[len][];
        for (int i = 0; i < len; i++)
            ints[i] = nextIntsB();
        return ints;
    }

    @Override
    public String nextString() {
        byte[] bts = nextBytesB();
        return new String(bts, StandardCharsets.UTF_8);
    }
}