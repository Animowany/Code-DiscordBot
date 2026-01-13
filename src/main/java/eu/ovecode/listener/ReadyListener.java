package eu.ovecode.listener;

import eu.ovecode.OveCode;
import eu.ovecode.manager.SchedulerManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class ReadyListener extends ListenerAdapter {

    private final OveCode oveCode;

    public ReadyListener(OveCode oveCode) {
        this.oveCode = oveCode;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        oveCode.getJda().getPresence().setActivity(Activity.listening("nowych zamówień."));

        oveCode.getJda().updateCommands().addCommands(
                Commands.slash("create", "Generuje dedykowane komunikaty, w tym te związane z procesem weryfikacji.").addOptions(new OptionData(OptionType.STRING, "type", "Typ wiadomości")
                        .addChoice("weryfikacja", "verify")
                        .addChoice("zamówienia", "orders")
                        .addChoice("zgłoszenia", "ticket")),
                Commands.slash("embed", "Generuje dedykowane komunikaty, w tym te związane z procesem weryfikacji.").addOptions(new OptionData(OptionType.STRING, "text", "Tekst"), new OptionData(OptionType.STRING, "image", "Obrazek").setRequired(false))
        ).complete();

        SchedulerManager.getInstance().getExecutorService().scheduleAtFixedRate(() -> {

            /* ONLINE */

            List<Member> onlineMembers = oveCode.getJda().getGuilds().get(0).getMembers().stream()
                    .filter(member -> member.getOnlineStatus() == OnlineStatus.ONLINE || member.getOnlineStatus() == OnlineStatus.IDLE || member.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB)
                    .toList();
            //todo: config
            VoiceChannel onlineVoiceChannel = oveCode.getJda().getVoiceChannelById("1195138125698175157");
            onlineVoiceChannel.getManager().setName("\uD83D\uDC66\uD83C\uDFFB・Online - " + (onlineMembers.size() - 1) + " osób").complete();

            /* CZAS */

            LocalTime currentTimeEuropeWarsaw = LocalTime.now(ZoneId.of("Europe/Warsaw"));
            String formattedTime = currentTimeEuropeWarsaw.format(DateTimeFormatter.ofPattern("HH:mm"));
            //todo: config
            VoiceChannel timeVoiceChannel = oveCode.getJda().getVoiceChannelById("1195138196472864818");
            timeVoiceChannel.getManager().setName("⏰・Godzina - " + formattedTime).complete();

        }, 5, 5, TimeUnit.MINUTES);
    }
}
