package net.wither.er.item.data.artifactdata;

import net.mcreator.er.ERConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.entity.ArtifactSlot;
import net.wither.er.init.AdditionalRegistries;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public final class ArtifactData {
    public static final Capability<ArtifactData> ARTIFACT_DATA = CapabilityManager.get(new CapabilityToken<>() {});

    private @NonNull ArtifactSlot slot;
    private @NonNull MainAffix main;
    private final @NonNull List<MinorAffix> minor;
    private @NonNull Supplier<ArtifactEffect> effect;
    private @NonNull ArtifactLevel level;
    private int rarity;

    public ArtifactData(@NonNull ArtifactSlot slot, @NonNull MainAffix main, @NonNull List<MinorAffix> minor, @NonNull Supplier<ArtifactEffect> effect, @NonNull ArtifactLevel level, int rarity) {
        this.slot = slot;
        this.main = main;
        this.minor = minor;
        this.effect = effect;
        this.level = level;
        this.rarity = rarity;
    }

    public int addTooltip(List<Component> components) {
        int index = 1;
        components.add(index++, Component.literal("+" + level.level()));
        int percent = experience_percentage(40, level, rarity);
        components.add(index++, Component.literal(level.experience() + "/" + getMaxExp(level.level(), rarity)));
        String greenBars = "|".repeat(percent);
        String whiteBars = "|".repeat(40 - Math.min(percent, 40));
        components.add(index++, Component.literal("§a" + greenBars + "§f" + whiteBars));
        if (this.main.amount() == 0) {
            components.add(index, Component.literal("§kthe mod is made by wither_123"));
            return index;
        }
        components.add(index++, Component.literal(main.toString(level.level(), rarity)));
        for (MinorAffix minorAffix : minor) {
            components.add(index++, Component.literal("§7 " + minorAffix.toString(rarity)));
        }
        return index;
    }

    public void setLevel(ArtifactLevel level, int minorUpgrade) {
        this.level = level;
        MinorAffix.rollingList(minor, main, minorUpgrade);
    }

    public void remove(LivingEntity entity) {
        if (this.main.amount() == 0)
            return;
        main.remove(slot, entity);
        int id = 0;
        for (MinorAffix affix : minor) {
            affix.remove(slot, entity, id++);
        }
    }

    public void apply(LivingEntity entity) {
        if (this.main.amount() == 0)
            return;
        main.apply(slot, entity, level.level(), rarity);
        int id = 0;
        for (MinorAffix affix : minor) {
            affix.apply(slot, entity, rarity, id++);
        }
    }

    public static int experience_percentage(int t, ArtifactLevel level, int rarity) {
        return experience_percentage(t, level.level(), level.experience(), rarity);
    }

    public static int experience_percentage(int t, int level, int experience, int rarity) {
        if (level >= getMaxLevel(rarity))
            return t;
        return Math.min(t, t * experience / getMaxExp(level, rarity));
    }

    public static int getMaxExp(int level, int rarity) {
        return (600 + 175 * level) * rarity;
    }

    public static int getMaxLevel(int star) {
        if (star <= 2)
            return 4;
        return star * 4;
    }

    public void rolling(int rarity) {
        String[] attrs;
        List<? extends String> config_get =
                switch (slot) {
                    case FLOWER_OF_LIFE -> ERConfig.FLOWER_OF_LIFE_MAIN_ATTR.get();
                    case PLUME_OF_DEATH -> ERConfig.PLUME_OF_DEATH_ATTR.get();
                    case SAND_OF_EON -> ERConfig.SANDS_OF_EON_ATTR.get();
                    case GOBLET_OF_EONOTHEM -> ERConfig.GOBLET_OF_EONOTHEM_ATTR.get();
                    case CIRCLET_OF_LOGOS -> ERConfig.CIRCLET_OF_LOGOS_ATTR.get();
                };


        attrs = config_get.toArray(new String[1]);
        int index = new Random().nextInt(attrs.length);
        String[] type = attrs[index].replaceAll(" ", "").split(",");

        int minorCount = rarity - 1;
        if (rarity > 2) {
            minorCount -= Mth.nextInt(RandomSource.create(), 0, 1);
        }

        this.main.create(type) ;
        MinorAffix.rollingList(minor, this.main, minorCount);
        this.level = new ArtifactLevel(0, 0, 0);
        this.rarity = rarity ;
    }

    public int getColor() {
        return switch (rarity) {
            case 1 -> 0xffffff;
            case 2 -> 0x00aa00;
            case 3 -> 0x5555ff;
            case 4 -> 0xaa00aa;
            case 5 -> 0xffaa00;
            case 6 -> 0xff55ff;
            case 7 -> 0x55ffff;
            default -> 0xaa0000;
        };
    }

    public void rolling() {
        rolling(rarity);
    }

    public ArtifactData setRarity(int rarity) {
        this.rarity = rarity;
        return this;
    }

    public void addMinor(@NonNull MinorAffix affix, int count) {
        MinorAffix.addMinor(this.minor, affix, count);
    }

    public void setMain(@NonNull MainAffix affix) {
        this.main = affix;
    }


    @Override
    public String toString() {
        return "ArtifactData[" +
                "slot=" + slot + ", " +
                "main=" + main + ", " +
                "minor=" + minor + ", " +
                "effect=" + effect + ", " +
                "level=" + level + ", " +
                "rarity=" + rarity + ']';
    }

    public ArtifactSlot slot() {
        return this.slot;
    }

    public ArtifactLevel level() {
        return this.level;
    }

    public MainAffix main() {
        return this.main;
    }

    public int rarity(){
        return this.rarity;
    }

    public List<MinorAffix> minor(){
        return this.minor;
    }

    public Supplier<ArtifactEffect> effect() {
        return this.effect;
    }

    public static class CapabilityProvider implements ICapabilitySerializable<CompoundTag> {
        public CapabilityProvider(ArtifactData data){
            this.data = data;
            instance = LazyOptional.of(() -> data);
        }

        public CapabilityProvider(ArtifactSlot slot, Supplier<ArtifactEffect> effect){
            this(new ArtifactData(slot,new MainAffix(Attributes.MAX_HEALTH,0,false), new ArrayList<>(), effect ,new ArtifactLevel(0,0,0),1));
        }

        private final ArtifactData data ;
        private final LazyOptional<ArtifactData> instance  ;

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == ARTIFACT_DATA ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("slot", data.slot.getSerializedName());
            tag.put("main", data.main.getTag());

            CompoundTag minor_tag = new CompoundTag();
            int i = 0 ;
            for(MinorAffix minorAffix : data.minor){
                CompoundTag tg = minorAffix.getTag();
                if(tg.getDouble("amount") == 0)
                    continue;
                minor_tag.put(String.valueOf(i ++), tg);
            }
            tag.put("minor", minor_tag);

            ResourceLocation location = AdditionalRegistries.ARTIFACT_REGISTRY.getKey(this.data.effect.get());
            if(location != null)
                tag.putString("effect", location.toString());
            tag.put("level", data.level.getTag());
            tag.putInt("rarity", data.rarity);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            this.data.slot = ArtifactSlot.byName(tag.getString("slot"));
            this.data.main = MainAffix.getByTag(tag.getCompound("main"));

            this.data.minor.clear();
            CompoundTag minor_tag = tag.getCompound("minor");
            Set<String> keys = minor_tag.getAllKeys();
            for(String s : keys){
                MinorAffix affix = MinorAffix.getByTag(minor_tag.getCompound(s));
                this.data.minor.add(affix);
            }

            Optional<Holder<ArtifactEffect>> eff = AdditionalRegistries.ARTIFACT_REGISTRY.getHolder(new ResourceLocation(tag.getString("effect"))) ;
            eff.ifPresent(effectHolder -> this.data.effect = effectHolder);
            this.data.level = ArtifactLevel.getByTag(tag.getCompound("level"));
            this.data.rarity = tag.getInt("rarity");
        }
    }
}
