package net.wither.er.entity.whopperflower;

import net.mcreator.er.init.ErModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.NotNull;

public class PyroWhopperflower extends Whopperflower{
    public int homingOrbCd;
    public PyroWhopperflower(EntityType<PyroWhopperflower> type, Level level) {
        super(type, level);
    }

    @Override
    public @NotNull ItemStack getFruitItem() {
        return new ItemStack(ErModItems.PYRO_WHOPPERFLOWER_FRUIT.get());
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Whopperflower.createAttributes();
        builder = builder.add(ErAttributeRegister.PYRO_RES, 75);
        return builder;
    }

    @Override
    public void tick() {
        super.tick();
        this.homingOrbCd--;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new PopGoal(this, ElementRegistry.PYRO.get()));
        this.goalSelector.addGoal(6, new SpinGoal(this, 0.33f, ElementRegistry.PYRO.get()));
        this.goalSelector.addGoal(6, new HomingOrbGoal(this));
        this.goalSelector.addGoal(6, new PyroConsumeFruitGoal(this));
    }
}
