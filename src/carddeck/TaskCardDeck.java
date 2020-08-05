package carddeck;

import card.ICard;
import card.TaskCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the task card deck, the deck has shuffle, draw card, and discard behaviors
 */
public class TaskCardDeck implements ICardDeck, IShuffleable, IDiscardable {

    private static List<TaskCard> taskCardDeckList = TaskCardDeckCreator.getTaskCardDeck();
    private static TaskCardDeck taskCardDeckObject = new TaskCardDeck();
    private static TaskCardDeckCreator creator = TaskCardDeckCreator.getCreatorInstance();

    // constants
    private final int DEAL_CARD_NUM = 3;

    // instance variable
    private List<TaskCard> dealt3Cards;

    /**
     * private constructor, uses Singleton pattern
     */
    private TaskCardDeck() {
        // initialize its length to be 3
        dealt3Cards = new LinkedList<>();
        for (int i = 0; i < DEAL_CARD_NUM; i++) {
            dealt3Cards.add(null);
        }
    }

    /**
     * call TaskCardDeckCreator.createCardDeck()
     */
    @Override
    public void createCardDeck() {
        creator.createCardDeck();
    }

    /**
     * get the only instance of task card deck
     * @return      task card deck object
     */
    public static TaskCardDeck getObjectInstance() {
        return taskCardDeckObject;
    }


    /**
     * the players can get/view 3 cards dealt to him/her
     */
    public List<TaskCard> getDealt3Cards() {
        return dealt3Cards;
    }


    /**
     * implements ICardDeck interface, get size of task card deck
     */
    @Override
    public int getSize() {
        return taskCardDeckList.size();
    }


    /**
     * implements ICardDeck interface, check if task card deck is empty
     */
    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }



    /**
     * implements IDsicardable interface, used with drawACard() and dealCards()
     * the task card being discarded will be put back to the bottom of the task card deck
     * @param index will always be 0 - 2, discarding behaviors are only available on the top 3 cards
     */
    @Override
    public void discard(int index) {
        if (dealt3Cards.isEmpty()) {
            throw new IllegalStateException("No card has been dealt, cannot discard a card.");
        }
        if (index >= DEAL_CARD_NUM || index < 0) {
            throw new IllegalArgumentException("Invalid index. Index should be between 0 to " + DEAL_CARD_NUM + ".");
        }
        if (index >= getSize()) {
            throw new IndexOutOfBoundsException("Invalid index. Input index number is larger than size.");
        }
        taskCardDeckList.add(dealt3Cards.get(index));    // add unselected card to bottom
        dealt3Cards.set(index, null);                    // set index to null
    }


    /**
     * implements IShuffleable interface, used only when task card deck is first created
     */
    @Override
    public void shuffle() {
        Collections.shuffle(taskCardDeckList);
    }


    /**
     * deal 3 cards from the deck top to players
     */
    public void dealCards() {
        for (TaskCard card : dealt3Cards) {
            if (card != null)
            throw new IllegalStateException("Still exist cards in dealt cards, cannot deal more cards.");
        }
        for (int i = 0; i < DEAL_CARD_NUM; i++) {
            dealt3Cards.set(i, taskCardDeckList.remove(0));
        }
    }

    public void displayTaskCards(List<TaskCard> taskCards) {
        for (int i = 0; i< taskCards.size(); i++) {
            System.out.println("index: " + i);
            TaskCard card = taskCards.get(i);
            System.out.println("Task: " + card.getCity1() + " - " + card.getCity2() + ", points: " + card.getPoints());
        }
    }


}
