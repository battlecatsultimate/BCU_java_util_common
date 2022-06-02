package common.util.stage.info;

import common.io.json.JsonClass;
import common.io.json.JsonDecoder;
import common.io.json.JsonField;
import common.pack.Identifier;
import common.util.stage.MapColc.PackMapColc;
import common.util.stage.Stage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

@JsonClass
public class CustomStageInfo implements StageInfo {
    private static final DecimalFormat df;

    static {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        df = (DecimalFormat) nf;

        df.applyPattern("#.##");
    }

    @JsonField(alias = Identifier.class)
    public final Stage st;
    @JsonField(generic = Stage.class, alias = Identifier.class)
    public final ArrayList<Stage> stages = new ArrayList<>();
    @JsonField(generic = Float.class)
    public final ArrayList<Float> chances = new ArrayList<>();
    public short totalChance;

    @JsonClass.JCConstructor
    public CustomStageInfo() {
        st = null;
    }

    public CustomStageInfo(Stage st) {
        this.st = st;
        st.info = this;
        ((PackMapColc)st.getCont().getCont()).si.add(this);
    }

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
        totalChance = (short)maxChance;

        if (maxChance > 100)
            setTotalChance((byte) 100);
    }

    public void setTotalChance(byte newChance) {
        for (int i = 0; i < chances.size(); i++)
            chances.set(i, (chances.get(i) / totalChance) * newChance);
        totalChance = newChance;
    }

    public void equalizeChances() {
        for (int i = 0; i < chances.size(); i++)
            chances.set(i, (float)totalChance / chances.size());
    }

    /**
     * Called to detach the followup from the stage and remove it from the list storing it
     */
    public void destroy() {
        stages.clear();
        chances.clear();
        ((PackMapColc)st.getCont().getCont()).si.remove(this);
        st.info = null;
    }

    @JsonDecoder.OnInjected
    public void onInjected() {
        st.info = this;
        for (int i = 0; i < chances.size(); i++)
            totalChance += chances.get(i);
    }
}
