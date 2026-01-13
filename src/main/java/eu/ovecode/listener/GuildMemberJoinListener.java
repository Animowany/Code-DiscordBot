package eu.ovecode.listener;

import eu.ovecode.OveCode;
import eu.ovecode.util.DiscordUtil;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


@SuppressWarnings("all")
public class GuildMemberJoinListener extends ListenerAdapter {

    private OveCode oveCode;

    public GuildMemberJoinListener(OveCode oveCode) {
        this.oveCode = oveCode;
    }

    @Override
    @SneakyThrows
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        User user = event.getUser();
        String userName = user.getName().toUpperCase();

        if (event.getUser().isBot() && isCorrectName(userName)) {
            return;
        }

        InputStream inputStream = getClass().getResourceAsStream("/welcome.png");
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        Font font = new Font("Arial", Font.BOLD, 25);
        Color color = new Color(0, 100, 255);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        graphics2D.setColor(color);
        graphics2D.setFont(font);
        graphics2D.drawString(userName, bufferedImage.getWidth()/2-(fontMetrics.stringWidth(userName)), 205);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        graphics2D.dispose();
        TextChannel textChannel = oveCode.getJda().getTextChannelById(oveCode.getConfigManager().getConfig().getJoinChannel());
        FileUpload fileUpload = FileUpload.fromData(byteArrayInputStream, "welcome.png");
        textChannel.sendMessage(" ").addFiles(fileUpload).complete();
    }

    private boolean isCorrectName(String nickName){
        return nickName.matches("[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9_]{1,64}");
    }
}
