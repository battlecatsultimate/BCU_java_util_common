package common.util.pack;

import common.battle.attack.AtkModelAb;
import common.battle.data.AtkDataModel;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.util.Animable;
import common.util.anim.*;
import common.util.stage.Music;
import org.jetbrains.annotations.NotNull;

@JsonClass
@IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
public class Soul extends Animable<AnimU<?>, AnimU.UType> implements Comparable<Soul>, Indexable<PackData, Soul> {

	@JsonClass.JCIdentifier
	@JsonField
	private final Identifier<Soul> id;

	@JsonField(gen = JsonField.GenType.GEN, usePool = true)
	public AtkDataModel[] atks = new AtkDataModel[0];

	@JsonField
	public Identifier<Music> audio;

	@JsonClass.JCConstructor
	public Soul() {
		id = null;
	}

	public Soul(Identifier<Soul> id, AnimU<?> animS) {
		anim = animS;
		this.id = id;
	}

	@Override
	public Identifier<Soul> getID() {
		return id;
	}

	@Override
	public String toString() {
		return "soul " + id.id;
	}

	@Override
	public int compareTo(Soul o) {
		return id.compareTo(o.id);
	}

	@Override
	public EAnimI getEAnim(AnimU.UType uType) {
		return anim.getEAnim(uType);
	}
}
