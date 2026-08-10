package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import com.mongodb.client.model.Filters;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.commons.text.Text;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.gui.inventory.ItemStacks;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

public class GUIProfileSelect extends StatelessView {
    private final UUID profileUuid;

    public GUIProfileSelect(UUID profileUuid) {
        this.profileUuid = profileUuid;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.profiles.select.title", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        // Switch to Profile
        layout.slot(11, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            String currentProfile = player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
            String switchingTo;
            try {
                switchingTo = SkyBlockDataHandler.createFromProfileOnly(new ProfilesDatabase(profileUuid.toString()).getDocument())
                        .get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
            } catch (Exception e) {
                switchingTo = "Unknown";
            }

            return ItemStacks.item(Material.GRASS_BLOCK, 1, Text.key("gui_sbmenu.profiles.select.switch"),
                    Text.keyLines("gui_sbmenu.profiles.select.switch.lore", currentProfile, switchingTo));
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockPlayerProfiles profiles = player.getProfiles();
            // Persist the selection before transfer preparation takes its account snapshot.
            profiles.setCurrentlySelected(profileUuid);
            UserDatabase database = new UserDatabase(player.getUuid());
            database.saveProfiles(profiles);

            player.sendTo(ServerType.SKYBLOCK_ISLAND, true);
        });

        // Delete Profile
        layout.slot(15, (s, c) -> ItemStacks.item(Material.RED_STAINED_GLASS, 1, Text.key("gui_sbmenu.profiles.select.delete"),
                        Text.keyLines("gui_sbmenu.profiles.select.delete.lore")),
                (click, c) -> {
                    SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                    if (CoopDatabase.getFromMemberProfile(profileUuid) != null) {
                        player.sendMessage(Text.key("gui_sbmenu.profiles.select.msg.cannot_delete_coop"));
                        player.sendMessage(Text.key("gui_sbmenu.profiles.select.msg.coop_leave"));
                        return;
                    }

                    SkyBlockPlayerProfiles profiles = player.getProfiles();
                    profiles.removeProfile(profileUuid);

                    try {
                        SkyBlockDataHandler handler = SkyBlockDataHandler.createFromProfileOnly(new ProfilesDatabase(profileUuid.toString()).getDocument());
                        player.sendMessage(Text.key("gui_sbmenu.profiles.select.msg.deleted",
                                handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue()));
                    } catch (Exception e) {
                        player.sendMessage(Text.key("gui_sbmenu.profiles.select.msg.deleted_generic"));
                    }

                    ProfilesDatabase.collection.deleteOne(Filters.eq("_id", profileUuid.toString()));
                    player.openView(new GUIProfileManagement());
                });
    }
}
