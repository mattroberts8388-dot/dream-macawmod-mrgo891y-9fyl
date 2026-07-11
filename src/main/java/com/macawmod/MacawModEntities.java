package com.macawmod;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MacawModEntities {
    public static final EntityType<MacawEntity> MACAW = FabricEntityTypeBuilder
        .create(SpawnGroup.CREATURE, MacawEntity::new)
        .dimensions(EntityDimensions.fixed(0.5f, 0.9f))
        .build();

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MacawModMod.MOD_ID, "macaw"),
            MACAW);
    }
}