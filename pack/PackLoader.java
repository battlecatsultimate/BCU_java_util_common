package common.pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import common.io.DataIO;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonEncoder;
import common.io.json.JsonField;
import common.io.json.JsonClass.JCConstructor;
import common.io.json.JsonClass.RType;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField.GenType;
import common.pack.PackLoader.ZipDesc.FileDesc;
import common.pack.Source.PackDesc;
import common.system.fake.FakeImage;
import common.system.files.FDByte;
import common.system.files.FileData;
import common.system.files.VFileRoot;

public class PackLoader {

	public static interface PatchFile {

		public File getFile(String path) throws Exception;

	}

	public static interface Preload {

		public boolean preload(FileDesc fd);

	}

	@JsonClass(read = RType.FILL)
	public static class ZipDesc {

		@JsonClass
		public static class FileDesc implements FileData {

			@JsonField
			public String path;

			@JsonField
			public int size;

			@JsonField
			private int offset;

			private File file; // writing only
			private ZipDesc pack; // reading only
			private FDByte data; // preload reading only

			public FileDesc(FileSaver parent, String path, File f) {
				this.path = path;
				file = f;
				size = (int) f.length();
				offset = parent.len;
				parent.len += (size & 0xF) == 0 ? size : (size | 0xF) + 1;
				parent.len += PASSWORD;
			}

			@Deprecated
			@JCConstructor
			public FileDesc(ZipDesc desc) {
				pack = desc;
			}

			@Override
			public FakeImage getImg() {
				try {
					return data != null ? data.getImg() : FakeImage.read(pack.readFile(path));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public InputStream getStream() throws Exception {
				return pack.readFile(path);
			}

			@Override
			public Queue<String> readLine() {
				try {
					return data != null ? data.readLine() : FileData.IS2L(pack.readFile(path));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

		}

		@JsonField
		public PackDesc desc;

		@JsonField(gen = GenType.GEN)
		public FileDesc[] files;

		public VFileRoot<FileDesc> tree = new VFileRoot<>(".");

		private int offset;
		private FileLoader loader;

		public ZipDesc(PackDesc pd, FileDesc[] fs) {
			desc = pd;
			files = fs;
		}

		private ZipDesc(FileLoader fl, int off) {
			loader = fl;
			offset = off;
		}

		public boolean match(byte[] data) {
			return Arrays.equals(data, loader.key);
		}

		@OnInjected
		public void onInjected() {
			for (FileDesc fd : files)
				tree.build(fd.path, fd);
		}

		public InputStream readFile(String path) throws Exception {
			FileDesc fd = tree.find(path).getData();
			return new FileLoader.FLStream(loader, offset + fd.offset, fd.size);
		}

		public void unzip(PatchFile func) throws Exception {
			InputStream fis = new FileInputStream(loader.file);
			fis.skip(offset);
			for (FileDesc fd : files) {
				int n = regulate(fd.size) / PASSWORD;
				File dest = func.getFile(fd.path);
				OutputStream fos = new FileOutputStream(dest);
				byte[] bs = new byte[PASSWORD];
				Cipher cipher = decrypt(loader.key);
				fis.read(bs);
				cipher.update(bs);
				for (int i = 0; i < n; i++) {
					fis.read(bs);
					byte[] ans = i == n - 1 ? cipher.doFinal(bs) : cipher.update(bs);
					fos.write(ans);
				}
				fos.close();
			}
			fis.close();
		}

		private void load() throws Exception {
			for (FileDesc fd : files)
				if (loader.context.preload(fd))
					fd.data = new FDByte(loader.decode(fd.size));
				else
					loader.fis.skip(regulate(fd.size + PASSWORD));
		}

		private void save(FileSaver saver) throws Exception {
			for (FileDesc fd : files) {
				FileInputStream fis = new FileInputStream(fd.file);
				int rem = fd.size;
				byte[] data = null;
				Cipher cipher = encrypt(saver.key);
				while (rem > 0) {
					int size = Math.min(rem, CHUNK);
					if (data == null || data.length != size)
						data = new byte[size];
					fis.read(data);
					rem -= size;
					saver.save(cipher, data, rem == 0);
				}
				fis.close();
			}
		}

	}

	private static class FileLoader {

		private static class FLStream extends InputStream {

			private final FileInputStream fis;
			private final Cipher cipher;

			private byte[] bs = new byte[PASSWORD];
			private int len, size;
			private byte[] cache;
			private int index;

			private FLStream(FileLoader ld, int offset, int size) throws Exception {
				len = (size & 0xF) == 0 ? size : (size | 0xF) + 1;
				this.size = size;
				cipher = decrypt(ld.key);
				fis = new FileInputStream(ld.file);
				fis.skip(offset);
				fis.read(bs);
				cipher.update(bs);
			}

			@Override
			public void close() throws IOException {
				fis.close();
			}

			@Override
			public int read() throws IOException {
				if (size == 0)
					return -1;
				if (cache == null || index >= cache.length)
					if (len > 0)
						update();
					else
						return -1;
				size--;
				return cache[index++];
			}

			private void update() throws IOException {
				fis.read(bs);
				len -= PASSWORD;
				index = 0;
				try {
					cache = len == 0 ? cipher.doFinal(bs) : cipher.update(bs);
				} catch (Exception e) {
					throw new IOException(e);
				}
			}

		}

		private final FileInputStream fis;
		private final Preload context;
		private final ZipDesc pack;
		private final File file;
		private final byte[] key;

		private FileLoader(Preload cont, File f) throws Exception {
			context = cont;
			file = f;
			fis = new FileInputStream(f);
			byte[] head = new byte[HEADER];
			fis.read(head);
			if (!Arrays.equals(head, HEAD_DATA))
				throw new Exception("Corrupted File: header not match");
			key = new byte[PASSWORD];
			fis.read(key);
			byte[] len = new byte[4];
			fis.read(len);
			int size = DataIO.toInt(DataIO.translate(len), 0);
			String desc = new String(decode(size));
			JsonElement je = JsonParser.parseString(desc);
			int offset = HEADER + PASSWORD * 2 + 4 + regulate(size);
			pack = JsonDecoder.inject(je, ZipDesc.class, new ZipDesc(this, offset));
			pack.load();
			fis.close();
		}

		private byte[] decode(int size) throws Exception {
			int len = regulate(size) + PASSWORD;
			byte[] bs = new byte[len];
			fis.read(bs);
			bs = decrypt(key).doFinal(bs);
			if (bs.length != size)
				return Arrays.copyOf(bs, size);
			return bs;
		}

	}

	private static class FileSaver {

		private final FileOutputStream fos;
		private byte[] key;

		private int len;

		private FileSaver(File dst, File folder, PackDesc pd, String password) throws Exception {
			List<FileDesc> fs = new ArrayList<>();
			addFiles(fs, folder, "./");
			ZipDesc desc = new ZipDesc(pd, fs.toArray(new FileDesc[0]));
			byte[] bytedesc = JsonEncoder.encode(desc).toString().getBytes();
			fos = new FileOutputStream(dst);
			fos.write(HEAD_DATA);
			key = getMD5(password.getBytes(), PASSWORD);
			fos.write(key);
			byte[] len = new byte[4];
			DataIO.fromInt(len, 0, bytedesc.length);
			fos.write(len);
			save(encrypt(key), bytedesc, true);
			desc.save(this);
			fos.close();
		}

		private void addFiles(List<FileDesc> fs, File f, String path) {
			if (f.isDirectory())
				for (File fi : f.listFiles())
					addFiles(fs, fi, path + f.getName() + "/");
			else
				fs.add(new FileDesc(this, path + f.getName(), f));
		}

		private void save(Cipher cipher, byte[] bs, boolean end) throws Exception {
			if ((bs.length & 0xF) != 0)
				bs = Arrays.copyOf(bs, (bs.length | 0xF) + 1);
			bs = end ? cipher.doFinal(bs) : cipher.update(bs);
			fos.write(bs);
		}

	}

	private static final int HEADER = 16, PASSWORD = 16, CHUNK = 1 << 16;

	private static final String HEAD_STR = "battlecatsultimate_packfile";
	private static final byte[] HEAD_DATA = getMD5(HEAD_STR.getBytes(), HEADER);
	private static final byte[] INIT_VECTOR = getMD5("battlecatsultimate".getBytes(), 16);

	public static Cipher decrypt(byte[] key) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR);
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		return cipher;
	}

	public static Cipher encrypt(byte[] key) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR);
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		return cipher;
	}

	public static byte[] getMD5(byte[] data, int len) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] ans = md5.digest(data);
		if (ans.length == len)
			return ans;
		return Arrays.copyOf(ans, len);
	}

	public static ZipDesc readPack(Preload cont, File f) throws Exception {
		return new FileLoader(cont, f).pack;
	}

	public static void writePack(File dst, File folder, PackDesc pd, String password) throws Exception {
		new FileSaver(dst, folder, pd, password);
	}

	private static int regulate(int size) {
		return (size & 0xF) == 0 ? size : (size | 0xF) + 1;
	}

}
