package carddeck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskCardDeckTest {

    TaskCardDeck deck = TaskCardDeck.getObjectInstance();

    @Test
    void getTaskCardDeckObject() {

    }

    @Test
    void getSize_beforeCreateDeck_returns0() {
        assertEquals(0, deck.getSize());
    }

    @Test
    void getSize_afterCreateDeck_returns30() {
        deck.createCardDeck();
        assertEquals(30, deck.getSize());
    }

    @Test
    void isEmpty_beforeCreate_returnsTrue() {
        assertTrue(deck.isEmpty());
    }

    @Test
    void getSize_afterCreate_returns30() {
        deck.createCardDeck();
        assertFalse(deck.isEmpty());
    }

    @Test
    void discard_invalidIndex10_throwsIllegalArgumentException() {
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, ()->  deck.discard(10));
        assertEquals("Invalid index. Index should be between 0 to 3.", t.getMessage());
    }

    @Test
    void discard_emptyDeck_throwsIllegalStateException() {
        Throwable t = Assertions.assertThrows(IllegalStateException.class, ()->  deck.discard(1));
        assertEquals("Invalid index. Index should be between 0 to 3.", t.getMessage());
    }

    @Test
    void discard_invalidIndex_throwsIllegalStateException() {
        Throwable t = Assertions.assertThrows(IllegalStateException.class, ()->  deck.discard(1));
        assertEquals("Invalid index. Index should be between 0 to 3.", t.getMessage());
    }

    @Test
    void drawACard() {

    }

    @Test
    void shuffle() {
    }

    @Test
    void dealCards() {
    }
}