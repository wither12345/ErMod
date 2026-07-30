package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ErAttributeRegister {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, ErMod.MODID);
    public static final RegistryObject<Attribute> ANEMO_RES = REGISTRY.register("anemo_res", () -> new RangedAttribute("attribute.er.anemo_res", 0, -512, 100).setSyncable(true));
    public static final RegistryObject<Attribute> CRYO_RES = REGISTRY.register("cryo_res", () -> new RangedAttribute("attribute.er.cryo_res", 0, -512, 100).setSyncable(true));
    public static final RegistryObject<Attribute> DENDRO_RES = REGISTRY.register("dendro_res", () -> new RangedAttribute("attribute.er.dendro_res", 0, -512, 100).setSyncable(true));
    public static final RegistryObject<Attribute> ELECTRO_RES = REGISTRY.register("electro_res", () -> new RangedAttribute("attribute.er.electro_res", 0, -512, 100).setSyncable(true));
    public static final RegistryObject<Attribute> GEO_RES = REGISTRY.register("geo_res", () -> new RangedAttribute("attribute.er.geo_res", 0, -512, 100).setSyncable(true));
    public static final RegistryObject<Attribute> HYDRO_RES = REGISTRY.register("hydro_res", () -> new RangedAttribute("attribute.er.hydro_res", 0, -512, 100).setSyncable(true));
    public static final RegistryObject<Attribute> PYRO_RES = REGISTRY.register("pyro_res", () -> new RangedAttribute("attribute.er.pyro_res", 0, -512, 100).setSyncable(true));
    public static final RegistryObject<Attribute> PHYSICAL_RES = REGISTRY.register("physical_res", () -> new RangedAttribute("attribute.er.physical_res", 0, -512, 100).setSyncable(true));

}
