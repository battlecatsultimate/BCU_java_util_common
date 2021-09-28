package common.util.pack.bgeffect;

import common.CommonStatic;
import common.battle.StageBasis;
import common.io.json.JsonClass;
import common.pack.Identifier;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.system.files.VFile;
import common.util.anim.ImgCut;

@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public abstract class BackgroundEffect {
    protected static final int battleOffset = (int) (400 / CommonStatic.BattleConst.ratio);

    public static void read() {
        CommonStatic.BCAuxAssets asset = CommonStatic.getBCAssets();

        asset.bgEffects.add(new StarBackgroundEffect());

        VFile rainFile = VFile.get("./org/battle/a/000_a.png");

        if(rainFile != null) {
            FakeImage rainImage = rainFile.getData().getImg();
            ImgCut rainCut = ImgCut.newIns("./org/battle/a/000_a.imgcut");

            FakeImage[] images = rainCut.cut(rainImage);

            asset.bgEffects.add(new RainBGEffect(images[29], images[28]));
        } else {
            throw new IllegalStateException("Can't find 000_a.png");
        }
    }

    /**
     * Load image or any data here
     */
    public abstract void check();

    /**
     * Effects which will be drawn behind entities
     * @param g Canvas
     * @param rect (x,y) coordinate of battle
     * @param siz size of battle
     * @param midH how battle will be shifted along y-axis
     */
    public abstract void preDraw(FakeGraphics g, P rect, final double siz, final double midH);

    /**
     * Effects which will be drawn in front of entities
     * @param g Canvas
     * @param rect (x,y) coordinate of battle
     * @param siz size of battle
     * @param midH how battle will be shifted along y-axis
     */
    public abstract void postDraw(FakeGraphics g, P rect, final double siz, final double midH);

    /**
     * Update data here
     * @param sb Stage data
     */
    public abstract void update(StageBasis sb);

    /**
     * Initialize data here
     * @param sb Stage data
     */
    public abstract void initialize(StageBasis sb);

    /**
     * Convert battle unit to pixel unit
     * @param p Position in battle
     * @param siz Size of battle
     * @return Converted pixel
     */
    protected int convertP(double p, double siz) {
        return (int) ((p * CommonStatic.BattleConst.ratio) * siz);
    }
}
