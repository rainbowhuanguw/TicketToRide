package card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainCardTest {

    @Test
    void getCardType_returns2() {
        TrainCard trainCard = new TrainCard("purple");
        assertEquals(2, trainCard.getCardType());
    }


    @Test
    void getColor_returnsRed() {
        TrainCard trainCard = new TrainCard("red");
        assertEquals("red", trainCard.getColor());
    }

    @Test
    void setOwnerId_returnsCheese() {
        TrainCard trainCard = new TrainCard("orange");
        trainCard.setOwnerId("cheese");
        assertEquals("cheese", trainCard.getOwnerId());
    }

    @Test
    void setOwnerId_throwsIllegalArgument() {
        TrainCard trainCard = new TrainCard("blue");
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> trainCard.setOwnerId(null));
        assertEquals("The owner id cannot be null.", t.getMessage());
    }

    @Test
    void setOwnerId_twoTimes_throwsIllegalArgument() {
        TrainCard trainCard = new TrainCard("yellow");
        trainCard.setOwnerId("bobby");
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> trainCard.setOwnerId("teddy"));
        assertEquals("This card is owned by another player, cannot reset owner.", t.getMessage());
    }

    @Test
    void clearOwnerId_returnsNull() {
        TrainCard trainCard = new TrainCard("blue");
        trainCard.setOwnerId("lol");
        trainCard.clearOwnerId();
        assertNull(trainCard.getOwnerId());
    }

    @Test
    void getCardId_returnsNull() {
        TrainCard trainCard = new TrainCard("blue");
        assertNull(trainCard.getOwnerId());
    }

    @Test
    void getCardId_set32_returnsTrain32() {
        TrainCard trainCard = new TrainCard("blue");
        trainCard.setCardId(32);
        assertEquals("Train32", trainCard.getCardId());
    }

    @Test
    void setCardId_onAssignedCard_throwsIllegalStateException() {
        TrainCard trainCard = new TrainCard("black");
        trainCard.setCardId(20);
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> trainCard.setCardId(1));
        assertEquals("This card has been assigned card id, cannot reassign id.", t.getMessage());
    }


}