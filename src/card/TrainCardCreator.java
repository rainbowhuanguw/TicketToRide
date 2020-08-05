package card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrainCardCreator implements ICardCreator {

    private static TrainCardCreator creator = new TrainCardCreator();

    // instance variables
    private Random rand;
    private List<String> availableColors;

    /**
     * private constructor
     */
    private TrainCardCreator() {
        rand  = new Random();
        availableColors = new ArrayList<>();
        availableColors.addAll(COLORS);
    }

    /**
     * return the only train card creator instance
     * @return train card creator
     */
    public static TrainCardCreator getCreatorInstance() {
        return creator;
    }


    /**
     * create a train card of specific color
     * @param color     a String representation of a color the card will be in
     * @return  a train card object of specific color
     * @throws  IllegalArgumentException     when this color is invalid, see COLORS in ICardCreator
     * @throws  IllegalArgumentException     when number of cards in this color has reached the maximum limit
     */
    public TrainCard createACard(String color) {
        color = color.toLowerCase();
        if (!COLORS.contains(color)) {
            throw new IllegalArgumentException( "Invalid color, cannot initialize a train card of color " + color + "." );
        } else if (!availableColors.contains(color)) {
            throw new IllegalArgumentException( "This color has reached maximum number, try another color." );
        }
        return new TrainCard(color);
    }


    /**
     * create a card of random color
     * @return  a train card object of any of the available colors
     */
    @Override
    public TrainCard createACard() {
        if (availableColors.size() <= 0) {
            throw new IllegalStateException("Cannot create more cards.");
        }
        int randIndexOfColor = rand.nextInt(availableColors.size());
        String randColor = availableColors.get(randIndexOfColor);
        return this.createACard(randColor);

    }


    /**
     * exclude a color when the number of cards of a specific color reaches max num
     * @param color     a String representation of a color in which the number of color has reached the maximum limit
     * @throws  IllegalArgumentException        when this color is invalid, see COLORS in ICardCreator
     * @throws  IllegalArgumentException        when this color is not available/ has been excluded
     */
    public void excludeColor(String color) {
        color = color.toLowerCase();
        if (!COLORS.contains(color)) {
            throw new IllegalArgumentException("Invalid color, cannot exclude color " + color + ".");
        } else if (!availableColors.contains(color)) {
            throw new IllegalArgumentException("This color has already been excluded.");
        }
        availableColors.remove(color);
    }

    /**
     * check if there are colors available to create train cards
     * @return true if all cards are created, no color is available, false otherwise
     */
    public boolean hasAvailableColors() {
        return availableColors.size() > 0;
    }

}
