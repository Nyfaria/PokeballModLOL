package com.nyfaria.pokeball;

import com.nyfaria.pokeball.cap.ExampleHolderAttacher;
import com.nyfaria.pokeball.datagen.*;
import com.nyfaria.pokeball.init.BlockInit;
import com.nyfaria.pokeball.init.EntityInit;
import com.nyfaria.pokeball.init.ItemInit;
import com.nyfaria.pokeball.network.NetworkHandler;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Pokeball.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Pokeball {
    public static final String MODID = "pokeball";
    public static final Logger LOGGER = LogManager.getLogger();

    public Pokeball() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ENTITIES.register(bus);
        ExampleHolderAttacher.register();

    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(new ModRecipeProvider(generator));
            generator.addProvider(new ModLootTableProvider(generator));
            generator.addProvider(new ModSoundProvider(generator, MODID,existingFileHelper));
        }
        if (event.includeClient()) {
            generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
            generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));
            generator.addProvider(new ModLangProvider(generator, MODID, "en_us"));
        }
    }
}
