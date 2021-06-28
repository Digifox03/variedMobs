package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.RedirectContext;
import it.digifox03.variedmobs.VariedMobs;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EndermanEyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SpiderEyesFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(SpiderEyesFeatureRenderer.class)
abstract class SpiderEyeFeatureRendererMixin extends EyesFeatureRenderer<Entity, EntityModel<Entity>> {
    protected LivingEntity variedMobs_ll;
    static private final Identifier id = new Identifier("textures/entity/spider_eyes.png");

    public SpiderEyeFeatureRendererMixin(FeatureRendererContext<Entity, EntityModel<Entity>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Inject(
            at = @At("RETURN"),
            method = "getEyesTexture",
            cancellable = true
    )
    public void getEyesTexture(CallbackInfoReturnable<RenderLayer> cir) {
        Map<Identifier, Object> ctx = new HashMap<>();
        ctx.put(RedirectContext.entity, variedMobs_ll);
        cir.setReturnValue(
                RenderLayer.getEyes(VariedMobs.redirectTexture(id, ctx))
        );
    }
}
