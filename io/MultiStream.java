package common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import common.CommonStatic;
import common.pack.Context.ErrType;

public class MultiStream {

	public static interface ByteStream {

		public void close() throws IOException;

		public void read(byte[] bs) throws IOException;

	}

	public static class TrueStream implements ByteStream {

		private final FileInputStream fis;

		public TrueStream(File f, int pos) throws IOException {
			fis = new FileInputStream(f);
			fis.skip(pos);
		}

		@Override
		public void close() throws IOException {
			fis.close();
		}

		@Override
		public void read(byte[] bs) throws IOException {
			fis.read(bs);
		}

	}

	private static interface RunExc {

		public void run() throws IOException;

	}

	private class SubStream implements ByteStream {

		private int pos;

		private SubStream(int pos) {
			this.pos = pos;
		}

		@Override
		public void close() throws IOException {
		}

		@Override
		public void read(byte[] bs) throws IOException {
			attempt(() -> readBytes(pos, bs));
			pos += bs.length;
		}

	}

	private static final Map<File, MultiStream> MAP = new HashMap<>();

	public static ByteStream getStream(File f, int pos, boolean useRAF) throws IOException {
		if (!useRAF) {
			return new TrueStream(f, pos);
		}
		MultiStream ms = MAP.get(f);
		if (ms == null)
			MAP.put(f, ms = new MultiStream(f));
		return ms.new SubStream(pos);
	}

	private final File file;
	private RandomAccessFile raf;
	private long poscache = -1;

	private MultiStream(File f) {
		file = f;
	}

	public void close() throws IOException {
		if (raf == null)
			return;
		poscache = -1;
		raf.close();
		raf = null;
	}

	private RandomAccessFile access() throws FileNotFoundException {
		if (raf != null)
			return raf;
		poscache = -1;
		raf = new RandomAccessFile(file, "r");
		return raf;
	}

	private void attempt(RunExc r) throws IOException {
		try {
			r.run();
			return;
		} catch (IOException e) {
			CommonStatic.ctx.printErr(ErrType.INFO, "failed to read, attempted again");
			close();
		}
		r.run();
		CommonStatic.ctx.printErr(ErrType.INFO, "attempt succeed");
	}

	private void readBytes(int pos, byte[] arr) throws IOException {
		access();
		if (poscache == -1)
			poscache = raf.getFilePointer();
		if (pos != poscache) {
			raf.seek(pos);
			poscache = pos;
		}
		raf.read(arr);
		poscache += arr.length;
	}

}
