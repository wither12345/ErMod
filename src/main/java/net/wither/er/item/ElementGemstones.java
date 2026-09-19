package net.wither.er.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public record ElementGemstones(DeferredItem<Item> sliver, DeferredItem<Item> fragment, DeferredItem<Item> chunk, DeferredItem<Item> gemstone) {
    public ElementGemstones(DeferredRegister.Items REGISTER, String name){
        this(
                REGISTER.register(name + "_sliver", () -> new Item(new Item.Properties())),
                REGISTER.register(name + "_fragment", () -> new Item(new Item.Properties())),
                REGISTER.register(name + "_chunk", () -> new Item(new Item.Properties())),
                REGISTER.register(name + "_gemstone", () -> new Item(new Item.Properties()))
        );
    }

    public void addToTab(CreativeModeTab.Output tabData){
        tabData.accept(this.sliver);
        tabData.accept(this.fragment);
        tabData.accept(this.chunk);
        tabData.accept(this.gemstone);
    }
}
