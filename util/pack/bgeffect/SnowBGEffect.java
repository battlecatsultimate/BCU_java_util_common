package common.util.pack.bgeffect;

import common.CommonStatic;
import common.system.P;
import common.system.VImg;
import common.system.fake.FakeGraphics;
import common.util.Data;
import common.util.pack.Background;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class SnowBGEffect extends BackgroundEffect{
    private final float maxSlope = (float) Math.tan(Math.toRadians(75));
    private final VImg snow;

    private final int sw;
    private final int sh;

    private final List<P> snowPosition = new ArrayList<>();
    private final List<P> initPos = new ArrayList<>();
    private final List<Float> speed = new ArrayList<>();
    private final List<Float> slope = new ArrayList<>();
    private final Random r = new Random();

    private final List<Integer> capture = new ArrayList<>();

    public SnowBGEffect(VImg snow) {
        this.snow = snow;

        this.sw = (int) (snow.getImg().getWidth() * 1.8);
        this.sh = (int) (snow.getImg().getHeight() * 1.8);
    }

    @Override
    public void check() {
        snow.check();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, float siz, float midH) {

    }

    @Override
    public void postDraw(FakeGraphics g, P rect, float siz, float midH) {
        g.setComposite(FakeGraphics.TRANS, 127, 0);

        for(int i = 0; i < snowPosition.size(); i++) {
            g.drawImage(snow.getImg(), convertP(snowPosition.get(i).x, siz) + (int) rect.x, (int) (snowPosition.get(i).y * siz - rect.y + midH * siz), sw * siz, sh * siz);
        }

        g.setComposite(FakeGraphics.DEF, 255, 0);
    }

    @Override
    public void update(int w, float h, float midH) {
        capture.clear();

        for(int i = 0; i < snowPosition.size(); i++) {
            if(snowPosition.get(i).y >= 1510 + sh || snowPosition.get(i).x < -sw || snowPosition.get(i).x >= w + battleOffset) {
                capture.add(i);
            } else {
                snowPosition.get(i).y += speed.get(i);
                //slope(y - initY) + initX = x
                snowPosition.get(i).x = revertP(slope.get(i) * (snowPosition.get(i).y - initPos.get(i).y)) + initPos.get(i).x;
            }
        }

        if(!capture.isEmpty()) {
            for(int i = 0; i < capture.size(); i++) {
                float x = r.nextInt(w + sw + battleOffset);
                float y = -sh;

                snowPosition.get(capture.get(i)).x = x;
                snowPosition.get(capture.get(i)).y = y;
                initPos.get(capture.get(i)).x = x;
                initPos.get(capture.get(i)).y = y;

                //0 ~ 75
                float angle = (float) Math.toRadians(r.nextInt(75));

                //-0.5angle + 1 is stabilizer
                if (CommonStatic.getConfig().performanceModeBattle) {
                    speed.set(capture.get(i), (float) ((Data.BG_EFFECT_SNOW_SPEED - r.nextInt(Data.BG_EFFECT_SNOW_SPEED - 3)) * (-0.75 * angle / maxSlope + 1)) / 2f);
                } else {
                    speed.set(capture.get(i), (float) ((Data.BG_EFFECT_SNOW_SPEED - r.nextInt(Data.BG_EFFECT_SNOW_SPEED - 3)) * (-0.75 * angle / maxSlope + 1)));
                }
                slope.set(capture.get(i), (float) Math.tan(-angle));
            }
        }
    }

    @Override
    public void initialize(int w, float h, float midH, Background bg) {
        for(int i = 0; i < snowPosition.size(); i++) {
            P.delete(snowPosition.get(i));
        }

        snowPosition.clear();

        int number = w / 200;

        for(int i = 0; i < number; i++) {
            float x = r.nextInt(w + sw + battleOffset);
            float y = r.nextInt(1510 + sh);
            snowPosition.add(P.newP(x, y));
            initPos.add(P.newP(x, y));

            //0~75
            float angle = (float) Math.toRadians(r.nextInt(75));

            //-0.5angle + 1 is stabilizer
            if (CommonStatic.getConfig().performanceModeBattle) {
                speed.add((float) ((Data.BG_EFFECT_SNOW_SPEED - r.nextInt(Data.BG_EFFECT_SNOW_SPEED - 3)) * (-0.75 * angle / maxSlope + 1)) / 2f);
            } else {
                speed.add((float) ((Data.BG_EFFECT_SNOW_SPEED - r.nextInt(Data.BG_EFFECT_SNOW_SPEED - 3)) * (-0.75 * angle / maxSlope + 1)));
            }

            slope.add((float) Math.tan(-angle));
        }
    }
}
