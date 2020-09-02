package common.io;

import common.CommonStatic;
import common.util.Data;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

public strictfp interface InStream {

	static InStream getIns(File f) {
		return Data.err(() -> new ISStream(f));
	}

	public default byte[] nextBytesB() {
		int len = nextByte();
		byte[] ints = new byte[len];
		for (int i = 0; i < len; i++)
			ints[i] = (byte) nextByte();
		return ints;
	}

	public default byte[] nextBytesI() {
		int len = nextInt();
		byte[] ints = new byte[len];
		for (int i = 0; i < len; i++)
			ints[i] = (byte) nextByte();
		return ints;
	}

	public default double[] nextDoubles() {
		int len = nextByte();
		double[] ints = new double[len];
		for (int i = 0; i < len; i++)
			ints[i] = nextDouble();
		return ints;
	}

	public default int[] nextIntsB() {
		int len = nextByte();
		int[] ints = new int[len];
		for (int i = 0; i < len; i++)
			ints[i] = nextInt();
		return ints;
	}

	public default int[][] nextIntsBB() {
		int len = nextByte();
		int[][] ints = new int[len][];
		for (int i = 0; i < len; i++)
			ints[i] = nextIntsB();
		return ints;
	}

	public default String nextString() {
		byte[] bts = nextBytesB();
		return new String(bts, StandardCharsets.UTF_8);
	}

	boolean end();

	int nextByte();

	double nextDouble();

	float nextFloat();

	int nextInt();

	long nextLong();

	int nextShort();

	InStream subStream();

	OutStream translate();

}

strictfp class InStreamDef extends DataIO implements InStream {

	private final int[] bs;
	private final int off, max;
	private int index;

	InStreamDef(InStreamDef isd) {
		bs = isd.bs;
		index = off = isd.index;
		max = isd.max;
	}

	InStreamDef(int[] data, int ofs, int m) {
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
	public double nextDouble() {
		check(8);
		double ans = toDouble(bs, index);
		index += 8;
		return ans;
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

strictfp class ISStream implements InStream {

	private RandomAccessFile raf;

	private int ind, len;

	ISStream(File f) throws Exception {
		raf = new RandomAccessFile(f, "r");
		len = DataIO.readInt(raf::read) + 4;
		ind = 4;
	}

	ISStream(RandomAccessFile raf, int ind, int len) {
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
	public InStream subStream() {
		int n = nextInt();
		ISStream is = new ISStream(raf, ind, ind + n);
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
		if (ind > len)
			System.out.println("error: overread");
	}

	private <T> T check(int size, BiFunction<int[], Integer, T> func) {
		check(size);
		return Data.err(() -> DataIO.readData(raf::read, size, func));
	}

}
