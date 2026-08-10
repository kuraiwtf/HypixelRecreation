package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.format.TextColor;
import net.swofty.commons.text.Text;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.replay.BedWarsReplayManager;
import net.swofty.type.bedwarsgame.stats.BedWarsStatsRecorder;
import net.swofty.type.game.game.event.TeamEliminatedEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.Optional;

public class TeamEliminationListener implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(TeamEliminatedEvent<BedWarsTeam> event) {
        BedWarsTeam team = event.team();
        TextColor teamColor = team.getColor();
        String teamName = team.getName();

        BedWarsGame game = (BedWarsGame) event.game();
        team.getPlayerIds().stream()
            .map(game::getPlayer)
            .flatMap(Optional::stream)
            .forEach(player -> {
                BedWarsStatsRecorder.recordBedLost(player, game.getGameType());
                BedWarsStatsRecorder.recordLoss(player, game.getGameType());
            });

        game.broadcastMessage(Text.empty());
        game.broadcastMessage(Text.of("<f><l>TEAM ELIMINATED > </l>{} <c>has been eliminated!",
            Text.of("<color:{}>{}", teamColor, teamName)));
        game.broadcastMessage(Text.empty());

        BedWarsReplayManager replayManager = game.getReplayManager();

        // Record to replay
        if (replayManager.isRecording()) {
            replayManager.recordTeamElimination(team.getTeamKey());
        }
    }
}
