package carddeck;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public interface ICardDeckCreator {

    int TOTAL_TRAIN_CARD_NUM = 110;
    int NON_RAINBOW_CARD_NUM_PER_COLOR = 12;
    int RAINBOW_CARD_NUM = 14;
    int TOTAL_TASK_CARD_NUM = 30;


    /**
     * create a collection of cards as card decks, can be used to create HeadsDownTrainCardDeck or TaskCardDeck
     */
    void createCardDeck();

    


}
