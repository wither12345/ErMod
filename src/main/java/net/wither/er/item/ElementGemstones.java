package net.wither.er.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public record ElementGemstones(RegistryObject<Item> sliver, RegistryObject<Item> fragment, RegistryObject<Item> chunk, RegistryObject<Item> gemstone) {
    public ElementGemstones(DeferredRegister<Item> REGISTRY, String name){
        this(
                REGISTRY.register(name + "_sliver", () -> new Item(new Item.Properties())),
                REGISTRY.register(name + "_fragment", () -> new Item(new Item.Properties())),
                REGISTRY.register(name + "_chunk", () -> new Item(new Item.Properties())),
                REGISTRY.register(name + "_gemstone", () -> new Item(new Item.Properties()))
        );
    }

    public void addToTab(CreativeModeTab.Output tabData){
        tabData.accept(this.sliver.get());
        tabData.accept(this.fragment.get());
        tabData.accept(this.chunk.get());
        tabData.accept(this.gemstone.get());
    }
}
