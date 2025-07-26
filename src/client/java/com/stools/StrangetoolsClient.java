package com.stools;

import com.stools.entity.ModEntities;
import com.stools.item.ModItems;
import com.stools.render.entity.EnderPhantomRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class StrangetoolsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModItems.TOOLS.forEach((id, item) -> {
			if (id.startsWith("glass_")) {
				ModelPredicateProviderRegistry.register(item,
						new Identifier("strangetools", "bottle_content"),
						(stack, world, entity, seed) -> {
							if (!stack.hasNbt()) return 0.0F;

							NbtCompound nbt = stack.getNbt();
							if (!nbt.contains("BottleContent")) return 0.0F;

							String content = nbt.getString("BottleContent");

							// 只处理水和空瓶状态
							return "water".equals(content) ? 1.0F : 0.0F;
						}
				);
			}
		});
		EntityRendererRegistry.register(ModEntities.ENDER_PHANTOM, EnderPhantomRenderer::new);
		// 注册虚影珍珠渲染器（使用原版投掷物品渲染器）
		EntityRendererRegistry.register(ModEntities.VOID_PEARL,
				context -> new FlyingItemEntityRenderer<>(context, 1.5f, true)); //渲染尺寸
	}
}