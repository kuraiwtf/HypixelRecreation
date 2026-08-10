package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.pvp.entity.projectile.ThrownEgg;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;
import org.jetbrains.annotations.Nullable;

public class BridgeEggs implements LuckyBlockConsumable {

    private static final Block BRIDGE_BLOCK = Block.WHITE_WOOL;

    @Override
    public String getId() {
        return "bridge_eggs";
    }

    @Override
    public String getDisplayName() {
        return "Bridge Eggs";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStacks.item(Material.EGG, 4, """
                <f><l>Bridge Eggs</l>
                <7>Throw an egg that creates
                <7>a bridge as it flies!

                <e>Right-click to throw!""")
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public void onConsume(SkywarsPlayer player) {
        SkywarsPlayerBridgeEgg egg = new SkywarsPlayerBridgeEgg(BRIDGE_BLOCK, player);
        egg.setInstance(player.getInstance(), player.getPosition().add(0, player.getEyeHeight(), 0));
        egg.setVelocity(player.getPosition().direction().mul(30));
    }

    private static class SkywarsPlayerBridgeEgg extends ThrownEgg {
        private final Block block;

        public SkywarsPlayerBridgeEgg(Block block, @Nullable Entity shooter) {
            super(shooter);
            this.block = block;
        }

        @Override
        public void tick(long time) {
            super.tick(time);

            if (this.instance != null && this.position != null) {
                Vec velocity = this.getVelocity();

                double length = Math.sqrt(velocity.x() * velocity.x() + velocity.z() * velocity.z());
                if (length > 0) {
                    double offsetX = -velocity.x() / length;
                    double offsetZ = -velocity.z() / length;

                    Point center = this.position.sub(0, 1, 0).add(offsetX, 0, offsetZ);

                    for (int x = -1; x <= 0; x++) {
                        for (int z = -1; z <= 0; z++) {
                            Point blockPos = center.add(x, 0, z);
                            if (this.instance.getBlock(blockPos).air()) {
                                this.instance.setBlock(blockPos, block);
                            }
                        }
                    }
                }
            }
        }
    }
}
