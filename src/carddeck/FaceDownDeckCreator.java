package carddeck;

import card.ICard;
import card.TrainCard;
import card.TrainCardCreator;
import java.util.*;

/**
 * This class is responsible for creating the only heads down train card deck, implements ICardDeckCreator interface,
 * @author rainbowhuang
 */
public class FaceDownDeckCreator implements ICardDeckCreator {

    // enable only one instance of this class
    private static List<TrainCard> faceDownDeck = new LinkedList<>();
    private static FaceDownDeckCreator deckCreator = new FaceDownDeckCreator();
    private static TrainCardCreator trainCardCreator =  TrainCardCreator.getCreatorInstance();

    // class variables
    private Map<String, Integer> countingMap;
    private boolean existed;

    /**
     * private constructor
     */
    private FaceDownDeckCreator() {
        countingMap = new HashMap<>();
        existed = false;
    }

    /**
     * create 110 train cards and put them into deck for the first time after creation
     * @throws      IllegalStateException       when this function is called more than once
     */
    @Override
    public void createCardDeck() {
        if (existed) {
            throw new IllegalStateException("The card deck already exists, cannot create more than one face-down deck.");
        }
        for (int i = 0; i < TOTAL_TRAIN_CARD_NUM; i++) {
            TrainCard card = createACard();
            card.setCardId(i);
            faceDownDeck.add(card);
        }
        setExisted();
    }

    /**
     * get the only instance of face-down deck
     * @return  all the face-down card in a list
     */
    public static List<TrainCard> getCardDeckListInstance() {
        return faceDownDeck;
    }

    /**
     * get the only instance of face-down deck creator
     * @return  the face-down deck creator object
     */
    public static FaceDownDeckCreator getDeckCreatorObjectInstance() {
        return deckCreator;
    }


    /**
     * create one Train card at a time using TrainCardCreator, a helper function to be used in createCardDeck(),
     * use a counting map to memorize whether a color has reached limit
     * @return  a train card of random color, will be added to the deck in createCardDeck()
     */
    private TrainCard createACard() {
        TrainCard trainCard = trainCardCreator.createACard();
        String color = trainCard.getColor();
        if (!countingMap.containsKey(color)) {
            countingMap.put(color, 0);
        }

        // within limit, update map
        if ((!color.equals("rainbow") && countingMap.get(color) < NON_RAINBOW_CARD_NUM_PER_COLOR) ||
                (color.equals("rainbow") && countingMap.get(color) < RAINBOW_CARD_NUM)) {
            countingMap.put(color, countingMap.get(color) + 1);
        } else {
            // reach or exceed limit, exclude this color, create another random card, repeat the process
            while ((trainCardCreator.hasAvailableColors()) &&
                    (!color.equals("rainbow") && countingMap.get(color) >= NON_RAINBOW_CARD_NUM_PER_COLOR) ||
                    (color.equals("rainbow") && countingMap.get(color) >= RAINBOW_CARD_NUM)) {
                trainCardCreator.excludeColor(color);
                trainCard = trainCardCreator.createACard();
                color = trainCard.getColor();
                }
            // keep counting
            if (trainCardCreator.hasAvailableColors()) {
                countingMap.put(color, countingMap.get(color) + 1);
            }
        }
        return trainCard;
    }

    /**
     * set existed to true after creating deck for the first time
     */
    private void setExisted() {
        existed = true;
    }


    /**
     * for testing only, make public if invoking createCardDeck_returnCorrectNumOfCardsForEachColor()
     * in FaceDownDeckCreatorTest
     * @return  a hashmap using color as key and the times of their occurrence as values
     */
    private Map<String, Integer> getCountingMap() {
        return countingMap;
    }

}