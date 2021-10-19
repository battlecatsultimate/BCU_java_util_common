package common.util.pack.bgeffect;

import common.pack.UserProfile;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.util.Data;
import common.util.pack.Background;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class ShiningBGEffect extends BackgroundEffect {
    private final FakeImage shine;

    private final int sw;
    private final int sh;

    private final List<P> shinePosition = new ArrayList<>();
    private final List<Byte> time = new ArrayList<>();
    private final Random r = new Random();

    private final List<Integer> capture = new ArrayList<>();

    public ShiningBGEffect() {
        Background bg = UserProfile.getBCData().bgs.get(55);

        bg.load();

        shine = bg.parts[20];

        sw = shine.getWidth();
        sh = shine.getHeight();
    }

    @Override
    public void check() {
        shine.bimg();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz, double midH) {

    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz, double midH) {
        g.setComposite(FakeGraphics.BLEND, 255, 1);

        for(int i = 0; i < shinePosition.size(); i++) {
            double size = Math.sin(Math.PI * time.get(i) / Data.BG_EFFECT_SHINING_TIME);

            g.drawImage(shine, convertP(shinePosition.get(i).x, siz) + (int) (rect.x - sw * size * siz / 2), (int) (shinePosition.get(i).y * siz - rect.y - sh * size * siz / 2), sw * size * siz, sh * size * siz);
        }

        g.setComposite(FakeGraphics.DEF, 255, 0);
    }

    @Override
    public void update(int w, double h, double midH) {
        capture.clear();

        for(int i = 0; i < shinePosition.size(); i++) {
            if(time.get(i) <= 0) {
                capture.add(i);
            } else {
                time.set(i, (byte) (time.get(i) - 1));
            }
        }

        if(!capture.isEmpty()) {
            for(int i = 0; i < capture.size(); i++) {
                shinePosition.get(capture.get(i)).x = r.nextInt(w + battleOffset);
                shinePosition.get(capture.get(i)).y = r.nextInt(BGHeight * 3 - BGHeight);
                time.set(capture.get(i), (byte) Data.BG_EFFECT_SHINING_TIME);
            }
        }
    }

    @Override
    public void initialize(int w, double h, double midH, Background bg) {
        for(int i = 0; i < shinePosition.size(); i++) {
            P.delete(shinePosition.get(i));
        }

        shinePosition.clear();
        time.clear();

        int number = w / 1600;

        for(int i = 0; i < number; i++) {
            shinePosition.add(P.newP(r.nextInt(w + battleOffset), r.nextInt(BGHeight * 3 - BGHeight)));
            time.add((byte) (r.nextInt(Data.BG_EFFECT_SHINING_TIME)));
        }
    }
}
