package net.wither.er.entity.whopperflower;

import net.mcreator.er.ErMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.wither.er.entity.goals.NoOwnerTargetGoal;
import net.wither.er.entity.goals.OwnableHurtByTargetGoal;
import net.wither.er.entity.goals.SyncTargetGoal;
import net.wither.er.init.ErAttributeRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Whopperflower extends PathfinderMob implements OwnableEntity, IShearable {
    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(Whopperflower.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FRUIT_COUNT = SynchedEntityData.defineId(Whopperflower.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<BlockState> DISGUISED_BLOCK = SynchedEntityData.defineId(Whopperflower.class, EntityDataSerializers.BLOCK_STATE);
    private static final AttributeModifier KNOCKBACK = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "whopperflower"), 200, AttributeModifier.Operation.ADD_VALUE);
    private static final EntityDimensions HIDE = EntityDimensions.fixed(1,1);

    private UUID ownerUUID ;
    private LivingEntity cachedOwner;
    public float animationStart = 0;
    public Action lastAction = Action.NORMAL;
    @Nullable private BlockState rememberedState = null;
    public boolean canDisguise = true;
    public float dy = 0;
    public int consumeFruitCd;
    public int fruitSpawnCd = 0 ;
    public int borrowCd;
    public int spinCd;
    public int cd;
;

    protected Whopperflower(EntityType<? extends Whopperflower> type, Level level) {
        super(type, level);
    }

    @NotNull public abstract ItemStack getFruitItem();

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new HidingGoal(this));
        this.goalSelector.addGoal(8, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(6, new TeleportToTargetGoal(this, 25));
        this.targetSelector.addGoal(1, new SyncTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnableHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NoOwnerTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new NoOwnerTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NoOwnerTargetGoal<>(this, Bee.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0);
        builder = builder.add(Attributes.MAX_HEALTH, 20);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 2.5);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0);
        builder = builder.add(ErAttributeRegister.ANEMO_RES, 35);
        builder = builder.add(ErAttributeRegister.CRYO_RES, 35);
        builder = builder.add(ErAttributeRegister.GEO_RES, 35);
        builder = builder.add(ErAttributeRegister.DENDRO_RES, 35);
        builder = builder.add(ErAttributeRegister.PYRO_RES, 35);
        builder = builder.add(ErAttributeRegister.HYDRO_RES, 35);
        builder = builder.add(ErAttributeRegister.ELECTRO_RES, 35);
        builder = builder.add(ErAttributeRegister.PHYSICAL_RES, 35);
        return builder;
    }

    public static void init(RegisterSpawnPlacementsEvent event, EntityType<? extends Mob> type) {
        event.register(type, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)),
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @Override
    public void tick() {
        super.tick();
        this.cd -- ;
        this.consumeFruitCd --;
        this.borrowCd --;
        this.spinCd --;
        this.fruitSpawnCd --;
    }

    public void trySpawnFruit(){
        if(this.fruitSpawnCd <= 0){
            this.setFruitCount(3);
            this.fruitSpawnCd = 2400;
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setAction(tag.getInt("action"));
        this.setFruitCount(tag.getInt("fruitCount"));
        if(tag.contains("DisguiseBlock"))
            this.setDisguisedBlock(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), tag.getCompound("DisguiseBlock")));
        if(tag.contains("rememberedState"))
            this.rememberedState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), tag.getCompound("rememberedState"));
        if(tag.contains("ownerUUID"))
            this.ownerUUID = tag.getUUID("ownerUUID");
        this.fruitSpawnCd = tag.getInt("fruitCd");
        this.canDisguise = tag.getBoolean("canDisguise");
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("action", this.getAction().save());
        tag.putInt("fruitCount", this.getFruitCount());
        tag.put("DisguiseBlock", NbtUtils.writeBlockState(this.getDisguiseBlockState()));
        if(this.rememberedState != null)
            tag.put("rememberedState", NbtUtils.writeBlockState(this.rememberedState));
        if(this.ownerUUID != null)
            tag.putUUID("ownerUUID", this.ownerUUID);
        tag.putInt("fruitCd", this.fruitSpawnCd);
        tag.putBoolean("canDisguise", this.canDisguise);
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(this.level() instanceof ServerLevel ){
            if(player.getItemInHand(hand).getItem() instanceof BlockItem blockItem && player.isCreative()) {
                this.rememberedState = blockItem.getBlock().defaultBlockState();
                if (this.isDisguise()) this.setDisguisedBlock(rememberedState);
                return InteractionResult.SUCCESS_NO_ITEM_USED;
            }
            else if(this.getOwner() == player && this.cd <= 0) {
                this.canDisguise = !this.canDisguise;
                this.cd = 3;
                return InteractionResult.SUCCESS_NO_ITEM_USED;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isShearable(@Nullable Player player, @NotNull ItemStack item, @NotNull Level level, @NotNull BlockPos pos) {
        return this.getFruitCount() > 0 && this.getOwner() == player;
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, @NotNull Level level, @NotNull BlockPos pos) {
        ItemStack stack = this.getFruitItem();
        stack.setCount(this.getFruitCount());
        this.setFruitCount(0);
        return List.of(stack);
    }

    public boolean isDisguise(){
        return this.getAction() == Action.DISGUISED;
    }

    public BlockState getDisguiseBlockState(){
        return this.entityData.get(DISGUISED_BLOCK);
    }

    public void disguise(){
        this.setDisguisedBlock(Objects.requireNonNullElseGet(this.rememberedState, Whopperflower::getRandomBlock));
    }

    public static BlockState getRandomBlock(){
        return (BuiltInRegistries.BLOCK.getOrCreateTag(BlockTags.SMALL_FLOWERS).getRandomElement(RandomSource.create()).orElseGet(() -> BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.POPPY)).value()).defaultBlockState();
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return this.isDisguise() ? HIDE : super.getDefaultDimensions(pose);
    }

    public void setDisguisedBlock(BlockState block){
        this.entityData.set(DISGUISED_BLOCK, block);
        onBlockUpdate(block);
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public void setOwner(@Nullable LivingEntity entity){
        this.cachedOwner = entity;
        if(entity != null)
            this.ownerUUID = entity.getUUID();
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        if(this.cachedOwner != null) return cachedOwner;
        if(this.ownerUUID == null) return null;
        cachedOwner = (this.level() instanceof ServerLevel serverLevel && serverLevel.getEntity(ownerUUID) instanceof LivingEntity living) ? living : this.level().getPlayerByUUID(ownerUUID);
        return cachedOwner;
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        if(accessor == DISGUISED_BLOCK){
            onBlockUpdate(this.entityData.get(DISGUISED_BLOCK));
        }
    }

    private void onBlockUpdate(BlockState state){
        AttributeInstance instance = this.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if(instance == null) return;
        if(state.getBlock() == Blocks.AIR) {
            instance.removeModifier(KNOCKBACK);
            setAction(Action.NORMAL);
        }
        else {
            BlockPos pos = this.getOnPos();
            this.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            instance.addOrUpdateTransientModifier(KNOCKBACK);
            setAction(Action.DISGUISED);
        }
        refreshDimensions();
    }

    @Override
    public void push(@NotNull Entity entity) {
        if(!this.isDisguise())
            super.push(entity);
    }

    @Override
    public boolean isPushable() {
        return !this.isDisguise();
    }

    boolean borrowTowards(Entity target) {
        Vec3 vec3 = new Vec3(this.getX() - target.getX(), this.getY(0.5) - target.getEyeY(), this.getZ() - target.getZ());
        vec3 = vec3.normalize();
        double d1 = this.getX() + (this.random.nextDouble() - (double)0.5F) * (double)4 - vec3.x * (double)8;
        double d2 = this.getY() + (double)(this.random.nextInt(8) - 4) - vec3.y * (double)8F;
        double d3 = this.getZ() + (this.random.nextDouble() - (double)0.5F) * (double)4 - vec3.z * (double)8F;
        return this.borrow(d1, d2, d3);
    }

    private boolean borrow(double x, double y, double z) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(x, y, z);

        while(blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1)
            return this.randomTeleport(x, y, z, false);
        return false;
    }

    public Action getAction(){
        return Action.from(this.entityData.get(ACTION));
    }

    public void setAction(Action a){
        setAction(a.ordinal());
    }

    public void setAction(int i){
        this.entityData.set(ACTION, i);
    }

    public int getFruitCount() {
        return this.entityData.get(FRUIT_COUNT);
    }

    public void setFruitCount(int i){
        this.entityData.set(FRUIT_COUNT, i);
    }

    public void consumeFruit() {
        this.setFruitCount(this.getFruitCount() - 1);
    }

    public @NotNull Vec3 getDeltaMovement() {
        return this.isDisguise()? Vec3.ZERO : super.getDeltaMovement();
    }

    public void setDeltaMovement(@NotNull Vec3 vec3) {
        if(!this.isDisguise())
            super.setDeltaMovement(vec3);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTION, Action.NORMAL.ordinal());
        builder.define(FRUIT_COUNT, 0);
        builder.define(DISGUISED_BLOCK, Blocks.AIR.defaultBlockState());
    }

    public enum Action{
        NORMAL,
        UP,
        DOWN,
        OPENING,
        CLOSING,
        SPIN,
        CONSUMING,
        SHIELD,
        STUN,
        FIRE,
        FIRE_CONSTANT,
        LOWER_HEAD,
        DISGUISED;

        public static Action from(int i){
            return Action.values()[i];
        }

        public int save(){
            if(this == DISGUISED) return this.ordinal();
            return NORMAL.ordinal();
        }
    }
}
