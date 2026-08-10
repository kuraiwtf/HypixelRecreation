package net.swofty.type.replayviewer.view;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.util.ReplayShareUtil;

public class GUIReplayViewer extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Replay Viewer", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(9, ItemStacks.item(Material.OAK_SIGN, 1, """
                <a>Settings
                <7>Manage your settings for viewing
                <7>replays.

                <e>Click to open!"""), (s, c) -> {
            c.push(new GUIViewerSettings());
        });

        layout.slot(11, ItemStacks.item(Material.BOOK, 1, """
                <a>Bookmarks
                <7>View bookmarks for this recording.

                <e>Click to view!"""), (_, c) -> c.push(new GUIBookmarks()));

        layout.slot(13, ItemStacks.item(Material.PAPER, 1, """
                <a>Share
                <7>Share this replay along with your
                <7>current timestamp and location.

                <e>Click to share!"""), (_, c) -> TypeReplayViewerLoader.getSession(c.player()).ifPresentOrElse(
            session -> ReplayShareUtil.sendShareCommandMessage(c.player(), session),
            () -> c.player().sendMessage("<c>Error: no active replay session.")
        ));

        // for now, this can't be implemented
        layout.slot(15, ItemStacks.item(Material.FILLED_MAP, 1, """
                <a>Submit Highlight
                <7>Did something cool? Share your
                <7>current timestamp and location with
                <7>us for a chance to be showcased on
                <7>Hypixel social media!

                <7>By submitting this highlight, you
                <7>agree to the Hypixel Server
                <7>potentially using this content on the
                <7>Hypixel Twitter, TikTok, Instagram, or
                <7>other social media platform. Make
                <7>sure you are in the right location
                <7>and time to showcase your highlight!

                <e>Click to submit!"""), (_, viewContext) -> viewContext.player().notImplemented());

        layout.slot(17, ItemStacks.item(Material.DARK_OAK_DOOR, 1, """
                <a>Leave Replay
                <e>Click to leave!"""), (_, c) -> {
            TypeReplayViewerLoader.getSession(c.player())
                .ifPresent(session -> session.removeViewer(c.player()));
            c.player().sendTo(ServerType.PROTOTYPE_LOBBY);
        });
    }
}
