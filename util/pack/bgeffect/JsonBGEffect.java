package common.util.pack.bgeffect;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.battle.StageBasis;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.files.VFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonBGEffect extends BackgroundEffect {
    private final List<BGEffectSegment> segments = new ArrayList<>();

    public JsonBGEffect(String jsonName) throws IOException {
        VFile vf = VFile.get("./org/data/"+jsonName);

        if(vf == null) {
            throw new FileNotFoundException("Such json file not found : ./org/data/"+jsonName);
        }

        Reader r = new InputStreamReader(vf.getData().getStream(), StandardCharsets.UTF_8);

        JsonElement elem = JsonParser.parseReader(r);

        r.close();

        JsonObject obj = elem.getAsJsonObject();

        if(obj.has("data")) {
            JsonArray arr = obj.getAsJsonArray("data");

            for(int i = 0; i < arr.size(); i++) {
                segments.add(new BGEffectSegment(arr.get(i).getAsJsonObject()));
            }
        }
    }

    @Override
    public void check() {

    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz, double midH) {

    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz, double midH) {

    }

    @Override
    public void update(StageBasis sb) {

    }

    @Override
    public void initialize(StageBasis sb) {

    }
}
