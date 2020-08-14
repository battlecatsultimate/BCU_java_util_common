package common.io.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import common.io.json.JsonClass.JCGeneric;
import common.io.json.JsonClass.JCGenericWrite;
import common.io.json.JsonException.Type;
import common.util.Data;

public class JsonEncoder {

	public static JsonElement encode(Object obj) {
		return Data.err(() -> encode(obj, null));
	}

	private static JsonElement encode(Object obj, JsonEncoder par) throws Exception {
		if (obj == null)
			return JsonNull.INSTANCE;
		if (obj instanceof JsonElement)
			return (JsonElement) obj;
		if (obj instanceof Number)
			return new JsonPrimitive((Number) obj);
		if (obj instanceof Boolean)
			return new JsonPrimitive((Boolean) obj);
		if (obj instanceof String)
			return new JsonPrimitive((String) obj);
		if (obj instanceof Class)
			return new JsonPrimitive(((Class<?>) obj).getName());
		Class<?> cls = obj.getClass();
		if (cls.isArray()) {
			if (par.curjfld != null && par.curjfld.usePool()) {
				JsonField.Handler handler = new JsonField.Handler();
				int n = Array.getLength(obj);
				JsonArray jarr = new JsonArray();
				for (int i = 0; i < n; i++)
					jarr.add(handler.add(Array.get(obj, i)));
				JsonObject jobj = new JsonObject();
				jobj.add("pool", encode(handler.list));
				jobj.add("data", jarr);
				return jobj;
			}
			int n = Array.getLength(obj);
			JsonArray arr = new JsonArray(n);
			for (int i = 0; i < n; i++)
				arr.add(encode(Array.get(obj, i), par));
			return arr;
		}
		if (cls.getAnnotation(JCGeneric.class) != null && par != null && par.curjfld.alias().length == 1) {
			JCGeneric jcg = cls.getAnnotation(JCGeneric.class);
			Class<?> alias = par.curjfld.alias()[0];
			boolean found = false;
			for (Class<?> ala : jcg.value())
				if (ala == alias) {
					found = true;
					break;
				}
			if (!found)
				throw new JsonException(Type.TYPE_MISMATCH, null, "class not present in JCGeneric");
			for (Method m : cls.getMethods()) {
				JCGenericWrite jcgw = m.getAnnotation(JCGenericWrite.class);
				if (jcgw != null && jcgw.value() == alias) {
					return encode(m.invoke(obj), par);
				}
			}
			throw new JsonException(Type.TYPE_MISMATCH, null, "no JCGenericWrite present");
		}
		if (par != null && par.curjfld != null) {
			JsonField jfield = par.curjfld;
			if (jfield.ser() == JsonField.SerType.FUNC) {
				if (jfield.serializer().length() == 0)
					throw new JsonException(Type.FUNC, null, "no serializer function");
				Method m = par.obj.getClass().getMethod(jfield.serializer(), cls);
				return encode(m.invoke(par.obj, obj));
			} else if (jfield.ser() == JsonField.SerType.CLASS) {
				JsonClass cjc = cls.getAnnotation(JsonClass.class);
				if (cjc == null || cjc.serializer().length() == 0)
					throw new JsonException(Type.FUNC, null, "no serializer function");
				String func = cjc.serializer();
				Method m = cls.getMethod(func);
				return encode(m.invoke(obj), null);
			}
		}
		JsonClass jc = cls.getAnnotation(JsonClass.class);
		if (jc != null)
			if (jc.write() == JsonClass.WType.DEF)
				return new JsonEncoder(par, obj).ans;
			else if (jc.write() == JsonClass.WType.CLASS) {
				if (jc.serializer().length() == 0)
					throw new JsonException(Type.FUNC, null, "no serializer function");
				String func = jc.serializer();
				Method m = cls.getMethod(func);
				return encode(m.invoke(obj), null);
			}
		if (obj instanceof List)
			return encodeList((List<?>) obj, par);
		if (obj instanceof Set)
			return encodeSet((Set<?>) obj, par);
		if (obj instanceof Map)
			return encodeMap((Map<?, ?>) obj, par);
		throw new JsonException(Type.UNDEFINED, null, "object " + obj + " not defined");
	}

	private static JsonElement encodeList(List<?> list, JsonEncoder par) throws Exception {
		if (par != null && par.curjfld != null && par.curjfld.usePool()) {
			JsonField.Handler handler = new JsonField.Handler();
			int n = list.size();
			JsonArray jarr = new JsonArray();
			for (int i = 0; i < n; i++)
				jarr.add(handler.add(list.get(i)));
			JsonObject jobj = new JsonObject();
			jobj.add("pool", encode(handler.list));
			jobj.add("data", jarr);
			return jobj;
		}
		JsonArray ans = new JsonArray(list.size());
		for (Object obj : list)
			ans.add(encode(obj, par));
		return ans;
	}

	private static JsonArray encodeMap(Map<?, ?> map, JsonEncoder par) throws Exception {
		JsonArray ans = new JsonArray(map.size());
		for (Entry<?, ?> obj : map.entrySet()) {
			JsonObject ent = new JsonObject();
			ent.add("key", encode(obj.getKey(), par));
			ent.add("val", encode(obj.getValue(), par));
			ans.add(ent);
		}
		return ans;
	}

	private static JsonArray encodeSet(Set<?> set, JsonEncoder par) throws Exception {
		JsonArray ans = new JsonArray(set.size());
		for (Object obj : set)
			ans.add(encode(obj, par));
		return ans;
	}

	private final JsonEncoder par;
	private final Object obj;
	private final JsonObject ans = new JsonObject();

	private JsonClass curjcls;
	private JsonField curjfld;

	private JsonEncoder(JsonEncoder parent, Object object) throws Exception {
		par = parent;
		obj = object;
		encode(obj.getClass());
	}

	private void encode(Class<?> cls) throws Exception {
		if (cls.getSuperclass().getAnnotation(JsonClass.class) != null)
			encode(cls.getSuperclass());
		curjcls = cls.getAnnotation(JsonClass.class);
		for (Field f : cls.getDeclaredFields())
			if (curjcls.noTag() == JsonClass.NoTag.LOAD || f.getAnnotation(JsonField.class) != null) {
				if (Modifier.isStatic(f.getModifiers()))
					continue;
				JsonField jf = f.getAnnotation(JsonField.class);
				if (jf == null)
					jf = JsonField.DEF;
				if (jf.block() || jf.io() == JsonField.IOType.R)
					continue;
				String tag = jf.tag().length() == 0 ? f.getName() : jf.tag();
				f.setAccessible(true);
				ans.add(tag, encode(f.get(obj), getInvoker()));
			}
		for (Method m : cls.getDeclaredMethods())
			if (m.getAnnotation(JsonField.class) != null) {
				JsonField jf = m.getAnnotation(JsonField.class);
				if (jf.io() == JsonField.IOType.R)
					continue;
				if (jf.io() == JsonField.IOType.RW)
					throw new JsonException(Type.FUNC, null, "functional fields should not have RW type");
				String tag = jf.tag();
				if (tag.length() == 0)
					throw new JsonException(Type.TAG, null, "function fields must have tag");
				ans.add(tag, encode(m.invoke(obj), getInvoker()));
			}
	}

	private JsonEncoder getInvoker() {
		return curjcls.bypass() ? par : this;
	}

}
