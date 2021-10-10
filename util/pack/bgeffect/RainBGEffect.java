package common.util.pack.bgeffect;

import common.battle.StageBasis;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.system.fake.FakeTransform;
import common.util.Data;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class RainBGEffect extends BackgroundEffect {
    private final FakeImage rain;
    private final FakeImage splash;

    private final int rw;
    private final int rh;
    private final int sw;
    private final int sh;

    private final List<P> rainPosition = new ArrayList<>();
    private final List<P> splashPosition = new ArrayList<>();

    public RainBGEffect(FakeImage rain, FakeImage splash) {
        this.rain = rain;
        this.splash = splash;

        rw = this.rain.getWidth();
        rh = this.rain.getHeight();

        sw = this.splash.getWidth();
        sh = this.splash.getHeight();
    }

    @Override
    public void check() {
        rain.bimg();
        splash.bimg();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz, double midH) {
        g.setComposite(FakeGraphics.TRANS, 96, 0);

        for(int i = 0; i < splashPosition.size(); i++) {
            g.drawImage(splash, convertP(splashPosition.get(i).x, siz) + (int) rect.x, (int) (splashPosition.get(i).y * siz - rect.y), sw * siz * 0.8, sh * siz * 0.8);
        }

        g.setComposite(FakeGraphics.DEF, 255, 0);
    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz, double midH) {
        g.setComposite(FakeGraphics.TRANS, 127, 0);

        FakeTransform at = g.getTransform();

        for(int i = 0; i < rainPosition.size(); i++) {
            //30 and 50 shifting is to draw image at center
            g.translate(convertP(rainPosition.get(i).x, siz) + (int) rect.x - 30 * siz * 0.8, (int) (rainPosition.get(i).y * siz - rect.y + midH * siz - 50 * siz * 0.8));
            g.rotate(Math.PI / 3);

            g.drawImage(rain, 0, 0, rw * siz * 0.8, rh * siz * 0.8);

            g.setTransform(at);
        }

        g.setTransform(at);
        g.delete(at);

        g.setComposite(FakeGraphics.DEF, 255, 0);
    }

    @Override
    public void update(StageBasis sb) {
        for(int i = 0; i < splashPosition.size(); i++) {
            P.delete(splashPosition.get(i));
        }

        for(int i = 0; i < rainPosition.size(); i++) {
            P.delete(rainPosition.get(i));
        }

        splashPosition.clear();
        rainPosition.clear();

        int rainNumber = sb.st.len / 100;
        int splashNumber = 2 * sb.st.len / 300;

        rainNumber += rainNumber / 6 - (int) (Math.random() * rainNumber / 3);
        splashNumber += splashNumber / 6 - (int) (Math.random() * splashNumber / 3);

        for(int i = 0; i < rainNumber; i++) {
            rainPosition.add(P.newP(Math.random() * (sb.st.len + battleOffset), Math.random() * BGHeight * 3));
        }

        for(int i = 0; i < splashNumber; i++) {
            //Y : BGHeight * 3 - 100 - random(0 ~ 80)
            splashPosition.add(P.newP(Math.random() * (sb.st.len + battleOffset), BGHeight * 3 - Data.BG_EFFECT_SPLASH_MIN_HEIGHT - Math.random() * Data.BG_EFFECT_SPLASH_RANGE));
        }
    }

    @Override
    public void initialize(StageBasis sb) {

    }
}
