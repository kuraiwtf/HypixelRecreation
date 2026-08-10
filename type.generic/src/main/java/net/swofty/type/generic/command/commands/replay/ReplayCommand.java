package net.swofty.type.generic.command.commands.replay;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.replay.ChooseReplayProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.UUID;

@CommandParameters(
	description = "Watch a specific replay by ID",
	usage = "/replay <uuid> [hex]",
	permission = Rank.DEFAULT,
	allowsConsole = false
)
public class ReplayCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		var replayIdArg = ArgumentType.String("replayId");
		var hexArg = ArgumentType.String("hex");

		command.setDefaultExecutor((sender, _) ->
			sender.sendMessage("<c>Usage: /replay \\<uuid> [hex]"));

		command.addSyntax((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender;
			String replayIdStr = context.get(replayIdArg);

			UUID replayId = parseUuid(player, replayIdStr);
			if (replayId == null) return;

			sendToReplayViewer(player, replayId, null);
		}, replayIdArg);

		command.addSyntax((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender;
			String replayIdStr = context.get(replayIdArg);
			String hex = context.get(hexArg);

			UUID replayId = parseUuid(player, replayIdStr);
			if (replayId == null) return;

			String cleanHex = hex.startsWith("#") ? hex : "#" + hex;
			if (cleanHex.length() != 9) {
				player.sendMessage("<c>Invalid share code format.");
				return;
			}

			sendToReplayViewer(player, replayId, cleanHex);
		}, replayIdArg, hexArg);
	}

	private UUID parseUuid(HypixelPlayer player, String uuidStr) {
		try {
			return UUID.fromString(uuidStr);
		} catch (IllegalArgumentException e) {
			player.sendMessage("<c>Invalid replay ID format. Must be a valid UUID.");
			return null;
		}
	}

	private void sendToReplayViewer(HypixelPlayer player, UUID replayId, String shareCode) {
		player.sendMessage("<a>Loading replay...");

		ProxyService replayService = new ProxyService(ServiceType.REPLAY);
		var request = new ChooseReplayProtocolObject.ChooseReplayMessage(player.getUuid(), replayId.toString(), shareCode);
		replayService.<ChooseReplayProtocolObject.ChooseReplayMessage, ChooseReplayProtocolObject.ChooseReplayResponse>handleRequest(request).thenAccept(response -> {
			if (!response.error()) {
				player.sendMessage("<7>Sending you to the Replay Viewer...");
				player.sendTo(ServerType.REPLAY_VIEWER);
			} else {
				player.sendMessage("<c>Replay not found or failed to load.");
			}
		}).exceptionally(e -> {
			player.sendMessage("<c>Failed to load replay: {}", e.getMessage());
			return null;
		});
	}
}
