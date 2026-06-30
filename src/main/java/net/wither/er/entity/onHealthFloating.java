package net.wither.er.entity;

import net.minecraft.world.entity.Entity;
import net.wither.er.artifact_effect.ArtifactEffect;

public class onHealthFloating {
    public static void onFloating(Entity entity){
        ArtifactEffect.berserkerCheck(entity);
    }
}
