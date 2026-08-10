package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.item.SplashPotionMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.pvp.entity.projectile.CustomEntityProjectile;
import net.swofty.pvp.entity.projectile.ItemHoldingProjectile;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItem;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaterBalloon implements LuckyBlockItem {

    @Override
    public String getId() {
        return "water_balloon";
    }

    @Override
    public String getDisplayName() {
        return "Water Balloon";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStacks.item(Material.SPLASH_POTION, """
                <b><l>Water Balloon</l>
                <7>Throw this at your enemies
                <7>to splash water where it lands!

                <e>Right-click to throw!""")
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public boolean onUse(SkywarsPlayer holder) {
        Instance instance = holder.getInstance();
        if (instance == null) return false;

        WaterBalloonProjectile projectile = new WaterBalloonProjectile(holder, createItemStack());
        projectile.setInstance(instance, holder.getPosition().add(0, holder.getEyeHeight(), 0));
        projectile.shootFromRotation(holder.getPosition().pitch(), holder.getPosition().yaw(), 0, 1.0, 1.0, 0.0);

        return true;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }

    private static class WaterBalloonProjectile extends CustomEntityProjectile implements ItemHoldingProjectile {
        private final ItemStack displayItem;

        public WaterBalloonProjectile(@Nullable Entity shooter, ItemStack displayItem) {
            super(shooter, EntityType.SPLASH_POTION);
            this.displayItem = displayItem;
            setItem(displayItem);
        }

        @Override
        public boolean onStuck() {
            triggerStatus((byte) 3);
            placeWaterPool();
            return true;
        }

        @Override
        public boolean onHit(Entity entity) {
            triggerStatus((byte) 3);
            placeWaterPool();
            return true;
        }

        private void placeWaterPool() {
            if (instance == null) return;

            Point pos = position;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    Point waterPos = pos.add(dx, 0, dz);
                    Block blockAt = instance.getBlock(waterPos);
                    if (blockAt.air()) {
                        instance.setBlock(waterPos, Block.WATER);
                    } else {
                        Point waterPosAbove = pos.add(dx, 1, dz);
                        Block blockAbove = instance.getBlock(waterPosAbove);
                        if (blockAbove.air()) {
                            instance.setBlock(waterPosAbove, Block.WATER);
                        }
                    }
                }
            }
        }

        @Override
        public void setItem(@NotNull ItemStack item) {
            ((SplashPotionMeta) getEntityMeta()).setItem(item);
        }
    }
}
