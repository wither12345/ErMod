package net.wither.er.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity implements ElementSourceInterface {
    @Unique
    ElementSource er$source;

    public ProjectileMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public ElementSource er$getSource() {
        return er$source;
    }

    @Override
    public Object er$setElement(@NotNull ElementSource source) {
        this.er$source = source ;
        return this ;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci){
        if(this.er$source != null && this.level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(Element.getParticle(er$source.getCategory()), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }
    }
}