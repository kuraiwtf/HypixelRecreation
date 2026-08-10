package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.pvp.entity.projectile.Snowball;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class Paintballs implements LuckyBlockWeapon {

    public static final String ID = "paintballs";

    private static final Block[] TERRACOTTA_COLORS = {
            Block.WHITE_TERRACOTTA,
            Block.ORANGE_TERRACOTTA,
            Block.MAGENTA_TERRACOTTA,
            Block.LIGHT_BLUE_TERRACOTTA,
            Block.YELLOW_TERRACOTTA,
            Block.LIME_TERRACOTTA,
            Block.PINK_TERRACOTTA,
            Block.GRAY_TERRACOTTA,
            Block.LIGHT_GRAY_TERRACOTTA,
            Block.CYAN_TERRACOTTA,
            Block.PURPLE_TERRACOTTA,
            Block.BLUE_TERRACOTTA,
            Block.BROWN_TERRACOTTA,
            Block.GREEN_TERRACOTTA,
            Block.RED_TERRACOTTA,
            Block.BLACK_TERRACOTTA
    };

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Paintballs";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.SNOWBALL;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStacks.raw(Material.SNOWBALL, 64, """
                        <d>Paintballs

                        <7>Throw to paint blocks
                        <7>with random colors!

                        <7>Uses: <a>64

                        <6><l>LUCKY BLOCK ITEM
                        """)
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }

    @Override
    public boolean onUse(SkywarsPlayer holder) {
        if (holder.getInstance() == null) return false;

        PaintballSnowball snowball = new PaintballSnowball(holder);
        snowball.setInstance(holder.getInstance(), holder.getPosition().add(0, holder.getEyeHeight(), 0));
        snowball.setVelocity(holder.getPosition().direction().mul(30));

        return true;
    }

    @Override
    public float onWeaponHit(SkywarsPlayer holder, Entity target, float damage) {
        return damage;
    }

    @Override
    public int getMaxUses() {
        return 64;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }

    @Override
    public boolean hasOnHitEffect() {
        return false;
    }

    private static class PaintballSnowball extends Snowball {

        public PaintballSnowball(@Nullable Entity shooter) {
            super(shooter);
        }

        @Override
        public boolean onStuck() {
            triggerStatus((byte) 3);
            paintNearbyBlocks();
            return true;
        }

        @Override
        public boolean onHit(Entity entity) {
            triggerStatus((byte) 3);
            paintNearbyBlocks();
            return true;
        }

        private void paintNearbyBlocks() {
            if (instance == null) return;

            Block color = TERRACOTTA_COLORS[ThreadLocalRandom.current().nextInt(TERRACOTTA_COLORS.length)];
            Point pos = position;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        Point blockPos = pos.add(dx, dy, dz);
                        Block block = instance.getBlock(blockPos);
                        if (!block.air() && !block.compare(Block.BEDROCK)) {
                            instance.setBlock(blockPos, color);
                        }
                    }
                }
            }
        }
    }
}
