package common.system.files;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

import common.system.fake.FakeImage;

public interface FileData {

	public FakeImage getImg();

	public Queue<String> readLine();
	
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

}

interface ByteData extends FileData {

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
	public default Queue<String> readLine() {
		return FileData.IS2L(new ByteArrayInputStream(getBytes()));
	}
	
}
