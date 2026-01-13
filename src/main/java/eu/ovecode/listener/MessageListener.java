package eu.ovecode.listener;

import eu.ovecode.util.DiscordUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class MessageListener extends ListenerAdapter {

    private final Pattern regex = Pattern.compile(".*((http|https)://)((www\\.){0,1})([a-zA-Z0-9@:%._\\+~#?&//=-]" + "{2,256})\\.([a-z]{2,6})\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*).*");
    private final String regexDiscord = "(https?:\\/\\/)?(www\\.)?(discord\\.(gg|io|me|li|com)|discordapp\\.(gg|io|me|li|com)\\/invite)\\/.+[a-z]";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) return;
        if (event.getMessage().getContentRaw().matches(regexDiscord)) {
            deleteMsg(event.getMember(), event.getGuild(), event.getMessage());
            return;
        }
        String[] toCheck = event.getMessage().getContentRaw().split(" ");
        for (String s : toCheck) {
            Matcher m = regex.matcher(event.getMessage().getContentRaw());
            while (m.find()) {
                if (m.group().contains("https://tenor.com") ||
                        m.group().contains("https://youtube.com") ||
                        m.group().contains("https://youtu.be") ||
                        m.group().contains("https://tiktok.com") ||
                        m.group().contains("https://vm.tiktok.com") ||
                        m.group().contains("https://imgur.com") ||
                        m.group().contains("https://i.imgur.com"))
                {
                    continue;
                } else {
                    deleteMsg(event.getMember(), event.getGuild(), event.getMessage());
                    return;
                }
            }
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) return;
        if (!event.getMessage().getContentRaw().matches(regexDiscord) && !regex.matcher(event.getMessage().getContentRaw()).find()) return;
        deleteMsg(event.getMember(), event.getGuild(), event.getMessage());
    }

    private void deleteMsg(Member m, Guild g, Message msg) {
        msg.delete().complete();
        //todo: config
        g.getTextChannelById("1196907716933845163").sendMessageEmbeds(DiscordUtil.messageEmbed("<@!"+m.getId()+"> wyslal link w wiadomosci: "+msg.getContentRaw())).complete();
    }
}
