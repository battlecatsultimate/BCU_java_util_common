package common.util.pack.bgeffect;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.battle.StageBasis;
import common.system.BattleRange;
import common.system.P;
import common.system.fake.FakeGraphics;

import java.util.function.Function;

public class BGEffectSegment extends BackgroundEffect {

    /**
     * Number of values which will be used in mamodel
     */
    public int[] model;
    /**
     * Number of components which have to be generated
     */
    public int count;
    /**
     * Position where component will be drawn. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public BattleRange<Integer> x;
    public BattleRange<Integer> y;
    /**
     * The Z-Order where the component will be drawn.
     */
    public BattleRange<Integer> zOrder;
    /**
     * The Size of the component to draw. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public BattleRange<Double> scaleX;
    public BattleRange<Double> scaleY;
    /**
     * The Angle of the component to draw. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public BattleRange<Double> angle;
    /**
     * Frame this component will start to appear.
     */
    public int startFrame, frame;

    public BattleRange<Integer> wait;

    public BattleRange<Integer> lifeTime;

    /**
     * The speed this component will rotate. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public BattleRange<Double> angleVelocity;
    /**
     * The speed this component will move. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public BattleRange<Integer> velocityX;
    public BattleRange<Integer> velocityY;
    /**
     * If the component goes beyond these values' direction, it will be destroyed.
     */
    public int destroyTop, destroyBottom, destroyLeft, destroyRight;
    /**
     * The size this component when made. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    BattleRange<Double> startScale;
    /**
     * Initial X position. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    BattleRange<Integer> startX;
    /**
     * Initial Y position. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    BattleRange<Integer> startY;
    /**
     * Initial X velocity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    BattleRange<Integer> startVelocityX;
    /**
     * Initial Y velocity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    BattleRange<Integer> startVelocityY;
    /**
     * Initial velocity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    BattleRange<Integer> startVelocity;
    /**
     * The component will be destroyed if it stays on the field longer than this time.
     */
    public int lifetime;
    /**
     * Component's velocity.
     */
    public int velocity;
    /**
     * ???
     */
    BattleRange<Double> moveAngle;
    /**
     * Component's opacity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    BattleRange<Integer> opacity;
    /**
     * Frame this component's animation will start at.
     */
    public int animStartFrame;

    public BGEffectSegment(JsonObject elem) {
        if(elem.has("model")) {
            JsonArray modelArray = elem.getAsJsonArray("model");

            model = new int[modelArray.size()];

            for(int i = 0; i < modelArray.size(); i++) {
                model[i] = modelArray.get(i).getAsInt();
            }
        }

        if(elem.has("count")) {
            JsonObject countObject = elem.getAsJsonObject("count");

            count = countObject.get("value").getAsInt();
        } else {
            count = 1;
        }

        if(elem.has("x")) {
            x = readRangedJsonObjectI(elem, "x");
        }

        if(elem.has("y")) {
            y = readRangedJsonObjectI(elem, "y");
        }

        if(elem.has("z")) {
            zOrder = readRangedJsonObjectI(elem, "z");
        }

        if(elem.has("frame")) {
            JsonObject frameObject = elem.getAsJsonObject("frame");

            if(frameObject.has("value")) {
                frame = frameObject.get("value").getAsInt();
            } else {
                throw new IllegalStateException("Unhandled frame data found : "+frameObject);
            }
        }

        if(elem.has("wait")) {
            wait = readRangedJsonObjectI(elem, "wait");
        }

        if(elem.has("lifeTime")) {
            lifeTime = readRangedJsonObjectI(elem, "lifeTime");
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
        //TODO Set codes to determine current position and current part lifetime
    }

    @Override
    public void initialize(StageBasis sb) {

    }

    private BattleRange.SNAP getSnap(String base) {
        switch (base) {
            case "worldLeft":
                return BattleRange.SNAP.LEFT;
            case "worldRight":
                return BattleRange.SNAP.RIGHT;
            case "worldTop":
                return BattleRange.SNAP.TOP;
            case "worldBottom":
                return BattleRange.SNAP.BOTTOM;
            case "frontChara":
                return BattleRange.SNAP.FRONT;
            case "backChara":
                return BattleRange.SNAP.BACK;
            case "animeInterval":
                return BattleRange.SNAP.INTERVAL;
            default:
                throw new IllegalStateException("Unknown base type found : "+base);
        }
    }

    private BattleRange<Integer> readRangedJsonObjectI(JsonObject obj, String mainKeyword) {
        if(obj.has(mainKeyword)) {
            JsonObject xObject = obj.getAsJsonObject(mainKeyword);

            int min;
            BattleRange.SNAP minSnap;
            int max;
            BattleRange.SNAP maxSnap;

            if(xObject.has("min") && xObject.has("max")) {
                if (xObject.has("min")) {
                    JsonObject xMinObject = xObject.getAsJsonObject("min");

                    min = xMinObject.get("value").getAsInt();

                    if (xMinObject.has("base")) {
                        minSnap = getSnap(xMinObject.get("base").getAsString());
                    } else {
                        minSnap = null;
                    }
                } else {
                    min = 0;
                    minSnap = null;
                }

                if (xObject.has("max")) {
                    JsonObject xMaxObject = xObject.getAsJsonObject("max");

                    max = xMaxObject.get("value").getAsInt();

                    if (xMaxObject.has("base")) {
                        maxSnap = getSnap(xMaxObject.get("base").getAsString());
                    } else {
                        maxSnap = null;
                    }
                } else {
                    max = 0;
                    maxSnap = null;
                }
            } else if(xObject.has("value")) {
                min = max = xObject.get("value").getAsInt();

                if(xObject.has("base")) {
                    minSnap = maxSnap = getSnap(xObject.get("base").getAsString());
                } else {
                    minSnap = maxSnap = null;
                }
            } else {
                throw new IllegalStateException("Unhandled situation while reading bg effect! | Caused while reading x : "+ xObject);
            }

            return new BattleRange<>(min, minSnap, max, maxSnap);
        }

        return null;
    }

    private BattleRange<Double> readRangedJsonObjectD(JsonObject obj, String mainKeyword, Function<Integer, Double> func) {
        if(obj.has(mainKeyword)) {
            JsonObject xObject = obj.getAsJsonObject(mainKeyword);

            double min;
            BattleRange.SNAP minSnap;
            double max;
            BattleRange.SNAP maxSnap;

            if(xObject.has("min") && xObject.has("max")) {
                if (xObject.has("min")) {
                    JsonObject xMinObject = xObject.getAsJsonObject("min");

                    min = func.apply(xMinObject.get("value").getAsInt());

                    if (xMinObject.has("base")) {
                        minSnap = getSnap(xMinObject.get("base").getAsString());
                    } else {
                        minSnap = null;
                    }
                } else {
                    min = 0;
                    minSnap = null;
                }

                if (xObject.has("max")) {
                    JsonObject xMaxObject = xObject.getAsJsonObject("max");

                    max = func.apply(xMaxObject.get("value").getAsInt());

                    if (xMaxObject.has("base")) {
                        maxSnap = getSnap(xMaxObject.get("base").getAsString());
                    } else {
                        maxSnap = null;
                    }
                } else {
                    max = 0;
                    maxSnap = null;
                }
            } else if(xObject.has("value")) {
                min = max = func.apply(xObject.get("value").getAsInt());

                if(xObject.has("base")) {
                    minSnap = maxSnap = getSnap(xObject.get("base").getAsString());
                } else {
                    minSnap = maxSnap = null;
                }
            } else {
                throw new IllegalStateException("Unhandled situation while reading bg effect! | Caused while reading x : "+ xObject);
            }

            return new BattleRange<>(min, minSnap, max, maxSnap);
        }

        return null;
    }
}
