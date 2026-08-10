package net.swofty.type.replayviewer.playback.scoreboard;

import net.minestom.server.entity.Player;
import net.swofty.commons.text.Text;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.util.List;

public interface ReplayScoreboard {

    void create(Player viewer);

    void update(ReplaySession session);

    void remove(Player viewer);

    Text getTitle();

    List<Text> getLines(ReplaySession session);

}
