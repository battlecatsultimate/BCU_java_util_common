package common.util.pack.bgeffect;

import common.system.P;
import common.system.fake.FakeGraphics;
import common.util.pack.Background;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class MixedBGEffect extends BackgroundEffect {
    private final BackgroundEffect[] effects;

    public MixedBGEffect(BackgroundEffect... effects) {
        this.effects = effects;
    }

    @Override
    public void check() {
        for(int i = 0; i < effects.length; i++)
            effects[i].check();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, float siz, float midH) {
        for(int i = 0; i < effects.length; i++)
            effects[i].preDraw(g, rect, siz, midH);
    }

    @Override
    public void postDraw(FakeGraphics g, P rect, float siz, float midH) {
        for(int i = 0; i < effects.length; i++)
            effects[i].postDraw(g, rect, siz, midH);
    }

    @Override
    public void update(int w, float h, float midH) {
        for(int i = 0; i < effects.length; i++)
            effects[i].update(w, h, midH);
    }

    @Override
    public void updateAnimation(int w, float h, float midH) {
        for(int i = 0; i < effects.length; i++)
            effects[i].updateAnimation(w, h, midH);
    }

    @Override
    public void initialize(int w, float h, float midH, Background bg) {
        for(int i = 0; i < effects.length; i++)
            effects[i].initialize(w, h, midH, bg);
    }
}
