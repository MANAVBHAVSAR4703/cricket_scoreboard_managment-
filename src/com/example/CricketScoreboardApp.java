package com.example;

import com.example.model.Ball;
import com.example.model.Innings;
import com.example.model.Match;
import com.example.model.Player;
import com.example.model.Team;
import com.example.util.JSONUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CricketScoreboardApp {

    private static final Scanner scanner = new Scanner(System.in);
    static boolean finalScoreCardDisplay = false;
    static boolean inningComplete = false;

    public static void main(String[] args) {
        try (scanner) {
            System.out.println("Welcome to Cricket Scoreboard Application!");
            
            Match match = null;
            
            while (match == null) {
                System.out.print("Do you want to load an existing match? (yes/no): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                switch (choice) {
                    case "yes" -> match = loadMatch();
                    case "no" -> match = startNewMatch();
                    default -> System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
            
            playMatch(match);
        }
    }

    private static Match loadMatch() {
        try {
            Match match = JSONUtils.readMatchFromJSON("data/match.txt");
            System.out.println("Match loaded successfully.");
            return match;
        } catch (IOException e) {
            System.err.println("Error loading match: " + e.getMessage());
            return null;
        }
    }

    private static Match startNewMatch() {
        String venue = getInputWithValidation("Enter match venue: ");
        String date = getInputWithValidation("Enter match date (YYYY-MM-DD): ");
        int numOvers = getValidatedInt("Enter number of overs: ");
        String team1Name = getInputWithValidation("Enter Name of Team 1: ");
        String team2Name = getInputWithValidation("Enter Name of Team 2: ");
        System.out.println("Enter details for Team 1:");
        Team team1 = initializeTeam(team1Name);

        System.out.println("Enter details for Team 2:");
        Team team2 = initializeTeam(team2Name);

        return new Match(venue, date, numOvers, team1, team2);
    }

    private static String getInputWithValidation(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                break;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
        return input;
    }

    private static Team initializeTeam(String teamName) {
        List<Player> players = new ArrayList<>();
        int numPlayers = getValidatedInt("Enter number of players for " + teamName + ": ");
        for (int i = 0; i < numPlayers; i++) {
            String playerName = getInputWithValidation("Enter name for Player " + (i + 1) + ": ");
            players.add(new Player(playerName));
        }
        return new Team(teamName, players);
    }

    private static int getValidatedInt(String prompt) {
        int number;
        while (true) {
            System.out.print(prompt);
            try {
                number = Integer.parseInt(scanner.nextLine().trim());
                if (number > -1) {
                    break;
                } else {
                    System.out.println("Number must be positive. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid runs.");
            }
        }
        return number;
    }

    private static void playMatch(Match match) {
        System.out.println("\nStarting the match:");
        System.out.println(match);

        playInnings(match, match.getTeam1(), match.getInnings1());

        if (!inningComplete) {
            int target = match.getInnings1().getTotalRuns() + 1;
            System.out.println("\nTarget for Team 2: " + target);
            playInnings(match, match.getTeam2(), match.getInnings2(), target);
        }
        if (!finalScoreCardDisplay) {
            System.out.println("\nThe match is completed.");
            System.out.println("\nFinal match details:");
            displayMatchDetails(match);
        }
    }

    private static void playInnings(Match match, Team battingTeam, Innings innings) {
        playInnings(match, battingTeam, innings, Integer.MAX_VALUE);
    }

    private static void playInnings(Match match, Team battingTeam, Innings innings, int target) {
        if (innings.getBalls().isEmpty()) {
            System.out.println("\nEnter opening batsmen for " + battingTeam.getName() + ":");
            Player striker = getPlayer(battingTeam, "Enter name of striker: ");
            Player nonStriker = getPlayer(battingTeam, "Enter name of non-striker: ");
            innings.setStriker(striker);
            innings.setNonStriker(nonStriker);
        }

        while (!match.isCompleted() && !innings.isCompleted(match.getNumOvers()) && innings.getTotalRuns() < target) {
            System.out.println(
                    "\nCurrent Over: " + innings.getCurrentOver() + " | Ball: " + (innings.getCurrentBall() + 1));

            displayOptions();
            int choice = getValidatedInt("Choose an option: ");
            handleBall(match, innings, choice);

            if (match.isCompleted()) {
                return;
            }

            saveMatch(match);
            displayScorecard(innings);
        }
    }

    private static void displayOptions() {
        System.out.println("1. Normal ball");
        System.out.println("2. No ball");
        System.out.println("3. Wide");
        System.out.println("4. Wicket");
    }

    private static void handleBall(Match match, Innings innings, int choice) {
        switch (choice) {
            case 1 -> handleNormalBall(match, innings);
            case 2 -> handleNoBall(innings);
            case 3 -> handleWide(innings);
            case 4 -> handleWicket(match, innings);
            default -> System.out.println("Invalid option. Please choose again.");
        }
    }

    private static void handleNormalBall(Match match, Innings innings) {
        int runs = getValidatedInt("Enter runs scored on this ball: ");
        Ball ball = new Ball(runs, innings.getStriker().getName());
        innings.addBall(ball);
        innings.getStriker().addRuns(runs);
        innings.addTotalRuns(runs);

        if (runs % 2 == 1) {
            Player temp = innings.getStriker();
            innings.setStriker(innings.getNonStriker());
            innings.setNonStriker(temp);
        }

        if (innings.getCurrentBall() % 6 == 0 && innings.getCurrentOver() != 0) {
            Player temp = innings.getStriker();
            innings.setStriker(innings.getNonStriker());
            innings.setNonStriker(temp);
        }

        if (innings.isCompleted(match.getNumOvers()) || battingTeamIsAllOut(innings)) {
            System.out.println("Innings completed for " + innings.getBattingTeam().getName());
        }
    }

    private static void handleNoBall(Innings innings) {
        int runs = getValidatedInt("Enter runs scored on no ball: ");
        Ball ball = new Ball(runs, innings.getStriker().getName());
        ball.setNoBall(true);
        innings.addBall(ball);
        innings.getStriker().addRuns(runs);
        innings.addTotalRuns(runs + 1);
    }

    private static void handleWide(Innings innings) {
        int runs = getValidatedInt("Enter runs scored on wide: ");
        Ball ball = new Ball(runs, innings.getStriker().getName());
        ball.setWide(true);
        innings.addBall(ball);
        innings.addTotalRuns(runs + 1);
    }

    private static void handleWicket(Match match, Innings innings) {
        System.out.println("Wicket! " + innings.getStriker().getName() + " is out.");
        innings.getStriker().setOut(true);
        innings.addWicket();

        if (battingTeamIsAllOut(innings)) {
            inningComplete = true;
            System.out.println("All out!");
            if (match.isCompleted()) {
                return;
            }
            int target = match.getInnings1().getTotalRuns() + 1;
            System.out.println("\nTarget for Team 2: " + target);
            displayScorecard(innings);

            playInnings(match, match.getTeam2(), match.getInnings2(), target);
            System.out.println("\nThe match is completed.");
            System.out.println("\nFinal match details:");
            displayMatchDetails(match);
            finalScoreCardDisplay = true;
            return;

        } else {
            Player newBatsman = getNextBatsman(innings.getBattingTeam(), innings.getStriker(), innings.getNonStriker());
            innings.setStriker(newBatsman);
        }

        Ball ball = new Ball(0, innings.getStriker().getName());
        ball.setWicket(true);
        innings.addBall(ball);

        if (innings.getCurrentBall() % 6 == 0 && innings.getCurrentOver() != 0) {
            Player temp = innings.getStriker();
            innings.setStriker(innings.getNonStriker());
            innings.setNonStriker(temp);
        }
    }

    private static boolean battingTeamIsAllOut(Innings innings) {
        int notOutPlayers = 0;
        for (Player player : innings.getBattingTeam().getPlayers()) {
            if (!player.isOut()) {
                notOutPlayers++;
            }
        }
        return notOutPlayers < 2;
    }

    private static Player getPlayer(Team team, String prompt) {
        while (true) {
            System.out.print(prompt);
            String playerName = scanner.nextLine().trim();
            for (Player player : team.getPlayers()) {
                if (player.getName().equalsIgnoreCase(playerName)) {
                    return player;
                }
            }
            System.out.println("\nNo such player found. Please try again.");
        }
    }

    private static Player getNextBatsman(Team team, Player striker, Player nonStriker) {
        for (Player player : team.getPlayers()) {
            if (!player.equals(striker) && !player.equals(nonStriker) && !player.isOut()) {
                return player;
            }
        }
        System.out.println("\nAll out!");
        return null; 
    }

    private static void displayScorecard(Innings innings) {
        System.out.println("\nScorecard for " + innings.getBattingTeam().getName() + ":");
        System.out.println("Total Runs: " + innings.getTotalRuns());
        System.out.println("Wickets: " + innings.getTotalWickets());
        if(innings.getCurrentOver()!=0 && innings.getCurrentBall() % 6 == 0){
            System.out.println("Run Rate: " + (double)(innings.getTotalRuns()/(innings.getCurrentOver())));
        }
        System.out.println("Overs: " + innings.getCurrentOver() + "." + innings.getCurrentBall());
        System.out.println("Players:");

        for (Player player : innings.getBattingTeam().getPlayers()) {
            if (innings.getStriker() != null && innings.getStriker().getName().equals(player.getName())) {
                System.out.println(player.getName() + "(striker)" + " - " + player.getRuns() + " (" +
                        (player.isOut() ? "out" : "not out") + ")");
            } else if (innings.getNonStriker() != null && innings.getNonStriker().getName().equals(player.getName())) {
                System.out.println(player.getName() + "(non-striker)" + " - " + player.getRuns() + " (" +
                        (player.isOut() ? "out" : "not out") + ")");
            } else {
                System.out.println(player.getName() + " - " + player.getRuns() + " (" +
                        (player.isOut() ? "out" : "not out") + ")");
            }
        }
    }

    private static void saveMatch(Match match) {
        try {
            JSONUtils.writeMatchToJSON("data/match.txt", match);
            System.out.println("Match progress saved.");
        } catch (IOException e) {
            System.err.println("Error saving match progress: " + e.getMessage());
        }
    }

    private static void displayMatchDetails(Match match) {
        System.out.println(match);

        displayScorecard(match.getInnings1());
        displayScorecard(match.getInnings2());

        int team1Score = match.getInnings1().getTotalRuns();
        int team2Score = match.getInnings2().getTotalRuns();
        int team2Wickets = match.getInnings2().getTotalWickets();
        int totalPlayers = match.getTeam1().getPlayers().size();

        if (team1Score > team2Score) {
            int margin = team1Score - team2Score;
            System.out.println(match.getTeam1().getName() + " won by " + margin + " runs.");
        } else if (team2Score > team1Score) {
            int wicketsRemaining = totalPlayers - team2Wickets;
            System.out.println(match.getTeam2().getName() + " won by " + wicketsRemaining + " wickets.");
        } else {
            System.out.println("Match ended in a tie.");
        }
    }
}
