package net.wither.er.elements;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public interface IAuraContainerOwner {
    AuraContainer er$getAuraContainer();

    int er$getElements();

    void er$updateElements(int elements) ;

    Vec3 er$getPosition();

    @NotNull Level er$getLevel();
}
