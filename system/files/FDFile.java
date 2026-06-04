package common.system.files;

import common.CommonStatic;
import common.pack.Context.ErrType;
import common.system.fake.FakeImage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

public class FDFile implements FileData {

	private final File file;

	public FDFile(File f) {
		file = f;
	}

	@Override
	public byte[] getBytes() {
		byte[] bs = new byte[(int) file.length()];

        try (BufferedInputStream buf = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
			buf.read(bs, 0, bs.length);
			return bs;
        } catch (Exception e) {
            e.printStackTrace();
			return null;
        }
	}

	@Override
	public FakeImage getImg() {
		return FakeImage.read(file);
	}

	@Override
	public InputStream getStream() {
		return CommonStatic.ctx.noticeErr(() -> new FileInputStream(file), ErrType.ERROR,
				"failed to read bcuzip at " + file);
	}

	@Override
	public int size() {
		return (int) file.length();
	}

	@Override
	public String toString() {
		return file.getName();
	}

}
