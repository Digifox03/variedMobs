package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.RedirectContext;
import it.digifox03.variedmobs.VariedMobs;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EndermanEyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(EyesFeatureRenderer.class)
abstract class EyesFeatureRendererMixin extends FeatureRenderer<Entity, EntityModel<Entity>> {
    protected LivingEntity variedMobs_ll;

    public EyesFeatureRendererMixin(FeatureRendererContext<Entity, EntityModel<Entity>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Inject(
            at = @At("HEAD"),
            method = "render",
            cancellable = true
    )
    public void getEyesTexture(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        variedMobs_ll = (LivingEntity) entity;
    }
}
