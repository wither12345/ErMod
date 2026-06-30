package net.wither.er.outcrop;

import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemGiver extends EntityModifier{
    EquipmentSlot slot ;
    Item added_item ;

    public ItemGiver(EquipmentSlot slot){
        this.slot = slot ;
    }

    @Override
    public void read(JsonElement element) {
        String item_name = element.getAsJsonObject().get("item").getAsString() ;
        added_item = BuiltInRegistries.ITEM.get(new ResourceLocation(item_name));
    }

    @Override
    public void apply(Entity entity , int level) {
        if (entity instanceof LivingEntity living) {
            ItemStack itemToApply = new ItemStack(added_item) ;

            living.setItemSlot(slot, itemToApply);
        }
    }

    public EntityModifier copy(){
        return new ItemGiver(this.slot) ;
    }
}
