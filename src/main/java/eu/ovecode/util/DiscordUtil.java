package eu.ovecode.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.*;
import java.util.Date;

public class DiscordUtil {

    public static MessageEmbed messageEmbed(String description) {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("OVECODE.EU", "https://discord.gg/MYABSpfat4", "https://i.imgur.com/YKzdIJG.png");
        embedBuilder.setColor(new Color(0, 127, 203).getRGB());
        embedBuilder.setDescription(description);
        embedBuilder.setFooter("\uD83C\uDF10");
        embedBuilder.setTimestamp(new Date().toInstant());
        return embedBuilder.build();
    }

    public static MessageEmbed messageEmbed(String description, String image) {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("OVECODE.EU", "https://discord.gg/MYABSpfat4", "https://i.imgur.com/YKzdIJG.png");
        embedBuilder.setColor(new Color(0, 127, 203).getRGB());
        embedBuilder.setDescription(description);
        embedBuilder.setFooter("\uD83C\uDF10");
        embedBuilder.setImage(image);
        embedBuilder.setTimestamp(new Date().toInstant());
        return embedBuilder.build();
    }
}
