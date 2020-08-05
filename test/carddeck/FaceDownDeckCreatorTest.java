package carddeck;

import card.ICard;
import card.TrainCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FaceDownDeckCreatorTest {


    private static FaceDownDeckCreator creator = FaceDownDeckCreator.getDeckCreatorObjectInstance();

    @Test
    void createCardDeck_returnsLength110() {
        creator.createCardDeck();
        List<TrainCard> cards = FaceDownDeckCreator.getCardDeckListInstance();
        assertEquals(110, cards.size());
    }

    /* note: this test will fail if running with the above test all at once, test separately */
    @Test
    void createCardDeckTwice_throwsIllegalStateException() {
        creator.createCardDeck();
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> creator.createCardDeck());
        assertEquals(t.getMessage(), "The card deck already exists, cannot create more than one face-down deck.");
    }

    /**
     * this test can only be invoked when getCountingMap() are made public in FaceDownCreator
     */
    /*
    @Test
    void createCardDeck_returnCorrectNumOfCardsForEachColor() {
        creator.createCardDeck();
        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("red", 12);
        expectedMap.put("yellow", 12);
        expectedMap.put("purple", 12);
        expectedMap.put("green", 12);
        expectedMap.put("blue", 12);
        expectedMap.put("black", 12);
        expectedMap.put("orange", 12);
        expectedMap.put("white", 12);
        expectedMap.put("rainbow", 14);
        assertEquals(expectedMap, creator.getCountingMap());
    }
     */


    @Test
    void getCardDeckCreatorInstance_returnsSameObject() {
        FaceDownDeckCreator creator2 = FaceDownDeckCreator.getDeckCreatorObjectInstance();
        assertSame(creator, creator2);
    }


    @Test
    void getCardDeckListInstance_returnsSameObject() {
        List<TrainCard> cards1 = FaceDownDeckCreator.getCardDeckListInstance();
        List<TrainCard> cards2 = FaceDownDeckCreator.getCardDeckListInstance();
        assertSame(cards1, cards2);
    }

}
