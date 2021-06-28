package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.RedirectContext;
import it.digifox03.variedmobs.VariedMobs;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin {
    private LivingEntity variedMobs_ll;
    private EquipmentSlot variedMobs_slot;

    @Inject(
            at = @At("HEAD"),
            method = "renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/model/BipedEntityModel;)V"
    )
    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity livingEntity, EquipmentSlot equipmentSlot, int i, @SuppressWarnings("rawtypes") BipedEntityModel bipedEntityModel, CallbackInfo ci) {
        variedMobs_ll = livingEntity;
        variedMobs_slot = equipmentSlot;
    }

    @Inject(
            at = @At("RETURN"),
            method = "getArmorTexture(Lnet/minecraft/item/ArmorItem;ZLjava/lang/String;)Lnet/minecraft/util/Identifier;",
            cancellable = true
    )
    private void getArmorTexture(ArmorItem armorItem, boolean bl, String string, CallbackInfoReturnable<Identifier> cir) {
        Map<Identifier, Object> ctx = new HashMap<>();
        ctx.put(RedirectContext.slot, variedMobs_slot);
        ctx.put(RedirectContext.entity, variedMobs_ll);
        ctx.put(RedirectContext.item, variedMobs_ll.getEquippedStack(variedMobs_slot));
        cir.setReturnValue(VariedMobs.redirectTexture(cir.getReturnValue(), ctx));
    }
}
