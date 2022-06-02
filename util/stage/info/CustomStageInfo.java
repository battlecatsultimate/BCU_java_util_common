package common.util.stage.info;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.util.stage.Stage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

@JsonClass(noTag = JsonClass.NoTag.LOAD)
public class CustomStageInfo implements StageInfo {
    private static final DecimalFormat df;

    static {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        df = (DecimalFormat) nf;

        df.applyPattern("#.##");
    }

    @JsonField(generic = Stage.class, alias = Identifier.class)
    public final ArrayList<Stage> stages = new ArrayList<>();
    @JsonField(generic = Float.class)
    public final ArrayList<Float> chances = new ArrayList<>();

    @Override
    public String getHTML() {
        if (stages.size() == 0)
            return null;
        StringBuilder ans = new StringBuilder("<html><table><tr><th>List of Followup Stages:</th></tr>");
        for (int i = 0; i < stages.size(); i++)
            ans.append("<tr><td>")
                    .append(stages.get(i).getCont().toString())
                    .append(" - ")
                    .append(stages.get(i).toString())
                    .append("</td><td>")
                    .append(df.format(chances.get(i)))
                    .append("%</td></tr>");

        return ans.toString();
    }

    @Override
    public boolean exConnection() {
        return false;
    }

    @Override
    public Stage[] getExStages() {
        return stages.toArray(new Stage[0]);
    }

    @Override
    public float[] getExChances() {
        float[] FChances = new float[chances.size()];
        for (int i = 0; i < chances.size(); i++)
            FChances[i] = chances.get(i);

        return FChances;
    }

    public void checkChances() {
        float maxChance = 0;
        for (int i = 0; i < chances.size(); i++)
            maxChance += chances.get(i);
        if (maxChance > 100)
            for (int i = 0; i < chances.size(); i++)
                chances.set(i, (chances.get(i) / maxChance) * 100);
    }
}
