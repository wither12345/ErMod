package net.wither.er.init;

import net.mcreator.er.init.ErModEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.wither.er.entity.slimes.ElementalSlime;

@EventBusSubscriber()
public class EntitySpawnPlacement {
    @SubscribeEvent
    public static void init(RegisterSpawnPlacementsEvent event) {
        ElementalSlime.init(event, ErModEntities.PYRO_SLIME.get());
        ElementalSlime.init(event, ErModEntities.ELECTRO_SLIME.get());
        ElementalSlime.init(event, ErModEntities.GEO_SLIME.get());
        ElementalSlime.init(event, ErModEntities.HYDRO_SLIME.get());
        ElementalSlime.init(event, ErModEntities.CRYO_SLIME.get());
        ElementalSlime.init(event, ErModEntities.DENDRO_SLIME.get());
        ElementalSlime.init(event, ErModEntities.ANEMO_SLIME.get());
    }
}
