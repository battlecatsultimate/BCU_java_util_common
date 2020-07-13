package common.io.json;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import common.io.json.JsonException.Type;

public class JsonDecoder {

	@Documented
	@Retention(RUNTIME)
	@Target({ METHOD })
	public static @interface OnInjected {
	}

	/**
	 * parse the json element into object
	 * 
	 * @param elem   the json element to parse
	 * @param cls    the class of the target value
	 * @param cont   the class holding this value
	 * @param jfield the annotation of the target field
	 * @param field  the field object of the target field
	 */
	public static Object decode(JsonElement elem, Class<?> cls, Object cont, JsonField jfield, Field field)
			throws JsonException {
		if (JsonElement.class.isAssignableFrom(cls))
			return elem;
		if (cls == Boolean.TYPE || cls == Boolean.class)
			return getBoolean(elem);
		if (cls == Integer.TYPE || cls == Integer.class)
			return getInt(elem);
		if (cls == Double.TYPE || cls == Double.class)
			return getDouble(elem);
		if (cls == String.class)
			return getString(elem);
		if (cls.isArray()) {
			if (elem.isJsonNull())
				return null;
			if (!elem.isJsonArray())
				throw new JsonException(Type.TYPE_MISMATCH, elem, "this element is not array");
			JsonArray jarr = elem.getAsJsonArray();
			int n = jarr.size();
			Class<?> ccls = cls.getComponentType();
			Object arr = null;
			if (jfield != null && jfield.GenType() == JsonField.GenType.FILL) {
				if (field == null || cont == null)
					throw new JsonException(Type.TAG, elem, "no enclosing object");
				try {
					arr = field.get(cont);
				} catch (Exception e) {
					JsonException je = new JsonException(Type.INTERNAL, elem, "");
					je.initCause(e);
					throw je;
				}
			} else
				arr = Array.newInstance(ccls, n);
			for (int i = 0; i < n; i++)
				Array.set(arr, i, decode(jarr.get(i), ccls, cont, jfield, field));
			return arr;
		}
		if (List.class.isAssignableFrom(cls)) {
			return decodeList(elem, cls, cont, jfield, field);
		}
		if (Map.class.isAssignableFrom(cls)) {
			return decodeMap(elem, cls, cont, jfield, field);
		}
		if (Set.class.isAssignableFrom(cls)) {
			return decodeSet(elem, cls, cont, jfield, field);
		}
		if (cont != null && jfield != null) {
			try {
				if (jfield.GenType() == JsonField.GenType.FILL) {
					Object val = field.get(cont);
					if (cls.getAnnotation(JsonClass.class) != null)
						inject(elem.getAsJsonObject(), cls, val);
					return val;
				}
				if (jfield.GenType() == JsonField.GenType.GEN) {
					Class<?> ccls = cont.getClass();
					Method m = ccls.getMethod(jfield.generator(), Class.class, JsonElement.class);
					Object val = m.invoke(cont, cls, elem);
					cls = val.getClass();
					return val;

				}
			} catch (Exception e) {
				if (e instanceof JsonException)
					throw (JsonException) e;
				JsonException je = new JsonException(Type.INTERNAL, elem, "");
				je.initCause(e);
				throw je;
			}
		}
		JsonClass jc = cls.getAnnotation(JsonClass.class);
		if (jc != null) {
			return decodeObject(elem, cls, cont, jfield, field, jc);
		}
		throw new JsonException(Type.UNDEFINED, elem, "class not possible to generate");
	}

	@SuppressWarnings("unchecked")
	public static <T> T decode(JsonElement elem, Class<T> cls) throws JsonException {
		return (T) decode(elem, cls, null, null, null);
	}

	public static boolean getBoolean(JsonElement elem) throws JsonException {
		if (!elem.isJsonPrimitive() || !((JsonPrimitive) elem).isBoolean())
			throw new JsonException(Type.TYPE_MISMATCH, elem, "this element is not boolean");
		return elem.getAsBoolean();
	}

	public static double getDouble(JsonElement elem) throws JsonException {
		if (!elem.isJsonPrimitive() || !((JsonPrimitive) elem).isNumber())
			throw new JsonException(Type.TYPE_MISMATCH, elem, "this element is not number");
		return elem.getAsDouble();
	}

	public static int getInt(JsonElement elem) throws JsonException {
		if (!elem.isJsonPrimitive() || !((JsonPrimitive) elem).isNumber())
			throw new JsonException(Type.TYPE_MISMATCH, elem, "this element is not number");
		return elem.getAsInt();
	}

	public static String getString(JsonElement elem) throws JsonException {
		if (elem.isJsonNull())
			return null;
		if (!elem.isJsonPrimitive() || !((JsonPrimitive) elem).isString())
			throw new JsonException(Type.TYPE_MISMATCH, elem, "this element is not string");
		return elem.getAsString();
	}

	/**
	 * inject the values from the json object into the target object
	 */
	public static Object inject(JsonObject jobj, Class<?> cls, Object obj) throws JsonException {
		JsonClass jc = cls.getAnnotation(JsonClass.class);
		if (jc == null)
			throw new JsonException(Type.TYPE_MISMATCH, jobj, "no annotation for class " + cls);
		if (cls.getSuperclass().getAnnotation(JsonClass.class) != null)
			inject(jobj, cls.getSuperclass(), obj);
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs) {
			JsonField jf = f.getAnnotation(JsonField.class);
			if (jc.read() == JsonClass.RType.ALLDATA)
				jf = JsonField.DEF;
			if (jf == null || jf.IOType() == JsonField.IOType.W)
				continue;
			try {
				String tag = jf.tag();
				if (tag.length() == 0)
					tag = f.getName();
				if (!jobj.has(tag))
					continue;
				JsonElement elem = jobj.get(tag);
				f.setAccessible(true);
				f.set(obj, decode(elem, f.getType(), obj, jf, f));
			} catch (Exception e) {
				if (jf.noErr())
					continue;
				if (e instanceof JsonException)
					throw (JsonException) e;
				JsonException je = new JsonException(Type.INTERNAL, null, "");
				je.initCause(e);
				throw je;
			}
		}
		Method oni = null;
		for (Method m : cls.getDeclaredMethods()) {
			if (m.getAnnotation(OnInjected.class) != null)
				if (oni == null)
					oni = m;
				else
					throw new JsonException(Type.FUNC, null, "duplicate OnInjected");
			JsonField jf = m.getAnnotation(JsonField.class);
			if (jf == null || jf.IOType() == JsonField.IOType.W)
				continue;
			if (jf.IOType() == JsonField.IOType.RW)
				throw new JsonException(Type.FUNC, null, "functional fields should not have RW type");
			if (m.getParameterCount() != 1)
				throw new JsonException(Type.FUNC, null, "parameter count should be 1");
			String tag = jf.tag();
			if (tag.length() == 0)
				throw new JsonException(Type.TAG, null, "function fields must have tag");
			if (!jobj.has(tag))
				continue;
			JsonElement elem = jobj.get(tag);
			Class<?> ccls = m.getParameters()[0].getType();
			try {
				m.invoke(obj, decode(elem, ccls, obj, jf, null));
			} catch (Exception e) {
				if (jf.noErr())
					continue;
				if (e instanceof JsonException)
					throw (JsonException) e;
				JsonException je = new JsonException(Type.INTERNAL, null, "");
				je.initCause(e);
				throw je;
			}
		}
		if (oni != null)
			try {
				oni.invoke(obj);
			} catch (Exception e) {
				if (e instanceof JsonException)
					throw (JsonException) e;
				JsonException je = new JsonException(Type.INTERNAL, null, "");
				je.initCause(e);
				throw je;
			}
		return obj;
	}

	private static List<Object> decodeList(JsonElement elem, Class<?> cls, Object cont, JsonField jfield, Field field)
			throws JsonException {
		if (jfield == null || jfield.generic().length != 1)
			throw new JsonException(Type.TAG, null, "generic data structure requires typeProvider tag");
		if (elem.isJsonNull())
			return null;
		if (!elem.isJsonArray())
			throw new JsonException(Type.TYPE_MISMATCH, elem, "this element is not array");
		JsonArray jarr = elem.getAsJsonArray();
		int n = jarr.size();
		try {
			@SuppressWarnings("unchecked")
			List<Object> val = (List<Object>) cls.newInstance();
			for (int i = 0; i < n; i++) {
				val.add(decode(jarr.get(i), jfield.generic()[0], cont, jfield, field));
			}
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			JsonException je = new JsonException(Type.INTERNAL, null, "");
			je.initCause(e);
			throw je;
		}
	}

	private static Map<Object, Object> decodeMap(JsonElement elem, Class<?> cls, Object cont, JsonField jfield,
			Field field) throws JsonException {
		if (jfield == null || jfield.generic().length != 2)
			throw new JsonException(Type.TAG, null, "generic data structure requires typeProvider tag");
		if (elem.isJsonNull())
			return null;
		if (!elem.isJsonArray())
			throw new JsonException(Type.TYPE_MISMATCH, elem, "this element is not array");

		JsonArray jarr = elem.getAsJsonArray();
		int n = jarr.size();
		try {
			@SuppressWarnings("unchecked")
			Map<Object, Object> val = (Map<Object, Object>) cls.newInstance();
			for (int i = 0; i < n; i++) {
				JsonObject obj = jarr.get(i).getAsJsonObject();
				Object key = decode(obj.get("key"), jfield.generic()[0], cont, jfield, field);
				Object ent = decode(obj.get("val"), jfield.generic()[1], cont, jfield, field);
				val.put(key, ent);
			}
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			JsonException je = new JsonException(Type.INTERNAL, null, "");
			je.initCause(e);
			throw je;
		}
	}

	private static Object decodeObject(JsonElement elem, Class<?> cls, Object cont, JsonField jfield, Field field,
			JsonClass jc) throws JsonException {
		if (elem.isJsonNull())
			return null;
		if (!elem.isJsonObject())
			throw new JsonException(Type.TYPE_MISMATCH, elem, "this element is not object");
		JsonObject jobj = elem.getAsJsonObject();
		if (jc.read() == JsonClass.RType.DATA || jc.read() == JsonClass.RType.ALLDATA) {
			try {
				return inject(jobj, cls, cls.newInstance());
			} catch (Exception e) {
				if (e instanceof JsonException)
					throw (JsonException) e;
				JsonException je = new JsonException(Type.INTERNAL, elem, "");
				je.initCause(e);
				throw je;
			}
		} else if (jc.read() == JsonClass.RType.MANUAL) {
			String func = jc.generator();
			if (func.length() == 0)
				throw new JsonException(Type.FUNC, elem, "no generate function");
			try {
				Method m = cls.getMethod(func, JsonObject.class);
				Object val = m.invoke(null, jobj);
				cls = val.getClass();
				return inject(jobj, cls, val);
			} catch (Exception e) {
				if (e instanceof JsonException)
					throw (JsonException) e;
				JsonException je = new JsonException(Type.INTERNAL, elem, "");
				je.initCause(e);
				throw je;
			}
		} else
			throw new JsonException(Type.UNDEFINED, elem, "class not possible to generate");
	}

	private static Set<Object> decodeSet(JsonElement elem, Class<?> cls, Object cont, JsonField jfield, Field field)
			throws JsonException {
		if (jfield == null || jfield.generic().length != 1)
			throw new JsonException(Type.TAG, null, "generic data structure requires typeProvider tag");
		if (elem.isJsonNull())
			return null;
		if (!elem.isJsonArray())
			throw new JsonException(Type.TYPE_MISMATCH, elem, "this element is not array");
		JsonArray jarr = elem.getAsJsonArray();
		int n = jarr.size();
		try {
			@SuppressWarnings("unchecked")
			Set<Object> val = (Set<Object>) cls.newInstance();
			for (int i = 0; i < n; i++) {
				val.add(decode(jarr.get(i), jfield.generic()[0], cont, jfield, field));
			}
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			JsonException je = new JsonException(Type.INTERNAL, null, "");
			je.initCause(e);
			throw je;
		}
	}

}
