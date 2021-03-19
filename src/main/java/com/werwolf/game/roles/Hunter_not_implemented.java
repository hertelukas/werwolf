package com.werwolf.game.roles;

import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Hunter_not_implemented extends Villager {

  //  boolean hasVoted = false;
    long voteMessageID = -1;
    CountDownLatch latch = new CountDownLatch(1);
    Map<String, Player> votes = new HashMap<>();
    Thread waiting = new Thread(() -> {
        long start = System.currentTimeMillis();
        while(!hasVoted) {
            try {
                Thread.sleep(1000);
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
        if (!isSavedByBodyguard()) {
            this.isAlive = false;

            if (game.getTumMode()) sendMessage("https://bit.ly/unexzellent");
            sendMessage("You died.");

            // hunter soll dann voten
            List<Player> alive = game.getController().isNight() ? game.getController().getNightController().getNights().peek().getAlive()
                    : game.getController().getDayController().getDays().peek().getAlive();
            alive.remove(this); // damit hunter nicht sich selbst voten kann (iwie sinnlos)
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
                    votes.put("\uD83c" + (char) (unicodeStart + i), alive.get(i));
                    //Ins Voting hinzufügen
                }
                // message.addReaction("\uD83c" + (char)(unicodeStart + alive.size())).queue(); <-- skip option?
            }));

            // TODO Fix waiting
            //waiting.start();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (game.getConfigurations().isShowRole()) {
                EmbedBuilder showRole = new EmbedBuilder();
                showRole.setTitle(getUsername()).setDescription(getUsername() + UserMessageCreator.getCreator().getMessage(game, "death-Message") + characterType);
                game.getChannel().sendMessage(showRole.build()).queue();
            }
            return true;
        } else
            return false;
    }

    @Override
    public void vote(String prefix, long voteMessageID, Game game) { // fix?
        if (voteMessageID == this.voteMessageID) {
            System.out.println("got msg");
            if(!hasVoted && votes.get(prefix) != null) {
                System.out.println("success");
                if (votes.get(prefix).die(game))
                    if (game.getController().isNight())
                        game.getController().getNightController().getNights().peek().getDiedtonight().add(votes.get(prefix));
                hasVoted = true;
                latch.countDown();
            }
        }
    }
}
