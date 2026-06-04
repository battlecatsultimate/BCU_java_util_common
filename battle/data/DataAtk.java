package common.battle.data;

import common.util.Data.Proc;

public class DataAtk implements MaskAtk {

	public final int index;

	public final DefaultData data;

	public DataAtk(DefaultData data, int index) {
		this.index = index;
		this.data = data;
	}

	@Override
	public int getAtk() {
        return switch (index) {
            case 0 -> data.atk;
            case 1 -> data.atk1;
            case 2 -> data.atk2;
            default -> 0;
        };
	}

	@Override
	public boolean isOmni() {
		return data.ldr[index] < 0;
	}

	@Override
	public int getLongPoint() {
		if (index >= data.lds.length)
			return data.lds[0] + data.ldr[0];
		return data.lds[index] + data.ldr[index];
	}

	@Override
	public Proc getProc() {
		return data.proc;
	}

	@Override
	public boolean getSPtrait() { return false; }

	@Override
	public int getShortPoint() {
		if (index >= data.lds.length)
			return data.lds[0];
		return data.lds[index];
	}

	@Override
	public boolean isRange() {
		return data.isrange;
	}
}
