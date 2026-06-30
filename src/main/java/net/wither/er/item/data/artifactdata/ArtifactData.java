package net.wither.er.item.data.artifactdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.ERConfig;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.entity.ArtifactSlot;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Random;

public record ArtifactData(@NonNull ArtifactSlot slot, @NonNull MainAffix main, @NonNull List<MinorAffix> minor, @NonNull Holder<ArtifactEffect> effect, @NonNull ArtifactLevel level, int rarity) {
    public static final Codec<ArtifactData> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, ArtifactData> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ArtifactSlot.CODEC.fieldOf("slot").forGetter(ArtifactData::slot),
                        MainAffix.CODEC.fieldOf("main").forGetter(ArtifactData::main),
                        MinorAffix.CODEC.listOf().fieldOf("minors").forGetter(ArtifactData::minor),
                        ArtifactEffect.CODEC.fieldOf("effect").forGetter(ArtifactData::effect),
                        ArtifactLevel.CODEC.fieldOf("level").forGetter(ArtifactData::level),
                        Codec.INT.fieldOf("rarity").forGetter(ArtifactData::rarity)
                ).apply(instance, ArtifactData::new));
        STREAM_CODEC = StreamCodec.composite(
                ArtifactSlot.STREAM_CODEC, ArtifactData::slot,
                MainAffix.STREAM_CODEC, ArtifactData::main,
                MinorAffix.STREAM_CODEC.apply(ByteBufCodecs.list(20)), ArtifactData::minor,
                ArtifactEffect.STREAM_CODEC, ArtifactData::effect,
                ArtifactLevel.STREAM_CODEC, ArtifactData::level,
                ByteBufCodecs.INT, ArtifactData::rarity,
                ArtifactData::new
        ) ;
    }

    public void addTooltip(List<Component> components){
        int index = 1 ;
        components.add(index ++,Component.literal("+" + level.level()));
        int percent = experience_percentage(40, level, rarity);
        components.add(index ++,Component.literal(level.experience() + "/" + getMaxExp(level.level(), rarity)));
        String greenBars = "|".repeat(percent);
        String whiteBars = "|".repeat(40 - Math.min(percent, 40));
        components.add(index ++,Component.literal("§a" + greenBars + "§f" + whiteBars));
        if (this.main.amount() == 0) {
            components.add(index ++,Component.literal("§kthe mod is made by wither_123"));
            return;
        }
        components.add(index ++,Component.literal(main.toString(level.level(), rarity))) ;
        for(MinorAffix minorAffix : minor){
            components.add(index ++,Component.literal("§7 " + minorAffix.toString(rarity))) ;
        }
    }

    public ArtifactData setLevel(ArtifactLevel level, int minorUpgrade){
        return new ArtifactData(slot, main, MinorAffix.rollingList(minor, main, minorUpgrade), effect, level, rarity) ;
    }

    public void remove(LivingEntity entity){
        if(this.main.amount() == 0)
            return;
        main.remove(slot, entity);
        int id = 0 ;
        for(MinorAffix affix : minor){
            affix.remove(slot, entity, id++);
        }
    }

    public void apply(LivingEntity entity){
        if(this.main.amount() == 0)
            return;
        main.apply(slot, entity, level.level(), rarity);
        int id = 0 ;
        for(MinorAffix affix : minor){
            affix.apply(slot, entity, rarity, id++);
        }
    }

    public static int experience_percentage(int t, ArtifactLevel level, int rarity) {
        return experience_percentage(t, level.level(), level.experience(), rarity) ;
    }

    public static int experience_percentage(int t, int level, int experience, int rarity) {
        if (level >= getMaxLevel(rarity))
            return t;
        return Math.min(t, t * experience / getMaxExp(level,rarity));
    }

    public static int getMaxExp(int level, int rarity){
        return (600 + 175 * level) * rarity ;
    }

    public static int getMaxLevel(int star) {
        if (star <= 2)
            return 4;
        return star * 4;
    }

    public ArtifactData rolling(int rarity){
        String[] attrs;
        List<? extends String> config_get =
                switch (slot){
                    case FLOWER_OF_LIFE -> ERConfig.FLOWER_OF_LIFE_MAIN_ATTR.get();
                    case PLUME_OF_DEATH -> ERConfig.PLUME_OF_DEATH_ATTR.get();
                    case SAND_OF_EON -> ERConfig.SANDS_OF_EON_ATTR.get();
                    case GOBLET_OF_EONOTHEM -> ERConfig.GOBLET_OF_EONOTHEM_ATTR.get();
                    case CIRCLET_OF_LOGOS -> ERConfig.CIRCLET_OF_LOGOS_ATTR.get();
                };


        attrs = config_get.toArray(new String[0]);
        int index = new Random().nextInt(attrs.length);
        String[] type = attrs[index].replaceAll(" ", "").split(",");

        MainAffix mainAffix = MainAffix.create(type) ;
        int minorCount = rarity - 1;
        if(rarity > 2){
            minorCount -= Mth.nextInt(RandomSource.create(), 0, 1) ;
        }
        return new ArtifactData(slot, mainAffix, MinorAffix.rollingList(minor, mainAffix, minorCount), effect, new ArtifactLevel(0,0,0), rarity);
    }
    
    public int getColor(){
        return switch (rarity){
            case 1 -> 0xffffff;
            case 2 -> 0x00aa00 ;
            case 3 -> 0x5555ff ;
            case 4 -> 0xaa00aa ;
            case 5 -> 0xffaa00 ;
            case 6 -> 0xff55ff ;
            case 7 -> 0x55ffff ;
            default -> 0xaa0000;
        };
    }

    public ArtifactData rolling(){
        return rolling(rarity) ;
    }

    public ArtifactData setRarity(int rarity){
        return new ArtifactData(slot, main, minor, effect, level, rarity) ;
    }

    public ArtifactData addMinor(@NonNull MinorAffix affix, int count){
        return new ArtifactData(slot, main, MinorAffix.addMinor(this.minor, affix, count), effect, level, rarity);
    }

    public ArtifactData setMain(@NonNull MainAffix affix){
        return new ArtifactData(slot, affix, minor, effect, level, rarity);
    }
}
