package net.wither.er.item;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.init.AdditionalRegistries;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.WeaponAbilityRegister;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.data.ErFoodData;
import net.wither.er.item.data.ErFoodDataListener;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.weapon.WeaponAbilityData;
import net.wither.er.item.data.weapon.WeaponLevelData;

import java.util.List;

@Mod.EventBusSubscriber(value = {Dist.CLIENT})
public class ModifyTooltip {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        List<Component> list = event.getToolTip();
        ItemStack item = event.getItemStack();
        WeaponLevelData weaponLevelData = DataComponentsRegister.WEAPON_LEVEL.getData(item);

        item.getCapability(ArtifactData.ARTIFACT_DATA).ifPresent(data ->
                addArtifactEffectId(data.addTooltip(list) , list, data.effect().get())
        );


        if(weaponLevelData != null && !item.is(WeaponLevelData.NOT_ENHANCEABLE)){
            list.add(1, Component.literal("Lv." + weaponLevelData.level() + "/" + WeaponLevelData.getMaxLevel(weaponLevelData.ascension()) + " " + getAscension(weaponLevelData.ascension(), WeaponLevelData.getItemWeaponStar(item))));
            if(weaponLevelData.level() < WeaponLevelData.getMaxLevel(weaponLevelData.ascension()))
                list.add(2, Component.literal(
                        "experience : " + weaponLevelData.experience() + "/" + WeaponLevelData.getMaxExp(weaponLevelData.level(), WeaponLevelData.getItemWeaponStar(item))));
            else
                list.add(2, Component.literal("§6Maxed")) ;
            item.getCapability(WeaponAbilityData.WEAPON_ABILITY).ifPresent(data -> {
                if(data.ability().get() != WeaponAbilityRegister.EMPTY.get() && data.ascension().get() != Items.AIR){
                    CompoundTag tag = item.getOrCreateTag();
                    list.add(1, Component.translatable("lore.er.refinement").append(" " + (tag.contains("refinement") ? tag.getInt("refinement") : 1)));
                }
            });
        }

        ErFoodData data = ErFoodDataListener.getData(item);
        if(data != null){
            data.addTooltip(list);
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
