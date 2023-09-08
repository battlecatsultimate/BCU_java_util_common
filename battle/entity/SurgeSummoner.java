package common.battle.entity;

import common.CommonStatic;
import common.battle.attack.AttackVolcano;
import common.battle.attack.ContVolcano;
import common.util.anim.EAnimD;

public class SurgeSummoner extends EAnimCont {
    private final Entity summoner;
    private final int time, sta, end, type;
    private int surge = COUNTER_SURGE_FORESWING, sound = COUNTER_SURGE_SOUND;

    public SurgeSummoner(float p, int lay, EAnimD<?> ead, Entity summoner, int time, int type, int sta, int end) {
        super(p, lay, ead);

        this.summoner = summoner;
        this.time = time;
        this.type = type;
        this.sta = Math.min(sta, end);
        this.end = Math.max(sta, end);
    }

    @Override
    public void update() {
        super.update();

        if (surge > 0) {
            surge--;

            if (surge == 0) {
                //Shoot surge
                int dire = summoner.dire;
                int addp = sta + (int) (summoner.basis.r.nextFloat() * (end - sta));
                float p0 = summoner.pos + dire * addp;
                float s = p0 + (dire == 1 ? W_VOLC_PIERCE : W_VOLC_INNER);
                float e = p0 - (dire == 1 ? W_VOLC_INNER : W_VOLC_PIERCE);

                AttackVolcano volcanoAttack = new AttackVolcano(summoner, summoner.aam.getAttack(0), s, e, type);

                if ((type & WT_MIVC) > 0) {
                    volcanoAttack.getProc().MINIVOLC.mult = 20;
                }

                ContVolcano volcano = new ContVolcano(volcanoAttack, p0, summoner.layer, time, 0);

                summoner.summoned.add(volcano);
            }
        }

        if (sound > 0) {
            sound--;

            if (sound == 0) {
                CommonStatic.setSE(SE_COUNTER_SURGE);
            }
        }
    }
}
