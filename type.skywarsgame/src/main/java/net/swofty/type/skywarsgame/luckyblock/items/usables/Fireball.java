package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.pvp.projectile.entities.FireballProjectile;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

public class Fireball implements LuckyBlockConsumable {

    @Override
    public String getId() {
        return "fireball";
    }

    @Override
    public String getDisplayName() {
        return "Fireball";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStacks.item(Material.FIRE_CHARGE, 8, """
                <c><l>Fireball</l>
                <7>Launch an explosive fireball!

                <e>Right-click to use!""")
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public void onConsume(SkywarsPlayer player) {
        new FireballProjectile(EntityType.FIREBALL, player)
                .shoot(player.getPosition().add(0, player.getEyeHeight(), 0).asVec(), 1, 1);
        player.playSound(Sound.sound(Key.key("minecraft:entity.ghast.shoot"), Sound.Source.PLAYER, 1f, 1f), Sound.Emitter.self());
    }
}
