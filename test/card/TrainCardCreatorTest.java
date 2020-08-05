package card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrainCardCreatorTest {
    TrainCardCreator creator = TrainCardCreator.getCreatorInstance();
    Set<String> COLORS = Set.of("purple", "blue", "red", "yellow", "rainbow",
            "green", "black", "orange", "white");

    @Test
    void createACardOfColorRed_returnCorrectColor() {
        TrainCard actualCard = creator.createACard("red");
        TrainCard expectedCard = new TrainCard("red");
        assertEquals(expectedCard.getColor(), actualCard.getColor());
    }

    @Test
    void createACardOfColorRainbow_returnCorrectColor() {
        TrainCard actualCard = creator.createACard("rainbow");
        TrainCard expectedCard = new TrainCard("rainbow");
        assertEquals(expectedCard.getColor(), actualCard.getColor());
    }

    @Test
    void createACardOfColorBlue_returnCorrectColor() {
        TrainCard actualCard = creator.createACard("blue");
        TrainCard expectedCard = new TrainCard("blue");
        assertEquals(expectedCard.getColor(), actualCard.getColor());
    }

    @Test
    void createACardOfColorGreen_returnCorrectColor() {
        TrainCard actualCard = creator.createACard("green");
        TrainCard expectedCard = new TrainCard("green");
        assertEquals(expectedCard.getColor(), actualCard.getColor());
    }

    @Test
    void createACardOfColorRandom_returnAnAllowedColor() {
        TrainCard randomColorCard = creator.createACard();
        assert(COLORS.contains(randomColorCard.getColor()));
    }

    @Test
    void createThreeCardsOfColorRandom_returnAllowedColors() {
        for (int i = 0; i < 3; i++) {
            TrainCard randomColorCard = creator.createACard();
            assert(COLORS.contains(randomColorCard.getColor()));
        }
    }

    @Test
    void createACardOfExcludedColor_throwCorrectException() {
        creator.excludeColor("yellow");
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> creator.createACard("yellow"));
        assertEquals("This color has reached maximum number, try another color.", t.getMessage());
    }

    @Test
    void createACardOfNonExistingColor_throwCorrectException() {
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> creator.createACard("unicorn"));
        assertEquals("Invalid color, cannot initialize a train card of color unicorn.", t.getMessage());
    }

    @Test
    void excludeColorMultipleTimes_throwCorrectException() {
        creator.excludeColor("red");
        creator.excludeColor("green");
        creator.excludeColor("purple");
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> creator.excludeColor("red"));
        assertEquals("This color has already been excluded.", t.getMessage());
    }

    @Test
    void excludeNonExistingColor_throwCorrectException() {
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> creator.excludeColor("blah"));
        assertEquals("Invalid color, cannot exclude color blah.", t.getMessage());
    }

}