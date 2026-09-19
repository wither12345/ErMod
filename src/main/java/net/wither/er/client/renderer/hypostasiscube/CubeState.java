package net.wither.er.client.renderer.hypostasiscube;

import org.joml.Matrix4f;

public interface CubeState {
    Matrix4f getByTime(int tick, float dt);

    default boolean shouldRender(int tick, float dt){
        return true;
    }

    default boolean shouldRenderAfter(){
        return true;
    }
}
