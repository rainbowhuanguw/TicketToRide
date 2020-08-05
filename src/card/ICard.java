package card;

/**
 * This class is the ICard interface, it defines what universal behaviors any card objects should have
 */
public interface ICard {

    /**
     * code to denote different types of cards
     */
    int TASK_CARD_CODE = 1;
    int TRAIN_CARD_CODE = 2;

    /**
     * get owner id of a card
     * @return      an owner id String
     */
    String getOwnerId();

    /**
     * get a unique card id
     * @return      a card id String
     */
    String getCardId();

    /**
     * get type of a card, whether it's a task card or train card
     * @return      1 for task card and 2 for train card
     */
    int getCardType();

    /**
     * set a card's owner id to input, meaning a card has been drawn and kept by a player
     * @param ownerId player id of the play who draw the card
     */
    void setOwnerId(String ownerId);

    /**
     * set a card with a unique id number, used in creators, eliminate repeated cards
     * @param num   current number of index when looping through and creating a deck
     */
    void setCardId(int num);

    /**
     * redefine equals function, exclude owner or other irrelevant info, only compare the original characteristics of
     * a card, e.g. color, id of train card; 2 destinations and points of task card
     * @param   otherCard     the other card being compared to
     * @return  true if two cards have the same values in the listed fields, false otherwise
     */
    boolean equals(ICard otherCard);

}
