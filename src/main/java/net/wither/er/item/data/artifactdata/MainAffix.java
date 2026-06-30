package net.wither.er.item.data.artifactdata;

import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.item.Artifact;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;
import net.wither.er.entity.ArtifactSlot;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class MainAffix {

    private Attribute attribute;
    private double amount;
    private boolean multi;

    public MainAffix(Attribute attribute, double amount, boolean multi) {
        this.attribute = attribute;
        this.amount = amount;
        this.multi = multi;
    }

    public void remove(ArtifactSlot slot, LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(getResourceLocation(slot));
        }
    }

    public void apply(ArtifactSlot slot, LivingEntity entity, int level, int rarity) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.addTransientModifier(new AttributeModifier(getResourceLocation(slot), "artifact", amount * Artifact.getScaling(level, rarity, this.getAtrType()), multi ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION));
        }
    }

    private UUID getResourceLocation(ArtifactSlot slot) {
        return UUID.fromString(switch (slot) {
            case FLOWER_OF_LIFE -> "1774C151-E661-BD14-5A88-A3A50EB9ACA9";
            case PLUME_OF_DEATH -> "44F9FE56-19B3-7655-E379-CD5A135BCEB9";
            case SAND_OF_EON -> "65ED2AD5-6DF9-CF76-082B-6B229255BCE9";
            case GOBLET_OF_EONOTHEM -> "0E305E56-EA7D-33C0-A725-641EF00FCB89";
            case CIRCLET_OF_LOGOS -> "20A38181-14B0-3DE1-82EF-CC2BED50F0F9";
        });
    }

    public String toString() {
        return toString(0, 5);
    }

    public String toString(int level, int rarity) {
        return Component.translatable(attribute.getDescriptionId()).getString() + ":"
                + new DecimalFormat(attribute == ErModAttributes.CRIT_RATE.get() || attribute == ErModAttributes.CRIT_DAMAGE.get() || multi ? "##.#%" : "##.#")
                .format(calculate(level, rarity));
    }

    public double calculate(int level, int rarity) {
        return amount * Artifact.getScaling(level, rarity, this.getAtrType());
    }

    private int getAtrType() {
        if (multi) {
            if (attribute == Attributes.ATTACK_DAMAGE)
                return 1;
            if (attribute == Attributes.MAX_HEALTH)
                return 2;
        }
        return 0;
    }

    public MainAffix create(String[] type) {
        Optional<Holder.Reference<Attribute>> attributeHolder = ForgeRegistries.ATTRIBUTES.getDelegate(new ResourceLocation(type[0]));
        if (attributeHolder.isEmpty() || type.length <= 2)
            return this;
        this.attribute = attributeHolder.get().value();
        this.amount = Double.parseDouble(type[1]);
        this.multi = type[2].equals("1");
        return this;
    }

    public static MainAffix createNew(String s) {
        String[] type = s.split(",");
        Optional<Holder.Reference<Attribute>> attributeHolder = ForgeRegistries.ATTRIBUTES.getDelegate(new ResourceLocation(type[0]));
        if (attributeHolder.isEmpty() || type.length <= 2)
            return new MainAffix(Attributes.MAX_HEALTH, 0, false);
        return new MainAffix(attributeHolder.get().get(), Double.parseDouble(type[1]), type[2].equals("1"));
    }

    public Attribute attribute() {
        return attribute;
    }

    public double amount() {
        return amount;
    }

    public boolean multi() {
        return multi;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MainAffix) obj;
        return Objects.equals(this.attribute, that.attribute) &&
                Double.doubleToLongBits(this.amount) == Double.doubleToLongBits(that.amount) &&
                this.multi == that.multi;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, amount, multi);
    }

    public CompoundTag getTag(){
        CompoundTag ret = new CompoundTag();
        ret.putString("attr", ForgeRegistries.ATTRIBUTES.getKey(this.attribute).toString());
        ret.putDouble("amount", this.amount);
        ret.putBoolean("multi", this.multi);
        return ret;
    }

    public static MainAffix getByTag(CompoundTag tag){
        Optional<Holder.Reference<Attribute>> attributeHolder = ForgeRegistries.ATTRIBUTES.getDelegate(new ResourceLocation(tag.getString("attr")));
        return attributeHolder.map(attributeReference -> new MainAffix(attributeReference.get(), tag.getDouble("amount"), tag.getBoolean("multi"))).orElseGet(() -> new MainAffix(Attributes.MAX_HEALTH, 0, false));
    }
}
