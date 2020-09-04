package player;

import carddeck.DeckManager;
import carddeck.FaceDownDeck;
import carddeck.FaceUpDeck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComputerPlayerTest {

    private ComputerPlayer player;
    private static FaceUpDeck faceUpDeck = FaceUpDeck.getObjectInstance();
    private static FaceDownDeck faceDownDeck = FaceDownDeck.getObjectInstance();
    private static DeckManager manager = DeckManager.getObjectInstance();


    public ComputerPlayerTest() {
        player = new ComputerPlayer("Testbot", 1);
        player.setPlayerId("comptest1");

    }


    @Test
    void drawTrainCard_1time_producesCorrectOutput() {
        manager.manageDecks();
        player.computerDrawTrainCard();
    }

    @Test
    void drawTrainCard_40times_producesCorrectOutput() {
        for (int i = 0; i < 60; i++) {
            manager.manageDecks();
            player.computerDrawTrainCard();
        }
    }

    @Test
    void generateCardIndices() {
    }
}