package common.util.pack.bgeffect;

import common.battle.StageBasis;
import common.system.P;
import common.system.fake.FakeGraphics;

public class BGEffectSegment extends BackgroundEffect {
    /**
     * Number of components which have to be generated
     */
    public int count;
    /**
     * Position where component will be drawn. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minX, maxX, minY, maxY;
    /**
     * The Z-Order where the component will be drawn.
     */
    public int zOrder;
    /**
     * The Size of the component to draw. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minScale, maxScale, minScaleX, maxScaleX, minScaleY, maxScaleY;
    /**
     * The Angle of the component to draw. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minAngle, maxAngle;
    /**
     * Frame this component will start to appear.
     */
    public int startFrame;
    /**
     * The speed this component will rotate. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minRotationVelocity, maxRotationVelocity;
    /**
     * The speed this component will move. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minXMoveVelocity, maxXMoveVelocity, minYMoveVelocity, maxYMoveVelocity;
    /**
     * If the component goes beyond these values' direction, it will be destroyed.
     */
    public int destroyTop, destroyBottom, destroyLeft, destroyRight;
    /**
     * The size this component when made. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minStartScale, maxStartScale;
    /**
     * Initial X position. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minStartX, maxStartX;
    /**
     * Initial Y position. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minStartY, maxStartY;
    /**
     * Initial X velocity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minStartVelocityX, maxStartVelocityX;
    /**
     * Initial Y velocity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minStartVelocityY, maxStartVelocityY;
    /**
     * Initial velocity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minStartVelocity, maxStartVelocity;
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
    public int minMoveAngle, maxMoveAngle;
    /**
     * Component's opacity. If there is a maximum value, the value will be a random number between the minimum and maximum value.
     */
    public int minOpacity, maxOpacity;
    /**
     * Frame this component's animation will start at.
     */
    public int animStartFrame;

    @Override
    public void check() {

    }

    @Override
    public void preDraw(FakeGraphics g, P rect, double siz) {

    }

    @Override
    public void postDraw(FakeGraphics g, P rect, double siz) {

    }

    @Override
    public void update(StageBasis sb) {
        //TODO Set codes to determine current position and current part lifetime
    }

    @Override
    public void initialize(StageBasis sb) {

    }
}
