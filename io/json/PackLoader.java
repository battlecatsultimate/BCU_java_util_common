package common.io.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import common.io.DataIO;
import common.io.json.JsonClass.NoTag;
import common.io.json.PackLoader.PackDesc.FileDesc;

public class PackLoader {

	public static interface Context {

		public File getFile(String id);

	}

	@JsonClass(noTag = NoTag.LOAD)
	public static class PackDesc {

		@JsonClass(read = JsonClass.RType.DATA)
		public static class FileDesc {

			@JsonField
			private String path;

			@JsonField
			private int size;

			private File file;

			@Deprecated
			public FileDesc() {
			}

			public FileDesc(String path, File f) {
				this.path = path;
				this.file = f;
				this.size = (int) f.length();
			}

		}

		public String BCU_VERSION;
		public String uuid;
		public String author;
		public FileDesc[] files;

		@Deprecated
		public PackDesc() {

		}

		public PackDesc(String ver, String id, String auth, FileDesc[] fs) {
			BCU_VERSION = ver;
			uuid = id;
			author = auth;
			files = fs;
		}

		private void load(FileLoader loader) throws Exception {
			for (FileDesc fd : files) {
				File f = loader.context.getFile(fd.path);
				int rem = fd.size;
				byte[] data = null;
				FileOutputStream fos = new FileOutputStream(f);
				while (rem > 0) {
					int size = Math.min(rem, CHUNK);
					if (data == null || data.length != size)
						data = new byte[size];
					data = loader.decode(size, false);
					fos.write(data, 0, size);
					rem -= size;
				}
				fos.close();

			}
		}

		private void save(FileSaver saver) throws IOException {
			for (FileDesc fd : files) {
				FileInputStream fis = new FileInputStream(fd.file);
				int rem = fd.size;
				byte[] data = null;
				while (rem > 0) {
					int size = Math.min(rem, CHUNK);
					if (data == null || data.length != size)
						data = new byte[size];
					fis.read(data);
					saver.save(data);
					rem -= size;
				}
				fis.close();
			}
		}

	}

	private static class FileLoader {

		private final FileInputStream fis;
		private final Context context;
		private final PackDesc pack;
		private final Cipher cipher;

		private FileLoader(Context cont, File f) throws Exception {
			context = cont;
			fis = new FileInputStream(f);
			byte[] head = new byte[HEADER];
			fis.read(head);
			if (!Arrays.equals(head, HEAD_DATA))
				throw new Exception("Corrupted File: header not match");
			byte[] password = new byte[PASSWORD];
			fis.read(password);
			cipher = decrypt(password);
			byte[] len = new byte[4];
			fis.read(len);
			int size = DataIO.toInt(DataIO.translate(len), 0);
			byte[] first = new byte[PASSWORD];
			fis.read(first);
			cipher.update(first);
			String desc = new String(decode(size, false));
			JsonElement je = JsonParser.parseString(desc);
			pack = JsonDecoder.decode(je, PackDesc.class);
			pack.load(this);
			fis.close();
		}

		private byte[] decode(int size, boolean end) throws Exception {
			int len = ((size & 0xF) == 0) ? size : (size | 0xF) + 1;
			byte[] bs = new byte[len];
			fis.read(bs);
			bs = end ? cipher.doFinal(bs) : cipher.update(bs);
			if (bs.length != size)
				return Arrays.copyOf(bs, size);
			return bs;
		}

	}

	private static class FileSaver {

		private final FileOutputStream fos;
		private final Cipher encry;

		private FileSaver(File dst, File folder, String ver, String id, String auth, String password) throws Exception {
			List<FileDesc> fs = new ArrayList<>();
			addFiles(fs, folder, "./");
			PackDesc desc = new PackDesc(ver, id, auth, fs.toArray(new FileDesc[0]));
			byte[] bytedesc = JsonEncoder.encode(desc).toString().getBytes();
			fos = new FileOutputStream(dst);
			fos.write(HEAD_DATA);
			byte[] key = getMD5(password.getBytes(), PASSWORD);
			fos.write(key);
			byte[] len = new byte[4];
			DataIO.fromInt(len, 0, bytedesc.length);
			fos.write(len);
			encry = encrypt(key);
			save(bytedesc);
			desc.save(this);
			fos.write(encry.doFinal());
			fos.close();
		}

		private void addFiles(List<FileDesc> fs, File f, String path) {
			if (f.isDirectory())
				for (File fi : f.listFiles())
					addFiles(fs, fi, path + f.getName() + "/");
			else
				fs.add(new FileDesc(path + f.getName(), f));
		}

		private void save(byte[] bs) throws IOException {
			if ((bs.length & 0xF) != 0)
				bs = Arrays.copyOf(bs, (bs.length | 0xF) + 1);
			bs = encry.update(bs);
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

	public static PackDesc readPack(Context cont, File f) throws Exception {
		return new FileLoader(cont, f).pack;
	}

	public static void writePack(File dst, File folder, String ver, String id, String auth, String password)
			throws Exception {
		new FileSaver(dst, folder, ver, id, auth, password);
	}

}
