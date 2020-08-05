package card;

import java.util.List;

/**
 * This class defines the characters(city and color) of a train card and defines the train card behaviors
 */
public class TrainCard implements ICard {

    private String ownerId;
    private String cardId;
    private String color;

    public TrainCard(String color) {
        ownerId = null;
        cardId = null;
        this.color = color;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public String getCardId() {
        return cardId;
    }

    @Override
    public int getCardType() {
        return TRAIN_CARD_CODE;
    }


    /**
     * get color of a train card
     * @return      String color
     */
    public String getColor() {
        return color;
    }


    /**
     * set owner id to a player, invoke when a player draws and keeps the card
     * @param ownerId       the id of the player who draws the card
     * @throws  IllegalArgumentException     when the input id is null
     * @throws  IllegalArgumentException     when trying to set a card that is already owned by a player
     */
    @Override
    public void setOwnerId(String ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("The owner id cannot be null.");
        } else if (getOwnerId() != null){
            throw new IllegalStateException("This card is owned by another player, cannot reset owner.");
        }
        this.ownerId = ownerId;
    }


    /**
     * clear a train card's owner id, designed to be used when player choose to claim a route,
     * release the card to the discards card deck
     */
    public void clearOwnerId() {
        ownerId = null;
    }


    /**
     * assign id to a task card, used in taskDeckCreator
     * @param    num      the index of a card stored in the task deck list
     * @throws      IllegalStateException   when trying to reset card id on a card with card id
     */
    public void setCardId(int num) {
        if (cardId != null) {
            throw new IllegalStateException("This card has been assigned card id, cannot reassign id.");
        }
        this.cardId = "Train" + num;
    }

    @Override
    public boolean equals(ICard otherCard) {
        return false;
    }
}
