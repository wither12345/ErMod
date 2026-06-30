package net.wither.er.mixins;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.wither.er.entity.ArtifactSlot;
import net.wither.er.entity.ErEntityInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends LivingEntity implements ErEntityInterface{

    protected ServerPlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;replaceWith(Lnet/minecraft/world/entity/player/Inventory;)V"))
    public void restoreFrom(ServerPlayer player, boolean flag, CallbackInfo info) {
        if(player instanceof ErEntityInterface entityInterface) {
            for (ArtifactSlot slot : ArtifactSlot.values())
                this.setArtifact(slot, entityInterface.er$getArtifact(slot));
            entityInterface.updateArtifact();
        }
    }

    @Override
    public void er$dropArtifact() {
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            for(ArtifactSlot slot : ArtifactSlot.values())
                this.level().addFreshEntity(new ItemEntity(level(),getX(),getY(),getZ(), this.er$getArtifact(slot)));
        }
    }
}
