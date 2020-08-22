package common.system.files;

import common.CommonStatic;
import common.io.PackLoader.ZipDesc.FileDesc;
import common.pack.UserProfile;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class VFile<T extends FileData> implements Comparable<VFile<T>> {

    public static VFile<FileDesc> get(String str) {
        return getBCFileTree().find(str);
    }

    public static final VFileRoot<FileDesc> getBCFileTree() {
        return UserProfile.getBCData().root;
    }

    public static VFile<? extends FileData> getFile(String path) {
        if (path.startsWith("./org/") || path.startsWith("./lang/"))
            return getBCFileTree().find(path);
        if (path.startsWith("./res/")) {
            File f = CommonStatic.def.route(path);
            if (!f.exists())
                return null;
            return new VFile<FDFile>(null, f.getName(), new FDFile(f));
        }
        return null;
    }

    public static Queue<String> readLine(String str) {
        return getFile(str).getData().readLine();
    }

    public String name;

    protected VFile<T> parent;

    private final Map<String, VFile<T>> subs;

    private T data;

    public int mark;

    /**
     * constructor for root directory
     */
    protected VFile(String str) {
        this(null, str);
    }

    /**
     * constructor for directory
     */
    protected VFile(VFile<T> par, String str) {
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
    protected VFile(VFile<T> par, String str, T fd) {
        parent = par;
        name = str;
        subs = null;
        data = fd;
        if (parent != null)
            parent.subs.put(name, this);
    }

    @Override
    public int compareTo(VFile<T> o) {
        return name.compareTo(o.name);
    }

    public int countSubDire() {
        int ans = 0;
        for (VFile<T> f : subs.values())
            if (f.subs != null)
                ans++;
        return ans;
    }

    public void delete() {
        parent.subs.remove(name);
    }

    public T getData() {
        return data;
    }

    public List<VFile<T>> getIf(Predicate<VFile<T>> p) {
        List<VFile<T>> ans = new ArrayList<VFile<T>>();
        for (VFile<T> v : list()) {
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

    public VFile<T> getParent() {
        return parent;
    }

    public String getPath() {
        if (parent != null)
            return parent.getPath() + "/" + name;
        return name;
    }

    public Collection<VFile<T>> list() {
        return subs == null ? null : subs.values();
    }

    public void merge(VFile<T> f) throws Exception {
        if (subs == null || f.subs == null)
            throw new Exception("merge can only happen for folders");
        for (VFile<T> fi : f.subs.values()) {
            fi.parent = this;
            if (fi.subs == null || !subs.containsKey(fi.name))
                subs.put(fi.name, fi);
            else
                subs.get(fi.name).merge(fi);
        }
    }

    public void replace(T t) {
        delete();
        new VFile<T>(parent, name, t);
    }

    public void setData(T fd) {
        data = fd;
    }

    @Override
    public String toString() {
        return name;
    }

}
