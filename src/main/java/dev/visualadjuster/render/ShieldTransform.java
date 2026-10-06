package dev.visualadjuster.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class ShieldTransform {
    public float scaleX = 1.0F;
    public float scaleY = 1.0F;
    public float scaleZ = 1.0F;
    public float rotationX;
    public float rotationY;
    public float rotationZ;
    public float translationX;
    public float translationY;
    public float translationZ;

    public ShieldTransform() {
    }

    public ShieldTransform(float scaleX, float scaleY, float scaleZ,
                           float rotationX, float rotationY, float rotationZ,
                           float translationX, float translationY, float translationZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.translationX = translationX;
        this.translationY = translationY;
        this.translationZ = translationZ;
    }

    public boolean isIdentity() {
        return scaleX == 1.0F && scaleY == 1.0F && scaleZ == 1.0F
                && rotationX == 0.0F && rotationY == 0.0F && rotationZ == 0.0F
                && translationX == 0.0F && translationY == 0.0F && translationZ == 0.0F;
    }

    public void apply(MatrixStack matrices) {
        if (isIdentity()) {
            return;
        }
        matrices.translate(translationX / 16.0F, translationY / 16.0F, translationZ / 16.0F);
        if (rotationX != 0.0F) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationX));
        }
        if (rotationY != 0.0F) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationY));
        }
        if (rotationZ != 0.0F) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotationZ));
        }
        matrices.scale(scaleX, scaleY, scaleZ);
    }
}
