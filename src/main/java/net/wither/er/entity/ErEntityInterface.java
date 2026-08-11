package net.wither.er.entity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.shield.ErShield;
import net.wither.er.shield.ShieldStack;

import java.util.List;

public interface ErEntityInterface {
    List<ShieldStack> er$getShieldStacks();
    List<ErShield> er$getShields();
    void er$setShields(CompoundTag tag);
    void er$addShield(ShieldStack shield) ;
    void er$removeShield(ErShield shield);
    void cleanShield();
    void er$syncShield() ;
    void er$syncShield(ServerPlayer player) ;
    void er$setArtifact(ArtifactSlot slot, ItemStack itemStack) ;
    Object2IntMap<ArtifactEffect> er$getEffectMap();
    ItemStack er$getArtifact(ArtifactSlot slot);
    void er$dropArtifact() ;
    void er$updateArtifact();
    int er$getArtifactEffectLevel(ArtifactEffect effectHolder);
    boolean er$shouldBurnBlock(long gameTime);
}
