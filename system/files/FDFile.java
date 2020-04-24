package common.system.files;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

import common.system.fake.FakeImage;

public class FDFile implements FileData {

	private final File file;

	public FDFile(File f) {
		file = f;
	}

	public byte[] getBytes() {
		try {
			byte[] bs = new byte[(int) file.length()];
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
			buf.read(bs, 0, bs.length);
			buf.close();

			return bs;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public FakeImage getImg() {
		try {
			return FakeImage.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Queue<String> readLine() {
		try {
			ArrayDeque<String> result = new ArrayDeque<>();

			FileInputStream fis = new FileInputStream(file);
			BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
			String line;
			while ((line = bfr.readLine()) != null) {
				result.add(line);
			}
			bfr.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString() {
		return file.getName();
	}

}
