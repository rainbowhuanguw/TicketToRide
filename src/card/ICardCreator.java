package card;

import java.util.HashSet;
import java.util.Set;

/**
 * This ICardCreator interface defines what colors are allowed to create cards and function(s) for creating a card
 */

public interface ICardCreator {
    /**
     * a set of colors in all small letters to define the nine allowed colors for train cards
     */
    Set<String> COLORS = Set.of("purple", "blue", "red", "yellow", "rainbow",
            "green", "black", "orange", "white");


    /**
     * create either a task card or a train card
     * @return ICard object
     */
    ICard createACard();
}
