package common.io;

import common.CommonStatic;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public strictfp interface InStream {

    static InStream getIns(byte[] bs) {
        int[] is = DataIO.translate(bs);
        int sig = DataIO.toInt(is, 0);
        if (sig == bs.length - 4) {
            InStream ans = new InStreamDef(is, 4, is.length);
            return ans;
        }
        throw new BCUException("Unsupported version");
    }

    boolean end();

    int nextByte();

    byte[] nextBytesB();

    byte[] nextBytesI();

    double nextDouble();

    double[] nextDoubles();

    float nextFloat();

    int nextInt();

    int[] nextIntsB();

    int[][] nextIntsBB();

    long nextLong();

    int nextShort();

    String nextString();

    InStream subStream();

    OutStream translate();

}

strictfp class InStreamDef extends DataIO implements InStream {

    private final int[] bs;
    private final int off, max;
    private int index;

    protected InStreamDef(InStreamDef isd) {
        bs = isd.bs;
        index = off = isd.index;
        max = isd.max;
    }

    protected InStreamDef(int[] data, int ofs, int m) {
        bs = data;
        off = ofs;
        max = m;
        index = off;
    }

    @Override
    public boolean end() {
        return index == max;
    }

    @Override
    public int nextByte() {
        check(1);
        int ans = toByte(bs, index);
        index++;
        return ans;
    }

    @Override
    public byte[] nextBytesB() {
        int len = nextByte();
        byte[] ints = new byte[len];
        for (int i = 0; i < len; i++)
            ints[i] = (byte) nextByte();
        return ints;
    }

    @Override
    public byte[] nextBytesI() {
        int len = nextInt();
        byte[] ints = new byte[len];
        for (int i = 0; i < len; i++)
            ints[i] = (byte) nextByte();
        return ints;
    }

    @Override
    public double nextDouble() {
        check(8);
        double ans = toDouble(bs, index);
        index += 8;
        return ans;
    }

    @Override
    public double[] nextDoubles() {
        int len = nextByte();
        double[] ints = new double[len];
        for (int i = 0; i < len; i++)
            ints[i] = nextDouble();
        return ints;
    }

    @Override
    public float nextFloat() {
        check(4);
        float ans = toFloat(bs, index);
        index += 4;
        return ans;
    }

    @Override
    public int nextInt() {
        check(4);
        int ans = toInt(bs, index);
        index += 4;
        return ans;
    }

    @Override
    public int[] nextIntsB() {
        int len = nextByte();
        int[] ints = new int[len];
        for (int i = 0; i < len; i++)
            ints[i] = nextInt();
        return ints;
    }

    @Override
    public int[][] nextIntsBB() {
        int len = nextByte();
        int[][] ints = new int[len][];
        for (int i = 0; i < len; i++)
            ints[i] = nextIntsB();
        return ints;
    }

    @Override
    public long nextLong() {
        check(8);
        long ans = toLong(bs, index);
        index += 8;
        return ans;
    }

    @Override
    public int nextShort() {
        check(2);
        int ans = toShort(bs, index);
        index += 2;
        return ans;
    }

    @Override
    public String nextString() {
        byte[] bts = nextBytesB();
        return new String(bts, StandardCharsets.UTF_8);
    }

    public int pos() {
        return index - off;
    }

    public void reread() {
        index = off;
    }

    public int size() {
        return max - off;
    }

    public void skip(int n) {
        index += n;
    }

    @Override
    public InStreamDef subStream() {
        int n = nextInt();
        if (n > size()) {
            new Exception("error in getting subStream").printStackTrace();
            CommonStatic.def.exit(false);
        }
        InStreamDef is = new InStreamDef(bs, index, index + n);
        index += n;
        return is;
    }

    @Override
    public String toString() {
        return "InStreamDef " + size();
    }

    @Override
    public OutStreamDef translate() {
        byte[] data = new byte[max - index];
        for (int i = 0; i < max - index; i++)
            data[i] = (byte) bs[index + i];
        return new OutStreamDef(data);
    }

    protected int[] getBytes() {
        return bs;
    }

    private void check(int i) {
        if (max - index < i) {
            String str = "out of bound: " + (index - off) + "/" + (max - off) + ", " + index + "/" + max + "/" + off
                    + "/" + bs.length;
            throw new BCUException(str);
        }
    }

}
