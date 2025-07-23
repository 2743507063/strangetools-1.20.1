package com.stools.render.entity;

import com.stools.entity.EnderPhantomEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class EnderPhantomRenderer extends EntityRenderer<EnderPhantomEntity> {
    public EnderPhantomRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EnderPhantomEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // 添加空检查 - 确保UUID不为null
        if (entity.getPlayerUuid() == null) {
            return;
        }

        PlayerEntity player = entity.getWorld().getPlayerByUuid(entity.getPlayerUuid());
        if (player == null) return;

        // 直接使用玩家皮肤纹理
        Identifier skinTexture = MinecraftClient.getInstance().getSkinProvider().loadSkin(player.getGameProfile());

        // 使用Steve模型（粗手臂
        PlayerEntityModel<PlayerEntity> model = new PlayerEntityModel<>(
                TexturedModelData.of(PlayerEntityModel.getTexturedModelData(Dilation.NONE, false), 64, 64)
                        .createModel(),
                false // 不是slim模型
        );

        matrices.push();
        matrices.translate(0, 0.1, 0); // 稍微浮空
        matrices.scale(1.02f, 1.02f, 1.02f); // 比玩家稍大

        // 渲染玩家模型（只渲染皮肤）
        renderSimplePlayerModel(model, player, tickDelta, matrices, vertexConsumers, light, skinTexture);
        matrices.pop();
    }

    private void renderSimplePlayerModel(
            PlayerEntityModel<PlayerEntity> model,
            PlayerEntity entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            Identifier texture) {

        // 设置模型角度和动画
        float limbAngle = entity.limbAnimator.getPos();
        float limbDistance = entity.limbAnimator.getSpeed();
        model.setAngles(entity, limbAngle, limbDistance, entity.age + tickDelta, entity.headYaw, entity.getPitch());

        // 创建半透明顶点消费者
        VertexConsumer translucentVertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucent(texture)
        );

        // 只渲染身体部分（不渲染盔甲和手部物品）
        matrices.push();
        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.translate(0.0D, -1.501F, 0.0D);

        // 渲染各个身体部位（只渲染皮肤）
        model.head.render(matrices, translucentVertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 0.7f);
        model.body.render(matrices, translucentVertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 0.7f);
        model.rightArm.render(matrices, translucentVertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 0.7f);
        model.leftArm.render(matrices, translucentVertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 0.7f);
        model.rightLeg.render(matrices, translucentVertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 0.7f);
        model.leftLeg.render(matrices, translucentVertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 0.7f);

        // 渲染帽子层
        matrices.push();
        matrices.scale(1.01f, 1.01f, 1.01f); // 稍微放大以覆盖底层
        model.hat.render(matrices, translucentVertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 0.7f);
        matrices.pop();

        matrices.pop();
    }

    @Override
    public Identifier getTexture(EnderPhantomEntity entity) {
        return null; // 使用玩家皮肤，不需要单独纹理
    }
}