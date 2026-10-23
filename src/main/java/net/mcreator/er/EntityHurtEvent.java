/*
 * Type :
 * 1 Anemo
 * 2 Cryo
 * 3 Dendro
 * 4 Electro
 * 5 Geo
 * 6 Hydro
 * 7 Pyro
*/
package net.mcreator.er;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.procedures.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.client.renderer.damge.RenderDamageAmount;
import net.wither.er.combat.DamageModifierInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import net.wither.er.elements.IAuraContainerOwner;
import net.wither.er.entity.IErEntity;
import net.wither.er.entity.slimes.DendroSlime;
import net.wither.er.init.AdvancementTriggerRegister;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.ElementRegistry;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.item.Vision;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.data.DelusionData;
import net.wither.er.item.data.FortunaData;
import net.wither.er.item.data.weapon.BeAttackedAbility;
import net.wither.er.item.data.weapon.DamageAbility;
import net.wither.er.item.data.weapon.InfusionAbility;
import net.wither.er.item.data.weapon.WeaponRefinement;
import net.wither.er.item.fortuna.StellaFortuna;
import net.wither.er.network.DamageDisplayMessage;
import net.wither.er.network.ErItemVariables;
import net.wither.er.network.MuteShakeData;
import net.wither.er.shield.ShieldStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 物理/元素伤害事件处理类。
 * <p>
 * 负责在实体受到伤害时统一处理伤害来源的修饰逻辑，包括：
 * 元素附着、元素反应乘区、暴击、抗性、护盾吸收、圣遗物/武器效果触发，
 * 以及伤害结算后用于客户端展示的伤害数字同步。
 * <p>
 * 通过 {@link SubscribeEvent} 在 GAME 总线上监听
 * {@link LivingIncomingDamageEvent}（受到伤害前）与
 * {@link LivingDamageEvent.Post}（伤害结算后）两类事件。
 *
 * @author [elemental_reaction]
 * @see LivingIncomingDamageEvent
 * @see LivingDamageEvent
 */
@EventBusSubscriber
public class EntityHurtEvent {
	private static final TagKey<DamageType> NO_CRITICAL = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:no_critical")) ;
    private static final TagKey<DamageType> CATALYZE = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:reaction_multiply/catalyze")) ;
    private static final TagKey<DamageType> TRANSFORMATIVE = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:reaction_multiply/transformative")) ;
    private static final TagKey<DamageType> LUNAR = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:lunar")) ;
	private static final TagKey<DamageType> NORMAL_ATTACK = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:normal_attack")) ;

    /**
     * 实体受到伤害的核心处理入口（伤害计算前触发）。
     * <p>
     * 整个流程大致如下：
     * <ol>
     *   <li>调用 {@link #modifyDamageSource} 初始化伤害来源的元素附着信息；</li>
     *   <li>调用 {@link #modifyReaction} 根据伤害标签应用反应乘区类型；</li>
     *   <li>当伤害来源实现了元素/伤害修饰接口时，依次处理：
     *       圣遗物与武器精炼提供的 {@link DamageAbility}、元素精通、附着元素、
     *       元素增伤与角色邪眼（Delusion）加成、暴击判定；</li>
     *   <li>通过 {@link DamageModifier#calculate} 汇总各乘区计算最终伤害；</li>
     *   <li>若目标实现了 {@link IErEntity}，先扣除护盾吸收量，再触发目标的
     *       {@link BeAttackedAbility} 受到攻击效果，并回写最终伤害。</li>
     * </ol>
     *
     * @param event NeoForge 的受击事件对象
     * @see #modifyDamageSource(DamageSource, Entity)
     * @see #modifyReaction(DamageSource)
     * @see DamageModifier#calculate(float, double)
     */
    @SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event == null)
			return;
		DamageSource damagesource = event.getSource();
		LivingEntity entity = event.getEntity();
		Entity sourceentity = event.getSource().getEntity();
        float amount = event.getAmount();
		modifyDamageSource(damagesource, entity) ;
        modifyReaction(damagesource);

		if(damagesource instanceof DamageModifierInterface modifierInterface && damagesource instanceof ElementSourceInterface elementSourceInterface && entity instanceof IAuraContainerOwner auraOwner) {
            DamageModifier modifier = modifierInterface.er$getModifier();
            if(sourceentity instanceof IErEntity erEntity){
                Object2IntMap<Holder<ArtifactEffect>> map = erEntity.er$getEffectMap();
                for(Object2IntMap.Entry<Holder<ArtifactEffect>> effect : map.object2IntEntrySet()){
                    if(effect.getKey().value() instanceof DamageAbility damageAbility){
                        damageAbility.onHurt(damagesource, entity, modifier, effect.getIntValue());
                    }
                }
            }
            if(sourceentity instanceof LivingEntity living) {
                WeaponRefinement refinement = living.getMainHandItem().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
                if (refinement != null && refinement.getAbility() instanceof DamageAbility damageAbility)
                    damageAbility.onHurt(damagesource, entity, modifier, refinement.refineLevel());
            }

            double elemental_mastery = 0 ;
			if (sourceentity instanceof LivingEntity living && living.getAttribute(ErModAttributes.ELEMENTAL_MASTERY) != null)
				elemental_mastery = living.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY);
            ElementSource source = elementSourceInterface.er$getSource();
			if(source != null) {
                auraOwner.er$getAuraContainer().addAura(source, modifier, sourceentity);
                ApplyElementMultiply(source.getElement(), entity, sourceentity, modifier);
                DelusionData data = Vision.getDelusion(sourceentity);
                if(data != null && sourceentity instanceof LivingEntity living){
                    modifier.crit_multiply += data.critMulti();
                    modifier.common_multiply += data.dmgMulti();
                    float dmg = amount * data.healthConsume();
                    if(living instanceof ServerPlayer player) {
                        PacketDistributor.sendToPlayer(player, new MuteShakeData());
                        AdvancementTriggerRegister.DELUSION.get().trigger(player, dmg);
                    }
                    if(living.getHealth() >= dmg)
                        living.setHealth(living.getHealth() - dmg);
                    else
                        living.hurt(new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.WITHER)), living.getMaxHealth() * 20);
                }
            }
            else {
                if (entity.getAttribute(ErAttributeRegister.PHYSICAL_RES) != null)
                    modifier.res_multiply *= (100f - (float) entity.getAttributeValue(ErAttributeRegister.PHYSICAL_RES)) / 100f;
                if (sourceentity instanceof LivingEntity living && living.getAttribute(ErModAttributes.PHYSICAL_DMG_BONUS) != null)
                    modifier.common_multiply += (float) living.getAttributeValue(ErModAttributes.PHYSICAL_DMG_BONUS) - 1;
            }
            if(damagesource.is(StellaFortuna.SKILL) && sourceentity instanceof LivingEntity living && living.getAttribute(ErModAttributes.ELEMENTAL_SKILL_DMG) != null)
                modifierInterface.er$getModifier().common_multiply += (float) living.getAttributeValue(ErModAttributes.ELEMENTAL_SKILL_DMG) - 1f;

			if (!damagesource.is(NO_CRITICAL) && sourceentity instanceof LivingEntity living && living.getAttributeValue(ErModAttributes.CRIT_RATE) > Math.random()) {
                modifier.crit_multiply += (float) living.getAttributeValue(ErModAttributes.CRIT_DAMAGE);
				modifier.critical = true;
			}

			if(entity instanceof DendroSlime slime && slime.onGround() && slime.isHiding() && damagesource.getDirectEntity() != null)
				modifier.reaction_multiply = 0 ;
			float final_amount = modifier.calculate(amount, elemental_mastery);
            
			if (entity instanceof IErEntity anInterface) {
				List<ShieldStack> shields = anInterface.er$getShieldStacks();
				float shield_absorb = 0f;
				for (ShieldStack shield : shields) {
					if(elementSourceInterface.er$getSource() != null)
						shield_absorb = Math.max(shield_absorb, shield.getShield().onHurt(shield, entity, damagesource, final_amount, elementSourceInterface.er$getSource().getCategory().getId()));
					else
						shield_absorb = Math.max(shield_absorb, shield.getShield().onHurt(shield, entity, damagesource, final_amount, 0));
				}
                final_amount -= shield_absorb;
				event.setAmount(final_amount);

                Object2IntMap<Holder<ArtifactEffect>> map = anInterface.er$getEffectMap();
                for(Object2IntMap.Entry<Holder<ArtifactEffect>> effect : map.object2IntEntrySet()){
                    if(effect.getKey().value() instanceof BeAttackedAbility ability){
                        ability.beAttacked(entity, damagesource, modifier, final_amount, effect.getIntValue());
                    }
                }
                WeaponRefinement refinement = entity.getMainHandItem().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
                if (refinement != null && refinement.getAbility() instanceof BeAttackedAbility ability)
                    ability.beAttacked(entity, damagesource, modifier, final_amount, refinement.refineLevel());

            }
		}
		//PacketDistributor.sendToAllPlayers(new ErData(entity.getId(), entity.getPersistentData().getInt("Frozen")));
	}

	/**
     * 伤害结算后的处理入口。
     * <p>
     * 当最终伤害为正时，若发生暴击且伤害来源者是服务器玩家，则触发暴击
     * 成就（{@link AdvancementTriggerRegister#CRITICAL_DAMAGE}），并向所有玩家
     * 广播伤害数字展示消息 {@link DamageDisplayMessage}（客户端据此绘制飘字）。
     * <p>
     * 处理完毕后会清空伤害来源上暂存的元素信息（{@link ElementSourceInterface#er$setElement}），
     * 避免污染下一次伤害计算。
     *
     * @param event NeoForge 的伤害结算事件对象
     * @see #getARGB(DamageSource)
     */
    @SubscribeEvent
	public static void afterDamage(LivingDamageEvent.Post event){
		int dmg = Mth.ceil(event.getNewDamage()) ;
        DamageSource source = event.getSource();
		if(source instanceof DamageModifierInterface modifierInterface && dmg > 0) {
            DamageModifier modifier = modifierInterface.er$getModifier();
            if(modifier.critical && source.getEntity() instanceof ServerPlayer player)
                AdvancementTriggerRegister.CRITICAL_DAMAGE.get().trigger(player, event.getNewDamage());
			PacketDistributor.sendToAllPlayers(new DamageDisplayMessage(dmg, event.getEntity().getId(), getARGB(source), modifier.critical, modifier.type));
		}
        ((ElementSourceInterface)source).er$setElement(null);
	}

    /**
     * 初始化并修正伤害来源 ({@link DamageSource}) 的元素附着与修饰状态。
     * <p>
     * 若本次伤害的目标不是当前实体，会重置修饰器（{@link DamageModifierInterface#er$reset()}）。
     * 元素附着信息按以下优先级补充：
     * <ol>
     *   <li>伤害来源携带的内建元素标签（经由 {@link Element.Category#match} 检测）；</li>
     *   <li>直接攻击实体（间接来源）携带的元素附着；</li>
     *   <li>普通攻击（{@link #NORMAL_ATTACK} 标签）由攻击者通过
     *       {@link #getElementSource} 计算出的武器附魔/元素附魔。</li>
     * </ol>
     *
     * @param source 当前伤害来源
     * @param entity 受到伤害的实体
     * @see ElementSource
     * @see ElementSourceInterface#er$setElement(ElementSource)
     */
    private static void modifyDamageSource(DamageSource source, Entity entity){
        float gauge ;
        DamageModifierInterface damageModifierInterface = (DamageModifierInterface) source;

        if(damageModifierInterface.er$getTarget() != entity){
            damageModifierInterface.er$setTarget(entity);
            damageModifierInterface.er$reset();
        }
        ElementSourceInterface elementSourceInterface = (ElementSourceInterface) source;

        if (!damageModifierInterface.er$oriEmpty())
            return;

        for(Element.Category category : Element.Category.values()){
            if(category.match(source)){
                gauge = category.getAura(source);
                Element element = category.getDefault();
                elementSourceInterface.er$setElement(new ElementSource(element, ResourceLocation.parse("er:default"), gauge, element.isApplicable()));
                break;
            }
        }

        if(source.getDirectEntity() instanceof ElementSourceInterface elementSourceInterface1){
            ElementSource source1 = elementSourceInterface1.er$getSource();
            if(source1 == null) return;
            elementSourceInterface.er$setElement(source1) ;
        }

        if(source.getEntity() != null && elementSourceInterface.er$getSource() == null && source.getEntity() == source.getDirectEntity() && source.is(NORMAL_ATTACK)) {
            elementSourceInterface.er$setElement(getElementSource(source.getEntity().level(), source.getEntity(), source.getDirectEntity()));
        }
    }

    /**
     * 根据伤害来源携带的伤害类型标签，设置反应乘区的具体类型与显示样式。
     * <p>
     * 支持的标签：
     * <ul>
     *   <li>{@link #CATALYZE}（激化）</li>
     *   <li>{@link #TRANSFORMATIVE}（剧变反应）</li>
     *   <li>{@link #LUNAR}（月反应，同时将显示样式设为 {@link RenderDamageAmount.Display#LUNAR}）</li>
     * </ul>
     * 未命中任何标签时保持默认的普通乘区。
     *
     * @param source 待处理的伤害来源
     * @see ReactionMultiply
     * @see DamageModifierInterface#er$getModifier()
     */
    private static void modifyReaction(DamageSource source){
        DamageModifierInterface damageModifierInterface = (DamageModifierInterface) source;
        if(source.is(CATALYZE))
            damageModifierInterface.er$getModifier().multiply = ReactionMultiply.CATALYZE;
        if(source.is(TRANSFORMATIVE))
            damageModifierInterface.er$getModifier().multiply = ReactionMultiply.TRANSFORMATIVE;
        if(source.is(LUNAR)) {
            damageModifierInterface.er$getModifier().multiply = ReactionMultiply.VARIANT;
            damageModifierInterface.er$getModifier().type = RenderDamageAmount.Display.LUNAR;
        }
    }

	/**
     * 获取伤害来源对应的飘字颜色（ARGB）。
     * <p>
     * 若伤害来源携带元素附着，则返回该元素的主题色；否则返回白色。
     *
     * @param source 伤害来源
     * @return ARGB 颜色值
     * @see ElementSource#getCategory()
     */
	private static int getARGB(DamageSource source){
		if(source instanceof ElementSourceInterface elementSourceInterface && elementSourceInterface.er$getSource() != null){
			return elementSourceInterface.er$getSource().getCategory().getColor() ;
		}
		return 0xffffffff ;
	}

	/**
     * 应用元素伤害加成与敌方元素抗性到伤害修饰器上。
     * <p>
     * 逻辑：
     * <ul>
     *   <li>若攻击者有该元素的伤害加成属性，则累乘到 {@code common_multiply}（通用增伤乘区）；</li>
     *   <li>若受击实体对元素免疫，则将反应乘区归零（{@code reaction_multiply = 0}）；</li>
     *   <li>否则若受击实体拥有该元素的抗性属性，则按其数值折算为元素抗性乘区
     *       （{@code res_multiply}）。</li>
     * </ul>
     *
     * @param element      命中的元素
     * @param entity       受击实体
     * @param sourceentity 伤害来源（用于读取攻击者的元素增伤属性）
     * @param modifier     目标伤害修饰器
     * @see Element#getDamageAttr()
     * @see Element#getResAttr()
     * @see Element#getImmuneTag()
     */
    private static void ApplyElementMultiply(@NotNull Element element, LivingEntity entity, Entity sourceentity , DamageModifier modifier){
		element.getDamageAttr();
		if(sourceentity instanceof LivingEntity living && element.getDamageAttr() != null && living.getAttribute(element.getDamageAttr()) != null)
			modifier.common_multiply += (float) living.getAttributeValue(element.getDamageAttr()) - 1;
		if(element.getImmuneTag() != null && entity.getType().is(element.getImmuneTag()))
			modifier.reaction_multiply = 0;
		else if(element.getResAttr() != null && entity.getAttribute(element.getResAttr()) != null)
			modifier.res_multiply *= (100f - (float) entity.getAttributeValue(element.getResAttr())) / 100f ;
	}

    /**
     * 根据攻击实体及其手持武器/元素附魔状态，解析出一次攻击对应的元素来源。
     * <p>
     * 仅当直接攻击实体与攻击者为同一实体时才会尝试解析。解析顺序：
     * <ol>
     *   <li>手持武器若含有 {@link WeaponRefinement} 且附带了 {@link InfusionAbility}
     *       （元素附魔），则直接返回其元素灌注结果；</li>
     *   <li>否则依次检测各元素附魔流程（{@code IsXInfusionProcedure}）返回对应元素。</li>
     * </ol>
     * 若无法解析出任何元素，返回 {@code null}。
     *
     * @param world                所在世界
     * @param entity               攻击实体
     * @param immediatesourceentity 直接攻击来源实体
     * @return 解析出的元素来源，无元素时返回 {@code null}
     * @see WeaponRefinement
     * @see InfusionAbility
     */
    @Nullable
    public static ElementSource getElementSource(LevelAccessor world, Entity entity, Entity immediatesourceentity){
        if (entity == null) return null;
        Element element = null;
        if (immediatesourceentity == entity) {
            if(entity instanceof LivingEntity living){
                ItemStack itemStack = living.getMainHandItem();
                WeaponRefinement refinement = itemStack.get(DataComponentsRegister.WEAPON_REFINEMENT);
                if(refinement != null && refinement.getAbility() instanceof InfusionAbility ability){
                    return ability.getInfusion(itemStack, entity, refinement.refineLevel());
                }
            }
            if (IsAnemoInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.ANEMO.get();
            } else if (IsCryoInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.CRYO.get();
            } else if (IsDendroInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.DENDRO.get();
            } else if (IsElectroInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.ELECTRO.get();
            } else if (IsGeoInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.GEO.get();
            } else if (IsHydroInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.HYDRO.get();
            } else if (IsPyroInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.PYRO.get();
            }
        }
        if(element == null) return null;
        return new ElementSource(element, ResourceLocation.parse("er:default"), 1, element.isApplicable());
    }

	/**
     * 根据元素精通数值计算各类反应的增幅倍率。
     * <p>
     * 按反应类型分类（{@code type}）：
     * <ul>
     *   <li>0 —— 增幅反应（融化/蒸发）；</li>
     *   <li>1 —— 剧变反应（超载、感电等）；</li>
     *   <li>2 —— 激化反应（蔓激化/超激化）；</li>
     *   <li>3 —— 结晶反应；</li>
     *   <li>其它 —— 返回 1（无加成）。</li>
     * </ul>
     * <p>
     * 注意：本方法按 {@code type} 直接给定拟合系数，与
     * 此方法目前仅用于结晶 将被弃用
     * {@link ReactionMultiply#getMulti(double)} 的作用类似，两者属于不同的调用入口。
     *
     * @param type              反应类型代号
     * @param elemental_mastery 元素精通数值
     * @return 对应反应的伤害倍率
     */
	public static float getElementalMasteryMultiply(int type, double elemental_mastery) {
        return switch (type){
            case 0 -> 2.78f * (float) (elemental_mastery / (elemental_mastery + 1400));//Melt Vaporize
            case 1 -> 16f * (float) (elemental_mastery / (elemental_mastery + 2000)) + 1f;//Overloaded, Superconduct, Electro-Charged, Burning, Shattered, Swirl, Bloom, Hyperbloom,Burgeon
            case 2 -> 5f * (float) (elemental_mastery / (elemental_mastery + 1200));//Spread Aggravate
            case 3 -> 4.44f * (float) (elemental_mastery / (elemental_mastery + 1400)) + 1;//Crystallize
            default -> 1f;
        };
	}

	/**
     * 判断两个实体之间是否应当发生互相伤害。
     * <p>
     * 规则依次为：
     * <ol>
     *   <li>任一实体为空时默认允许伤害；</li>
     *   <li>先追溯各自的主人（{@link OwnableEntity}）；</li>
     *   <li>若双方在计分板上存在队伍，则仅在不同队伍间允许伤害；</li>
     *   <li>同一实体之间不允许伤害；</li>
     *   <li>若一方将另一方列为攻击目标（{@link Targeting}）则允许；</li>
     *   <li>最后按是否为敌对生物（{@link Enemy}）进行异或判断。</li>
     * </ol>
     *
     * @param entity1 实体 A
     * @param entity2 实体 B
     * @return 允许互相伤害返回 {@code true}
     * @see OwnableEntity
     * @see Targeting
     * @see Enemy
     */
	public static boolean shouldHurt(Entity entity1, Entity entity2) {
		if (entity1 == null || entity2 == null)
			return true;
        if(entity1 instanceof OwnableEntity ownable && ownable.getOwner() != null)
            entity1 = ownable.getOwner();
        if(entity2 instanceof OwnableEntity ownable && ownable.getOwner() != null)
            entity2 = ownable.getOwner();
		PlayerTeam team1 = entity1.level().getScoreboard().getPlayersTeam(entity1 instanceof Player _pl ? _pl.getGameProfile().getName() : entity1.getStringUUID());
		PlayerTeam team2 = entity2.level().getScoreboard().getPlayersTeam(entity2 instanceof Player _pl ? _pl.getGameProfile().getName() : entity2.getStringUUID());
        if(team1 != null || team2 != null) return team1 != team2;
        if(entity1 == entity2) return false;
        if(entity1 instanceof Targeting targeting && targeting.getTarget() == entity2) return true;
        if(entity2 instanceof Targeting targeting && targeting.getTarget() == entity1) return true;
        return (entity1 instanceof Enemy) ^ (entity2 instanceof Enemy);
    }

	/**
     * 根据实体等级（玩家为圣遗物 Fortuna 等级，生物为其自定义等级）转换为伤害倍率。
     * <p>
     * 使用三次多项式拟合格兰尼亚等级成长曲线，传入等级返回对应的倍率系数。
     *
     * @param level 实体等级
     * @return 对应的伤害倍率
     * @see #getEntityLevel(Entity)
     */
	public static float getLevelMultiply(int level) {
		return (0.001642f * level * level * level + 0.015823f * level * level + 16.980456f * level + 0.832476f)/17;
	}

	/**
     * 便捷重载：直接基于实体的等级计算伤害倍率。
     *
     * @param entity 目标实体
     * @return 对应的伤害倍率；实体为空时返回 1
     * @see #getLevelMultiply(int)
     * @see #getEntityLevel(Entity)
     */
	public static float getLevelMultiply(Entity entity) {
		return entity == null ? 1 : getLevelMultiply(getEntityLevel(entity)) ;
	}

	/**
     * 获取实体的“等级”。不同实体类型的判定方式不同：
     * <ul>
     *   <li>玩家 —— 读取其圣遗物 Fortuna 等级（{@link FortunaData}），缺失时视为 1 级；</li>
     *   <li>普通活体生物 —— 读取其持久数据中的 {@code erLevel} 字段；</li>
     *   <li>可拥有主（{@link OwnableEntity}）／可追踪主（{@link TraceableEntity}）的实体 —— 递归取其主人的等级；</li>
     *</ul>
     *
     * @param entity 目标实体
     * @return 实体的等级
     */
	public static int getEntityLevel(Entity entity) {
		if (entity instanceof Player) {
            FortunaData data = entity.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna.get(DataComponentsRegister.FORTUNA);
			if (data != null) {
				return data.level() + 1;
			}
			return 1 ;
		}
		else if(entity instanceof LivingEntity)
			return entity.getPersistentData().getInt("erLevel");
		else if(entity instanceof OwnableEntity ownable)
			return getEntityLevel(ownable.getOwner()) ;
        else if(entity instanceof TraceableEntity traceable)
            return getEntityLevel(traceable.getOwner()) ;
		return 0 ;
	}

	/**
     * 获取实体的元素精通数值。
     *
     * @param entity 目标实体
     * @return 元素精通数值；实体不是活体实体时返回 0
     */
	public static double getElementalMastery(Entity entity){
		if(entity instanceof LivingEntity living){
			return living.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY) ;
		}
		return 0 ;
	}

    /**
     * 伤害乘区修饰器。
     * <p>
     * 集中存储一次伤害结算所需的各个乘区系数，供元素/物理/圣遗物/武器等系统
     * 在 {@link LivingIncomingDamageEvent} 阶段增量累加，最后在
     * {@link #calculate(float, double)} 中一次性汇总为最终伤害。
     * <p>
     * 各字段含义（默认值均为中性值 1 或 0）：
     * <ul>
     *   <li>{@code basic} —— 基础倍率乘区；</li>
     *   <li>{@code common_multiply} —— 通用增伤乘区（元素/物理增伤、角色钻研等）；</li>
     *   <li>{@code reaction_multiply} —— 反应乘区（元素附着度引起的增幅）；</li>
     *   <li>{@code res_multiply} —— 抗性乘区；</li>
     *   <li>{@code crit_multiply} —— 暴击加成乘区；</li>
     *   <li>{@code additional_amount} —— 追加的固定伤害数值；</li>
     *   <li>{@code multiply} —— 由反应类型决定的乘区倍率（{@link ReactionMultiply}）；</li>
     *   <li>{@code critical} —— 本次伤害是否判定为暴击；</li>
     *   <li>{@code locked} —— 是否锁定（预留，暂未使用）。</li>
     * </ul>
     */
    public static class DamageModifier {
        public boolean locked = false ;
        public boolean critical = false ;
        public float reaction_multiply = 1;
        public float common_multiply = 1;
        public float crit_multiply = 1;
        public float basic = 1;
        public float res_multiply = 1;
        public float additional_amount = 0;
        public ReactionMultiply multiply = null;
        public RenderDamageAmount.Display type = RenderDamageAmount.Display.NORMAL;

        /**
         * 汇总各乘区计算最终伤害。
         * <p>
         * 计算公式：
         * <pre>
         * (原始伤害 + additional_amount) × basic
         *   × (reaction_multiply + (反应倍率 或 通用增伤) - 1)
         *   × res_multiply
         *   × (暴击时取 crit_multiply，否则 1)
         * </pre>
         * 当存在具体反应倍率（{@link #multiply}）时以
         * {@link ReactionMultiply#getMulti(double)} 基于元素精通计算乘区；
         * 否则使用通用增伤乘区 {@link #common_multiply}。
         *
         * @param dmg              原始伤害数值
         * @param elementalMastery 攻击者的元素精通数值
         * @return 计算后的最终伤害
         * @see ReactionMultiply#getMulti(double)
         */
        public float calculate(float dmg, double elementalMastery){
            return (dmg + additional_amount) * basic * (reaction_multiply + (multiply == null ? common_multiply : multiply.getMulti(elementalMastery)) - 1) * res_multiply * (critical ? crit_multiply : 1);
        }

        @Override
        public String toString() {
            return "DamageModifier{" +
                    "locked=" + locked +
                    ", critical=" + critical +
                    ", reaction_multiply=" + reaction_multiply +
                    ", common_multiply=" + common_multiply +
                    ", basic=" + basic +
                    ", res_multiply=" + res_multiply +
                    ", additional_amount=" + additional_amount +
                    ", multiply=" + multiply +
                    ", type=" + type +
                    '}';
        }
    }

    /**
     * 元素反应的乘区倍率类型。
     * <p>
     * 每种反应对应的伤害成长曲线由两个参数描述：
     * {@code maxMultiply}（最大倍率）与 {@code halfMultiAmount}（达到半数倍率所需的元素精通值）。
     * <p>
     * 枚举项说明（均按原神/仿元素反应规则分类）：
     * <ul>
     *   <li>{@link #AMPLIFYING} —— 增幅反应（融化、蒸发）；</li>
     *   <li>{@link #CATALYZE} —— 激化反应（蔓激化、超激化）；</li>
     *   <li>{@link #TRANSFORMATIVE} —— 剧变反应（超载、感电、燃烧等）；</li>
     *   <li>{@link #VARIANT} —— 星/月反应；</li>
     *   <li>{@link #CRYSTALLIZE} —— 结晶反应。</li>
     * </ul>
     *
     * @see #getMulti(double)
     * @see #getMulti(Entity)
     */
    public enum ReactionMultiply {
        AMPLIFYING(2.78f, 1400),//Melt Vaporize
        CATALYZE(5, 1200),//Spread Aggravate
        TRANSFORMATIVE(16, 2000),//Overloaded, Superconduct, Electro-Charged, Burning, Shattered, Swirl, Bloom, Hyperbloom, Burgeon
        VARIANT(6, 2000),
        CRYSTALLIZE(4.44f, 1400);

        private final float maxMultiply;
        private final int halfMultiAmount;

        ReactionMultiply(float maxMultiply, int halfMultiAmount) {
            this.maxMultiply = maxMultiply;
            this.halfMultiAmount = halfMultiAmount;
        }

        /**
         * 根据元素精通数值计算该反应的伤害倍率。
         * <p>
         * 采用标准双曲线拟合公式：
         * <pre>
         * maxMultiply × EM / (EM + halfMultiAmount) + 1
         * </pre>
         * 元素精通越高越接近 {@code maxMultiply + 1}。
         *
         * @param elementalMastery 元素精通数值
         * @return 反应的伤害倍率（不小于 1）
         */
        public float getMulti(double elementalMastery) {
            return (float) (maxMultiply * (elementalMastery) / (elementalMastery + halfMultiAmount)) + 1;
        }

        /**
         * 从实体快捷读取元素精通并计算反应伤害倍率。
         * <p>
         * 若实体为活体实体且拥有元素精通属性，则使用其值调用
         * {@link #getMulti(double)}；否则返回 1（无加成）。
         *
         * @param entity 目标实体
         * @return 反应的伤害倍率
         * @see #getMulti(double)
         */
        public float getMulti(Entity entity){
            if(entity instanceof LivingEntity living){
                AttributeInstance instance = living.getAttribute(ErModAttributes.ELEMENTAL_MASTERY);
                if(instance != null)
                    return this.getMulti(instance.getValue());
            }
            return 1;
        }
    }
}