package net.wither.er.entity.outcrop;

import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemGiver extends EntityModifier{
    private final EquipmentSlot slot ;
    private final Item added_item ;

    private ItemGiver(EquipmentSlot slot, Item added_item){
        this.slot = slot ;
        this.added_item = added_item;
    }

    public static ItemGiver read(JsonElement element, EquipmentSlot slot) {
        String item_name = element.getAsJsonObject().get("item").getAsString() ;
        return new ItemGiver(slot, BuiltInRegistries.ITEM.get(new ResourceLocation(item_name)));
    }

    @Override
    public void apply(Entity entity , int level) {
        if (entity instanceof LivingEntity living) {
            ItemStack itemToApply = new ItemStack(added_item) ;
            living.setItemSlot(slot, itemToApply);
        }
    }
}
