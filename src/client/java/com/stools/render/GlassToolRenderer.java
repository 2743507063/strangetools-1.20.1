package com.stools.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

public class GlassToolRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private static final Identifier EMPTY_MODEL = new Identifier("strangetools:item/glass_axe_empty");
    private static final Identifier WATER_MODEL = new Identifier("strangetools:item/glass_axe_water");
    private static final Identifier LAVA_MODEL = new Identifier("strangetools:item/glass_axe_lava");
    
    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, 
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        
        // 获取物品渲染器
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        
        // 根据NBT内容选择模型
        Identifier modelId = EMPTY_MODEL;
        if (stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();
            if (nbt.contains("BottleContent")) {
                String content = nbt.getString("BottleContent");
                if ("water".equals(content)) {
                    modelId = WATER_MODEL;
                } else if ("lava".equals(content)) {
                    modelId = LAVA_MODEL;
                }
            }
        }
        
        // 获取模型并渲染
        BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(modelId);
        itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, model);
    }
}