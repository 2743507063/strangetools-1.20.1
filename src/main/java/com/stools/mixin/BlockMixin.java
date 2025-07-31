package com.stools.mixin;

import com.stools.item.materials.ModToolMaterials;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolItem;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Block.class)
public class BlockMixin {

    private static final ModToolMaterials[] FLAME_MATERIALS = {
            ModToolMaterials.BLAZE_POWDER,
            ModToolMaterials.NETHERRACK,
            ModToolMaterials.COAL,
    };

    @Inject(method = "afterBreak", at = @At("HEAD"), cancellable = true)
    private void afterBreak(World world, PlayerEntity player, BlockPos pos, net.minecraft.block.BlockState state,
                            net.minecraft.block.entity.BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        if (world.isClient()) return;

        // 使用模式变量简化类型判断和转换
        if (!(stack.getItem() instanceof ToolItem tool)) return;

        // 直接通过模式匹配获取ModToolMaterials
        if (!(tool.getMaterial() instanceof ModToolMaterials material)) return;

        if (!isFlameTool(material) || !(tool instanceof PickaxeItem)) return;

        // 50%概率触发烧炼效果
        if (world.random.nextFloat() < 0.5f) {
            int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, stack);
            handleSmeltingDrop((ServerWorld) world, pos, (Block) (Object) this, player, fortuneLevel, ci);
        }
        // 不满足概率时自然保留原版逻辑，无需额外代码
    }

    // 添加@Unique注解标识Mixin特有方法，避免与目标类冲突
    @Unique
    private boolean isFlameTool(ModToolMaterials material) {
        for (ModToolMaterials flameMat : FLAME_MATERIALS) {
            if (material == flameMat) {
                return true;
            }
        }
        return false;
    }

    // 添加@Unique注解，且明确参数含义
    @Unique
    private static void handleSmeltingDrop(ServerWorld world, BlockPos pos, Block block, PlayerEntity player,
                                           int fortuneLevel, CallbackInfo ci) {
        ItemStack rawDrop = new ItemStack(block.asItem());
        if (rawDrop.isEmpty()) return;

        Optional<SmeltingRecipe> recipe = world.getRecipeManager()
                .getFirstMatch(RecipeType.SMELTING, new SimpleInventory(rawDrop), world);

        if (recipe.isPresent()) {
            // 处理烧炼掉落
            ItemStack smelted = recipe.get().getOutput(world.getRegistryManager()).copy();
            int dropCount = calculateDropCount(fortuneLevel, world);
            smelted.setCount(dropCount);

            Block.dropStack(world, pos, smelted);
            world.setBlockState(pos, net.minecraft.block.Blocks.AIR.getDefaultState(), 3);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);

            ci.cancel(); // 仅在有配方时取消原版逻辑
        }
        //无配方时自然执行原版逻辑
    }

    // 添加@Unique注解
    @Unique
    private static int calculateDropCount(int fortuneLevel, ServerWorld world) {
        int dropCount = 1;
        if (fortuneLevel > 0 && world.random.nextInt(fortuneLevel + 2) - 1 > 0) {
            dropCount = world.random.nextInt(fortuneLevel + 1) + 1;
        }
        return dropCount;
    }
}
