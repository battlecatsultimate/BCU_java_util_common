package common.util.lang;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import common.CommonStatic;
import common.io.assets.Admin.StaticPermitted;
import common.util.anim.AnimI;
import common.util.anim.AnimU;
import common.util.anim.AnimU.UType;
import common.util.pack.Background.BGWvType;
import common.util.pack.EffAnim;
import common.util.pack.NyCastle.NyType;
import common.util.pack.WaveAnim.WaveType;

public class AnimTypeLocale {

	@StaticPermitted
	public static final Set<AnimI.AnimType<?, ?>> TYPES = new HashSet<>();

	static {
		Collections.addAll(TYPES, BGWvType.values());
		Collections.addAll(TYPES, NyType.values());
		Collections.addAll(TYPES, UType.SOUL);
		Collections.addAll(TYPES, UType.values());
		Collections.addAll(TYPES, WaveType.values());
		Collections.addAll(TYPES, EffAnim.ArmorEff.values());
		Collections.addAll(TYPES, EffAnim.BarrierEff.values());
		Collections.addAll(TYPES, EffAnim.DefEff.values());
		Collections.addAll(TYPES, EffAnim.KBEff.values());
		Collections.addAll(TYPES, EffAnim.SniperEff.values());
		Collections.addAll(TYPES, EffAnim.SpeedEff.values());
		Collections.addAll(TYPES, EffAnim.VolcEff.values());
		Collections.addAll(TYPES, EffAnim.WarpEff.values());
		Collections.addAll(TYPES, EffAnim.WeakUpEff.values());
		Collections.addAll(TYPES, EffAnim.ZombieEff.values());
		Collections.addAll(TYPES, EffAnim.ShieldEff.values());
		Collections.addAll(TYPES, EffAnim.DmgCap.values());
	}

	public static void read() {
		String loc = CommonStatic.Lang.LOC_CODE[CommonStatic.getConfig().lang];
		InputStream f;
		if (CommonStatic.getConfig().lang == 3)
			f = CommonStatic.ctx.getLangFile("animation_type_jp.json");
		else
			f = CommonStatic.ctx.getLangFile("animation_type.json");
		JsonElement je = JsonParser.parseReader(new InputStreamReader(f, StandardCharsets.UTF_8));
		for (AnimI.AnimType<?, ?> type : TYPES) {
			JsonObject obj = je.getAsJsonObject().get(type.getClass().getSimpleName()).getAsJsonObject();
			String val = obj.get(type.toString()).getAsString();
			MultiLangCont.getStatic().ANIMNAME.put(loc, type, val);
		}
	}

}
