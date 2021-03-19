package com.werwolf.core.handler.message;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleInstructions extends MessageHandler {

    public HandleInstructions() {
        setName("Instructions");
        setCommand("instructions");
        setDescription("Get detailed game instructions");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if (!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        channel.sendMessage(createInstructions()).queue();

        return true;
    }

    private MessageEmbed createInstructions(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Instructions");
        builder.setDescription("Detailed instructions");

        builder.addField("General", "In the game, a village is haunted by werewolves, or depending on the settings, a serial killer as well. The werewolves want to kill everyone, as does the serial killer. Both have won when they have killed all the others. The serial killer must accordingly kill the werewolves, the werewolves must also kill the serial killer.\n" +
                "The village can always hang a person during the day based on elections. The serial killer and the werewolves can also vote here. Their goal is to mislead the village in the process. However, the village can gather information and track down the villains using a variety of special roles.\n", false);
        builder.addField("In the beginning", "Before the game begins, a mayor is chosen. All players must give their vote as a reaction to the desired player. The mayor has the upper hand in the event of a tie vote during daytime. If the mayor dies, he can pass on his office to any player. In addition, all players are given a role via private message, which is also explained to them.", false);
        builder.addField("During nights", "All roles, except the villager, the little girl, the spy and the hunter, must call an action during the night, they can do this again via a reaction to the new vote message. Self votes are not allowed in the night. After everyone has voted, the roles are executed in the following order:", false);
        builder.addField("1. Jailor", "The jailor locks up a person. The person locked up cannot perform any activity during that night. The imprisoned person can still die", false);
        builder.addField("2. Bodyguard", "The bodyguard protects a person, from werewolves as well as from the serial killer. If the person to be protected is attacked, the bodyguard is notified. However, he does not know from whom.", false);
        builder.addField("3. Prostitute", "The prostitute visits her client, if the prostitute is attacked, nothing happens to her, because she is not at home. However, if her client gets attacked, she dies with him.", false);
        builder.addField("4. Seer", "The seer can choose a player. From this player she gets told if he's a werewolf or a villager. The little girl will identify itself as a werewolf.", false);
        builder.addField("5. Sheriff", "The sheriff can spy on a person. Every night he writes himself a report if the person has behaved in a suspicious way. However, he is always differently certain.", false);
        builder.addField("6. Serial killer", "The serial killer can choose a person he wants to kill. His goal is to kill everyone", false);
        builder.addField("7. Werewolves", "The werewolves can talk in their private channel and choose who they want to kill that night.", false);

        builder.addField("The Little Girl", "The little girl is a normal villager. However, at night she is always among the werewolves, and appears in the Werewolf Channel as a werewolf and can also write there. However, she cannot vote. The seer sees her as a werewolf.", false);
        builder.addField("The Hunter", "The hunter, if he dies, must take another person with him to death. He will be notified privately should this happen and can choose a person per reaction", false);
        builder.addField("The Spy", "The spy gets all the messages from the werewolves sent privately. However, he does not know who sends which message. But maybe he can still gain useful information?", false);
        builder.setThumbnail("https://media.discordapp.net/attachments/821717465679003670/822568803778035732/user-guide-2702483.png?width=679&height=679");

        return builder.build();
    }
}
