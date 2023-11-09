package common.util.pack.bgeffect;

import common.CommonStatic;
import common.system.P;
import common.system.VImg;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.Data;
import common.util.pack.Background;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class BlizzardBGEffect extends BackgroundEffect {
    private final VImg blizzard;

    private final int bw;
    private final int bh;

    private final List<P> blizzardPosition = new ArrayList<>();
    private final List<P> initPos = new ArrayList<>();
    private final List<Float> slope = new ArrayList<>();
    private final List<Float> angle = new ArrayList<>();
    private final List<Byte> size = new ArrayList<>();
    private final List<Float> speed = new ArrayList<>();
    private final Random r = new Random();

    private final List<Integer> capture = new ArrayList<>();

    public BlizzardBGEffect(VImg blizzard) {
        this.blizzard = blizzard;

        bw = this.blizzard.getImg().getWidth();
        bh = this.blizzard.getImg().getHeight();
    }

    @Override
    public void check() {
        blizzard.check();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, float siz, float midH) {

    }

    @Override
    public void postDraw(FakeGraphics g, P rect, float siz, float midH) {
        FakeTransform at = g.getTransform();

        for(int i = 0; i < blizzardPosition.size(); i++) {
            g.translate(convertP(blizzardPosition.get(i).x, siz) + rect.x, blizzardPosition.get(i).y * siz - rect.y + midH * siz);
            g.rotate(-angle.get(i));

            g.drawImage(blizzard.getImg(), 0, 0, (int) (bw * 0.5 * Data.BG_EFFECT_BLIZZARD_SIZE[size.get(i)] * siz), (int) (bh * Data.BG_EFFECT_BLIZZARD_SIZE[size.get(i)] * siz));

            g.setTransform(at);
        }

        g.delete(at);
    }

    @Override
    public void update(int w, float h, float midH) {
        capture.clear();

        for(int i = 0; i < blizzardPosition.size(); i++) {
            if(blizzardPosition.get(i).x >= w + battleOffset || blizzardPosition.get(i).y >= BGHeight * 3 + bh * Data.BG_EFFECT_BLIZZARD_SIZE[size.get(i)]) {
                capture.add(i);
            } else {
                blizzardPosition.get(i).y += speed.get(i);
                blizzardPosition.get(i).x = revertP(slope.get(i) * (blizzardPosition.get(i).y - initPos.get(i).y)) + initPos.get(i).x;
            }
        }

        if(!capture.isEmpty()) {
            for(int i = 0; i < capture.size(); i++) {
                byte sizeIndex = (byte) Math.min(Data.BG_EFFECT_BLIZZARD_SIZE.length - 1, r.nextInt(Data.BG_EFFECT_BLIZZARD_SIZE.length));

                float x = r.nextFloat() * (w + battleOffset + bw * Data.BG_EFFECT_BLIZZARD_SIZE[sizeIndex] + revertP(BGHeight * 3)) - revertP(BGHeight * 3);
                float y = -bh * Data.BG_EFFECT_BLIZZARD_SIZE[sizeIndex];

                blizzardPosition.get(capture.get(i)).x = x;
                blizzardPosition.get(capture.get(i)).y = y;

                initPos.get(capture.get(i)).x = x;
                initPos.get(capture.get(i)).y = y;

                if (CommonStatic.getConfig().performanceModeBattle) {
                    speed.set(capture.get(i), (Data.BG_EFFECT_BLIZZARD_SPEED - r.nextInt(5)) / 2f);
                } else {
                    speed.set(capture.get(i), (Data.BG_EFFECT_BLIZZARD_SPEED - r.nextInt(5)) * 1f);
                }

                float a = (float) (Math.toRadians(60 - r.nextInt(15)));

                angle.set(capture.get(i), a);
                slope.set(capture.get(i), (float) Math.tan(a));
                size.set(capture.get(i), sizeIndex);
            }
        }
    }

    @Override
    public void initialize(int w, float h, float midH, Background bg) {
        for(int i = 0; i < blizzardPosition.size(); i++) {
            P.delete(blizzardPosition.get(i));
            P.delete(initPos.get(i));
        }

        blizzardPosition.clear();
        initPos.clear();
        speed.clear();
        slope.clear();
        size.clear();

        int number = w / 50;

        for(int i = 0; i < number; i++) {
            byte sizeIndex = (byte) Math.min(Data.BG_EFFECT_BLIZZARD_SIZE.length - 1, r.nextInt(Data.BG_EFFECT_BLIZZARD_SIZE.length));

            float x = r.nextFloat() * (w + battleOffset + bw * Data.BG_EFFECT_BLIZZARD_SIZE[sizeIndex] + revertP(BGHeight * 3)) - revertP(BGHeight * 3);
            float y = r.nextFloat() * (BGHeight * 3 + bh * Data.BG_EFFECT_BLIZZARD_SIZE[sizeIndex]);

            blizzardPosition.add(P.newP(x, y));
            initPos.add(P.newP(x, y));

            if (CommonStatic.getConfig().performanceModeBattle) {
                speed.add((Data.BG_EFFECT_BLIZZARD_SPEED - r.nextInt(5)) / 2f);
            } else {
                speed.add((Data.BG_EFFECT_BLIZZARD_SPEED - r.nextInt(5)) * 1f);
            }

            float a = (float) (Math.toRadians(60 - r.nextInt(15)));

            angle.add(a);
            slope.add((float) (Math.tan(a)));
            size.add(sizeIndex);
        }
    }
}
