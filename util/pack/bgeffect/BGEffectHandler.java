package common.util.pack.bgeffect;

import common.pack.UserProfile;
import common.system.BattleRange;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.Data;
import common.util.anim.EAnimD;
import common.util.pack.Background;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class BGEffectHandler {
    private static final P origin = new P(0, 0);

    private final BGEffectSegment segment;
    private final BGEffectAnim[] anims;

    private int count;

    private final List<EAnimD<BGEffectAnim.BGEffType>> animation = new ArrayList<>();

    private P[] position;
    private double[] angle;
    private P[] size;
    private int[] opacity;
    /**
     * false if behind, true if front
     */
    private boolean[] zOrder;

    //this will be null if moveAngle and v aren't null
    private P[] velocity;

    //below two will be null if moveAngle and v are null
    private int[] v;
    private double[] moveAngle;

    private double[] angleVelocity;
    private int[] lifeTime;
    private int[] destroyLeft, destroyTop, destroyRight, destroyBottom;

    private int[] wait;

    private boolean animLoaded = false;

    private final List<Integer> capture = new ArrayList<>();
    private final Random r = new Random();

    public BGEffectHandler(BGEffectSegment segment, int bgId) {
        this.segment = segment;

        if(segment.model != null) {
            Background bg = UserProfile.getBCData().bgs.get(bgId);

            anims = new BGEffectAnim[segment.model.length];

            for(int i = 0; i < segment.model.length; i++) {
                anims[i] = new BGEffectAnim(
                        "./org/img/bg/bg"+Data.trio(bgId)+".png",
                        "./org/battle/bg/bg"+Data.duo(bg.ic)+".imgcut",
                        "./org/battle/bg/bg"+ Data.duo(bg.ic)+"_"+Data.duo(segment.model[i])+".mamodel",
                        "./org/battle/bg/bg"+ Data.duo(bg.ic)+"_"+Data.duo(segment.model[i])+".maanim"
                );
            }
        } else if(segment.files != null) {
            anims = new BGEffectAnim[] {
                    new BGEffectAnim(
                            segment.files[BGEffectSegment.BGFile.IMAGE.ordinal()],
                            segment.files[BGEffectSegment.BGFile.IMGCUT.ordinal()],
                            segment.files[BGEffectSegment.BGFile.MODEL.ordinal()],
                            segment.files[BGEffectSegment.BGFile.ANIME.ordinal()]
                    )
            };
        } else {
            throw new IllegalStateException("Segment has both null model and files variable!");
        }
    }

    public void check() {
        if(!animLoaded) {
            for(int i = 0; i < anims.length; i++) {
                anims[i].load();
            }

            animLoaded = true;
        }
    }

    public void initialize(int w, double h, double midH) {
        if(count != 0) {
            for(int i = 0; i < count; i++) {
                P.delete(position[i]);
                P.delete(size[i]);

                if(velocity != null)
                    P.delete(velocity[i]);
            }
        }

        count = segment.count.getRangeI(w);

        animation.clear();

        position = new P[count];
        angle = new double[count];
        size = new P[count];
        opacity = new int[count];
        zOrder = new boolean[count];

        if(segment.moveAngle != null && segment.velocity != null) {
            velocity = null;
            moveAngle = new double[count];
            v = new int[count];
        } else if(segment.velocityX != null || segment.velocityY != null || segment.startVelocityX != null || segment.startVelocityY != null) {
            velocity = new P[count];
            moveAngle = null;
            v = null;
        } else {
            velocity = null;
            moveAngle = null;
            v = null;
        }

        if(segment.lifeTime != null)
            lifeTime = new int[count];
        else
            lifeTime = null;

        if(segment.destroyLeft != null)
            destroyLeft = new int[count];
        else
            destroyLeft = null;

        if(segment.destroyTop != null)
            destroyTop = new int[count];
        else
            destroyTop = null;

        if(segment.destroyRight != null)
            destroyRight = new int[count];
        else
            destroyRight = null;

        if(segment.destroyBottom != null)
            destroyBottom = new int[count];
        else
            destroyBottom = null;

        if(segment.angleVelocity != null)
            angleVelocity = new double[count];
        else
            angleVelocity = null;

        if(segment.wait != null)
            wait = new int[count];
        else
            wait = null;

        for(int i = 0; i < count; i++) {
            EAnimD<BGEffectAnim.BGEffType> anim = anims[Math.min(anims.length - 1, r.nextInt(anims.length))].getEAnim(BGEffectAnim.BGEffType.DEF);
            anim.removeBasePivot();

            int time = 0;

            if(segment.startFrame != null) {
                time = segment.startFrame.getAnimFrame(anim);
            } else if(segment.frame != null) {
                time = segment.frame.getAnimFrame(anim);
            }

            anim.setTime(time - 1);
            anim.update(false);

            animation.add(anim);

            double x;
            double y;

            if(segment.startX != null) {
                x = segment.startX.getRangeX(w);
            } else {
                x = segment.x.getRangeX(w);
            }

            if(segment.startY != null) {
                y = segment.startY.getRangeY(h, midH);
            } else {
                y = segment.y.getRangeY(h, midH);
            }

            position[i] = P.newP(x, y);

            if(segment.angle != null) {
                angle[i] = segment.angle.getRangeD(w);
            }

            double sx = 1.0;
            double sy = 1.0;

            if(segment.startScale != null) {
                double s = segment.startScale.getRangeD(w);

                sx *= s;
                sy *= s;
            } else if(segment.scale != null) {
                double s = segment.scale.getRangeD(w);

                sx *= s;
                sy *= s;
            }

            if(segment.scaleX != null) {
                sx *= segment.scaleX.getRangeD(w);
            }

            if(segment.scaleY != null) {
                sy *= segment.scaleY.getRangeD(w);
            }

            sx /= animation.get(i).getBaseSizeX();
            sy /= animation.get(i).getBaseSizeY();

            size[i] = P.newP(sx, sy);

            if(segment.opacity != null) {
                opacity[i] = (int) (segment.opacity.getRangeD(w));
            } else {
                opacity[i] = 255;
            }

            zOrder[i] = segment.zOrder.isFront();

            if(velocity != null) {
                double vx;
                double vy;

                if(segment.startVelocityX != null) {
                    vx = segment.startVelocityX.getRangeD(w);
                } else if(segment.velocityX != null) {
                    vx = segment.velocityX.getRangeD(w);
                } else {
                    vx = 0;
                }

                if(segment.startVelocityY != null) {
                    vy = segment.startVelocityY.getRangeD(w);
                } else if(segment.velocityY != null) {
                    vy = segment.velocityY.getRangeD(w);
                } else {
                    vy = 0;
                }

                velocity[i] = P.newP(vx, vy);
            } else if(v != null && moveAngle != null) {
                if(segment.moveAngle != null) {
                    moveAngle[i] = segment.moveAngle.getRangeD(w);
                } else {
                    moveAngle[i] = 0;
                }

                if(segment.startVelocity != null) {
                    v[i] = segment.startVelocity.getRangeI(w);
                } else if(segment.velocity != null) {
                    v[i] = segment.velocity.getRangeI(w);
                } else {
                    v[i] = 0;
                }
            }

            if(segment.lifeTime != null) {
                lifeTime[i] = segment.lifeTime.getAnimFrame(animation.get(i));

                if(lifeTime[i] == 0)
                    lifeTime[i] = -1;
                else
                    lifeTime[i] = Math.max(1, lifeTime[i] - time);
            }

            if(segment.destroyLeft != null) {
                destroyLeft[i] = (int) segment.destroyLeft.getRangeX(w);
            }

            if(segment.destroyTop != null) {
                destroyTop[i] = (int) segment.destroyTop.getRangeY(h, midH);
            }

            if(segment.destroyRight != null) {
                destroyRight[i] = (int) segment.destroyRight.getRangeX(w);
            }

            if(segment.destroyBottom != null) {
                destroyBottom[i] = (int) segment.destroyBottom.getRangeY(h, midH);
            }

            if(segment.angleVelocity != null) {
                angleVelocity[i] = segment.angleVelocity.getRangeD(w);
            }
        }
    }

    public void update(int w, double h, double midH) {
        capture.clear();

        for(int i = 0; i < count; i++) {
            if(wait != null && wait[i] != 0) {
                wait[i]--;

                if(wait[i] == 0) {
                    EAnimD<BGEffectAnim.BGEffType> anim = anims[Math.min(anims.length - 1, r.nextInt(anims.length))].getEAnim(BGEffectAnim.BGEffType.DEF);
                    anim.removeBasePivot();

                    animation.set(i, anim);

                    if(segment.frame != null) {
                        int time = segment.frame.getAnimFrame(anim);

                        anim.setTime(time - 1);
                        anim.update(false);

                        reInitialize(i, w, h, midH, time);
                    } else {
                        reInitialize(i, w, h, midH, 0);
                    }
                }
            } else {
                if(checkDestroy(i)) {
                    capture.add(i);
                } else {
                    animation.get(i).update(false);

                    if(v != null && moveAngle != null) {
                        position[i].x += v[i] * Math.cos(moveAngle[i]);
                        position[i].y += v[i] * Math.sin(moveAngle[i]);
                    } else if(velocity != null) {
                        position[i].x += velocity[i].x;
                        position[i].y += velocity[i].y;
                    }

                    if(angleVelocity != null) {
                        angle[i] += angleVelocity[i];
                    }

                    if(lifeTime != null) {
                        lifeTime[i]--;
                    }
                }
            }
        }

        if(!capture.isEmpty()) {
            for(int i = 0; i < capture.size(); i++) {
                int ind = capture.get(i);

                if(segment.wait != null) {
                    wait[ind] = segment.wait.getPureRangeI();
                } else {
                    EAnimD<BGEffectAnim.BGEffType> anim = anims[Math.min(anims.length - 1, r.nextInt(anims.length))].getEAnim(BGEffectAnim.BGEffType.DEF);
                    anim.removeBasePivot();

                    animation.set(ind, anim);

                    if(segment.frame != null) {
                        int time = segment.frame.getAnimFrame(anim);

                        anim.setTime(time - 1);
                        anim.update(false);

                        reInitialize(ind, w, h, midH, time);
                    } else {
                        reInitialize(ind, w, h, midH, 0);
                    }
                }
            }
        }
    }

    public void preDraw(FakeGraphics g, P rect, double siz) {
        FakeTransform at = g.getTransform();

        for(int i = 0; i < count; i++) {
            if(!zOrder[i]) {
                EAnimD<BGEffectAnim.BGEffType> anim = animation.get(i);

                g.translate(convertP(position[i].x, siz) + rect.x, convertP(position[i].y, siz) - rect.y);
                g.rotate(angle[i]);
                anim.drawBGEffect(g, origin, siz * 0.8, opacity[i], size[i].x, size[i].y);
            }

            g.setTransform(at);
        }

        g.delete(at);
    }

    public void postDraw(FakeGraphics g, P rect, double siz) {
        FakeTransform at = g.getTransform();

        for(int i = 0; i < count; i++) {
            if(zOrder[i]) {
                EAnimD<BGEffectAnim.BGEffType> anim = animation.get(i);

                g.translate(convertP(position[i].x, siz) + rect.x, convertP(position[i].y, siz) - rect.y);
                g.rotate(angle[i]);
                anim.drawBGEffect(g, origin, siz * 0.8, opacity[i], size[i].x, size[i].y);
            }

            g.setTransform(at);
        }

        g.delete(at);
    }

    private boolean checkDestroy(int index) {
        if(lifeTime != null && lifeTime[index] == 0)
            return true;

        if(destroyLeft != null && position[index].x < destroyLeft[index])
            return true;

        if(destroyTop != null && position[index].y < destroyTop[index])
            return true;

        if(destroyRight != null && position[index].x > destroyRight[index])
            return true;

        return destroyBottom != null && position[index].y > destroyBottom[index];
    }

    /**
     * Convert battle unit to pixel unit
     * @param p Position in battle
     * @param siz Size of battle
     * @return Converted pixel
     */
    private int convertP(double p, double siz) {
        return (int) (p * BattleRange.battleRatio * siz);
    }

    private void reInitialize(int ind, int w, double h, double midH, int time) {
        position[ind].x = segment.x.getRangeX(w);
        position[ind].y = segment.y.getRangeY(h, midH);

        size[ind].x = 1.0 / animation.get(ind).getBaseSizeX();
        size[ind].y = 1.0 / animation.get(ind).getBaseSizeY();

        if(segment.scale != null) {
            double s = segment.scale.getRangeD(w);

            size[ind].x *= s;
            size[ind].y *= s;
        }

        if(segment.scaleX != null) {
            size[ind].x *= segment.scaleX.getRangeD(w);
        }

        if(segment.scaleY != null) {
            size[ind].y *= segment.scaleY.getRangeD(w);
        }

        if(segment.opacity != null) {
            opacity[ind] = (int) (segment.opacity.getRangeD(w));
        }

        zOrder[ind] = segment.zOrder.isFront();

        if(velocity != null) {
            if(segment.startVelocityX != null && segment.velocityX == null) {
                velocity[ind].x = segment.startVelocityX.getRangeD(w);
            } else if(segment.velocityX != null) {
                velocity[ind].x = segment.velocityX.getRangeD(w);
            } else {
                velocity[ind].x = 0;
            }

            if(segment.startVelocityY != null && segment.velocityY == null) {
                velocity[ind].y = segment.startVelocityY.getRangeD(w);
            } else if(segment.velocityY != null) {
                velocity[ind].y = segment.velocityY.getRangeD(w);
            } else {
                velocity[ind].y = 0;
            }
        } else if(v != null && moveAngle != null) {
            if(segment.moveAngle != null) {
                moveAngle[ind] = segment.moveAngle.getRangeD(w);
            } else {
                moveAngle[ind] = 0;
            }

            if(segment.velocity != null) {
                v[ind] = segment.velocity.getRangeI(w);
            } else {
                v[ind] = 0;
            }
        }

        if(segment.lifeTime != null) {
            int l = segment.lifeTime.getAnimFrame(animation.get(ind));

            lifeTime[ind] = Math.max(0,l - time);

            if(lifeTime[ind] == 0)
                lifeTime[ind] = -1;
        }

        if(segment.destroyLeft != null) {
            destroyLeft[ind] = (int) segment.destroyLeft.getRangeX(w);
        }

        if(segment.destroyTop != null) {
            destroyTop[ind] = (int) segment.destroyTop.getRangeY(h, midH);
        }

        if(segment.destroyRight != null) {
            destroyRight[ind] = (int) segment.destroyRight.getRangeX(w);
        }

        if(segment.destroyBottom != null) {
            destroyBottom[ind] = (int) segment.destroyBottom.getRangeY(h, midH);
        }

        if(segment.angleVelocity != null) {
            angleVelocity[ind] = segment.angleVelocity.getRangeD(w);
        }
    }

    public void release() {
        animation.clear();

        count = 0;
    }
}
