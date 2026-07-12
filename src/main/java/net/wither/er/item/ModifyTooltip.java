package net.wither.er.item;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.init.AdditionalRegistries;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.weapon.WeaponLevelData;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.weapons.AbilityWeapon;

import java.util.List;

@Mod.EventBusSubscriber(value = {Dist.CLIENT})
public class ModifyTooltip {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        List<Component> list = event.getToolTip();
        ItemStack item = event.getItemStack();
        ArtifactData artifactData = DataComponentsRegister.ARTIFACT.getData(item);
        WeaponLevelData weaponLevelData = DataComponentsRegister.WEAPON_LEVEL.getData(item);

        if(artifactData != null)
            addArtifactEffectId(artifactData.addTooltip(event.getToolTip()) , list, artifactData.effect().get());


        if(weaponLevelData != null && !item.is(WeaponLevelData.not_enhanceable)){
            list.add(1, Component.literal("Lv." + weaponLevelData.level() + "/" + WeaponLevelData.getMaxLevel(weaponLevelData.ascension()) + " " + getAscension(weaponLevelData.ascension(), WeaponLevelData.getItemWeaponStar(item))));
            if(weaponLevelData.level() < WeaponLevelData.getMaxLevel(weaponLevelData.ascension()))
                list.add(2, Component.literal(
                        "experience : " + weaponLevelData.experience() + "/" + WeaponLevelData.getMaxExp(weaponLevelData.level(), WeaponLevelData.getItemWeaponStar(item))));
            else
                list.add(2, Component.literal("§6Maxed")) ;
            if(item.getItem() instanceof AbilityWeapon weapon){
                CompoundTag tag = item.getOrCreateTag();
                list.add(1, Component.translatable("lore.er.refinement").append(" " + (tag.contains("refinement") ? tag.getInt("refinement") : 1)));
            }
        }
    }

    private static void addArtifactEffectId(int index, List<Component> list, ArtifactEffect effect){
        ResourceLocation location = AdditionalRegistries.ARTIFACT_REGISTRY.getKey(effect) ;
        if (location != null) {
            String id = "artifact_effect" + "." + location.getNamespace() + "." + location.getPath() ;
            if(I18n.exists(id))
                list.add(index ++, Component.translatable(id));
            id += "." ;
            int i = 1 ;
            while(true) {
                String id_index = id + (i ++) ;
                if (I18n.exists(id_index))
                    list.add(index ++, Component.translatable(id_index));
                else
                    break;
            }
        }
    }

    private static String getAscension(int ascension, int star){
        return "✦".repeat(ascension) + "✧".repeat(Math.max((star < 3 ? 4 : 6) - ascension,0)) ;
    }
}
