package common.pack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonEncoder;
import common.io.json.JsonField;
import common.io.json.JsonField.IOType;
import common.pack.PackData.Indexable;
import common.util.Data;

@JsonClass
public class FixIndexList<T> extends Data {

	public static class FixIndexMap<T extends Indexable<?>> extends FixIndexList<T> {

		public FixIndexMap(Class<T> cls) {
			super(cls);
		}

	}

	protected T[] arr;
	private int size = 0;

	@SuppressWarnings("unchecked")
	public FixIndexList(Class<T> cls) {
		arr = (T[]) Array.newInstance(cls, 16);
	}

	public void add(T t) {
		arr[nextInd()] = t;
		if (t != null)
			size++;
	}

	public void clear() {
		for (int i = 0; i < arr.length; i++)
			arr[i] = null;
		size = 0;
	}

	public boolean contains(T t) {
		if (t == null)
			return false;
		for (T a : arr)
			if (t.equals(a))
				return true;
		return false;
	}

	public void forEach(BiConsumer<Integer, T> c) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i] != null)
				c.accept(i, arr[i]);
	}

	public T get(int ind) {
		if (ind < 0 || ind >= arr.length)
			return null;
		return arr[ind];
	}

	public int getFirstInd() {
		if (size == 0)
			return -1;
		for (int i = 0; i < arr.length; i++)
			if (arr[i] != null)
				return i;
		return -1;
	}

	public List<T> getList() {
		List<T> ans = new ArrayList<>(size);
		for (T t : arr)
			if (t != null)
				ans.add(t);
		return ans;
	}

	public Map<Integer, T> getMap() {
		Map<Integer, T> map = new TreeMap<>();
		for (int i = 0; i < arr.length; i++)
			if (arr[i] != null)
				map.put(i, arr[i]);
		return map;
	}

	public int indexOf(T tar) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i] != null && arr[i].equals(tar))
				return i;
		return -1;
	}

	public int nextInd() {
		for (int i = 0; i < arr.length; i++)
			if (arr[i] == null)
				return i;
		arr = Arrays.copyOf(arr, arr.length * 2);
		return nextInd();
	}

	public void remove(T t) {
		if (t == null)
			return;
		for (int i = 0; i < arr.length; i++)
			if (arr[i] == t) {
				arr[i] = null;
				size--;
			}
	}

	public void set(int ind, T t) {
		while (arr.length < ind)
			arr = Arrays.copyOf(arr, arr.length * 2);
		if (arr[ind] != null)
			size--;
		if (t != null)
			size++;
		arr[ind] = t;

	}

	public int size() {
		return size;
	}

	@JsonField(tag = "data", io = IOType.R)
	public void zgen(JsonElement e) throws Exception {
		@SuppressWarnings("unchecked")
		Class<T> cls = (Class<T>) arr.getClass().getComponentType();
		JsonArray jarr = e.getAsJsonArray();
		for (int i = 0; i < jarr.size(); i++) {
			JsonObject ji = jarr.get(i).getAsJsonObject();
			int ind = ji.get("ind").getAsInt();
			T val = JsonDecoder.decode(ji.get("val"), cls);
			this.set(ind, val);
		}

	}

	@JsonField(tag = "data", io = IOType.W)
	public JsonElement zser() throws Exception {
		JsonArray data = new JsonArray();
		for (int i = 0; i < arr.length; i++)
			if (arr[i] != null) {
				JsonObject ent = new JsonObject();
				ent.addProperty("ind", i);
				ent.add("val", JsonEncoder.encode(arr[i]));
			}
		return data;
	}

}
