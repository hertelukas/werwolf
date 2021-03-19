package com.werwolf.game.roles;

import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

public class Hunter_not_implemented extends Villager {

    boolean hasVoted = false;
    long voteMessageID = -1;
    Thread waiting = new Thread(() -> {
        long start = System.currentTimeMillis();
        while(!hasVoted) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("oh no");
            }
        }
        System.out.println("Waited for " + (System.currentTimeMillis() - start)/1000 + " seconds");
    });

    public Hunter_not_implemented(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        characterType = CharacterType.Hunter;
    }

    public boolean die(Game game) {
        if (super.die(game)) {
            sendMessage("You died.");

            // hunter soll dann voten
            List<Player> alive = game.getController().isNight() ? game.getController().getNightController().getNights().peek().getAlive()
                    : game.getController().getDayController().getDays().peek().getAlive();
            StringBuilder playerSb = new StringBuilder();
            EmbedBuilder votingMessageBuilder = new EmbedBuilder();

            char prefix = 'A';

            for (Player p : alive) {
                playerSb.append(prefix++).append(": ").append(p.getUsername()).append("\r");
            }

            // do we need skip?
            // playerSb.append(prefix).append(": ").append("Skip");

            votingMessageBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "hunter-death"))
                    .addField(UserMessageCreator.getCreator().getMessage(game, "living-players"), playerSb.toString(), true);

            user.openPrivateChannel().queue(channel -> channel.sendMessage(votingMessageBuilder.build()).queue(message -> {
                voteMessageID = message.getIdLong();
                int unicodeStart = 0xDDE6;
                for (int i=0; i < alive.size(); i++) {
                    message.addReaction("\uD83c" + (char) (unicodeStart + i)).queue();
                    //Ins Voting hinzufügen
                }
                // message.addReaction("\uD83c" + (char)(unicodeStart + alive.size())).queue(); <-- skip option?
            }));

            waiting.start();
            try {
                waiting.join(); // necessary?
            } catch (InterruptedException e) {
                System.out.println("no good");
            }

            if (game.getConfigurations().isShowRole()) {
                EmbedBuilder showRole = new EmbedBuilder();
                showRole.setTitle(getUsername()).setDescription(getUsername() + UserMessageCreator.getCreator().getMessage(game, "death-Message") + characterType);

                game.getChannel().sendMessage(showRole.build()).queue();
            }
            return true;
        }
        return false;
    }

    public void vote(String prefix, long voteMessageID) { // fix?
        if (voteMessageID == this.voteMessageID) {
            // player.die()
        }
    }
}
