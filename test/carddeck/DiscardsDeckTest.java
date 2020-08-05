package carddeck;

import card.TrainCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscardsDeckTest {

    DiscardsDeck discardsDeck = DiscardsDeck.getObjectInstance();

    @Test
    void getDiscardsDeckInstance_returnSameObject() {
    }

    @Test
    void getDiscardsDeckList() {
    }

    @Test
    void getSize_returns0() {
        assertEquals(0, discardsDeck.getSize());
    }

    @Test
    void isEmpty_returnsTrue() {
        assertTrue(discardsDeck.isEmpty());
    }

    @Test
    void getSize_added10Cards_returns10() {
        for (int i = 0; i < 10; i++) {
            discardsDeck.addToDeck(new TrainCard("green"));
        }
        assertEquals(10, discardsDeck.getSize());
    }

    @Test
    void isEmpty_added2Cards_returnFalse() {
        for (int i = 0; i < 2; i++) {
            discardsDeck.addToDeck(new TrainCard("red"));
        }
        assertFalse(discardsDeck.isEmpty());
    }


    @Test
    void shuffle() {

    }

    @Test
    void addToDeck() {
    }

    @Test
    void clearDeck() {
    }
}