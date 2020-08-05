package carddeck;

import card.TrainCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DiscardsDeck implements ICardDeck, IShuffleable {

    private static List<TrainCard> discardsDeckList = new LinkedList<>();
    private static DiscardsDeck discardsDeckObject = new DiscardsDeck();

    /**
     * private constructor
     */
    private DiscardsDeck() {}

    @Override
    public void createCardDeck() { }

    /**
     * get the only DiscardsDeck object
     * @return  DiscardDeck object
     */
    public static DiscardsDeck getObjectInstance() {
        return discardsDeckObject;
    }

    /**
     * get the only DiscardsDeck list
     * @return  discardsDeck list
     */
    public static List<TrainCard> getListInstance() {
        return discardsDeckList;
    }


    @Override
    public int getSize() {
        return discardsDeckList.size();
    }


    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }


    /**
     * shuffling the deck, only when there are more than one cards
     */
    @Override
    public void shuffle() {
        if (getSize() > 1) {
            Collections.shuffle(discardsDeckList);
        }
    }

    /**
     * add the a list of discarded cards to discards deck,
     * @param cards a list of cards that come from either:
     *              (1) players played to claim routes
     *              (2) when there are 3 or more locomotive cards in the face-up deck, all 5 cards are discarded
     */
    public void addToDeck(List<TrainCard> cards) {
        discardsDeckList.addAll(cards);
    }


    /**
     * add a card to the discards deck, similar to addToDeck(List<TrainCard> cards)
     * @param card a given train card being added
     */
    public void addToDeck(TrainCard card) { discardsDeckList.add(card); }


    /**
     * remove all cards from the discards after pushing them to the face-down head
     */
    protected void clearDeck() {
        discardsDeckList.clear();
    }
}
