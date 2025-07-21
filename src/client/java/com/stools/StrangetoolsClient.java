package com.stools;

import com.stools.render.GlassToolRenderer;
import com.stools.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class StrangetoolsClient implements ClientModInitializer {
	private static final String GLASS = "FUCK GLASS TOOLS";
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
	}
}