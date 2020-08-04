package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.core.VariedMobManager;
import net.minecraft.resource.*;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManagerImpl.class)
@Debug(export = true)
public abstract class ClientMixin implements ReloadableResourceManager {
    @Final
    @Shadow
    private ResourceType type;

    private Boolean variedMobs_init = false;

    @Inject(
            method = "beginMonitoredReload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/resource/ResourceReloadMonitor;",
            at = @At("HEAD")
    )
    public void regs(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs, CallbackInfoReturnable<ResourceReloadMonitor> cir) {
        if (!variedMobs_init) {
            variedMobs_init = true;
            if (type == ResourceType.CLIENT_RESOURCES) {
                this.registerListener(VariedMobManager.INSTANCE);
            }
        }
    }
}
