package common.util.pack.bgeffect;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.system.BattleRange;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class BGEffectSegment {
    public enum BGFile {
        IMAGE,
        IMGCUT,
        MODEL,
        ANIME
    }

    public String name = "";
    public final String json;

    /**
     * Animation file
     */
    public final int[] model;

    /**
     * Animation file, but with specified file name
     */
    public final String[] files;

    /**
     * Number of components which have to be generated
     */
    public final BattleRange<Integer> count;
    /**
     * Position where component will be drawn. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public final BattleRange<Integer> x;
    public final BattleRange<Integer> y;
    /**
     * The Z-Order where the component will be drawn.
     */
    public final BattleRange<Integer> zOrder;
    /**
     * The Size of the component to draw. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public BattleRange<Double> scale;
    public BattleRange<Double> scaleX;
    public BattleRange<Double> scaleY;
    /**
     * The Angle of the component to draw. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public BattleRange<Double> angle;
    /**
     * Frame this component will start to appear.
     */
    public final BattleRange<Integer> startFrame;

    public final BattleRange<Integer> frame;

    public final BattleRange<Integer> wait;

    /**
     * The speed this component will rotate. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public BattleRange<Double> angleVelocity;
    /**
     * Component's velocity.
     */
    public final BattleRange<Double> velocity;
    /**
     * The speed this component will move. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public final BattleRange<Double> velocityX;
    public final BattleRange<Double> velocityY;
    /**
     * If the component goes beyond these values' direction, it will be destroyed.
     */
    public BattleRange<Integer> destroyTop, destroyBottom, destroyLeft, destroyRight;
    /**
     * The size this component when made. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public final BattleRange<Double> startScale;
    /**
     * Initial X position. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public final BattleRange<Integer> startX;
    /**
     * Initial Y position. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public final BattleRange<Integer> startY;
    /**
     * Initial X velocity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public final BattleRange<Double> startVelocityX;
    /**
     * Initial Y velocity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public final BattleRange<Double> startVelocityY;
    /**
     * Initial velocity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public final BattleRange<Double> startVelocity;
    /**
     * The component will be destroyed if it stays on the field longer than this time.
     */
    public final BattleRange<Integer> lifeTime;
    /**
     * ???
     */
    public final BattleRange<Double> moveAngle;
    /**
     * Component's opacity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public final BattleRange<Integer> opacity;

    public BGEffectSegment(JsonObject elem, String json) {
        this.json = json;

        if(elem.has("name")) {
            name = elem.get("name").getAsString();
        }

        if(elem.has("model")) {
            JsonObject modelObject = elem.getAsJsonObject("model");

            if(modelObject.has("values")) {
                JsonArray modelArray = modelObject.getAsJsonArray("values");

                model = new int[modelArray.size()];

                for(int i = 0; i < modelArray.size(); i++) {
                    model[i] = modelArray.get(i).getAsInt();
                }
            } else if(modelObject.has("value")) {
                model = new int[] {modelObject.get("value").getAsInt()};
            } else {
                System.out.println("W/BGEffectSegment | "+ json +"/ model has weird data type : \"model\" : "+modelObject);
                model = null;
            }
        } else {
            model = null;
        }

        if(elem.has("file")) {
            JsonObject fileObject = elem.getAsJsonObject("file");

            files = new String[BGFile.values().length];

            if(fileObject.has("image")) {
                files[BGFile.IMAGE.ordinal()] = "./org/img/bgEffect/"+fileObject.get("image").getAsString();
            }

            if(fileObject.has("imgcut")) {
                files[BGFile.IMGCUT.ordinal()] = "./org/battle/bg/"+fileObject.get("imgcut").getAsString();
            }

            if(fileObject.has("model")) {
                files[BGFile.MODEL.ordinal()] = "./org/battle/bg/"+fileObject.get("model").getAsString();
            }

            if(fileObject.has("anime")) {
                files[BGFile.ANIME.ordinal()] = "./org/battle/bg/"+fileObject.get("anime").getAsString();
            }

            for (String file : files) {
                if (file == null) {
                    throw new IllegalStateException("File name isn't fully specified! : " + fileObject);
                }
            }
        } else {
            files = null;
        }

        if(files == null && model == null) {
            throw new IllegalStateException("Unhandled file/model data found, both are null");
        } else if(files != null && model != null) {
            throw new IllegalStateException("Unhandled file/model data found, both aren't null");
        }

        if(elem.has("count")) {
            count = readRangedJsonObjectI(elem, "count");
        } else {
            count = new BattleRange<>(1, null, 1, null);
        }

        if(elem.has("x")) {
            x = readRangedJsonObjectI(elem, "x", i -> i*4);
        } else {
            x = new BattleRange<>(0, null, 0, null);
        }

        if(elem.has("y")) {
            y = readRangedJsonObjectI(elem, "y", i -> i*4);
        } else {
            y = new BattleRange<>(0, null, 0, null);
        }

        if(elem.has("startX")) {
            startX = readRangedJsonObjectI(elem, "startX", i -> i*4);
        } else {
            startX = null;
        }

        if(elem.has("startY")) {
            startY = readRangedJsonObjectI(elem, "startY", i -> i*4);
        } else {
            startY = null;
        }

        if(elem.has("z")) {
            zOrder = readRangedJsonObjectI(elem, "z");
        } else {
            zOrder = new BattleRange<>(0, BattleRange.SNAP.BACK, 0, BattleRange.SNAP.BACK);
        }

        if(elem.has("angle")) {
            angle = readRangedJsonObjectD(elem, "angle", Math::toRadians);
        } else {
            angle = null;
        }

        if(elem.has("scale")) {
            scale = readRangedJsonObjectD(elem, "scale");
        } else {
            scale = null;
        }

        if(elem.has("scaleX")) {
            scaleX = readRangedJsonObjectD(elem, "scaleX");
        } else {
            scaleX = null;
        }

        if(elem.has("scaleY")) {
            scaleY = readRangedJsonObjectD(elem, "scaleY");
        } else {
            scaleY = null;
        }

        if(elem.has("startFrame")) {
            startFrame = readRangedJsonObjectI(elem, "startFrame");
        } else {
            startFrame = null;
        }

        if(elem.has("frame")) {
            frame = readRangedJsonObjectI(elem, "frame");
        } else {
            frame = null;
        }

        if(elem.has("wait")) {
            wait = readRangedJsonObjectI(elem, "wait");
        } else {
            wait = null;
        }

        if(elem.has("lifeTime")) {
            lifeTime = readRangedJsonObjectI(elem, "lifeTime");
        } else {
            lifeTime = null;
        }

        if(elem.has("startScale")) {
            startScale = readRangedJsonObjectD(elem, "startScale");

            if(elem.getAsJsonObject("startScale").has("randGroup")) {
                System.out.println("W/BGEffectSegment | "+json+" / Random group found in start scale -> startScale : "+elem.getAsJsonObject("startScale").get("randGroup").getAsString());
            }
        } else {
            startScale = null;
        }

        if(elem.has("v")) {
            velocity = readRangedJsonObjectD(elem, "v", d -> d * 4.0);
        } else {
            velocity = null;
        }

        if(elem.has("vx")) {
            velocityX = readRangedJsonObjectD(elem, "vx", d -> d * 4.0);
        } else {
            velocityX = null;
        }

        if(elem.has("vy")) {
            velocityY = readRangedJsonObjectD(elem, "vy", d -> d * 4.0);
        } else {
            velocityY = null;
        }

        if(elem.has("startV")) {
            startVelocity = readRangedJsonObjectD(elem, "startV", d -> d * 4.0);
        } else {
            startVelocity = null;
        }

        if(elem.has("startVx")) {
            startVelocityX = readRangedJsonObjectD(elem, "startVx", d -> d * 4.0);

            if(elem.getAsJsonObject("startVx").has("randGroup")) {
                System.out.println("W/BGEffectSegment | "+json+" / Random group found in start velocity x -> startVx : "+elem.getAsJsonObject("startVx").get("randGroup").getAsString());
            }
        } else {
            startVelocityX = null;
        }

        if(elem.has("startVy")) {
            startVelocityY = readRangedJsonObjectD(elem, "startVy", d -> d * 4.0);

            if(elem.getAsJsonObject("startVy").has("randGroup")) {
                System.out.println("W/BGEffectSegment | "+json+" / Random group found in start velocity y -> startVy : "+elem.getAsJsonObject("startVy").get("randGroup").getAsString());
            }
        } else {
            startVelocityY = null;
        }

        if(elem.has("moveAngle")) {
            moveAngle = readRangedJsonObjectD(elem, "moveAngle", Math::toRadians);

            if(velocity == null) {
                System.out.println("W/BGEffectSegment | "+json+" / Non-defined velocity data found while moveAngle is defined -> vx == null : "+(velocityX == null)+" | vy == null : "+(velocityY == null));
            }
        } else {
            moveAngle = null;
        }

        if(elem.has("alpha")) {
            opacity = readRangedJsonObjectI(elem, "alpha");
        } else {
            opacity = null;
        }

        if(elem.has("destroyLeft")) {
            destroyLeft = readRangedJsonObjectI(elem, "destroyLeft");
        } else {
            destroyLeft = null;
        }

        if(elem.has("destroyTop")) {
            destroyTop = readRangedJsonObjectI(elem, "destroyTop");
        } else {
            destroyTop = null;
        }

        if(elem.has("destroyRight")) {
            destroyRight = readRangedJsonObjectI(elem, "destroyRight");
        } else {
            destroyRight = null;
        }

        if(elem.has("destroyBottom")) {
            destroyBottom = readRangedJsonObjectI(elem, "destroyBottom");
        } else {
            destroyBottom = null;
        }

        if(elem.has("angularV")) {
            angleVelocity = readRangedJsonObjectD(elem, "angularV", Math::toRadians);
        } else {
            angleVelocity = null;
        }
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
            case "animeLength":
                return BattleRange.SNAP.LENGTH;
            case "secondToFrame":
                return BattleRange.SNAP.SECOND;
            case "percentToFloat":
                return BattleRange.SNAP.PERCENT;
            default:
                throw new IllegalStateException("Unknown base type found in " + json + " : "+base);
        }
    }

    @Nonnull
    private BattleRange<Integer> readRangedJsonObjectI(JsonObject obj, String mainKeyword) {
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

    @Nonnull
    private BattleRange<Integer> readRangedJsonObjectI(JsonObject obj, String mainKeyword, Function<Integer, Integer> func) {
        JsonObject xObject = obj.getAsJsonObject(mainKeyword);

        int min;
        BattleRange.SNAP minSnap;
        int max;
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

    private BattleRange<Double> readRangedJsonObjectD(JsonObject obj, String mainKeyword) {
        JsonObject xObject = obj.getAsJsonObject(mainKeyword);

        double min;
        BattleRange.SNAP minSnap;
        double max;
        BattleRange.SNAP maxSnap;

        if(xObject.has("min") && xObject.has("max")) {
            if (xObject.has("min")) {
                JsonObject xMinObject = xObject.getAsJsonObject("min");

                min = xMinObject.get("value").getAsDouble();

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

                max = xMaxObject.get("value").getAsDouble();

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
            min = max = xObject.get("value").getAsDouble();

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

    private BattleRange<Double> readRangedJsonObjectD(JsonObject obj, String mainKeyword, Function<Integer, Double> func) {
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
}
