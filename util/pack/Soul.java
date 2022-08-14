package common.util.pack;

import common.battle.data.AtkDataModel;
import common.battle.data.DataAtk;
import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.util.Animable;
import common.util.anim.*;
import common.util.stage.Music;

@JsonClass
@IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
public class Soul extends Animable<AnimU<?>, AnimU.UType> implements Indexable<PackData, Soul> {

	@JsonClass.JCIdentifier
	@JsonField
	private final Identifier<Soul> id;

	@JsonField
	public Identifier<Music> audio;

	@JsonField
	public String name;

//	@JsonField(gen = JsonField.GenType.GEN, usePool = true)
//	public AtkDataModel[] attacks;

	@JsonClass.JCConstructor
	public Soul() {
		id = null;
	}

	public Soul(Identifier<Soul> id, AnimU<?> animS) {
		anim = animS;
		this.id = id;

		if (id.pack.equals(Identifier.DEF))
			name = "soul " + id.id;
		else
			name = "custom soul " + id.id;
	}

	@Override
	public Identifier<Soul> getID() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public EAnimI getEAnim(AnimU.UType uType) {
		return anim.getEAnim(uType);
	}
}
