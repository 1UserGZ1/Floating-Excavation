package com.dyxiaojiazi.floatingexcavation.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = com.dyxiaojiazi.floatingexcavation.FloatingExcavation.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerBreak(PlayerEvent.BreakSpeed event) {
        if (event.getEntity().isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(event.getEntity())) {
            event.setNewSpeed(event.getOriginalSpeed() * 5);
        }

        if (!event.getEntity().onGround()) {
            event.setNewSpeed(event.getOriginalSpeed() * 5);
        }
    }

}

