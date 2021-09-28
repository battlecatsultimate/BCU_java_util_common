package common.util.pack.bgeffect;

import common.CommonStatic;
import common.battle.StageBasis;
import common.io.json.JsonClass;
import common.pack.Identifier;
import common.system.P;
import common.system.fake.FakeGraphics;

@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public abstract class BackgroundEffect {
    public static void read() {
        CommonStatic.BCAuxAssets asset = CommonStatic.getBCAssets();

        asset.bgEffects.add(new StarBackgroundEffect());
    }

    public abstract void check();
    public abstract void preDraw(FakeGraphics g, P rect, final double siz);
    public abstract void postDraw(FakeGraphics g, P rect, final double siz);
    public abstract void update(StageBasis sb);
    public abstract void initialize(StageBasis sb);

    protected int convertP(double p, double siz) {
        return (int) ((p * CommonStatic.BattleConst.ratio) * siz);
    }
}
