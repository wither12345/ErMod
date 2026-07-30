package net.wither.er.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

public class ElementalArmorItem extends ArmorItem {
    private final Holder<Attribute> attribute;
    private final ResourceLocation location ;
    private final double value ;
    private final AttributeModifier.Operation operation;

    public ElementalArmorItem(Holder<ArmorMaterial> armorMaterialHolder,
                              Type type,
                              Properties properties,
                              Holder<Attribute> attribute,
                              double value,
                              ResourceLocation location,
                              AttributeModifier.Operation operation) {
        super(armorMaterialHolder, type, properties);
        this.attribute = attribute;
        this.location = location;
        this.value = value;
        this.operation = operation;
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
        return super.getDefaultAttributeModifiers().withModifierAdded(attribute, new AttributeModifier(location, value, operation), EquipmentSlotGroup.bySlot(this.type.getSlot()));
    }

    public record Group(DeferredItem<Item> helmet, DeferredItem<Item> chest, DeferredItem<Item> leggings, DeferredItem<Item> boots){
        public static Group createBasic(DeferredRegister.Items register, Holder<ArmorMaterial> armorMaterialHolder, Element.Category category){
            return new Group(
                    register,
                    armorMaterialHolder,
                    category.getResAttr(),
                    10,
                    category.getSerializedName(),
                    AttributeModifier.Operation.ADD_VALUE,
                    15
            );
        }

        private Group(
                DeferredRegister.Items register,
                Holder<ArmorMaterial> armorMaterialHolder,
                Holder<Attribute> attribute,
                double value,
                String namePrefix,
                AttributeModifier.Operation operation,
                int durability
        ){
            this(
                    register.register(namePrefix + "_helmet", () -> new ElementalArmorItem(armorMaterialHolder, Type.HELMET,
                            new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(durability)), attribute, value,
                            ResourceLocation.fromNamespaceAndPath(register.getNamespace(), namePrefix + "." + "helmet"),
                            operation)),
                    register.register(namePrefix + "_chestplate", () -> new ElementalArmorItem(armorMaterialHolder, Type.CHESTPLATE,
                            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(durability)), attribute, value,
                            ResourceLocation.fromNamespaceAndPath(register.getNamespace(), namePrefix + "." + "chestplate"),
                            operation)),
                    register.register(namePrefix + "_leggings", () -> new ElementalArmorItem(armorMaterialHolder, Type.LEGGINGS,
                            new Item.Properties().durability(Type.LEGGINGS.getDurability(durability)), attribute, value,
                            ResourceLocation.fromNamespaceAndPath(register.getNamespace(), namePrefix + "." + "leggings"),
                            operation)),
                   register.register(namePrefix + "_boots", () -> new ElementalArmorItem(armorMaterialHolder, Type.BOOTS,
                           new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(durability)), attribute, value,
                           ResourceLocation.fromNamespaceAndPath(register.getNamespace(), namePrefix + "." + "boots"),
                           operation))
            );
        }
    }
}
