package net.swofty.type.bedwarsconfigurator.autosetup;

import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.commons.mc.HypixelPosition;
import net.swofty.commons.text.Text;
import net.swofty.type.generic.utility.EntityUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DebugMarkerManager {

    private static final Map<UUID, List<Entity>> playerMarkers = new HashMap<>();

    public static void showMarkers(UUID playerUuid, AutoSetupSession session, Instance instance) {
        hideMarkers(playerUuid);

        List<Entity> markers = new ArrayList<>();

        for (Map.Entry<TeamKey, AutoSetupSession.TeamConfig> entry : session.getTeams().entrySet()) {
            TeamKey team = entry.getKey();
            AutoSetupSession.TeamConfig config = entry.getValue();
            TextColor teamColor = team.chatColor();

            if (config.getBedFeet() != null) {
                markers.add(createMarker(instance, config.getBedFeet().asHypixelPosition(), Text.of("<color:{}>{} Bed (Feet)", teamColor, team.getName()), Material.RED_BED));
            }
            if (config.getBedHead() != null) {
                markers.add(createMarker(instance, config.getBedHead().asHypixelPosition(), Text.of("<color:{}>{} Bed (Head)", teamColor, team.getName()), Material.RED_BED));
            }

            if (config.getSpawn() != null) {
                HypixelPosition pos = new HypixelPosition(config.getSpawn().x(), config.getSpawn().y(), config.getSpawn().z());
                markers.add(createMarker(instance, pos, Text.of("<color:{}>{} Spawn", teamColor, team.getName()), Material.PLAYER_HEAD));
            }

            if (config.getGenerator() != null) {
                markers.add(createMarker(instance, config.getGenerator(), Text.of("<color:{}>{} Generator", teamColor, team.getName()), Material.IRON_INGOT));
            }

            if (config.getItemShop() != null) {
                HypixelPosition pos = new HypixelPosition(config.getItemShop().x(), config.getItemShop().y(), config.getItemShop().z());
                markers.add(createMarker(instance, pos, Text.of("<color:{}>{} Item Shop", teamColor, team.getName()), Material.EMERALD));
            }
            if (config.getTeamShop() != null) {
                HypixelPosition pos = new HypixelPosition(config.getTeamShop().x(), config.getTeamShop().y(), config.getTeamShop().z());
                markers.add(createMarker(instance, pos, Text.of("<color:{}>{} Team Shop", teamColor, team.getName()), Material.NETHER_STAR));
            }
        }

        int diamondIndex = 1;
        for (HypixelPosition pos : session.getDiamondGenerators()) {
            markers.add(createMarker(instance, pos, Text.of("<b>Diamond Gen #{}", diamondIndex++), Material.DIAMOND_BLOCK));
        }

        int emeraldIndex = 1;
        for (HypixelPosition pos : session.getEmeraldGenerators()) {
            markers.add(createMarker(instance, pos, Text.of("<a>Emerald Gen #{}", emeraldIndex++), Material.EMERALD_BLOCK));
        }

        if (session.getWaitingLocation() != null) {
            HypixelPosition pos = new HypixelPosition(session.getWaitingLocation().x(), session.getWaitingLocation().y(), session.getWaitingLocation().z());
            markers.add(createMarker(instance, pos, Text.of("<e>Waiting Spawn"), Material.CLOCK));
        }

        if (session.getSpectatorLocation() != null) {
            HypixelPosition pos = new HypixelPosition(session.getSpectatorLocation().x(), session.getSpectatorLocation().y(), session.getSpectatorLocation().z());
            markers.add(createMarker(instance, pos, Text.of("<7>Spectator Spawn"), Material.ENDER_EYE));
        }

        if (session.hasBounds()) {
            markers.add(createMarker(instance, new HypixelPosition(session.getMinX(), session.getMinY(), session.getMinZ()), Text.of("<8>Bounds Min"), Material.BARRIER));
            markers.add(createMarker(instance, new HypixelPosition(session.getMaxX(), session.getMaxY(), session.getMaxZ()), Text.of("<8>Bounds Max"), Material.BARRIER));
        }

        playerMarkers.put(playerUuid, markers);
    }

    public static void hideMarkers(UUID playerUuid) {
        List<Entity> markers = playerMarkers.remove(playerUuid);
        if (markers != null) {
            for (Entity marker : markers) {
                marker.remove();
            }
        }
    }

    private static Entity createMarker(Instance instance, HypixelPosition pos, Text label, Material headItem) {
        Entity armorStand = new Entity(EntityType.ARMOR_STAND);

        ArmorStandMeta meta = (ArmorStandMeta) armorStand.getEntityMeta();
        meta.setMarker(true);
        meta.setInvisible(true);
        meta.setHasNoGravity(true);
        meta.setSmall(true);
        meta.setCustomNameVisible(true);

        EntityUtility.nameEntity(armorStand, label);

        armorStand.setInstance(instance, new Pos(pos.x(), pos.y() + 1.5, pos.z()));

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (armorStand.isRemoved()) return TaskSchedule.stop();

            double time = System.currentTimeMillis() / 500.0;
            double yOffset = Math.sin(time) * 0.1;
            Pos currentPos = armorStand.getPosition();
            armorStand.teleport(currentPos.withY(pos.y() + 1.5 + yOffset));

            return TaskSchedule.tick(2);
        }, TaskSchedule.immediate());

        return armorStand;
    }

    public static Entity createSingleMarker(Instance instance, double x, double y, double z, String label) {
        return createMarker(instance, new HypixelPosition(x, y, z), Text.literal(label), Material.ARMOR_STAND);
    }

    public static void refreshMarkers(UUID playerUuid, AutoSetupSession session, Instance instance) {
        if (playerMarkers.containsKey(playerUuid)) {
            showMarkers(playerUuid, session, instance);
        }
    }

    public static boolean areMarkersShown(UUID playerUuid) {
        return playerMarkers.containsKey(playerUuid) && !playerMarkers.get(playerUuid).isEmpty();
    }
}

