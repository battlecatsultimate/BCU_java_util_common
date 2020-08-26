package common.system.files;

import common.CommonStatic;
import common.pack.UserProfile;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class VFile implements Comparable<VFile> {

	public static VFile get(String str) {
		return getBCFileTree().find(str);
	}

	public static final VFileRoot getBCFileTree() {
		return UserProfile.getBCData().root;
	}

	public static VFile getFile(String path) {
		if (path.startsWith("./org/") || path.startsWith("./lang/"))
			return getBCFileTree().find(path);
		if (path.startsWith("./res/")) {
			File f = CommonStatic.def.route(path);
			if (!f.exists())
				return null;
			return new VFile(null, f.getName(), new FDFile(f));
		}
		return null;
	}

	public static Queue<String> readLine(String str) {
		return getFile(str).getData().readLine();
	}

	public String name;

	protected VFile parent;

	private final Map<String, VFile> subs;

	private FileData data;

	public int mark;

	/**
	 * constructor for directory
	 */
	public VFile(VFile par, String str) {
		parent = par;
		name = str;
		subs = new TreeMap<>();
		data = null;
		if (parent != null)
			parent.subs.put(name, this);
	}

	/**
	 * constructor for data file
	 */
	public VFile(VFile par, String str, FileData fd) {
		parent = par;
		name = str;
		subs = null;
		data = fd;
		if (parent != null)
			parent.subs.put(name, this);
	}

	/**
	 * constructor for root directory
	 */
	protected VFile(String str) {
		this(null, str);
	}

	@Override
	public int compareTo(VFile o) {
		return name.compareTo(o.name);
	}

	public int countSubDire() {
		int ans = 0;
		for (VFile f : subs.values())
			if (f.subs != null)
				ans++;
		return ans;
	}

	public void delete() {
		parent.subs.remove(name);
	}

	public FileData getData() {
		return data;
	}

	public List<VFile> getIf(Predicate<VFile> p) {
		List<VFile> ans = new ArrayList<VFile>();
		for (VFile v : list()) {
			if (p.test(v))
				ans.add(v);
			if (v.subs != null)
				ans.addAll(v.getIf(p));
		}
		return ans;
	}

	public String getName() {
		return name;
	}

	public VFile getParent() {
		return parent;
	}

	public String getPath() {
		if (parent != null)
			return parent.getPath() + "/" + name;
		return name;
	}

	public Collection<VFile> list() {
		return subs == null ? null : subs.values();
	}

	public void merge(VFile f) throws Exception {
		if (subs == null || f.subs == null)
			throw new Exception("merge can only happen for folders");
		for (VFile fi : f.subs.values()) {
			fi.parent = this;
			if (fi.subs == null || !subs.containsKey(fi.name))
				subs.put(fi.name, fi);
			else
				subs.get(fi.name).merge(fi);
		}
	}

	public void replace(FileData t) {
		delete();
		new VFile(parent, name, t);
	}

	public void setData(FileData fd) {
		data = fd;
	}

	@Override
	public String toString() {
		return name;
	}

}
