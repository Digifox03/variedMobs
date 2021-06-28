package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.RedirectContext;
import it.digifox03.variedmobs.VariedMobs;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PaintingEntityRenderer.class)
public abstract class PaintingEntityRenderMixin {
    @Inject(
            at = @At("RETURN"),
            method = "getTexture(Lnet/minecraft/entity/decoration/painting/PaintingEntity;)Lnet/minecraft/util/Identifier;",
            cancellable = true
    )
    public void getTexture(PaintingEntity paintingEntity, CallbackInfoReturnable<Identifier> cir) {
        Map<Identifier, Object> ctx = new java.util.HashMap<>();
        ctx.put(RedirectContext.entity, paintingEntity);
        cir.setReturnValue(VariedMobs.redirectTexture(cir.getReturnValue(), ctx));
    }
}
