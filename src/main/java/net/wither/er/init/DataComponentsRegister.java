package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.item.Vision;
import net.wither.er.item.data.weapon.WeaponAttributeData;
import net.wither.er.item.data.weapon.WeaponLevelData;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.MainAffix;
import net.wither.er.item.data.artifactdata.MinorAffix;
import net.wither.er.item.data.weapon.WeaponRefinement;


public class DataComponentsRegister {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,ErMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WeaponLevelData>> WEAPON_LEVEL = REGISTRAR.registerComponentType(
            "weapon_level",
            builder -> builder
                    .persistent(WeaponLevelData.BASIC_CODEC)
                    .networkSynchronized(WeaponLevelData.BASIC_STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WeaponAttributeData>> WEAPON_ATTR = REGISTRAR.registerComponentType(
            "weapon_attr",
            builder -> builder
                    .persistent(WeaponAttributeData.BASIC_CODEC)
                    .networkSynchronized(WeaponAttributeData.UNIT_STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WeaponRefinement>> WEAPON_REFINEMENT = REGISTRAR.registerComponentType(
            "weapon_refinement",
            builder -> builder
                    .persistent(WeaponRefinement.BASIC_CODEC)
                    .networkSynchronized(WeaponRefinement.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ArtifactData>> ARTIFACT = REGISTRAR.registerComponentType(
            "artifact",
            builder -> builder
                    .persistent(ArtifactData.CODEC)
                    .networkSynchronized(ArtifactData.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MainAffix>> ARTIFACT_MAIN = REGISTRAR.registerComponentType(
            "artifact_main",
            builder -> builder
                    .persistent(MainAffix.CODEC)
                    .networkSynchronized(MainAffix.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MinorAffix>> ARTIFACT_MINOR = REGISTRAR.registerComponentType(
            "artifact_minor",
            builder -> builder
                    .persistent(MinorAffix.CODEC)
                    .networkSynchronized(MinorAffix.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Vision.Frame>> VISION_FRAME = REGISTRAR.registerComponentType(
            "vision_frame",
            builder -> builder
                    .persistent(Vision.FRAME_CODEC)
                    .networkSynchronized(Vision.FRAME_STREAM_CODEC)
    );
}