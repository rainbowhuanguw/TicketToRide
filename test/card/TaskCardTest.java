package card;

import city.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskCardTest {

    /**
     * some sample variables for testing
     */
    private City city1 = new City("Seattle");
    private City city2 = new City("Vancouver");
    private City city3 = new City("Los Angeles");
    private City city4 = new City("New York");

    @Test
    void constructTaskCard_dameCityObject_throwsIllegalArgumentException() {
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> new TaskCard(city1, city1, 3));
        assertEquals(t.getMessage(), "Two destination cities cannot be the same.");
    }

    @Test
    void constructTaskCard_differentCityObjectSameName_throwsIllegalArgumentException() {
        City city1_dup = new City("Seattle");
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> new TaskCard(city1, city1_dup, 3));
        assertEquals(t.getMessage(), "Two destination cities cannot be the same.");
    }

    @Test
    void constructTaskCard_negativePoints_throwsIllegalArgumentException() {
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> new TaskCard(city1, city2, -1));
        assertEquals(t.getMessage(), "Points have to be positive integers.");
    }

    @Test
    void getCardType_return1() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        assertEquals(defaultTaskCard.getCardType(), 1 );
    }

    @Test
    void getCity1_returnsCity1() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        assertEquals(defaultTaskCard.getCity1(), city1);
    }

    @Test
    void getCity2_returnsCity2() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        assertEquals(defaultTaskCard.getCity2(), city2);
    }

    @Test
    void getPoints_returns10() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        assertEquals(defaultTaskCard.getPoints(), 10);
    }

    @Test
    void isCompleted_returnsFalse() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        assertFalse(defaultTaskCard.isCompleted());
    }

    @Test
    void setCompleted_throwsIllegalStateException() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        Throwable t = Assertions.assertThrows(IllegalStateException.class, defaultTaskCard::setCompleted);
        assertEquals(t.getMessage(), "This task card is not owned by any player, cannot be set to complete.");
    }

    @Test
    void isCompleted_afterSettingOwnerAndComplete_returnsTrue() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        defaultTaskCard.setOwnerId("Jimmy");
        defaultTaskCard.setCompleted();
        assertTrue(defaultTaskCard.isCompleted());
    }

    @Test
    void getOwnerId_returnsNull() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        assertNull(defaultTaskCard.getOwnerId());
    }


    @Test
    void setOwnerId_returnsJane() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        defaultTaskCard.setOwnerId("Jane");
        assertEquals(defaultTaskCard.getOwnerId(), "Jane");
    }

    @Test
    void setOwnerId_onOccupiedCard_throwIllegalStateException() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        defaultTaskCard.setOwnerId("Mike");
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> defaultTaskCard.setOwnerId("Ben"));
        assertEquals(t.getMessage(), "This card is owned by another player, cannot reset owner.");
    }

    @Test
    void setOwnerId_withNull_throwsIllegalArgumentException() {
        TaskCard defaultTaskCard = new TaskCard(city1, city2, 10);
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> defaultTaskCard.setOwnerId(null));
        assertEquals(t.getMessage(), "The owner id cannot be null.");
    }

    @Test
    void equals_withoutId_returnsTrue() {
        TaskCard taskCard = new TaskCard(city1, city2, 10);
        TaskCard taskCard2 = new TaskCard(city1, city2, 10);
        assertTrue(taskCard.equals(taskCard2));
    }

    @Test
    void equals_withoutIdChangeDirection_returnsTrue() {
        TaskCard taskCard = new TaskCard(city1, city2, 10);
        TaskCard taskCard2 = new TaskCard(city2, city1, 10);
        assertTrue(taskCard.equals(taskCard2));
    }

    @Test
    void equals_withoutIdDifferentCity_returnFalse() {
        TaskCard taskCard = new TaskCard(city1, city2, 10);
        TaskCard taskCard2 = new TaskCard(city3, city2, 10);
        assertFalse(taskCard.equals(taskCard2));
    }

    @Test
    void equals_sameCardWithAndWithOutOwnerId_returnTrue() {
        TaskCard taskCard = new TaskCard(city1, city2, 10);
        taskCard.setOwnerId("Ann");
        TaskCard taskCard2 = new TaskCard(city1, city2, 10);
        assertTrue(taskCard.equals(taskCard2));
    }

    @Test
    void equals_differentCardsWithSameOwnerId_returnFalse() {
        TaskCard taskCard = new TaskCard(city1, city2, 10);
        taskCard.setOwnerId("Ann");
        TaskCard taskCard2 = new TaskCard(city3, city4, 10);
        taskCard2.setOwnerId("Ann");
        assertFalse(taskCard.equals(taskCard2));
    }

    @Test
    void setCardId_twoTimes_throwsIllegalStateException() {
        TaskCard taskCard = new TaskCard(city1, city2, 10);
        taskCard.setCardId(23);
        Throwable t = Assertions.assertThrows(IllegalStateException.class, () -> taskCard.setCardId(2));
        assertEquals(t.getMessage(), "This card has been assigned card id, cannot reassign id.");
    }

    @Test
    void setCardId_returnsTask10() {
        TaskCard taskCard = new TaskCard(city1, city2, 10);
        taskCard.setCardId(10);
        assertEquals(taskCard.getCardId(), "Task10");
    }

    @Test
    void getCardId_returnNull() {
        TaskCard taskCard = new TaskCard(city1, city2, 9);
        assertNull(taskCard.getCardId());
    }

}