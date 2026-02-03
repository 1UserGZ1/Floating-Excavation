package com.dyxiaojiazi.floatingexcavation.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = com.dyxiaojiazi.floatingexcavation.FloatingExcavation.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();

        // 关键修复：区分不同情况
        boolean isUnderWater = player.isUnderWater(); // 完全浸没（头部在水中）
        boolean isInWater = player.isInWater();       // 任何部位在水中
        boolean isOnGround = player.onGround();       // 站在坚实地面

        // 情况分析：
        // 1. isUnderWater = true, isOnGround = false -> 水下悬空挖掘（需要移除惩罚）✓
        // 2. isUnderWater = true, isOnGround = true  -> 水下站立挖掘（需要移除惩罚）✓
        // 3. isUnderWater = false, isInWater = true, isOnGround = true -> 水面站立挖掘（不应移除惩罚）← 这是问题所在！
        // 4. isUnderWater = false, isInWater = false, isOnGround = false -> 空中挖掘（需要移除惩罚）✓
        // 5. isUnderWater = false, isInWater = true, isOnGround = false -> 水面悬空挖掘（需要移除惩罚）✓

        // 确定是否需要移除惩罚
        boolean shouldRemovePenalty = false;

        // 规则1：如果完全浸没在水中，总是移除惩罚
        if (isUnderWater) {
            shouldRemovePenalty = true;
        }
        // 规则2：如果悬空且不在坚实地面，移除惩罚
        else if (!isOnGround) {
            shouldRemovePenalty = true;
        }
        // 规则3：如果只是接触水面但站在坚实地面，不处理（原版正常）
        // else if (isInWater && isOnGround) {
        //     shouldRemovePenalty = false; // 明确不处理
        // }

        // 应用惩罚移除逻辑
        if (shouldRemovePenalty) {
            float currentSpeed = event.getNewSpeed();
            float originalSpeed = event.getOriginalSpeed();

            // 计算惩罚因子
            float penaltyFactor = 1.0F;

            // 只有完全浸没时才应用水下惩罚
            if (isUnderWater) {
                penaltyFactor *= 0.2F;
            }

            // 只有不在坚实地面时才应用悬空惩罚
            if (!isOnGround) {
                penaltyFactor *= 0.2F;
            }

            // 如果有惩罚，移除它
            if (penaltyFactor < 0.99F) {
                float speedWithoutPenalty = originalSpeed / penaltyFactor;

                // 安全限制：最大加速10倍
                float maxSpeed = originalSpeed * 10.0F;
                speedWithoutPenalty = Math.min(speedWithoutPenalty, maxSpeed);

                // 只增加速度
                if (speedWithoutPenalty > currentSpeed) {
                    event.setNewSpeed(speedWithoutPenalty);

                    // 调试信息
                    boolean debug = System.getProperty("floatingexcavation.debug") != null;
                    if (debug) {
                        System.out.printf("[浮空挖掘] 状态: 浸没=%b, 水中=%b, 地面=%b, 移除惩罚=%b%n",
                                isUnderWater, isInWater, isOnGround, shouldRemovePenalty);
                        System.out.printf("[浮空挖掘] 速度: %.2f -> %.2f (惩罚因子=%.2f)%n",
                                currentSpeed, speedWithoutPenalty, penaltyFactor);
                    }
                }
            }
        }
    }
}