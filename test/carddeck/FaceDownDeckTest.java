package carddeck;

import card.TrainCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is the test file for the FaceDownDeck class, because these tests involve static methods and variables,
 * most of these tests need to be run separately.
 * date: July 13, 2020
 */
class FaceDownDeckTest {

    private FaceDownDeck faceDownDeck = FaceDownDeck.getObjectInstance();
    private DiscardsDeck discardsDeck = DiscardsDeck.getObjectInstance();

    @Test
    void getFaceDownDeckInstance_returnsSameObject() {
        FaceDownDeck faceDownDeck2 = FaceDownDeck.getObjectInstance();
        assertSame(faceDownDeck, faceDownDeck2);
    }

    @Test
    void getSize_BeforeCreate_returns0() {
        assertEquals(0, faceDownDeck.getSize());
    }


    @Test
    void getSize_AfterCreate_returns110() {
        faceDownDeck.createCardDeck();
        assertEquals(110, faceDownDeck.getSize());
    }

    @Test
    void isEmpty_beforeCreate_returnsTrue() {
        assertTrue(faceDownDeck.isEmpty());
    }

    @Test
    void isEmpty_afterCreate_returnsFalse() {
        faceDownDeck.createCardDeck();
        assertFalse(faceDownDeck.isEmpty());
    }

    @Test
    void drawACard_withInvalidIndex_throwsIllegalArgumentException() {
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, ()-> faceDownDeck.drawACard(-99));
        assertEquals("Invalid index, index can only be 0.", t.getMessage());
    }

    @Test
    void drawACard_withIndex0_returnsNotNullObject() {
        faceDownDeck.createCardDeck();
        TrainCard card = faceDownDeck.drawACard(0);
        assertNotNull(card);
    }

    @Test
    void drawACard_withIndex0_returnsLength109() {
        //faceDownDeck.createCardDeck();
        TrainCard card = faceDownDeck.drawACard(0);
        assertEquals(109, faceDownDeck.getSize());
    }

    @Test
    void fillDeck_withOneCardinDiscards_returnsLength1() {
        TrainCard trainCard = new TrainCard("blue");
        discardsDeck.addToDeck(trainCard);
        faceDownDeck.fillDeck();
        assertEquals(1, faceDownDeck.getSize());
    }

    @Test
    void fillDeck_withOneCardinDiscards_returnsColorBlue() {
        TrainCard trainCard = new TrainCard("blue");
        discardsDeck.addToDeck(trainCard);
        faceDownDeck.fillDeck();
        TrainCard card = faceDownDeck.drawACard(0);
        assertEquals("blue", card.getColor());
    }

    @Test
    void fillDeck_notEmptyDeck_throwsIllegalStateException() {
        //faceDownDeck.createCardDeck();
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> faceDownDeck.fillDeck());
        assertEquals("The deck is not empty, cannot refill.", t.getMessage());
    }

    @Test
    void fillDeck_emptyDiscardsDeck_throwsIllegalStateException() {
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> faceDownDeck.fillDeck());
        assertEquals("The discards deck is empty, cannot refill.", t.getMessage());
    }
}