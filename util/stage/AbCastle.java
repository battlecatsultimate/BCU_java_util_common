package common.util.stage;

import java.util.List;

import common.system.VImg;

public interface AbCastle {

	public static final String REG_CASTLE = "castle";

	public VImg get(int ind);

	public int getCasID(VImg val);

	public List<VImg> getList();

	public int size();

}
