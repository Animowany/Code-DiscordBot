package eu.ovecode.listener;

import eu.ovecode.util.DiscordUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.channel.ChannelManager;

import java.util.Objects;

@SuppressWarnings("all")
public class ModalInteractionListener extends ListenerAdapter {
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().startsWith("ticket")) {
            if (event.getGuild().getTextChannels().stream().anyMatch(e -> {
                if (Objects.nonNull(e.getTopic())) return e.getTopic().equals(event.getUser().getId());
                return false;
            })) {
                event.getInteraction().editMessageEmbeds(DiscordUtil.messageEmbed("Możesz stworzyć maksymalnie 1 ticket.")).setComponents().complete();
                return;
            }
            String categoryId = event.getModalId().split("-")[1];
            TextChannel ticket = event.getGuild().createTextChannel("zamówienie-" + event.getUser().getName(), event.getGuild().getCategoryById(categoryId)).setTopic(event.getMember().getId()).complete();
            ChannelManager ticketManager = ticket.getManager().putPermissionOverride(event.getMember(), 3072L, 8192L).putPermissionOverride(event.getGuild().getRolesByName("@everyone", true).get(0), 0L, 1024L);
            ticketManager.complete();
            Message ping = ticket.sendMessage("@everyone").complete();
            ping.delete().complete();
            ticket.sendMessageEmbeds(
                    DiscordUtil.messageEmbed("**Aby zamknać zgłoszenie nacisnij przycisk.**" +
                            "\n**Zamawiający**:\n" + event.getMember().getAsMention() +
                            "\n**Nazwa projektu:**\n" + event.getInteraction().getValue("projectName").getAsString() +
                            "\n**Budżet na projekt:**\n" + event.getInteraction().getValue("projectBudget").getAsString() +
                            "\n**Opis projektu:**\n" + event.getInteraction().getValue("projectDescription").getAsString()))
                    .setActionRow(Button.secondary("ticketClose", "Zamknij zgłoszenie!")).complete();
            event.getInteraction().editMessageEmbeds(DiscordUtil.messageEmbed("Pomyślnie stworzono zgłoszenie, kanał: <#" + ticket.getId() + ">.")).setComponents().complete();
        }
    }
}
