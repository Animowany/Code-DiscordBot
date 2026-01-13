package eu.ovecode.listener;

import eu.ovecode.OveCode;
import eu.ovecode.util.DiscordUtil;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class SlashCommandInteractionListener extends ListenerAdapter {

    private OveCode oveCode;

    public SlashCommandInteractionListener(OveCode oveCode) {
        this.oveCode = oveCode;
    }

    @Override
    @SneakyThrows
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "create":
                String type = event.getOption("type").getAsString();
                if (type.equalsIgnoreCase("verify")) {
                    InputStream inputStream = getClass().getResourceAsStream("/verify.png");
                    FileUpload fileUpload = FileUpload.fromData(inputStream, "verify.png");
                    Set<Button> buttons = new HashSet<>();
                    buttons.add(Button.secondary("verify", "Zweryfikuj swoje konto discord, klikając ten przycisk."));
                    Message message = event.getChannel().sendMessage(" ").addFiles(fileUpload).setActionRow(buttons).complete();
                    event.reply("Pomyślnie stworzono wiadomość weryfikacji.").setEphemeral(true).complete();
                }
                else if (type.equalsIgnoreCase("ticket")) {
                    InputStream inputStream = getClass().getResourceAsStream("/ticket.png");
                    FileUpload fileUpload = FileUpload.fromData(inputStream, "ticket.png");
                    Set<Button> buttons = new HashSet<>();
                    buttons.add(Button.secondary("ticket", "Stwórz zgłoszenie, klikając ten przycisk."));
                    Message message = event.getChannel().sendMessage(" ").addFiles(fileUpload).setActionRow(buttons).complete();
                    event.reply("Pomyślnie stworzono wiadomość zgłoszeń.").setEphemeral(true).complete();
                }
                break;
            case "embed":
                OptionMapping textOption = event.getOption("text");
                OptionMapping imageOption = event.getOption("image");

                if (textOption != null) {
                    String text = textOption.getAsString();
                    String image = (imageOption != null) ? imageOption.getAsString() : null;

                    if (image != null && isValidURL(image)) {
                        event.getChannel().sendMessageEmbeds(DiscordUtil.messageEmbed(text, image)).complete();
                    } else {
                        event.getChannel().sendMessageEmbeds(DiscordUtil.messageEmbed(text)).complete();
                    }
                    event.reply("Pomyślnie stworzono embed.").setEphemeral(true).complete();
                } else {
                    break;
                }
                break;
            default:
                break;
        }
    }

    private boolean isValidURL(String urlString) {
        Pattern pattern = Pattern.compile("^(https?|ftp)://.*$");
        Matcher matcher = pattern.matcher(urlString);
        return matcher.matches();
    }
}
