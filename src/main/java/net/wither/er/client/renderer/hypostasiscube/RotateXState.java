package net.wither.er.client.renderer.hypostasiscube;

import org.joml.Matrix4f;

public class RotateXState implements CubeState{
    private final float x;
    private final float y;
    private final float z;
    private final float rotY;
    private final float speed;
    private final float startTick;
    private final float maxTheta;
    private final float rot0;
    private final float scaleX;
    private final float scaleY;
    private final float scaleZ;

    public RotateXState(float x, float y, float z, float rotY, float speed, float startTick, int maxTheta, int rot0) {
        this(x, y, z, rotY, speed, startTick, maxTheta, rot0, 1, 1, 1);
    }

    public RotateXState(float x, float y, float z, float rotY, float speed, float startTick, int maxTheta, int rot0, float scale) {
        this(x, y, z, rotY, speed, startTick, maxTheta, rot0, scale, scale, scale);
    }

    public RotateXState(float x, float y, float z, float rotY, float speed, float startTick, int maxTheta, int rot0, float scaleX, float scaleY, float scaleZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotY = (float) Math.toRadians(rotY);
        this.speed = speed;
        this.startTick = startTick;
        this.maxTheta = (float) Math.toRadians(maxTheta);
        this.rot0 = (float) Math.toRadians(rot0);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    @Override
    public Matrix4f getByTime(int tick, float dt) {
        float rot = tick >= startTick ? (tick + dt - startTick) * speed + rot0 : rot0;
        if((maxTheta == 0 && ((rot > 0 && rot0 < 0) || (rot < 0 && rot0 > 0))) || (rot < maxTheta && maxTheta < 0) || (rot > maxTheta && maxTheta > 0))
            rot = maxTheta;
        return new Matrix4f().rotateY(rotY).rotateX(rot).translate(x, y, z).scale(scaleX, scaleY, scaleZ);
    }
}
