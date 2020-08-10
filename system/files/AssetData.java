package common.system.files;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import common.CommonStatic.Lang;
import common.system.MultiLangCont;
import common.system.MultiLangFile;
import common.system.fake.FakeImage;

public interface AssetData extends FileData {

	public static AssetData getAsset(byte[] bs) {
		return new DefAsset(bs);
	}

	public static AssetData getAsset(File f) {
		return new FileAsset(f);
	}

	public static AssetData getAsset(VFile<AssetData> vf) {
		return new MultiLangAsset(vf);
	}

	@Override
	public byte[] getBytes();

	public FakeImage getImg(MultiLangFile mlf);

}

class DefAsset extends FileByte implements AssetData {

	protected DefAsset(byte[] bs) {
		super(bs);
	}

	@Override
	public FakeImage getImg(MultiLangFile mlf) {
		return getImg();
	}

}

class FileAsset extends FDFile implements AssetData {
	private final String name;

	protected FileAsset(File f) {
		super(f);
		name = f.getName();
	}

	@Override
	public FakeImage getImg(MultiLangFile mlf) {
		return getImg();
	}

	@Override
	public String toString() {
		return name;
	}
}

class MultiLangAsset extends Lang implements AssetData {

	private Map<String, AssetData> map = new TreeMap<>();

	public MultiLangAsset(VFile<AssetData> vf) {
		for (VFile<AssetData> f : vf.list())
			map.put(f.getName().substring(6), f.getData());
	}

	@Override
	public byte[] getBytes() {
		return getData().getBytes();
	}

	@Override
	public FakeImage getImg() {
		return getData().getImg();
	}

	@Override
	public FakeImage getImg(MultiLangFile mlf) {
		if (!MultiLangCont.VFILE.containsKey(mlf))
			MultiLangCont.VFILE.put(mlf, this);
		return getImg();
	}

	@Override
	public InputStream getStream() throws Exception {
		return getData().getStream();
	}

	@Override
	public Queue<String> readLine() {
		return getData().readLine();
	}

	private AssetData getData() {
		String loc = LOC_CODE[lang];
		AssetData ad = map.get(loc);
		if (ad == null)
			ad = map.values().iterator().next();
		return ad;
	}

}
