package player;

import java.util.*;

public class PlayersCreator {

    private static List<Player> players = new ArrayList<>();

    private final List<String> COMP_PLAYER_NAMES = List.of("Albot", "Bobot", "Cheesebot", "Databot", "Elizabot", "Fulbot");
    private int difficultyLevel;
    private int numOfPlayer;
    private Random rand;
    private Set<String> usedCompName;

    /**
     * private construcutor
     * @param numOfPlayer  the number of players in this game
     * @param humanPlayerName   the player name input by human player
     * @param difficultyLevel   1 - 3, the higher the difficulty level, the more intelligent the robots are
     */
    private PlayersCreator(int numOfPlayer, String humanPlayerName, int difficultyLevel) {
        this.numOfPlayer = numOfPlayer;
        this.difficultyLevel = difficultyLevel;
        usedCompName = new HashSet<>();

        // use a random number to see where the human player will be placed,
        // this order will remain the same in the game
        rand = new Random();
        int humanPos = rand.nextInt(numOfPlayer);

        int compNamePos;
        Player currPlayer;
        for (int i = 0; i < numOfPlayer; i++) {
            // insert human player in random position
            if (i == humanPos) {
                currPlayer = new Player(humanPlayerName);
            } else {
                // create computer players in other positions
                // randomly chosen robot name
                compNamePos = rand.nextInt(COMP_PLAYER_NAMES.size());
                String compName = COMP_PLAYER_NAMES.get(compNamePos);
                while (usedCompName.contains(compName)) {
                    compNamePos = rand.nextInt(COMP_PLAYER_NAMES.size());
                    compName = COMP_PLAYER_NAMES.get(compNamePos);
                }
                currPlayer = new ComputerPlayer(compName, difficultyLevel);
                usedCompName.add(compName);
            }
            // set player id
            currPlayer.setPlayerId("p" + i);
            players.add(currPlayer);
        }
    }

    public static void createPlayers(int numOfPlayer, String humanPlayerName, int difficultyLevel) {
        if (players.isEmpty()) {
            PlayersCreator creator = new PlayersCreator(numOfPlayer, humanPlayerName, difficultyLevel);
        }
    }

    public static List<Player> getPlayers() {
        return players;
    }




}
