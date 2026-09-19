package net.wither.er.client.renderer.hypostasiscube;

import org.joml.Matrix4f;

record SimpleState(Matrix4f state) implements CubeState{
    @Override
    public Matrix4f getByTime(int tick, float dt) {
        return new Matrix4f(this.state);
    }
}
