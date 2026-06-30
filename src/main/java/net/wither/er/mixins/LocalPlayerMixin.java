package net.wither.er.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErSyncGameRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends Player {
    public LocalPlayerMixin(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Inject(method = "canStartSprinting" , at = @At("RETURN") , cancellable = true)
    public void canStartSprinting(CallbackInfoReturnable<Boolean> ci) {
        LazyOptional<ErCombatVariables.PlayerVariables> opt = this.getCapability(ErCombatVariables.PLAYER_VARIABLES);
        opt.ifPresent(
                (t) -> ci.setReturnValue(ci.getReturnValue() && (t.stamina > 0 || !ErSyncGameRule.getRunningStamina() ||
                        Minecraft.getInstance().getConnection().getPlayerInfo(this.getGameProfile().getId()).getGameMode() == GameType.CREATIVE))
        );
    }
}
