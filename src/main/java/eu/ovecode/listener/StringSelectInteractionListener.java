package eu.ovecode.listener;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class StringSelectInteractionListener extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equalsIgnoreCase("ticket")) {
            TextInput input1 = TextInput.create("projectName", "Nazwa projektu", TextInputStyle.SHORT)
                    .setPlaceholder("Plugin na enderchest")
                    .setRequiredRange(10, 100)
                    .setRequired(true)
                    .build();
            TextInput input2 = TextInput.create("projectBudget", "Budżet na projekt", TextInputStyle.SHORT)
                    .setPlaceholder("100zł")
                    .setRequiredRange(3, 10)
                    .setRequired(true)
                    .build();
            TextInput input3 = TextInput.create("projectDescription", "Opis projektu", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Chciałbym aby mój plugin na enderchesty posiadał w sobie ulepszanie za lvl.")
                    .setRequiredRange(30, 1000)
                    .setRequired(true)
                    .build();
            Modal modal = Modal.create("ticket-" + event.getInteraction().getSelectedOptions().get(0).getValue(), "Stwórz zgłoszenie")
                    .addComponents(
                            ActionRow.of(input1), ActionRow.of(input2), ActionRow.of(input3)
                    ).build();
            event.replyModal(modal).complete();
        }
    }
}
