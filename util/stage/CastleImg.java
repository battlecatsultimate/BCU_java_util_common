package common.util.stage;

import common.io.json.JsonClass;
import common.io.json.JsonDecoder.OnInjected;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.Source;
import common.pack.UserProfile;
import common.system.VImg;
import common.system.files.VFile;

import java.util.Queue;

@IndexCont(CastleList.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class CastleImg implements Indexable<CastleList, CastleImg> {

	@JsonClass.JCIdentifier
	@JsonField
	public Identifier<CastleImg> id;
	@JsonField
	public double boss_spawn = 0;
	public VImg img;

	public static void getBossSpawns() {
		Queue<String> legendData = VFile.readLine("./org/data/enemyCastleDataLegend.csv");
		for (int i = 0; i < legendData.size(); i++) {
			String[] str = legendData.poll().split(",");
			int y = Integer.parseInt(str[0]);
			int z = Integer.parseInt(str[2]);
			CastleImg castle = CastleList.getList("000000").get(i);
			if (castle != null)
				castle.boss_spawn = (3314 + Math.floor(z * y / 10.0)) / 4;
		}
		Queue<String> empireData = VFile.readLine("./org/data/enemyCastleData0.csv");
		for (int i = 0; i < empireData.size(); i++) {
			String[] str = empireData.poll().split(",");
			int y = Integer.parseInt(str[0]);
			int z = Integer.parseInt(str[2]);
			CastleImg castle = CastleList.getList("000001").get(i);
			if (castle != null)
				castle.boss_spawn = (int) (3314 + Math.floor(z * y / 10.0)) / 4;
		}
		Queue<String> futureData = VFile.readLine("./org/data/enemyCastleData1.csv");
		for (int i = 0; i < futureData.size(); i++) {
			String[] str = futureData.poll().split(",");
			int y = Integer.parseInt(str[0]);
			int z = Integer.parseInt(str[2]);
			CastleImg castle = CastleList.getList("000002").get(i);
			if (castle != null)
				castle.boss_spawn = (int) (3314 + Math.floor(z * y / 10.0)) / 4;
		}
		Queue<String> cosmoData = VFile.readLine("./org/data/enemyCastleData1.csv");
		for (int i = 0; i < cosmoData.size(); i++) {
			String[] str = cosmoData.poll().split(",");
			int y = Integer.parseInt(str[0]);
			int z = Integer.parseInt(str[2]);
			CastleImg castle = CastleList.getList("000003").get(i);
			if (castle != null)
				castle.boss_spawn = (int) (3314 + Math.floor(z * y / 10.0)) / 4;
		}
	}

	public CastleImg() {
	}

	public CastleImg(Identifier<CastleImg> id, VImg img) {
		this.id = id;
		this.img = img;
	}

	@Override
	public Identifier<CastleImg> getID() {
		return id;
	}

	@OnInjected
	public void onInjected() {
		img = UserProfile.getUserPack(id.pack).source.readImage(Source.CASTLE, id.id);
	}

	@Override
	public String toString() {
		if(id == null)
			return super.toString();

		return id.toString();
	}
}
