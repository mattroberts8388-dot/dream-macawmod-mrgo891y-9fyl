package com.macawmod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
public class MacawModModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(MacawModEntities.MACAW, NoopRenderer::new);
    }
    private static class NoopRenderer<T extends Entity> extends EntityRenderer<T> {
        NoopRenderer(EntityRendererFactory.Context ctx) { super(ctx); }
        @Override public Identifier getTexture(T e) { return new Identifier("minecraft", "textures/misc/unknown_server.png"); }
    }
}