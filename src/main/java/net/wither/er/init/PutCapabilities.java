package net.wither.er.init;

import net.mcreator.er.item.Artifact;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.item.data.WeaponAttributeData;
import net.wither.er.item.data.WeaponLevelData;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;

import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.wither.er.item.data.WeaponLevelData.CapabilityProvider;
import static net.wither.er.network.ErItemVariables.PlayerVariablesProvider;

@Mod.EventBusSubscriber
public class PutCapabilities {
    @SubscribeEvent
    public static void onAttachEbtityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer)) {
            event.addCapability(new ResourceLocation("er", "item_variables"), new PlayerVariablesProvider());
            event.addCapability(new ResourceLocation("er", "combat_variables"), new ErCombatVariables.PlayerVariablesProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        Item item = event.getObject().getItem();
        if (hasLevelComponents(item)) {
            event.addCapability(new ResourceLocation("er", "weapon_level"), new CapabilityProvider());
        }
        if(item == Items.DIAMOND_SWORD)
            event.addCapability(new ResourceLocation("er", "weapon_attr"), new WeaponAttributeData.CapabilityProvider(ATTACK_DAMAGE, 0.018, true));
        if(item instanceof Artifact artifact){
            event.addCapability(new ResourceLocation("er", "artifact"), new ArtifactData.CapabilityProvider(artifact.getSlot(), artifact.getEffect()));
        }
    }

    private static boolean hasLevelComponents(Item item){
        return item instanceof ArmorItem || item instanceof SwordItem || item instanceof BowItem;
    }
}
