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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Block.class)
public class BlockMixin {

    // 定义火焰相关的工具材料
    private static final ModToolMaterials[] FLAME_MATERIALS = {
            ModToolMaterials.BLAZE_POWDER,
            ModToolMaterials.NETHERRACK,
            ModToolMaterials.COAL,
    };

    @Inject(method = "afterBreak", at = @At("HEAD"), cancellable = true)
    private void afterBreak(World world, PlayerEntity player, BlockPos pos, net.minecraft.block.BlockState state, net.minecraft.block.entity.BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        // 如果是客户端世界，直接返回
        if (world.isClient()) return;

        // 检查手持物品是否为工具
        if (!(stack.getItem() instanceof ToolItem)) return;

        ToolItem tool = (ToolItem) stack.getItem();
        net.minecraft.item.ToolMaterial vanillaMaterial = tool.getMaterial();
        ModToolMaterials material = null;

        // 检查工具材料是否为自定义的 ModToolMaterials 类型
        if (vanillaMaterial instanceof ModToolMaterials) {
            material = (ModToolMaterials) vanillaMaterial;
        } else {
            // 不是自定义材料，不处理，继续原有逻辑
            return;
        }

        // 检查是否是火焰材料的工具
        if (!isFlameTool(material)) return;

        // 检查是否为镐子
        if (!(tool instanceof PickaxeItem)) return;

        // 50%概率触发烧炼效果
        if (world.random.nextFloat() < 0.5f) {
            int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, stack);
            handleSmeltingDrop((ServerWorld) world, pos, (Block) (Object) this, player, fortuneLevel);
            // 取消原有的掉落逻辑
            ci.cancel();
        }
        // 不满足条件时不取消原有的掉落逻辑
    }

    /**
     * 检查工具材料是否为火焰材料
     * @param material 工具材料
     * @return 如果是火焰材料返回 true，否则返回 false
     */
    private boolean isFlameTool(ModToolMaterials material) {
        for (ModToolMaterials flameMat : FLAME_MATERIALS) {
            if (material == flameMat) {
                return true;
            }
        }
        return false;
    }

    private static void handleSmeltingDrop(ServerWorld world, BlockPos pos, Block block, PlayerEntity player, int fortuneLevel) {
        // 获取原始掉落物
        ItemStack rawDrop = new ItemStack(block.asItem());
        if (rawDrop.isEmpty()) return;

        // 查找烧炼配方
        Optional<SmeltingRecipe> recipe = world.getRecipeManager()
                .getFirstMatch(RecipeType.SMELTING, new SimpleInventory(rawDrop), world);

        if (recipe.isPresent()) {
            // 获取烧炼后的物品
            ItemStack smelted = recipe.get().getOutput(world.getRegistryManager());

            // 根据时运等级调整掉落数量
            int dropCount = calculateDropCount(fortuneLevel, world);

            // 生成烧炼后的物品
            for (int i = 0; i < dropCount; i++) {
                Block.dropStack(world, pos, smelted.copy());
            }

            // 手动更新方块状态为空，防止原始物品掉落
            world.setBlockState(pos, net.minecraft.block.Blocks.AIR.getDefaultState(), 3);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }
    }

    /**
     * 根据时运等级计算掉落数量
     * @param fortuneLevel 时运等级
     * @param world 游戏世界
     * @return 计算后的掉落数量
     */
    private static int calculateDropCount(int fortuneLevel, ServerWorld world) {
        int dropCount = 1;
        if (fortuneLevel > 0) {
            if (world.random.nextInt(fortuneLevel + 2) - 1 > 0) {
                dropCount = world.random.nextInt(fortuneLevel + 1) + 1;
            }
        }
        return dropCount;
    }
}