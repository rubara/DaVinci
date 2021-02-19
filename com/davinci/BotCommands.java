package com.davinci;


import com.davinci.DataBase.Database;
import com.davinci.Games.Games;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.ResultSet;
import java.sql.PreparedStatement;


import java.awt.*;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by macintoshhd on 10.01.18.
 */
public class BotCommands {


    static java.sql.Connection data = Database.dbconnect();
    static Calendar calendar = Calendar.getInstance();
    public static java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());


    public static String returnID(User member) {
        String userid = member.getId();
        return userid;
    }

    public static String accSwitcher(String givenID) {
        String acc = "neshto ne e kat trqa mai a?";
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT game_acc FROM user WHERE disc_id = ?");
            statement.setString(1, givenID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                acc = rs.getString("game_acc");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

    public static String accToID(String givenID) {
        String acc = "neshto ne e kat trqa mai a?";
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM user WHERE game_acc = ?");
            statement.setString(1, givenID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                acc = rs.getString("disc_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

    public static int points(String userQauth) {
        int points = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT battlepoints FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                points = rs.getInt("battlepoints");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }

    public static int rank(String userQauth) {
        int rank = 0;
        try {
            java.sql.PreparedStatement statement = data.prepareStatement("Select rank FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                rank = rs.getInt("rank");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rank;
    }

    public static boolean notFound(String name) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT count(*) as count FROM user WHERE disc_id = ?");
            statement.setString(1, name);
            System.err.println(name);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int count = rs.getInt("count");
                System.err.println(count);
                if (count == 1) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception yea) {
            yea.printStackTrace();
        }
        return true;
    }

    public static int ingame(String userQauth) {
        int vouched = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT ingame FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                vouched = rs.getInt("ingame");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vouched;
    }

    public static int getWins(String userQauth) {
        int rank = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT wins FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                rank = rs.getInt("wins");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rank;
    }

    public static int getLoss(String userQauth) {
        int rank = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT loss FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                rank = rs.getInt("loss");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rank;
    }

    public static int getExp(String userQauth) {
        int rank = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT exp FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                rank = rs.getInt("exp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rank;
    }

    public static void setLoss(String userAuth, int points) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET loss = ? WHERE disc_id = ?");
            statement.setInt(1, getLoss(userAuth) + points);
            statement.setString(2, userAuth);
            statement.execute();
        } catch (Exception ers) {
            ers.printStackTrace();
        }
    }

    public static int getDraw(String userQauth) {
        int rank = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT draw FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                rank = rs.getInt("draw");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rank;
    }

    public static void setDraw(String userAuth, int points) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET draw = ? WHERE disc_id = ?");
            statement.setInt(1, getDraw(userAuth) + points);
            statement.setString(2, userAuth);
            statement.execute();
        } catch (Exception ers) {
            ers.printStackTrace();
        }
    }

    public static void setWin(String userAuth, int points) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET wins = ? WHERE disc_id = ?");
            statement.setInt(1, getWins(userAuth) + points);
            statement.setString(2, userAuth);
            statement.execute();
        } catch (Exception ers) {
            ers.printStackTrace();
        }
    }

    public static void setExp(String userAuth, int points) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET exp = ? WHERE disc_id = ?");
            statement.setInt(1, getExp(userAuth) + points);
            statement.setString(2, userAuth);
            statement.execute();
        } catch (Exception ers) {
            ers.printStackTrace();
        }
    }

    public static void setStreakTo(String userAuth, int points) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET streak = ? WHERE disc_id = ?");
            statement.setInt(1, points);
            statement.setString(2, userAuth);
            statement.execute();
        } catch (Exception ers) {
            ers.printStackTrace();
        }
    }

    public static void setStreak(String userAuth, int points) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET streak = ? WHERE disc_id = ?");
            statement.setInt(1, getStreak(userAuth) + points);
            statement.setString(2, userAuth);
            statement.execute();
        } catch (Exception ers) {
            ers.printStackTrace();
        }
    }

    public static void setBP(String userAuth, int points) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET battlepoints = ? WHERE disc_id = ?");
            statement.setInt(1, points(userAuth) + points);
            statement.setString(2, userAuth);
            statement.execute();
        } catch (Exception ers) {
            ers.printStackTrace();
        }
    }

    public static void setlg(String userAuth, int points) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET lastgame = ? WHERE disc_id = ?");
            statement.setInt(1, points);
            statement.setString(2, userAuth);
            statement.execute();
        } catch (Exception ers) {
            ers.printStackTrace();
        }
    }

    public static void setIngame(String user, int ingame) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET ingame = ? WHERE disc_id = ?");
            statement.setInt(1, ingame);
            statement.setString(2, user);
            statement.executeUpdate();
        } catch (Exception radiantvoteset) {
            System.out.println("we fucked up at voting for dire");
        }
    }

    public static int gameRes(int gamenumb) {
        int res = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT result FROM games WHERE gameid = ?");
            statement.setInt(1, gamenumb);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                res = rs.getInt("result");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void whoAmIinfos(User usr, MessageChannel chat) {

        EmbedBuilder eb = new EmbedBuilder();


        String output = " neshto za pisane ?!";
        int rank = rank(usr.getId());
        String wordRank = "";
        String time = "";
        String nqkuv = usr.getId();
        //time = time.substring(0, time.length() - 2);
        System.err.println(nqkuv);
        switch (rank) {
            case 1:
                wordRank = "Knight of Justice";
                break;
            case 2:
                wordRank = "Knight of Grace";
                break;
            case 3:
                wordRank = "Knight Grand Cross";
                break;
            case 4:
                wordRank = "Monsignor";
                break;
            case 5:
                wordRank = "Knight Paladin";
                break;
            case 6:
                wordRank = "Knight Commander";
                break;
            case 7:
                wordRank = "Guardian of the Temple";
        }
        if (notFound(nqkuv)) {
            output = "user not found";
        }



        String query = "SELECT * FROM user WHERE (game_acc = ? OR disc_id = ?)";
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement(query);
            statement.setString(1, usr.getId());
            statement.setString(2, usr.getId());
            ResultSet infos = statement.executeQuery();
            while (infos.next()) {
                eb.setTitle("Profile information - " + " '" + wordRank + "' " + account(usr.getId()) + ", ID: " + infos.getInt("id"));
                eb.setFooter("https://strategyinrush.com", "https://scontent-sof1-1.xx.fbcdn.net/v/t1.0-9/29339777_1854763191232351_8508745203172956479_n.png?_nc_cat=103&_nc_ht=scontent-sof1-1.xx&oh=95dab87e7f6c4cab641e65b3de6b1045&oe=5D07F699");
                eb.setThumbnail(usr.getAvatarUrl());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("'at'" + " hh:mm:ss " + "'on'" + " dd MMMM yyyy");
                String dateAsString = simpleDateFormat.format(infos.getTimestamp("vouchdate"));
                if (infos.getString("game_acc") != "") {
                    if (infos.getInt("vouched") == 0) {
                        eb.addField("Player Division: ", infos.getInt("division") + "-division", true);
                        eb.addField("Registration information: ", "Unvouched " + dateAsString + " by " + accSwitcher(infos.getString("vouchedby")), true);
                        eb.setDescription("Player description: " + infos.getString("comment"));
                        eb.setImage("https://riskremoval.com/wp-content/uploads/2017/09/banned-1726366_1920-e1510011206304.jpg");
                        //output = "Player " + infos.getString("game_acc") + " (" + infos.getInt("id") + ")" + ": " + "has been unvouched by " + infos.getString("vouchedby") + " " + dateAsString + " with reason: " + infos.getString("comment");
                    } else if (infos.getInt("vouched") == 1) {
                        eb.addField("Player Division: ", infos.getInt("division") + "-division", true);
                        eb.addField("Registration information: ", "Vouched " + dateAsString + " by " + accSwitcher(infos.getString("vouchedby")), true);
                        eb.setDescription("Player description: " + infos.getString("comment"));
                        eb.setImage("https://s21425.pcdn.co/wp-content/uploads/2014/03/Registration-of-Food-Business-300x185.jpg");
                        //output = "div-" + infos.getInt("division") + " Player " + infos.getString("game_acc") + " (" + "ID:" + infos.getInt("id") + ")" + ": " + "vouched by " + accSwitcher(infos.getString("vouchedby")) + " " + dateAsString + ", Rank: " + wordRank + ", vouched with comment: " + infos.getString("comment");
                    }
                }
            }
        } catch (Exception queryerror) {
            queryerror.printStackTrace();
        }
        chat.sendMessage(eb.build()).queue();
        //chat.sendMessage(output).queue();
    }

    public static int getRanked(String user) {
        int position = 0;
        try {
            PreparedStatement statetement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM user ORDER BY battlepoints DESC");
            ResultSet rs = statetement.executeQuery();
            while (rs.next()) {
                position++;
                if (rs.getString("disc_id").equalsIgnoreCase(user)) {
                    return position;
                }
            }
        } catch (Exception ranked) {
            ranked.printStackTrace();
        }
        return position;
    }

    public static int getStreak(String userQauth) {
        int rank = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT streak FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                rank = rs.getInt("streak");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rank;
    }

    public static String streakranks(int rank) {
        String wordstreak = "";
        if (rank >= 19) {
            wordstreak = Integer.toString(Math.abs(rank)) + " VIGOSS' NIGHTMARE";
        } else if (rank >= 18) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Bringer of the Darkness";
        } else if (rank >= 16) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Satan's right hand";
        } else if (rank >= 15) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Apocalyptic";
        } else if (rank >= 14) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Hell warrior";
        } else if (rank >= 11) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Demonic";
        } else if (rank >= 10) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Overwhelming";
        } else if (rank >= 9) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Devastating";
        } else if (rank >= 8) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Crushing";
        } else if (rank >= 7) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Raging";
        } else if (rank >= 6) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Dangerous";
        } else if (rank >= 5) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Wrathful";
        } else if (rank == 4) {
            wordstreak = "4";
        } else if (rank == 3) {
            wordstreak = "3";
        } else if (rank == 2) {
            wordstreak = "2";
        } else if (rank == 1) {
            wordstreak = "1";
        } else if (rank == -1) {
            wordstreak = "-1";
        } else if (rank == -2) {
            wordstreak = "-2";
        } else if (rank == -3) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Unlucker";
        } else if (rank == -4) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Loser";
        } else if (rank == -5) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Despairing";
        } else if (rank == -6) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Hopeless";
        } else if (rank == -7) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Stinking";
        } else if (rank == -8) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Sucking";
        } else if (rank == -9) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Blowjob";
        } else if (rank == -10) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Deepthroat Blowjob";
        } else if (rank == -11) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Double Anal Penetration";
        } else if (rank == -12) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Porn Star";
        } else if (rank <= -13) {
            wordstreak = Integer.toString(Math.abs(rank)) + " Anal Fire";
        }
        //else {return Integer.toString(Math.abs(rank)); }
        return wordstreak;

    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public void showStats(User usr, MessageChannel chat) {

        String line = " ";
        String line2 = " ";
        int rankz = rank(usr.getId());

        EmbedBuilder eb = new EmbedBuilder();


        String query = "SELECT id, disc_id, game_acc, vouched, rank, division, wins, loss, draw, battlepoints, streak, exp, ingame, lastgame FROM user WHERE disc_id = ?";
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement(query);
            statement.setString(1, usr.getId());
            ResultSet infos = statement.executeQuery();
            while (infos.next()) {
                String ranked = "";
                if (getStreak(usr.getId()) == 0) {
                    ranked = "non-ranked";
                } else {
                    ranked = "#" + getRanked(usr.getId());
                }
                String textLine = "";
                int streak = infos.getInt("streak");
                if (infos.getInt("streak") == 0) {
                    line = "," + " Inactive";
                    textLine = "Inactive";
                } else {
                    if (streak > 0) {
                        textLine = "Winning streak: " + "+" + streakranks(streak);
                        line = ", Current winning streak: " + "+" + streakranks(streak);
                    } else {
                        textLine = "Losing streak: " + "" + streakranks(streak);
                        line = ", Current losing streak: " + "" + streakranks(streak);
                    }
                }

                eb.setTitle("Stats for Player " + usr.getName() + " - " + infos.getString("game_acc"), null);
                eb.setColor(Color.red);
                eb.setColor(new Color(0xF40C0C));
                eb.setColor(new Color(255, 0, 54));
                eb.setDescription("Title - " + wordRanks(rankz));
                eb.addField("Wins:", "" +infos.getInt("wins"), true);
                eb.addField("Loss:", "" + infos.getInt("loss"), true);
                eb.addField("Total Points:", "" + infos.getInt("battlepoints"), true);
                eb.addField("Current streak:",  textLine, true);
                eb.addField("Current position:", "Ladder: " + ranked, true);
                //eb.setImage("https://gamepedia.cursecdn.com/dota2_gamepedia/thumb/e/e7/SeasonalRank0-0.png/230px-SeasonalRank0-0.png?version=6d3899e42d1569b076a3518934d42054");
                eb.addBlankField(false);
                eb.setTimestamp(Instant.now());
                eb.setFooter("https://strategyinrush.com", "https://scontent-sof1-1.xx.fbcdn.net/v/t1.0-9/29339777_1854763191232351_8508745203172956479_n.png?_nc_cat=103&_nc_ht=scontent-sof1-1.xx&oh=95dab87e7f6c4cab641e65b3de6b1045&oe=5D07F699");
                eb.setThumbnail(usr.getAvatarUrl());


                int wins = infos.getInt("wins");
                int loss = infos.getInt("loss");
                int draw = infos.getInt("draw");
                String perc = " ";
                String imgaddr = " ";
                if (wins == 0 && loss == 0 && draw == 0) {
                    perc = "";
                    imgaddr = "https://gamepedia.cursecdn.com/dota2_gamepedia/thumb/e/e7/SeasonalRank0-0.png/230px-SeasonalRank0-0.png?version=6d3899e42d1569b076a3518934d42054";
                    eb.setImage(imgaddr);
                } else {
                    if (isBetween(infos.getInt("battlepoints"), 1200, 2000)) {
                        //Лорд
                    } else if (isBetween(infos.getInt("battlepoints"), 1050, 1199)) {
                        //
                    } else if (isBetween(infos.getInt("battlepoints"), 850, 1049)) {
                        //
                    } else if (isBetween(infos.getInt("battlepoints"), 701, 849)) {
                        //Фермер
                    } else if (isBetween(infos.getInt("battlepoints"), 500, 700)) {
                        //Ратай
                    }

                    perc = "" + wins * 100 / (wins + loss) + "% win rate, ";
                }

                //String line3 = ", Status: " + ;
                //line2 = "Stats for player " + infos.getString("game_acc") + ": " + infos.getInt("wins") + " wins, " + perc + infos.getInt("loss") + " losses, " + infos.getInt("battlepoints") + " points, " + "Ranked: " + ranked + " EXP: " + infos.getInt("exp");
                //line2 = line2 + line; //+line3
                //chat.sendMessage(line2).queue();
                chat.sendMessage(eb.build()).queue();
            }
        } catch (Exception queryerror) {
            queryerror.printStackTrace();
        }

    }

    public static boolean ifVouched(User user) {
        String nick = user.getName();
        String userID = user.getId();
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT vouched FROM user WHERE disc_id = ?");
            statement.setString(1, userID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getInt("vouched") > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String account(String userQauth) {
        String name = "(not set yet)";
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("Select game_acc FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                name = rs.getString("game_acc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public int gameType(int gamenumb) {
        int res = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT type FROM games WHERE gameid = ?");
            statement.setInt(1, gamenumb);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                res = rs.getInt("type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public int getRadVotes(int gamenum) {
        int votes = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT count(*) as count FROM gamevotes WHERE gameid = ? and side = ?");
            statement.setInt(1, gamenum);
            statement.setInt(2, 1);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                votes = rs.getInt("count");
                return votes;
            }
        } catch (Exception s) {
            s.printStackTrace();
        }
        return votes;
    }

    public int getDireVotes(int gamenum) {
        int votes = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT count(*) as count FROM gamevotes WHERE gameid = ? and side = ?");
            statement.setInt(1, gamenum);
            statement.setInt(2, 2);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                votes = rs.getInt("count");
                return votes;
            }
        } catch (Exception s) {
            s.printStackTrace();
        }
        return votes;
    }

    public int getDrawVotes(int gamenum) {
        int votes = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT count(*) as count FROM gamevotes WHERE gameid = ? and side = ?");
            statement.setInt(1, gamenum);
            statement.setInt(2, 0);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                votes = rs.getInt("count");
                return votes;
            }
        } catch (Exception s) {
            s.printStackTrace();
        }
        return votes;
    }

    public boolean getResVote(String auth, int game) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM gamevotes WHERE gameid = ? and disc_id = ?");
            statement.setInt(1, game);
            statement.setString(2, auth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (Exception gamez) {
            gamez.printStackTrace();
        }
        return false;
    }

    public int getDiv(String userQauth) {
        int div = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT division FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                div = rs.getInt("division");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return div;
    }

    public int getlg(String userQauth) {
        int div = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT lastgame FROM user WHERE disc_id = ?");
            statement.setString(1, userQauth);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                div = rs.getInt("lastgame");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return div;
    }

    public void vouchUser(User usr, User sender, String vouchCom, MessageChannel chat) {
        String output = "toz pyk :D ";
        if (rank(sender.getId()) < 4) {
            chat.sendMessage("You don`t have access to .vouch to command").queue();
            return;
        }
        if (ifVouched(usr) == true) {
            chat.sendMessage("Player " + usr.getName() + " is already vouched").queue();
            return;
        }
        if (notFound(usr.getId()) == true) {
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("INSERT INTO user (disc_id, division, vouched, vouchedby, vouchdate, comment) VALUES ( ?, ?, ?, ?, ?, ?) ");
                statement.setString(1, usr.getId());
                statement.setInt(2, 2);
                statement.setInt(3, 1);
                statement.setString(4, sender.getId());
                statement.setTimestamp(5, ourJavaTimestampObject);
                statement.setString(6, vouchCom);
                statement.executeUpdate();
                chat.sendMessage("Player " + usr.getName() + " has been vouched by " + sender.getName() + " with comment: " + vouchCom).queue();
                return;
            } catch (Exception es) {
                es.printStackTrace();
            }
        } else if (ifVouched(usr) == false) {
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET vouched = ?, vouchedby = ?, vouchdate = ?, comment = ? WHERE disc_id =? ");
                statement.setInt(1, 1);
                statement.setString(2, usr.getId());
                statement.setTimestamp(3, ourJavaTimestampObject);
                statement.setString(4, vouchCom);
                statement.setString(5, sender.getId());
                statement.executeUpdate();
                chat.sendMessage("Player " + usr.getName() + " has been revouched by his new master " + sender.getName() + " with comment: " + vouchCom).queue();
                return;
            } catch (Exception s) {
                s.printStackTrace();
            }
        }
    }

    public void acc(User usr, String gameacc, MessageChannel chat) {
        String output = "haha tapooonger :D";
        if (ifVouched(usr) == false) {
            chat.sendMessage("You are not registered").queue();
        }
        int ggccount = 0;
        int gamelenght = gameacc.length();
        if (gameacc.equalsIgnoreCase("my dick")) {
            chat.sendMessage("Nice try retard _|_").queue();
        }
        if (gamelenght > 30) {
            chat.sendMessage("The name is too long").queue();
        }
        if (gamelenght < 3) {
            chat.sendMessage("it`s.. too short, just like your dick").queue();
        }
        if (ifVouched(usr) == true & gamelenght >= 3 & gamelenght <= 15 & gameacc != account(usr.getId())) {
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT count(*) as count FROM user WHERE game_acc = ? ");
                statement.setString(1, gameacc);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    int count = rs.getInt("count");
                    if (count == 1) {
                        chat.sendMessage("Already taken").queue();
                    } else {
                        try {
                            PreparedStatement statement2 = (PreparedStatement) data.prepareStatement("UPDATE user SET game_acc =? WHERE disc_id = ?");
                            statement2.setString(1, gameacc);
                            statement2.setString(2, usr.getId());
                            statement2.executeUpdate();
                            chat.sendMessage("Player " + usr.getName() + " changed his steam account to " + gameacc).queue();
                        } catch (Exception add) {
                            add.printStackTrace();
                        }
                    }
                }
            } catch (Exception check) {
                check.printStackTrace();
            }
        }
    }

    public String ShowAccount(User user) {
        if (ifVouched(user) == false) {
            return "You are not registered";
        }
        return "Your steam account is " + account(user.getId());
    }

    public void unvouch(User sender, User usr, String vouchCom, MessageChannel chat) {
        String output = "neshto ne e kat trqa a ?";
        if (rank(sender.getId()) < 4) {
            chat.sendMessage("You don`t have access to !unvouch command").queue();
            return;
        }
        if (sender.getName().equalsIgnoreCase(usr.getName())) {
            chat.sendMessage("NIGGA?!?! You just went full retard..").queue();
            return;
        }
        if (usr.getName().equalsIgnoreCase("rubara")) {
            chat.sendMessage("toz pyk :D ").queue();
            return;
        }
        if (ifVouched(sender) == false) {
            chat.sendMessage("You are not vouched").queue();
            return;
        }
        if (rank(usr.getId()) >= 4) {
            chat.sendMessage("Demote your staff first").queue();
            return;
        }
        if (ifVouched(usr) == false) {
            chat.sendMessage("Player " + usr.getName() + " is already unvouched").queue();
            return;
        }
        if (notFound(usr.getId())) {
            chat.sendMessage("The user is not vouched..").queue();
            return;
        }
        if (rank(sender.getId()) == rank(usr.getId())) {
            chat.sendMessage("Nice try, but the i wont do it ;(").queue();
            return;
        }
        if (rank(sender.getId()) >= 4 || rank(usr.getId()) < 4) {
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET vouched = ?, vouchedby = ?, vouchdate = ?, comment = ? WHERE disc_id = ?");
                statement.setInt(1, 0);
                statement.setString(2, sender.getId());
                statement.setTimestamp(3, ourJavaTimestampObject);
                statement.setString(4, vouchCom);
                statement.setString(5, usr.getId());
                statement.executeUpdate();
                chat.sendMessage("Player " + usr.getName() + " has been unvouched by " + sender.getName() + ", description msg: " + vouchCom).queue();
                return;
            } catch (Exception uv) {
                uv.printStackTrace();
            }
        }
    }

    public void confirm(User userAuth, MessageChannel chat) {
        String shit = "ops fucked up";
        if (ifVouched(userAuth) == false) {
            chat.sendMessage("?!?!").queue();
            return;
        }
        if (Games.getGclass().gameOn == false) {
            chat.sendMessage("No games opened").queue();
            return;
        }
        if (Games.getGclass().sgon == true) {
            if (!Games.getGclass().gcreator.equals(userAuth.getId())) {
                chat.sendMessage("You don`t enough access to !confirm").queue();
                return;
            }
            if (Games.getGclass().sgconf == false) {
                if (Games.getGclass().gametime.size() < 10) {
                    chat.sendMessage("Not enough players in").queue();
                    return;
                }
            }
            Games.getGclass().tryGameStart(chat);
        }
        if (Games.getGclass().chalGame == true) {
            if (!Games.getGclass().captain1.equalsIgnoreCase(userAuth.getId()) && !Games.getGclass().captain2.equalsIgnoreCase(userAuth.getId())) {
                chat.sendMessage("You don`t enough access to !Confirm").queue();
                return;
            }
            if (Games.getGclass().challconf == false) {
                if (Games.getGclass().clgame.size() < 8) {
                    chat.sendMessage("Not enough players").queue();
                    return;
                }
                if (!Games.getGclass().chalCF.contains(userAuth.getId())) {
                    Games.getGclass().chalCF.add(userAuth.getId());
                    return;
                }
                if (Games.getGclass().chalCF.size() == 1) {
                    chat.sendMessage("Captain " + accSwitcher(userAuth.getId()) + " is ready to pick, second captain has to !confirm it").queue();
                    return;
                } else {
                    Games.getGclass().radiant.add(Games.getGclass().captain1);
                    Games.getGclass().dire.add(Games.getGclass().captain2);
                    Games.getGclass().pick = 0;
                    Games.getGclass().pickround = 1;
                    Games.getGclass().challconf = true;
                    Games.getGclass().chalCF.clear();
                    chat.sendMessage("Chose your teams! Captain " + accSwitcher(Games.getGclass().captain1) + " picks first.").queue();
                    return;
                }
            }
        }
    }


    public boolean sign(User auth, MessageChannel chat) {
        String x;
        String query = "SELECT * FROM user WHERE disc_id = ?";
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement(query);
            statement.setString(1, auth.getId());
            ResultSet infos = statement.executeQuery();
            while (infos.next()) {
                if (infos.getInt("vouched") < 1) {
                    chat.sendMessage("You are not vouched").queue();
                    return false;
                }
                if (Games.getGclass().gamedivision == 1) {
                    if (infos.getInt("division") == 2) {
                        chat.sendMessage("You must be div1 vouched to play in this game").queue();
                        return false;
                    }
                }
                if (account(auth.getId()).equalsIgnoreCase("(must be set)")) {
                    chat.sendMessage("Please, set your steam account first by using !steam <Steam Game Name>").queue();
                    return false;
                }
                ///DO NOT FORGET TO CHANGE THIS WHEN YOU MAKE CHALLENGE STUFF!!!!!
                if (Games.getGclass().gameOn() == false && Games.getGclass().chalGame == false) {
                    chat.sendMessage("There are no games to sign").queue();
                    return false;
                }
                if (infos.getInt("ingame") > 0) {
                    chat.sendMessage("You are still in game " + infos.getInt("ingame")).queue();
                    return false;
                }
                if (Games.getGclass().gametime.contains(auth.getId()) || Games.getGclass().clgame.contains(auth.getId())) {
                    chat.sendMessage("You have already signed in.").queue();
                    return false;
                }
                if (Games.getGclass().forbid.contains(auth.getId())) {
                    chat.sendMessage("You are not welcome in this game").queue();
                    return false;
                }
                //also change this when u make cl if(Games.getSG().gametime.size() == 10 & typeSg == 1 ) {
                if (Games.getGclass().gametime.size() == 10) {
                    chat.sendMessage("No more slots left").queue();
                    return false;
                }
                //and this also
                if (Games.getGclass().challconf == true) {
                    chat.sendMessage("The game is confirmed").queue();
                    return false;
                }
                if (Games.getGclass().chalGame == true) {
                    if (auth.getId().equalsIgnoreCase(Games.getGclass().captain1) || auth.getId().equalsIgnoreCase(Games.getGclass().captain2)) {
                        return false;
                    }
                    Games.getGclass().clgame.add(auth.getId());
                    return true;
                }
                if (Games.getGclass().sgon == true) {
                    Games.getGclass().gametime.add(auth.getId());
                    if (Games.getGclass().gametime.size() == 10) {
                        x = " No free slots are left. " + accSwitcher(Games.getGclass().gcreator) + " need !confirm the game";
                    } else {
                        x = " " + Integer.toString(10 - Games.getGclass().gametime.size()) + " slots left.";
                    }
                    chat.sendMessage("Player " + accSwitcher(auth.getId()) + " has joined the game!" + x).queue();
                    return true;
                }
            }
        } catch (Exception sign) {
            sign.printStackTrace();
        }
        return false;
    }


    public void lp(User auth, MessageChannel chat) {
        String query = "SELECT * FROM user WHERE disc_id = ?";
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement(query);
            statement.setString(1, auth.getId());
            ResultSet infos = statement.executeQuery();
            while (infos.next()) {
                if (infos.getInt("vouched") < 1) {
                    chat.sendMessage("You must be vouched to use the commands").queue();
                }
                if (Games.getGclass().gameOn == false) {
                    chat.sendMessage("There are no games").queue();
                } else if (Games.getGclass().gameOn == true && Games.getGclass().sgon == true) {
                    StringBuilder sb = new StringBuilder();
                    String sep = "";
                    for (int i = 0; i < Games.getGclass().gametime.size(); i++) {
                        String userche = account(Games.getGclass().gametime.get(i));
                        String compus = userche + " " + "(" + points(Games.getGclass().gametime.get(i)) + ")";
                        sb.append(sep).append(compus);
                        sep = ", ";
                    }
                    chat.sendMessage("Player list [" + Games.getGclass().gametime.size() + "]: " + sb).queue();
                }
                if (Games.getGclass().chalGame == true) {
                    if (Games.getGclass().clgame.size() == 0) {
                        chat.sendMessage("No one signed yet").queue();
                    } else {
                        StringBuilder sb = new StringBuilder();
                        String sep = "";
                        for (int i = 0; i < Games.getGclass().clgame.size(); i++) {
                            String userche = account(Games.getGclass().clgame.get(i));
                            String compus = userche + " " + "(" + points(Games.getGclass().clgame.get(i)) + ")";
                            sb.append(sep).append(compus);
                            sep = ", ";
                        }
                        chat.sendMessage("Player pool [" + Games.getGclass().clgame.size() + "]: " + sb).queue();
                    }
                }
            }
        } catch (Exception listpool) {
            listpool.printStackTrace();
        }
    }

    public void abort(User userAuth, MessageChannel chat) {
        if (ifVouched(userAuth) == false) {
            chat.sendMessage("Your are not vouched").queue();
            return;
        }
        if (Games.getGclass().gameOn == false) {
            chat.sendMessage("There are no open games").queue();
            return;
        }
        if (Games.getGclass().sgon == true) {
            if (rank(userAuth.getId()) > 4) {
                Games.getGclass().sgon = false;
                Games.getGclass().gameOn = false;
                Games.getGclass().forbid.clear();
                Games.getGclass().gametime.clear();
                chat.sendMessage("Game aborted").queue();
                return;
            }
            if (Games.getGclass().gcreator.equalsIgnoreCase(userAuth.getId())) {
                if (ingame(userAuth.getId()) > 0) {
                    chat.sendMessage("The game is already confirmed").queue();
                    return;
                }
                Games.getGclass().sgon = false;
                Games.getGclass().gameOn = false;
                Games.getGclass().forbid.clear();
                Games.getGclass().gametime.clear();
                chat.sendMessage("Game aborted").queue();
                return;
            } else {
                chat.sendMessage("Permission Denied").queue();
                return;
            }
        }
        if (Games.getGclass().chalGame == true && Games.getGclass().gameOn == true) {
            if (Games.getGclass().challconf == true) {
                if (!Games.getGclass().captain1.equalsIgnoreCase(userAuth.getId()) && !Games.getGclass().captain2.equalsIgnoreCase(userAuth.getId())) {
                    chat.sendMessage("One day you`ll be the chosen one!").queue();
                    return;
                }
                if (!Games.getGclass().chalabort.contains(userAuth)) {
                    Games.getGclass().chalabort.add(userAuth.getId());
                    return;
                }
                if (Games.getGclass().chalabort.size() == 1) {
                    chat.sendMessage("Captain " + accSwitcher(userAuth.getId()) + " voted for abort, second captain must !abort to abort game").queue();
                    return;
                } else {
                    Games.getGclass().gameOn = false;
                    Games.getGclass().clgame.clear();
                    Games.getGclass().chalGame = false;
                    Games.getGclass().captain1 = "";
                    Games.getGclass().captain2 = "";
                    Games.getGclass().chalGame = false;
                    Games.getGclass().chalabort.clear();
                    Games.getGclass().abortUser = "";
                    chat.sendMessage("Game aborted!").queue();
                    System.err.println("First Else abort");
                    return;
                }
            } else {
                Games.getGclass().gameOn = false;
                Games.getGclass().clgame.clear();
                Games.getGclass().captain1 = "";
                Games.getGclass().captain2 = "";
                Games.getGclass().chalGame = false;
                Games.getGclass().chalabort = new ArrayList<String>();
                Games.getGclass().abortUser = "";
                Games.getGclass().chalGame = false;
                chat.sendMessage("Game aborted!").queue();
                System.err.println("Second Else abort");
                return;
            }
        }
    }

    public String top() {
        List<String> tops = new ArrayList<String>();
        StringBuilder str = new StringBuilder();
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM user ORDER BY battlepoints DESC LIMIT 20");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (tops.contains(rs.getString("disc_id"))) {
                    break;
                } else {
                    tops.add(rs.getString("disc_id"));
                }
            }
            for (int i = 0; i < tops.size(); i++) {
                str.append(accSwitcher(tops.get(i)) + " (" + points(tops.get(i)) + ")");
                if (i != 19) {
                    str.append(", ");
                }
            }
            return "Top 20 players: " + str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "si ebalo maikata veche";
    }

    public String bot() {
        List<String> tops = new ArrayList<String>();
        StringBuilder str = new StringBuilder();
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM user ORDER BY battlepoints ASC LIMIT 20");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (tops.contains(rs.getString("disc_id"))) {
                    break;
                } else {
                    tops.add(rs.getString("disc_id"));
                }
            }
            for (int i = 0; i < tops.size(); i++) {
                str.append(accSwitcher(tops.get(i)) + " (" + points(tops.get(i)) + ")");
                if (i != 19) {
                    str.append(", ");
                }
            }
            return "Bottom 20 players: " + str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "basiii maikata";
    }

    public void out(User senderAuth, MessageChannel chat) {
        System.out.println(Games.getGclass().gcreator);
        if (ifVouched(senderAuth) == false) {
            chat.sendMessage("You are not vouched").queue();
            return;
        }
        if (Games.getGclass().gameOn == false) {
            chat.sendMessage("There are no open games").queue();
            return;
        }
        if (Games.getGclass().chalGame == true && Games.getGclass().captain2.equalsIgnoreCase(senderAuth.getId()) || Games.getGclass().captain1.equalsIgnoreCase(senderAuth.getId())) {
            chat.sendMessage("You are the fucking captain, i wont let you to lead your squad down..").queue();
            return;

        }
        if (Games.getGclass().chalGame == true && Games.getGclass().challconf == false) {
            Games.getGclass().clgame.remove(senderAuth.getId());
            return;
        }
        if (Games.getGclass().gcreator.equals(senderAuth.getId())) {
            chat.sendMessage("You are the game creator, you can`t leave before aborting it!").queue();
            return;

        } else if (!Games.getGclass().gametime.contains(senderAuth.getId())) {
            chat.sendMessage("You are not in the game pool").queue();
            return;
        }
        Games.getGclass().gametime.remove(senderAuth.getId());
        chat.sendMessage(accSwitcher(senderAuth.getId()) + " signed out. Needed players: [" + (10 - Games.getGclass().gametime.size()) + "]").queue();
        return;

    }

    public void games(MessageChannel chat) {
        String games = "";//game in the list
        Long timenow = System.currentTimeMillis();
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT * FROM games WHERE timeend = 1000000000");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                long gamestdur = rs.getLong("timestart");
                int gameid = rs.getInt("gameid");
                long result = timenow - gamestdur;
                int seconds = (int) (result / 1000) % 60;
                int minutes = (int) ((result / (1000 * 60)) % 60);
                int hours = (int) ((result / (1000 * 60 * 60)) % 24);
                games = games + " [" + gameid + " lasts: " + String.format("%02d:%02d:%02d", hours, minutes, seconds) + "]";
                //System.out.println(games);
            }
            if (!games.equals("")) {
                chat.sendMessage("Games in progress:" + games).queue();
            }
            if (games.equals("")) {
                chat.sendMessage("There are no games in progress!").queue();
            }
        } catch (Exception s) {
            s.printStackTrace();
        }
    }

    public void resultvotes(User userAuth, int Gameres, MessageChannel chat) {
        if (ifVouched(userAuth) == false) {
            chat.sendMessage("You are not vouched").queue();
        }
        if (ingame(userAuth.getId()) == 0) {
            chat.sendMessage(accSwitcher(userAuth.getId()) + " you are not ingame").queue();
            return;
        }
        if (Gameres != 1 && Gameres != 2 && Gameres != 0) {

        }
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT * FROM user WHERE disc_id = ?");
            statement.setString(1, userAuth.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int lg = rs.getInt("ingame");
                if (lg == 0) {
                    chat.sendMessage(accSwitcher(userAuth.getId()) + " you are not ingame").queue();
                    return;
                }
                if (gameRes(lg) == 1 || gameRes(lg) == 2 || gameRes(lg) == 0) {
                    String x = "";
                    if (gameRes(lg) == 1) {
                        x = "as win for The Radiant team";
                    }
                    if (gameRes(lg) == 2) {
                        x = "as win for The Dire team";
                    }
                    if (gameRes(lg) == 0) {
                        x = "as draw";
                    }
                    chat.sendMessage("This game already closed " + x).queue();
                    return;
                }
                if (getResVote(userAuth.getId(), lg)) {
                    chat.sendMessage("You have already voted").queue();
                    return;
                }
                if (Gameres == 1) {
                    try {
                        PreparedStatement votes = (PreparedStatement) data.prepareStatement("INSERT INTO gamevotes (disc_id, gameid, side) VALUES ( ?, ?, ?)");
                        votes.setString(1, userAuth.getId());
                        votes.setInt(2, lg);
                        votes.setInt(3, 1);
                        votes.execute();
                    } catch (Exception radiantvoteset) {
                        System.out.println("we fucked up at voting for radiant");
                        radiantvoteset.printStackTrace();
                    }
                }
                if (Gameres == 2) {
                    try {
                        PreparedStatement votes = (PreparedStatement) data.prepareStatement("INSERT INTO gamevotes (disc_id, gameid, side) VALUES ( ?, ?, ?)");
                        votes.setString(1, userAuth.getId());
                        votes.setInt(2, lg);
                        votes.setInt(3, 2);
                        votes.execute();
                    } catch (Exception radiantvoteset) {
                        System.out.println("we fucked up at voting for dire");
                        radiantvoteset.printStackTrace();
                    }
                }
                if (Gameres == 0) {
                    try {
                        PreparedStatement votes = (PreparedStatement) data.prepareStatement("INSERT INTO gamevotes (disc_id, gameid, side) VALUES ( ?, ?, ?)");
                        votes.setString(1, userAuth.getId());
                        votes.setInt(2, lg);
                        votes.setInt(3, 0);
                        votes.execute();
                    } catch (Exception radiantvoteset) {
                        System.out.println("we fucked up at voting for draw");
                        radiantvoteset.printStackTrace();
                    }
                }
                if ((lg == 0)) {
                    chat.sendMessage("No such game..").queue();
                }
                if (getRadVotes(lg) == 6) {
                    if (gameType(lg) == 1) {
                        Games.getGclass().resultStartGame(Gameres, lg, chat);
                    }
                    if (gameType(lg) == 2) {
                        Games.getGclass().resultStartGame(Gameres, lg, chat);
                    }
                }
                if (getDireVotes(lg) == 6) {
                    if (gameType(lg) == 1) {
                        Games.getGclass().resultStartGame(Gameres, lg, chat);
                    }
                    if (gameType(lg) == 2) {
                        Games.getGclass().resultStartGame(Gameres, lg, chat);
                    }
                }
                if (getDrawVotes(lg) == 6) {
                    if (gameType(lg) == 1) {
                        Games.getGclass().resultStartGame(Gameres, lg, chat);
                    }
                    if (gameType(lg) == 2) {
                        Games.getGclass().resultStartGame(Gameres, lg, chat);
                    }
                }
            }
        } catch (Exception rv) {
            rv.printStackTrace();
        }
        chat.sendMessage("Voted!").queue();
    }

    public String gameDivChange(User user, int divisionz) {
        if (ifVouched(user) == false) {
            return "You are not vouched";
        }
        if (Games.getGclass().gameOn == false) {
            return "There are no active games";
        }
        if (!Games.getGclass().gcreator.equalsIgnoreCase(user.getId())) {
            return "Only the game creator can change the division.";
        }
        if (divisionz > 2 || divisionz == 0) {
            return "Invalid game division. Avalible divisions - 1, 2";
        }
        if (Games.getGclass().gamedivision == divisionz) {
            return "It`s the same division you dumb shit..";
        }
        if (Games.getGclass().gamedivision != divisionz) {
            Games.getGclass().gamedivision = divisionz;
            return "Game division has been changed to " + divisionz;

        }
        return "si ebalo mecha";
    }

    public void setDiv(User senderAuth, int division, User divUser, MessageChannel chat) {
        if (rank(senderAuth.getId()) < 5) {
            chat.sendMessage("You don`t have enough access to use this command").queue();
            return;
        }
        if (senderAuth.getId().equalsIgnoreCase(divUser.getId())) {
            chat.sendMessage("You can`t change your own division..").queue();
            return;
        }
        if (ifVouched(divUser) == false) {
            chat.sendMessage("The player is not vouched").queue();
            return;
        }
        if (division == getDiv(divUser.getId())) {
            chat.sendMessage("The user is in the same division.").queue();
            return;
        }
        if (rank(senderAuth.getId()) > 4 && division != getDiv(divUser.getId())) {
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET division = ? WHERE disc_id = ? ");
                statement.setInt(1, division);
                statement.setString(2, divUser.getId());
                statement.executeUpdate();
                chat.sendMessage("Player " + accSwitcher(divUser.getId()) + " is div-" + division + " vouched now").queue();
            } catch (Exception div) {
                div.printStackTrace();
            }
        } else {
            return;
        }
    }

    public boolean theGame(int gamenum) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT count(*) as count FROM games WHERE gameid = ? ");
            statement.setInt(1, gamenum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getInt("count") == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception s) {
            s.printStackTrace();
        }
        return false;
    }

    public void gameDetails(int gamenum, MessageChannel chat) {
        if (theGame(gamenum) == false) {
            chat.sendMessage("Invalid gameID").queue();
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.red);
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT * FROM games WHERE gameid = ?");
            statement.setInt(1, gamenum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String res = "";
                String type = "";
                String x = "";
                String ended = "";
                String winnerz = "";
                int gres = rs.getInt("result");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("'at'" + " hh:mm:ss " + "'on'" + " dd MMMM yyyy");
                //String dateAsString = simpleDateFormat.format(rs.getTimestamp("ended"));
                String started = simpleDateFormat.format(rs.getTimestamp("started"));
                if (rs.getInt("type") == 1) {
                    type = "StartGame";
                } else {
                    type = "Challenge";
                }
                if (rs.getLong("timeend") == 1000000000) {
                    ended = "Still in progress";
                } else {
                    ended = simpleDateFormat.format(rs.getTimestamp("ended"));
                }
                switch (gres) {
                    case 0:
                        res = "resulted as draw";
                        winnerz = "data:image/png;base64,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";
                        break;
                    case 1:
                        res = "Radiant" + " has won!";
                        winnerz = "https://i.ytimg.com/vi/QLMqNk5WvU0/maxresdefault.jpg";
                        break;
                    case 2:
                        res = "Dire" + " has won!";
                        winnerz = "https://steamuserimages-a.akamaihd.net/ugc/838083598172997810/0A0D1832244926E677C41C1740019113397C3EB6/";
                        break;
                    case 3:
                        res = "";
                        break;
                }
                eb.setTitle("Information about Div-" + rs.getInt("division") + " game #" + rs.getInt("gameid"));
                eb.addField("Game Type: ", type, true);
                eb.addField("Started: ", started, true);
                eb.addField("Finished: ", ended, true);
                eb.setDescription("Result: "+ res);
                eb.setThumbnail(winnerz);
                x = "Div-" + rs.getInt("division") + " game " + "#" + rs.getInt("gameid") + ", Type [" + type + "] started " + started + " finished: " + ended + " " + res;
                //chat.sendMessage(x).queue();
                chat.sendMessage(eb.build()).queue();
            }
        } catch (Exception s) {
            s.printStackTrace();
        }
    }

    public void listTeams(User user, int gamenumber, MessageChannel chat) {
        if (gamenumber == -1) {
            gamenumber = getlg(user.getId());
        }
        if (theGame(gamenumber) == true) {
            List<String> radiant = new ArrayList();
            List<String> dire = new ArrayList();
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 1);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (radiant.contains(rs.getString("disc_id"))) {
                        break;
                    } else {
                        radiant.add(rs.getString("disc_id"));
                    }
                }
                //System.err.println(radiant);
            } catch (Exception radiants) {
                radiants.printStackTrace();
            }
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 2);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (dire.contains(rs.getString("disc_id"))) {
                        break;
                    } else {
                        dire.add(rs.getString("disc_id"));
                    }
                }
                //System.err.println(radiant);
            } catch (Exception radiants) {
                radiants.printStackTrace();
            }
            String gameinf = " Teams for game: #" + gamenumber;

            StringBuilder str = new StringBuilder();
            str.append("Team Radiant: ");
            for (int i = 0; i < 5; i++) {
                str.append(accSwitcher(radiant.get(i)));
                if (i != 4) {
                    str.append(", ");
                }
            }

            StringBuilder str2 = new StringBuilder();
            str2.append("Team Dire: ");
            for (int i = 0; i < 5; i++) {
                str2.append(accSwitcher(dire.get(i)));
                if (i != 4) {
                    str2.append(", ");
                }
            }
            String s = String.join("\n"
                    , gameinf
                    , str
                    , str2);

            chat.sendMessage(s).queue();
        } else {
            chat.sendMessage("no such game").queue();
        }
    }

    public void adminResult(User user, int result, int gamenum, MessageChannel chat) {
        if (rank(user.getId()) < 7) {

        } else {
            Games.getGclass().resultStartGame(result, gamenum, chat);
        }

    }


    public void justAtest(MessageChannel chat) {

        chat.sendMessage("testvam si malko az").queue();

    }

    public String whoPicks(int picks) {
        String res = "";
        switch (picks) {
            case 1:
            case 4:
            case 5:
            case 8:
            case 9:
                res = Games.getGclass().captain1;
                break;
            case 2:
            case 3:
            case 6:
            case 7:
            case 10:
                res = Games.getGclass().captain2;
                break;
            default:
                res = " we fucked up somewhere call rubara";
                break;
        }
        return res;
    }

    public void pickMethod(User captain, String user, MessageChannel chat) {
        if (ifVouched(captain) == false) {
            chat.sendMessage("You are not vouched retard..").queue();
            return;
        }
        if (Games.getGclass().chalGame == false) {
            chat.sendMessage("There is no challenge oO").queue();
            return;
        }
        if (!captain.getId().equalsIgnoreCase(Games.getGclass().captain1) && !captain.getId().equalsIgnoreCase(Games.getGclass().captain2)) {
            chat.sendMessage("You don`t have enough access to !Pick").queue();
            return;
        }
        if (!captain.getId().equalsIgnoreCase(whoPicks(Games.getGclass().pickround))) {
            chat.sendMessage(accSwitcher(whoPicks(Games.getGclass().pickround)) + "`s turn to pick.").queue();
            return;
        } else {
            pickPlayer(user, chat);
        }

    }


    public void pickPlayer(String user, MessageChannel chat) {
        String playerz = accToID(user);
        String match = null;
        System.err.println("This is match - " + match);
        System.err.println("This is playerz - " + match);
        for (String matching : Games.getGclass().clgame) {
            System.err.println("This is matching - " + matching);
            if (matching.contains(playerz)) {
                if (match != null) {
                    chat.sendMessage("There are a few players matching the: " + user + ", please be more specific.").queue();
                    return;
                }
                match = matching.toLowerCase();
            }
        }
        if (!Games.getGclass().clgame.contains(match.toLowerCase())) {
            chat.sendMessage(user + " is not in the pool.").queue();
            return;
        }
        String picker = "";
        String whopicknext = "";
        switch (Games.getGclass().pickround + 1) {
            case 1:
            case 4:
            case 5:
            case 8:
            case 9:
                whopicknext = Games.getGclass().captain1;
                break;
            case 2:
            case 3:
            case 6:
            case 7:
            case 10:
                whopicknext = Games.getGclass().captain2;
                break;
            default:
                whopicknext = "WARRNING BITCHES WE FUCKED UP!! CALL RUBARA ASAP!";
                break;
        }

        if (whoPicks(Games.getGclass().pickround) == Games.getGclass().captain1) {
            Games.getGclass().radiant.add(match);
            picker = Games.getGclass().captain1;
        }
        if (whoPicks(Games.getGclass().pickround) == Games.getGclass().captain2) {
            Games.getGclass().dire.add(match);
            picker = Games.getGclass().captain2;
        }
        Games.getGclass().clgame.remove(match);
        if (Games.getGclass().pickround < 8) {
            chat.sendMessage(accSwitcher(picker) + " has picked " + accSwitcher(match) + " ! " + accSwitcher(whopicknext) + "`s turn to pick. Total picks " + (Games.getGclass().radiant.size() + Games.getGclass().dire.size()) + "/" + "10").queue();
        } else {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                str.append(accSwitcher(Games.getGclass().radiant.get(i)));
                if (i != 4) {
                    str.append(", ");
                }
            }

            StringBuilder str2 = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                str2.append(accSwitcher(Games.getGclass().dire.get(i)));
                if (i != 4) {
                    str2.append(", ");
                }
            }
            long curtime = System.currentTimeMillis();
            int gamenum = 0;
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("INSERT INTO games (started, timestart, division, type) VALUES ( ?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                statement.setTimestamp(1, ourJavaTimestampObject);
                statement.setLong(2, curtime);
                statement.setInt(3, 2);
                statement.setInt(4, 2);
                statement.execute();
                ResultSet res = statement.getGeneratedKeys();
                while (res.next()) {
                    gamenum = res.getInt(1);
                }
//botmsg.sendAction(ch, "the number is: " + gamenum);
            } catch (Exception errors) {
                System.out.println(errors);
            }
            for (String users : Games.getGclass().radiant) {
                setIngame(users, gamenum);
            }
            for (String users2 : Games.getGclass().dire) {
                setIngame(users2, gamenum);
            }
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("INSERT INTO players (disc_id, gameid, side) VALUES ( ?, ?, ?)");
                statement.setInt(3, 1);
                for (int i = 0; i < 5; i++) {
                    statement.setString(1, Games.getGclass().radiant.get(i));
                    statement.setInt(2, gamenum);
                    statement.execute();
                }
                statement.setInt(3, 2);
                for (int i = 0; i < 5; i++) {
                    statement.setString(1, Games.getGclass().dire.get(i));
                    statement.setInt(2, gamenum);
                    statement.execute();
                }
            } catch (Exception newusers) {
                System.out.println(newusers);
            }
            Games.getGclass().gameOn = false;
            Games.getGclass().chalGame = false;
            Games.getGclass().chalCF.clear();
            Games.getGclass().clgame.clear();
            Games.getGclass().challconf = false;
            Games.getGclass().pickround = 0;
            Games.getGclass().pick = 0;
            Games.getGclass().radiant.clear();
            Games.getGclass().dire.clear();
            Games.getGclass().captain1 = "";
            Games.getGclass().captain2 = "";
            chat.sendMessage("Creating game: Game number: " + "#" + gamenum + ", Gametype: [Challenge] with teams:").queue();
            chat.sendMessage("The Radiant: " + str).queue();
            chat.sendMessage("The Dire: " + str2).queue();
        }
        Games.getGclass().pickround++;
    }

    public void promote(User userAuth, User promoted, int rank, MessageChannel chat) {
        int newrank = 0;
        if (rank == -1) {
            newrank = rank(promoted.getId()) + 1;
        } else {
            newrank = rank;
        }
        String wordRank = "";
        switch (newrank) {
            case 1:
                wordRank = "Knight of Justice";
                break;
            case 2:
                wordRank = "Knight of Grace";
                break;
            case 3:
                wordRank = "Knight Grand Cross";
                break;
            case 4:
                wordRank = "Monsignor";
                break;
            case 5:
                wordRank = "Knight Paladin";
                break;
            case 6:
                wordRank = "Knight Commander";
                break;
            case 7:
                wordRank = "Guardian of the Temple";
        }
        if (ifVouched(userAuth) == false) {
            chat.sendMessage("You are not vouched.").queue();
            return;
        }
        if (userAuth.getId().equalsIgnoreCase(promoted.getId())) {
            chat.sendMessage("Nice try bitch, see ya later.").queue();
            return;
        }
        if (ifVouched(promoted) == false) {
            chat.sendMessage("User " + promoted + " is not vouched.").queue();
            return;
        }
        if (rank(userAuth.getId()) < 5) {
            chat.sendMessage("Access Denied. Not enough rank to promote the user.").queue();
            return;
        }
        if (rank(userAuth.getId()) < rank(promoted.getId())) {
            chat.sendMessage("You are trying to promote a user with higher rank than yours? WTF DUDE??!").queue();
            return;
        }
        if (rank(promoted.getId()) == newrank) {
            chat.sendMessage("The user is already promoted with this rank.").queue();
            return;
        }
        if (rank(userAuth.getId()) <= newrank) {
            chat.sendMessage("Access Denied. You are trying to overpower yourself, or this is the maximum rank.").queue();
            return;
        } else {
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET rank = ? WHERE disc_id = ?");
                statement.setInt(1, newrank);
                statement.setString(2, promoted.getId());
                statement.executeUpdate();
            } catch (Exception E) {
                E.printStackTrace();
            }
            chat.sendMessage("User " + accSwitcher(promoted.getId()) + " has been promoted to " + wordRank + " by " + accSwitcher(userAuth.getId())).queue();
        }
    }


    public String wordRanks(int rankz) {
        String wordRank = "";
        switch (rankz) {
            case 1:
                wordRank = "Knight of Justice";
                break;
            case 2:
                wordRank = "Knight of Grace";
                break;
            case 3:
                wordRank = "Knight Grand Cross";
                break;
            case 4:
                wordRank = "Monsignor";
                break;
            case 5:
                wordRank = "Knight Paladin";
                break;
            case 6:
                wordRank = "Knight Commander";
                break;
            case 7:
                wordRank = "Guardian of the Temple";
        }
        return wordRank;
    }

    public void demote(User userAuth, User demoted, int rank, MessageChannel chat) {
        int newrank = 0;
        if (rank == -1) {
            newrank = rank(demoted.getId()) - 1;
        } else {
            newrank = rank;
        }
        String wordRank = "";
        switch (newrank) {
            case 1:
                wordRank = "Knight of Justice";
                break;
            case 2:
                wordRank = "Knight of Grace";
                break;
            case 3:
                wordRank = "Knight Grand Cross";
                break;
            case 4:
                wordRank = "Monsignor";
                break;
            case 5:
                wordRank = "Knight Paladin";
                break;
            case 6:
                wordRank = "Knight Commander";
                break;
            case 7:
                wordRank = "Guardian of the Temple";
        }
        if (ifVouched(userAuth) == false) {
            chat.sendMessage("You are not vouched.").queue();
            return;
        }
        if (ifVouched(demoted) == false) {
            chat.sendMessage("The user is not vouched.").queue();
            return;
        }
        if (rank(userAuth.getId()) < 5) {
            chat.sendMessage("Access Denied.").queue();
            return;
        }
        if (rank(userAuth.getId()) > rank(demoted.getId()) && rank(demoted.getId()) == 1) {
            chat.sendMessage("You cannot demote this user to any lower level.").queue();
            return;
        }
        if (rank(userAuth.getId()) < rank(demoted.getId())) {
            chat.sendMessage("You cannot demote a user with higher class than yours.").queue();
            return;
        }
        if (rank(demoted.getId()) == newrank) {
            chat.sendMessage("The user is already this rank.").queue();
            return;
        }
        if (rank > rank(demoted.getId())) {
            chat.sendMessage("Use .promote you fucking idiot..").queue();
            return;
        }
        if (rank(userAuth.getId()) <= newrank) {
            chat.sendMessage("You cannot demote a user with rank higher than yours").queue();
            return;
        }
        if (userAuth.getId().equalsIgnoreCase(demoted.getId())) {
            chat.sendMessage("You cannot demote yourself, please contact the administrator.").queue();
            return;
        } else {
            int crank = rank(demoted.getId());
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE user SET rank = ? WHERE disc_id = ?");
                statement.setInt(1, newrank);
                statement.setString(2, demoted.getId());
                statement.executeUpdate();
            } catch (Exception E) {
            }
            chat.sendMessage("Player " + accSwitcher(demoted.getId()) + " has been demoted from rank " + wordRanks(crank) + " to " + wordRanks(newrank)).queue();
        }

    }


    public void swap(User admin, User player1, User player2, MessageChannel chat) {
        if (rank(admin.getId()) < 3) {
            chat.sendMessage("Access Denied.").queue();
            return;
        }
        if (ifVouched(admin) == false) {
            chat.sendMessage("You are not vouched.").queue();
            return;
        }
        if (ifVouched(player1) == false) {
            chat.sendMessage("The user is not vouched.").queue();
            return;
        }
        if (ifVouched(player2) == false) {
            chat.sendMessage("The user is not vouched.").queue();
            return;
        }
        if (ingame(player1.getId()) == 0) {
            chat.sendMessage(player1 + " is not ingame.").queue();
            return;
        }
        if (ingame(player2.getId()) > 0) {
            chat.sendMessage(player2 + " is still ingame and can`t be swaped.");
            return;
        }
        if (ingame(player1.getId()) == ingame(player2.getId())) {
            chat.sendMessage("Both captains fucked up with the picks and called you to fix it? Well the users are in the same game so..").queue();
            return;
        }
        if (gameType(ingame(player1.getId())) == 1) {
            chat.sendMessage("Stacking in StartGame is not allowed!").queue();
            return;
        }
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE players SET disc_id = ? WHERE disc_id = ? AND gameid = ?");
            statement.setString(1, player2.getId());
            statement.setString(2, player1.getId());
            statement.setInt(3, ingame(player1.getId()));
            statement.executeUpdate();
            chat.sendMessage(player1 + " successfully swaped with " + player2 + " in game #" + ingame(player1.getId()));
        } catch (Exception swap) {
            swap.printStackTrace();
        }
        setIngame(player2.getId(), ingame(player1.getId()));
        setIngame(player1.getId(), 0);
    }


    public void setWarn(User sender, User reciver, int type, String reason) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("INSERT INTO warns (disc_id, warnedby_id, type, warnreason, timer, active) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, reciver.getId());
            statement.setString(2, sender.getId());
            statement.setInt(3, type);
            statement.setString(4, reason);
            statement.setLong(5, System.currentTimeMillis());
            statement.setInt(6,1);
            statement.executeUpdate();
        } catch (Exception trywarn) {
            trywarn.printStackTrace();
        }
    }

    public void addWarn(User sender, User reciver, int type, String reason, MessageChannel chat) {
        if (ifVouched(sender) == false) {
            chat.sendMessage("You are not vouched.").queue();
            return;
        }
        if (ifVouched(reciver) == false) {
            chat.sendMessage("That user is not vouched.").queue();
            return;
        }
        if (rank(sender.getId()) < 5) {
            chat.sendMessage("Access Denied.").queue();
            return;
        }
        if (reciver.getId().equalsIgnoreCase(sender.getId())) {
            chat.sendMessage("You can`t warn yourself..").queue();
            return;
        }
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT warnid, warnedby_id, type, warnreason, timer, active FROM warns WHERE disc_id = ? ORDER BY warnid DESC ");
            statement.setString(1, reciver.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (System.currentTimeMillis() - rs.getLong("timer") < 3600000 && rs.getInt("active") == 1) {
                    chat.sendMessage("You can warn this user once per hour.").queue();
                    return;
                }
            }
            if (type == 1) {
                chat.sendMessage("Player " + accSwitcher(reciver.getId()) + " has been warned by " + accSwitcher(sender.getId()) + ", Type [Poor play], the player lost 40 battle points and 125 EXP. Warned for: " + reason).queue();
                setBP(reciver.getId(), -40);
                setExp(reciver.getId(), -125);
                setWarn(sender, reciver, type, reason);
            }
            if (type == 2) {
                chat.sendMessage("Player " + accSwitcher(reciver.getId()) + " has been warned by " + accSwitcher(sender.getId()) + ", Type [Serious violation], the player lost 20 battle points and 90 EXP. Warned for: " + reason).queue();
                setBP(reciver.getId(), -20);
                setExp(reciver.getId(), -90);
                setWarn(sender, reciver, type, reason);
            }
            if (type == 3) {
                chat.sendMessage("Player " + accSwitcher(reciver.getId()) + " has been warned by " + accSwitcher(sender.getId()) + ", Type [Misc. violation], the player lost 10 battle points and 50 EXP. Warned for: " + reason).queue();
                setBP(reciver.getId(), -10);
                setExp(reciver.getId(), -50);
                setWarn(sender, reciver, type, reason);
            }
        } catch (Exception addwarn) {
            addwarn.printStackTrace();
        }
    }

    public boolean warnIdChecker(int warn) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT count(*) as count FROM warns WHERE warnid = ? ");
            statement.setInt(1, warn);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getInt("count") == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception s) {
            s.printStackTrace();
        }
        return false;
    }


    public void warnRemove(User admin, int warnid, MessageChannel chat) {
        if (ifVouched(admin) == false) {
            chat.sendMessage("You are not vouched").queue();
            return;
        }
        if (warnIdChecker(warnid) == false) {
            chat.sendMessage("The warning may be deleted or not received yet").queue();
            return;
        }
        if (rank(admin.getId()) < 5) {
            chat.sendMessage("Access Denied.").queue();
            return;
        }
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id, warnedby_id, type, warnreason, active FROM warns WHERE warnid = ?");
            statement.setInt(1, warnid);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int type = rs.getInt("type");
                String wtype = "";
                switch (type) {
                    case 1:
                        wtype = "Poor play (-40 battle points and -125 EXP) ";
                        break;
                    case 2:
                        wtype = "Serious violation (-20 battle points and -90 EXP) ";
                        break;
                    case 3:
                        wtype = "Misc. violation (-10 battle points and -50 EXP) ";
                        break;
                }
                if (rs.getString("disc_id").equalsIgnoreCase(admin.getId())) {
                    chat.sendMessage("You can`t remove your own warnings :)").queue();
                    return;
                }
                if (rs.getInt("active") == 1) {
                    try {
                        PreparedStatement statement1 = (PreparedStatement) data.prepareStatement("UPDATE warns SET active = ?  WHERE warnid =?");
                        statement1.setInt(1, 0);
                        statement1.setInt(2, warnid);
                        statement1.executeUpdate();
                        switch (type) {
                            case 1:
                                setBP(rs.getString("disc_id"), 40);
                                setExp(rs.getString("disc_id"), 125);
                                break;
                            case 2:
                                setBP(rs.getString("disc_id"), 20);
                                setExp(rs.getString("disc_id"), 90);
                                break;
                            case 3:
                                setBP(rs.getString("disc_id"), 10);
                                setExp(rs.getString("disc_id"), 50);
                                break;
                        }
                        chat.sendMessage("Warning removed").queue();
                        return;
                    } catch (Exception try2) {
                        try2.printStackTrace();
                    }
                } else {
                    chat.sendMessage("That warning was removed before.").queue();
                    return;
                }
                try {
                    PreparedStatement statement1 = (PreparedStatement) data.prepareStatement("UPDATE warns SET active = ? FROM warns WHERE warnid =?");
                    statement1.setInt(1, 0);
                    statement1.setInt(2, warnid);
                    statement1.executeUpdate();
                    switch (type) {
                        case 1:
                            setBP(rs.getString("disc_id"), 40);
                            setExp(rs.getString("disc_id"), 125);
                            break;
                        case 2:
                            setBP(rs.getString("disc_id"), 20);
                            setExp(rs.getString("disc_id"), 90);
                            break;
                        case 3:
                            setBP(rs.getString("disc_id"), 10);
                            setExp(rs.getString("disc_id"), 50);
                            break;
                    }
                    chat.sendMessage("Warning removed").queue();
                    return;
                } catch (Exception try2) {
                    try2.printStackTrace();
                }
            }
        } catch (Exception warn) {
            warn.printStackTrace();
        }

    }


    public void warnInfo(int warnid, MessageChannel chat) {
        if (warnIdChecker(warnid) == true) {
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id, warnedby_id, type, warnreason, active FROM warns WHERE warnid = ?");
                statement.setInt(1, warnid);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    int type = rs.getInt("type");
                    String wtype = "";
                    switch (type) {
                        case 1:
                            wtype = "Poor play (-40 battle points and -125 EXP) ";
                            break;
                        case 2:
                            wtype = "Serious violation (-20 battle points and -90 EXP) ";
                            break;
                        case 3:
                            wtype = "Misc. violation (-10 battle points and -50 EXP) ";
                            break;
                    }
                    int active = rs.getInt("active");
                    String wactive = "";
                    switch (active) {
                        case 0:
                            wactive = "Inactive";
                            break;
                        case 1:
                            wactive = "Active";
                            break;
                    }
                    chat.sendMessage("Warning " + "#" + warnid + " Status [" + wactive + "]: Player " +  accSwitcher(rs.getString("disc_id")) + " was warned by " + accSwitcher(rs.getString("warnedby_id")) + " with warn type [" + wtype + "] with reason: " + rs.getString("warnreason")).queue();
                    return;
                }
            } catch (Exception warn) {
                warn.printStackTrace();
            }
        } else {
            chat.sendMessage("Invalid ID.").queue();
            return;
        }
    }

    public void checkWarns(User admin, User user, MessageChannel chat) {
        List<Integer> warns = new ArrayList<Integer>();
        StringBuilder str = new StringBuilder();
        if (ifVouched(user) == false) {
            chat.sendMessage("The user is not vouched.").queue();
            return;
        }
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT warnid, active FROM warns WHERE disc_id = ? ");
            statement.setString(1, user.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getInt("active") == 1) {
                    warns.add(rs.getInt("warnid"));
                }
            }
            if (warns.size() == 0) {
                chat.sendMessage("Player " + accSwitcher(user.getId()) + " has 0 warning(s).").queue();
                return;
            }
            for (int i = 0; i < warns.size(); i++) {
                str.append("#" + warns.get(i));
                if (i != warns.size() - 1) {
                    str.append(", ");
                }
            }
            chat.sendMessage("Warning(s): " + str).queue();
        } catch (Exception check) {
            check.printStackTrace();
        }
    }


    public void capstats (User user) {
        /*List<String> radiant = new ArrayList();
        List<String> dire = new ArrayList();
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
            statement.setInt(1, 1);
            statement.setInt(2, gamenumber);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (radiant.contains(rs.getString("disc_id"))) {
                    break;
                } else {
                    radiant.add(rs.getString("disc_id"));
                }
            }
            //System.err.println(radiant);
        } catch (Exception radiants) {
            radiants.printStackTrace();
        }
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
            statement.setInt(1, 2);
            statement.setInt(2, gamenumber);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (dire.contains(rs.getString("disc_id"))) {
                    break;
                } else {
                    dire.add(rs.getString("disc_id"));
                }
            }
            //System.err.println(radiant);
        } catch (Exception radiants) {
            radiants.printStackTrace();
        }*/
        int numb = 1;

    }


public int gamesCount(User user) {
int counters = getWins(user.getId()) + getLoss(user.getId());
return counters;
}

public int gamesTogetherWin(User user1, User user2) {
    int results = 0;

    int i = 1;
    int u1lg = getlg(user1.getId());
    int u2lg = getlg(user2.getId());


        return results;
}

    public void compareUsers(User user1, User user2, MessageChannel chat) {
        if (ifVouched(user1) == false) {
            chat.sendMessage("You are not vouched.").queue();
            return;
        }
        if (ifVouched(user2) == false) {
            chat.sendMessage("The user is not vouched.").queue();
            return;
        }
        int user1games = gamesCount(user1);
        int user2games = gamesCount(user2);
        int gamediff = Math.abs(user1games - user2games);
        String output = "Player " + accSwitcher(user1.getId()) + " [" + points(user1.getId()) + "] compared to " + accSwitcher(user2.getId()) + " [" + points(user2.getId()) + "]: ";
        if (gamesCount(user1) > gamesCount(user2)) {
        output = output + gamediff + " games more for " + accSwitcher(user1.getId()) + ", ";
            int windiff = (getWins(user1.getId()) - getWins(user2.getId()));
            int lossdiff = (getLoss(user1.getId()) - getLoss(user2.getId()));
            if (getWins(user1.getId()) == getWins(user2.getId())) {
                output = output + " same win count: [" + getWins(user1.getId()) + "], ";
            } else {
                String word = "";
                if (windiff > 0) {
                  word = "more";
                } else if (windiff < 0) {
                    word = "less";
                }
                output = output + Math.abs(windiff) + " wins " + word + ", ";
            }
            if (getLoss(user1.getId()) == getLoss(user2.getId())) {
                output = output + " same losses count: [" + getLoss(user1.getId()) + "], ";
            } else {
               String lossword = "";
               if (lossdiff > 0) {
                   lossword = "more";
               } else if (lossdiff < 0) {
                   lossword = "less";
               }
               output = output + Math.abs(lossdiff) + " losses " + lossword;
            }
        } else if(gamesCount(user1) < gamesCount(user2)) {
            output = output + gamediff + " games more for " + accSwitcher(user2.getId()) + ", ";
            int windiff = (getWins(user2.getId()) - getWins(user1.getId()));
            int lossdiff = (getLoss(user2.getId()) - getLoss(user1.getId()));
            if (getWins(user1.getId()) == getWins(user2.getId())) {
                output = output + " same win count: [" + getWins(user2.getId()) + "], ";
            } else {
                String word = "";
                if (windiff > 0) {
                    word = "more";
                } else if (windiff < 0) {
                    word = "less";
                }
                output = output + Math.abs(windiff) + " wins " + word + ", ";
            }
            if (getLoss(user2.getId()) == getLoss(user1.getId())) {
                output = output + " same losses count: [" + getLoss(user2.getId()) + "], ";
            } else {
                String lossword = "";
                if (lossdiff > 0) {
                    lossword = "more";
                } else if (lossdiff < 0) {
                    lossword = "less";
                }
                output = output + Math.abs(lossdiff) + " losses " + lossword;
            }
        } else if (gamesCount(user1) == gamesCount(user2)) {
            output = output + gamediff + " same games count, ";
            int windiff = (getWins(user2.getId()) - getWins(user1.getId()));
            int lossdiff = (getLoss(user2.getId()) - getLoss(user1.getId()));
            if (getWins(user1.getId()) == getWins(user2.getId())) {
                output = output + " same win count: [" + windiff + "], ";
            } else {
                String word = "";
                if (windiff > 0) {
                    word = "more";
                } else if (windiff < 0) {
                    word = "less";
                }
                output = output + Math.abs(windiff) + " wins " + word + ", ";
            }
            if (getLoss(user2.getId()) == getLoss(user1.getId())) {
                output = output + " same losses count: [" + lossdiff + "], ";
            } else {
                String lossword = "";
                if (lossdiff > 0) {
                    lossword = "more";
                } else if (lossdiff < 0) {
                    lossword = "less";
                }
                output = output + Math.abs(lossdiff) + " losses " + lossword;
            }
        }
        //Need to continue with ranking comparation
        int user1rank = getRanked(user1.getId());
        int user2rank = getRanked(user2.getId());
        if (user1rank < user2rank) {
            output = output + ". Rank difference: +" + Math.abs(user1rank - user2rank) + " for " + accSwitcher(user1.getId()) + " (" + user1rank + " against " + user2rank + ").";
        } else {
            output = output + ". Rank difference: +" + Math.abs(user2rank - user1rank) + " for " + accSwitcher(user2.getId()) + " (" + user2rank + " against " + user1rank + ").";
        }
        chat.sendMessage(output).queue();
    }

}
