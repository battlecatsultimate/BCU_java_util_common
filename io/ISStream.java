package common.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.function.BiFunction;

import common.util.Data;

strictfp class ISStream extends InputStream implements InStream {

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

	public int read() throws IOException {
		if (ind == len)
			return -1;
		ind++;
		return raf.read();
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