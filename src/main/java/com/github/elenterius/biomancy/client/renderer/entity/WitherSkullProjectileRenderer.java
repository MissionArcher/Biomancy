package com.github.elenterius.biomancy.client.renderer.entity;

import com.github.elenterius.biomancy.entity.projectile.WitherSkullProjectileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WitherSkullProjectileRenderer extends EntityRenderer<WitherSkullProjectileEntity> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/wither/wither.png");
	private final GenericHeadModel skullModel = new GenericHeadModel();

	public WitherSkullProjectileRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		skullModel.setupAnim(0, -90f, 0);
	}

	@Override
	protected int getBlockLightLevel(WitherSkullProjectileEntity entityIn, BlockPos pos) {
		return 15;
	}

	@Override
	public void render(WitherSkullProjectileEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		matrixStackIn.translate(0, 0.5F, 0);
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.yRotO, entityIn.yRot) - 90f));
		matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot)));

		IVertexBuilder vertexBuilder = bufferIn.getBuffer(skullModel.renderType(getTextureLocation(entityIn)));
		skullModel.renderToBuffer(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
		matrixStackIn.popPose();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(WitherSkullProjectileEntity entity) {
		return TEXTURE;
	}

}
