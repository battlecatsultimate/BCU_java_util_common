package common.system.files;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

import common.CommonStatic;
import common.pack.Context.ErrType;
import common.system.fake.FakeImage;

public interface FileData {

	public static Queue<String> IS2L(InputStream is) {
		try {
			Queue<String> ans = new ArrayDeque<>();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String temp = null;
			while ((temp = reader.readLine()) != null)
				ans.add(temp);
			reader.close();
			return ans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public default byte[] getBytes() {
		try {
			byte[] ans = new byte[size()];
			InputStream is = getStream();
			int r = is.read(ans);
			is.close();
			if (r != size())
				CommonStatic.ctx.printErr(ErrType.FATAL, "failed to read data");
			return ans;
		} catch (Exception e) {
			CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to read data");
			return null;
		}
	}

	public FakeImage getImg();

	public InputStream getStream() throws Exception;

	public Queue<String> readLine();

	public int size();

}

interface ByteData extends FileData {

	@Override
	public byte[] getBytes();

	@Override
	public default FakeImage getImg() {
		try {
			return FakeImage.read(getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public default InputStream getStream() {
		return new ByteArrayInputStream(getBytes());
	}

	@Override
	public default Queue<String> readLine() {
		return FileData.IS2L(new ByteArrayInputStream(getBytes()));
	}

	@Override
	public default int size() {
		return getBytes().length;
	}

}
