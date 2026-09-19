package net.wither.er.client.renderer.hypostasiscube;

import org.joml.Matrix4f;

import java.util.List;

record SimpleStates(List<Matrix4f> states, int tickPerAnimation, int size, int totalAnimation, int waitingTick, boolean keepLast, boolean remove) implements CubeState{
    public SimpleStates(List<Matrix4f> states, int tickPerAnimation) {
        this(states, tickPerAnimation, states.size(), states.size() * tickPerAnimation, 0, false, false);
    }

    public SimpleStates(List<Matrix4f> states, int tickPerAnimation, int waitingTick, boolean remove) {
        this(states, tickPerAnimation, states.size(), (states.size() - 1) * tickPerAnimation, waitingTick, true, remove);
    }

    @Override
    public Matrix4f getByTime(int tick, float dt) {
        if (this.size == 1 || tick < waitingTick)
            return new Matrix4f(this.states.get(0));
        if(tick - waitingTick + dt >= totalAnimation && keepLast)
            return new Matrix4f(states.get(size - 1));
        int nowTime = (tick - waitingTick) % totalAnimation;
        int indexPre = nowTime / tickPerAnimation;
        float per = (nowTime % tickPerAnimation + dt) / tickPerAnimation;
        int indexPost = indexPre + 1;
        if (indexPost >= size)
            indexPost = 0;
        return HypostasisCubeState.mix(states.get(indexPre), states.get(indexPost), per);
    }

    @Override
    public boolean shouldRender(int tick, float dt) {
        return tick < this.totalAnimation + this.waitingTick || !remove;
    }

    @Override
    public boolean shouldRenderAfter() {
        return !remove;
    }
}
