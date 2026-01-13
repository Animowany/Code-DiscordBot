package eu.ovecode;

import eu.ovecode.config.ConfigManager;
import eu.ovecode.listener.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;

@Getter
@Setter
public class OveCode {

    @Getter
    private static OveCode instance;
    private JDA jda;
    @Getter
    private String jarPath;
    private final ConfigManager configManager;

    @SneakyThrows
    OveCode() {
        instance = this;
        jarPath = new File(OveCode.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath();
        configManager = new ConfigManager(this);
        jda = JDABuilder.create(configManager.getConfig().getToken(), GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES).setStatus(OnlineStatus.DO_NOT_DISTURB).build();
        jda.addEventListener(
                new ReadyListener(this),
                new GuildMemberJoinListener(this),
                new ButtonInteractionListener(this),
                new SlashCommandInteractionListener(this),
                new StringSelectInteractionListener(),
                new ModalInteractionListener(),
                new MessageListener()
        );

    }
}
