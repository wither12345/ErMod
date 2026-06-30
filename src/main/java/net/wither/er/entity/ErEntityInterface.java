package net.wither.er.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.shield.ErShield;
import net.wither.er.shield.ShieldStack;

import java.util.List;

public interface ErEntityInterface {
    List<ShieldStack> getShieldStacks();
    List<ErShield> getShields();
    void setShields(CompoundTag tag);
    void addShield(ShieldStack shield) ;
    void removeShield(ErShield shield);
    void cleanShield();
    void syncShield() ;
    void syncShield(ServerPlayer player) ;
    void setArtifact(ArtifactSlot slot, ItemStack itemStack) ;
    ItemStack getArtifact(ArtifactSlot slot);
    void er$dropArtifact() ;
    void er$updateArtifact();
    int er$getArtifactEffectLevel(ArtifactEffect effectHolder);
}
