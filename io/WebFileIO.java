package common.io;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.*;
import com.google.api.client.util.ExponentialBackOff;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import common.io.assets.Admin.StaticPermitted;
import common.pack.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class WebFileIO {

	public static final int BUFFER = 1 << 12, SMOOTH = 1 << 16, FAST = 1 << 18, MAX = 1 << 20;

	@StaticPermitted(StaticPermitted.Type.TEMP)
	private static HttpTransport transport;

	public static void download(int size, String url, File file, Consumer<Double> c, boolean direct) throws Exception {
		Context.check(file);
		OutputStream out = new FileOutputStream(file);
		if (direct)
			direct(url, out, c);
		else
			impl(size, url, out, c, 0, direct);
	}

	public static void download(String url, File file) throws Exception {
		download(FAST, url, file, null, false);
	}

	public static void download(String url, File file, Consumer<Double> c, boolean direct) throws Exception {
		download(SMOOTH, url, file, c, direct);
	}

	public static JsonElement read(String url) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		impl(FAST, url, out, null, 5000, true);
		return JsonParser.parseReader(
				new InputStreamReader(new ByteArrayInputStream(out.toByteArray()), StandardCharsets.UTF_8));
	}

	private static void impl(int size, String url, OutputStream out, Consumer<Double> c, int timeout, boolean direct)
			throws Exception {
		if (transport == null)
			transport = new com.google.api.client.http.javanet.NetHttpTransport();
		GenericUrl gurl = new GenericUrl(url);
		MediaHttpDownloader downloader = new MediaHttpDownloader(transport, (request) -> {
			if (timeout == 0) {
				request.setUnsuccessfulResponseHandler(
						new HttpBackOffUnsuccessfulResponseHandler(new ExponentialBackOff()));
				request.setIOExceptionHandler(new HttpBackOffIOExceptionHandler(new ExponentialBackOff()));
			} else {
				request.setConnectTimeout(timeout);
				request.setReadTimeout(timeout);
			}
			request.setEncoding(null);
		});

		if (timeout > 0 || direct) {
			downloader.setDirectDownloadEnabled(true);
		} else {
			downloader.setChunkSize(size);
			downloader.setProgressListener(new Progress(c));
		}
		downloader.download(gurl, out);
		out.close();
	}

	private static void direct(String url, OutputStream out, Consumer<Double> prog) throws IOException {
		URLConnection conn = new URL(url).openConnection();
		InputStream is = conn.getInputStream();
		int n, ava = 0, count = 0;
		byte[] buffer = new byte[BUFFER];
		while ((n = is.read(buffer)) != -1) {
			out.write(buffer, 0, n);
			count += n;
			if ((ava = is.available()) > 0)
				prog.accept(1.0 * count / ava);
		}
		out.close();
	}

}
