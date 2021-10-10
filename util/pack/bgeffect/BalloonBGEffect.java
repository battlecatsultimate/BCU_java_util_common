package common.util.pack.bgeffect;

import common.battle.StageBasis;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;
import common.util.Data;
import common.util.pack.Background;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class BalloonBGEffect extends BackgroundEffect {
    private FakeImage balloon;
    private FakeImage bigBalloon;

    private final List<P> balloonPosition = new ArrayList<>();
    private final List<Boolean> isBigBalloon = new ArrayList<>();
    private final List<Byte> speed = new ArrayList<>();

    private final List<Integer> capture = new ArrayList<>();

    @Override
    public void check() {
        if(balloon != null)
            balloon.bimg();

        if(bigBalloon != null)
            bigBalloon.bimg();
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz, double midH) {

    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz, double midH) {
        for(int i = 0; i < balloonPosition.size(); i++) {
            FakeImage img = isBigBalloon.get(i) ? bigBalloon : balloon;

            g.drawImage(
                    img,
                    convertP(balloonPosition.get(i).x + Data.BG_EFFECT_BALLOON_FACTOR * Math.sin(balloonPosition.get(i).y / Data.BG_EFFECT_BALLOON_STABILIZER), siz) + (int) rect.x,
                    (int) (balloonPosition.get(i).y * siz - rect.y + midH * siz),
                    img.getWidth() * siz,
                    img.getHeight() * siz
            );
        }
    }

    @Override
    public void update(StageBasis sb) {
        capture.clear();

        for(int i = 0; i < balloonPosition.size(); i++) {
            int bh = isBigBalloon.get(i) ? bigBalloon.getHeight() : balloon.getHeight();

            if(balloonPosition.get(i).y < -bh) {
                capture.add(i);
            } else {
                balloonPosition.get(i).y -= speed.get(i);
            }
        }

        if(!capture.isEmpty()) {
            for(int i = 0; i < capture.size(); i++) {
                boolean isBig = Math.random() >= 0.5;

                int bw = isBig ? bigBalloon.getWidth() : balloon.getWidth();

                balloonPosition.get(capture.get(i)).x = Math.random() * (sb.st.len + battleOffset + 2 * revertP(bw)) - revertP(bw);
                balloonPosition.get(capture.get(i)).y = BGHeight * 3;
                isBigBalloon.set(capture.get(i), isBig);
            }
        }
    }

    @Override
    public void initialize(StageBasis sb) {
        for(int i = 0; i < balloonPosition.size(); i++) {
            P.delete(balloonPosition.get(i));
        }

        balloonPosition.clear();
        isBigBalloon.clear();

        balloon = null;
        bigBalloon = null;

        Background bg;

        if(!sb.bg.id.pack.equals(Identifier.DEF) || sb.bg.effect != Data.BG_EFFECT_BALLOON) {
            bg = UserProfile.getBCData().bgs.get(81);
        } else {
            bg = sb.bg;
        }

        bg.load();

        balloon = bg.parts[20];
        bigBalloon = bg.parts[21];

        int number = sb.st.len / 400;

        for(int i = 0; i < number; i++) {
            boolean isBig = Math.random() >= 0.5;

            int bw = isBig ? bigBalloon.getWidth() : balloon.getWidth();

            balloonPosition.add(P.newP(Math.random() * (sb.st.len + battleOffset + 2 * revertP(bw)) - revertP(bw), Math.random() * BGHeight * 3));
            isBigBalloon.add(isBig);
            speed.add((byte) Data.BG_EFFECT_BALLOON_SPEED);
        }
    }
}
