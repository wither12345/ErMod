package net.wither.er.item.data.artifactdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.ERConfig;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.item.Artifact;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.wither.er.entity.ArtifactSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public record MinorAffix(Holder<Attribute> attribute, double amount, boolean multi, int count, int upgrade) {
    public static final Codec<MinorAffix> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, MinorAffix> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Attribute.CODEC.fieldOf("attribute").forGetter(MinorAffix::attribute),
                        Codec.DOUBLE.fieldOf("amount").forGetter(MinorAffix::amount),
                        Codec.BOOL.fieldOf("multi").forGetter(MinorAffix::multi),
                        Codec.INT.fieldOf("count").forGetter(MinorAffix::count),
                        Codec.INT.fieldOf("upgrade").orElse(0).forGetter(MinorAffix::upgrade)
                ).apply(instance, MinorAffix::new));
        STREAM_CODEC = StreamCodec.composite(
                Attribute.STREAM_CODEC, MinorAffix::attribute,
                ByteBufCodecs.DOUBLE, MinorAffix::amount,
                ByteBufCodecs.BOOL, MinorAffix::multi,
                ByteBufCodecs.INT, MinorAffix::count,
                ByteBufCodecs.INT, MinorAffix::upgrade,
                MinorAffix::new
        ) ;
    }

    public static List<MinorAffix> rollingList(final List<MinorAffix> affixList, MainAffix mainAffix, int count){
        List<MinorAffix> mutableList = new ArrayList<>(affixList);
        if(mutableList.size() < 4) {
            List<? extends  String> config_get = ERConfig.MINOR_ATTR.get();
            int attr_size = config_get.size();
            int[] rand = randomArray(attr_size);
            String[] effects_type = config_get.toArray(new String[0]);
            int i = -1;
            while (mutableList.size() < 4 && count > 0) {
                i++;
                String[] type = effects_type[rand[i]].replaceAll(" ", "").split(",");
                Optional<Holder.Reference<Attribute>> attributeHolder = BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(type[0]));
                if (type.length < 2 || attributeHolder.isEmpty() || attributeHolder.get() == mainAffix.attribute())
                    continue;
                boolean flag = false ;
                for(MinorAffix affix : affixList){
                    if(affix.attribute == attributeHolder.get())
                        flag = true ;
                }
                if(flag) continue;
                mutableList.add(new MinorAffix(attributeHolder.get(), Double.parseDouble(type[1]), type[2].equals("1"), 10, 0)) ;
                count--;
            }
        }
        int size = mutableList.size() ;
        int[] minor_rolling = new int[size] ;
        while(count -- > 0){
            int index = Mth.nextInt(RandomSource.create(), 0, size - 1);
            minor_rolling[index] += 1 ;
        }
        for(int i = 0; i < size; i ++){
            mutableList.set(i, mutableList.get(i).add(minor_rolling[i]));
        }
        return mutableList;
    }

    public static List<MinorAffix> addMinor(final List<MinorAffix> affixList, MinorAffix affixToAdd, int c){
        List<MinorAffix> mutableList = new ArrayList<>(affixList);
        for(MinorAffix affix: mutableList){
            if(affix.attribute == affixToAdd.attribute && affix.multi == affixToAdd.multi && affix.amount == affixToAdd.amount){
                mutableList.replaceAll(minor -> minor.add(affixToAdd.count, c));
                return mutableList;
            }
        }
        mutableList.add(affixToAdd) ;
        return mutableList;
    }

    public void remove(ArtifactSlot slot, LivingEntity entity, int id){
        AttributeInstance instance = entity.getAttribute(attribute) ;
        if (instance != null) {
            instance.removeModifier(getResourceLocation(slot, id)) ;
        }
    }

    public void apply(ArtifactSlot slot, LivingEntity entity, int rarity, int id){
        AttributeInstance instance = entity.getAttribute(attribute) ;
        if (instance != null) {
            instance.addOrUpdateTransientModifier(new AttributeModifier(getResourceLocation(slot, id),
                    amount * Artifact.getScaling(0,rarity,this.getAtrType()) * count / 10,
                    multi ? AttributeModifier.Operation.ADD_MULTIPLIED_BASE : AttributeModifier.Operation.ADD_VALUE)); ;
        }
    }

    private ResourceLocation getResourceLocation(ArtifactSlot slot, int id){
        return ResourceLocation.parse("er:artifact." + slot.toString().toLowerCase() + ".main." + id) ;
    }

    private MinorAffix add(int c, int t) {
        return new MinorAffix(attribute, amount, multi, count + c, upgrade + t);
    }

    private MinorAffix add(int i) {
        return new MinorAffix(attribute, amount, multi, count + i * 10, upgrade + i);
    }

    public String toString(){
        return toString(5);
    }

    public static MinorAffix create(String[] type){
        Optional<Holder.Reference<Attribute>> attributeHolder = BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(type[0]));
        if(attributeHolder.isEmpty() || type.length <= 2)
            return new MinorAffix(Attributes.MAX_HEALTH,0,false, 10, 0) ;
        return new MinorAffix(attributeHolder.get(), Double.parseDouble(type[1]), type[2].equals("1"), 10, 0) ;
    }

    public String toString(int rarity){
        return Component.translatable(attribute.value().getDescriptionId()).getString() + ":"
                + new java.text.DecimalFormat(attribute == ErModAttributes.CRIT_RATE.getDelegate() || attribute == ErModAttributes.CRIT_DAMAGE.getDelegate() || multi ? "##.#%" : "##.#")
                .format(this.calculate(rarity));
    }

    public double calculate(int rarity){
        return amount * count / 10 * Artifact.getScaling(0,rarity,this.getAtrType());
    }

    private int getAtrType(){
        if(multi){
            if(attribute == Attributes.ATTACK_DAMAGE)
                return 1 ;
            if(attribute == Attributes.MAX_HEALTH)
                return 2 ;
        }
        return  0 ;
    }

    private static int[] randomArray(int n) {
        int[] arr = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }
}
