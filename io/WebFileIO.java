package common.io;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.function.Consumer;

public class WebFileIO {

	public static final int SMOOTH = 1 << 16, FAST = 1 << 18, MAX = 1 << 20;

	@StaticPermitted(StaticPermitted.Type.TEMP)
	private static HttpTransport transport;

	public static void download(int size, String url, File file, Consumer<Progress> c) throws Exception {
		Context.check(file);
		OutputStream out = new FileOutputStream(file);
		impl(size, url, out, c);
		System.out.println("download success: " + url);
	}

	public static void download(String url, File file) throws Exception {
		download(FAST, url, file, null);
	}

	public static void download(String url, File file, Consumer<Progress> c) throws Exception {
		download(SMOOTH, url, file, c);
	}

	public static JsonElement read(String url) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		impl(FAST, url, out, null);
		return JsonParser.parseReader(new InputStreamReader(new ByteArrayInputStream(out.toByteArray())));
	}

	private static void impl(int size, String url, OutputStream out, Consumer<Progress> c) throws Exception {
		if (transport == null)
			transport = GoogleNetHttpTransport.newTrustedTransport();
		GenericUrl gurl = new GenericUrl(url);
		MediaHttpDownloader downloader = new MediaHttpDownloader(transport, (request) -> {
			request.setUnsuccessfulResponseHandler(
					new HttpBackOffUnsuccessfulResponseHandler(new ExponentialBackOff()));
			request.setIOExceptionHandler(new HttpBackOffIOExceptionHandler(new ExponentialBackOff()));

		});
		downloader.setChunkSize(size);
		downloader.setProgressListener(new Progress(c));
		downloader.download(gurl, out);
		out.close();

	}

}
