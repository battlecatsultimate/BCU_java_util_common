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
		switch (index) {
		case 0:
			return data.atk;
		case 1:
			return data.atk1;
		case 2:
			return data.atk2;
		default:
			return 0;
		}
	}

	@Override
	public int getLongPoint() {
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
		return data.lds[index];
	}

	@Override
	public boolean isRange() {
		return data.isrange;
	}
}
