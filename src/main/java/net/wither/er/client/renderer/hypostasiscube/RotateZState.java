package net.wither.er.client.renderer.hypostasiscube;

import org.joml.Matrix4f;

public class RotateZState implements CubeState{
    private final float r;
    private final float z;
    private final float speed;
    private final float theta0;
    private final float rot0;
    private final float rotX;
    private final float scale;

    public RotateZState(float r, float z, float speed, int theta0, int rot0, int rotX, float scale) {
        this.r = r;
        this.z = z;
        this.speed = speed;
        this.theta0 = (float) Math.toRadians(theta0 * 90);
        this.rot0 = (float) Math.toRadians(rot0);
        this.rotX = (float) Math.toRadians(rotX);
        this.scale = scale;
    }

    @Override
    public Matrix4f getByTime(int tick, float dt) {
        float rot = (tick + dt) * speed + rot0 ;
        return new Matrix4f().rotateZ(rot).translate(r, 0 , z).rotateY(theta0).rotateY(rotX).scale(scale);
    }
}
