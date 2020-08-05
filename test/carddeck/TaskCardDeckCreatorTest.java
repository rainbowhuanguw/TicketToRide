package carddeck;

import card.ICard;
import card.TaskCard;
import card.TrainCard;
import city.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TaskCardDeckCreatorTest {

    TaskCardDeckCreator creator = TaskCardDeckCreator.getCreatorInstance();

    private List<Integer> RANGE = IntStream.range(0, 30).boxed().collect(Collectors.toList());


    @Test
    void shuffleRangeList_returnsLength30() {
        assertEquals(30, RANGE.size());
        creator.shuffledRange();
        assertEquals(30, RANGE.size());
    }


    @Test
    void createACard_index19_returnCorrectCard() {
        TaskCard actualCard = creator.createACard(19);
        assertEquals("Vancouver", actualCard.getCity1().getCityName());
        assertEquals("Santa Fe", actualCard.getCity2().getCityName());
        assertEquals(13, actualCard.getPoints());
    }

    @Test
    void createACard_index8_returnCorrectCard() {
        TaskCard actualCard = creator.createACard(8);
        assertEquals("Portland", actualCard.getCity1().getCityName());
        assertEquals("Phoenix", actualCard.getCity2().getCityName());
        assertEquals(11, actualCard.getPoints());
    }

    @Test
    void createACard_putOfBoundIndex_throwsIllegalArgumentException() {
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class,
                () -> creator.createACard(50));
        assertEquals("Invalid index. Indices should be between 0 and 30.", t.getMessage());
    }


    /**
     * create card deck tests need to be run separately, involves static methods
     */
    @Test
    void createCardDeck_returnsLength30() {
        creator.createCardDeck();
        List<TaskCard> taskCards = TaskCardDeckCreator.getTaskCardDeck();
        assertEquals(taskCards.size(), 30);
    }

    @Test
    void createCardDeck_3Times_returnSameObject() {
        creator.createCardDeck();
        List<TaskCard> taskCards1 = TaskCardDeckCreator.getTaskCardDeck();
        creator.createCardDeck();
        List<TaskCard> taskCards2 = TaskCardDeckCreator.getTaskCardDeck();
        creator.createCardDeck();
        List<TaskCard> taskCards3 = TaskCardDeckCreator.getTaskCardDeck();
        assertSame(taskCards1, taskCards2);
        assertSame(taskCards2, taskCards3);
    }


    /*
    // the following tests are only available when the respective functions are turned public for testing
    @Test
    void getTasksList_returnCorrectList() {
        creator.createCardDeck();
        List<ICard> taskCards = TaskCardDeckCreator.getTaskCardDeck();
        for (ICard taskCard : taskCards) {
            assertEquals(taskCard.);
        }
    }
     */


}