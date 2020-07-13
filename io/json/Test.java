package common.io.json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import common.io.json.JsonClass.RType;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField.GenType;

public class Test {

	public static class JsonTest_0 {

		@JsonClass
		public static class JsonA {

			public JsonB gen(String tag, JsonElement jobj) {
				return new JsonB(this);
			}

			@JsonField(generic = Integer.class)
			public final ArrayList<Integer> f0 = null;

			@JsonField()
			public JsonC f1;

			@JsonField(GenType = GenType.GEN, generator = "gen", generic = JsonB.class)
			public ArrayList<JsonB> f2;

			@JsonField(GenType = GenType.FILL)
			public JsonB f3 = new JsonB(this);

			@JsonField(generic = { Integer.class, String.class })
			public HashMap<Integer, String> f4 = null;

			@JsonField
			public JsonD data;

		}

		@JsonClass(read = RType.FILL)
		public static class JsonB {

			public JsonA par;

			@JsonField(generic = Integer.class)
			public HashSet<Integer> f;

			public JsonB(JsonA a) {
				par = a;
			}

			@OnInjected
			public void create() {
				System.out.println("OnInjected: " + f.size());
			}

		}

		@JsonClass(read = RType.MANUAL, generator = "gen")
		public static class JsonC {

			public static JsonC gen(JsonObject o) throws JsonException {
				return new JsonC();
			}

			@JsonField(tag = "a", IOType = JsonField.IOType.W)
			public int getA() {
				return 10;
			}

			@JsonField(tag = "a", IOType = JsonField.IOType.R)
			public void setA(int a) {
				System.out.println(a);
			};

		}

		@JsonClass(read = RType.ALLDATA)
		public static class JsonD {

			public int a;

			public int[] b;

			public String c;

			public String[] d;

			public boolean e;

		}

	}

	public static class JsonTest_1 {

		@JsonClass
		public static class JsonA {

			public Object gen(Class<?> cls, JsonElement elem) throws JsonException {
				if (cls == String.class)
					return elem.getAsString();
				return JsonDecoder.inject(elem.getAsJsonObject(), cls, new JsonB(this));
			}

			@JsonField
			public String name;

			@JsonField(generic = { String.class, JsonB.class }, GenType = JsonField.GenType.GEN, generator = "gen")
			public HashMap<String, JsonB> list;

			@JsonField(GenType = JsonField.GenType.FILL)
			public JsonC link = new JsonC(this);

		}

		@JsonClass(read = JsonClass.RType.FILL)
		public static class JsonB {

			public final JsonA parent;

			@JsonField
			public String key;

			@JsonField
			public String name;

			public JsonB(JsonA par) {
				parent = par;
			}

		}

		@JsonClass
		public static class JsonC {

			public final JsonA parent;

			public JsonC(JsonA par) {
				parent = par;
			}

			@JsonField(generic = { JsonB.class,
					Integer.class }, GenType = JsonField.GenType.GEN, generator = "gen", SerType = JsonField.SerType.FUNC, serializer = "ser")
			public HashMap<JsonB, Integer> list;

			public Object gen(Class<?> cls, JsonElement elem) {
				if (cls == JsonB.class)
					return parent.list.get(elem.getAsString());
				if (cls == Integer.class)
					return elem.getAsInt() * 10;
				return null;
			}

			public Object ser(JsonB b) {
				return b.key;
			}

		}

	}

	public static void main(String[] args) throws Exception {
		testJson();
	}

	public static void testJson() throws Exception {
		File f = new File("./testjson/test_1.json");
		JsonElement elem = JsonParser.parseReader(new FileReader(f));
		JsonTest_1.JsonA obj = JsonDecoder.decode(elem, JsonTest_1.JsonA.class);
		System.out.println(JsonEncoder.encode(obj));
	}

	public static void testIO() throws Exception {
		PackLoader.writePack(new File("./pack.pack"), new File("./src"), "ver", "id", "test", "password");
		PackLoader.readPack((str) -> getFile(new File("./out/" + str)), new File("./pack.pack"));

	}

	private static File getFile(File f) {
		try {
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			if (!f.exists())
				f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

}
