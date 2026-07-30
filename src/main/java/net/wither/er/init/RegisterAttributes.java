package net.wither.er.init;

import net.mcreator.er.init.ErModEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.wither.er.entity.BloomEntityEntity;
import net.wither.er.entity.slimes.AnemoSlime;
import net.wither.er.entity.slimes.ElementalSlime;
import net.wither.er.entity.whopperflower.CryoWhopperflower;
import net.wither.er.entity.whopperflower.PyroWhopperflower;

@EventBusSubscriber()
public class RegisterAttributes {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ErModEntities.BLOOM_ENTITY.get(), BloomEntityEntity.createAttributes().build());
        event.put(ErModEntities.ELECTRO_SLIME.get(),  ElementalSlime.createAttributes().build());
        event.put(ErModEntities.GEO_SLIME.get(),  ElementalSlime.createAttributes().build());
        event.put(ErModEntities.PYRO_SLIME.get(),  ElementalSlime.createAttributes().build());
        event.put(ErModEntities.HYDRO_SLIME.get(),  ElementalSlime.createAttributes().build());
        event.put(ErModEntities.CRYO_SLIME.get(),  ElementalSlime.createAttributes().build());
        event.put(ErModEntities.DENDRO_SLIME.get(),  ElementalSlime.createAttributes().build());
        event.put(ErModEntities.ANEMO_SLIME.get(),  AnemoSlime.createAttributes().build());
        event.put(ErModEntities.PYRO_WHOPPERFLOWER.get(),  PyroWhopperflower.createAttributes().build());
        event.put(ErModEntities.CRYO_WHOPPERFLOWER.get(),  CryoWhopperflower.createAttributes().build());
    }
}
