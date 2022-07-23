package com.nyfaria.pokeball.entity;

import com.nyfaria.pokeball.init.EntityInit;
import com.nyfaria.pokeball.init.ItemInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class PokeballEntity extends ThrowableItemProjectile {


    public PokeballEntity(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
    }

    public PokeballEntity(EntityType<? extends ThrowableItemProjectile> p_37432_, double p_37433_, double p_37434_, double p_37435_, Level p_37436_) {
        super(p_37432_, p_37433_, p_37434_, p_37435_, p_37436_);
    }

    public PokeballEntity(Level p_37440_, LivingEntity p_37439_) {
        super(EntityInit.POKEBALL.get(), p_37439_, p_37440_);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        ItemStack itemStack = getItem();
        if (pResult.getEntity() instanceof LivingEntity livingEntity) {
            CompoundTag tag = itemStack.getOrCreateTag();
            if (tag.contains("pokeData")) {
                CompoundTag pokeData = tag.getCompound("pokeData");
                UUID uuid = pokeData.getUUID("UUID");
                UUID uuid2 = livingEntity.getUUID();
                if(uuid.equals(uuid2)) {
                    livingEntity.save(pokeData);
                    itemStack.getTag().put("pokeData", pokeData);
                    itemStack.getTag().putString("name",livingEntity.getType().getDescription().getString());
                    itemStack.getTag().putString("pokeType", livingEntity.getType().getRegistryName().toString());
                    itemStack.getTag().putBoolean("inBall", true);
                    livingEntity.remove(RemovalReason.DISCARDED);
                }

            } else {
                CompoundTag pokeData = new CompoundTag();
                livingEntity.save(pokeData);
                itemStack.getOrCreateTag().put("pokeData", pokeData);
                itemStack.getTag().putString("name",livingEntity.getType().getDescription().getString());
                itemStack.getTag().putString("pokeType", livingEntity.getType().getRegistryName().toString());
                itemStack.getTag().putBoolean("inBall", true);
                livingEntity.remove(RemovalReason.DISCARDED);
            }
        }
        spawnDroppedItemStack(level, position(), itemStack);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        ItemStack itemStack = getItem();
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.contains("pokeData") && tag.contains("inBall") && tag.getBoolean("inBall")) {
            LivingEntity livingEntity = (LivingEntity) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString("pokeType"))).create(level);
            livingEntity.load(itemStack.getOrCreateTag().getCompound("pokeData"));
            livingEntity.setPos(position().x, position().y + 0.5, position().z);
            level.addFreshEntity(livingEntity);
            itemStack.getTag().putBoolean("inBall", false);
        }
        spawnDroppedItemStack(level, position(), itemStack);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.POKEBALL.get();
    }

    public static void spawnDroppedItemStack(Level level, Vec3 vec3, ItemStack stack) {
        ItemEntity drop = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), stack);
        drop.setDefaultPickUpDelay();
        level.addFreshEntity(drop);
    }
}
