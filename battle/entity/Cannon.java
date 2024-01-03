package common.battle.entity;

import common.CommonStatic;
import common.CommonStatic.BattleConst;
import common.battle.StageBasis;
import common.battle.attack.*;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.Data;
import common.util.anim.AnimU.UType;
import common.util.anim.EAnimD;
import common.util.anim.EAnimU;
import common.util.pack.NyCastle.NyType;
import common.util.unit.Form;
import common.util.unit.Trait;
import common.util.unit.Unit;

import java.util.ArrayList;
import java.util.Comparator;

public class Cannon extends AtkModelAb {

    public final int id, base, deco;
    private EAnimD<?> anim, atka, exta;
    private int preTime = 0;
    private EUnit wall = null;
    public float pos;
    private int duration = 0;

    public Cannon(StageBasis sb, int type, int deco, int base) {
        super(sb);
        id = type;
        this.base = base;
        this.deco = deco;
    }

    /**
     * call when shoot the canon
     */
    public void activate() {
        anim = CommonStatic.getBCAssets().atks[id].getEAnim(NyType.BASE);
        preTime = NYPRE[id];
        CommonStatic.setSE(SE_CANNON[id][0]);
    }

    /**
     * attack part of animation
     */
    public void drawAtk(FakeGraphics g, P ori, float siz) {
        FakeTransform at = g.getTransform();
        if (atka != null)
            atka.draw(g, ori, siz);
        g.setTransform(at);
        if (exta != null)
            exta.draw(g, ori, siz);
        g.setTransform(at);
        boolean waveOrBeam = id == BASE_H || id == BASE_SLOW || id == BASE_GROUND || id == BASE_CURSE;
        if (!CommonStatic.getConfig().ref || waveOrBeam) {
            g.delete(at);
            return;
        }

        // after this is the drawing of hit boxes
        siz *= 1.25;
        float rat = BattleConst.ratio;
        int h = (int) (640 * rat * siz);
        g.setColor(FakeGraphics.MAGENTA);
        float ra = id == BASE_BARRIER ? b.b.t().getCannonMagnification(id, Data.BASE_RANGE) : NYRAN[id];
        float d0 = id == BASE_BARRIER ? getBreakerSpawnPoint(pos, ra) : pos;
        if (id == BASE_STOP || id == BASE_WATER)
            d0 -= ra / 2;
        if (id == BASE_BARRIER)
            d0 -= ra;
        int x = (int) ((d0 - pos) * rat * siz + ori.x);
        int y = (int) ori.y;
        int w = (int) (ra * rat * siz);
        if (duration > 0)
            g.fillRect(x, y, w, h);
        else
            g.drawRect(x, y, w, h);

        g.delete(at);
    }

    /**
     * base part of animation
     */
    public void drawBase(FakeGraphics g, P ori, float siz) {
        if (anim == null)
            return;
        anim.draw(g, ori, siz);

    }

    @Override
    public int getAbi() {
        return 0;
    }

    @Override
    public int getDire() {
        return -1;
    }

    @Override
    public float getPos() {
        return 0;
    }

    public void update() {
        if (duration > 0)
            duration--;
        if (anim != null && anim.done()) {
            anim = null;
            if (id > 2 && id < 5) {
                atka = CommonStatic.getBCAssets().atks[id].getEAnim(NyType.ATK);
                CommonStatic.setSE(SE_CANNON[id][1]);
            }
        }
        if (atka != null && atka.done())
            atka = null;
        if (exta != null && exta.done())
            exta = null;
        if (anim != null) {
            if (id == 7) {
                if (anim.ind() < 32) {
                    anim.update(false);
                } else {
                    anim = null;
                }
            } else {
                anim.update(false);
            }
        }
        if (atka != null)
            atka.update(false);
        if (exta != null)
            exta.update(false);

        if (anim == null && atka == null && exta == null) {
            if (id > 2 && id < 5) {
                pos = b.ubase.pos;
                for (Entity e : b.le)
                    if (e.dire == -1 && e.pos < pos && (e.touchable() & (TCH_N | TCH_KB)) != 0)
                        pos = e.pos;
                pos -= NYRAN[id] / 2.0;
            } else if (id == 2 || id == 6) {
                pos = Math.max(800f, b.ebase.pos);
                for (Entity e : b.le)
                    if (e.dire == 1 && e.pos > pos && (e.touchable() & (TCH_N | TCH_KB | TCH_CORPSE | TCH_SOUL)) != 0)
                        pos = e.pos;
            }
        }

        if (preTime == -1 && id == 2) {
            // wall canon
            Form f = Identifier.parseInt(339, Unit.class).get().forms[0];
            EAnimU enter = f.getEAnim(UType.ENTER);
            enter.setTime(0);
            wall = new EUnit(b, f.du, enter, 1);
            b.le.add(wall);
            b.le.sort(Comparator.comparingInt(e -> e.layer));
            wall.added(-1, (int) (pos + 100)); // guessed distance from enemy compared from BC
            preTime = (int) b.b.t().getCannonMagnification(id, Data.BASE_WALL_ALIVE_TIME) + enter.len();
        }

        if (preTime == 0 || --preTime != 0)
            return;

        if (id == 2) {
            // wall canon
            if (wall != null)
                wall.kill(Entity.KillMode.SELF_DESTRUCT);
            wall = null;
            return;
        }

        Proc proc = Proc.blank();
        ArrayList<Trait> traits = new ArrayList<>();

        if (id == 0) {
            // basic canon
            traits.add(UserProfile.getBCData().traits.get(TRAIT_TOT));
            proc.WAVE.lv = b.b.t().tech[LV_CRG] + 2;
            proc.SNIPER.prob = 1;
            float wid = NYRAN[0];
            float p = b.ubase.pos - wid / 2 + 100;
            int atk = b.b.t().getCanonAtk();
            AttackCanon eatk = new AttackCanon(this, atk, traits, 0, proc, 0, 0, 1);
            new ContWaveCanon(new AttackWave(eatk.attacker, eatk, p, wid, WT_CANN | WT_WAVE), p, 0);
        } else if (id == 1) {
            // slow canon
            traits.add(UserProfile.getBCData().traits.get(TRAIT_TOT));
            proc.SLOW.time = (int) (b.b.t().getCannonMagnification(id, Data.BASE_SLOW_TIME) * (100 + b.b.getInc(C_SLOW)) / 100);
            int wid = NYRAN[1];
            int spe = 137;
            float p = b.ubase.pos - wid / 2f + spe;
            AttackCanon eatk = new AttackCanon(this, 0, traits, 0, proc, 0, 0, 1);
            new ContExtend(eatk, p, wid, spe, 1, 31, 0, 9);
        } else if (id == 3) {
            // freeze canon
            traits.add(UserProfile.getBCData().traits.get(TRAIT_TOT));
            duration = 1;
            proc.STOP.time = (int) (b.b.t().getCannonMagnification(id, Data.BASE_TIME) * (100 + b.b.getInc(C_STOP)) / 100.0);
            int atk = (int) (b.b.t().getCanonAtk() * b.b.t().getCannonMagnification(id, Data.BASE_ATK_MAGNIFICATION) / 100.0);
            int rad = NYRAN[3] / 2;
            b.getAttack(new AttackCanon(this, atk, traits, 0, proc, pos - rad, pos + rad, duration));
        } else if (id == 4) {
            // water canon
            traits.add(UserProfile.getBCData().traits.get(TRAIT_TOT));
            duration = 1;
            proc.CRIT.mult = -(int) (b.b.t().getCannonMagnification(id, Data.BASE_HEALTH_PERCENTAGE));
            int rad = NYRAN[4] / 2;
            b.getAttack(new AttackCanon(this, 1, new ArrayList<>(), 0, proc, pos - rad, pos + rad, duration));
        } else if (id == 5) {
            // zombie canon
            traits.add(UserProfile.getBCData().traits.get(TRAIT_TOT));
            proc.WAVE.lv = b.b.t().tech[LV_CRG] + 2;
            float wid = NYRAN[5];
            proc.STOP.time = (int) (b.b.t().getCannonMagnification(id, Data.BASE_TIME) * (100 + b.b.getInc(C_STOP)) / 100);
            proc.SNIPER.prob = 1;
            float p = b.ubase.pos - wid / 2 + 100;
            traits.set(0, UserProfile.getBCData().traits.get(TRAIT_ZOMBIE));
            AttackCanon eatk = new AttackCanon(this, 0, traits, AB_ONLY | AB_ZKILL | AB_CKILL, proc, 0, 0, 1);
            new ContWaveCanon(new AttackWave(eatk.attacker, eatk, p, wid, WT_CANN | WT_WAVE), p, 5);
        } else if (id == 6) {
            // barrier canon
            // guessed hit box time
            traits.addAll(UserProfile.getAll(Identifier.DEF, Trait.class));
            duration = 11;
            proc.BREAK.prob = 1;
            proc.KB.dis = KB_DIS[INT_KB];
            proc.KB.time = KB_TIME[INT_KB];
            int atk = (int) (b.b.t().getCanonAtk() * b.b.t().getCannonMagnification(id, Data.BASE_ATK_MAGNIFICATION) / 100.0);
            float rad = b.b.t().getCannonMagnification(id, Data.BASE_RANGE);
            float newPos = getBreakerSpawnPoint(pos, rad);
            b.getAttack(new AttackCanon(this, atk, traits, AB_CKILL, proc, newPos - rad, newPos - 1, duration));

            atka = CommonStatic.getBCAssets().atks[id].getEAnim(NyType.ATK);
            exta = CommonStatic.getBCAssets().atks[id].getEAnim(NyType.EXT);
        } else if (id == 7) {
            // curse cannon
            traits.add(UserProfile.getBCData().traits.get(TRAIT_TOT));
            proc.CURSE.time = (int) b.b.t().getCannonMagnification(id, Data.BASE_CURSE_TIME);
            int wid = NYRAN[7];
            int spe = 137;
            float p = b.ubase.pos - wid / 2f + spe;
            AttackCanon eatk = new AttackCanon(this, 0, traits, 0, proc, 0, 0, 1);
            new ContExtend(eatk, p, wid, spe, 1, 31, 0, 9);
        }
    }

    public void updateAnimation() {
        if (anim != null) {
            if (id == 7) {
                if (anim.ind() < 32) {
                    anim.update(false);
                } else {
                    anim = null;
                }
            } else {
                anim.update(false);
            }
        }
        if (atka != null)
            atka.update(false);
        if (exta != null)
            exta.update(false);
    }

    private float getBreakerSpawnPoint(float pos, float range) {
        return pos + (float) Math.ceil(range * 4 / 5) / 4f;
    }
}
