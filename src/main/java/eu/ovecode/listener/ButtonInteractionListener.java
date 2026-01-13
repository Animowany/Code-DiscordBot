package eu.ovecode.listener;

import com.mewebstudio.captcha.Captcha;
import com.mewebstudio.captcha.Config;
import com.mewebstudio.captcha.GeneratedCaptcha;
import com.mewebstudio.captcha.util.RandomStringGenerator;
import eu.ovecode.OveCode;
import eu.ovecode.util.DiscordUtil;
import eu.ovecode.util.transcript.DiscordHTMLTranscript;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.*;

@SuppressWarnings("all")
public class ButtonInteractionListener extends ListenerAdapter {

    private OveCode oveCode;
    private HashMap<Long, Long> userTimeMap = new HashMap<>();

    public ButtonInteractionListener(OveCode oveCode) {
        this.oveCode = oveCode;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        long userId = event.getUser().getIdLong();
        long lastTime = userTimeMap.getOrDefault(userId, 0L);
        if (lastTime + 5000 >= System.currentTimeMillis()) {
            long millisecondsLeft = lastTime + 5000 - System.currentTimeMillis();
            event.replyEmbeds(DiscordUtil.messageEmbed("Poczekaj chwilę przed kolejnym kliknięciem. Pozostało " + millisecondsLeft + " milisekund.")).setEphemeral(true).complete();
            return;
        }
        userTimeMap.put(userId, System.currentTimeMillis());

        switch (event.getButton().getId()) {
            case "verify":
                try {
                    Config captchaConfig = new Config();
                    captchaConfig.setDark(true);
                    captchaConfig.setWidth(500);
                    captchaConfig.setHeight(100);
                    captchaConfig.setLength((int) 4 + (int) (Math.random() * 4));
                    captchaConfig.setNoise(50);
                    captchaConfig.setDarkPalette(new Color[] {new Color(0, 144, 255)});
                    GeneratedCaptcha generatedCaptcha = new Captcha(captchaConfig).generate();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(generatedCaptcha.getImage(), "png", byteArrayOutputStream);
                    InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    Set<Button> buttons = new HashSet<>();
                    buttons.add(Button.secondary("verify-success", generatedCaptcha.getCode().toUpperCase()));
                    buttons.add(Button.secondary("verify-error1", new RandomStringGenerator((int) 4 + (int) (Math.random() * 4)).next().toUpperCase()));
                    buttons.add(Button.secondary("verify-error2", new RandomStringGenerator((int) 4 + (int) (Math.random() * 4)).next().toUpperCase()));
                    buttons.add(Button.secondary("verify-error3", new RandomStringGenerator((int) 4 + (int) (Math.random() * 4)).next().toUpperCase()));
                    buttons.add(Button.secondary("verify-error4", new RandomStringGenerator((int) 4 + (int) (Math.random() * 4)).next().toUpperCase()));
                    FileUpload fileUpload = FileUpload.fromData(byteArrayInputStream, "captcha.png");
                    event.replyEmbeds(DiscordUtil.messageEmbed("Witaj **" + event.getUser().getName() + "**!\nAby się zweryfikować wybierz poprawny kod captcha.", "attachment://captcha.png"))
                            .addFiles(fileUpload)
                            .addActionRow(buttons)
                            .setEphemeral(true)
                            .complete();
                    userTimeMap.remove(userId);
                } catch (IOException exception) {

                }
                break;
            case "verify-success":
                Role role = event.getGuild().getRoleById(this.oveCode.getConfigManager().getConfig().getUserRole());
                event.getGuild().addRoleToMember(event.getMember(), role).complete();
                event.deferReply(true).complete();
                break;
            case "verify-error1":
            case "verify-error2":
            case "verify-error3":
            case "verify-error4":
                event.getMember().kick().complete();
                break;

            case "ticket":
                event.replyEmbeds(DiscordUtil.messageEmbed("Wybierz kategorię zgłoszenia która Cię interesuje."))
                    .addActionRow(
                        StringSelectMenu.create("ticket")
                            .addOption("Strony internetowe", "1197239298773237840", "Nowoczesne i funkcjonalne rozwiązania.", Emoji.fromUnicode("\uD83D\uDD14"))
                            .addOption("Hosting stron internetowych", "1197239529430589691", "Niezawodne i szybkie dla Twojej witryny.", Emoji.fromUnicode("\uD83C\uDF10"))
                            .addOption("Projektowanie UI/UX", "1197239343434174484", "Maksymalna funkcjonalność w designie.", Emoji.fromUnicode("\uD83C\uDFA8"))
                            .addOption("Aplikacje Java", "1196917539075264623", "Różnorodne projekty w języku Java.", Emoji.fromUnicode("☕"))
                            .addOption("Identyfikacja wizualna", "1197239428888928347", "Unikatowe projekty graficzne.", Emoji.fromUnicode("\uD83D\uDDBC\uFE0F"))
                            .addOption("Boty discord", "1197239256972791848", "Funkcjonalne i dostosowane do Twoich potrzeb.", Emoji.fromUnicode("\uD83E\uDD16"))
                        .build()
                    ).setEphemeral(true).complete();
                break;
            case "ticketClose":
                try {
                    //todo: config
                    TextChannel textChannelTarget = event.getJDA().getTextChannelById("1196922853568819321");
                    DiscordHTMLTranscript transcript = DiscordHTMLTranscript.getInstance();
                    transcript.createTranscript(event.getMember(), event.getChannel().asTextChannel(), textChannelTarget);
                    event.getChannel().asTextChannel().delete().complete();
                } catch (IOException | ErrorResponseException ignored) {
                    ignored.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
