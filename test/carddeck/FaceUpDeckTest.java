package carddeck;

import card.TrainCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is intended for testing the FaceUpDeck class, each test function should be called separately
 */
class FaceUpDeckTest {

    private FaceUpDeck faceUpDeck = FaceUpDeck.getObjectInstance();
    private List<TrainCard> cards = FaceUpDeck.getListInstance();
    private FaceDownDeck faceDownDeck = FaceDownDeck.getObjectInstance();

    @Test
    void getSize_beforeCreateFaceDown_returns0() {
        assertEquals(0, faceUpDeck.getSize());
    }

    @Test
    void getSize_afterCreateFaceDownBeforeFill_returns5() {
        faceDownDeck.createCardDeck();
        faceUpDeck.createCardDeck();
        assertEquals(0, faceUpDeck.getSize());
    }

    @Test
    void isEmpty_afterCreate_returnsFalse() {
        faceDownDeck.createCardDeck();
        assertFalse(faceUpDeck.isEmpty());
    }


    @Test
    void discard_emptyDeck_throwsIllegalStateException() {
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> faceUpDeck.discard(0));
        assertEquals("Cannot invoke discard, the face-up deck is not full.", t.getMessage());
    }

    @Test
    void discard_invalidIndex9_throwsIllegalArgumentException() {
        faceDownDeck.createCardDeck();
        faceUpDeck.createCardDeck();
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> faceUpDeck.discard(9));
        assertEquals("Invalid index, index needs to be in 0 - 4 range.", t.getMessage());
    }

    @Test
    void discard_2times_returnsLength3() {
        faceDownDeck.createCardDeck();
        faceUpDeck.createCardDeck();
        faceUpDeck.discard(0);
        faceUpDeck.discard(1);
        assertEquals(3, faceUpDeck.getSize());
    }

    @Test
    void discard_5times_returnsLength0() {
        faceDownDeck.createCardDeck();
        faceUpDeck.createCardDeck();
        for (int i = 0; i < 5; i++) {
            faceUpDeck.discard(i);
        }
        assertEquals(0, faceUpDeck.getSize());
    }

    @Test
    void discard_5times_afterRefill_returnsLength5() {
        faceDownDeck.createCardDeck();
        faceUpDeck.createCardDeck();
        for (int i = 0; i < 5; i++) {
            faceUpDeck.discard(i);
        }
        faceUpDeck.fillDeck();
        assertEquals(5, faceUpDeck.getSize());
    }

    @Test
    void discard_validIndex2_returnsLength4() {
        faceDownDeck.createCardDeck();
        faceUpDeck.createCardDeck();
        faceUpDeck.discard(2);
        assertEquals(4, faceUpDeck.getSize());
    }

    @Test
    void discard_5times_insufficientFaceDown_returnsLength0 () {
        faceDownDeck.createCardDeck();
        faceUpDeck.createCardDeck();
        // total 106 cards, exhaust the face-down deck
        for (int i = 0; i < 106; i++) {
            faceDownDeck.drawACard(0);
        }
        //
        for (int i = 0; i < 5; i++) {
            faceUpDeck.discard(i);
        }
        faceUpDeck.fillDeck();
        assertEquals(0, faceUpDeck.getSize());
    }


    @Test
    void drawACard_emptyDeckValidIndex_throwsIllegalStateException() {
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> faceUpDeck.drawACard(0));
        assertEquals("The card deck is empty, cannot draw a card.", t.getMessage());
    }

    @Test
    void drawACard_fullDeckInvalidIndex10_throwsIllegalArgumentException() {
        faceDownDeck.createCardDeck();
        faceUpDeck.fillDeck();
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> faceUpDeck.drawACard(10));
        assertEquals("Invalid index, index needs to be in 0 - 4 range.", t.getMessage());
    }

    @Test
    void drawACard_returnsLength5() {
        faceDownDeck.createCardDeck();
        faceUpDeck.createCardDeck();
        faceUpDeck.drawACard(3);
        assertEquals(5, faceUpDeck.getSize());
    }

    @Test
    void drawACard_5timesSamePosition_returnLength5() {
        faceDownDeck.createCardDeck();
        faceUpDeck.fillDeck();
        for (int i = 0; i < 5; i++) {
            faceUpDeck.drawACard(2);
        }
        assertEquals(5, faceUpDeck.getSize());
    }

    @Test
    void fillDeck_emptyFaceDownDeck_throwsIllegalStateException() {
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> faceUpDeck.fillDeck());
        assertEquals("Cannot fill deck, face-down deck is empty.", t.getMessage());
    }

    @Test
    void fillDeck_fullDeck_throwsIllegalStateException() {
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> faceUpDeck.fillDeck());
        assertEquals("The deck is full, no need to refill.", t.getMessage());
    }


}