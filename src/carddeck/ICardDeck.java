package carddeck;

import card.ICard;

import java.util.List;

public interface ICardDeck {

    /**
     * get size of the card deck, how many cards are in the deck
     */
    int getSize();

    /**
     * check if the card deck is empty based on its size
     */
    boolean isEmpty();

    /**
     * instantiate card deck and fill up after first creation
     */
    void createCardDeck();

}
