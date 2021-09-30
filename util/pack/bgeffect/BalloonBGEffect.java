package common.util.pack.bgeffect;

import common.battle.StageBasis;
import common.pack.Identifier;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.util.Data;

import java.util.ArrayList;
import java.util.List;

public class BalloonBGEffect extends BackgroundEffect {
    private FakeImage balloon;
    private FakeImage balloon2;

    private final List<P> balloonPosition = new ArrayList<>();
    private final List<Boolean> isBalloon2 = new ArrayList<>();


    @Override
    public void check() {
        if(balloon != null)
            balloon.bimg();

        if(balloon2 != null)
            balloon2.bimg();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz, double midH) {

    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz, double midH) {

    }

    @Override
    public void update(StageBasis sb) {

    }

    @Override
    public void initialize(StageBasis sb) {
        //TODO Finish Balloon Effect
        balloon = null;
        balloon2 = null;

        if(!sb.bg.id.pack.equals(Identifier.DEF) || sb.bg.effect != Data.BG_EFFECT_BALLOON) {
            return;
        }

        sb.bg.load();

        balloon = sb.bg.parts[9];

        int number = sb.st.len / 1000;

        for(int i = 0; i < number; i++) {

        }
    }
}
