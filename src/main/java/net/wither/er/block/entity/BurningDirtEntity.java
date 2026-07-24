package net.wither.er.block.entity;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModBlockEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.wither.er.elements.Element;
import net.wither.er.entity.ErEntityInterface;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class BurningDirtEntity extends BlockEntity implements TraceableEntity {
    private static final TagKey<Block> BURNABLE_TAG = TagKey.create(Registries.BLOCK, new ResourceLocation(ErMod.MODID, "burnable"));
    private static final TagKey<EntityType<?>> BURNING_BYPASSES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(ErMod.MODID, "bypasses_block_burning"));
    @Nullable private UUID ownerUUID;
    @Nullable private Entity cachedOwner;
    private int age;

    public BurningDirtEntity(BlockPos position, BlockState state) {
        super(ErModBlockEntities.BURNING_DIRT.get(), position, state);
    }

    public static void ticking(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if(level instanceof ClientLevel clientLevel){
            clientLevel.addParticle(ParticleTypes.FLAME, (pos.getX() + Math.random()), (pos.getY() + 1.05), (pos.getZ() + Math.random()), 0, 0, 0);
        }
        else if(entity instanceof BurningDirtEntity burningDirtEntity){
            burningDirtEntity.age ++ ;
            burningDirtEntity.modifyBlockUpper();
            if(burningDirtEntity.age > 100){
                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
            }
        }
    }

    public void burn(@NotNull Entity entity) {
        if(this.level instanceof ServerLevel serverLevel &&
                !entity.getType().is(BURNING_BYPASSES) &&
                entity instanceof ErEntityInterface erEntityInterface &&
                erEntityInterface.er$shouldBurnBlock(serverLevel.getGameTime())) {
            Entity owner = this.getOwner();
            entity.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(Element.BURNING), owner), EntityHurtEvent.getLevelMultiply(getOwner()));
        }
    }

    private void modifyBlockUpper(){
        if(this.level instanceof ServerLevel serverLevel && serverLevel.getBlockState(this.getBlockPos().above()).is(BURNABLE_TAG)){
            serverLevel.setBlock(this.getBlockPos(), Blocks.DIRT.defaultBlockState(), 3);
            serverLevel.setBlock(this.getBlockPos().above(), Blocks.AIR.defaultBlockState(), 3);
        }
    }
    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
            this.cachedOwner = null;
        }
        this.age = tag.getInt("Age");
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.ownerUUID != null) tag.putUUID("Owner", this.ownerUUID);
        tag.putInt("Age", this.age);
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUUID = entity.getUUID();
            this.cachedOwner = entity;
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else {
            if (this.ownerUUID != null) {
                Level var2 = this.getLevel();
                if (var2 instanceof ServerLevel serverlevel) {
                    this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
                    return this.cachedOwner;
                }
            }

            return null;
        }
    }
}
