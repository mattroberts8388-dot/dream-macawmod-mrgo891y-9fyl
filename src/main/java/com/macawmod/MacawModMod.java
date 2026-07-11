package com.macawmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.SpawnGroup;

public class MacawModMod implements ModInitializer {
    public static final String MOD_ID = "macawmod";

    @Override
    public void onInitialize() {
        MacawModEntities.register();

        FabricDefaultAttributeRegistry.register(MacawModEntities.MACAW, MacawEntity.createMacawAttributes());

        BiomeModifications.addSpawn(
            BiomeSelectors.tag(ConventionalBiomeTags.JUNGLE),
            SpawnGroup.CREATURE,
            MacawModEntities.MACAW,
            10, 1, 3
        );
    }
}