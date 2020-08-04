package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.core.VariedMobManager;
import it.digifox03.variedmobs.core.VariedMobManagerKt;
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

    @Inject(method = "renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/model/BipedEntityModel;)V", at = @At("HEAD"))
    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity livingEntity, EquipmentSlot equipmentSlot, int i, @SuppressWarnings("rawtypes") BipedEntityModel bipedEntityModel, CallbackInfo ci) {
        variedMobs_ll = livingEntity;
        variedMobs_slot = equipmentSlot;
    }

    @Inject(method = "getArmorTexture(Lnet/minecraft/item/ArmorItem;ZLjava/lang/String;)Lnet/minecraft/util/Identifier;", at = @At("RETURN"), cancellable = true)
    private void getArmorTexture(ArmorItem armorItem, boolean bl, String string, CallbackInfoReturnable<Identifier> cir) {
        Map<Identifier, Object> ctx = new HashMap<>();
        ctx.put(new Identifier(VariedMobManagerKt.MODID, "slot"), variedMobs_slot);
        cir.setReturnValue(VariedMobManager.INSTANCE.redirectTexture(cir.getReturnValue(), variedMobs_ll, ctx));
    }
}
