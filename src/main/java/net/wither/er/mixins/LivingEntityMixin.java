package net.wither.er.mixins;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.artifact_effect.TwoSetAttrEffect;
import net.wither.er.elements.AuraContainer;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.entity.ArtifactSlot;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.network.ErShieldData;
import net.wither.er.entity.listener.onHealthFloating;
import net.wither.er.shield.ErShield;
import net.wither.er.shield.ShieldRegistry;
import net.wither.er.shield.ShieldStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ErEntityInterface , AuraContainerInterface {
	@Unique private final ArrayList<ShieldStack> er$shields = new ArrayList<>();
	@Unique private final AuraContainer er$container = new AuraContainer(this);
    @Unique private float er$lastHealth ;
	@Unique private static final EntityDataAccessor<Integer> ER$ELEMENT = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
	@Unique private final NonNullList<ItemStack> er$artifactItem = NonNullList.withSize(5, ItemStack.EMPTY);
	@Unique private Object2IntMap<ArtifactEffect> er$effectMap = new Object2IntArrayMap<>();

    @Shadow public abstract AttributeMap getAttributes();
    @Shadow public abstract ItemStack getMainHandItem();
    @Shadow public abstract float getHealth();
    @Unique private long er$lastBurn;

    @Shadow
    @Final
    public int invulnerableDuration;

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
		super(p_19870_, p_19871_);
	}

	public List<ShieldStack> er$getShieldStacks() {
		return this.er$shields;
	}

	public List<ErShield> er$getShields() {
		List<ErShield> erShields = new ArrayList<>();
		for (ShieldStack shield : er$shields)
			erShields.add(shield.getShield());
		return erShields;
	}

	public void er$addShield(ShieldStack shield) {
		shield.getShield().start(this);
		this.er$shields.add(shield);
		er$syncShield();
	}

	@Override
	public AuraContainer er$getAuraContainer() {
		return er$container;
	}

	@Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
	public void addAdditionalSaveData(CompoundTag compound, CallbackInfo info) {
		CompoundTag tag = new CompoundTag();
		for (ShieldStack shield : er$shields) {
			tag.put(ShieldRegistry.SHIELD_REGISTRY.getKey(shield.getShield()).toString(), shield.toTag());
		}
		compound.put("ErShield", tag);

		ListTag listtag = new ListTag();

        for (ItemStack itemstack : this.er$artifactItem) {
            if (!itemstack.isEmpty()) {
                listtag.add(itemstack.save(new CompoundTag()));
            } else {
                listtag.add(new CompoundTag());
            }
        }

		compound.put("ArtifactItems", listtag);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
	public void readAdditionalSaveData(CompoundTag compound, CallbackInfo info) {
		er$setShields(compound.getCompound("ErShield"));

		CompoundTag compoundtag1;
		if (compound.contains("ArtifactItems", 9)) {
			ListTag list_tag = compound.getList("ArtifactItems", 10);
            for(ArtifactSlot slot: ArtifactSlot.values()){
                compoundtag1 = list_tag.getCompound(slot.getId());
                this.er$setArtifact(slot, ItemStack.of(compoundtag1));
            }

			this.er$updateArtifact();
		}
	}

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo ci){
        if(this.getHealth() != this.er$lastHealth){
            onHealthFloating.onFloating((LivingEntity)(Object)this, this.getHealth() - this.er$lastHealth);
            this.er$lastHealth = getHealth();
        }
    }

	@Inject(method = "defineSynchedData" , at = @At("TAIL"))
	protected void defineSynchedData(CallbackInfo info) {
		this.entityData.define(ER$ELEMENT, 0);
	}

	@Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
	public void dropAllDeathLoot(DamageSource source, CallbackInfo info) {
		this.er$dropArtifact();
	}

    @ModifyArg(method = "hurt" , at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"), index = 0)
    public TagKey<DamageType> modifyKnockBack(TagKey<DamageType> p_270890_){
        if(p_270890_ == DamageTypeTags.IS_EXPLOSION)
            return EntityHurtEvent.ER$NO_KB;
        return p_270890_;
    }

    @Inject(method = "createLivingAttributes", at = @At("TAIL"))
    private static void createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir){
        cir.getReturnValue()
                .add(ErAttributeRegister.ANEMO_RES.get())
                .add(ErAttributeRegister.CRYO_RES.get())
                .add(ErAttributeRegister.HYDRO_RES.get())
                .add(ErAttributeRegister.GEO_RES.get())
                .add(ErAttributeRegister.DENDRO_RES.get())
                .add(ErAttributeRegister.PYRO_RES.get())
                .add(ErAttributeRegister.ELECTRO_RES.get())
                .add(ErAttributeRegister.PHYSICAL_RES.get());
    }

	public void er$removeShield(ErShield shield) {
		shield.end(this);
        this.er$shields.removeIf(shieldstack -> shieldstack.getShield() == shield);
		er$syncShield();
	}

	public void er$setShields(CompoundTag tag) {
		Set<String> keys = tag.getAllKeys();
		this.er$shields.clear();
		for (String key : keys) {
			ErShield shield = ShieldRegistry.SHIELD_REGISTRY.getValue(new ResourceLocation(key));
			this.er$shields.add(new ShieldStack(shield, tag.getCompound(key)));
		}
	}

	public void cleanShield() {
		er$shields.clear();
		er$syncShield();
	}

	public void er$syncShield() {
		CompoundTag tag = new CompoundTag();
		for (ShieldStack shield : er$shields) {
			tag.put(ShieldRegistry.SHIELD_REGISTRY.getKey(shield.getShield()).toString(), shield.toTag());
		}
		ErMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this), new ErShieldData(this.getId(), tag));
	}

	public void er$syncShield(ServerPlayer player) {
		CompoundTag tag = new CompoundTag();
		for (ShieldStack shield : er$shields) {
			tag.put(ShieldRegistry.SHIELD_REGISTRY.getKey(shield.getShield()).toString(), shield.toTag());
		}
		ErMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ErShieldData(this.getId(), tag));
	}

	@Override
	public int getElements() {
		return this.entityData.get(ER$ELEMENT);
	}

	@Override
	public void updateElements(int elements) {
		this.entityData.set(ER$ELEMENT, elements);
	}

	@Override
	public void er$setArtifact(ArtifactSlot slot, ItemStack itemStack) {
		ArtifactData data = DataComponentsRegister.ARTIFACT.getData(er$getArtifact(slot));
		if(this.level() instanceof ServerLevel && data != null)
			data.remove((LivingEntity)(Object) this);
		this.er$artifactItem.set(slot.getId(), itemStack.copy());
		data = DataComponentsRegister.ARTIFACT.getData(itemStack);
		if(this.level() instanceof ServerLevel && data != null)
			data.apply((LivingEntity)(Object) this);
	}

	@Override
	public ItemStack er$getArtifact(ArtifactSlot slot) {
		return this.er$artifactItem.get(slot.getId()) ;
	}

    @Override
    public Object2IntMap<ArtifactEffect> er$getEffectMap() {
        return this.er$effectMap;
    }

    @Override
	public void er$dropArtifact() {
		for(ItemStack artifact : er$artifactItem){
			this.level().addFreshEntity(new ItemEntity(level(),getX(),getY(),getZ(), artifact));
		}
	}

	@Override
	public void er$updateArtifact(){
		Object2IntMap<ArtifactEffect> newEffectMap = new Object2IntArrayMap<>();
		for (ItemStack artifact : er$artifactItem) {
			ArtifactData artifactData = DataComponentsRegister.ARTIFACT.getData(artifact);
			if (artifactData != null) {
				ArtifactEffect effect = artifactData.effect().get();
				newEffectMap.put(effect, newEffectMap.getOrDefault(effect, 0) + 1);
			}
		}
		if(!newEffectMap.equals(er$effectMap)){
			er$removeArtifactAttr();
			this.er$effectMap = newEffectMap;
			er$updateArtifactAttr();
		}
	}

    @Override
    public boolean er$shouldBurnBlock(long tick) {
        if(tick - this.er$lastBurn > 5) {
            this.er$lastBurn = tick;
            return true;
        }
        return false;
    }

	@Override
	public int er$getArtifactEffectLevel(ArtifactEffect effectHolder){
		return er$effectMap.getOrDefault(effectHolder, 0) ;
	}

	@Unique
    private void er$removeArtifactAttr(){
		AttributeMap attributeMap = this.getAttributes() ;
		for(Object2IntMap.Entry<ArtifactEffect> holderEntry : er$effectMap.object2IntEntrySet()){
			ArtifactEffect effect = holderEntry.getKey() ;
			if(effect instanceof TwoSetAttrEffect attrEffect)
				attrEffect.removeAttributeModifiers(attributeMap);
		}
	}

	@Unique
    private void er$updateArtifactAttr(){
		AttributeMap attributeMap = this.getAttributes() ;
		for(Object2IntMap.Entry<ArtifactEffect> holderEntry : er$effectMap.object2IntEntrySet()){
			ArtifactEffect effect = holderEntry.getKey() ;
			if(effect instanceof TwoSetAttrEffect attrEffect)
				attrEffect.addAttributeModifiers(attributeMap, holderEntry.getIntValue());
		}
	}
}
