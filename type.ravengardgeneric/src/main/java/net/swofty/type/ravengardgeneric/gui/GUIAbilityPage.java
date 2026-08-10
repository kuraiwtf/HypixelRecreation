package net.swofty.type.ravengardgeneric.gui;

import net.swofty.commons.text.Text;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.ravengardgeneric.classes.RavengardAbility;
import net.swofty.type.ravengardgeneric.classes.RavengardClass;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.List;

/**
 * The per-slot ability page, rebuilt from a capture: the class's seven abilities laid out down
 * the panel, each marked selected or locked, with the hover offsets following the slot grid.
 */
public class GUIAbilityPage extends RavengardView {
    private static final int PANEL_ICON = 0xE23D;
    private static final int[] ABILITY_SLOTS = {4, 11, 15, 22, 29, 33, 40};

    private final int abilitySlot;

    public GUIAbilityPage(int abilitySlot) {
        this.abilitySlot = abilitySlot;
    }

    @Override
    protected String title() {
        return abilitySlot == 1 ? "Ability One" : "Ability Two";
    }

    @Override
    protected int panelIcon() {
        return PANEL_ICON;
    }

    @Override
    protected void content(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        if (!(ctx.player() instanceof RavengardPlayer player)) {
            return;
        }
        RavengardClass playerClass = player.getRavengardClass();
        if (playerClass == null) {
            return;
        }

        List<RavengardAbility> abilities = playerClass.getAbilities();
        List<RavengardAbility> equipped = playerClass.defaultAbilities();

        for (int index = 0; index < ABILITY_SLOTS.length && index < abilities.size(); index++) {
            RavengardAbility ability = abilities.get(index);
            int equippedSlot = equipped.indexOf(ability) + 1;

            RavengardItems.Builder button = RavengardItems.button(ability)
                    .label(Text.of("Ability {} - {}", index + 1, ability.getDisplayName()))
                    .lore(ability.getWrappedDescription())
                    .blankLine()
                    .lore(Text.of("<7>Cooldown: <e>{}", ability.getCooldownText()))
                    .blankLine();

            if (equippedSlot > 0) {
                button.lore("<a><l>SELECTED")
                        .lore(Text.of(" <7>This ability is already in <e>slot {}<7>!",
                                equippedSlot == 1 ? "one" : "two"));
            } else {
                button.lore("<c><l>LOCKED",
                        " <7>Purchase this skill for <e>1<7> Ability Points to use it.");
            }

            place(layout, ABILITY_SLOTS[index], button);
        }

        backButton(layout);
    }
}
