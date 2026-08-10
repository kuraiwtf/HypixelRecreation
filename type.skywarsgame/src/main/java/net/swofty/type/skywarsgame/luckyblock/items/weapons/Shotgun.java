package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.pvp.entity.projectile.Snowball;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.Random;

public class Shotgun implements LuckyBlockWeapon {

    public static final String ID = "shotgun";
    private static final int PELLET_COUNT = 5;
    private static final double SPREAD_ANGLE = 15.0;
    private static final double POWER = 1.5;
    private static final Random RANDOM = new Random();

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Shotgun";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.COMPARATOR;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStacks.raw(Material.COMPARATOR, 6, """
                        <7>Shotgun

                        <7>Right-click to fire 5
                        <7>short-range projectiles
                        <7>in a spread pattern.

                        <7>Uses: <a>6

                        <6><l>LUCKY BLOCK ITEM
                        """)
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }

    @Override
    public boolean onUse(SkywarsPlayer holder) {
        Instance instance = holder.getInstance();
        if (instance == null) return false;

        Pos eyePos = holder.getPosition().add(0, holder.getEyeHeight(), 0);
        float baseYaw = holder.getPosition().yaw();
        float basePitch = holder.getPosition().pitch();

        for (int i = 0; i < PELLET_COUNT; i++) {
            float yawOffset = (float) ((RANDOM.nextDouble() - 0.5) * 2 * SPREAD_ANGLE);
            float pitchOffset = (float) ((RANDOM.nextDouble() - 0.5) * 2 * SPREAD_ANGLE);

            Snowball snowball = new Snowball(holder);
            snowball.setInstance(instance, eyePos);
            snowball.shootFromRotation(basePitch + pitchOffset, baseYaw + yawOffset, 0, POWER, 0, 0.0);
        }

        return true;
    }

    @Override
    public float onWeaponHit(SkywarsPlayer holder, Entity target, float damage) {
        return damage;
    }

    @Override
    public int getMaxUses() {
        return 6;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }

    @Override
    public boolean hasOnHitEffect() {
        return false;
    }
}
