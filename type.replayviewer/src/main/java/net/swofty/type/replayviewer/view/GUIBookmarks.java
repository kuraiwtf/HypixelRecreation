package net.swofty.type.replayviewer.view;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.text.Text;
import net.swofty.type.game.replay.recordable.Recordable;
import net.swofty.type.game.replay.recordable.bedwars.RecordableBedDestruction;
import net.swofty.type.game.replay.recordable.bedwars.RecordableKill;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.playback.ReplaySession;
import net.swofty.type.replayviewer.util.ReplayShareUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GUIBookmarks implements StatefulView<GUIBookmarks.State> {

    private static final int[] BOOKMARK_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34
    };

    public record State(int page) {
    }

    private record BookmarkEntry(int tick, Text title, Text playerDisplayName) {
    }

    @Override
    public State initialState() {
        return new State(0);
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Bookmarks", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        var sessionOpt = TypeReplayViewerLoader.getSession(ctx.player());
        if (sessionOpt.isEmpty()) {
            layout.slot(22, ItemStacks.item(Material.BARRIER, 1, """
                    <c>No Replay Session
                    <7>You are not currently watching
                    <7>a replay."""));
            Components.back(layout, 49, ctx);
            return;
        }

        ReplaySession replaySession = sessionOpt.get();
        List<BookmarkEntry> bookmarks = collectBookmarks(replaySession);

        int pageSize = BOOKMARK_SLOTS.length;
        int totalPages = Math.max(1, (bookmarks.size() + pageSize - 1) / pageSize);
        int currentPage = Math.min(state.page(), totalPages - 1);
        int startIndex = currentPage * pageSize;

        for (int i = 0; i < BOOKMARK_SLOTS.length; i++) {
            int slot = BOOKMARK_SLOTS[i];
            int index = startIndex + i;

            if (index >= bookmarks.size()) {
                layout.slot(slot, ItemStack.AIR.builder());
                continue;
            }

            BookmarkEntry entry = bookmarks.get(index);
            List<Text> lore = List.of(
                Text.of("<7>Time: <a>{}", formatBookmarkTime(entry.tick())),
                Text.empty(),
                Text.of("<a>Player: ").append(entry.playerDisplayName()),
                Text.empty(),
                Text.of("<e>Left Click to go to this time!"),
                Text.of("<e>Right Click to share!")
            );
            layout.slot(slot, ItemStacks.item(Material.PAPER, 1, entry.title(), lore), (click, c) -> {
                if (click.click() instanceof Click.Right) {
                    ReplayShareUtil.sendShareCommandMessage(c.player(), replaySession, entry.tick());
                    return;
                }

                replaySession.seekTo(entry.tick());
            });
        }

        if (currentPage > 0) {
            layout.slot(45, ItemStacks.item(Material.ARROW, 1, "<a>Previous Page\n<7>Page <e>{}", currentPage),
                (_, c) -> c.session(State.class).setState(new State(currentPage - 1)));
        }

        if (currentPage < totalPages - 1) {
            layout.slot(53, ItemStacks.item(Material.ARROW, 1, "<a>Next Page\n<7>Page <e>{}", currentPage + 2),
                (_, c) -> c.session(State.class).setState(new State(currentPage + 1)));
        }

        Components.back(layout, 49, ctx);
        if (bookmarks.isEmpty()) {
            layout.slot(22, ItemStacks.item(Material.BARRIER, 1, "<c>No Bookmarks Found\n<7>There are no bookmarks in this match."));
        }
    }

    private static List<BookmarkEntry> collectBookmarks(ReplaySession session) {
        List<BookmarkEntry> entries = new ArrayList<>();

        for (int tick : session.getReplayData().getAllTicks()) {
            List<Recordable> tickRecordables = session.getReplayData().getRecordablesAt(tick);
            for (Recordable recordable : tickRecordables) {
                if (recordable instanceof RecordableBedDestruction bedDestruction) {
                    String fallback = bedDestruction.getDestroyerEntityId() >= 0
                        ? session.getEntityDisplayName(bedDestruction.getDestroyerEntityId())
                        : null;
                    Text playerName = resolveDisplayName(bedDestruction.getDestroyerUuid(), fallback);

                    entries.add(new BookmarkEntry(
                        tick,
                        Text.of("<a>{} Bed Destroyed", getTeamName(bedDestruction.getTeamId())),
                        playerName
                    ));
                    continue;
                }

                if (recordable instanceof RecordableKill kill && kill.getFinalKill() != 0) {
                    String fallback = session.getEntityDisplayName(kill.getVictimEntityId());
                    Text playerName = resolveDisplayName(kill.getVictimUuid(), fallback);

                    entries.add(new BookmarkEntry(
                        tick,
                        Text.of("<a>Final Death"),
                        playerName
                    ));
                }
            }
        }

        return entries;
    }

    private static String formatBookmarkTime(int tick) {
        int totalSeconds = tick / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d seconds", minutes, seconds);
    }

    private static Text resolveDisplayName(UUID uuid, String fallback) {
        if (uuid == null) {
            return fallback != null
                ? (fallback.indexOf('\u00a7') >= 0 ? Text.legacy(fallback) : Text.parse(fallback))
                : Text.of("<7>Unknown");
        }

        try {
            return HypixelPlayer.getDisplayName(uuid);
        } catch (Exception ignored) {
            return fallback != null
                ? (fallback.indexOf('\u00a7') >= 0 ? Text.legacy(fallback) : Text.parse(fallback))
                : Text.of("<7>Unknown");
        }
    }

    private static String getTeamName(byte teamId) {
        return switch (teamId) {
            case 0 -> "Red";
            case 1 -> "Blue";
            case 2 -> "Green";
            case 3 -> "Yellow";
            case 4 -> "Aqua";
            case 5 -> "White";
            case 6 -> "Pink";
            case 7 -> "Gray";
            default -> "Team " + teamId;
        };
    }
}
