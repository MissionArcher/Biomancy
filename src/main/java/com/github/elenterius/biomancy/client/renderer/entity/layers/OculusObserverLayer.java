package com.github.elenterius.biomancy.client.renderer.entity.layers;

import com.github.elenterius.biomancy.client.renderer.entity.OculusObserverRenderer;
import com.github.elenterius.biomancy.client.renderer.entity.model.OculusObserverModel;
import com.github.elenterius.biomancy.entity.aberration.OculusObserverEntity;
import com.github.elenterius.biomancy.init.ClientSetupHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OculusObserverLayer<T extends PlayerEntity> extends LayerRenderer<T, PlayerModel<T>> {

	//	private static final Set<String> NAMES = ImmutableSet.of("Dev", "Dev2");
	private static final float SHOULDER_OFFSET = 0.5f;

	private final OculusObserverModel<OculusObserverEntity> model = new OculusObserverModel<>();
	private final boolean leftShoulder = true;

	public OculusObserverLayer(IEntityRenderer<T, PlayerModel<T>> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (/*NAMES.contains(livingEntity.getGameProfile().getName()) ||*/ ClientSetupHandler.isPlayerCosmeticVisible(livingEntity)) {

			boolean isFlying = livingEntity.getFallFlyingTicks() > 4;
			boolean isSwimming = livingEntity.isVisuallySwimming();

			float swimAnimation = livingEntity.getSwimAmount(partialTicks);
			float partialAngle = (float) Math.PI / 180f;
			double verticalOffset = 0;
			double horizontalOffset = 0;

			model.body.yRot = netHeadYaw * partialAngle;
			if (isFlying) {
				model.body.xRot = (-(float) Math.PI / 4f);
				verticalOffset = 3;
				horizontalOffset = 0.5f;
			}
			else if (swimAnimation > 0f) {
				if (isSwimming) {
					model.body.xRot = lerpAngle(swimAnimation, model.body.xRot, (-(float) Math.PI / 4f));
					verticalOffset = 3;
					horizontalOffset = 0.5f;
				}
				else model.body.xRot = lerpAngle(swimAnimation, model.body.xRot, headPitch * partialAngle);
			}
			else model.body.xRot = headPitch * partialAngle;

			matrixStackIn.pushPose();
			verticalOffset += -1f + (livingEntity.isCrouching() ? (double) -1.3f : -1.5d);
			matrixStackIn.translate(leftShoulder ? SHOULDER_OFFSET + horizontalOffset : -SHOULDER_OFFSET - horizontalOffset, verticalOffset + MathHelper.cos(livingEntity.tickCount * 0.8f) * (float) Math.PI * 0.015f, 0);
			IVertexBuilder buffer = bufferIn.getBuffer(model.renderType(OculusObserverRenderer.TEXTURE));
			model.renderOnPlayer(OculusObserverModel.State.HOVERING, matrixStackIn, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, limbSwing, limbSwingAmount, netHeadYaw, headPitch, livingEntity.tickCount);
			matrixStackIn.popPose();
		}
	}

	protected float lerpAngle(float angle, float maxAngle, float multiplier) {
		float tau = (float) Math.PI * 2f;
		float v = (multiplier - maxAngle) % tau;
		if (v < -(float) Math.PI) v += tau;
		if (v >= (float) Math.PI) v -= tau;
		return maxAngle + angle * v;
	}

}