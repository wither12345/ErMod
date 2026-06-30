package net.wither.er.player;

import net.minecraft.world.entity.Entity;
import net.wither.er.artifact_effect.ArtifactEffect;

public class onHealthFloating {
    public static void onFloating(Entity entity){
        ArtifactEffect.BerserkerCheck(entity);
    }
}
