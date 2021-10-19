package common.system;

import common.util.unit.Enemy;

import java.util.List;

public class ENode extends Node<Enemy> {

    public ENode(Enemy v) {
        super(v);
        mul = 100;
        mula = 100;
    }

    public ENode(Enemy v, int[] muls) {
        super(v);
        mul = muls[0];
        mula = muls[1];
    }

    public static ENode getList(List<Enemy> list, Enemy enemy) {
        ENode ans = null, ret = null;
        for (Enemy e : list) {
            ENode temp = new ENode(e);
            if (ans != null)
                ans.add(temp);
            if (e == enemy)
                ret = temp;
            ans = temp;
        }
        return ret;
    }

    public static ENode getList(List<Enemy> list, Enemy enemy, List<int[]> muls) {
        ENode ans = null, ret = null;
        for (int i = 0; i < list.size(); i++) {
            ENode temp = new ENode(list.get(i), muls.get(i));
            if (ans != null)
                ans.add(temp);
            if (list.get(i) == enemy)
                ret = temp;
            ans = temp;
        }
        return ret;
    }

    public final int mul, mula;
}
