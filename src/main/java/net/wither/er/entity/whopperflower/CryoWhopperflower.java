package net.wither.er.entity.whopperflower;

import net.mcreator.er.init.ErModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.NotNull;

public class CryoWhopperflower extends Whopperflower{
    public CryoWhopperflower(EntityType<CryoWhopperflower> type, Level level) {
        super(type, level);
    }
    public int spikeCd ;

    @Override
    public void tick() {
        super.tick();
        this.spikeCd --;
    }

    @Override
    public @NotNull ItemStack getFruitItem() {
        return new ItemStack(ErModItems.CRYO_WHOPPERFLOWER_FRUIT.get());
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Whopperflower.createAttributes();
        builder = builder.add(ErAttributeRegister.CRYO_RES, 75);
        return builder;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new PopGoal(this, ElementRegistry.CRYO.get()));
        this.goalSelector.addGoal(6, new SpinGoal(this, 0.5f, ElementRegistry.CRYO.get()));
        this.goalSelector.addGoal(6, new CrystallineSpikesGoal(this));
        this.goalSelector.addGoal(6, new CryoConsumeFruitGoal(this));
    }
}
