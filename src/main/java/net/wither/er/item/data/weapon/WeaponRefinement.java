package net.wither.er.item.data.weapon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.ERConfig;
import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.wither.er.init.AdditionalRegistries;

public record WeaponRefinement(Holder<WeaponAbility> ability, Holder<Item> refinementItem, int refineLevel) {
    public static final Codec<WeaponRefinement> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    AdditionalRegistries.WEAPON_ABILITY_REGISTRY.holderByNameCodec().fieldOf("ability").forGetter(WeaponRefinement::ability),
                    ItemStack.ITEM_NON_AIR_CODEC.fieldOf("refinementItem").forGetter(WeaponRefinement::refinementItem),
                    Codec.INT.fieldOf("refineLevel").forGetter(WeaponRefinement::refineLevel)
            ).apply(instance, WeaponRefinement::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, WeaponRefinement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(AdditionalRegistries.WEAPON_ABILITY), WeaponRefinement::ability,
            ByteBufCodecs.holderRegistry(Registries.ITEM), WeaponRefinement::refinementItem,
            ByteBufCodecs.INT, WeaponRefinement::refineLevel,
            WeaponRefinement::new
    );


    public void modify(DamageSource source, LivingEntity entity, EntityHurtEvent.DamageModifier modifier){
        ability.value().modify(source,entity,modifier,refineLevel);
    }

    public WeaponRefinement refine(int r){
        return new WeaponRefinement(ability, refinementItem, Math.min(ERConfig.MAX_REFINEMENT.get(), r + refineLevel));
    }
}
