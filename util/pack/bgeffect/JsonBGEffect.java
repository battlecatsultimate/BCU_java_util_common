package common.util.pack.bgeffect;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.CommonStatic;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.files.VFile;
import common.util.Data;
import common.util.pack.Background;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class JsonBGEffect extends BackgroundEffect {
    private final int id;
    private final List<BGEffectHandler> handlers = new ArrayList<>();

    public JsonBGEffect(int bgID) throws IOException {
        id = bgID;
        String jsonName = "bg"+ Data.trio(id)+".json";

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
                BGEffectSegment segment = new BGEffectSegment(arr.get(i).getAsJsonObject(), jsonName);
                handlers.add(new BGEffectHandler(segment, id));
            }
        } else if (obj.has("id")) {
            int efID = obj.get("id").getAsInt();

            ArrayList<BackgroundEffect> effs = CommonStatic.getBCAssets().bgEffects;
            for (BackgroundEffect bge : effs)
                if (bge instanceof JsonBGEffect && ((JsonBGEffect)bge).id == efID) {
                    handlers.addAll(((JsonBGEffect)bge).handlers);
                    break;
                }
        }
    }

    @Override
    public void check() {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).check();
        }
    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz, double midH) {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).preDraw(g, rect, siz);
        }
    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz, double midH) {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).postDraw(g, rect, siz);
        }
    }

    @Override
    public void update(int w, double h, double midH) {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).update(w, h, midH);
        }
    }

    @Override
    public void initialize(int w, double h, double midH, Background bg) {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).initialize(w, h, midH);
        }
    }

    @Override
    public void release() {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).release();
        }
    }
}
