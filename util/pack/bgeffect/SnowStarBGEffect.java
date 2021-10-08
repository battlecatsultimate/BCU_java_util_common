package common.util.pack.bgeffect;

import common.CommonStatic;
import common.battle.StageBasis;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.util.Data;

public class SnowStarBGEffect extends BackgroundEffect {
    @Override
    public void check() {
        CommonStatic.getBCAssets().bgEffects.get(Data.BG_EFFECT_STAR).check();
        CommonStatic.getBCAssets().bgEffects.get(Data.BG_EFFECT_SNOW).check();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz, double midH) {
        CommonStatic.getBCAssets().bgEffects.get(Data.BG_EFFECT_STAR).preDraw(g, rect, siz, midH);
    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz, double midH) {
        CommonStatic.getBCAssets().bgEffects.get(Data.BG_EFFECT_SNOW).postDraw(g, rect, siz, midH);
    }

    @Override
    public void update(StageBasis sb) {
        CommonStatic.getBCAssets().bgEffects.get(Data.BG_EFFECT_STAR).update(sb);
        CommonStatic.getBCAssets().bgEffects.get(Data.BG_EFFECT_SNOW).update(sb);
    }

    @Override
    public void initialize(StageBasis sb) {
        CommonStatic.getBCAssets().bgEffects.get(Data.BG_EFFECT_STAR).initialize(sb);
        CommonStatic.getBCAssets().bgEffects.get(Data.BG_EFFECT_SNOW).initialize(sb);
    }
}
