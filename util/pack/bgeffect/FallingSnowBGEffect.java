package common.util.pack.bgeffect;

import common.CommonStatic;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.util.Data;
import common.util.pack.Background;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class FallingSnowBGEffect extends BackgroundEffect {
    private final FakeImage snow;

    private final int sw;
    private final int sh;

    private final List<P> snowPosition = new ArrayList<>();
    private final List<Double> speed = new ArrayList<>();
    private final List<Double> size = new ArrayList<>();
    private final Random r = new Random();

    private final List<Integer> capture = new ArrayList<>();

    public FallingSnowBGEffect(FakeImage snow) {
        this.snow = snow;

        sw = this.snow.getWidth();
        sh = this.snow.getHeight();
    }

    @Override
    public void check() {
        snow.bimg();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz, double midH) {

    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz, double midH) {
        for(int i = 0; i < snowPosition.size(); i++) {
            g.drawImage(snow, convertP(snowPosition.get(i).x, siz) + (int) rect.x, (int) (snowPosition.get(i).y * siz - rect.y + midH * siz), sw * size.get(i) * siz, sh * size.get(i) * siz);
        }
    }

    @Override
    public void update(int w, double h, double midH) {
        capture.clear();

        for(int i = 0; i < snowPosition.size(); i++) {
            if(snowPosition.get(i).y >= BGHeight * 3 + sh * size.get(i)) {
                capture.add(i);
            } else {
                snowPosition.get(i).y += speed.get(i);
            }
        }

        if(!capture.isEmpty()) {
            for(int i = 0; i < capture.size(); i++) {
                double siz = Data.BG_EFFECT_FALLING_SNOW_SIZE - r.nextDouble() * 1.5;

                snowPosition.get(capture.get(i)).x = r.nextDouble() * (w + battleOffset + sw * siz) - sw * siz;
                snowPosition.get(capture.get(i)).y = -sh * siz;

                if (CommonStatic.getConfig().performanceMode) {
                    speed.set(capture.get(i), (Data.BG_EFFECT_FALLING_SNOW_SPEED - r.nextDouble() * 1.5) / 2.0);
                } else {
                    speed.set(capture.get(i), Data.BG_EFFECT_FALLING_SNOW_SPEED - r.nextDouble() * 1.5);
                }

                size.set(capture.get(i), siz);
            }
        }
    }

    @Override
    public void initialize(int w, double h, double midH, Background bg) {
        for(int i = 0; i < snowPosition.size(); i++) {
            P.delete(snowPosition.get(i));
        }

        snowPosition.clear();
        speed.clear();
        size.clear();

        int number = w / 50;

        for(int i = 0; i < number; i++) {
            snowPosition.add(P.newP(r.nextInt(w + battleOffset + sw) - sw, r.nextInt(BGHeight * 3 + sh)));

            if (CommonStatic.getConfig().performanceMode) {
                speed.add((Data.BG_EFFECT_FALLING_SNOW_SPEED - r.nextDouble() * 1.5) / 2.0);
            } else {
                speed.add(Data.BG_EFFECT_FALLING_SNOW_SPEED - r.nextDouble() * 1.5);
            }

            size.add(Data.BG_EFFECT_FALLING_SNOW_SIZE - r.nextDouble() * 1.5);
        }
    }
}
