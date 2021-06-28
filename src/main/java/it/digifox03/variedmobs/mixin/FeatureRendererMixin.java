package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.RedirectContext;
import it.digifox03.variedmobs.VariedMobs;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(FeatureRenderer.class)
abstract class FeatureRendererMixin {
    private static LivingEntity variedMobs_l;

    @Inject(
            at = @At("HEAD"),
            method = "renderModel"
    )
    private static void renderModelHead(EntityModel<LivingEntity> model, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float red, float green, float blue, CallbackInfo ci) {
        variedMobs_l = entity;
    }

    @ModifyVariable(
            at = @At("HEAD"),
            method = "renderModel"
    )
    private static Identifier renderModel(Identifier id) {
        Map<Identifier, Object> ctx = new HashMap<>();
        ctx.put(RedirectContext.entity, variedMobs_l);
        return VariedMobs.redirectTexture(id, ctx);
    }
}
