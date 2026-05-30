package common.battle.attack;

import common.battle.entity.AbEntity;
import common.battle.entity.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttackBlast extends AttackAb {
    public ContBlast handler;
    protected final Set<Entity> bcapt = new HashSet<>();
    protected final int stage;

    boolean attacked = false;

    protected AttackBlast(Entity attacker, AttackSimple src, float sta, float end, int bt, int stage) {
        super(attacker, src, sta, end, false);
        waveType = bt;
        this.stage = stage;
    }

    @Override
    public void capture() {
        capt.clear();

        List<AbEntity> le = new ArrayList<>();

        if (stage == 0)
            le.addAll(model.b.inRange(touch, -dire, sta, end, excludeRightEdge));
        else // todo: figure out if sta is always rightmost and end is always leftmost
            le.addAll(model.b.inRange(touch, -dire, sta + EXPLOSION_SHIFT * stage, end - EXPLOSION_SHIFT * stage, excludeRightEdge, 150 + (EXPLOSION_SHIFT * (stage - 1) * 2)));
        for (AbEntity e : le)
            if (e instanceof Entity && !bcapt.contains((Entity) e))
                capt.add(e);
    }

    @Override
    public void excuse() {
        process();

        atk = rawAtk;

        if (attacker != null) {
            int[][] status = attacker.status;
            if (status[P_STRONG][0] != 0)
                atk += atk * status[P_STRONG][0] / 100;
            if (status[P_STRONG][1] != 0)
                atk += atk * status[P_STRONG][1] / 100;
            if (status[P_WEAK][0] != 0)
                atk = atk * status[P_WEAK][1] / 100;
            atk = (atk * (100 - (30 * stage)) / 100);
        }

        for (AbEntity e : capt) {
            if (e.isBase() && !(e instanceof Entity))
                continue;

            if (e instanceof Entity) {
                boolean damaged = e.damaged(this);
                if (damaged)
                    ((Entity) e).lastHitBy.add(this);
                attacked = true;
                bcapt.add((Entity) e);
            }
        }
    }
}
