package com.davinci;

import com.davinci.Games.Games;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Executer extends ListenerAdapter {

    public static JDA api;
    BotCommands botcmd = new BotCommands();

    public static void main(String[] args) {
        try
        {
            JDA jda = JDABuilder.createDefault(Configs.TOKEN) // The token of the account that is logging in.
                    .addEventListeners(new Executer())   // An instance of a class that will handle events.
                    .build();
            jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            System.out.println("Finished Building JDA!");
        }
        catch (LoginException e)
        {
            //If anything goes wrong in terms of authentication, this is the exception that will represent it
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            //Due to the fact that awaitReady is a blocking method, one which waits until JDA is fully loaded,
            // the waiting can be interrupted. This is the exception that would fire in that situation.
            //As a note: in this extremely simplified example this will never occur. In fact, this will never occur unless
            // you use awaitReady in a thread that has the possibility of being interrupted (async thread usage and interrupts)
            e.printStackTrace();
        }
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        //These are provided with every event in JDA
        JDA jda = event.getJDA();                       //JDA, the core of the api.
        long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

        //Event specific information
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
        //  This could be a TextChannel, PrivateChannel, or Group!

        String msg = message.getContentDisplay();              //This returns a human readable version of the Message. Similar to
        // what you would see in the client.

        boolean bot = author.isBot();                    //This boolean is useful to determine if the User that
        // sent the Message is a BOT or not!

        if (event.isFromType(ChannelType.TEXT))         //If this message was sent to a Guild TextChannel
        {
            //Because we now know that this message was sent in a Guild, we can do guild specific things
            // Note, if you don't check the ChannelType before using these methods, they might return null due
            // the message possibly not being from a Guild!

            Guild guild = event.getGuild();             //The Guild that this message was sent in. (note, in the API, Guilds are Servers)
            TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
            Member member = event.getMember();          //This Member that sent the message. Contains Guild specific information about the User!

            String name;
            if (message.isWebhookMessage())
            {
                name = author.getName();                //If this is a Webhook message, then there is no Member associated
            }                                           // with the User, thus we default to the author for name.
            else
            {
                name = member.getEffectiveName();       //This will either use the Member's nickname if they have one,
            }                                           // otherwise it will default to their username. (User#getName())

            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        }
        else if (event.isFromType(ChannelType.PRIVATE)) //If this message was sent to a PrivateChannel
        {
            //The message was sent in a PrivateChannel.
            //In this example we don't directly use the privateChannel, however, be sure, there are uses for it!
            PrivateChannel privateChannel = event.getPrivateChannel();

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }

        //Now that you have a grasp on the things that you might see in an event, specifically MessageReceivedEvent,
        // we will look at sending / responding to messages!
        //This will be an extremely simplified example of command processing.

        //Remember, in all of these .equals checks it is actually comparing
        // message.getContentDisplay().equals, which is comparing a string to a string.
        // If you did message.equals() it will fail because you would be comparing a Message to a String!
        if (msg.equals("!ping"))
        {
            //This will send a message, "pong!", by constructing a RestAction and "queueing" the action with the Requester.
            // By calling queue(), we send the Request to the Requester which will send it to discord. Using queue() or any
            // of its different forms will handle ratelimiting for you automatically!

            channel.sendMessage("pong!").queue();
        }
        else if (msg.equals("!roll"))
        {
            //In this case, we have an example showing how to use the flatMap operator for a RestAction. The operator
            // will provide you with the object that results after you execute your RestAction. As a note, not all RestActions
            // have object returns and will instead have Void returns. You can still use the flatMap operator to run chain another RestAction!

            Random rand = ThreadLocalRandom.current();
            int roll = rand.nextInt(6) + 1; //This results in 1 - 6 (instead of 0 - 5)
            channel.sendMessage("Your roll: " + roll)
                    .flatMap(
                            (v) -> roll < 3, // This is called a lambda expression. If you don't know what they are or how they work, try google!
                            // Send another message if the roll was bad (less than 3)
                            sentMessage -> channel.sendMessage("The roll for messageId: " + sentMessage.getId() + " wasn't very good... Must be bad luck!\n")
                    )
                    .queue();
        }
        else if (msg.startsWith("!kick"))   //Note, I used "startsWith, not equals.
        {
            //This is an admin command. That means that it requires specific permissions to use it, in this case
            // it needs Permission.KICK_MEMBERS. We will have a check before we attempt to kick members to see
            // if the logged in account actually has the permission, but considering something could change after our
            // check we should also take into account the possibility that we don't have permission anymore, thus Discord
            // response with a permission failure!
            //We will use the error consumer, the second parameter in queue!

            //We only want to deal with message sent in a Guild.
            if (message.isFromType(ChannelType.TEXT))
            {
                //If no users are provided, we can't kick anyone!
                if (message.getMentionedUsers().isEmpty())
                {
                    channel.sendMessage("You must mention 1 or more Users to be kicked!").queue();
                }
                else
                {
                    Guild guild = event.getGuild();
                    Member selfMember = guild.getSelfMember();  //This is the currently logged in account's Member object.
                    // Very similar to JDA#getSelfUser()!

                    //Now, we the the logged in account doesn't have permission to kick members.. well.. we can't kick!
                    if (!selfMember.hasPermission(Permission.KICK_MEMBERS))
                    {
                        channel.sendMessage("Sorry! I don't have permission to kick members in this Guild!").queue();
                        return; //We jump out of the method instead of using cascading if/else
                    }

                    //Loop over all mentioned users, kicking them one at a time. Mwauahahah!
                    List<User> mentionedUsers = message.getMentionedUsers();
                    for (User user : mentionedUsers)
                    {
                        Member member = guild.getMember(user);  //We get the member object for each mentioned user to kick them!

                        //We need to make sure that we can interact with them. Interacting with a Member means you are higher
                        // in the Role hierarchy than they are. Remember, NO ONE is above the Guild's Owner. (Guild#getOwner())
                        if (!selfMember.canInteract(member))
                        {
                            // use the MessageAction to construct the content in StringBuilder syntax using append calls
                            channel.sendMessage("Cannot kick member: ")
                                    .append(member.getEffectiveName())
                                    .append(", they are higher in the hierarchy than I am!")
                                    .queue();
                            continue;   //Continue to the next mentioned user to be kicked.
                        }

                        //Remember, due to the fact that we're using queue we will never have to deal with RateLimits.
                        // JDA will do it all for you so long as you are using queue!
                        guild.kick(member).queue(
                                success -> channel.sendMessage("Kicked ").append(member.getEffectiveName()).append("! Cya!").queue(),
                                error ->
                                {
                                    //The failure consumer provides a throwable. In this case we want to check for a PermissionException.
                                    if (error instanceof PermissionException)
                                    {
                                        PermissionException pe = (PermissionException) error;
                                        Permission missingPermission = pe.getPermission();  //If you want to know exactly what permission is missing, this is how.
                                        //Note: some PermissionExceptions have no permission provided, only an error message!

                                        channel.sendMessage("PermissionError kicking [")
                                                .append(member.getEffectiveName()).append("]: ")
                                                .append(error.getMessage()).queue();
                                    }
                                    else
                                    {
                                        channel.sendMessage("Unknown error while kicking [")
                                                .append(member.getEffectiveName())
                                                .append("]: <").append(error.getClass().getSimpleName()).append(">: ")
                                                .append(error.getMessage()).queue();
                                    }
                                });
                    }
                }
            }
            else
            {
                channel.sendMessage("This is a Guild-Only command!").queue();
            }
        }
        else if (msg.equals("!block"))
        {
            //This is an example of how to use the complete() method on RestAction. The complete method acts similarly to how
            // JDABuilder's awaitReady() works, it waits until the request has been sent before continuing execution.
            //Most developers probably wont need this and can just use queue. If you use complete, JDA will still handle ratelimit
            // control, however if shouldQueue is false it won't queue the Request to be sent after the ratelimit retry after time is past. It
            // will instead fire a RateLimitException!
            //One of the major advantages of complete() is that it returns the object that queue's success consumer would have,
            // but it does it in the same execution context as when the request was made. This may be important for most developers,
            // but, honestly, queue is most likely what developers will want to use as it is faster.

            try
            {
                //Note the fact that complete returns the Message object!
                //The complete() overload queues the Message for execution and will return when the message was sent
                //It does handle rate limits automatically
                Message sentMessage = channel.sendMessage("I blocked and will return the message!").complete();
                //This should only be used if you are expecting to handle rate limits yourself
                //The completion will not succeed if a rate limit is breached and throw a RateLimitException
                Message sentRatelimitMessage = channel.sendMessage("I expect rate limitation and know how to handle it!").complete(false);

                System.out.println("Sent a message using blocking! Luckly I didn't get Ratelimited... MessageId: " + sentMessage.getId());
            }
            catch (RateLimitedException e)
            {
                System.out.println("Whoops! Got ratelimited when attempting to use a .complete() on a RestAction! RetryAfter: " + e.getRetryAfter());
            }
            //Note that RateLimitException is the only checked-exception thrown by .complete()
            catch (RuntimeException e)
            {
                System.out.println("Unfortunately something went wrong when we tried to send the Message and .complete() threw an Exception.");
                e.printStackTrace();
            }
        } else if (msg.startsWith("!whois")) {
            String[] whoIsData = msg.split(" ", 2);
            if (whoIsData.length < 2) {
                channel.sendMessage("Usage !whois <Discord Nick>").queue();
            } else {
                System.err.println(whoIsData[1]);
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (!botcmd.notFound(memberche.getId())) {
                        botcmd.whoAmIinfos(memberche, channel);
                    }
                } catch (IndexOutOfBoundsException e) {
                        channel.sendMessage("User not found!").queue();
                }
            }
        } else if (msg.startsWith("!stats")) {
            String[] data = msg.split(" ", 2);
            if (data.length < 2) {
                botcmd.showStats(author, channel);
            } else {
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    botcmd.showStats(memberche, channel);
                } catch (IndexOutOfBoundsException ex) {
                    channel.sendMessage("User not found!").queue();
                }
            }
        } else if (msg.equalsIgnoreCase("!checkme")) {
            channel.sendMessage("This is the name?!: " + author.getName()).queue();
            channel.sendMessage("This should be the ID from Discord DB directly: " + author.getId()).queue();
        } else if (msg.equalsIgnoreCase("!whoami")) {
            List<User> pedal = jda.getUsersByName(author.getName(), true);
            for (User usr : pedal) {
                if (pedal.contains(usr)) {
                    if (botcmd.ifVouched(usr)) {
                        botcmd.whoAmIinfos(usr, channel);
                    }
                }
            }
        } else if (msg.startsWith("!test")) {

            String[] data = msg.split(" ", 2);
            User memberche;
            List<Member> usrs = event.getGuild().getMembers();
            List<User> pedal = jda.getUsersByName(data[1].toString(), true);

            if (data.length < 2) {
                channel.sendMessage("sme se usrali a :D");
            } else {
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    channel.sendMessage(botcmd.returnID(memberche)).queue();
                } catch (IndexOutOfBoundsException ex) {
                    channel.sendMessage("Opaaa").queue();
                }

            }
        } else if (msg.startsWith("!vouch")) {
            String[] whoIsData = msg.split(" ", 3);
            if (whoIsData.length < 2) {
                channel.sendMessage("Usage !vouch <Discord Nick> comment").queue();
            } else {
                System.err.println(whoIsData[1]);
                List<User> pedal = jda.getUsersByName(whoIsData[1].toString(), true);
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (botcmd.notFound(memberche.getId())) {
                        botcmd.vouchUser(memberche, author, whoIsData[2], channel);
                        event.getGuild().addRoleToMember(memberche.getId(), jda.getRoleById("796996100392222750")).queue();
                    } else {
                        channel.sendMessage("User not found!").queue();
                    }
                } catch (IndexOutOfBoundsException ex) {
                        channel.sendMessage("You need to @mention the user").queue();
                }
            }
        } else if (msg.startsWith("!unvouch")) {
            String[] whoIsData = msg.split(" ", 3);
            if (whoIsData.length < 2) {
                channel.sendMessage("Usage !unvouch <Discord Nick> comment").queue();
            } else {
                System.err.println(whoIsData[1]);
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (!botcmd.notFound(memberche.getId())) {
                        botcmd.unvouch(author, memberche, whoIsData[2], channel);
                        event.getGuild().removeRoleFromMember(memberche.getId(), jda.getRoleById("796996100392222750")).queue();
                    } else {
                        channel.sendMessage("User not found!").queue();
                    }
                } catch (IndexOutOfBoundsException ex) {
                        channel.sendMessage("You need to @mention the user").queue();
                }

            }
        } else if (msg.startsWith("!steam")) {
            String[] accCmd = msg.split(" ", 2);
            if (accCmd.length < 2) {
                channel.sendMessage(botcmd.ShowAccount(author)).queue();
            } else {
                botcmd.acc(author, accCmd[1], channel);
            }
        } else if (msg.equalsIgnoreCase("!sign")) {
            botcmd.sign(author, channel);
        } else if (msg.equalsIgnoreCase("!lp")) {
            botcmd.lp(author, channel);
        } else if (msg.startsWith("!startgame") || msg.startsWith("!sg")) {
            String[] unvouchCmd = msg.split(" ", 2);
            if (unvouchCmd.length < 2) {
                Games.getGclass().startGame(author, 2, channel);
            } else {
                Games.getGclass().startGame(author, Integer.parseInt(unvouchCmd[1]), channel);
            }
        } else if (msg.startsWith("!challenge") || msg.startsWith("!cl")) {
            String[] unvouchCmd = msg.split(" ", 2);
            User memberche;
            try {
                memberche = event.getMessage().getMentionedUsers().get(0);
                if (!botcmd.notFound(memberche.getId())) {
                    Games.getGclass().challenge(author, memberche, channel);
                }
            } catch (IndexOutOfBoundsException ex) {
                    channel.sendMessage("You need to @mention the user").queue();
            }
            /*List<User> pedal = jda.getUsersByName(unvouchCmd[1].toString(), true);
            for (User usr : pedal) {
                if (pedal.contains(usr)) {
                    if (!botcmd.notFound(usr.getId())) {
                        Games.getGclass().Challenge(author, usr, channel);
                    } else {
                        //channel.sendMessage("User not found!").queue();
                    }
                }
            }*/
        } else if (msg.equalsIgnoreCase("!top")) {
            channel.sendMessage(botcmd.top()).queue();
        } else if (msg.equalsIgnoreCase("!bot")) {
            channel.sendMessage(botcmd.bot()).queue();
        } else if (msg.equalsIgnoreCase("!abort")) {
            botcmd.abort(author, channel);
        } else if (msg.equalsIgnoreCase("!out")) {
            botcmd.out(author, channel);
        } else if (msg.equalsIgnoreCase("!games")) {
            botcmd.games(channel);
        } else if (msg.startsWith("!result")) {
            String[] accCmd = msg.split(" ", 2);
            if (accCmd.length < 2 && accCmd[0].equalsIgnoreCase("!result") || accCmd.length < 2 && accCmd[0].equalsIgnoreCase("!r")) {
                channel.sendMessage("Use !result 1, 2, 0").queue();
            } else if (accCmd.length == 2 && accCmd[0].equalsIgnoreCase("!result") || accCmd.length == 2 && accCmd[0].equalsIgnoreCase("!r")) {
                botcmd.resultvotes(author, Integer.parseInt(accCmd[1]), channel);
            }
        } else if (msg.startsWith("!setdiv")) {
            String[] divCmd = msg.split(" ", 3);
            if (divCmd.length < 3) {
                channel.sendMessage("Usage !setdiv <user>").queue();
            } else {
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (!botcmd.notFound(memberche.getId())) {
                        botcmd.setDiv(author, Integer.parseInt(divCmd[1]), memberche, channel);
                    }
                } catch (IndexOutOfBoundsException ex) {
                        channel.sendMessage("You have to @mention the user").queue();
                }

            }
        } else if (msg.startsWith("!confirm")) {
            botcmd.confirm(author, channel);
        } else if (msg.startsWith("!gd") || msg.startsWith("!gamedetails")) {
            String[] gdcmd = msg.split(" ", 2);
            if (gdcmd.length < 2) {
                channel.sendMessage("Usage !gd <game number>").queue();
            } else {
                botcmd.gameDetails(Integer.parseInt(gdcmd[1]), channel);
            }
        } else if (msg.startsWith("!lt")) {
            String[] data = msg.split(" ", 2);
            if (data.length < 2) {
                botcmd.listTeams(author, -1, channel);
            } else {
                botcmd.listTeams(author, Integer.parseInt(data[1]), channel);
            }
        } else if (msg.startsWith("!ares")) {
            String[] data = msg.split(" ", 3);
            botcmd.adminResult(author, Integer.parseInt(data[1]), Integer.parseInt(data[2]), channel);
        } else if (msg.equalsIgnoreCase("!laino")) {
            botcmd.justAtest(channel);
        } else if (msg.startsWith("!pick")) {
            String[] accCmd = msg.split(" ", 2);
            if (accCmd.length < 2 && accCmd[0].equalsIgnoreCase("!p") || accCmd.length < 2 && accCmd[0].equalsIgnoreCase("!pick")) {
                channel.sendMessage("Use !pick <steamname>").queue();
            } else if (accCmd[0].equalsIgnoreCase("!p") && accCmd.length == 2 || accCmd[0].equalsIgnoreCase("!pick") && accCmd.length == 2 || accCmd[0].equalsIgnoreCase("+") && accCmd.length == 2) {
                botcmd.pickMethod(author, accCmd[1], channel);

            }
        } else if (msg.startsWith("!promote")) {
            String[] data = msg.split(" ", 3);
            if (data.length < 2) {
                channel.sendMessage("Usage: !promote <user> <rank>");
            }
            if (data[1].equalsIgnoreCase(author.getName())) {
                channel.sendMessage("I am busy, You are ugly, have a nice day :)").queue();
                return;
            } else if (data.length < 3) {
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (!botcmd.notFound(memberche.getId())) {
                        botcmd.promote(author, memberche, -1, channel);
                    } else {
                        channel.sendMessage("User not found!").queue();
                    }
                } catch (IndexOutOfBoundsException ex) {
                        channel.sendMessage("You have to @mention the user").queue();
                }
            } else {
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (!botcmd.notFound(memberche.getId())) {
                        botcmd.promote(author, memberche, Integer.parseInt(data[2]), channel);
                    } else {
                        channel.sendMessage("User not found!").queue();
                    }
                } catch (IndexOutOfBoundsException ex) {
                        channel.sendMessage("You have to @mention the user");
                }
            }
        } else if (msg.startsWith("!demote")) {
            String[] data = msg.split(" ", 3);
            if (data.length < 2) {
                channel.sendMessage("Usage: !demote <@user> <rank>").queue();
            }
            if (data[1].equalsIgnoreCase(author.getName())) {
                channel.sendMessage("I am busy, You are ugly, have a nice day :)").queue();
            } else if (data.length < 3) {
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (!botcmd.notFound(memberche.getId())) {
                        botcmd.demote(author, memberche, -1, channel);
                    } else {
                        channel.sendMessage("User not found!").queue();
                    }
                } catch (IndexOutOfBoundsException ex) {
                        channel.sendMessage("You have to @mention the user").queue();
                }
            } else {
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (!botcmd.notFound(memberche.getId())) {
                        botcmd.demote(author, memberche, Integer.parseInt(data[2]), channel);
                    } else {
                        channel.sendMessage("User not found!").queue();
                    }
                } catch (IndexOutOfBoundsException ex) {
                        channel.sendMessage("You have to @mention the user").queue();
                }
            }
        }
        if (msg.startsWith("!warn")) {
            String[] data = msg.split(" ", 4);
            if (data[0].equalsIgnoreCase("!warn") && data.length < 3) {
                channel.sendMessage("Usage: !warn <user> <type 1-2-3> <reason>").queue();
            } else if (data[0].equalsIgnoreCase("!warn") && data.length == 4 && Integer.parseInt(data[2]) < 4) {
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (!botcmd.notFound(memberche.getId())) {
                        botcmd.addWarn(author, memberche, Integer.parseInt(data[2]), data[3], channel);
                    } else {
                        channel.sendMessage("User not found!").queue();
                    }
                } catch (IndexOutOfBoundsException ex) {
                        channel.sendMessage("You have to @mention the user").queue();
                }
            }
        }
        if (msg.startsWith("!warns")) {
            String[] data = msg.split(" ", 2);
            if (data.length < 2) {
                botcmd.checkWarns(author, author, channel);
            } else {
                User memberche;
                try {
                    memberche = event.getMessage().getMentionedUsers().get(0);
                    if (!botcmd.notFound(memberche.getId())) {
                        botcmd.checkWarns(author, memberche, channel);
                    } else {
                        channel.sendMessage("User not found!").queue();
                    }
                } catch (IndexOutOfBoundsException ex) {
                        channel.sendMessage("You have to @mention the user").queue();
                }
            }
        }
        if (msg.startsWith("!warninfo")) {
            String[] data = msg.split(" ", 2);
            if (data.length < 2 && data[0].equalsIgnoreCase("!warninfo") || data.length < 2 && data[0].equalsIgnoreCase("!wi")) {
                channel.sendMessage("Use !warninfo <warnid>").queue();
            } else if (data.length == 2 && data[0].equalsIgnoreCase("!warninfo") || data.length == 2 && data[0].equalsIgnoreCase("!wi")) {
                botcmd.warnInfo(Integer.parseInt(data[1]), channel);
            }
        }
        if (msg.startsWith("!warnremove")) {
            String[] data = msg.split(" ", 2);
            if (data.length < 2 && data[0].equalsIgnoreCase("!warnremove") || data.length < 2 && data[0].equalsIgnoreCase("!wr")) {
                channel.sendMessage("Use !warnremove <warnid>").queue();
            } else if (data.length == 2 && data[0].equalsIgnoreCase("!warnremove") || data.length == 2 && data[0].equalsIgnoreCase("!wr")) {
                botcmd.warnRemove(author, Integer.parseInt(data[1]), channel);
            }
        }
        if (msg.startsWith("!compare")) {
            String[] data = msg.split(" ", 3);
            /*List<User> pedal = jda.getUsersByName(data[1].toString(), true);
            for (User usr : pedal) {
                if (pedal.contains(usr)) {
                    if (!botcmd.notFound(usr.getId())) {
                        botcmd.compareUsers(author, usr, channel);
                    } else {
                        channel.sendMessage("User not found!").queue();
                    }
                }
            }*/
            User memberche;
            try {
                memberche = event.getMessage().getMentionedUsers().get(0);
                if (!botcmd.notFound(memberche.getId())) {
                    botcmd.compareUsers(author, memberche, channel);
                } else {
                    channel.sendMessage("User not found!").queue();
                }
            } catch (IndexOutOfBoundsException ex) {
                    channel.sendMessage("You have to @mention the user").queue();
            }
        }
    }

}


