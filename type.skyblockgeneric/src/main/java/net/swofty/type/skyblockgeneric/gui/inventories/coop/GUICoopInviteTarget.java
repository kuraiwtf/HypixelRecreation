package net.swofty.type.skyblockgeneric.gui.inventories.coop;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.commons.text.Text;
import net.swofty.type.generic.data.datapoints.DatapointBoolean;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUID;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GUICoopInviteTarget extends HypixelInventoryGUI {
    private static final Map<Integer, List<Integer>> SLOTS_MAP = new HashMap<>(
            Map.of(
                    1, List.of(13),
                    2, List.of(12, 14),
                    3, List.of(11, 13, 15),
                    4, List.of(10, 12, 14, 16),
                    5, List.of(11, 12, 13, 14, 15)
            )
    );

    public GUICoopInviteTarget(CoopDatabase.Coop coop) {
        super(Text.key("gui_coop.target.title"), InventoryType.CHEST_5_ROW);

        fill(ItemStacks.named(Material.BLACK_STAINED_GLASS_PANE, ""));

        int amountInProfile = coop.memberInvites().size() + coop.members().size();
        int[] slots = SLOTS_MAP.get(amountInProfile).stream().mapToInt(Integer::intValue).toArray();

        // Put everyone who is a member as TRUE and ones only invited as FALSE
        Map<UUID, Boolean> invites = new HashMap<>();
        coop.members().forEach(uuid -> invites.put(uuid, true));
        coop.memberInvites().forEach(uuid -> invites.put(uuid, false));

        // Remove originator
        invites.remove(coop.originator());

        set(new GUIItem(slots[0]) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStacks.head(PlayerSkin.fromUuid(String.valueOf(coop.originator())),
                        SkyBlockPlayer.getDisplayName(coop.originator()),
                        Text.keyLines("gui_coop.target.originator_head.lore"));
            }
        });

        for (int i = 0; i < invites.size(); i++) {
            UUID target = (UUID) invites.keySet().toArray()[i];
            boolean accepted = invites.get(target);
            Text displayName = SkyBlockPlayer.getDisplayName(target);

            set(new GUIItem(slots[i + 1]) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    Text status = accepted ? Text.key("gui_coop.sender.accepted_yes") : Text.key("gui_coop.sender.accepted_no");
                    return ItemStacks.head(PlayerSkin.fromUuid(String.valueOf(target)), displayName,
                            List.of(Text.literal(" "), Text.key("gui_coop.sender.player_accepted", status)));
                }
            });
        }

        set(new GUIClickableItem(33) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                coop.removeInvite(player.getUuid());
                coop.save();
                player.sendMessage(Text.key("gui_coop.target.denied_message"));
                player.closeInventory();

                SkyBlockPlayer target = SkyBlockGenericLoader.getLoadedPlayers().stream().filter(player1 -> player1.getUuid().equals(coop.originator())).findFirst().orElse(null);
                if (target != null &&
                        (coop.memberInvites().contains(target.getUuid()) || coop.members().contains(target.getUuid())))
                    target.sendMessage(Text.key("gui_coop.target.denied_notify", player.getUsername()));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStacks.item(Material.BARRIER, 1, "<key:gui_coop.target.deny_button>");
            }
        });

        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                coop.memberInvites().remove(player.getUuid());
                coop.members().add(player.getUuid());
                coop.save();

                UUID profileId = UUID.randomUUID();
                SkyBlockDataHandler handler = SkyBlockDataHandler.initUserWithDefaultData(player.getUuid(), profileId);

                handler.get(SkyBlockDataHandler.Data.IS_COOP, DatapointBoolean.class).setValue(true);
                if (coop.memberProfiles().isEmpty()) {
                    handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(UUID.randomUUID());
                    handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(SkyBlockPlayerProfiles.getRandomName());
                } else {
                    UUID otherCoopMember = coop.memberProfiles().getFirst();
                    ProfilesDatabase islandDatabase = new ProfilesDatabase(otherCoopMember.toString());
                    if (islandDatabase.exists()) {
                        SkyBlockDataHandler islandHandler = SkyBlockDataHandler.createFromProfileOnly(islandDatabase.getDocument());
                        handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(islandHandler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue());
                        handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(islandHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
                        handler.get(SkyBlockDataHandler.Data.PROFILE_MODE, DatapointString.class).setValue(islandHandler.get(SkyBlockDataHandler.Data.PROFILE_MODE, DatapointString.class).getValue());
                    } else {
                        SkyBlockPlayer profileOwner = SkyBlockGenericLoader.getPlayerFromProfileUUID(otherCoopMember);

                        // Access the SkyBlock data handler through the separate cache
                        SkyBlockDataHandler profileOwnerHandler = SkyBlockDataHandler.getUser(profileOwner.getUuid());
                        handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(
                                profileOwnerHandler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue());
                        handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(
                                profileOwnerHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue()
                        );
                        handler.get(SkyBlockDataHandler.Data.PROFILE_MODE, DatapointString.class).setValue(
                                profileOwnerHandler.get(SkyBlockDataHandler.Data.PROFILE_MODE, DatapointString.class).getValue());
                    }
                }

                player.kick(Text.key("gui_coop.target.reconnect_kick"));

                ProfilesDatabase.collection.insertOne(handler.toProfileDocument());
                coop.memberProfiles().add(profileId);
                coop.save();

                MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                    SkyBlockPlayerProfiles profiles = player.getProfiles();
                    profiles.getProfiles().add(profileId);
                    profiles.setCurrentlySelected(profileId);
                    new UserDatabase(player.getUuid()).saveProfiles(profiles);
                }, TaskSchedule.tick(5), TaskSchedule.stop());

                SkyBlockPlayer target = SkyBlockGenericLoader.getLoadedPlayers().stream().filter(player1 -> player1.getUuid().equals(coop.originator())).findFirst().orElse(null);
                if (target != null &&
                        (coop.memberInvites().contains(target.getUuid()) || coop.members().contains(target.getUuid())))
                    target.sendMessage(Text.key("gui_coop.target.accepted_notify", player.getUsername()));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStacks.item(Material.GREEN_TERRACOTTA, 1, Text.key("gui_coop.target.accept_button"),
                        Text.keyLines("gui_coop.target.accept_button.lore", player.getProfiles().getProfiles().size()));
            }
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
