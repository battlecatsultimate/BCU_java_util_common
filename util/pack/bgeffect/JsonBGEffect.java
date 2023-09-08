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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonBGEffect extends BackgroundEffect {
    protected final int id;
    private final List<BGEffectHandler> handlers = new ArrayList<>();

    protected boolean postNeed = false;

    public JsonBGEffect(int bgID, boolean post) throws IOException {
        id = bgID;

        String jsonName = "bg"+ Data.trio(id)+".json";

        VFile vf = VFile.get("./org/data/"+jsonName);

        if(vf == null) {
            throw new FileNotFoundException("Such json file not found : ./org/data/"+jsonName);
        }

        try {
            Reader r = new InputStreamReader(vf.getData().getStream(), StandardCharsets.UTF_8);

            JsonElement elem = JsonParser.parseReader(r);

            r.close();

            JsonObject obj = elem.getAsJsonObject();

            if(obj.has("data")) {
                JsonArray arr = obj.getAsJsonArray("data");

                for(int i = 0; i < arr.size(); i++) {
                    BGEffectSegment segment = new BGEffectSegment(arr.get(i).getAsJsonObject(), jsonName, id);
                    handlers.add(new BGEffectHandler(segment, id));
                }
            } else if (obj.has("id")) {
                if(post) {
                    int efID = obj.get("id").getAsInt();

                    ArrayList<BackgroundEffect> effs = CommonStatic.getBCAssets().bgEffects;

                    for (BackgroundEffect bge : effs)
                        if (bge instanceof JsonBGEffect && ((JsonBGEffect)bge).id == efID) {
                            handlers.addAll(((JsonBGEffect)bge).handlers);
                            break;
                        }
                } else {
                    postNeed = true;
                }
            }
        } catch (Exception ignored) {
            Pattern idExtractor = Pattern.compile("\\{(\\s+)?\"id\"(\\s+)?:(\\s+)?\\d+(\\s+)?}");

            Matcher matcher = idExtractor.matcher(new String(vf.getData().getBytes()));

            while(matcher.find()) {
                if(post) {
                    String group = matcher.group();

                    JsonElement elem = JsonParser.parseString(group);

                    JsonObject obj = elem.getAsJsonObject();

                    if(obj.has("id")) {
                        int efID = obj.get("id").getAsInt();

                        ArrayList<BackgroundEffect> effs = CommonStatic.getBCAssets().bgEffects;

                        for (BackgroundEffect bge : effs)
                            if (bge instanceof JsonBGEffect && ((JsonBGEffect)bge).id == efID) {
                                handlers.addAll(((JsonBGEffect)bge).handlers);
                                break;
                            }
                    } else {
                        throw new IllegalStateException("Unhandled bg effect found for " + jsonName);
                    }
                } else {
                    postNeed = true;
                    break;
                }
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
    public void preDraw(FakeGraphics g, P rect, float siz, float midH) {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).preDraw(g, rect, siz);
        }
    }

    @Override
    public void postDraw(FakeGraphics g, P rect, float siz, float midH) {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).postDraw(g, rect, siz);
        }
    }

    @Override
    public void update(int w, float h, float midH) {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).update(w, h, midH);
        }
    }

    @Override
    public void updateAnimation(int w, float h, float midH) {
        for(int i = 0; i < handlers.size(); i++) {
            handlers.get(i).updateAnimation();
        }
    }

    @Override
    public void initialize(int w, float h, float midH, Background bg) {
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
