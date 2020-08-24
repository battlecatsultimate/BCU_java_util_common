package common.io.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import common.CommonStatic;
import common.io.PackLoader;
import common.io.PackLoader.Preload;
import common.io.PackLoader.ZipDesc;
import common.io.PackLoader.ZipDesc.FileDesc;
import common.io.assets.Admin;
import common.io.json.JsonClass.NoTag;
import common.io.json.JsonClass.RType;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField.GenType;
import common.pack.PackData;
import common.pack.UserProfile;
import common.util.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Test {

	public static class JsonTest_0 {

		@JsonClass
		public static class JsonA {

			@JsonField(generic = Integer.class)
			public final ArrayList<Integer> f0 = null;

			@JsonField()
			public JsonC f1;

			@JsonField(gen = GenType.GEN, generator = "gen", generic = JsonB.class)
			public ArrayList<JsonB> f2;

			@JsonField(gen = GenType.FILL)
			public JsonB f3 = new JsonB(this);

			@JsonField(generic = { Integer.class, String.class })
			public HashMap<Integer, String> f4 = null;

			@JsonField
			public JsonD data;

			public JsonB gen(Class<?> cls, JsonElement jobj) {
				return new JsonB(this);
			}

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

			public static JsonC gen(JsonElement o) throws JsonException {
				return new JsonC();
			}

			@JsonField(tag = "a", io = JsonField.IOType.W)
			public int getA() {
				return 10;
			}

			@JsonField(tag = "a", io = JsonField.IOType.R)
			public void setA(int a) {
				System.out.println(a);
			}

		}

		@JsonClass(noTag = NoTag.LOAD)
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

			@JsonField
			public String name;

			@JsonField(generic = { String.class, JsonB.class }, gen = JsonField.GenType.GEN)
			public HashMap<String, JsonB> list;

			@JsonField(gen = JsonField.GenType.FILL)
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

			@JsonField(generic = { JsonB.class,
					Integer.class }, gen = JsonField.GenType.GEN, generator = "gen", ser = JsonField.SerType.FUNC, serializer = "ser")
			public HashMap<JsonB, Integer> list;

			public JsonC(JsonA par) {
				parent = par;
			}

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

	public static class JsonTest_2 {

		@JsonClass
		public static class JsonA {

			public static final String REG = "test";

			@JsonField(usePool = true)
			public JsonB[] list;

		}

		@JsonClass(noTag = NoTag.LOAD)
		public static class JsonB {

			public String name;
			public int val;

		}

		public static void test() throws Exception {
			JsonA a = new JsonA();
			a.list = new JsonB[4];
			JsonB b0 = new JsonB();
			b0.name = "a";
			b0.val = 1;
			JsonB b1 = new JsonB();
			b1.name = "b";
			b1.val = 2;
			a.list[0] = a.list[1] = b0;
			a.list[2] = a.list[3] = b1;
			JsonElement out = JsonEncoder.encode(a);
			System.out.println(out);
			JsonA a1 = JsonDecoder.decode(out, JsonA.class);
			a1.list[0].val = 3;
			JsonElement out1 = JsonEncoder.encode(a1);
			System.out.println(out1);

		}

	}

	public static class JsonTest_3 {

		@JsonClass
		public static class JsonA<T> {

			@JsonField
			public T obj;

		}

		@JsonClass
		public static class JsonB {

			@JsonField
			public int val = 0;

		}

		@SuppressWarnings("unchecked")
		public static void test() throws Exception {
			System.out.println(Class.forName(JsonB.class.getName()));
			JsonA<JsonB> a = new JsonA<>();
			a.obj = new JsonB();
			a.obj.val = 10;
			String json = JsonEncoder.encode(a).toString();
			System.out.println(json);
			JsonA<JsonB> x = JsonDecoder.decode(JsonParser.parseString(json), JsonA.class);
			System.out.println(JsonEncoder.encode(x).toString());
		}

	}

	public static void main(String[] args) throws Exception {
		UserProfile.profile();
		CommonStatic.ctx = new Admin.AdminContext();
		JsonTest_3.test();
		// testIO();
	}

	public static void testIO() throws Exception {
		@SuppressWarnings("deprecation")
		PackData.PackDesc pd = new PackData.PackDesc();
		pd.author = "test";
		pd.BCU_VERSION = Data.revVer(41100);
		pd.id = "12345678abcdefgh";
		PackLoader.writePack(new File("./pack.pack"), new File("./src"), pd, "password");
		Preload ctx = (fd) -> fd.size < 4096;
		ZipDesc desc = PackLoader.readPack(ctx, new File("./pack.pack"));
		for (FileDesc fd : desc.files)
			if (!ctx.preload(fd)) {
				System.out.println("large: " + fd.path);
				File f = new File("./out/" + fd.path);
				getFile(f);
				FileOutputStream fos = new FileOutputStream(f);
				byte[] bs = new byte[fd.size];
				desc.readFile(fd.path).read(bs);
				fos.write(bs);
				fos.close();
			}

	}

	public static void testJson() throws Exception {
		File f = new File("./../BCU-JSON-IO/testjson/test_0.json");
		JsonElement elem = JsonParser.parseReader(new FileReader(f));
		JsonTest_0.JsonA obj = JsonDecoder.decode(elem, JsonTest_0.JsonA.class);
		System.out.println(JsonEncoder.encode(obj));
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
