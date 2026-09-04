package net.wither.er.mixins;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.artifact_effect.AttrArtifactEffect;
import net.wither.er.elements.AuraContainer;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.entity.ArtifactSlot;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.entity.listener.onHealthFloating;
import net.wither.er.init.AdditionalRegistries;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.network.ErShieldData;
import net.wither.er.shield.ErShield;
import net.wither.er.shield.ShieldStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ErEntityInterface , AuraContainerInterface {
    @Unique private final ArrayList<ShieldStack> er$shields = new ArrayList<>();
	@Unique private final AuraContainer er$auraContainer = new AuraContainer(this);
    @Unique private float er$lastHealth ;
    @Unique private static final EntityDataAccessor<Integer> ER$ELEMENT = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
	@Unique private final NonNullList<ItemStack> er$artifactItem = NonNullList.withSize(5, ItemStack.EMPTY);
	@Unique private Object2IntMap<Holder<ArtifactEffect>> er$effectMap = new Object2IntArrayMap<>();
    @Unique private long er$lastBurn;


	@Shadow public abstract AttributeMap getAttributes();

	@Shadow public abstract ItemStack getMainHandItem();

    @Shadow
    public abstract float getHealth();

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

    @Override
	public void er$addShield(ShieldStack shield) {
		shield.getShield().start(this);
		this.er$shields.add(shield);
		er$syncShield();
	}

	@Override
	public AuraContainer er$getAuraContainer() {
		return er$auraContainer;
	}

	@Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
	public void addAdditionalSaveData(CompoundTag compound, CallbackInfo info) {
		CompoundTag tag = new CompoundTag();
		for (ShieldStack shield : er$shields) {
			tag.put(AdditionalRegistries.SHIELD_REGISTRY.getKey(shield.getShield()).toString(), shield.toTag());
		}
		compound.put("ErShield", tag);

		ListTag listtag = new ListTag();

        for (ItemStack itemstack : this.er$artifactItem) {
            if (!itemstack.isEmpty()) {
                listtag.add(itemstack.save(this.registryAccess()));
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
                this.setArtifact(slot, ItemStack.parseOptional(this.registryAccess(), compoundtag1));
			}
			this.updateArtifact();
		}
	}

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo ci){
        if(this.getHealth() != this.er$lastHealth){
            onHealthFloating.onFloating((LivingEntity) (Object)this, this.getHealth() - this.er$lastHealth);
            this.er$lastHealth = getHealth();
        }
    }

	@Inject(method = "defineSynchedData" , at = @At("TAIL"))
	protected void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo info) {
		builder.define(ER$ELEMENT, 0);
	}

	@Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
	public void dropAllDeathLoot(ServerLevel level, DamageSource source, CallbackInfo info) {
		this.er$dropArtifact();
	}

    @Inject(method = "createLivingAttributes", at = @At("TAIL"))
    private static void createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir){
        cir.getReturnValue()
                .add(ErAttributeRegister.ANEMO_RES)
                .add(ErAttributeRegister.CRYO_RES)
                .add(ErAttributeRegister.HYDRO_RES)
                .add(ErAttributeRegister.GEO_RES)
                .add(ErAttributeRegister.DENDRO_RES)
                .add(ErAttributeRegister.PYRO_RES)
                .add(ErAttributeRegister.ELECTRO_RES)
                .add(ErAttributeRegister.PHYSICAL_RES);
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
			ErShield shield = AdditionalRegistries.SHIELD_REGISTRY.get(ResourceLocation.parse(key));
			this.er$shields.add(new ShieldStack(shield, tag.getCompound(key)));
		}
	}

	public void er$cleanShield() {
		er$shields.clear();
		er$syncShield();
	}

	public void er$syncShield() {
		CompoundTag tag = new CompoundTag();
		for (ShieldStack shield : er$shields) {
			tag.put(AdditionalRegistries.SHIELD_REGISTRY.getKey(shield.getShield()).toString(), shield.toTag());
		}
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(this, new ErShieldData(this.getId(), tag));
	}

	public void er$syncShield(ServerPlayer player) {
		CompoundTag tag = new CompoundTag();
		for (ShieldStack shield : er$shields) {
			tag.put(AdditionalRegistries.SHIELD_REGISTRY.getKey(shield.getShield()).toString(), shield.toTag());
		}
		PacketDistributor.sendToPlayer(player, new ErShieldData(this.getId(), tag));
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
	public void setArtifact(ArtifactSlot slot, ItemStack itemStack) {
		ArtifactData data = er$getArtifact(slot).getComponents().get(DataComponentsRegister.ARTIFACT.get());
		if(this.level() instanceof ServerLevel && data != null)
			data.remove((LivingEntity)(Object) this);
		this.er$artifactItem.set(slot.getId(), itemStack.copy());
		data = itemStack.getComponents().get(DataComponentsRegister.ARTIFACT.get());
		if(this.level() instanceof ServerLevel && data != null)
			data.apply((LivingEntity)(Object) this);
	}

	@Override
	public ItemStack er$getArtifact(ArtifactSlot slot) {
		return this.er$artifactItem.get(slot.getId()) ;
	}

	@Override
	public void er$dropArtifact() {
		for(ItemStack artifact : er$artifactItem){
			this.level().addFreshEntity(new ItemEntity(level(),getX(),getY(),getZ(), artifact));
		}
	}

	@Override
	public void updateArtifact(){
		Object2IntMap<Holder<ArtifactEffect>> newEffectMap = new Object2IntArrayMap<>();
		for (ItemStack artifact : er$artifactItem) {
			ArtifactData artifactData = artifact.getComponents().get(DataComponentsRegister.ARTIFACT.get());
			if (artifactData != null) {
				Holder<ArtifactEffect> effect = artifactData.effect();
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
	public int er$getArtifactEffectLevel(Holder<ArtifactEffect> effectHolder){
		return er$effectMap.getOrDefault(effectHolder, 0) ;
	}

    public Object2IntMap<Holder<ArtifactEffect>> er$getEffectMap() {
        return er$effectMap;
    }

    @Unique
    private void er$removeArtifactAttr(){
		AttributeMap attributeMap = this.getAttributes() ;
		for(Object2IntMap.Entry<Holder<ArtifactEffect>> holderEntry : er$effectMap.object2IntEntrySet()){
			ArtifactEffect effect = holderEntry.getKey().value() ;
			if(effect instanceof AttrArtifactEffect attrEffect)
				attrEffect.removeAttributeModifiers(attributeMap);
		}
	}

	@Unique
    private void er$updateArtifactAttr(){
		AttributeMap attributeMap = this.getAttributes() ;
		for(Object2IntMap.Entry<Holder<ArtifactEffect>> holderEntry : er$effectMap.object2IntEntrySet()){
			ArtifactEffect effect = holderEntry.getKey().value() ;
			if(effect instanceof AttrArtifactEffect attrEffect)
				attrEffect.addAttributeModifiers(attributeMap, holderEntry.getIntValue());
		}
	}
}
