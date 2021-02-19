package com.davinci.Games;

import com.davinci.BotCommands;
import com.davinci.BotCommands.*;
import com.davinci.DataBase.Database;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static com.davinci.BotCommands.*;



/**
 * Created by macintoshhd on 3.02.18.
 */
public class Games {
    private static Games ref;
    public Calendar calendar = Calendar.getInstance();
    public List<String> gametime = new ArrayList();
    public List<String> radiant = new ArrayList();
    public List<String> dire = new ArrayList();
    public List<String> forbid = new ArrayList();
    public List<String> clgame = new ArrayList();
    public int gamedivision;
    public boolean sgon = false;
    public boolean sgconf = false;
    public String gcreator;
    public boolean gameOn = false;
    public String captain1 = "";
    public String captain2 = "";
    public int pick = 0;
    public int pickround = 0;
    public boolean challconf = false;
    public boolean chalGame = false;
    public List<String> chalCF = new ArrayList();
    public List<String> chalabort = new ArrayList();
    public String abortUser = "";
    java.sql.Connection data = Database.dbconnect();

    private Games() {
    }

    public static Games getGclass() {
        if (ref == null) // it's ok, we can call this constructor
        {
            ref = new Games();
        }
        return ref;
    }

    private static int showRandomInteger(int aStart, int aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }

    //Finds the stronger team and returns its index. Used by tryGameStart() function
    public static Team getHigherTeam(Team[] teams) {
        if (teams[0].getAverageRating() >= teams[1].getAverageRating()) {
            return teams[0];
        } else {
            return teams[1];
        }
    }

    public static Team getLowerTeam(Team[] teams) {
        if (teams[0].getAverageRating() >= teams[1].getAverageRating()) {
            return teams[1];
        } else {
            return teams[0];
        }
    }

    public boolean gameOn() {
        return sgon;
    }

    public void updatePlayers(int points, String user, int gamenumber) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE players SET points = ? WHERE disc_id = ? and gameid = ?");
            statement.setInt(1, points);
            statement.setString(2, user);
            statement.setInt(3, gamenumber);
            statement.executeUpdate();
        } catch (Exception pointsadd) {
            System.out.println(pointsadd);
        }
    }

    public int getType(int gamenum) {
        int types = 0;
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT type FROM games WHERE gameid = ?");
            statement.setInt(1, gamenum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                types = rs.getInt("type");
                return types;
            }
        } catch (Exception type) {
            type.printStackTrace();
        }
        return types;
    }

    public String tryGameStart(MessageChannel channel) {
        if (gametime.size() == 10) {
            //create teams
            Gamers[] playerPool = new Gamers[10];
            Team[] teams = new Team[2]; // 2 teams
            teams[0] = new Team("Radiant");
            teams[1] = new Team("Dire   ");
            Iterator iterator = gametime.iterator();
            //iterates through the list of players and getting their ratings/points. An array of Player (A Player class has the Nick and Points of a particular auth) is created.
            int k = 0;
            while (iterator.hasNext()) {
                String thisAuth = (String) iterator.next();
                playerPool[k] = new Gamers(thisAuth, BotCommands.points(thisAuth));
                k++;
            }
            //sort the list based on player ratings
            Arrays.sort(playerPool, new RatingComparator());
            List temp = Arrays.asList(playerPool);
            Iterator<Gamers> iteratorSorted = temp.iterator(); //iterator of the sorted list of players
            //autobalance starts
            for (int i = 0; i < 5; i++) { //iterate through the list of players (from high to small rating)
                float startingTeam = Math.round((float) Math.random());
                if (i == 0) { //no one assigned yet
                    if (startingTeam == 0) {
                        teams[0].addPlayer(iteratorSorted.next());
                        teams[1].addPlayer(iteratorSorted.next());
                    } else {
                        teams[1].addPlayer(iteratorSorted.next());
                        teams[0].addPlayer(iteratorSorted.next());
                    }
                } else {
                    getLowerTeam(teams).addPlayer(iteratorSorted.next());
                    getHigherTeam(teams).addPlayer(iteratorSorted.next());
                }
            }
            //print out the teams

            long curtime = System.currentTimeMillis();
            int gamenum = 0;
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("INSERT INTO games (started, timestart, division, type) VALUES ( ?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                statement.setTimestamp(1, BotCommands.ourJavaTimestampObject);
                statement.setLong(2, curtime);
                statement.setInt(3, gamedivision);
                statement.setInt(4, 1);
                statement.execute();
                ResultSet res = statement.getGeneratedKeys();
                while (res.next()) {
                    gamenum = res.getInt(1);
                }
//botmsg.sendAction(ch, "the number is: " + gamenum);
            } catch (Exception errors) {
                System.out.println(errors);
            }
            for (String user : gametime) {
                setIngame(user, gamenum);
            }
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("INSERT INTO players (disc_id, gameid, side) VALUES ( ?, ?, ?)");
                statement.setInt(3, 1);
                for (int i = 0; i < 5; i++) {
                    statement.setString(1, teams[0].getPlayerNick(i));
                    statement.setInt(2, gamenum);
                    statement.execute();
                }
                statement.setInt(3, 2);
                for (int i = 0; i < 5; i++) {
                    statement.setString(1, teams[1].getPlayerNick(i));
                    statement.setInt(2, gamenum);
                    statement.execute();
                }
            } catch (Exception newusers) {
                System.out.println(newusers);
            }
            String gameinfoz = "Creating game: Game number: #" + gamenum + ", Gametype: [StartGame] with teams:";
            StringBuilder str = new StringBuilder();
            str.append(teams[0].getName() + " (" + teams[0].getAverageRating() + "): ");
            for (int i = 0; i < 5; i++) {
                str.append(BotCommands.accSwitcher(teams[0].getPlayerNick(i)) + " (" + teams[0].getPlayerRating(i) + ")");
                if (i != 4) {
                    str.append(", ");
                }
            }

            StringBuilder str2 = new StringBuilder();
            str2.append(teams[1].getName() + "(" + teams[1].getAverageRating() + "): ");
            for (int i = 0; i < 5; i++) {
                str2.append(BotCommands.accSwitcher(teams[1].getPlayerNick(i)) + " (" + teams[1].getPlayerRating(i) + ")");
                if (i != 4) {
                    str2.append(", ");
                }
            }

            String s = String.join("\n"
                    , gameinfoz
                    , str
                    , str2);


            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!start the game (interface with SQL?) You do this part

            //create a new queue, reopen signups
            //!!!Need to reopen signup?
            gametime = new ArrayList<String>();

            gameOn = false;
            sgon = false;
            channel.sendMessage(s).queue();
        }
        //dont do anything otherwise
        return "";
    }

    public int streakcalc(int streaks) {
        streaks = (4 * streaks);
        return streaks;
    }


    public void setEnd(int gn) {
        java.sql.Timestamp ourJavaTimestampObject2 = new java.sql.Timestamp(calendar.getTime().getTime());
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE games SET ended = ?, timeend = ? WHERE gameid = ?");
            statement.setTimestamp(1, ourJavaTimestampObject2);
            statement.setLong(2, System.currentTimeMillis());
            statement.setInt(3, gn);
            statement.executeUpdate();
        } catch (Exception setend) {
            setend.printStackTrace();
        }
    }

    public void setResult(int result, int gamenumber) {
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement("UPDATE games SET result = ? WHERE gameid = ?");
            statement.setInt(1, result);
            statement.setInt(2, gamenumber);
            statement.executeUpdate();
        } catch (Exception gameres) {
            gameres.printStackTrace();
        }
    }

    public String gType(int number) {
        String type = "";
        if (getType(number) == 1) {
            type = "StartGame";
        } else {
            type = "Challenge";
        }
        return type;
    }


    public boolean startGame(User creator, int gdiv, MessageChannel chat) {

        int gamediv = 0;
        String query = "SELECT * FROM user WHERE disc_id = ?";
        try {
            PreparedStatement statement = (PreparedStatement) data.prepareStatement(query);
            statement.setString(1, creator.getId());
            ResultSet infos = statement.executeQuery();
            while (infos.next()) {
                if (infos.getInt("vouched") == 0) {
                    chat.sendMessage("You are not vouched.").queue();
                    return false;

                }
                if (gametime.isEmpty() == false) {
                    chat.sendMessage("The pool is open for signups, just jump in.").queue();
                    return false;
                }
                if (infos.getInt("ingame") != 0) {
                    chat.sendMessage("You are still in game: " + infos.getInt("ingame")).queue();
                    return false;
                }

                if (infos.getInt("division") == 2 && gdiv == 1) {
                    chat.sendMessage("Well.. thats sad :( you can`t create a game on 1st division").queue();
                    return false;
                }
                if (infos.getString("game_acc").equalsIgnoreCase("(must be set)")) {
                    chat.sendMessage("Set your steam name first").queue();
                    return false;
                }
                if (gameOn || sgon) {
                    chat.sendMessage("Just sign in.. TO THE OTHER OPEN GAME.").queue();
                    return false;
                }
                if (infos.getInt("division") == 1 && gdiv == 2 || infos.getInt("division") == gdiv) {
                    gamediv = gdiv;
                }
                gameOn = true;
                sgon = true;
                gametime.add(creator.getId());
                gamedivision = gdiv;
                gcreator = creator.getId();
                chat.sendMessage( BotCommands.accSwitcher(creator.getId()) + " has created " + "div-" + gamediv + " game, type is [Startgame], ingame mode is: [CM]").queue();
                return true;
            }
        } catch (Exception gogame) {
            gogame.printStackTrace();
        }
        return false;
    }


    public void resultStartGame(int result, int gamenumber, MessageChannel chat) {
        if (result == 1 && BotCommands.gameRes(gamenumber) == 3) {
            List<String> e_streaks = new ArrayList<String>();
            List<String> e_streaks_ints = new ArrayList<String>();
            List<String> t_dire = new ArrayList<String>();
            List<String> t_radiant = new ArrayList<String>();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            StringBuilder sb4 = new StringBuilder();
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 2);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_dire.contains(rs.getString("disc_id"))) {
                        break;
                    } else {
                        t_dire.add(rs.getString("disc_id"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 1);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_radiant.contains(rs.getString("disc_id"))) {
                        break;
                    } else {
                        t_radiant.add(rs.getString("disc_id"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            //now its time to get the dire team streaks if they are any+
            String usersWithStreak = " ";
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                String user = t_dire.get(i);
                int streakz = getStreak(user);
                String x = "" + streakz;
                if (streakz >= 5) {
                    e_streaks_ints.add(x);
                    e_streaks.add(accSwitcher(user) + " " + streakranks(streakz));
                    if (e_streaks.size() > 1) {
                        usersWithStreak = "for breaking streaks of: " + e_streaks;
                    } else if (e_streaks.size() == 1) {
                        usersWithStreak = "for breaking streak of " + accSwitcher(user) + " " + streakz + " (" + streakranks(streakz) + ")";
                    }
                }
            }
            for (int a = 0; a < e_streaks_ints.size(); a++) {
                sum += Integer.parseInt(e_streaks_ints.get(a));
            }
            sum = sum * 4;
            String special_bonus_rad = "";
            if (e_streaks.size() == 0) {
                special_bonus_rad = "";
            } else {
                special_bonus_rad = "Radiant got special bonus points " + sum + " " + usersWithStreak;
            }
            // end of the streak stuff now its time to give the points to the both teams
            //getting the random points aw yea ;d
            int START = 35;
            int END = 40;
            Random random = new Random();
            // time to set the wins to +1 and the points
            String rad_user = "";
            String dire_user = "";
            for (int i = 0; i < 5; i++) {
                rad_user = t_radiant.get(i);
                //int streak_rad = getStreak(rad_user);
                int wonexp = showRandomInteger(START, END, random) + sum;
                setWin(rad_user, 1);
                setBP(rad_user, wonexp);
                updatePlayers(wonexp, rad_user, gamenumber);
                setExp(rad_user, 250);
                if (getStreak(rad_user) >= 0) {
                    setStreak(rad_user, 1);
                } else {
                    setStreakTo(rad_user, 1);
                }
                setlg(rad_user, gamenumber);
                setIngame(rad_user, 0);
                String sep1 = " ";
                String compus = accSwitcher(rad_user) + " (+" + wonexp + ") ";
                sb1.append(sep1).append(compus);

                String sep3 = " ";
                String compus3 = accSwitcher(rad_user);
                sb3.append(sep3).append(compus3);
            }
            //points and streaks are set for radiant team now its time to set the dire and the usergames
            for (int i = 0; i < 5; i++) {
                dire_user = t_dire.get(i);
                int lostexp = showRandomInteger(START, END, random);
                int streak_dire = getStreak(dire_user);
                if ((streak_dire >= 5) || (streak_dire <= 5)) {
                    lostexp += streak_dire;
                }
                setLoss(dire_user, 1);
                setlg(dire_user, gamenumber);
                setBP(dire_user, -lostexp);
                updatePlayers(lostexp, dire_user, gamenumber);
                setExp(dire_user, 150);
                if (getStreak(dire_user) >= 0) {
                    setStreakTo(dire_user, -1);
                } else {
                    setStreakTo(dire_user, -1);
                }
                setIngame(dire_user, 0);
                String sep2 = " ";
                String compus = accSwitcher(dire_user) + " (-" + lostexp + ") ";
                sb2.append(sep2).append(compus);

                String sep4 = " ";
                String compus4 = accSwitcher(dire_user);
                sb4.append(sep4).append(compus4);
            }

            setResult(result, gamenumber);
            setEnd(gamenumber);
            String ginfo = "Game : #" + gamenumber + " Type [" + gType(gamenumber) + "] - Radiant has won! Expierience change:";
            String rad1 = "Radiant: " + sb1;
            String dire1 = "Dire: " + sb2;
            String final_msg = String.join("\n", special_bonus_rad, ginfo, rad1, dire1);
            chat.sendMessage(final_msg).queue();

        }
        // RESULTING FOR TEAM DIREEEEEEEEEEEEEE
        if (result == 2 && gameRes(gamenumber) == 3) {
            List<String> e_streaks = new ArrayList<String>();
            List<String> e_streaks_ints = new ArrayList<String>();
            List<String> t_dire = new ArrayList<String>();
            List<String> t_radiant = new ArrayList<String>();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            StringBuilder sb4 = new StringBuilder();
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 2);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_dire.contains(rs.getString("disc_id"))) {
                        break;
                    } else {
                        t_dire.add(rs.getString("disc_id"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 1);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_radiant.contains(rs.getString("disc_id"))) {
                        break;
                    } else {
                        t_radiant.add(rs.getString("disc_id"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            //now its time to get the dire team streaks if they are any+
            String usersWithStreak = " ";
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                String user = t_radiant.get(i);
                int streakz = getStreak(user);
                String x = "" + streakz;
                if (streakz >= 5) {
                    e_streaks_ints.add(x);
                    e_streaks.add(accSwitcher(user) + " " + streakranks(streakz));
                    if (e_streaks.size() > 1) {
                        usersWithStreak = "for breaking streaks of: " + e_streaks;
                    } else if (e_streaks.size() == 1) {
                        usersWithStreak = "for breaking streak of " + accSwitcher(user) + " " + streakz + " (" + streakranks(streakz) + ")";
                    }
                }
            }
            for (int a = 0; a < e_streaks_ints.size(); a++) {
                sum += Integer.parseInt(e_streaks_ints.get(a));
            }
            sum = sum * 4;
            String dire_streak = "";
            if (e_streaks.size() == 0) {
                dire_streak = "";
            } else {
                dire_streak = "Dire got special bonus points " + sum + " " + usersWithStreak;
            }
            // end of the streak stuff now its time to give the points to the both teams
            //getting the random points aw yea ;d
            int START = 35;
            int END = 40;
            Random random = new Random();
            // time to set the wins to +1 and the points
            String rad_user = "";
            String dire_user = "";
            for (int i = 0; i < 5; i++) {
                dire_user = t_dire.get(i);
                //int streak_rad = getStreak(rad_user);
                int wonexp = showRandomInteger(START, END, random) + sum;
                setWin(dire_user, 1);
                setBP(dire_user, wonexp);
                setlg(dire_user, gamenumber);
                updatePlayers(wonexp, dire_user, gamenumber);
                setExp(dire_user, 250);
                if (getStreak(dire_user) >= 0) {
                    setStreak(dire_user, 1);
                } else {
                    setStreakTo(dire_user, 1);
                }
                setIngame(dire_user, 0);
                String sep1 = " ";
                String compus = accSwitcher(dire_user) + " (+" + wonexp + ") ";
                sb1.append(sep1).append(compus);

                String sep4 = " ";
                String compus4 = accSwitcher(dire_user);
                sb4.append(sep4).append(compus4);
            }
            //points and streaks are set for radiant team now its time to set the dire and the usergames
            for (int i = 0; i < 5; i++) {
                rad_user = t_radiant.get(i);
                int lostexp = showRandomInteger(START, END, random);
                int streak_radiant = getStreak(rad_user);
                if ((streak_radiant >= 5) || (streak_radiant <= 5)) {
                    lostexp += streak_radiant;
                }
                setLoss(rad_user, 1);
                setlg(rad_user, gamenumber);
                setBP(rad_user, -lostexp);
                updatePlayers(lostexp, rad_user, gamenumber);
                setExp(rad_user, 150);
                if (getStreak(rad_user) >= 0) {
                    setStreakTo(rad_user, -1);
                } else {
                    setStreakTo(rad_user, -1);
                }
                setIngame(rad_user, 0);
                String sep2 = " ";
                String compus = accSwitcher(rad_user) + " (" + "-" + lostexp + ") ";
                sb2.append(sep2).append(compus);

                String sep3 = " ";
                String compus3 = accSwitcher(rad_user);
                sb3.append(sep3).append(compus3);
            }

            setResult(result, gamenumber);
            setEnd(gamenumber);
            String ginfo = "Game : #" + gamenumber + " Type [" + gType(gamenumber) + "] - Dire has won! Expierience change:";
            String rad1 = "Radiant: " + sb2;
            String dire1 = "Dire: " + sb1;
            String final_msg = String.join("\n", dire_streak, ginfo, rad1, dire1);
            chat.sendMessage(final_msg).queue();

        }


        if (result == 0 && gameRes(gamenumber) == 3) {
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            List<String> t_dire = new ArrayList<String>();
            List<String> t_radiant = new ArrayList<String>();
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 2);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_dire.contains(rs.getString("disc_id"))) {
                        break;
                    } else {
                        t_dire.add(rs.getString("disc_id"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 1);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_radiant.contains(rs.getString("disc_id"))) {
                        break;
                    } else {
                        t_radiant.add(rs.getString("disc_id"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            for (int i = 0; i < 5; i++) {
                String rad_user = t_radiant.get(i);
                setIngame(rad_user, 0);
                String sep1 = " ";
                String compus1 = accSwitcher(rad_user);
                sb1.append(sep1).append(compus1);
                setDraw(rad_user, 1);
                setlg(rad_user, gamenumber);
            }
            for (int i = 0; i < 5; i++) {
                String dire_user = t_dire.get(i);
                setIngame(dire_user, 0);
                String sep2 = " ";
                String compus2 = accSwitcher(dire_user);
                sb2.append(sep2).append(compus2);
                setDraw(dire_user, 1);
                setlg(dire_user, gamenumber);
            }

            setResult(result, gamenumber);
            setEnd(gamenumber);
            chat.sendMessage("Game: #" + gamenumber + " Type [" + gType(gamenumber) + "] - " + "Draw! No Expierience changes.").queue();
        }
    }


    public boolean challenge(User cap1, User cap2, MessageChannel chat) {
        if (gameOn || sgon) {
            chat.sendMessage("just sign in..");
            return false;
        }
        if (BotCommands.ifVouched(cap1) == false) {
            chat.sendMessage("You are not vouched baby :(").queue();
            return false;
        }
        if (BotCommands.notFound(cap2.getId())) {
            chat.sendMessage("Challenging ghost is against the rules here :(").queue();
            return false;
        }
        if (BotCommands.account(cap2.getId()).equalsIgnoreCase("(must be set)")) {
            chat.sendMessage("The captain must set his steam account first.").queue();
            return false;
        }
        if (account(cap1.getId()).equalsIgnoreCase("(must be set)")) {
            chat.sendMessage("The captain must set his steam account first.").queue();
            return false;
        }
        if (ingame(cap1.getId()) > 0) {
            chat.sendMessage("Finish your game first, you are not that good to play in 2 games at the same time oO").queue();
            return false;
        }
        if (ingame(cap2.getId()) > 0) {
            chat.sendMessage("Easy tiger, let him finish his game first.").queue();
            return false;
        }
        if (rank(cap1.getId()) < 2) {
            chat.sendMessage("Not enough brain to lead a squad.").queue();
            return false;
        }
        if (rank(cap2.getId()) < 2) {
            chat.sendMessage("You are trying to challenge a low rank user.").queue();
            return false;
        }
        if (cap1.getId().equalsIgnoreCase(cap2.getId())) {
            chat.sendMessage("Playing against yourself is kinda retarded, don`t you think??").queue();
            return false;
        }
        Random random = new Random();
        int thecaprand = showRandomInteger(0, 1, random);
        if (thecaprand < 1) {
            captain1 = cap1.getId();
            captain2 = cap2.getId();
        } else {
            captain1 = cap2.getId();
            captain2 = cap1.getId();
        }
        gameOn = true;
        //captain1 = cap1.getId();
        //captain2 = cap2.getId();
        chalGame = true;
        sgon = false;
        pick = 1;
        System.err.println("the int is " + thecaprand);
        chat.sendMessage( accSwitcher(cap1.getId()) + " challenges " + accSwitcher(cap2.getId()) + "! Type !sign to enter. " + accSwitcher(captain1) + " pick first!").queue();
        return true;
    }


    /*public void resultChallenge(int result, int gamenumber, MessageChannel chat) {
        if (result == 1 && gameRes(gamenumber) == 3) {
            List<String> e_streaks = new ArrayList<String>();
            List<String> e_streaks_ints = new ArrayList<String>();
            List<String> t_dire = new ArrayList<String>();
            List<String> t_radiant = new ArrayList<String>();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            StringBuilder sb4 = new StringBuilder();
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 2);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_dire.contains(rs.getString("disc_id"))) {
                        return;
                    } else {
                        t_dire.add(rs.getString("disc_id"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 1);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_radiant.contains(rs.getString("disc_id"))) {
                        return;
                    } else {
                        t_radiant.add(rs.getString("disc_id"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            //now its time to get the dire team streaks if they are any+
            String usersWithStreak = " ";
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                String user = t_dire.get(i);
                int streakz = getStreak(user);
                String x = "" + streakz;
                if (streakz >= 5) {
                    e_streaks_ints.add(x);
                    e_streaks.add(user + " " + streakranks(streakz));
                    if (e_streaks.size() > 1) {
                        usersWithStreak = "for breaking streaks of: " + e_streaks;
                    } else if (e_streaks.size() == 1) {
                        usersWithStreak = "for breaking streak of " + accSwitcher(user) + " " + streakz + " (" + streakranks(streakz) + ")";
                    }
                }
            }
            for (int a = 0; a < e_streaks_ints.size(); a++) {
                sum += Integer.parseInt(e_streaks_ints.get(a));
            }
            sum = sum * 2;
            String x = "";
            if (e_streaks.size() == 0) {
                x = "";
            } else {
                chat.sendMessage("Radiant got special bonus points " + sum +" " + usersWithStreak).queue();
            }
            // end of the streak stuff now its time to give the points to the both teams
            //getting the random points aw yea ;d
            int START = 35;
            int END = 45;
            Random random = new Random();
            // time to set the wins to +1 and the points
            String rad_user = "";
            String dire_user = "";
            for (int i = 0; i < 5; i++) {
                rad_user = t_radiant.get(i);
                //int streak_rad = getStreak(rad_user);
                int wonexp = showRandomInteger(START, END, random) + sum;
                setWin(rad_user, 1);
                setBP(rad_user, wonexp);
                updatePlayers(wonexp, rad_user, gamenumber);
                setExp(rad_user, 250);
                if (getStreak(rad_user) >= 0) {
                    setStreak(rad_user, 1);
                } else {
                    setStreakTo(rad_user, 1);
                }
                setlg(rad_user, gamenumber);
                setIngame(rad_user, 0);
                String sep1 = " ";
                String compus = accSwitcher(rad_user) + " (+" + wonexp + ")" + " [+" + 250 + "] ";
                sb1.append(sep1).append(compus);

                String sep3 = " ";
                String compus3 = accSwitcher(rad_user);
                sb3.append(sep3).append(compus3);
            }
            //points and streaks are set for radiant team now its time to set the dire and the usergames
            for (int i = 0; i < 5; i++) {
                dire_user = t_dire.get(i);
                int lostexp = showRandomInteger(START, END, random);
                int streak_dire = getStreak(dire_user);
                if ((streak_dire >= 5) || (streak_dire <= 5)) {
                    lostexp += streak_dire;
                }
                setLoss(dire_user, 1);
                setlg(dire_user, gamenumber);
                setBP(dire_user, -lostexp);
                updatePlayers(lostexp, dire_user, gamenumber);
                setExp(dire_user, 150);
                if (getStreak(dire_user) >= 0) {
                    setStreakTo(dire_user, -1);
                } else {
                    setStreakTo(dire_user, -1);
                }
                setIngame(dire_user, 0);
                String sep2 = " ";
                String compus = accSwitcher(dire_user) + " (-" + lostexp + ")" + " [+" + 100 + "] ";
                sb2.append(sep2).append(compus);

                String sep4 = " ";
                String compus4 = accSwitcher(dire_user);
                sb4.append(sep4).append(compus4);
            }

            setResult(result, gamenumber);
            setEnd(gamenumber);
            chat.sendMessage("Game: #" + gamenumber + " Type [" + gType(gamenumber) + "] - Radiant has won! Expirience change:").queue();
            chat.sendMessage("Radiant: " + sb1).queue();
            chat.sendMessage("Dire: " + sb2).queue();
        }
        // RESULTING FOR TEAM DIREEEEEEEEEEEEEE
        if (result == 2 && gameRes(gamenumber) == 3) {
            List<String> e_streaks = new ArrayList<String>();
            List<String> e_streaks_ints = new ArrayList<String>();
            List<String> t_dire = new ArrayList<String>();
            List<String> t_radiant = new ArrayList<String>();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            StringBuilder sb4 = new StringBuilder();
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 2);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_dire.contains(rs.getString("disc_id"))) {
                        return;
                    } else {
                        t_dire.add(rs.getString("disc_id"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            try {
                PreparedStatement statement = (PreparedStatement) data.prepareStatement("SELECT disc_id FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 1);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_radiant.contains(rs.getString("auth"))) {
                        return;
                    } else {
                        t_radiant.add(rs.getString("auth"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            //now its time to get the dire team streaks if they are any+
            String usersWithStreak = " ";
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                String user = t_radiant.get(i);
                int streakz = getStreak(user);
                String x = "" + streakz;
                if (streakz >= 5) {
                    e_streaks_ints.add(x);
                    e_streaks.add(user + " " + streakranks(streakz));
                    if (e_streaks.size() > 1) {
                        usersWithStreak = "for breaking streaks of: " + e_streaks;
                    } else if (e_streaks.size() == 1) {
                        usersWithStreak = "for breaking streak of " + usernames(user) + " " + Colors.RED + streakz + " (" + streakranks(streakz) + ")";
                    }
                }
            }
            for (int a = 0; a < e_streaks_ints.size(); a++) {
                sum += Integer.parseInt(e_streaks_ints.get(a));
            }
            sum = sum * 2;
            String x = "";
            if (e_streaks.size() == 0) {
                x = "";
            } else {
                chat.sendAction(chat.ch, Colors.DARK_GREEN + "Dire " + Colors.NORMAL + "got special bonus points " + Colors.DARK_GREEN + sum + Colors.NORMAL + " " + usersWithStreak);
            }
            // end of the streak stuff now its time to give the points to the both teams
            //getting the random points aw yea ;d
            int START = 35;
            int END = 45;
            Random random = new Random();
            // time to set the wins to +1 and the points
            String rad_user = "";
            String dire_user = "";
            for (int i = 0; i < 5; i++) {
                dire_user = t_dire.get(i);
                //int streak_rad = getStreak(rad_user);
                int wonexp = showRandomInteger(START, END, random) + sum;
                setWin(dire_user, 1);
                setBP(dire_user, wonexp);
                setlg(dire_user, gamenumber);
                updatePlayers(wonexp, dire_user, gamenumber);
                setExp(dire_user, 250);
                if (getStreak(dire_user) >= 0) {
                    setStreak(dire_user, 1);
                } else {
                    setStreakTo(dire_user, 1);
                }
                setIngame(dire_user, 0);
                String sep1 = " ";
                String compus = usernames(dire_user) + Colors.NORMAL + " (" + Colors.DARK_GREEN + "+" + wonexp + Colors.NORMAL + ")" + " [" + Colors.PURPLE + "+" + 250 + Colors.NORMAL + "] ";
                sb1.append(sep1).append(compus);

                String sep4 = " ";
                String compus4 = getNickFromAuth(z, dire_user);
                sb4.append(sep4).append(compus4);
            }
            //points and streaks are set for radiant team now its time to set the dire and the usergames
            for (int i = 0; i < 5; i++) {
                rad_user = t_radiant.get(i);
                int lostexp = showRandomInteger(START, END, random);
                int streak_radiant = getStreak(rad_user);
                if ((streak_radiant >= 5) || (streak_radiant <= 5)) {
                    lostexp += streak_radiant;
                }
                setLoss(rad_user, 1);
                setlg(rad_user, gamenumber);
                setBP(rad_user, -lostexp);
                updatePlayers(lostexp, rad_user, gamenumber);
                setExp(rad_user, 150);
                if (getStreak(rad_user) >= 0) {
                    setStreakTo(rad_user, -1);
                } else {
                    setStreakTo(rad_user, -1);
                }
                setIngame(rad_user, 0);
                String sep2 = " ";
                String compus = usernames(rad_user) + Colors.NORMAL + " (" + Colors.RED + "-" + lostexp + Colors.NORMAL + ")" + " [" + Colors.PURPLE + "+" + 100 + Colors.NORMAL + "] ";
                sb2.append(sep2).append(compus);

                String sep3 = " ";
                String compus3 = getNickFromAuth(z, rad_user);
                sb3.append(sep3).append(compus3);
            }
            int usebot = 1;
            if (chat.a >= chat.b) {
                usebot = 2;
            }
            gameDevoice(z, sb4, sb3);
            setResult(result, gamenumber);
            setEnd(gamenumber);
            chat.forceAction(chat.ch, Colors.NORMAL + "Game: #" + gamenumber + " Type [" + Colors.DARK_GREEN + gType(gamenumber) + Colors.NORMAL + "] - " + Colors.DARK_GREEN + "Dire" + Colors.NORMAL + " has won! Expirience change:", usebot);
            chat.forceAction(chat.ch, "Radiant: " + sb2, usebot);
            chat.forceAction(chat.ch, "Dire: " + sb1, usebot);
        }


        if (result == 0 && gameRes(gamenumber) == 3) {
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            List<String> t_dire = new ArrayList<String>();
            List<String> t_radiant = new ArrayList<String>();
            try {
                PreparedStatement statement = data.prepareStatement("SELECT auth FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 2);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_dire.contains(rs.getString("auth"))) {
                        return;
                    } else {
                        t_dire.add(rs.getString("auth"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            try {
                PreparedStatement statement = data.prepareStatement("SELECT auth FROM players WHERE side = ? and gameid = ?");
                statement.setInt(1, 1);
                statement.setInt(2, gamenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (t_radiant.contains(rs.getString("auth"))) {
                        return;
                    } else {
                        t_radiant.add(rs.getString("auth"));
                    }
                }
            } catch (Exception diress) {
                diress.printStackTrace();
            }
            for (int i = 0; i < 5; i++) {
                String rad_user = t_radiant.get(i);
                setIngame(rad_user, 0);
                String sep1 = " ";
                String compus1 = getNickFromAuth(z, rad_user);
                sb1.append(sep1).append(compus1);
                setDraw(rad_user, 1);
                setlg(rad_user, gamenumber);
            }
            for (int i = 0; i < 5; i++) {
                String dire_user = t_dire.get(i);
                setIngame(dire_user, 0);
                String sep2 = " ";
                String compus2 = getNickFromAuth(z, dire_user);
                sb2.append(sep2).append(compus2);
                setDraw(dire_user, 1);
                setlg(dire_user, gamenumber);
            }

            gameDevoice(z, sb1, sb2);
            setResult(result, gamenumber);
            setEnd(gamenumber);
            chat.sendAction(chat.ch, Colors.NORMAL + "Game: #" + gamenumber + " Type [" + Colors.DARK_GREEN + gType(gamenumber) + Colors.NORMAL + "] - " + Colors.DARK_GRAY + "Draw" + Colors.NORMAL + "! No expirience changes.");
        }
    }*/


}
