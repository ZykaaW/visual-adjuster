package dev.visualadjuster.mixin;

import dev.visualadjuster.config.VisualAdjusterConfig;
import dev.visualadjuster.render.ShieldTransform;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class ShieldRenderMixin {
    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void visualadjuster$pushAndApply(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand,
                                             float swingProgress, ItemStack item, float equipProgress,
                                             MatrixStack matrices, OrderedRenderCommandQueue queue,
                                             int light, CallbackInfo ci) {
        matrices.push();
        if (!item.isOf(Items.SHIELD)) {
            return;
        }

        boolean rendersOnRight = (hand == Hand.MAIN_HAND) == (player.getMainArm() == Arm.RIGHT);
        ShieldTransform transform = VisualAdjusterConfig.INSTANCE.createTransform(rendersOnRight);
        transform.apply(matrices);
    }

    @Inject(method = "renderFirstPersonItem", at = @At("RETURN"))
    private void visualadjuster$pop(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand,
                                    float swingProgress, ItemStack item, float equipProgress,
                                    MatrixStack matrices, OrderedRenderCommandQueue queue,
                                    int light, CallbackInfo ci) {
        matrices.pop();
    }
}
