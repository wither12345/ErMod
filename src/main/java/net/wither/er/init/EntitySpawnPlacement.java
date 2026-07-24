package net.wither.er.init;

import net.mcreator.er.init.ErModEntities;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.entity.slimes.ElementalSlime;
import net.wither.er.entity.whopperflower.Whopperflower;

@Mod.EventBusSubscriber()
public class EntitySpawnPlacement {
    @SubscribeEvent
    public static void init(SpawnPlacementRegisterEvent event) {
        ElementalSlime.init(ErModEntities.PYRO_SLIME.get());
        ElementalSlime.init(ErModEntities.ELECTRO_SLIME.get());
        ElementalSlime.init(ErModEntities.GEO_SLIME.get());
        ElementalSlime.init(ErModEntities.HYDRO_SLIME.get());
        ElementalSlime.init(ErModEntities.CRYO_SLIME.get());
        ElementalSlime.init(ErModEntities.DENDRO_SLIME.get());
        ElementalSlime.init(ErModEntities.ANEMO_SLIME.get());
        Whopperflower.init(ErModEntities.PYRO_WHOPPERFLOWER.get());
    }
}
