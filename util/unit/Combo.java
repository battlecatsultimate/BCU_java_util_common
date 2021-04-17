package common.util.unit;

import common.CommonStatic;
import common.io.json.FieldOrder;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.pack.IndexContainer;
import common.pack.PackData;
import common.pack.UserProfile;
import common.system.files.VFile;
import common.util.Data;
import common.util.lang.MultiLangCont;

import java.util.Arrays;
import java.util.Queue;

@IndexContainer.IndexCont(PackData.class)
@JsonClass.JCGeneric(Identifier.class)
@JsonClass
public class Combo extends Data implements IndexContainer.Indexable<IndexContainer, Combo> {
	public static void readFile() {
		CommonStatic.BCAuxAssets aux = CommonStatic.getBCAssets();
		PackData.DefPack data = UserProfile.getBCData();
		Queue<String> qs = VFile.readLine("./org/data/NyancomboData.csv");
		int i = 0;
		for (String str : qs) {
			if (str.length() < 20) {
				// data.combos.add(null);
				continue;
			}
			String[] strs = str.trim().split(",");
			Combo c = new Combo(Identifier.parseInt(i++, Combo.class), strs);
			if (c.show > 0)
				data.combos.add(c);
		}

		qs = VFile.readLine("./org/data/NyancomboParam.tsv");
		for (i = 0; i < C_TOT; i++) {
			String[] strs = qs.poll().trim().split("\t");
			if (strs.length < 5)
				continue;
			for (int j = 0; j < 5; j++) {
				aux.values[i][j] = Integer.parseInt(strs[j]);
				if (i == C_C_SPE)
					aux.values[i][j] = (aux.values[i][j] - 10) * 15;
			}
		}
		qs = VFile.readLine("./org/data/NyancomboFilter.tsv");
		aux.filter = new int[qs.size()][];
		for (i = 0; i < aux.filter.length; i++) {
			String[] strs = qs.poll().trim().split("\t");
			aux.filter[i] = new int[strs.length];
			for (int j = 0; j < strs.length; j++)
				aux.filter[i][j] = Integer.parseInt(strs[j]);
		}
	}

	@JsonClass.JCIdentifier
	@JsonField
	@FieldOrder.Order(0)
	public Identifier<Combo> id;

	@JsonField
	@FieldOrder.Order(1)
	public int lv, show, type;

	@JsonField(alias = Form.FormJson.class)
	@FieldOrder.Order(2)
	public Form[] forms;

	@JsonField
	@FieldOrder.Order(3)
	public String name;

	@JsonClass.JCConstructor
	public Combo() {
		id = null;
	}

	protected Combo(Identifier<Combo> ID, String[] strs) {
		id = ID;
		name = strs[0];
		show = Integer.parseInt(strs[1]);
		int n;
		for (n = 0; n < 5; n++)
			if (Integer.parseInt(strs[2 + n * 2]) == -1)
				break;
		forms = new Form[n];
		for (int i = 0; i < n; i++) {
			Identifier<Unit> u = Identifier.parseInt(Integer.parseInt(strs[2 + i * 2]), Unit.class);
			forms[i] = u.get().forms[Integer.parseInt(strs[3 + i * 2])];
		}
		type = Integer.parseInt(strs[12]);
		lv = Integer.parseInt(strs[13]);
	}

	public Combo(Identifier<Combo> ID, String n, int l, int t, int s, Form f) {
		id = ID;
		name = n;
		lv = l;
		type = t;
		show = s;
		forms = new Form[] { f };
	}

	@Override
	public String toString() {
		return id.toString();
	}

	@Override
	public Identifier<Combo> getID() {
		return id;
	}

	public String getName() {
		String n = MultiLangCont.get(this);
		if (n != null && n.length() > 0)
			return n;
		else if (name != null && name.length() > 0)
			return name;
		else
			return null;
	}

	public void setType(int t) {
		type = t;
	}

	public void setLv(int l) {
		lv = l;
	}

	public void addForm(Form f) {
		forms = Arrays.copyOf(forms, forms.length + 1);
		forms[forms.length - 1] = f;
	}

	public void removeForm(int index) {
		Form[] formSrc = new Form[forms.length - 1];
		for (int i = 0, j = 0; i < forms.length; i++) {
			if (i != index)
				formSrc[j++] = forms[j];
		}
		forms = formSrc;
	}
}