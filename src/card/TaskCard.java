package card;

import carddeck.TaskFileReader;
import city.City;

import java.util.Objects;

/**
 * The TaskCard class implements the ICard interface, represents a task card
 */
public class TaskCard implements ICard {

    /**
     * instance variables
     */
    private City city1;
    private City city2;
    private int points;
    private String ownerId;
    private String cardId;
    private boolean completed;


    /**
     * constructor
     * @param city1     a City Object of destination 1
     * @param city2     a City Object of destination 2
     * @param points    points players can gain after finish this task
     * @throws  IllegalArgumentException     if city1 and city 2 are the same
     * @throws  IllegalArgumentException     if points are 0 or negative
     */
    public TaskCard(City city1, City city2, int points) {
        if (city1.equals(city2) || city1.getCityName().equals(city2.getCityName())) {
            throw new IllegalArgumentException("Two destination cities cannot be the same.");
        }
        if (points <= 0) {
            throw new IllegalArgumentException("Points have to be positive integers.");
        }
        this.city1 = city1;
        this.city2 = city2;
        this.points = points;
        ownerId = null;
        cardId = null;
        completed = false;
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
        return TASK_CARD_CODE;
    }


    /**
     * get city 1
     * @return      first City entered in the card, the task has no direction, there is no indication of which city
     *              is the start or end
     * */
    public City getCity1() {
        return city1;
    }


    /**
     * get city 2
     * @return      first City entered in the card, the task has no direction, there is no indication of which city
     *              is the start or end
     */
    public City getCity2() {
        return city2;
    }

    public int getPoints() {
        return points;
    }


    /**
     * check if a task card is completed by a player or not
     * @return      true if a task has been completed, false otherwise
     */
    public boolean isCompleted() {
        return (ownerId != null && completed);
    }


    /**
     * set a task card's status to complete
     * @throws  IllegalStateException      when attempting to change status when no player owns this task card
     */
    public void setCompleted() {
        if (ownerId == null) {
            throw new IllegalStateException("This task card is not owned by any player, cannot be set to complete.");
        }
        completed = true;
    }


    /**
     * set owner id to a player, designed to be used in cardDeck
     * @throws  IllegalArgumentException     when the input ownerId is null
     * @throws  IllegalStateException        when the card has been set to a user before
     */
    @Override
    public void setOwnerId(String ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("The owner id cannot be null.");
        } else if (getOwnerId() != null) {
            throw new IllegalStateException("This card is owned by another player, cannot reset owner.");
        }
        this.ownerId = ownerId;
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
        this.cardId = "Task" + num;
    }



    /**
     * redefine equals(), only comparing four fields: city1, city2, cardId, points
     * @param otherCard     the other task card object being compared to
     * @return      true if two objects have same values in the four fields above, false otherwise
     * @throws      IllegalArgumentException     if the input object is null
     * @throws      IllegalArgumentException     if the input object is not a taskCard
     */
    @Override
    public boolean equals(ICard otherCard) {
        if (otherCard == null) {
            throw new IllegalArgumentException("Cannot compare to a null object.");
        }
        if (otherCard.getCardType() != TASK_CARD_CODE) {
            throw new IllegalArgumentException("The input object should be a task card, " +
                    "cannot compare to other types of cards.");
        }

        return equalsHelper((TaskCard) otherCard);


    }

    /**
     * helper function to be used in equals()
     * @param otherCard  the other card being compared to
     * @return  true if two cards are equal in the defined fields, false otherwise
     */
    private boolean equalsHelper(TaskCard otherCard) {
        return  getPoints() == otherCard.getPoints() &&
                // city1, city2 can be interchangeable
                ((getCity1().equals(otherCard.getCity1()) && getCity2().equals(otherCard.getCity2())) ||
                        (getCity1().equals(otherCard.getCity2()) && getCity2().equals(otherCard.getCity1())))
                && ((getCardId() == null && otherCard.getCardId() == null)
                || getCardId().equals(otherCard.getCardId()));
    }


    /**
     * compute hash function on the immutable fields, including city1, city2, cardId, points
     */
    public int hashcode() {
        return Objects.hash(city1, city2, cardId, points);
    }



}
