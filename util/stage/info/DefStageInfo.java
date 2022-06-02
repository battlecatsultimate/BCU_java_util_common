package common.util.stage.info;

import common.CommonStatic;
import common.pack.Identifier;
import common.util.Data;
import common.util.lang.MultiLangCont;
import common.util.stage.MapColc;
import common.util.stage.Music;
import common.util.stage.Stage;
import common.util.stage.StageMap;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DefStageInfo implements StageInfo {
    private static final DecimalFormat df;

    static {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        df = (DecimalFormat) nf;

        df.applyPattern("#.##");
    }

    public final Stage st;
    public final StageMap.StageMapInfo map;
    public final int energy, xp, once, rand;
    public final int[][] drop;
    public final int[][] time;
    public Stage[] exStages;
    public float[] exChances;
    public int diff = -1, exChance = -1, exMapID = -1, exStageIDMin = -1, exStageIDMax = -1;
    public boolean exConnection = false;

    public DefStageInfo(StageMap.StageMapInfo info, Stage s, int[] data) {
        map = info;
        st = s;

        energy = data[0];
        xp = data[1];
        s.mus0 = Identifier.parseInt(data[2], Music.class);
        s.mush = data[3];
        s.mus1 = Identifier.parseInt(data[4], Music.class);

        once = data[data.length - 1];
        boolean isTime = data.length > 15;
        if (isTime)
            for (int i = 8; i < 15; i++)
                if (data[i] != -2) {
                    isTime = false;
                    break;
                }
        if (isTime) {
            time = new int[(data.length - 17) / 3][3];
            for (int i = 0; i < time.length; i++)
                for (int j = 0; j < 3; j++)
                    time[i][j] = data[16 + i * 3 + j];
        } else
            time = new int[0][3];
        boolean isMulti = !isTime && data.length > 9;
        if (data.length == 6) {
            drop = new int[0][];
            rand = 0;
        } else if (!isMulti) {
            drop = new int[1][];
            rand = 0;
        } else {
            drop = new int[(data.length - 7) / 3][3];
            rand = data[8];
            for (int i = 1; i < drop.length; i++)
                for (int j = 0; j < 3; j++)
                    drop[i][j] = data[6 + i * 3 + j];
        }
        if (drop.length > 0)
            drop[0] = new int[] { data[5], data[6], data[7] };
    }

    public void setData(String[] strs) {
        int chance = CommonStatic.parseIntN(strs[2]);

        exConnection = chance != 0;
        exChance = chance;

        exMapID = CommonStatic.parseIntN(strs[3]);

        exStageIDMin = CommonStatic.parseIntN(strs[4]);
        exStageIDMax = CommonStatic.parseIntN(strs[5]);
    }

    @Override
    public String getHTML() {
        StringBuilder ans = new StringBuilder("<html>energy cost: " + energy + "<br> xp: " + xp);

        ans.append("<br> Will be hidden upon full clear : ")
                .append(st.getCont().info.hiddenUponClear);

        if (st.getCont().info.resetMode != -1) {
            switch (st.getCont().info.resetMode) {
                case 1:
                    ans.append("<br> This map's reward will get reset upon each appearance");
                    break;
                case 2:
                    ans.append("<br> This map's clear status will be reset upon each appearance");
                    break;
                case 3:
                    ans.append("<br> This map's number of plays can be done will be reset upon each appearance");
                    break;
                default:
                    ans.append("<br> Reset mode flag ")
                            .append(st.getCont().info.resetMode);
            }
        }

        if (st.getCont().info.waitTime != -1) {
            ans.append("<br> You have to wait for ")
                    .append(st.getCont().info.waitTime)
                    .append(" minute(s) to play this stage");
        }

        if (st.getCont().info.clearLimit != -1) {
            ans.append("<br> number that you can play this stage : ")
                    .append(st.getCont().info.clearLimit);
        }

        ans.append("<br><br> EX stage existing : ")
                .append(exConnection || (exStages != null && exChances != null));

        if (exConnection) {
            ans.append("<br> EX stage appearance chance : ")
                    .append(exChance)
                    .append("%<br> EX Map Name : ")
                    .append(MultiLangCont.get(MapColc.get("000004").maps.get(exMapID)))
                    .append("<br> EX Stage ID Min : ")
                    .append(Data.duo(exStageIDMin))
                    .append("<bR> EX Stage ID Max : ")
                    .append(Data.duo(exStageIDMax))
                    .append("<br>");
        }

        if (exStages != null && exChances != null) {
            ans.append("<table><tr><th>EX Stage Name</th><th>Chance</th></tr>");

            for (int i = 0; i < exStages.length; i++) {
                if (exStages[i] == null)
                    continue;

                String name = MultiLangCont.get(exStages[i]);
                String smName = MultiLangCont.get(exStages[i].getCont());

                if (name == null || name.isEmpty())
                    name = exStages[i].id.toString();
                else if (smName == null || smName.isEmpty()) {
                    smName = exStages[i].getCont().id.toString();

                    name = smName + " - " + name;
                } else {
                    name = smName + " - " + name;
                }

                ans.append("<tr><td>")
                        .append(name)
                        .append("</td><td>")
                        .append(df.format(exChances[i]))
                        .append("%</td></tr>");
            }

            ans.append("</table>");
        }

        if (!exConnection && (exStages == null || exChances == null)) {
            ans.append("<br>");
        }

        ans.append("<br> drop rewards");

        if(drop == null || drop.length == 0) {
            ans.append(" : none");
        } else {
            ans.append("<br>");
            appendDropData(ans);
        }

        if (time.length > 0) {
            ans.append("<br> time scores: count: ").append(time.length).append("<br>");
            ans.append("<table><tr><th>score</th><th>item name</th><th>number</th></tr>");
            for (int[] tm : time)
                ans.append("<tr><td>").append(tm[0]).append("</td><td>").append(MultiLangCont.getStatic().RWNAME.getCont(tm[1])).append("</td><td>").append(tm[2]).append("</td><tr>");
            ans.append("</table>");
        }
        return ans.toString();
    }

    @Override
    public boolean exConnection() {
        return exConnection;
    }

    @Override
    public Stage[] getExStages() {
        return exStages;
    }

    @Override
    public float[] getExChances() {
        if (exConnection)
            return new float[]{-1, exChance};
        return exChances;
    }

    private void appendDropData(StringBuilder ans) {
        if (drop == null || drop.length == 0) {
            ans.append("none");
            return;
        }

        List<String> chances = analyzeRewardChance();

        if(chances == null) {
            ans.append("none");
            return;
        }

        if(chances.isEmpty()) {
            ans.append("<table><tr><th>No.</th><th>item name</th><th>amount</th></tr>");
        } else {
            ans.append("<table><tr><th>chance</th><th>item name</th><th>amount</th></tr>");
        }

        for(int i = 0; i < drop.length; i++) {
            if(!chances.isEmpty() && i < chances.size() && Double.parseDouble(chances.get(i)) == 0.0)
                continue;

            String chance;

            if(chances.isEmpty())
                chance = String.valueOf(i + 1);
            else
                chance = chances.get(i) + "%";

            String reward = MultiLangCont.getStatic().RWNAME.getCont(drop[i][1]);

            if(reward == null || reward.isEmpty())
                reward = "Reward " + drop[i][1];

            if(i == 0 && (rand == 1 || (drop[i][1] >= 1000 && drop[i][1] < 30000)))
                reward += " (Once)";

            if(i == 0 && drop[i][0] != 100 && rand != -4)
                reward += " [Treasure Radar]";

            ans.append("<tr><td>")
                    .append(chance)
                    .append("</td><td>")
                    .append(reward)
                    .append("</td><td>")
                    .append(drop[i][2])
                    .append("</td></tr>");
        }

        ans.append("</table>");
    }

    private List<String> analyzeRewardChance() {
        ArrayList<String> res = new ArrayList<>();

        int sum = 0;

        for(int[] d : drop) {
            sum += d[0];
        }

        if(sum == 0)
            return null;

        if(sum == 1000) {
            for(int[] d : drop) {
                res.add(df.format(d[0]/10.0));
            }
        } else if((sum == drop.length && sum != 1) || rand == -3) {
            return res;
        } else if(sum == 100) {
            for(int[] d : drop) {
                res.add(String.valueOf(d[0]));
            }
        } else if(sum > 100 && (rand == 0 || rand == 1)) {
            double rest = 100.0;

            if(drop[0][0] == 100) {
                res.add(String.valueOf(100));

                for(int i = 1; i < drop.length; i++) {
                    double filter = rest * drop[i][0] / 100.0;

                    rest -= filter;

                    res.add(df.format(filter));
                }
            } else {
                for(int[] d : drop) {
                    double filter = rest * d[0] / 100.0;

                    rest -= filter;

                    res.add(df.format(filter));
                }
            }
        } else if(rand == -4) {
            int total = 0;

            for(int[] d : drop) {
                total += d[0];
            }

            if(total == 0)
                return null;

            for(int[] d : drop) {
                res.add(df.format(d[0] * 100.0 / total));
            }
        } else {
            for(int[] d : drop) {
                res.add(String.valueOf(d[0]));
            }
        }

        return res;
    }
}
