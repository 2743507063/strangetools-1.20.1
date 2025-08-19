package com.stools.mixin;

import com.stools.item.ModItems;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    @Inject(method = "createFuelTimeMap", at = @At("TAIL"))
    private static void addFuelItems(CallbackInfoReturnable<Map<Item, Integer>> cir) {
        cir.getReturnValue().put(ModItems.RAW_VOID, 3200);
        cir.getReturnValue().put(ModItems.VOID_INGOT, 4200);
        Map<Item, Integer> fuelMap = cir.getReturnValue();

        addCoalToolsAsFuel(fuelMap);
        addBlazeToolsAsFuel(fuelMap);
    }

    private static void addCoalToolsAsFuel(Map<Item, Integer> fuelMap) {
        int fuelTime = 2400;
        fuelMap.put(ModItems.TOOLS.get("coal_sword"), fuelTime);
        fuelMap.put(ModItems.TOOLS.get("coal_pickaxe"), fuelTime);
        fuelMap.put(ModItems.TOOLS.get("coal_axe"), fuelTime);
        fuelMap.put(ModItems.TOOLS.get("coal_shovel"), fuelTime);
        fuelMap.put(ModItems.TOOLS.get("coal_hoe"), fuelTime);
        fuelMap.put(ModItems.ARMORS.get("coal_boots"), fuelTime);
        fuelMap.put(ModItems.ARMORS.get("coal_helmet"), fuelTime);
        fuelMap.put(ModItems.ARMORS.get("coal_chestplate"), fuelTime);
        fuelMap.put(ModItems.ARMORS.get("coal_leggings"), fuelTime);
    }
    private static void addBlazeToolsAsFuel(Map<Item, Integer> fuelMap) {
        int fuelTime = 3600;
        fuelMap.put(ModItems.TOOLS.get("blaze_powder_sword"), fuelTime);
        fuelMap.put(ModItems.TOOLS.get("blaze_powder_pickaxe"), fuelTime);
        fuelMap.put(ModItems.TOOLS.get("blaze_powder_axe"), fuelTime);
        fuelMap.put(ModItems.TOOLS.get("blaze_powder_shovel"), fuelTime);
        fuelMap.put(ModItems.TOOLS.get("blaze_powder_hoe"), fuelTime);
    }
}