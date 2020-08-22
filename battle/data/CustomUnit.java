package common.battle.data;

import common.io.InStream;
import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.util.unit.Form;

@JsonClass
public class CustomUnit extends CustomEntity implements MaskUnit {

    public Form pack;

    @JsonField
    public int price, resp;

    public CustomUnit() {
        rep = new AtkDataModel(this);
        atks = new AtkDataModel[1];
        atks[0] = new AtkDataModel(this);
        width = 320;
        speed = 8;
        hp = 1000;
        hb = 1;
        type = 0;
        price = 50;
        resp = 60;
    }

    public void fillData(int ver, InStream is) {
        zread(ver, is);
    }

    @Override
    public int getBack() {
        return 9;
    }

    @Override
    public int getFront() {
        return 0;
    }

    @Override
    public Orb getOrb() {
        return pack.orbs;
    }

    @Override
    public Form getPack() {
        return pack;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getRespawn() {
        return resp;
    }

    @Override
    public void importData(MaskEntity de) {
        super.importData(de);
        if (de instanceof MaskUnit) {
            MaskUnit mu = (MaskUnit) de;
            price = mu.getPrice();
            resp = mu.getRespawn();
        }
    }

    private void zread(int val, InStream is) {
        val = getVer(is.nextString());
        if (val >= 400)
            zread$000400(is);
    }

    private void zread$000400(InStream is) {
        zreada(is);
        price = is.nextInt();
        resp = is.nextInt();
    }

}
