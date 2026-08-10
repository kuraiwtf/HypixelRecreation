package net.swofty.type.galatea.gui.david;

import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStacks;
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

        layout.slot(11, ItemStacks.head(
                "d61b87f1a1040a8b922ca51be9c0bc6d6fc71ba5d745c6bf659cbd0d9a9cf4fc",
                """
                        "<5>Pocket Black Holes",
                        <7>Obtained from <b>Albert <7>in the Village.
                        
                        <5>Pocket Black Holes <7>are deployables
                        <7>that have the ability to capture
                        <7>Combat Shards when monsters are
                        <7>below <c>10% <7>of their Max Health.
                        
                        <e>Click to view all creatures caught
                        <e>with Black Holes!"""
        ));

        layout.slot(13, ItemStacks.item(
                Material.COBWEB,
                """
                        "<9>Fishing Nets",
                        <7>Obtained from <b>Jaeger <7>and <a>Collections<7>.
                        
                        <7>Many creatures live in the water,
                        <7>and what best than a <b>Fishing Net <7>to
                        <7>capture their Shards!
                        
                        <7>For some creatures you will need to
                        <7>learn their special behaviors to
                        <7>catch them!
                        
                        <e>Click to view all creatures caught
                        <e>with Fishing Nets!"""
        ));

        layout.slot(15, ItemStacks.item(
                Material.LEAD,
                """
                        "<6>Lassos",
                        <7>Obtained from <b>Auryon <7>and <a>Collections<7>.
                        
                        <2>Lassos <7>are particularly useful to
                        <7>hunt many Forest creatures. Once a
                        <7>creature is hooked, they will
                        <7>struggle, make sure you keep aiming
                        <7>straight at them until they lose all
                        <7>their <9>stamina<7>.
                        
                        <e>Click to view all creatures caught
                        <e>with Lassos!"""
        ));

        layout.slot(29, ItemStacks.item(
                Material.GOLDEN_AXE,
                """
                        "<5>Hunting Axes",
                        <7>Obtained from <b>Alan <7>and <a>Collections<7>.
                        
                        <7>Hunting Weapons go hand in hand with
                        <5>Pocket Black Holes<7>, as they allow you
                        <7>to damage monsters without
                        <7>accidentally killing them. Perfect for
                        <7>making sure the creatures will get in
                        <7>range of the <5>Black Holes<7>!
                        
                        <7>These weapons can hold <a>any sword<7>,
                        <7>and will add a portion of their stats
                        <7>to themselves."""
        ));

        layout.slot(31, ItemStacks.item(
                Material.PAPER,
                """
                        "<6>Hunting Traps",
                        <7>Obtained from <b>Alan <7>and <a>Collections<7>.
                        
                        <7>As long as a <c>Combat <7>creature can
                        <7>be hunted, you can place a <6>Hunting
                        <6>Trap <7>right next to their spawn point,
                        <7>and eventually you will trap their
                        <7>Shard. Even if you are not around!
                        <7>However, it can take a long time."""
        ).set(DataComponents.ITEM_MODEL, "hypixel_skyblock:item/island_relevant/foraging_2/traps/small_huntrap"));

        layout.slot(33, ItemStacks.item(
                Material.PAPER,
                """
                        <d>Salts
                        <7>Obtained from harvesting §dBerry
                        <d>Bushes <7>and <a>Collections<7>.
                        
                        <d>Salts <7>are consumables that grant
                        <7>you various effects, such as
                        <7>charming <c>Combat <7>creatures into
                        <7>granting you their Shards when
                        <7>defeated, and more!"""
        ).set(DataComponents.ITEM_MODEL, "hypixel_skyblock:item/island_relevant/foraging_2/salts/lushlilac"));
    }
}