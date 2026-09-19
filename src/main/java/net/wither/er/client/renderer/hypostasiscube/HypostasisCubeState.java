package net.wither.er.client.renderer.hypostasiscube;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.FriendlyByteBuf;
import net.wither.er.entity.hypostasiscube.HypostasisCube;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public enum HypostasisCubeState {
    NORMAL(Map.of(
            0, new SimpleStates(List.of(
                    new Matrix4f().translate(0.6f, 0.6f, 0.6f),
                    new Matrix4f().translate(0.7f, 0.7f, 0.7f)
            ), 40),
            1, new SimpleStates(List.of(
                    new Matrix4f().translate(0.6f, 0.6f, -0.6f).rotateY((float) Math.toRadians(90)),
                    new Matrix4f().translate(0.7f, 0.7f, -0.7f).rotateY((float) Math.toRadians(90))
            ), 40),
            2, new SimpleStates(List.of(
                    new Matrix4f().translate(-0.6f, 0.6f, -0.6f).rotateY((float) Math.toRadians(180)),
                    new Matrix4f().translate(-0.7f, 0.7f, -0.7f).rotateY((float) Math.toRadians(180))
            ), 40),
            3, new SimpleStates(List.of(
                    new Matrix4f().translate(-0.6f, 0.6f, 0.6f).rotateY((float) Math.toRadians(270)),
                    new Matrix4f().translate(-0.7f, 0.7f, 0.7f).rotateY((float) Math.toRadians(270))
            ), 40),
            4, new SimpleStates(List.of(
                    new Matrix4f().translate(-0.6f, -0.6f, 0.6f).rotateZ((float) Math.toRadians(180)),
                    new Matrix4f().translate(-0.7f, -0.7f, 0.7f).rotateZ((float) Math.toRadians(180))
            ), 40),
            5, new SimpleStates(List.of(
                    new Matrix4f().translate(-0.6f, -0.6f, -0.6f).rotateZ((float) Math.toRadians(180)).rotateY((float) Math.toRadians(90)),
                    new Matrix4f().translate(-0.7f, -0.7f, -0.7f).rotateZ((float) Math.toRadians(180)).rotateY((float) Math.toRadians(90))
            ), 40),
            6, new SimpleStates(List.of(
                    new Matrix4f().translate(0.6f, -0.6f, -0.6f).rotateZ((float) Math.toRadians(180)).rotateY((float) Math.toRadians(180)),
                    new Matrix4f().translate(0.7f, -0.7f, -0.7f).rotateZ((float) Math.toRadians(180)).rotateY((float) Math.toRadians(180))
            ), 40),
            7, new SimpleStates(List.of(
                    new Matrix4f().translate(0.6f, -0.6f, 0.6f).rotateZ((float) Math.toRadians(180)).rotateY((float) Math.toRadians(270)),
                    new Matrix4f().translate(0.7f, -0.7f, 0.7f).rotateZ((float) Math.toRadians(180)).rotateY((float) Math.toRadians(270))
            ), 40)
    ), 1.6, false),
    COMBAT(Map.of(
            0, new CircleState(0, 1.5f, 0.1f, 0),
            1, new CircleState(1.6f, -0.7f, 0.1f, 0),
            2, new CircleState(1.6f, 0, 0.1f, 60),
            3, new CircleState(1.6f, 0.7f, 0.1f, 120),
            4, new CircleState(1.6f, -0.7f, 0.1f, 180),
            5, new CircleState(1.6f, 0, 0.1f, 240),
            6, new CircleState(1.6f, 0.7f, 0.1f, 320),
            7, new CircleState(0, -1.5f, 0.1f, 0)
    ), 1.6, false),
    EMPTY(Map.of(), 0, true),
    RESPAWN(Map.of(), 2, true),
    CLAMP(new ImmutableMap.Builder<Integer, CubeState>()
            .put(0, new RotateYState(-0.5f, -0.5f, -1.1f, -0.7f, 20, -90, 1))
            .put(1, new RotateYState(-0.5f, -0.5f, 0, -0.7f, 20, -90, 3))
            .put(2, new RotateYState(-0.5f, -0.5f, 1.1f, -0.7f, 20, -90, 2))
            .put(3, new RotateYState(0.5f, 0.5f, -1.1f, 0.7f, 20, 90, 0))
            .put(4, new RotateYState(0.5f, 0.5f, 0, 0.7f, 20, 90, 2))
            .put(5, new RotateYState(0.5f, 0.5f, 1.1f, 0.7f, 20, 90, 1))

            .put(6, new RotateYState(0.5f, 1.6f, -0.6f, 0.7f, 20, 90, 3))
            .put(7, new RotateYState(-0.5f, -1.6f, -0.6f, -0.7f, 20, -90, 1))
            .put(8, new RotateYState(0.5f, 1.6f, 0.5f, 0.7f, 20, 90, 2))
            .put(9, new RotateYState(0.5f, 1.6f, 1.6f, 0.7f, 20, 90, 3))
            .put(10, new RotateYState(-0.5f, -1.6f, 0.5f, -0.7f, 20, -90, 1))
            .put(11, new RotateYState(-0.5f, -1.6f, 1.6f, -0.7f, 20, -90, 0))

            .put(12, new RotateYState(0.5f, 2.7f, -0.1f, 0.7f, 20, 90, 2))
            .put(13, new RotateYState(0.5f, 2.7f, 1f, 0.7f, 20, 90, 3))
            .put(14, new RotateYState(-0.5f, -2.7f, -0.1f, -0.7f, 20, -90, 0))
            .put(15, new RotateYState(-0.5f, -2.7f, 1f, -0.7f, 20, -90, 1))
            .build(), 1.6, false),
    FIST(new ImmutableMap.Builder<Integer, CubeState>()
            .put(0, new SimpleState(new Matrix4f().translate(0, 0, 1.7f).scale(4, 3, 3)))
            .put(1, new SimpleStates(List.of(
                    new Matrix4f().translate(0, 1f, 3.8f).rotateX((float) Math.toRadians(-15)),
                    new Matrix4f().translate(0, 1f, 3.8f).rotateX((float) Math.toRadians(-60)),
                    new Matrix4f().translate(0, 0.8f, 3.8f).rotateX((float) Math.toRadians(45))
            ), 5, 10, false))
            .put(2, new SimpleStates(List.of(
                    new Matrix4f().translate(0, 1f, 4.9f),
                    new Matrix4f().translate(0, 1.8f, 4.5f).rotateX((float) Math.toRadians(-45)),
                    new Matrix4f().translate(0, -0.2f, 4.5f).rotateX((float) Math.toRadians(90))
            ), 5, 10, false))
            .put(3, new SimpleStates(List.of(
                    new Matrix4f().translate(0, 1f, 5.9f).scale(0.8f),
                    new Matrix4f().translate(0, 2.5f, 5.3f).rotateX((float) Math.toRadians(-30)).scale(0.8f),
                    new Matrix4f().translate(0, -1.2f, 4.3f).rotateX((float) Math.toRadians(120)).scale(0.8f)
            ), 5, 10, false))

            .put(4, new SimpleStates(List.of(
                    new Matrix4f().translate(1.2f, 0.8f, 3.8f).rotateY((float) Math.toRadians(10)).rotateX((float) Math.toRadians(-10)),
                    new Matrix4f().translate(1.2f, 0.8f, 3.8f).rotateY((float) Math.toRadians(5)).rotateX((float) Math.toRadians(45))
            ), 5, 15, false))
            .put(5, new SimpleStates(List.of(
                    new Matrix4f().translate(1.3f, 0.8f, 4.9f).rotateY((float) Math.toRadians(10)).rotateX((float) Math.toRadians(10)),
                    new Matrix4f().translate(1.2f, -0.2f, 4.5f).rotateY((float) Math.toRadians(5)).rotateX((float) Math.toRadians(90))
            ), 5, 15, false))
            .put(6, new SimpleStates(List.of(
                    new Matrix4f().translate(1.4f, 0.6f, 5.9f).rotateY((float) Math.toRadians(10)).rotateX((float) Math.toRadians(25)).scale(0.8f),
                    new Matrix4f().translate(1.4f, 0.4f, 5.9f).rotateY((float) Math.toRadians(10)).rotateX((float) Math.toRadians(45)).scale(0.8f),
                    new Matrix4f().translate(1.2f, -1.2f, 4.3f).rotateY((float) Math.toRadians(5)).rotateX((float) Math.toRadians(120)).scale(0.8f)
            ), 5, 10, false))

            .put(7, new SimpleStates(List.of(
                    new Matrix4f().translate(-1.2f, 0.8f, 3.8f).rotateY((float) Math.toRadians(-10)).rotateX((float) Math.toRadians(-10)),
                    new Matrix4f().translate(-1.2f, 0.8f, 3.8f).rotateY((float) Math.toRadians(-10)).rotateX((float) Math.toRadians(-40)),
                    new Matrix4f().translate(-1.2f, 0.8f, 3.8f).rotateY((float) Math.toRadians(5)).rotateX((float) Math.toRadians(45))
            ), 5, 10, false))
            .put(8, new SimpleStates(List.of(
                    new Matrix4f().translate(-1.3f, 0.7f, 4.9f).rotateY((float) Math.toRadians(-10)),
                    new Matrix4f().translate(-1.3f, 1.4f, 4.8f).rotateY((float) Math.toRadians(-10)).rotateX((float) Math.toRadians(-25)),
                    new Matrix4f().translate(-1.2f, -0.2f, 4.5f).rotateY((float) Math.toRadians(-5)).rotateX((float) Math.toRadians(90))
            ), 5, 10, false))
            .put(9, new SimpleStates(List.of(
                    new Matrix4f().translate(-1.5f, 1f, 5.9f).rotateY((float) Math.toRadians(-10)).scale(0.8f),
                    new Matrix4f().translate(-1.5f, 1.8f, 5.7f).rotateY((float) Math.toRadians(-10)).rotateX((float) Math.toRadians(-10)).scale(0.8f),
                    new Matrix4f().translate(-1.2f, -1.2f, 4.3f).rotateY((float) Math.toRadians(5)).rotateX((float) Math.toRadians(120)).scale(0.8f)
            ), 5, 10, false))

            .put(10, new SimpleStates(List.of(
                    new Matrix4f().translate(-2.6f, 0.8f, 2.7f).rotateX((float) Math.toRadians(30)).rotateY((float) Math.toRadians(50)),
                    new Matrix4f().translate(-2.6f, 0.8f, 2.7f).rotateX((float) Math.toRadians(45)).rotateY((float) Math.toRadians(75))
            ), 5, 15, false))
            .put(11, new SimpleStates(List.of(
                    new Matrix4f().translate(-3f, 0.2f, 3.6f).rotateX((float) Math.toRadians(40)).rotateY((float) Math.toRadians(70)),
                    new Matrix4f().translate(-2.7f, 0f, 3.6f).rotateX((float) Math.toRadians(45)).rotateY((float) Math.toRadians(90))
            ), 5, 15, false))
            .put(12, new SimpleStates(List.of(
                    new Matrix4f().translate(-3.3f, -0.4f, 4.4f).rotateX((float) Math.toRadians(50)).rotateY((float) Math.toRadians(80)).scale(0.7f),
                    new Matrix4f().translate(-2.4f, -0.7f, 4.4f).rotateX((float) Math.toRadians(45)).rotateY((float) Math.toRadians(120)).scale(0.7f)
            ), 5, 15, false))
            .put(13, new SimpleStates(List.of(
                    new Matrix4f().translate(-3.3f, -1.2f, 5f).rotateX((float) Math.toRadians(50)).rotateY((float) Math.toRadians(90)).scale(0.7f),
                    new Matrix4f().translate(-1.7f, -1.3f, 5f).rotateX((float) Math.toRadians(60)).rotateY((float) Math.toRadians(150)).scale(0.7f)
            ), 5, 15, false))
            .build(), 1.6, false),
    SCISSOR(new ImmutableMap.Builder<Integer, CubeState>()
            .put(0, new RotateYState(0.5f, 1, 0, -2, -0.8f, 20, 0, 0 , 45,  20))
            .put(1, new RotateYState(0.5f, 0, 0, -3, -0.8f, 20, 0, 0 , 45,  20))
            .put(2, new RotateYState(0.5f, 1, 0, -4, -0.8f, 20, 0, 0 , 45,  20))
            .put(3, new RotateYState(0.5f, 0, 0, -5, -0.8f, 20, 0, 0 , 45,  20))
            .put(4, new RotateYState(-0.5f, -1, 0, -2, 0.8f, 20, 0, 0 , -45,  20))
            .put(5, new RotateYState(-0.5f, 0, 0, -3, 0.8f, 20, 0, 0 , -45,  20))
            .put(6, new RotateYState(-0.5f, -1, 0, -4, 0.8f, 20, 0, 0 , -45,  20))
            .put(7, new RotateYState(-0.5f, 0, 0, -5, 0.8f, 20, 0, 0 , -45,  20))
            .put(8, new RotateYState(-0.5f, 0, 0, 2, -0.8f, 20, 0, 0 , 45,  20))
            .put(9, new RotateYState(-0.5f, 0, 0, 3, -0.8f, 20, 0, 0 , 45,  20))
            .put(10, new RotateYState(-0.5f, 0, 0, 4, -0.8f, 20, 0, 0 , 45,  20))
            .put(11, new RotateYState(-0.5f, 0, 0, 5, -0.8f, 20, 0, 0 , 45,  20))
            .put(12, new RotateYState(0.5f, 0, 0, 2, 0.8f, 20, 0, 0 , -45,  20))
            .put(13, new RotateYState(0.5f, 0, 0, 3, 0.8f, 20, 0, 0 , -45,  20))
            .put(14, new RotateYState(0.5f, 0, 0, 4, 0.8f, 20, 0, 0 , -45,  20))
            .put(15, new RotateYState(0.5f, 0, 0, 5, 0.8f, 20, 0, 0 , -45,  20))
            .build(), 1.6, false),
    HAND(new ImmutableMap.Builder<Integer, CubeState>()
            .put(0, new RotateXState( 0, -2.1f, 1.4f, 0 , 0.25f, 15, 0, -45, 4, 2.5f, 3))
            .put(1, new RotateXState(0, -3.1f, 3.5f, 0 , 0.25f, 15, 0, -45))
            .put(2, new RotateXState(0, -3.1f, 4.6f, 0 , 0.25f, 15, 0, -45))
            .put(3, new RotateXState(0, -3.1f, 5.6f, 0 , 0.25f, 15, 0, -45, 0.8f))

            .put(4, new RotateXState(1.2f, -3.1f, 3.6f, 8, 0.25f, 15, 0, -45))
            .put(5, new RotateXState(1.2f, -3.1f, 4.7f, 8, 0.25f, 15, 0, -45))
            .put(6, new RotateXState(1.2f, -3.1f, 5.7f, 8, 0.25f, 15, 0, -45, 0.8f))

            .put(7, new RotateXState(-1.2f, -3.1f, 3.6f, -8, 0.25f, 15, 0, -45))
            .put(8, new RotateXState(-1.2f, -3.1f, 4.7f, -8, 0.25f, 15, 0, -45))
            .put(9, new RotateXState(-1.2f, -3.1f, 5.7f, -8, 0.25f, 15, 0, -45, 0.8f))

            .put(10, new RotateXState(-0.7f, -3.1f, 3.3f, -45, 0.25f, 15, 0, -45))
            .put(11, new RotateXState(-0.7f, -3.1f, 4.4f, -45, 0.25f, 15, 0, -45))
            .put(12, new RotateXState(-0.7f, -3.1f, 5.4f, -45, 0.25f, 15, 0, -45, 0.8f))
            .build(), 3.6, false),
    SHOOTING(new ImmutableMap.Builder<Integer, CubeState>()
            .put(0, new SimpleStates(List.of(
                    new Matrix4f().translate(0, 2.5f, 0),
                    new Matrix4f().translate(0, 2.5f, 0).scale(0.5f)
            ), 10, 20, true))
            .put(1, new SimpleStates(List.of(
                    new Matrix4f().translate(1.7f, 1.7f, 0),
                    new Matrix4f().translate(1.7f, 1.7f, 0).scale(0.5f)
            ), 10, 30, true))
            .put(2, new SimpleStates(List.of(
                    new Matrix4f().translate(2.5f, 0, 0),
                    new Matrix4f().translate(2.5f, 0, 0).scale(0.5f)
            ), 10, 40, true))
            .put(3, new SimpleStates(List.of(
                    new Matrix4f().translate(1.7f, -1.7f, 0),
                    new Matrix4f().translate(1.7f, -1.7f, 0).scale(0.5f)
            ), 10, 50, true))
            .put(4, new SimpleStates(List.of(
                    new Matrix4f().translate(0, -2.5f, 0),
                    new Matrix4f().translate(0, -2.5f, 0).scale(0.5f)
            ), 10, 50, true))
            .put(5, new SimpleStates(List.of(
                    new Matrix4f().translate(-1.7f, -1.7f, 0),
                    new Matrix4f().translate(-1.7f, -1.7f, 0).scale(0.5f)
            ), 10, 30, true))
            .put(6, new SimpleStates(List.of(
                    new Matrix4f().translate(-2.5f, 0, 0),
                    new Matrix4f().translate(-2.5f, 0, 0).scale(0.5f)
            ), 10, 40, true))
            .put(7, new SimpleStates(List.of(
                    new Matrix4f().translate(-1.7f, 1.7f, 0),
                    new Matrix4f().translate(-1.7f, 1.7f, 0).scale(0.5f)
            ), 10, 50, true))
            .build(), 3, true
    ),
    DRILL(new ImmutableMap.Builder<Integer, CubeState>()
            .put(0, new RotateZState(0, 2f, 0.7f, 1, 0, 0, 3))
            .put(1, new RotateZState(3, 1.5f, 0.7f, 3, 0, -30, 2))
            .put(2, new RotateZState(3, 1.5f, 0.7f, 1, 90, -30, 2))
            .put(3, new RotateZState(3, 1.5f, 0.7f, 2, 180, -30, 2))
            .put(4, new RotateZState(3, 1.5f, 0.7f, 0, 270, -30, 2))
            .put(5, new RotateZState(3, 1.5f, 0.7f, 3, 45, -30, 2))
            .put(6, new RotateZState(3, 1.5f, 0.7f, 0, 135, -30, 2))
            .put(7, new RotateZState(3, 1.5f, 0.7f, 2, 225, -30, 2))
            .put(8, new RotateZState(3, 1.5f, 0.7f, 1, 315, -30, 2))
            .put(9, new RotateZState(2, 3.2f, 0.7f, 2, 15, -30, 2))
            .put(10, new RotateZState(2, 3.2f, 0.7f, 1, 105, -30, 2))
            .put(11, new RotateZState(2, 3.2f, 0.7f, 2, 195, -30, 2))
            .put(12, new RotateZState(2, 3.2f, 0.7f, 3, 285, -30, 2))
            .put(13, new RotateZState(2, 3.2f, 0.7f, 0, 60, -30, 2))
            .put(14, new RotateZState(2, 3.2f, 0.7f, 1, 150, -30, 2))
            .put(15, new RotateZState(2, 3.2f, 0.7f, 2, 240, -30, 2))
            .put(16, new RotateZState(2, 3.2f, 0.7f, 0, 3-30, -30, 2))
            .put(17, new RotateZState(1, 5, 0.7f, 1, 75, -30, 2))
            .put(18, new RotateZState(1, 5, 0.7f, 2, 165, -30, 2))
            .put(19, new RotateZState(1, 5, 0.7f, 0, 255, -30, 2))
            .put(20, new RotateZState(1, 5, 0.7f, 1, 345, -30, 2))
            .put(21, new RotateZState(0.3f, 6.8f, 0.7f, 2, 90, -30, 1.5f))
            .put(22, new RotateZState(0.3f, 6.8f, 0.7f, 1, 180, -30, 1.5f))
            .put(23, new RotateZState(0.3f, 6.8f, 0.7f, 3, 270, -30, 1.5f))
            .put(24, new RotateZState(0.3f, 6.8f, 0.7f, 1, 0, -30, 1.5f))
            .put(25, new RotateZState(0, 7.4f, 0.7f, 3, 0, 0, 1f))
            .put(26, new RotateZState(2.5f, 1.51f, 0.7f, 3, -30, 0, 2))
            .put(27, new RotateZState(2.5f, 1.51f, 0.7f, 2, 120, 0, 2))
            .put(28, new RotateZState(2.5f, 1.51f, 0.7f, 0, 210, 0, 2))
            .put(29, new RotateZState(2.5f, 1.51f, 0.7f, 1, -300, 0, 2))
            .build(), 3, true
    );


    private final Map<Integer, CubeState> states;
    private final double yPos;
    private final boolean canHurt;
    private static final Matrix4f MATRIX_0 = new Matrix4f().scale(0.01f);


    HypostasisCubeState(Map<Integer, CubeState> states, double yPos, boolean canHurt) {
        this.states = states;
        this.yPos = yPos;
        this.canHurt = canHurt;
    }

    public static HypostasisCubeState byInt(int val){
        return HypostasisCubeState.values()[val];
    }

    public void renderTurning(HypostasisCube cube, ModelPart model, Turning newState, PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay, float dt){
        Set<Integer> intSet = new HashSet<>();
        int preTime = cube.getAnimateTime(false);
        int postTime = cube.getAnimateTime(true);
        float per = (postTime + dt) / newState.usingTick;
        this.states.forEach(
                (integer, cubeState) -> {
                    poseStack.pushPose();
                    intSet.add(integer);
                    Matrix4f mul = MATRIX_0;
                    if(newState.post().states.containsKey(integer)){
                        CubeState newSimple = newState.post().states.get(integer);
                        if(newSimple.shouldRender(postTime, dt))
                            mul = newSimple.getByTime(postTime, dt);
                    }
                    if(cubeState.shouldRenderAfter()) {
                        if(mul == MATRIX_0)
                            mul = cubeState.getByTime(preTime, dt).translate(0, per * 2, 0).scale(1 - per);
                        else
                            mul = mix(cubeState.getByTime(preTime, dt), mul, per);
                    }
                    else
                        mul = mix(MATRIX_0, mul, per);
                    poseStack.mulPoseMatrix(mul);
                    model.render(poseStack, vertexConsumer, light, overlay);
                    poseStack.popPose();
                }
        );

        newState.post().states.forEach(
                (integer, cubeState) -> {
                    if(!intSet.contains(integer)){
                        poseStack.pushPose();
                        Matrix4f mul = MATRIX_0;
                        if(cubeState.shouldRender(postTime, dt))
                            mul = mix(mul, cubeState.getByTime(postTime, dt), per);
                        Vector3f vec3 = new Vector3f();
                        mul.getScale(vec3);

                        poseStack.mulPoseMatrix(mul);
                        model.render(poseStack, vertexConsumer, light, overlay);
                        poseStack.popPose();
                    }
                }
        );
    }

    public void render(ModelPart cube, PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay, int tick, float dt){
        //Map<Integer, CubeState> states = FIST.states;

        for(CubeState state: states.values()){
            if(state.shouldRender(tick, dt)) {
                poseStack.pushPose();
                poseStack.mulPoseMatrix(state.getByTime(tick, dt));
                cube.render(poseStack, vertexConsumer, light, overlay);
                poseStack.popPose();
            }
        }
    }

    public static Matrix4f mix(Matrix4f pre4f, Matrix4f post4f, float per){
        Matrix4f pre = new Matrix4f(pre4f);
        Matrix4f post = new Matrix4f(post4f);

        Vector3f preTranslation = new Vector3f();
        Vector3f preScale = new Vector3f();
        Quaternionf preRotation = new Quaternionf();
        pre.getScale(preScale);
        pre.getTranslation(preTranslation);
        pre.getUnnormalizedRotation(preRotation);

        Vector3f postTranslation = new Vector3f();
        Vector3f postScale = new Vector3f();
        Quaternionf postRotation = new Quaternionf();
        post.getScale(postScale);
        post.getTranslation(postTranslation);
        post.getUnnormalizedRotation(postRotation);

        Vector3f trans = new Vector3f(preTranslation).lerp(postTranslation, per);
        Vector3f scale = new Vector3f(preScale).lerp(postScale, per);
        Quaternionf rot = new Quaternionf(preRotation).slerp(postRotation, per);

        return new Matrix4f()
                .translation(trans)
                .rotate(rot)
                .scale(scale);
    }

    public boolean canHurt() {
        return canHurt;
    }

    public double getYPos() {
        return this.yPos;
    }

    public record Turning(HypostasisCubeState post, int usingTick){
        public static void encode(FriendlyByteBuf byteBuf, Optional<Turning> turning) {
            turning.ifPresentOrElse(
                    turning1 -> {
                        byteBuf.writeInt(turning1.post().ordinal());
                        byteBuf.writeInt(turning1.usingTick());
                    },
                    () -> byteBuf.writeInt(-1)
            );
        }

        public static @NotNull Optional<Turning> decode(@NotNull ByteBuf byteBuf) {
            int i = byteBuf.readInt();
            return i == -1 ? Optional.empty() : Optional.of(new Turning(
                    HypostasisCubeState.byInt(i),
                    byteBuf.readInt()
            ));
        }
    }
}
