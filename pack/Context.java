package common.pack;

import java.io.File;
import java.io.IOException;

import common.pack.PackLoader.ZipDesc.FileDesc;

public interface Context {

	public static enum ErrType {
		FATAL, ERROR, WARN, INFO
	}

	public static interface RunExc {

		public void run() throws Exception;

	}

	public static interface SupExc<T> {

		public T get() throws Exception;

	}

	public static void check(boolean bool, String str, File f) throws IOException {
		if (bool)
			throw new IOException("failed to " + str + " file " + f);
	}

	public static void check(File f) throws IOException {
		if (!f.getParentFile().exists())
			check(f.getParentFile().mkdirs(), "create", f);
		if (!f.exists())
			check(f.createNewFile(), "create", f);
	}

	public static void delete(File f) throws IOException {
		if (f == null || !f.exists())
			return;
		if (f.isDirectory())
			for (File i : f.listFiles())
				delete(i);
		check(!f.delete(), "delete", f);
	}

	public boolean confirmDelete();

	public File getLangFile(String file);

	public File getPackFolder();

	public File getWorkspaceFile(String relativePath);

	public default void noticeErr(Context.RunExc r, ErrType t, String str) {
		try {
			r.run();
		} catch (Exception e) {
			noticeErr(e, t, str);
		}
	}

	public void noticeErr(Exception e, ErrType t, String str);

	public default <T> T noticeError(Context.SupExc<T> r, ErrType t, String str) {
		try {
			return r.get();
		} catch (Exception e) {
			noticeErr(e, t, str);
			return null;
		}
	}

	public boolean preload(FileDesc desc);

	public void printErr(ErrType t, String str);

}