package net.swofty.type.replayviewer.view;

import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.TimedPotion;
import net.swofty.commons.text.Text;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.entity.ReplayPlayerEntity;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GUIViewPlayer implements StatefulView<GUIViewPlayer.State> {

    public record State(ReplayPlayerEntity entity) {
    }

    @Override
    public State initialState() {
        return new State(null);
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withText((state, ctx) -> {
            var sessionOpt = TypeReplayViewerLoader.getSession(ctx.player());
            if (sessionOpt.isEmpty()) {
                return Text.of("Player");
            }

            return state.entity != null ? getDisplayName(state.entity) : Text.of("Player");
        }, InventoryType.CHEST_2_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        var sessionOpt = TypeReplayViewerLoader.getSession(ctx.player());
        if (sessionOpt.isEmpty()) {
            layout.slot(4, ItemStacks.item(Material.BARRIER, 1, """
                    <c>No Replay Session
                    <7>You are not currently watching
                    <7>a replay."""));
            Components.back(layout, 13, ctx);
            return;
        }

        ReplaySession replaySession = sessionOpt.get();
        ReplayPlayerEntity replayPlayer = state.entity;

        if (replayPlayer == null) {
            layout.slot(4, ItemStacks.item(Material.BARRIER, 1, """
                    <c>Player Not Found
                    <7>This player entity is not
                    <7>available at this timestamp."""));
            Components.back(layout, 49, ctx);
            return;
        }

        Text displayName = getDisplayName(replayPlayer);
        List<Text> headLore = List.of(
            Text.of("<7>Health: <f>{}", Math.max(0, Math.round(replayPlayer.getHealth()))),
            Text.empty(),
            Text.of("<e>Right Click for first person!")
        );
        ItemStack.Builder head = replayPlayer.getSkin() != null
            ? ItemStacks.head(replayPlayer.getSkin(), 1, displayName, headLore)
            : ItemStacks.item(Material.PLAYER_HEAD, 1, displayName, headLore);

        layout.slot(0, head, (click, c) -> {
            if (click.click() instanceof Click.Right) {
                replaySession.followEntity(c.player(), state.entity.getInternalId());
                c.player().closeInventory();
                return;
            }

            c.player().teleport(replayPlayer.getPosition());
        });

        layout.slot(1, createEffectsItem(replayPlayer));
        layout.autoUpdating(3, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.MAIN_HAND), "<c>Empty main hand slot."), Duration.ofSeconds(1));
        layout.autoUpdating(5, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.HELMET), "<c>Empty helmet slot."), Duration.ofSeconds(1));
        layout.autoUpdating(6, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.CHESTPLATE), "<c>Empty chestplate slot."), Duration.ofSeconds(1));
        layout.autoUpdating(7, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.LEGGINGS), "<c>Empty leggings slot."), Duration.ofSeconds(1));
        layout.autoUpdating(8, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.BOOTS), "<c>Empty boots slot."), Duration.ofSeconds(1));

        layout.slot(9, ItemStacks.item(Material.ANVIL, 1, """
                <a>Report Player
                <7>Report this player for breaking the
                <7>rules. This replay will be saved
                <7>along with the report to be reviewed.

                <e>Click to report!"""), (_, c) -> c.player().notImplemented());
    }

    private static Text getDisplayName(ReplayPlayerEntity replayPlayer) {
        try {
            return HypixelPlayer.getDisplayName(replayPlayer.getActualUuid());
        } catch (Exception ignored) {
            return Text.of("<7>{}", replayPlayer.getPlayerName());
        }
    }

    private static ItemStack.Builder createEffectsItem(ReplayPlayerEntity replayPlayer) {
        List<TimedPotion> effects = new ArrayList<>(replayPlayer.getActiveEffects());
        if (effects.isEmpty()) {
            return ItemStacks.item(Material.POTION, 1, "<a>Active Status Effects\n<7>No status effects.");
        }

        List<Text> lore = new ArrayList<>();
        for (TimedPotion timedPotion : effects) {
            String effectName = formatEffectName(timedPotion.potion().effect().toString());
            int amplifier = timedPotion.potion().amplifier() + 1;
            lore.add(Text.of("<7>- <a>{} {:roman}", effectName, amplifier));
        }

        return ItemStacks.item(Material.POTION, 1, Text.of("<a>Active Status Effects"), lore);
    }

    private static ItemStack.Builder createEquipmentItem(ItemStack itemStack, String emptyMarkup) {
        if (itemStack == null || itemStack.isAir()) {
            return ItemStacks.item(Material.RED_STAINED_GLASS_PANE, 1, emptyMarkup);
        }
        return itemStack.builder();
    }

    private static String formatEffectName(String raw) {
        String cleaned = raw.toLowerCase(Locale.ROOT)
            .replace("minecraft:", "")
            .replace('_', ' ');

        String[] words = cleaned.split(" ");
        StringBuilder out = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (!out.isEmpty()) {
                out.append(' ');
            }
            out.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                out.append(word.substring(1));
            }
        }
        return out.toString();
    }
}
