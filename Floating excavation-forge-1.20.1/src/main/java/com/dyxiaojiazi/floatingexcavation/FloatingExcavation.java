package com.dyxiaojiazi.floatingexcavation;

import com.dyxiaojiazi.floatingexcavation.event.ModEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FloatingExcavation.MODID)
public class FloatingExcavation {
    public static final String MODID = "floatingexcavation";
    public static final String MODNAME = "Floating Excavation";
    public static final Logger LOGGER = LogManager.getLogger(MODNAME);

    public FloatingExcavation() {
        // 使用推荐的方式获取IEventBus
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册模组初始化事件
        modEventBus.addListener(this::commonSetup);

        // 注册事件处理器
        MinecraftForge.EVENT_BUS.register(new ModEvents());

        LOGGER.info("{} v{} 已加载", MODNAME, "1.0.0");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("浮空挖掘模组初始化完成 - 已移除水下和悬空挖掘惩罚");
    }
}