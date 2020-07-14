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

import common.io.json.JsonException.Type;

public class JsonEncoder {

	public static JsonElement encode(Object obj) throws Exception {
		return encode(obj, null, null);
	}

	public static JsonElement encode(Object obj, JsonField jfield, Object holder) throws Exception {
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
		Class<?> cls = obj.getClass();
		if (cls.isArray()) {
			if (jfield != null && jfield.usePool()) {
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
				arr.add(encode(Array.get(obj, i), jfield, holder));
			return arr;
		}
		if (obj instanceof List)
			return encodeList((List<?>) obj, jfield, holder);
		if (obj instanceof Set)
			return encodeSet((Set<?>) obj, jfield, holder);
		if (obj instanceof Map)
			return encodeMap((Map<?, ?>) obj, jfield, holder);

		if (jfield != null) {
			if (jfield.ser() == JsonField.SerType.FUNC) {
				if (holder == null || jfield.serializer().length() == 0)
					throw new JsonException(Type.FUNC, null, "no serializer function");
				Method m = holder.getClass().getMethod(jfield.serializer(), cls);
				return encode(m.invoke(holder, obj));
			} else if (jfield.ser() == JsonField.SerType.CLASS) {
				JsonClass cjc = cls.getAnnotation(JsonClass.class);
				if (cjc == null || cjc.serializer().length() == 0)
					throw new JsonException(Type.FUNC, null, "no serializer function");
				String func = cjc.serializer();
				Method m = cls.getMethod(func);
				return encode(m.invoke(obj), null, null);
			}
		}

		JsonClass jc = cls.getAnnotation(JsonClass.class);
		if (jc != null)
			if (jc.write() == JsonClass.WType.DEF)
				return encodeObject(new JsonObject(), obj, cls);
			else if (jc.write() == JsonClass.WType.CLASS) {
				if (jc.serializer().length() == 0)
					throw new JsonException(Type.FUNC, null, "no serializer function");
				String func = jc.serializer();
				Method m = cls.getMethod(func);
				return encode(m.invoke(obj), null, null);
			}

		throw new JsonException(Type.UNDEFINED, null, "object " + obj + " not defined");
	}

	private static JsonElement encodeList(List<?> list, JsonField jfield, Object holder) throws Exception {
		if (jfield != null && jfield.usePool()) {
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
			ans.add(encode(obj, jfield, holder));
		return ans;
	}

	private static JsonArray encodeMap(Map<?, ?> map, JsonField jfield, Object holder) throws Exception {
		JsonArray ans = new JsonArray(map.size());
		for (Entry<?, ?> obj : map.entrySet()) {
			JsonObject ent = new JsonObject();
			ent.add("key", encode(obj.getKey(), jfield, holder));
			ent.add("val", encode(obj.getValue(), jfield, holder));
			ans.add(ent);
		}
		return ans;
	}

	private static JsonObject encodeObject(JsonObject jobj, Object obj, Class<?> cls) throws Exception {
		if (cls.getSuperclass().getAnnotation(JsonClass.class) != null)
			encodeObject(jobj, obj, cls);
		JsonClass jc = cls.getAnnotation(JsonClass.class);
		for (Field f : cls.getDeclaredFields())
			if (jc.noTag() == JsonClass.NoTag.LOAD || f.getAnnotation(JsonField.class) != null) {
				if(Modifier.isStatic(f.getModifiers()))
					continue;
				JsonField jf = f.getAnnotation(JsonField.class);
				if (jf == null)
					jf = JsonField.DEF;
				if (jf.block() || jf.io() == JsonField.IOType.R)
					continue;
				String tag = jf.tag().length() == 0 ? f.getName() : jf.tag();
				f.setAccessible(true);
				jobj.add(tag, encode(f.get(obj), jf, obj));
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
				jobj.add(tag, encode(m.invoke(obj), jf, obj));
			}
		return jobj;
	}

	private static JsonArray encodeSet(Set<?> set, JsonField jfield, Object holder) throws Exception {
		JsonArray ans = new JsonArray(set.size());
		for (Object obj : set)
			ans.add(encode(obj, jfield, holder));
		return ans;
	}

}
