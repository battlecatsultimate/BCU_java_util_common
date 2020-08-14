package common.system.files;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

import common.CommonStatic;
import common.pack.Context.ErrType;
import common.system.fake.FakeImage;
import common.util.Data;

public interface FileData {

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

	public InputStream getStream();

	public default Queue<String> readLine() {
		InputStream is = getStream();
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
			CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to read lines");
			return null;
		}
	}

	public int size();

}

interface ByteData extends FileData {

	@Override
	public byte[] getBytes();

	@Override
	public default FakeImage getImg() {
		return Data.err(() -> FakeImage.read(getBytes()));
	}

	@Override
	public default InputStream getStream() {
		return new ByteArrayInputStream(getBytes());
	}

	@Override
	public default int size() {
		return getBytes().length;
	}

}
