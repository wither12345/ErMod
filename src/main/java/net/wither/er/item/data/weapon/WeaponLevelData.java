package net.wither.er.item.data.weapon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.init.ErModItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.wither.er.init.DataComponentsRegister;

public record WeaponLevelData(int level, int ascension, int experience, int total_experience) {
    public static final TagKey<Item> not_enhanceable =  ItemTags.create(ResourceLocation.parse("er:not_enhanceable")) ;

    public static final Codec<WeaponLevelData> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("level").forGetter(WeaponLevelData::level),
                    Codec.INT.fieldOf("ascension").forGetter(WeaponLevelData::ascension),
                    Codec.INT.fieldOf("experience").forGetter(WeaponLevelData::experience),
                    Codec.INT.fieldOf("total_experience").forGetter(WeaponLevelData::total_experience)
            ).apply(instance, WeaponLevelData::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, WeaponLevelData> BASIC_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, WeaponLevelData::level,
            ByteBufCodecs.INT, WeaponLevelData::ascension,
            ByteBufCodecs.INT, WeaponLevelData::experience,
            ByteBufCodecs.INT, WeaponLevelData::total_experience,
            WeaponLevelData::new
    );

    public static WeaponLevelData create(int level, int ascension, int experience, int total_experience){
        return new WeaponLevelData(level,ascension,experience,total_experience);
    }

    public static WeaponLevelData create(int level){
        int ascension = 0 ;
        for( ; ascension < 6 ; ascension ++) {
            if(getMaxLevel(ascension) >= level)
                return new WeaponLevelData(level,ascension,0,0);
        }
        return new WeaponLevelData(level,ascension,0,0);
    }

    public static WeaponLevelData create(int ascension, boolean max){
        return new WeaponLevelData(max ? getMaxLevel(ascension) : getMaxLevel(ascension - 1),ascension,0,0);
    }

    public static int getMaxExp(int level, int star){
        double multi = switch (star) {
            case 1 -> 0.2;
            case 2 -> 0.3;
            case 3 -> 0.44;
            case 4 -> 0.66666;
            default -> 1;
        };
        if (level >= 80) {
            return (int) (Math.ceil((Math.pow(level, 2) * 100 - level * 15128.9 + 575654.6) * multi) * 25);
        } else if (level >= 70) {
            return (int) (Math.ceil((Math.pow(level, 2) * 0.95 + level * 24.8 + 81) * multi) * 25);
        } else if (level >= 60) {
            return (int) (Math.ceil(((Math.pow(level, 2) * 0.8 + level * 36.42) - 370) * multi) * 25);
        } else if (level >= 50) {
            return (int) (Math.ceil(((Math.pow(level, 2) * 0.8 + level * 27) - 109.8) * multi) * 25);
        } else if (level >= 40) {
            return (int) (Math.ceil(((Math.pow(level, 2) * 0.8 + level * 25.3) - 89.7) * multi) * 25);
        } else if (level >= 20) {
            return (int) (Math.ceil(((Math.pow(level, 2) * 0.8 + level * 20.6) - 21) * multi) * 25);
        }
        return (int) (Math.ceil((Math.pow(level, 2) * 0.9 + level * 14.8 + 21.2) * multi) * 25);
    }

    public static int getItemWeaponStar(ItemStack stack){
        if(stack.is(ItemTags.create(ResourceLocation.parse("er:five_star_weapon"))))
            return 5;
        if(stack.is(ItemTags.create(ResourceLocation.parse("er:four_star_weapon"))))
            return 4;
        if(stack.is(ItemTags.create(ResourceLocation.parse("er:three_star_weapon"))))
            return 3;
        if(stack.is(ItemTags.create(ResourceLocation.parse("er:two_star_weapon"))))
            return 2;
        return 1 ;
    }

    public static int getBasicExperience(ItemStack stack){
        Item item = stack.getItem() ;
        if(item == ErModItems.ENHANCEMENT_ORE.get())
            return 400 ;
        else if(item == ErModItems.FINE_ENHANCEMENT_ORE.get())
            return 2000 ;
        else if(item == ErModItems.MYSTIC_ENHANCEMENT_ORE.get())
            return 10000 ;
        if(!stack.getComponents().has(DataComponentsRegister.WEAPON_LEVEL.get()))
            return 0 ;
        return switch (getItemWeaponStar(stack)) {
            case 1 -> 600;
            case 2 -> 1200;
            case 3 -> 1800;
            case 4 -> 50000;
            case 5 -> 300000;
            default -> 0;
        };
    }

    public static int getMaxLevel(int ascension){
        if(ascension == 0)
            return 20 ;
        return 30 + 10 * ascension ;
    }
}
