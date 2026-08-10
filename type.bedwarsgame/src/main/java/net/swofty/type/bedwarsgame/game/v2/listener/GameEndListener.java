package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.sound.SoundEventKeys;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.commons.bedwars.BedwarsLevelUtil;
import net.swofty.commons.text.Text;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.stats.BedWarsStatsRecorder;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.GameTeamWinConditionEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.guild.GuildManager;
import org.tinylog.Logger;

import java.util.List;
import java.util.Optional;

public class GameEndListener implements HypixelEventClass {

    private static final Text THICK_BAR = Text.of("<a><l>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onGameEnd(GameTeamWinConditionEvent<BedWarsTeam> event) {
        BedWarsGame game = (BedWarsGame) event.game();
        String gameId = game.getGameId();

        // Show results to all players
        for (BedWarsPlayer player : game.getPlayers()) {
            player.playSound(Sound.sound(SoundEventKeys.UI_TOAST_CHALLENGE_COMPLETE.key(),
                Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());

            // Record win
            event.team().ifPresent(team -> {
                if (team.hasPlayer(player.getUuid())) {
                    if (player.allowsPersistentProgress()) {
                        BedWarsStatsRecorder.recordWin(player, game.getGameType());
                        player.getAchievementHandler().addProgressByTrigger("bedwars.wins", 1);
                        GuildManager.recordProgress(player, 0, true);
                    }
                }
            });

            player.setGameMode(GameMode.ADVENTURE);
            if (player.allowsPersistentProgress()) GuildManager.recordProgress(player, 20, false);
        }

        boolean isRecording = game.getReplayManager().isRecording();
        game.getReplayManager().stopRecording();

        game.getGeneratorManager().stopAllGenerators();
        game.getGameEventManager().stop();
        game.getSwappageManager().stop();
        game.getOneBlockManager().stop();

        Logger.info("Ending game " + gameId);
        game.end();

        for (BedWarsPlayer player : game.getPlayers()) {
            player.sendMessage(THICK_BAR);
            player.sendMessage("<center><f><l>Bed Wars</center>");
            player.sendMessage("");

            event.team().ifPresent(team -> {
                List<Text> playerNames = team.getPlayerIds().stream()
                    .map(game::getPlayer)
                    .flatMap(Optional::stream)
                    .map(p -> Text.of("{}", p.getColouredName()))
                    .toList();
                player.sendMessage("<center><f>{} <7>- {}</center>",
                    Text.of("<color:{}>{}", team.getColor(), team.getName()),
                    Text.join(Text.of("<7>,"), playerNames));
                player.sendMessage("");
            });

            player.sendMessage("<center><e><l>1st Killer </l><7>- Username - 0</center>");
            player.sendMessage("<center><6><l>2nd Killer </l><7>- Username - 0</center>");
            player.sendMessage("<center><c><l>3rd Killer </l><7>- Username - 0</center>");
            player.sendMessage("");
            player.sendMessage(THICK_BAR);
        }

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (BedWarsPlayer player : game.getPlayers()) {
                player.sendMessage(THICK_BAR);
                player.sendMessage("<center><f><l>Reward Summary</center>");
                player.sendMessage("");
                player.sendMessage("   <7>You earned:");
                player.sendMessage("    <f>• <2>{} Bed Wars Tokens", player.getTokensThisGame());
                player.sendMessage("    <f>• <3>{} Hypixel Experience", player.getHypixelXpThisGame());
                player.sendMessage("    <f>• <2>0 Guild Experience");
                player.sendMessage("");
                player.sendMessage("<center><b>Bed Wars XP</center>");

                long currentLevel = player.getCurrentBedWarsLevel();
                player.sendMessage("<f>          <b>Level {}                                     Level {}",
                    currentLevel, currentLevel + 1);

                long experience = player.getCurrentBedWarsExperience();
                int progress = BedwarsLevelUtil.calculateExperienceSinceLastLevel(experience);
                int maxExperience = BedwarsLevelUtil.calculateMaxExperienceFromExperience(experience);

                double percentage = Math.min(1.0, (double) progress / maxExperience);
                int filledSquares = (int) Math.round(percentage * 34);
                player.sendMessage("<f>          <8>[<b>{}<7>{}<8>]",
                    "■".repeat(filledSquares), "■".repeat(34 - filledSquares));

                player.sendMessage("<center><b>{:,} <7>/ <a>{:,} <7>({}%)</center>",
                    experience, maxExperience, String.format("%.1f", percentage * 100));

                player.sendMessage("");
                player.sendMessage("<7>You earned <b>{} Bed Wars XP", player.getXpThisGame());
                player.sendMessage("");
                // xp multipliers shown here
                player.sendMessage(THICK_BAR);
                if (isRecording) {
                    player.sendMessage("<click:run:'/replay {}'><a>This game has been recorded. <6>Click here to watch the Replay!",
                        game.getReplayManager().getRecorder().getReplayId());
                }
                player.sendMessage("");
            }
        }).delay(TaskSchedule.seconds(2)).schedule();

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            game.getPlayers().forEach(p -> p.sendTo(ServerType.BEDWARS_LOBBY));
            game.dispose();
        }).delay(TaskSchedule.seconds(7)).schedule();
    }

}
