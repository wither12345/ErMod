package net.wither.er.item.data.artifactdata;

import net.mcreator.er.ERConfig;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.item.Artifact;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;
import net.wither.er.entity.ArtifactSlot;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public record MinorAffix(Attribute attribute, double amount, boolean multi, int count, int upgrade) {

    public static void rollingList(List<MinorAffix> affixList, MainAffix MinorAffix, int count){
        if(affixList.size() < 4) {
            List<? extends  String> config_get = ERConfig.MINOR_ATTR.get();
            int attr_size = config_get.size();
            int[] rand = randomArray(attr_size);
            String[] effects_type = config_get.toArray(new String[0]);
            int i = -1;
            while (affixList.size() < 4 && count > 0) {
                i++;
                String[] type = effects_type[rand[i]].replaceAll(" ", "").split(",");
                Optional<Holder.Reference<Attribute>> attributeHolder = ForgeRegistries.ATTRIBUTES.getDelegate(new ResourceLocation(type[0]));
                if(attributeHolder.isEmpty())
                    continue;
                Attribute newAttribute = attributeHolder.get().get();
                if (type.length < 2  ||newAttribute == MinorAffix.attribute())
                    continue;
                boolean flag = false ;
                for(MinorAffix affix : affixList){
                    if (affix.attribute == newAttribute) {
                        flag = true;
                        break;
                    }
                }
                if(flag) continue;
                affixList.add(new MinorAffix(newAttribute, Double.parseDouble(type[1]), type[2].equals("1"), 10, 0)) ;
                count--;
            }
        }
        int size = affixList.size() ;
        int[] minor_rolling = new int[size] ;
        while(count -- > 0){
            int index = Mth.nextInt(RandomSource.create(), 0, size - 1);
            minor_rolling[index] += 1 ;
        }
        for(int i = 0; i < size; i ++){
            affixList.set(i, affixList.get(i).add(minor_rolling[i]));
        }
    }

    public static void addMinor(List<MinorAffix> affixList, MinorAffix affixToAdd, int c){
        for(MinorAffix affix: affixList){
            if(affix.attribute == affixToAdd.attribute && affix.multi == affixToAdd.multi && affix.amount == affixToAdd.amount){
                affixList.replaceAll(minor -> minor.add(affixToAdd.count, c));
                return;
            }
        }
        affixList.add(affixToAdd) ;
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
            instance.addTransientModifier(new AttributeModifier(getResourceLocation(slot, id),
                    "artifact_minor",
                    amount * Artifact.getScaling(0,rarity,this.getAtrType()) * count / 10,
                    multi ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION));
        }
    }

    private UUID getResourceLocation(ArtifactSlot slot, int id){
        return UUID.fromString(switch (slot){
            case FLOWER_OF_LIFE -> "1774C151-E661-BD14-5A88-A3A50EB9ACA" + id ;
            case PLUME_OF_DEATH -> "44F9FE56-19B3-7655-E379-CD5A135BCEB" + id ;
            case SAND_OF_EON -> "65ED2AD5-6DF9-CF76-082B-6B229255BCE" + id ;
            case GOBLET_OF_EONOTHEM -> "0E305E56-EA7D-33C0-A725-641EF00FCB8" + id ;
            case CIRCLET_OF_LOGOS -> "20A38181-14B0-3DE1-82EF-CC2BED50F0F" + id ;
        });
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

    public static MinorAffix create(String s){
        String[] type = s.split(",");
        Optional<Holder.Reference<Attribute>> attributeHolder = ForgeRegistries.ATTRIBUTES.getDelegate(new ResourceLocation(type[0]));
        if(attributeHolder.isEmpty())
            return new MinorAffix(Attributes.MAX_HEALTH,0,false, 10, 0) ;
        Attribute newAttr = attributeHolder.get().value();
        if(type.length <= 2)
            return new MinorAffix(Attributes.MAX_HEALTH,0,false, 10, 0) ;
        return new MinorAffix(newAttr, Double.parseDouble(type[1]), type[2].equals("1"), 10, 0) ;
    }

    public String toString(int rarity){
        return Component.translatable(attribute.getDescriptionId()).getString() + ":"
                + new java.text.DecimalFormat(attribute == ErModAttributes.CRIT_RATE.get() || attribute == ErModAttributes.CRIT_DAMAGE.get() || multi ? "##.#%" : "##.#")
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

    public CompoundTag getTag(){
        CompoundTag ret = new CompoundTag();
        ret.putString("attr", ForgeRegistries.ATTRIBUTES.getKey(this.attribute).toString());
        ret.putDouble("amount", this.amount);
        ret.putBoolean("multi", this.multi);
        ret.putInt("count", this.count);
        ret.putInt("upgrade", this.upgrade);
        return ret;
    }

    public static MinorAffix getByTag(CompoundTag tag){
        Optional<Holder.Reference<Attribute>> attributeHolder = ForgeRegistries.ATTRIBUTES.getDelegate(new ResourceLocation(tag.getString("attr")));
        return attributeHolder.map(attributeReference -> new MinorAffix(attributeReference.get(), tag.getDouble("amount"), tag.getBoolean("multi"), tag.getInt("count"), tag.getInt("upgrade")))
                .orElseGet(() -> new MinorAffix(Attributes.MAX_HEALTH,0,false, 10, 0)) ;
    }
}
