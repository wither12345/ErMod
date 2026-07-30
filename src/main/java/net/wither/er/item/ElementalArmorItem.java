package net.wither.er.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.UUID;

public class ElementalArmorItem extends ArmorItem {
    private static final EnumMap<Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        map.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        map.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        map.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });

    private final RegistryObject<Attribute> attribute;
    private final UUID uuid;
    private final double value ;
    private final AttributeModifier.Operation operation;
    private final String location;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public static final EnumMap<Type, Integer> IRON_ARMOR = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 2);
        map.put(ArmorItem.Type.LEGGINGS, 5);
        map.put(ArmorItem.Type.CHESTPLATE, 6);
        map.put(ArmorItem.Type.HELMET, 2);
    });

    public ElementalArmorItem(ArmorMaterial armorMaterial,
                              Type type,
                              Properties properties,
                              RegistryObject<Attribute> attribute,
                              double value,
                              UUID uuid,
                              AttributeModifier.Operation operation, String location) {
        super(armorMaterial, type, properties);
        this.attribute = attribute;
        this.value = value;
        this.uuid = uuid;
        this.operation = operation;
        this.location = location;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid_ = (UUID)ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        double defense = armorMaterial.getDefenseForType(type);
        double toughness = armorMaterial.getToughness();
        double knockbackResistance = armorMaterial.getKnockbackResistance();
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid_, "Armor modifier", defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", toughness, AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0.0F) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        builder.put(attribute.get(), new AttributeModifier(uuid, this.type.getName(), value, operation));
        this.defaultModifiers = builder.build();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return location;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public record Group(RegistryObject<Item> helmet, RegistryObject<Item> chest, RegistryObject<Item> leggings, RegistryObject<Item> boots){
        public static Group createBasic(DeferredRegister<Item> register, ArmorMaterial armorMaterial, Element.Category category){
            return new Group(
                    register,
                    armorMaterial,
                    category.getResHolder(),
                    10,
                    category.getSerializedName(),
                    AttributeModifier.Operation.ADDITION,
                    "er:textures/models/armor/" + category.getSerializedName() + "__layer_"
            );
        }

        private Group(
                DeferredRegister<Item> register,
                ArmorMaterial armorMaterial,
                RegistryObject<Attribute> attribute,
                double value,
                String namePrefix,
                AttributeModifier.Operation operation,
                String locate
        ){
            this(
                    register.register(namePrefix + "_helmet", () -> new ElementalArmorItem(armorMaterial, Type.HELMET,
                            new Properties(), attribute, value,
                            UUID.fromString("6D434B3A-C823-C57C-2A06-D9160A987852"),
                            operation,
                            locate + "1.png"
                    )),
                    register.register(namePrefix + "_chestplate", () -> new ElementalArmorItem(armorMaterial, Type.CHESTPLATE,
                            new Properties(), attribute, value,
                            UUID.fromString("530B92FB-21FA-7C2F-23D7-4942FE286559"),
                            operation,
                            locate + "1.png"
                    )),
                    register.register(namePrefix + "_leggings", () -> new ElementalArmorItem(armorMaterial, Type.LEGGINGS,
                            new Properties(), attribute, value,
                            UUID.fromString("202284AE-D62C-0EE7-D30F-7C81FC804468"),
                            operation,
                            locate + "2.png"
                    )),
                    register.register(namePrefix + "_boots", () -> new ElementalArmorItem(armorMaterial, Type.BOOTS,
                            new Properties(), attribute, value,
                            UUID.fromString("DEBE4F1D-8F04-E294-7F22-9EECF1CD2D51"),
                            operation,
                            locate + "1.png"
                    ))
            );
        }
    }
}
