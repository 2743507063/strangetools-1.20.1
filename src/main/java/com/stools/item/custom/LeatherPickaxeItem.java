package com.stools.item.custom;

import com.stools.config.ModConfigManager;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;

public class LeatherPickaxeItem extends PickaxeItem implements DyeableItem {
    private static final String COLOR_KEY = "color";
    private static final int DEFAULT_COLOR = 0xA06540; // 皮革默认颜色

    public LeatherPickaxeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt(DISPLAY_KEY);
        if (nbt != null && nbt.contains(COLOR_KEY, NbtElement.INT_TYPE)) {
            return nbt.getInt(COLOR_KEY);
        }
        return DEFAULT_COLOR;
    }
    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        // 如果开启了染色耐久条，始终显示
        if (ModConfigManager.CONFIG.general.coloredDurabilityBar) {
            return true;
        }

        // 否则使用默认逻辑（只有耐久不满时才显示）
        return super.isItemBarVisible(stack);
    }
    @Override
    public int getItemBarColor(ItemStack stack) {
        // 根据配置决定是否使用染色颜色
        if (ModConfigManager.CONFIG.general.coloredDurabilityBar) {
            return getColor(stack);
        }

        // 默认使用原版耐久条颜色逻辑
        float durabilityRatio = Math.max(0.0F, ((float)stack.getMaxDamage() - (float)stack.getDamage()) / (float)stack.getMaxDamage());
        return MathHelper.hsvToRgb(durabilityRatio / 3.0F, 1.0F, 1.0F);
    }
}