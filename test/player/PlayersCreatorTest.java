package player;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayersCreatorTest {

    private static List<Player> players = PlayersCreator.getPlayers();

    @Test
    void getPlayers_beforeCreate_returns0() {
        assertEquals(0, players.size());
    }

    @Test
    void getPlayers_afterCreate_returns3() {
        int numOfPlayer = 3;
        PlayersCreator.createPlayers(numOfPlayer, "Molly", 2);
        assertEquals(numOfPlayer, players.size());
    }


}