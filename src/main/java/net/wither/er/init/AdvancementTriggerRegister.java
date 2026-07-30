package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.wither.er.advancements.trigger.CriticalDamageTrigger;
import net.wither.er.advancements.trigger.ReactionTrigger;

@Mod.EventBusSubscriber(modid = ErMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AdvancementTriggerRegister {
    public static final CriticalDamageTrigger CRIT_DAMAGE = new CriticalDamageTrigger();
    public static final ReactionTrigger REACTION = new ReactionTrigger();
    public static final PlayerTrigger WHOPPERFLOWER = new PlayerTrigger(new ResourceLocation(ErMod.MODID, "whopperflower"));
    public static final PlayerTrigger ELEMENTAL_HOE = new PlayerTrigger(new ResourceLocation(ErMod.MODID, "elemental_hoe"));

    @SubscribeEvent
    public static void RegisterTrigger(FMLCommonSetupEvent event){
        CriteriaTriggers.register(CRIT_DAMAGE);
        CriteriaTriggers.register(REACTION);
        CriteriaTriggers.register(WHOPPERFLOWER);
        CriteriaTriggers.register(ELEMENTAL_HOE);
    }
}
