package common.util.pack.bgeffect;

import common.battle.StageBasis;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.util.Data;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class BubbleBGEffect extends BackgroundEffect {
    private final FakeImage bubble;

    private final int bw;
    private final int bh;

    private final List<P> bubblePosition = new ArrayList<>();
    private final List<Byte> differentiator = new ArrayList<>();

    private final List<Integer> capture = new ArrayList<>();

    public BubbleBGEffect(FakeImage bubble) {
        this.bubble = bubble;

        bw = (int) (this.bubble.getWidth() * 1.8);
        bh = (int) (this.bubble.getHeight() * 1.8);
    }

    @Override
    public void check() {
        bubble.bimg();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz, double midH) {

    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz, double midH) {
        for(int i = 0; i < bubblePosition.size(); i++) {
            g.drawImage(
                    bubble,
                    convertP(bubblePosition.get(i).x + Data.BG_EFFECT_BUBBLE_FACTOR * Math.sin(differentiator.get(i) + bubblePosition.get(i).y / Data.BG_EFFECT_BUBBLE_STABILIZER), siz) + (int) rect.x,
                    (int) (bubblePosition.get(i).y * siz - rect.y + midH * siz),
                    bw * siz, bh * siz
            );
        }
    }

    @Override
    public void update(StageBasis sb) {
        capture.clear();

        for(int i = 0; i < bubblePosition.size(); i++) {
            if(bubblePosition.get(i).y < -bh) {
                capture.add(i);
            } else {
                bubblePosition.get(i).y -= (1530.0 + bh) / Data.BG_EFFECT_BUBBLE_TIME;
            }
        }

        if(!capture.isEmpty()) {
            for(int i = 0; i < capture.size(); i++) {
                P.delete(bubblePosition.get(capture.get(i)));

                bubblePosition.set(capture.get(i), P.newP(sb.r.nextDouble() * (sb.st.len + battleOffset), 1530));
                differentiator.set(capture.get(i), (byte) (3 - sb.r.nextDouble() * 6));
            }
        }
    }

    @Override
    public void initialize(StageBasis sb) {
        bubblePosition.clear();
        differentiator.clear();

        int number = sb.st.len / 200 - (int) (sb.r.nextDouble() * sb.st.len / 1000);

        for(int i = 0; i < number; i++) {
            bubblePosition.add(P.newP(sb.r.nextDouble() * (sb.st.len + battleOffset), (1530.0 + bh) * sb.r.nextDouble()));
            differentiator.add((byte) (3 - sb.r.nextDouble() * 6));
        }
    }
}
