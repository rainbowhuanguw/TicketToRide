package player;

import card.TaskCard;
import card.TrainCard;
import carddeck.FaceDownDeck;
import carddeck.FaceUpDeck;
import carddeck.TaskCardDeck;
import city.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import route.Route;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private static FaceUpDeck faceUpDeckObject = FaceUpDeck.getObjectInstance();
    private static List<TrainCard> faceUpDeckList = FaceUpDeck.getListInstance();
    private static FaceDownDeck faceDownDeckObject = FaceDownDeck.getObjectInstance();
    private static TaskCardDeck taskCardDeck = TaskCardDeck.getObjectInstance();

    private Player testPlayer;
    private Route<City> SFLARoute;
    private Route<City> SEVARoute;

    public PlayerTest() {
        testPlayer = new Player("test");
        testPlayer.setPlayerId("t1");
        faceDownDeckObject.createCardDeck();
        faceUpDeckObject.fillDeck();
        taskCardDeck.createCardDeck();

        SEVARoute = new Route<>( new City("Seattle"), new City("Vancouver"),
                 List.of("grey", "grey"), 1);
        SFLARoute = new Route<>( new City("San Francisco"), new City("Los Angeles"),
                List.of("purple", "yellow"), 3);
    }

    /* ----------------------------------- getter methods -------------------------------------- */
    @Test
    void getPlayerName_returnsTest() {
        assertEquals("test", testPlayer.getPlayerName());
    }

    @Test
    void getPlayerId_returnsNull() {
        assertNull(testPlayer.getPlayerId());
    }

    @Test
    void getPoints_returns0() {
        assertEquals(0, testPlayer.getPoints());
    }

    @Test
    void getNumOfTrainLeft_returns45() {
        assertEquals(45, testPlayer.getNumOfTrainLeft());
    }

    @Test
    void getNumOfTrainCard_returns0() {
        assertEquals(0, testPlayer.getNumOfTrainCard());
    }

    @Test
    void getNumOfTaskCard_returns0() {
        assertEquals(0, testPlayer.getNumOfTaskCard());
    }

    /* ----------------------------------- claim a route -------------------------------------- */
    // the following test is available when drawAFaceUpCard() returns TrainCard
    /*
    @Test
    void getCardCombinationOnATargetRoute_draw2FaceUpCards_greyColor1CarRoute_returnSameLength() {
        TrainCard card1 = testPlayer.drawAFaceUpCard(2);
        TrainCard card2 = testPlayer.drawAFaceUpCard(3);
        // set up dictionary to calculate combinations
        Map<String, Integer> map = new HashMap<>();
        map.put(card1.getColor(), 1);
        if (card2.getColor().equals(card1.getColor())) {
            map.put(card1.getColor(), 2);
        } else {
            map.put(card2.getColor(), 1);
        }

        List<Map<String, Integer>> list = testPlayer.getCardCombinationOnATargetRoute(SEVARoute, "grey");

        // for 1-car long route, how many cards should equal to how many combinations
        assertEquals(map.keySet().size(), list.size());
    }
     */
    @Test
    void getCardCombinationOnATargetRoute_noCards_throwsIllegalStateException() {

        Throwable t = Assertions.assertThrows(IllegalStateException.class,
                () -> testPlayer.getCardCombinationOnATargetRoute(SEVARoute, "grey"));
        assertEquals("Cannot claim this route, doesn't have enough cards.", t.getMessage());
    }

    @Test
    void getCardCombinationOnATargetRoute_noSuchColor_throwsIllegalStateException() {
        for (int i = 0; i < 3; i++) {
            testPlayer.addATrainCard(new TrainCard("yellow"));
            testPlayer.addATrainCard(new TrainCard("purple"));
            testPlayer.addATrainCard(new TrainCard("rainbow"));
        }

        Throwable t = Assertions.assertThrows(IllegalArgumentException.class,
                () -> testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "blue"));
        assertEquals("Doesn't have this color.", t.getMessage());
    }

    /**
     * this test is only available when Player.addATrainCard() is made public
     */
    @Test
    void getCardCombinationOnATargetRoute_returnsSize4() {
        // manually add cards
        for (int i = 0; i < 3; i++) {
            testPlayer.addATrainCard(new TrainCard("yellow"));
            testPlayer.addATrainCard(new TrainCard("purple"));
            testPlayer.addATrainCard(new TrainCard("rainbow"));
        }

        List<Map<String, Integer>> list = testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "purple");

        // for 1-car long route, how many cards should equal to how many combinations
        assertEquals(4, list.size());
    }


    @Test
    void chooseACombination_returnsSameMap() {
        // manually add cards
        for (int i = 0; i < 3; i++) {
            testPlayer.addATrainCard(new TrainCard("yellow"));
            testPlayer.addATrainCard(new TrainCard("purple"));
            testPlayer.addATrainCard(new TrainCard("rainbow"));
        }

        List<Map<String, Integer>> list = testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "purple");
        // 0: r3, 1: r2 + 1p, 2: r1 + p2, 3: p3

        Map<String, Integer> expectedMap = Map.of("purple", 2, "rainbow", 1);
        testPlayer.chooseACombination(SFLARoute,"purple",  list,2);
        // for 1-car long route, how many cards should equal to how many combinations
        assertEquals(expectedMap, list.get(2));
    }

    @Test
    void chooseACombination_returnsOwnedRouteSize1() {
        // manually add cards
        for (int i = 0; i < 3; i++) {
            testPlayer.addATrainCard(new TrainCard("yellow"));
            testPlayer.addATrainCard(new TrainCard("purple"));
            testPlayer.addATrainCard(new TrainCard("rainbow"));
        }

        List<Map<String, Integer>> list = testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "purple");
        // 0: r3, 1: r2 + 1p, 2: r1 + p2, 3: p3

        testPlayer.chooseACombination(SFLARoute,"purple",  list,2);
        assertEquals(1, testPlayer.getOwnedRoutes().size());
    }

    @Test
    void chooseACombination_returnsOwnedTrainCardsSize6() {
        for (int i = 0; i < 3; i++) {
            testPlayer.addATrainCard(new TrainCard("yellow"));
            testPlayer.addATrainCard(new TrainCard("purple"));
            testPlayer.addATrainCard(new TrainCard("rainbow"));
        }

        List<Map<String, Integer>> list = testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "purple");
        // 0: r3, 1: r2 + 1p, 2: r1 + p2, 3: p3

        testPlayer.chooseACombination(SFLARoute,"purple",  list,2);
        // for 1-car long route, how many cards should equal to how many combinations
        assertEquals(6, testPlayer.getNumOfTrainCard());
    }

    @Test
    void chooseACombination_returnsPoints4() {
        for (int i = 0; i < 3; i++) {
            testPlayer.addATrainCard(new TrainCard("yellow"));
            testPlayer.addATrainCard(new TrainCard("purple"));
            testPlayer.addATrainCard(new TrainCard("rainbow"));
        }

        List<Map<String, Integer>> list = testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "purple");
        // 0: r3, 1: r2 + 1p, 2: r1 + p2, 3: p3

        testPlayer.chooseACombination(SFLARoute,"purple",  list,2);
        // for 1-car long route, how many cards should equal to how many combinations
        assertEquals(4, testPlayer.getPoints());
    }

    @Test
    void chooseACombination__claimParallelRoute_throwsIllegalStateException() {
        for (int i = 0; i < 3; i++) {
            testPlayer.addATrainCard(new TrainCard("yellow"));
            testPlayer.addATrainCard(new TrainCard("purple"));
            testPlayer.addATrainCard(new TrainCard("rainbow"));
        }

        List<Map<String, Integer>> list = testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "yellow");

        Throwable t = Assertions.assertThrows(IllegalArgumentException.class,
                () -> testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "purple"));
        assertEquals("Doesn't have this color.", t.getMessage());
    }

    @Test
    void chooseACombination_finishesATask() {
        for (int i = 0; i < 3; i++) {
            testPlayer.addATrainCard(new TrainCard("yellow"));
            testPlayer.addATrainCard(new TrainCard("purple"));
            testPlayer.addATrainCard(new TrainCard("rainbow"));
        }

        //TaskCard card = new TaskCard();
        List<Map<String, Integer>> list = testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "yellow");

        Throwable t = Assertions.assertThrows(IllegalArgumentException.class,
                () -> testPlayer.getCardCombinationOnATargetRoute(SFLARoute, "purple"));
        assertEquals("Doesn't have this color.", t.getMessage());
    }

    /* ----------------------------------- draw a face-up train card ------------------------------------ */
    /**
     * the following 3 tests are only available when the drawAFaceUpCard() returns TrainCard
     */
    /*
    @Test
    void drawAFaceUpCard_index2_returnsSameColor() {
        TrainCard card_index2 = faceUpDeckList.get(2);
        TrainCard drawn_index2 = testPlayer.drawAFaceUpCard(2);
        assertEquals(card_index2.getColor(), drawn_index2.getColor());
    }

    @Test
    void drawAFaceUpCard_returnsOwnerIdt1() {
        TrainCard drawn_index2 = testPlayer.drawAFaceUpCard(2);
        assertEquals("t1", drawn_index2.getOwnerId());
    }

    @Test
    void drawAFaceUpCard_returnsOwnedTrainCardSize1() {
        TrainCard drawn_index2 = testPlayer.drawAFaceUpCard(2);
        assertEquals(1, testPlayer.getNumOfTrainCard());
    }
     */

    /* ------------------------------------ draw a face-down card ---------------------------------------- */
    @Test
    void drawAFaceDownCard_returnsOwnedTrainCardSize1() {
        testPlayer.drawAFaceDownCard();
        assertEquals(1, testPlayer.getNumOfTrainCard());
    }

    @Test
    void drawAFaceDownCard_Twice_returnsOwnedTrainCardSize2() {
        for (int i = 0; i < 2; i++) {
            testPlayer.drawAFaceDownCard();
        }
        assertEquals(2, testPlayer.getNumOfTrainCard());
    }

    /* ------------------------------------ draw task cards ---------------------------------------- */
    @Test
    void drawTaskCards_returnsSize3() {
        List<TaskCard> cards = testPlayer.drawTaskCards();
        assertEquals(3, cards.size());
    }

    @Test
    void chooseTaskCards_choose1AtBeginning_throwsIllegalArgumentException() {
        List<TaskCard> cards = testPlayer.drawTaskCards();
        Set<Integer> chosenIndices = Set.of(1);
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () ->
                testPlayer.chooseTaskCards(cards, chosenIndices));
        assertEquals("The player needs to choose at least 2 task cards in this turn.", t.getMessage());
    }

    @Test
    void chooseTaskCards_choose2AtBeginning_returnsOwnedTaskCardSize2() {
        List<TaskCard> cards = testPlayer.drawTaskCards();
        Set<Integer> chosenIndices = Set.of(1, 2);
        testPlayer.chooseTaskCards(cards, chosenIndices);
        assertEquals(2, testPlayer.getNumOfTaskCard());
    }

    @Test
    void chooseTaskCards_choose0InMiddleGame_throwsIllegalArgumentException() {
        List<TaskCard> cardsFirstTime = testPlayer.drawTaskCards();
        Set<Integer> chosenIndicesFirstTime = Set.of(1, 2);
        testPlayer.chooseTaskCards(cardsFirstTime, chosenIndicesFirstTime);

        List<TaskCard> cardsSecondTime = testPlayer.drawTaskCards();
        Set<Integer> chosenIndicesSecondTime = new HashSet<>();
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () ->
                testPlayer.chooseTaskCards(cardsSecondTime, chosenIndicesSecondTime));
        assertEquals("The player needs to choose at least 1 task card(s) in this turn.", t.getMessage());
    }

    @Test
    void chooseTaskCards_TwoTimes_returnsSize5() {
        List<TaskCard> cardsFirstTime = testPlayer.drawTaskCards();
        Set<Integer> chosenIndicesFirstTime = Set.of(0, 1, 2);
        testPlayer.chooseTaskCards(cardsFirstTime, chosenIndicesFirstTime);

        List<TaskCard> cardsSecondTime = testPlayer.drawTaskCards();
        Set<Integer> chosenIndicesSecondTime = Set.of(0, 2);
        testPlayer.chooseTaskCards(cardsSecondTime, chosenIndicesSecondTime);
        assertEquals(5, testPlayer.getNumOfTaskCard());
    }

    @Test
    void chooseTaskCards_ThreeTimes_returnsSize6() {
        List<TaskCard> cardsFirstTime = testPlayer.drawTaskCards();
        Set<Integer> chosenIndicesFirstTime = Set.of(0, 1);
        testPlayer.chooseTaskCards(cardsFirstTime, chosenIndicesFirstTime);

        List<TaskCard> cardsSecondTime = testPlayer.drawTaskCards();
        Set<Integer> chosenIndicesSecondTime = Set.of(0);
        testPlayer.chooseTaskCards(cardsSecondTime, chosenIndicesSecondTime);

        List<TaskCard> cardsThirdTime = testPlayer.drawTaskCards();
        Set<Integer> chosenIndicesThirdTime = Set.of(0,1,2);
        testPlayer.chooseTaskCards(cardsThirdTime, chosenIndicesThirdTime);
        assertEquals(6, testPlayer.getNumOfTaskCard());
    }



}