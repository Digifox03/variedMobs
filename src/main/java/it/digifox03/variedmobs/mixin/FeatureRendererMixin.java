package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.core.VariedMobManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderer.class)
@Debug(export = true)
public abstract class FeatureRendererMixin {
    static private LivingEntity variedMobs_l;

    @Inject(
            at = @At("HEAD"),
            method = "renderModel(Lnet/minecraft/client/render/entity/model/EntityModel;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFF)V"
    )
    static private void aaa(EntityModel<LivingEntity> model, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float red, float green, float blue, CallbackInfo ci) {
        variedMobs_l = entity;
    }

    @ModifyVariable(
            at = @At("HEAD"),
            method = "renderModel(Lnet/minecraft/client/render/entity/model/EntityModel;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFF)V"
    )
    private static Identifier newId(Identifier id) {
        return VariedMobManager.INSTANCE.redirectTexture(id, variedMobs_l, null);
    }
}
