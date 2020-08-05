package carddeck;

import card.TaskCard;
import city.City;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskCardDeckCreator implements ICardDeckCreator {

    // class variables
    private static List<TaskCard> taskCardDeck = new LinkedList<>();
    private static TaskCardDeckCreator creator = new TaskCardDeckCreator();
    private static List<String> tasks = TaskFileReader.getTasks();

    // constants
    private static List<Integer> RANGE = IntStream.range(0, TOTAL_TASK_CARD_NUM).boxed().collect(Collectors.toList());

    // instance variables
    private Set<Integer> usedIds = new HashSet<>();
    private boolean existed = false;

    /**
     * private constructor
     */
    private TaskCardDeckCreator() {
    }


    /**
     * only instantiate task card deck for the first time, when it's empty, it's uncommon during game the task card
     * deck becomes empty
     * @throws      IllegalStateException   when trying to create card deck more than once
     */
    @Override
    public void createCardDeck() {
        if (isExisted()) {
            throw new IllegalStateException("Deck exists, cannot create deck.");
        }
        shuffledRange();
        for (int i : RANGE) {
            TaskCard currCard = createACard(i);
            setTaskCardId(i, currCard);
            taskCardDeck.add(currCard);
        }
        setExisted();
    }


    /**
     * provides public access to the only instance of task card deck
     * @return  the only instance of a list of ICard
     */
    public static List<TaskCard> getTaskCardDeck() {
        return taskCardDeck;
    }


    /**
     * get the only instance of TaskCardDeckCreator instance
     */
    public static TaskCardDeckCreator getCreatorInstance() {
        return creator;
    }


    /**
     * create a task card given the random index, used in createdCardDeck() to get a string from tasks list
     * @param randomRangeNum: a non repeating index number between 0(inclusive) to 30(exclusive)
     */
    public TaskCard createACard(int randomRangeNum) {
        if (randomRangeNum < 0 || randomRangeNum >= TOTAL_TASK_CARD_NUM) {
            throw new IllegalArgumentException( "Invalid index. Indices should be between 0 and 30." );
        }
        String taskString = tasks.get(randomRangeNum);
        return convertStringToTaskCard(taskString);
    }


    /**
     * shuffle a list of numbers to get non-repeating random number within a range, intended for shuffling indices
     * used in createCardDeck()
     */
    public void shuffledRange() {
        Collections.shuffle(RANGE);
    }

    public List<Integer> getRange() {
        return RANGE;
    }


    /**
     * parse a string of format "cityName1|cityName2|points" to a taskCard object, used in createACard
     */
    private TaskCard convertStringToTaskCard(String taskString) {
        String[] parts = taskString.split("\\|");
        int points = Integer.parseInt(parts[2]); // convert string to int
        City city1 = new City(parts[0]);
        City city2 = new City(parts[1]);
        return new TaskCard(city1, city2, points);
    }


    /**
     * check if this is the first attempt to create a task card deck and whether the taskCardDeck has been filled
     */
    private boolean isExisted() {
        return existed;
    }


    /**
     * set existed to true after filling in the deck for the first time
     */
    private void setExisted() {
        existed = true;
    }


    /**
     * set card id with given num
     * @param num   an input number that will be used to form card id, card id format: "Taskxxx"
     * @param currCard      the target card object that the id is being assigned on
     */
    private void setTaskCardId(int num, TaskCard currCard) {
        if (num < 0 || num > TOTAL_TASK_CARD_NUM) {
            throw new IllegalArgumentException( "Invalid number. Numbers should be between 0 and 30." );
        }
        if (usedIds.contains(num)) {
            throw new IllegalArgumentException("Invalid id, id number has been assigned to another card," +
                    " use another number.");
        }
        if (currCard == null) {
            throw new IllegalArgumentException("Task card not found, cannot assign id to an unknown card. ");
        }
        currCard.setCardId(num);
        usedIds.add(num);
    }
}
