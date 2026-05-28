package common.battle.attack;

import common.CommonStatic;
import common.battle.entity.Entity;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.Data;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim;

public class ContBlast extends ContAb {
    protected final AttackBlast[] atk = new AttackBlast[3];
    protected final EAnimD<EffAnim.BlastEff> anim;
    private int t = 0;

    protected ContBlast(Entity attacker, AttackSimple src, float p, int lay) {
        super(src.model.b, p, 8);
        anim = (src.dire == 1 ? effas().A_E_BLAST : effas().A_BLAST).getEAnim(EffAnim.BlastEff.START);

        for (int i = 0; i < 3; i++) {
            AttackBlast blast = new AttackBlast(attacker, src, pos + 75, pos - 75, Data.WT_BLST, i);
            atk[i] = blast;
            atk[i].handler = this;
        }

        anim.setTime(1);
    }

    @Override
    public void draw(FakeGraphics gra, P p, float psiz) {
        FakeTransform at = gra.getTransform();
        P s = new P(atk[0].dire == -1 ? p.x + (100 * psiz) : p.x - (30 * psiz), p.y); // todo: correct offset for enemies
        anim.draw(gra, s, psiz);
        P.delete(s);
        gra.setTransform(at);
        if (CommonStatic.getConfig().ref)
            drawAxis(gra, p, psiz);

    }

    public void drawAxis(FakeGraphics gra, P p, float siz) {
        siz *= 1.25f;
        float rat = CommonStatic.BattleConst.ratio;
        int h = (int) (640 * rat * siz);
        float d0 = Math.min(atk[0].sta, atk[0].end); // leftmost point
        int y = (int) p.y;
        gra.setColor(FakeGraphics.MAGENTA);

        for (int i = 0; i < 3; i++) {
            AttackBlast a = atk[i];
            if (i == 0 && t >= 10 && t < 25) {
                float rawWidth = Math.abs(a.sta - a.end); // raw length
                int x = (int) ((d0 - pos) * rat * siz + p.x);
                int w = (int) (rawWidth * rat * siz);
                if (a.attacked)
                    gra.fillRect(x, y, w, h);
                else
                    gra.drawRect(x, y, w, h);
            } else if (t >= 10 + 10 * i && t <= 25 + 10 * i) {
                int x1 = (int) ((d0 - EXPLOSION_SHIFT * i - pos) * rat * siz + p.x);
                int x2 = (int) ((Math.max(a.sta, a.end) - pos + EXPLOSION_SHIFT * (i - 1)) * rat * siz + p.x);
                int w = (int) (EXPLOSION_SHIFT * rat * siz);
                if (a.attacked) {
                    gra.fillRect(x1, y, w, h);
                    gra.fillRect(x2, y, w, h);
                }
                else {
                    gra.drawRect(x1, y, w, h);
                    gra.drawRect(x2, y, w, h);
                }
            }
        }
    }

    @Override
    public void update() {
        t++;
        for (AttackBlast a : atk)
            if (a.attacked)
                a.attacked = false;

        if (t == EXPLOSION_PRE)
            anim.changeAnim(EffAnim.BlastEff.EXPLODE, true);
        if (t == 10)
            CommonStatic.setSE(EXPLOSION_SE);
        else if (t == 20)
            CommonStatic.setSE(EXPLOSION_SE + 1);
        else if (t == 30)
            CommonStatic.setSE(EXPLOSION_SE + 2);
        for (int i = 0; i < 3; i++) {
            if (t >= 10 + 10 * i && t <= 25 + 10 * i)
                sb.getAttack(atk[i]);
        }
        if (t > 45)
            activate = false;
        updateAnimation();
    }

    @Override
    public void updateAnimation() {
        anim.update(false);
    }

    @Override
    public boolean IMUTime() {
        return false;
    }

    public int getTime() {
        return t;
    }
}
