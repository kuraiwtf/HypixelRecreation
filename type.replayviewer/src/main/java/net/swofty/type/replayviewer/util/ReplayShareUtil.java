package net.swofty.type.replayviewer.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.playback.ReplaySession;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReplayShareUtil {

    public static String buildShareCommand(ReplaySession session, Pos position, int tick) {
        int maxTick = Math.max(0, session.getTotalTicks() - 1);
        int clampedTick = Math.max(0, Math.min(tick, maxTick));

        String shareCode = ReplayShareCodec.encode(
            position,
            clampedTick,
            session.getMetadata().getMapCenterX(),
            session.getMetadata().getMapCenterZ()
        );

        return "/replay " + session.getReplayId() + " " + shareCode;
    }

    public static void sendShareCommandMessage(HypixelPlayer player, ReplaySession session) {
        sendShareCommandMessage(player, session, session.getCurrentTick(), player.getPosition());
    }

    public static void sendShareCommandMessage(HypixelPlayer player, ReplaySession session, int tick) {
        sendShareCommandMessage(player, session, tick, player.getPosition());
    }

    public static void sendShareCommandMessage(HypixelPlayer player, ReplaySession session, int tick, Pos position) {
        String fullCommand = buildShareCommand(session, position, tick);

        player.sendMessage(
            "<hover:'<e>Click to copy command to chat'><click:suggest:'{}'><6><l>Click here to put share command in chat!</click></hover>",
            fullCommand
        );
    }
}