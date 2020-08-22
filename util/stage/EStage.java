package common.util.stage;

import common.battle.StageBasis;
import common.battle.entity.EEnemy;
import common.pack.Identifier;
import common.util.BattleObj;
import common.util.stage.SCDef.Line;
import common.util.unit.AbEnemy;

public class EStage extends BattleObj {

    public final Stage s;
    public final Limit lim;
    public final int[] num, rem;
    public final double mul;
    public final int star;

    private StageBasis b;

    public EStage(Stage st, int stars) {
        s = st;
        star = stars;
        st.validate();
        Line[] datas = s.data.getSimple();
        rem = new int[datas.length];
        num = new int[datas.length];
        for (int i = 0; i < rem.length; i++)
            num[i] = datas[i].number;
        lim = st.getLim(star);
        mul = st.map.stars[star] * 0.01;
    }

    /**
     * add n new enemies to StageBasis
     */
    public EEnemy allow() {
        for (int i = 0; i < rem.length; i++) {
            Line data = s.data.getSimple(i);
            if (inHealth(data) && s.data.allow(b, data.group) && rem[i] == 0 && num[i] != -1) {
                rem[i] = data.respawn_0 + (int) (b.r.nextDouble() * (data.respawn_1 - data.respawn_0));
                if (num[i] > 0) {
                    num[i]--;
                    if (num[i] == 0)
                        num[i] = -1;
                }
                if (data.boss == 1)
                    b.shock = true;

                double multi = (data.multiple == 0 ? 100 : data.multiple) * mul * 0.01;
                double mulatk = (data.mult_atk == 0 ? 100 : data.mult_atk) * mul * 0.01;
                AbEnemy e = Identifier.getOr(data.enemy);
                EEnemy ee = e.getEntity(b, data, multi, mulatk, data.layer_0, data.layer_1, data.boss);
                ee.group = data.group;
                return ee;
            }
        }
        return null;
    }

    public void assign(StageBasis sb) {
        b = sb;
        Line[] datas = s.data.getSimple();
        for (int i = 0; i < rem.length; i++) {
            rem[i] = datas[i].spawn_0;
            if (Math.abs(datas[i].spawn_0) < Math.abs(datas[i].spawn_1))
                rem[i] += (int) ((datas[i].spawn_1 - datas[i].spawn_0) * b.r.nextDouble());
        }
    }

    /**
     * get the Entity representing enemy base, return null if none
     */
    public EEnemy base(StageBasis sb) {
        int ind = num.length - 1;
        if (ind < 0)
            return null;
        Line data = s.data.getSimple(ind);
        if (data.castle_0 == 0) {
            num[ind] = -1;
            double multi = data.multiple * mul * 0.01;
            double mulatk = data.mult_atk == 0 ? multi : data.mult_atk * mul * 0.01;
            AbEnemy e = Identifier.getOr(data.enemy);
            return e.getEntity(sb, this, multi, mulatk, data.layer_0, data.layer_1, -1);
        }
        return null;
    }

    /**
     * return true if there is still boss in the base
     */
    public boolean hasBoss() {
        for (int i = 0; i < rem.length; i++) {
            Line data = s.data.getSimple(i);
            if (data.boss == 1 && num[i] > 0)
                return true;
        }
        return false;
    }

    public void update() {
        for (int i = 0; i < rem.length; i++) {
            Line data = s.data.getSimple(i);
            if (inHealth(data) && rem[i] < 0)
                rem[i] *= -1;
            if (rem[i] > 0)
                rem[i]--;
        }
    }

    private boolean inHealth(Line line) {
        int c0 = line.castle_0;
        int c1 = line.castle_1;
        double d = !s.trail ? b.getEBHP() * 100 : b.ebase.maxH - b.ebase.health;
        return c0 >= c1 ? (s.trail ? d >= c0 : d <= c0) : (d > c0 && d <= c1);
    }

}
