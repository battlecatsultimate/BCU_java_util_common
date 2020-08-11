package common.pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import common.io.PackLoader;
import common.io.PackLoader.ZipDesc;
import common.io.PackLoader.ZipDesc.FileDesc;
import common.pack.Context.ErrType;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.fake.ImageBuilder;
import common.system.files.FDFile;
import common.system.files.FileData;
import common.util.anim.AnimCE;
import common.util.anim.AnimCI;
import common.util.anim.ImgCut;
import common.util.anim.MaAnim;
import common.util.anim.MaModel;

public abstract class Source {

	public static interface AnimLoader {
		public VImg getEdi();

		public ImgCut getIC();

		public MaAnim[] getMA();

		public MaModel getMM();

		public ResourceLocation getName();

		public FakeImage getNum();

		public int getStatus();

		public VImg getUni();
	}

	public static class ResourceLocation {

		public static final String LOCAL = "_local";
		public String pack;
		public String id;

		public ResourceLocation(String pack, String id) {
			this.pack = pack;
			this.id = id;
		}

	}

	public static class SourceAnimLoader implements Source.AnimLoader {

		public static interface SourceLoader {

			public FileData loadFile(ResourceLocation id, String str);

		}

		public static final String IC = "imgcut.txt";
		public static final String MM = "mamodel.txt";
		public static final String[] MA = { "maanim_walk.txt", "maanim_idle.txt", "maanim_attack.txt", "maanim_kb.txt",
				"maanim_burrow_down.txt", "maanim_burrow_move.txt", "maanim_burrow_up.txt" };
		public static final String SP = "sprite.png";
		public static final String EDI = "icon_display.png";
		public static final String UNI = "icon_deploy.png";

		private final ResourceLocation id;
		private final SourceLoader loader;

		public SourceAnimLoader(ResourceLocation id, SourceLoader loader) {
			this.id = id;
			this.loader = loader == null ? Workspace::loadAnimFile : loader;
		}

		@Override
		public VImg getEdi() {
			FileData edi = loader.loadFile(id, EDI);
			if (edi == null)
				return null;
			return ctx.noticeErr(() -> new VImg(FakeImage.read(edi)), ErrType.ERROR,
					"failed to read Display Icon" + id);
		}

		@Override
		public ImgCut getIC() {
			return ImgCut.newIns(loader.loadFile(id, IC));
		}

		@Override
		public MaAnim[] getMA() {
			MaAnim[] ans = new MaAnim[MA.length];
			for (int i = 0; i < MA.length; i++)
				ans[i] = MaAnim.newIns(loader.loadFile(id, MA[i]));
			return ans;
		}

		@Override
		public MaModel getMM() {
			return MaModel.newIns(loader.loadFile(id, MM));
		}

		@Override
		public ResourceLocation getName() {
			return id;
		}

		@Override
		public FakeImage getNum() {
			return ctx.noticeErr(() -> FakeImage.read(loader.loadFile(id, SP)), ErrType.ERROR,
					"failed to read sprite sheet " + id);
		}

		@Override
		public int getStatus() {
			return id.pack.equals("_local") ? 0 : 1;
		}

		@Override
		public VImg getUni() {
			FileData uni = loader.loadFile(id, UNI);
			if (uni == null)
				return null;
			return ctx.noticeErr(() -> new VImg(FakeImage.read(uni)), ErrType.ERROR,
					"failed to read deploy icon " + id);
		}

	}

	public static class SourceAnimSaver {

		private final ResourceLocation id;
		private final AnimCI anim;

		public SourceAnimSaver(ResourceLocation name, AnimCI animCI) {
			this.id = name;
			this.anim = animCI;
		}

		public void delete() {
			ctx.noticeErr(() -> Context.delete(ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id)),
					ErrType.ERROR, "failed to delete animation: " + id);
		}

		public void saveAll() {
			saveData();
			saveImgs();
		}

		public void saveData() {
			try {
				write("imgcut.txt", anim.imgcut::write);
				write("mamodel.txt", anim.mamodel::write);
				write("maanim_walk.txt", anim.anims[0]::write);
				write("maanim_idle.txt", anim.anims[1]::write);
				write("maanim_attack.txt", anim.anims[2]::write);
				write("maanim_kb.txt", anim.anims[3]::write);
				write("maanim_burrow_down.txt", anim.anims[4]::write);
				write("maanim_burrow_move.txt", anim.anims[5]::write);
				write("maanim_burrow_up.txt", anim.anims[6]::write);
			} catch (IOException e) {
				ctx.noticeErr(e, ErrType.ERROR, "Error during saving animation data: " + anim);
			}
		}

		public void saveIconDeploy() {
			if (anim.getUni() != null)
				ctx.noticeErr(() -> write("icon_deploy.png", anim.getUni().getImg()), ErrType.ERROR,
						"Error during saving deploy icon: " + id);
		}

		public void saveIconDisplay() {
			if (anim.getEdi() != null)
				ctx.noticeErr(() -> write("icon_display.png", anim.getEdi().getImg()), ErrType.ERROR,
						"Error during saving display icon: " + id);
		}

		public void saveImgs() {
			saveSprite();
			saveIconDisplay();
			saveIconDeploy();
		}

		public void saveSprite() {
			ctx.noticeErr(() -> write("sprite.png", anim.getNum()), ErrType.ERROR,
					"Error during saving sprite sheet: " + id);
		}

		private void write(String type, Consumer<PrintStream> con) throws IOException {
			File f = ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + type);
			Context.check(f);
			PrintStream ps = new PrintStream(f);
			con.accept(ps);
			ps.close();
		}

		private void write(String type, FakeImage img) throws IOException {
			File f = ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + type);
			Context.check(f);
			Context.check(FakeImage.write(img, "PNG", f), "save", f);
		}

	}

	public static class Workspace extends Source {
		
		public static ResourceLocation validate(ResourceLocation rl) {
			//FIXME find valid path
			return rl;
		}

		public static List<AnimCE> loadAnimations(String id) {
			if (id == null)
				id = ResourceLocation.LOCAL;
			File folder = ctx.getWorkspaceFile("./" + id + "/animations/");
			List<AnimCE> list = new ArrayList<>();
			if (!folder.exists() || !folder.isDirectory())
				return list;
			for (File f : folder.listFiles()) {
				String path = "./" + id + "/animations/" + f.getName() + "/sprite.png";
				if (f.isDirectory() && ctx.getWorkspaceFile(path).exists())
					list.add(new AnimCE(new ResourceLocation(id, f.getName())));
			}
			return list;
		}

		private static FileData loadAnimFile(ResourceLocation id, String str) {
			return new FDFile(ctx.getWorkspaceFile("./" + id.pack + "/animations/" + id.id + "/" + str));
		}

		public Workspace(String id) {
			super(id);
		}

		@Override
		public AnimCI loadAnimation(String name) {
			return new AnimCI(new SourceAnimLoader(new ResourceLocation(id, name), null));
		}

		@Override
		public FakeImage readImage(String path) throws IOException {
			return ImageBuilder.builder.build(getFile(path));
		}

		@Override
		public InputStream streamFile(String path) throws IOException {
			return new FileInputStream(getFile(path));
		}

		public OutputStream writeFile(String path) throws IOException {
			File f = getFile(path);
			Context.check(f);
			return new FileOutputStream(f);
		}

		private File getFile(String path) {
			return ctx.getWorkspaceFile("./" + id + "/" + path);
		}

	}

	public static class ZipSource extends Source {

		private final ZipDesc zip;

		public ZipSource(ZipDesc desc) {
			super(desc.desc.id);
			zip = desc;
		}

		@Override
		public AnimCI loadAnimation(String name) {
			return new AnimCI(new SourceAnimLoader(new ResourceLocation(id, name), this::loadAnimationFile));
		}

		@Override
		public FakeImage readImage(String path) throws Exception {
			return zip.tree.find(path).getData().getImg();
		}

		@Override
		public InputStream streamFile(String path) throws Exception {
			return zip.readFile(path);
		}

		public Workspace unzip(Context ctx, String password) throws Exception {
			if (!zip.match(PackLoader.getMD5(password.getBytes(), 16)))
				return null;
			File f = ctx.getWorkspaceFile("./" + id + "/main.pack.json");
			File folder = f.getParentFile();
			if (folder.exists()) {
				if (!ctx.confirmDelete())
					return null;
				Context.delete(f);
			}
			Context.check(folder.mkdirs(), "create", folder);
			Context.check(f.createNewFile(), "create", f);
			Workspace ans = new Workspace(id);
			zip.unzip(id -> {
				File file = ans.getFile(id);
				Context.check(file.createNewFile(), "create", file);
				return file;
			});
			return ans;
		}

		private FileDesc loadAnimationFile(ResourceLocation id, String path) {
			return zip.tree.find("./animations/" + id.id + "/" + path).getData();
		}

	}

	// FIXME static variable
	public static Context ctx;

	public final String id;

	public Source(String id) {
		this.id = id;
	}

	public abstract AnimCI loadAnimation(String name);

	/** read images from file. Use it */
	public abstract FakeImage readImage(String path) throws Exception;

	/** used for streaming music. Do not use it for images and small text files */
	public abstract InputStream streamFile(String path) throws Exception;

}
