package common.io.assets;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import common.CommonStatic;
import common.io.PackLoader;
import common.io.PackLoader.ZipDesc;
import common.io.PackLoader.ZipDesc.FileDesc;
import common.io.assets.Admin.StaticPermitted;
import common.pack.Context;
import common.pack.PackData.PackDesc;
import common.pack.UserProfile;
import common.system.fake.ImageBuilder;
import common.util.Data;
import jogl.util.GLIB;
import utilpc.UtilPC;
import utilpc.awt.PCIB;

@StaticPermitted
public class Admin {

	public static class AdminContext implements Context {

		@Override
		public boolean confirmDelete() {
			System.out.println("skip delete confirmation");
			return true;
		}

		@Override
		public File getAssetFile(String string) {
			return new File("./assets/" + string);
		}

		@Override
		public File getLangFile(String file) {
			return new File("./assets/lang/en/" + file);
		}

		@Override
		public File getPackFolder() {
			return new File("./packs");
		}

		@Override
		public File getWorkspaceFile(String relativePath) {
			return new File("./workspace/" + relativePath);
		}

		@Override
		public void initProfile() {
			AssetLoader.load();
			//UserProfile.getBCData().load(); TODO
			// UserProfile.loadPacks(); TODO
		}

		@Override
		public void noticeErr(Exception e, ErrType t, String str) {
			printErr(t, str);
			e.printStackTrace(t == ErrType.INFO ? System.out : System.err);
		}

		@Override
		public boolean preload(FileDesc desc) {
			return Admin.preload(desc);
		}

		@Override
		public void printErr(ErrType t, String str) {
			(t == ErrType.INFO ? System.out : System.err).println(str);
		}

	}

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.FIELD })
	public static @interface StaticPermitted {

		public static enum Type {
			FINAL, ENV, TEMP
		}

		Type value() default Type.FINAL;

	}

	private static final String[] ANIMFL = { ".imgcut", ".mamodel", ".maanim" };

	private static final String[] NONPRE = { "\\./org/img/../.....\\.png", "\\./org/enemy/.../..._.\\.png",
			"\\./org/unit/..././..._.\\.png", "\\./org/unit/..././udi..._.\\.png" };

	public static void main(String[] args) {
		UserProfile.profile();
		CommonStatic.def = new UtilPC.PCItr();
		CommonStatic.ctx = new AdminContext();
		CommonStatic.ctx.initProfile();
		// AssetLoader.merge();
		// searchForStaticFields();
	}

	public static List<ZipDesc> read() throws Exception {
		File folder = new File("./assets/assets/");
		List<ZipDesc> list = new ArrayList<>();
		for (File f : folder.listFiles()) {
			if (f.getName().endsWith(".asset.bcuzip")) {
				ZipDesc zd = PackLoader.readPack(Admin::preload, f);
				list.add(zd);
			}
		}
		return list;
	}

	public static void searchForStaticFields() throws ClassNotFoundException {
		File f = new File("./src/common");
		Queue<File> qfile = new ArrayDeque<>();
		Queue<Class<?>> qcls = new ArrayDeque<>();
		qfile.add(f);
		while (qfile.size() > 0) {
			File fi = qfile.poll();
			if (fi.isDirectory())
				for (File fx : fi.listFiles())
					qfile.add(fx);
			else if (fi.getName().endsWith(".java")) {
				String str = fi.toString();
				str = str.substring(6, str.length() - 5);
				str = str.replaceAll("/", ".");
				qcls.add(Class.forName(str));
			}
		}
		List<Class<?>> clist = new ArrayList<>();
		while (qcls.size() > 0) {
			Class<?> cls = qcls.poll();
			clist.add(cls);
			for (Class<?> ci : cls.getDeclaredClasses())
				qcls.add(ci);
		}
		List<Field> flist = new ArrayList<>();
		for (Class<?> cls : clist) {
			if (cls.isEnum())
				continue;
			if (cls.getAnnotation(StaticPermitted.class) != null)
				continue;
			for (Field field : cls.getDeclaredFields()) {
				if ((field.getModifiers() & Modifier.STATIC) == 0)
					continue;
				if ((field.getModifiers() & Modifier.FINAL) != 0) {
					if (field.getType().isPrimitive())
						continue;
					if (field.getType() == String.class)
						continue;
				}
				if (field.getAnnotation(StaticPermitted.class) != null)
					continue;
				flist.add(field);
			}
		}
		for (Field field : flist) {
			System.out.println(field);
		}
	}

	public static void write() throws Exception {
		for (int i = 1; i <= 9; i++) {
			File f = new File("./assets/" + Data.hex(i));
			File dst = new File("./assets/assets/" + Data.hex(i) + ".asset.bcuzip");
			Context.check(dst);
			PackDesc pd = new PackDesc("asset_" + Data.hex(i));
			pd.author = "PONOS";
			pd.BCU_VERSION = "0.5.0.0";
			pd.desc = "default required asset " + Data.hex(i);
			PackLoader.writePack(dst, f, pd, "battlecatsultimate");
		}
	}

	private static boolean preload(FileDesc fd) {
		for (String str : ANIMFL)
			if (fd.path.endsWith(str))
				return false;
		for (String str : NONPRE)
			if (fd.path.length() == str.length() - 2 && fd.path.matches(str))
				return false;
		return true;
	}

}
