package common.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.function.BiFunction;

import common.util.Data;

strictfp class ISStream extends InputStream implements InStream {

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

	}

	private FileTracer raf;
	private int ind, len;

	ISStream(File f) throws Exception {
		raf = new FileTracer(f);
		len = DataIO.readInt(raf::read) + 4;
		ind = 4;
	}

	ISStream(FileTracer raf, int ind, int len) {
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