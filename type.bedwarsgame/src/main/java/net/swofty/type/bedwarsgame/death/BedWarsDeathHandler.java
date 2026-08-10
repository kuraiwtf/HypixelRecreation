package net.swofty.type.bedwarsgame.death;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.entity.Entity;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.commons.text.Text;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BedWarsDeathHandler {

    private BedWarsDeathHandler() {
    }

    public static BedWarsDeathResult calculateDeath(@NotNull BedWarsPlayer victim, @NotNull BedWarsGame game, boolean isVoidKill) {
        TeamKey teamKey = victim.getTeamKey();
        boolean isFinalKill = teamKey != null && !game.isBedAlive(teamKey);

        BedWarsPlayer recentAttacker = BedWarsCombatTracker.getRecentAttacker(victim);
        Material lastAttackerWeapon = BedWarsCombatTracker.getLastAttackerWeapon(victim);

        BedWarsDeathResult.Builder builder = BedWarsDeathResult.builder()
            .victim(victim)
            .isFinalKill(isFinalKill)
            .weaponUsed(lastAttackerWeapon);

        if (isVoidKill) {
            if (recentAttacker != null) {
                return builder
                    .deathType(BedWarsDeathType.VOID_ASSISTED)
                    .assistPlayer(recentAttacker)
                    .build();
            }
            return builder
                .deathType(BedWarsDeathType.VOID)
                .build();
        }

        if (recentAttacker != null) {
            BedWarsDeathType type = isRangedWeapon(lastAttackerWeapon) ? BedWarsDeathType.BOW : BedWarsDeathType.GENERIC_ASSISTED;
            return builder
                .deathType(type)
                .killer(recentAttacker)
                .build();
        }

        return builder
            .deathType(BedWarsDeathType.GENERIC)
            .build();
    }

    private static boolean isRangedWeapon(@Nullable Material weapon) {
        if (weapon == null) return false;
        return weapon == Material.BOW
            || weapon == Material.CROSSBOW
            || weapon == Material.TRIDENT;
    }

    public static Text createDeathMessage(@NotNull BedWarsDeathResult result) {
        BedWarsPlayer victim = result.victim();
        Text victimDisplay = colorizeName(victim);

        Text message = switch (result.deathType()) {
            case VOID -> Text.of("<7>{} fell into the void.", victimDisplay);
            case VOID_ASSISTED -> {
                Text assistDisplay = colorizeName(result.assistPlayer());
                yield Text.of("<7>{} was knocked into the void by {}.", victimDisplay, assistDisplay);
            }
            case GENERIC -> Text.of("<7>{} died.", victimDisplay);
            case GENERIC_ASSISTED -> {
                Text killerDisplay = colorizeName(result.getKillCreditPlayer());
                yield Text.of("<7>{} was killed by {}.", victimDisplay, killerDisplay);
            }
            case BOW -> {
                Text killerDisplay = colorizeName(result.getKillCreditPlayer());
                yield Text.of("<7>{} was shot by {}.", victimDisplay, killerDisplay);
            }
            case ENTITY -> {
                Entity entity = result.attackerEntity();
                String entityName = entity != null ? entity.getEntityType().name() : "an entity";
                BedWarsPlayer killer = result.killer();
                Text killerDisplay = colorizeName(killer);
                yield Text.of("<7>{} was slain by {}<7>'s {}.", victimDisplay, killerDisplay, entityName);
            }
        };

        if (result.isFinalKill()) {
            message = message.append(" <b><l>FINAL KILL!");
        }
        return message;
    }

    private static Text colorizeName(@Nullable BedWarsPlayer player) {
        if (player == null) {
            return Text.of("<7>Unknown");
        }

        TeamKey teamKey = player.getTeamKey();
        TextColor color = teamKey != null ? teamKey.chatColor() : NamedTextColor.GRAY;
        return Text.of("<color:{}>{}", color, player.getUsername());
    }
}
