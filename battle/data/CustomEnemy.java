package common.battle.data;

import common.battle.Basis;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.util.pack.Soul;
import common.util.unit.AbEnemy;
import common.util.unit.Enemy;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

@JsonClass
public class CustomEnemy extends CustomEntity implements MaskEnemy {

	public Enemy pack;

	@JsonField
	public int star, drop;
	@JsonField
	public float limit;

	public CustomEnemy() {
		rep = new AtkDataModel(this);
		atks = new AtkDataModel[1];
		atks[0] = new AtkDataModel(this);
		width = 320;
		speed = 8;
		hp = 10000;
		hb = 1;
		traits = new ArrayList<>();
		traits.add(UserProfile.getBCData().traits.get(TRAIT_RED));
		death = new Identifier<>(Identifier.DEF, Soul.class, 0);
	}

	public CustomEnemy copy(Enemy e) {
		CustomEnemy ce = new CustomEnemy();
		ce.importData(this);
		ce.pack = e;

		return ce;
	}

	@Override
	public int getDrop() {
		return drop * 100;
	}

	@Override
	public Enemy getPack() {
		return pack;
	}

	@Override
	public int getStar() {
		return star;
	}

	@Override
	public Set<AbEnemy> getSummon() {
		Set<AbEnemy> ans = new TreeSet<>();
		if (common) {
			if (rep.proc.SUMMON.prob > 0 && (rep.proc.SUMMON.id == null || AbEnemy.class.isAssignableFrom(rep.proc.SUMMON.id.cls)))
				ans.add(Identifier.getOr(rep.proc.SUMMON.id, AbEnemy.class));
		} else
			for (AtkDataModel adm : atks)
				if (adm.proc.SUMMON.prob > 0 && (adm.proc.SUMMON.id == null || AbEnemy.class.isAssignableFrom(adm.proc.SUMMON.id.cls)))
					ans.add(Identifier.getOr(adm.proc.SUMMON.id, AbEnemy.class));
		return ans;
	}

	@Override
	public void importData(MaskEntity de) {
		super.importData(de);
		if (de instanceof MaskEnemy) {
			MaskEnemy me = (MaskEnemy) de;
			star = me.getStar();
			drop = me.getDrop() / 100;
			limit = me.getLimit();
		}
	}

	@Override
	public float multi(Basis b) {
		if (star > 0)
			return b.t().getStarMulti(star);
		if (traits.contains(UserProfile.getBCData().traits.get(TRAIT_ALIEN)))
			return b.t().getAlienMulti();
		return 1;
	}

	@Override
	public float getLimit() {
		return limit;
	}
}
