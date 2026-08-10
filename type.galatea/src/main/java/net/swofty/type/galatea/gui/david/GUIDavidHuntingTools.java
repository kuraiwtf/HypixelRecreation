package net.swofty.type.galatea.gui.david;

import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIDavidHuntingTools extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Hunting Tools", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(final ViewLayout<DefaultState> layout, final DefaultState state, final ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 48, ctx);
        Components.close(layout, 49);

        layout.slot(11, ItemStackCreator.getStackHead(
                "§5Pocket Black Holes",
                "d61b87f1a1040a8b922ca51be9c0bc6d6fc71ba5d745c6bf659cbd0d9a9cf4fc",
                1,
                "§7Obtained from §bAlbert §7in the Village.",
                "",
                "§5Pocket Black Holes §7are deployables",
                "§7that have the ability to capture",
                "§7Combat Shards when monsters are",
                "§7below §c10% §7of their Max Health.",
                "",
                "§eClick to view all creatures caught",
                "§ewith Black Holes!"
        ));

        layout.slot(13, ItemStackCreator.getStack(
                "§9Fishing Nets",
                Material.COBWEB,
                1,
                "§7Obtained from §bJaeger §7and §aCollections§7.",
                "",
                "§7Many creatures live in the water,",
                "§7and what best than a §bFishing Net §7to",
                "§7capture their Shards!",
                "",
                "§7For some creatures you will need to",
                "§7learn their special behaviors to",
                "§7catch them!",
                "",
                "§eClick to view all creatures caught",
                "§ewith Fishing Nets!"
        ));

        layout.slot(15, ItemStackCreator.getStack(
                "§6Lassos",
                Material.LEAD,
                1,
                "§7Obtained from §bAuryon §7and §aCollections§7.",
                "",
                "§2Lassos §7are particularly useful to",
                "§7hunt many Forest creatures. Once a",
                "§7creature is hooked, they will",
                "§7struggle, make sure you keep aiming",
                "§7straight at them until they lose all",
                "§7their §9stamina§7.",
                "",
                "§eClick to view all creatures caught",
                "§ewith Lassos!"
        ));

        layout.slot(29, ItemStackCreator.getStack(
                "§5Hunting Axes",
                Material.GOLDEN_AXE,
                1,
                "§7Obtained from §bAlan §7and §aCollections§7.",
                "",
                "§7Hunting Weapons go hand in hand with",
                "§5Pocket Black Holes§7, as they allow you",
                "§7to damage monsters without",
                "§7accidentally killing them. Perfect for",
                "§7making sure the creatures will get in",
                "§7range of the §5Black Holes§7!",
                "",
                "§7These weapons can hold §aany sword§7,",
                "§7and will add a portion of their stats",
                "§7to themselves."
        ));

        layout.slot(31, ItemStackCreator.getStack(
                "§6Hunting Traps",
                Material.PAPER,
                1,
                "§7Obtained from §bAlan §7and §aCollections§7.",
                "",
                "§7As long as a §cCombat §7creature can",
                "§7be hunted, you can place a §6Hunting",
                "§6Trap §7right next to their spawn point,",
                "§7and eventually you will trap their",
                "§7Shard. Even if you are not around!",
                "§7However, it can take a long time."
        ).set(DataComponents.ITEM_MODEL, "hypixel_skyblock:item/island_relevant/foraging_2/traps/small_huntrap"));

        layout.slot(33, ItemStackCreator.getStack(
                "§dSalts",
                Material.PAPER,
                1,
                "§7Obtained from harvesting §dBerry",
                "§dBushes §7and §aCollections§7.",
                "",
                "§dSalts §7are consumables that grant",
                "§7you various effects, such as",
                "§7charming §cCombat §7creatures into",
                "§7granting you their Shards when",
                "§7defeated, and more!"
        ).set(DataComponents.ITEM_MODEL, "hypixel_skyblock:item/island_relevant/foraging_2/salts/lushlilac"));
    }
}
