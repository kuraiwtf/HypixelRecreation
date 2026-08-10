package net.swofty.type.replayviewer.view;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.text.Text;
import net.swofty.type.generic.data.datapoints.DatapointReplaySettings;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.playback.ReplaySession;
import net.swofty.type.replayviewer.util.ReplaySettingsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GUIViewerSettings extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Viewer Settings", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        DatapointReplaySettings.ReplaySettings settings = ReplaySettingsUtil.getSettings(ctx.player());
        short currentFlySpeed = settings.getFlySpeed();
        short nextFlySpeed = ReplaySettingsUtil.cycleFlySpeed(currentFlySpeed);
        short currentSkip = settings.getSkipIntervals();
        short nextSkip = cycleSkip(currentSkip);

        layout.slot(10, createToggleItem(
            "Chat Messages",
            settings.isChatMessages(),
            "<7>Toggle chat messages such as kills",
            "<7>and other actions in the game."
        ), (_, c) -> updateSetting(c, replaySettings -> replaySettings.setChatMessages(!replaySettings.isChatMessages()), false));

        layout.slot(11, createToggleItem(
            "Chat Timeline",
            settings.isChatTimeline(),
            "<7>Toggle a timeline of the replay which",
            "<7>is displayed via the chat."
        ), (_, c) -> updateSetting(c, replaySettings -> replaySettings.setChatTimeline(!replaySettings.isChatTimeline()), false));

        layout.slot(12, createToggleItem(
            "Show Spectators",
            settings.isShowSpectators(),
            "<7>Toggle the ability to see spectators",
            "<7>from the replay."
        ), (_, c) -> updateSetting(c, replaySettings -> replaySettings.setShowSpectators(!replaySettings.isShowSpectators()), false));

        layout.slot(13, createToggleItem(
            "Night Vision",
            settings.isNightVision(),
            "<7>Toggle having night vision when",
            "<7>watching a replay."
        ), (_, c) -> updateSetting(c, replaySettings -> replaySettings.setNightVision(!replaySettings.isNightVision()), false));

        layout.slot(14, createToggleItem(
            "Show Particles",
            settings.isShowParticles(),
            "<7>Toggle showing particles when",
            "<7>watching a replay."
        ), (_, c) -> updateSetting(c, replaySettings -> replaySettings.setShowParticles(!replaySettings.isShowParticles()), false));

        layout.slot(15, createToggleItem(
            "Advancing Time",
            settings.isAdvanceTime(),
            "<7>Toggle the time on the scoreboard",
            "<7>advancing while the replay is being",
            "<7>played."
        ), (_, c) -> updateSetting(c, replaySettings -> replaySettings.setAdvanceTime(!replaySettings.isAdvanceTime()), false));

        layout.slot(16, ItemStacks.item(Material.PAPER, 1, """
                <a>Fly Speed
                <7>Change the speed you fly at in the
                <7>replay.

                <a>Currently Selected: <6>{}x

                <7>Click to set Fly Speed to <6>{}x.

                <e>Click to cycle!""", currentFlySpeed, nextFlySpeed),
            (_, c) -> updateSetting(c, replaySettings -> replaySettings.setFlySpeed(ReplaySettingsUtil.cycleFlySpeed(replaySettings.getFlySpeed())), false));

        layout.slot(17, ItemStacks.item(Material.PAPER, 1, """
                <a>Skip Intervals
                <7>Change the interval used when using
                <7>the forward & backward buttons. Can
                <7>also be toggled by left clicking the
                <7>buttons themselves.

                <a>Currently Selected: <6>{}s

                <7>Click to set Skip Intervals to <6>{}s.

                <e>Click to cycle!""", currentSkip, nextSkip),
            (_, c) -> updateSetting(c, replaySettings -> replaySettings.setSkipIntervals(cycleSkip(replaySettings.getSkipIntervals())), true));

        Components.back(layout, 49, ctx);
    }

    private static ItemStack.Builder createToggleItem(String title, boolean enabled, String... description) {
        List<Text> lore = new ArrayList<>();
        for (String line : description) {
            lore.add(Text.parse(line));
        }
        lore.add(Text.empty());
        lore.add(enabled ? Text.of("<e>Click to disable!") : Text.of("<e>Click to enable!"));

        Text name = Text.of((enabled ? "<a>" : "<c>") + "{}", title);

        return ItemStacks.item(enabled ? Material.LIME_DYE : Material.GRAY_DYE, 1, name, lore);
    }

    private static void updateSetting(ViewContext ctx,
                                      Consumer<DatapointReplaySettings.ReplaySettings> updater,
                                      boolean refreshReplayHotbar) {
        boolean success = ReplaySettingsUtil.updateSettings(ctx.player(), updater);
        if (!success) {
            ctx.player().sendMessage("<c>Error: failed to update replay settings.");
            return;
        }

        ReplaySettingsUtil.applyVisualSettings(ctx.player());
        if (refreshReplayHotbar) {
            TypeReplayViewerLoader.populateInventory(ctx.player());
        }

        ctx.session(DefaultState.class).refresh();
    }

    private static short cycleSkip(short previous) {
        for (short preset : ReplaySession.SKIP_PRESETS) {
            if (preset > previous) {
                return preset;
            }
        }
        return ReplaySession.SKIP_PRESETS[0];
    }
}
