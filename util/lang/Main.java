package common.util.lang;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import common.io.json.JsonDecoder;
import common.io.json.JsonEncoder;
import common.util.Data.Proc;
import common.util.Data.Proc.ProcItem;
import common.util.lang.Formatter.Context;

public class Main {

	public static void main(String[] args) throws Exception {
		args = new String[] { "parse", "locale", "proc","KB" };
		if (args.length == 3 && args[0].equals("generate")) {
			if (args[1].equals("proc")) {
				File f = fix(args[2]);
				JsonElement e = JsonEncoder.encode(Proc.blank());
				e.getAsJsonObject().add("context", JsonEncoder.encode(new Context()));
				String str = e.toString();
				Files.write(f.toPath(), Arrays.asList(str));
				System.out.println("file generated");
				return;
			}
			if (args[1].equals("locale")) {
				File f = fix(args[2]);
				JsonElement jo = JsonEncoder.encode(ProcLang.gen(null));
				String str = jo.toString();
				System.out.println(str);
				Files.write(f.toPath(), Arrays.asList(str));
				System.out.println("file generated");
				return;
			}
		}
		if (args.length == 4 && args[0].equals("parse")) {
			JsonElement locale = JsonParser.parseReader(Files.newBufferedReader(parse(args[1])));
			JsonElement jeproc = JsonParser.parseReader(Files.newBufferedReader(parse(args[2])));
			Context ctx = null;
			if (jeproc.getAsJsonObject().has("context"))
				ctx = JsonDecoder.decode(jeproc.getAsJsonObject().get("context"), Context.class);
			else
				ctx = new Context();
			ProcItem item = JsonDecoder.decode(jeproc, Proc.class).get(args[3]);
			ProcLang lang = JsonDecoder.decode(locale, ProcLang.class);
			System.out.println(JsonEncoder.encode(lang));
			String pattern = lang.get(args[3]).format;
			System.out.println(Formatter.format(pattern, item, ctx));
			return;
		}
		System.out.println("you entered: " + Arrays.asList(args).toString());
		System.out.println("usage: generate proc [name] // for generate a new PROC json file");
		System.out.println("usage: generate locale [name] // for generate a new LOCALE json file");
		System.out.println("usage: parse [locale] [file] [proc] // parse a proc in a proc file with specified locale");
	}

	private static File fix(String path) throws IOException {
		if (!path.startsWith("./"))
			path = "./" + path;
		if (!path.endsWith(".json"))
			path += ".json";
		File f = new File(path);
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if (!f.exists())
			f.createNewFile();
		return f;
	}

	private static Path parse(String path) {
		if (!path.startsWith("./"))
			path = "./" + path;
		if (!path.endsWith(".json"))
			path += ".json";
		return new File(path).toPath();
	}

}
