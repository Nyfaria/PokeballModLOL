package com.nyfaria.pokeball.init;

import com.nyfaria.pokeball.Pokeball;
import com.nyfaria.pokeball.item.PokeballItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Pokeball.MODID);

    public static final RegistryObject<Item> POKEBALL = ITEMS.register("pokeball", () -> new PokeballItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
}
