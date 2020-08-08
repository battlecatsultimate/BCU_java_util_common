package common.util.lang;

import common.io.json.JsonClass;
import common.io.json.JsonClass.NoTag;

public class LocaleCenter {

	public static interface Binder {

		String getNameID();

		String getNameValue();

		String getTooltipID();

		String getToolTipValue();

		Binder refresh();

		void setNameValue(String str);

		void setToolTipValue(String str);

	}

	@JsonClass(noTag = NoTag.LOAD)
	public static class DisplayItem {
		public String name, tooltip;
	}

	public static class ObjBinder implements Binder {

		public static interface BinderFunc {

			public Binder refresh(String name);

		}

		public final String name;
		public final DisplayItem item;
		public final BinderFunc func;

		public ObjBinder(DisplayItem item, String name, BinderFunc func) throws Exception {
			this.item = item;
			this.name = name;
			this.func = func;
		}

		@Override
		public String getNameID() {
			return name;
		}

		@Override
		public String getNameValue() {
			return item.name;
		}

		@Override
		public String getTooltipID() {
			return name;
		}

		@Override
		public String getToolTipValue() {
			return item.tooltip;
		}

		@Override
		public Binder refresh() {
			return func.refresh(name);
		}

		@Override
		public void setNameValue(String str) {
			item.name = str;
		}

		@Override
		public void setToolTipValue(String str) {
			item.tooltip = str;
		}

	}

}
