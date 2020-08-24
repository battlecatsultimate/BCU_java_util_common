package common.io;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.*;
import com.google.api.client.util.ExponentialBackOff;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import common.CommonStatic;
import common.pack.Context;
import common.pack.Context.ErrType;
import common.util.Data;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.function.Consumer;

public class WebFileIO {

	public static final int SMOOTH = 1 << 16, FAST = 1 << 18, MAX = 1 << 20;

	private static HttpTransport transport;

	public static boolean download(int size, String url, File file, Consumer<Progress> c) {
		try {
			Data.err(() -> Context.check(file));
			OutputStream out = new FileOutputStream(file);
			impl(size, url, out, c);
			System.out.println("download success: " + url);
			return true;
		} catch (Exception e) {
			CommonStatic.ctx.noticeErr(e, ErrType.WARN, "failed to download " + url);
			return false;
		}
	}

	public static boolean download(String url, File file) {
		return download(FAST, url, file, null);
	}

	public static boolean download(String url, File file, Consumer<Progress> c) {
		return download(SMOOTH, url, file, c);
	}

	public static JsonElement read(String url) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			impl(FAST, url, out, null);
			return JsonParser.parseReader(new InputStreamReader(new ByteArrayInputStream(out.toByteArray())));
		} catch (Exception e) {
			CommonStatic.ctx.noticeErr(e, ErrType.WARN, "cannot connect to internet");
			return null;
		}
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
