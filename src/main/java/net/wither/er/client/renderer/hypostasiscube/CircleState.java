package net.wither.er.client.renderer.hypostasiscube;

import org.joml.Matrix4f;

public class CircleState implements CubeState{
    private final float r;
    private final float y;
    private final float speed;
    private final float theta;
    private static final Matrix4f base = new Matrix4f().rotateZ(0.6545F).rotateX(0.7854F);

    public CircleState(float r, float y, float speed, int theta) {
        this.r = r;
        this.y = y;
        this.speed = speed;
        this.theta = (float) Math.toRadians(theta);
    }

    @Override
    public Matrix4f getByTime(int tick, float dt) {
        float rot = theta + (tick + dt) * speed;
        return new Matrix4f().translate(r * (float) Math.cos(rot), y, r * (float) Math.sin(rot)).rotateY(rot).mul(base);
    }
}
