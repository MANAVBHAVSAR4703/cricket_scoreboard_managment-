package com.example.util;

import com.example.model.Ball;
import com.example.model.Match;
import com.example.model.Player;
import com.example.model.Team;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static Match readMatchFromJSON(String filePath) throws IOException {
        String json = FileUtils.readFileAsString(filePath);
        return parseMatchFromJSON(json);
    }

    public static void writeMatchToJSON(String filePath, Match match) throws IOException {
        String json = generateJSONFromMatch(match);
        FileUtils.writeStringToFile(filePath, json);
    }

    private static Match parseMatchFromJSON(String json) {
        String[] lines = json.split("\n");
        String venue = lines[0].split(":")[1].trim();
        String date = lines[1].split(":")[1].trim();
        int numOvers = Integer.parseInt(lines[2].split(":")[1].trim());
        Team team1 = parseTeamFromJSON(lines[3]);
        Team team2 = parseTeamFromJSON(lines[4]);
        List<Ball> balls = new ArrayList<>();
        for (int i = 5; i < lines.length; i++) {
            balls.add(parseBallFromJSON(lines[i]));
        }
        Match match = new Match(venue, date, numOvers, team1, team2);
        for (Ball ball : balls) {
            match.addBall(ball);
        }
        return match;
    }

    private static Team parseTeamFromJSON(String json) {
        String[] parts = json.split(":");
        String teamName = parts[0].trim();
        String[] playersArray = parts[1].split(",");
        List<Player> players = new ArrayList<>();
        for (String player : playersArray) {
            players.add(new Player(player.trim()));
        }
        return new Team(teamName, players);
    }

    private static Ball parseBallFromJSON(String json) {
        String[] parts = json.split(",");
        int runs = Integer.parseInt(parts[0].split(":")[1].trim());
        String playerName = parts[1].split(":")[1].trim();
        return new Ball(runs, playerName);
    }

    private static String generateJSONFromMatch(Match match) {
        StringBuilder sb = new StringBuilder();
        sb.append("Venue: ").append(match.getVenue()).append("\n");
        sb.append("Date: ").append(match.getDate()).append("\n");
        sb.append("Overs: ").append(match.getNumOvers()).append("\n");
        sb.append(generateJSONFromTeam(match.getTeam1())).append("\n");
        sb.append(generateJSONFromTeam(match.getTeam2())).append("\n");
        for (Ball ball : match.getBalls()) {
            sb.append(generateJSONFromBall(ball)).append("\n");
        }
        return sb.toString();
    }

    private static String generateJSONFromTeam(Team team) {
        StringBuilder sb = new StringBuilder();
        sb.append(team.getName()).append(": ");
        List<Player> players = team.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            sb.append(players.get(i).getName());
            if (i < players.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private static String generateJSONFromBall(Ball ball) {
        return "Runs: " + ball.getRuns() + ", Player: " + ball.getPlayerName();
    }
}
