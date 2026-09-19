package net.wither.er;

import net.mcreator.er.ErMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.SingleElementalContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ElementalLivingEntity extends LivingEntity {
    private final Element element;
    private static final ResourceLocation ELEMENTAL_LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "init");

    protected ElementalLivingEntity(EntityType<? extends LivingEntity> entityType, Level level, Element element) {
        super(entityType, level);
        this.element = element;
        if(this instanceof AuraContainerInterface containerInterface)
            containerInterface.er$getAuraContainer().addAura(new ElementSource(element, ELEMENTAL_LOCATION, 3, true, true));
    }

    protected Element getElement(){
        return this.element;
    }

    protected abstract int getMaxAura();

    protected float getAura(){
        if(this instanceof AuraContainerInterface auraContainerInterface) {
            SingleElementalContainer container = auraContainerInterface.er$getAuraContainer().getAura().get(getElement().getCategory().getId());
            return container.getGauge(getElement());
        }
        return 0;
    }

    @Override
    public float getHealth() {
        return this.getMaxHealth() / this.getMaxAura() * this.getAura();
    }

    @Override
    public void tick() {
        if(this.getAura() <= 0 && !this.isDeadOrDying())
            this.die(this.damageSources().generic());
        super.tick();
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot equipmentSlot, @NotNull ItemStack itemStack) {

    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
}
