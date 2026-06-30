package net.wither.er.item.data.weapon;

import net.mcreator.er.init.ErModItems;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.wither.er.init.DataComponentsRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class WeaponLevelData {
    public static final Capability<WeaponLevelData> WEAPON_LEVEL = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final TagKey<Item> not_enhanceable = ItemTags.create(new ResourceLocation("er:not_enhanceable"));

    private int level;
    private int ascension;
    private int experience;
    private int total_experience;

    public WeaponLevelData(int level, int ascension, int experience, int total_experience) {
        this.level = level;
        this.ascension = ascension;
        this.experience = experience;
        this.total_experience = total_experience;
    }

    public WeaponLevelData update(int level, int ascension, int experience, int total_experience){
        this.level = level;
        this.ascension = ascension;
        this.experience = experience;
        this.total_experience = total_experience;
        return this;
    }

    public static WeaponLevelData create(int level, int ascension, int experience, int total_experience) {
        return new WeaponLevelData(level, ascension, experience, total_experience);
    }

    public static WeaponLevelData create(int level) {
        int ascension = 0;
        for (; ascension < 6; ascension++) {
            if (getMaxLevel(ascension) >= level)
                return new WeaponLevelData(level, ascension, 0, 0);
        }
        return new WeaponLevelData(level, ascension, 0, 0);
    }

    public static WeaponLevelData create(int ascension, boolean max) {
        return new WeaponLevelData(max ? getMaxLevel(ascension) : getMaxLevel(ascension - 1), ascension, 0, 0);
    }

    public static int getMaxExp(int level, int star) {
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

    public static int getItemWeaponStar(ItemStack stack) {
        if (stack.is(ItemTags.create(new ResourceLocation("er:five_star_weapon"))))
            return 5;
        if (stack.is(ItemTags.create(new ResourceLocation("er:four_star_weapon"))))
            return 4;
        if (stack.is(ItemTags.create(new ResourceLocation("er:three_star_weapon"))))
            return 3;
        if (stack.is(ItemTags.create(new ResourceLocation("er:two_star_weapon"))))
            return 2;
        return 1;
    }

    public static int getBasicExperience(ItemStack stack) {
        Item item = stack.getItem();
        if (item == ErModItems.ENHANCEMENT_ORE.get())
            return 400;
        else if (item == ErModItems.FINE_ENHANCEMENT_ORE.get())
            return 2000;
        else if (item == ErModItems.MYSTIC_ENHANCEMENT_ORE.get())
            return 10000;
        if (!DataComponentsRegister.WEAPON_LEVEL.itemHas(stack))
            return 0;
        return switch (getItemWeaponStar(stack)) {
            case 1 -> 600;
            case 2 -> 1200;
            case 3 -> 1800;
            case 4 -> 50000;
            case 5 -> 300000;
            default -> 0;
        };
    }

    public static int getMaxLevel(int ascension) {
        if (ascension == 0)
            return 20;
        return 30 + 10 * ascension;
    }

    public int level() {
        return level;
    }

    public int ascension() {
        return ascension;
    }

    public int experience() {
        return experience;
    }

    public int total_experience() {
        return total_experience;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (WeaponLevelData) obj;
        return this.level == that.level &&
                this.ascension == that.ascension &&
                this.experience == that.experience &&
                this.total_experience == that.total_experience;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, ascension, experience, total_experience);
    }

    @Override
    public String toString() {
        return "WeaponLevelData[" +
                "level=" + level + ", " +
                "ascension=" + ascension + ", " +
                "experience=" + experience + ", " +
                "total_experience=" + total_experience + ']';
    }


    public static class CapabilityProvider implements ICapabilitySerializable<CompoundTag> {
        private final WeaponLevelData data = new WeaponLevelData(1, 0, 0, 0);
        private final LazyOptional<WeaponLevelData> instance = LazyOptional.of(() -> data);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == WEAPON_LEVEL ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("level", data.level());
            tag.putInt("ascension", data.ascension());
            tag.putInt("experience", data.experience());
            tag.putInt("total_experience", data.total_experience());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            data.level = tag.getInt("level");
            data.ascension = tag.getInt("ascension");
            data.experience = tag.getInt("experience");
            data.total_experience = tag.getInt("total_experience");
        }
    }
}