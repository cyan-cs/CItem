package teamPasuta.citem.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teamPasuta.citem.client.CitemClient;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Inject(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            shift = At.Shift.AFTER
        )
    )
    private void applyCustomAnimation(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (hand == Hand.MAIN_HAND && CitemClient.animationTicks > 0 && !item.isEmpty()) {
            float remainingTicks = CitemClient.animationTicks - tickDelta;
            float progress = 1.0f - (remainingTicks / CitemClient.animationDuration);

            // Select animation based on the random ID from CitemClient
            switch (CitemClient.activeAnimationId) {
                case 0: // Original: Toss and Y-axis spin
                    float tossHeight = (float) Math.sin(progress * Math.PI) * 0.5f;
                    matrices.translate(0.0, tossHeight, 0.0);
                    float spinAngleY = progress * 360.0f;
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(spinAngleY));
                    break;

                case 1: // New: Forward flip (X-axis spin)
                    float flipAngleX = progress * 360.0f;
                    matrices.translate(0.0, 0.2, -0.4); // Move it a bit to center for a better flip visual
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(flipAngleX));
                    break;

                case 2: // New: Side roll (Z-axis spin)
                    float rollAngleZ = progress * 360.0f;
                    matrices.translate(0.0, 0.3, -0.2); // Move it a bit to center
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rollAngleZ));
                    break;
            }
        }
    }
}