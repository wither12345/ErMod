package net.wither.er.client.renderer.hypostasiscube;

import org.joml.Matrix4f;

public class RotateYState implements CubeState{
    private final float rotX0;
    private final float x;
    private final float y;
    private final float z;
    private final float speed;
    private final float startTick;
    private final float maxTheta;
    private final float theta0;
    private final float rot0;
    private final float rotX;

    public RotateYState(float rotX0, float x, float y, float speed, float startTick, int maxTheta, int theta0) {
        this.rotX0 = rotX0;
        this.x = x;
        this.y = y;
        this.z = 0;
        this.speed = speed;
        this.startTick = startTick;
        this.maxTheta = (float) Math.toRadians(maxTheta);
        this.theta0 = (float) Math.toRadians(theta0 * 90);
        this.rot0 = 0;
        this.rotX = 0;
    }

    public RotateYState(float rotX0, float x, float y, float z, float speed, float startTick, int maxTheta, int theta0, int rot0, int rotX) {
        this.rotX0 = rotX0;
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;
        this.startTick = startTick;
        this.maxTheta = (float) Math.toRadians(maxTheta);
        this.theta0 = (float) Math.toRadians(theta0 * 90);
        this.rot0 = (float) Math.toRadians(rot0);
        this.rotX = (float) Math.toRadians(rotX);
    }

    @Override
    public Matrix4f getByTime(int tick, float dt) {
        float rot = tick >= startTick ? (tick + dt - startTick) * speed + rot0 : rot0;
        if((maxTheta == 0 && ((rot > 0 && rot0 < 0) || (rot < 0 && rot0 > 0))) || (rot < maxTheta && maxTheta < 0) || (rot > maxTheta && maxTheta > 0))
            rot = maxTheta;
        return new Matrix4f().rotateX(rotX).translate(rotX0, 0 , 0).rotateY(-rot).translate(x, y, z).rotateY(theta0);
    }
}
