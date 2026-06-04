package common.system.files;

import common.CommonStatic;
import common.pack.Context.ErrType;
import common.system.fake.FakeImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Queue;

public interface FileData {

	default byte[] getBytes() {
		try (InputStream is = getStream()) {
			byte[] ans = new byte[size()];
			int r = is.read(ans);
			if (r != size())
				CommonStatic.ctx.printErr(ErrType.FATAL, "failed to read data");
			return ans;
		} catch (Exception e) {
			CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to read data");
			return null;
		}
	}

	FakeImage getImg();

	InputStream getStream();

	default Queue<String> readLine() {
        try (InputStream is = getStream()) {
            try {
                Queue<String> ans = new ArrayDeque<>();
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(isr);
                String temp;
                while ((temp = reader.readLine()) != null)
                    ans.add(temp);
                reader.close();
                isr.close();
                return ans;
            } catch (Exception e) {
                CommonStatic.ctx.noticeErr(e, ErrType.FATAL, "failed to read lines");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
			return null;
        }
	}

	int size();

}
