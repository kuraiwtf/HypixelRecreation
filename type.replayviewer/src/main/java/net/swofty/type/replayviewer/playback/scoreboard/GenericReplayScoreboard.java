package net.swofty.type.replayviewer.playback.scoreboard;

import net.minestom.server.entity.Player;
import net.swofty.commons.text.Text;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericReplayScoreboard implements ReplayScoreboard {

    private final HypixelScoreboard scoreboard = new HypixelScoreboard();
    private final ReplaySession session;
    private Player viewer;

    public GenericReplayScoreboard(ReplaySession session) {
        this.session = session;
    }

    @Override
    public void create(Player viewer) {
        this.viewer = viewer;
        scoreboard.createScoreboard(viewer, getTitle());
        update(session);
    }

    @Override
    public void update(ReplaySession session) {
        if (viewer == null) return;

        scoreboard.updateLines(viewer, getLines(session));
    }

    @Override
    public void remove(Player viewer) {
        scoreboard.removeScoreboard(viewer);
        this.viewer = null;
    }

    @Override
    public Text getTitle() {
        String gameType = session.getMetadata().getGameTypeName();
        if (gameType == null || gameType.isEmpty()) {
            return Text.of("<e><l>REPLAY");
        }
        return Text.of("<e><l>{}", gameType.toUpperCase());
    }

    @Override
    public List<Text> getLines(ReplaySession session) {
        List<Text> lines = new ArrayList<>();
        lines.add(Text.of("<7>{}  <8>{}",
            new SimpleDateFormat("MM/dd/yyyy").format(new Date()), HypixelConst.getServerName()));
        lines.add(Text.of("<7>Replay from {}", session.getMetadata().getServerId()));
        lines.add(Text.of("<7> "));

        lines.add(Text.of("<f>Date: <a>{}",
            new SimpleDateFormat("MM/dd/yyyy").format(new Date(session.getMetadata().getStartTime()))));
        lines.add(Text.of("<f>Time: <a>{} (EST)",
            new SimpleDateFormat("HH:mm").format(new Date(session.getMetadata().getStartTime()))));
        lines.add(Text.of("<7> "));

        String gameType = session.getMetadata().getGameTypeName();
        if (gameType != null && !gameType.isEmpty()) {
            gameType = gameType.substring(0, 1).toUpperCase() + gameType.substring(1).toLowerCase();
        } else {
            gameType = "Unknown";
        }

        lines.add(Text.of("<f>Game: <a>BedWars"));
        lines.add(Text.of("<f>Mode: <a>{}", gameType));
        lines.add(Text.of("<7> "));

        String mapName = session.getMetadata().getMapName();
        if (mapName != null && !mapName.isEmpty()) {
            lines.add(Text.of("<f>Map: <a>{}", mapName));
        }
        lines.add(Text.of("<e>www.hypixel.net"));

        return lines;
    }

}
