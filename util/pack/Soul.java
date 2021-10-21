package common.util.pack;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer.IndexCont;
import common.pack.IndexContainer.Indexable;
import common.pack.PackData;
import common.util.Animable;
import common.util.anim.*;
import org.jetbrains.annotations.NotNull;

@JsonClass.JCGeneric(Identifier.class)
@JsonClass
@IndexCont(PackData.class)
public class Soul extends AbSoul implements Comparable<Soul>, Indexable<PackData, Soul> {

	@JsonClass.JCIdentifier
	@JsonField
	private final Identifier<Soul> id;

	@JsonClass.JCConstructor
	public Soul() {
		super(null);
		id = null;
	}

	public Soul(Identifier<Soul> id, AnimU<?> animS) {
		super(animS);
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
}
