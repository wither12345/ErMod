package net.wither.er.mixins;

import net.minecraft.world.item.ProjectileWeaponItem;
import net.wither.er.item.IProjectileWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin implements IProjectileWeapon {
    @Unique double er$damage = 4 ;
    @Unique double er$damageScale = 0.33 ;

    @Override
    public void er$setDamage(double damage){
        this.er$damage = damage ;
    }

    @Override
    public void er$setDamageScale(double scale){
        this.er$damageScale = scale ;
    }

    @Override
    public double er$getDamageScale(){
        return this.er$damageScale;
    }

    @Override
    public double er$getDamage(){
        return this.er$damage;
    }
}
