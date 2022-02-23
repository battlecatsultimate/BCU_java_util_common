package common.util.pack.bgeffect;

import common.CommonStatic;
import common.io.json.JsonClass;
import common.pack.Context;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.system.files.VFile;
import common.util.anim.ImgCut;
import common.util.pack.Background;

import java.util.HashMap;
import java.util.Map;

@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public abstract class BackgroundEffect {
    public static Map<Integer, MixedBGEffect> mixture = new HashMap<>();
    public static int BGHeight = 512;
    public static final int battleOffset = (int) (400 / CommonStatic.BattleConst.ratio);
    public static final int[] jsonList = {102, 103, 110, 117, 121, 128, 132, 137, 141, 142, 145, 148, 153, 154, 155, 157, 158, 159, 164, 166, 172, 173, 174, 180, 1000, 1002, 1003, 1004, 1005, 1006, 1007, 1008};

    public static void read() {
        CommonStatic.BCAuxAssets asset = CommonStatic.getBCAssets();

        asset.bgEffects.add(new StarBackgroundEffect());

        VFile rainFile = VFile.get("./org/battle/a/000_a.png");

        FakeImage rainImage = rainFile.getData().getImg();
        ImgCut rainCut = ImgCut.newIns("./org/battle/a/000_a.imgcut");

        FakeImage[] images = rainCut.cut(rainImage);

        asset.bgEffects.add(new RainBGEffect(images[29], images[28]));

        VFile bubbleFile = VFile.get("./org/img/bgEffect/bubble02.png");

        FakeImage bubbleImage = bubbleFile.getData().getImg();

        asset.bgEffects.add(new BubbleBGEffect(bubbleImage));

        VFile secondBubbleFile = VFile.get("./org/img/bgEffect/bubble03_bg040.png");

        FakeImage secondBubbleImage = secondBubbleFile.getData().getImg();

        asset.bgEffects.add(new FallingSnowBGEffect(secondBubbleImage));

        VFile snowFile = VFile.get("./org/img/bgEffect/img021.png");

        FakeImage snowImage = snowFile.getData().getImg();

        asset.bgEffects.add(new SnowBGEffect(snowImage));

        asset.bgEffects.add(new SnowStarBGEffect());

        asset.bgEffects.add(new BlizzardBGEffect(secondBubbleImage));

        asset.bgEffects.add(new ShiningBGEffect());

        asset.bgEffects.add(new BalloonBGEffect());

        asset.bgEffects.add(new RockBGEffect());

        CommonStatic.ctx.noticeErr(() -> {
            int currentSize = asset.bgEffects.size();

            for(int i = 0; i < jsonList.length; i++) {
                asset.bgEffects.add(new JsonBGEffect(jsonList[i]));

                UserProfile.getBCData().bgs.getRaw(jsonList[i]).effect = currentSize;

                currentSize++;
            }

            for(int i = 0; i < UserProfile.getBCData().bgs.size(); i++) {
                Background bg = UserProfile.getBCData().bgs.get(i);

                if(bg.reference != -1) {
                    Background ref = UserProfile.getBCData().bgs.get(bg.reference);

                    if(bg.effect == -1)
                        bg.effect = ref.effect;
                    else if(bg.effect >= 0 && ref.effect != -1) {
                        if(ref.effect >= 0) {
                            mixture.put(bg.id.id, new MixedBGEffect(asset.bgEffects.get(bg.effect), asset.bgEffects.get(ref.effect)));

                            bg.effect = -bg.id.id;
                        } else if(ref.effect == -ref.id.id && mixture.containsKey(ref.id.id)) {
                            mixture.put(bg.id.id, new MixedBGEffect(asset.bgEffects.get(bg.effect), mixture.get(ref.id.id)));

                            bg.effect = -bg.id.id;
                        } else if(ref.id.id != -ref.id.id || !mixture.containsKey(ref.id.id)) {
                            System.out.println("W/BackgroundEffect::read - Unhandled situation for background effect mixing -> Reference BG ID : "+ref.id.id+" | Mixture contains key : "+mixture.containsKey(ref.id.id));
                        }
                    }
                }
            }
        }, Context.ErrType.FATAL, "Failed to read bg effect data");
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
     * @param w Width of battlefield as P
     * @param h Height of battlefield as Px
     */
    public abstract void update(int w, double h, double midH);

    /**
     * Initialize data here
     * @param w Width of battlefield as P
     * @param h Height of battlefield as Px
     */
    public abstract void initialize(int w, double h, double midH, Background bg);

    public void release() {

    }

    /**
     * Convert battle unit to pixel unit
     * @param p Position in battle
     * @param siz Size of battle
     * @return Converted pixel
     */
    protected int convertP(double p, double siz) {
        return (int) (p * CommonStatic.BattleConst.ratio * siz);
    }

    protected int revertP(double px) {
        return (int) (px / CommonStatic.BattleConst.ratio);
    }
}
