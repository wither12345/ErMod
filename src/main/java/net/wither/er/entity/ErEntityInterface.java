package net.wither.er.entity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.shield.ErShield;
import net.wither.er.shield.ShieldStack;

import java.util.List;

public interface ErEntityInterface {
    List<ShieldStack> er$getShieldStacks();
    List<ErShield> er$getShields();
    void er$setShields(CompoundTag tag);
    void er$addShield(ShieldStack shield) ;
    void removeShield(ErShield shield);
    void cleanShield();
    void syncShield() ;
    void syncShield(ServerPlayer player) ;
    void setArtifact(ArtifactSlot slot, ItemStack itemStack) ;
    ItemStack er$getArtifact(ArtifactSlot slot);
    Object2IntMap<Holder<ArtifactEffect>> er$getEffectMap();
    void er$dropArtifact() ;
    void updateArtifact();
    int er$getArtifactEffectLevel(Holder<ArtifactEffect> effectHolder);
}
