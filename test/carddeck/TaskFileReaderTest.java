package carddeck;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskFileReaderTest {

    List<String> expectedList = List.of(
            "Los Angeles|New York City|21",
            "Duluth|Houston|8",
            "Sault Ste Marie|Nashville|8",
            "New York|Atlanta|6",
            "Portland|Nashville|17",
            "Vancouver|Montréal|20",
            "Duluth|El Paso|10", "Toronto|Miami|10",
            "Portland|Phoenix|11",
            "Dallas|New York City|11",
            "Calgary|Salt Lake City|7",
            "Calgary|Phoenix|13",
            "Los Angeles|Miami|20",
            "Winnipeg|Little Rock|11",
            "San Francisco|Atlanta|17",
            "Kansas City|Houston|5",
            "Los Angeles|Chicago|16",
            "Denver|Pittsburgh|11",
            "Chicago|Santa Fe|9",
            "Vancouver|Santa Fe|13",
            "Boston|Miami|12",
            "Chicago|New Orleans|7",
            "Montreal|Atlanta|9",
            "Seattle|New York|22",
            "Denver|El Paso|4",
            "Helena|Los Angeles|8",
            "Winnipeg|Houston|12",
            "Montreal|New Orleans|13",
            "Sault Ste. Marie|Oklahoma City|9",
            "Seattle|Los Angeles|9");

    @Test
    void createTaskList_returnCorrectLength() {
        List<String> tasks = TaskFileReader.getTasks();
        assertEquals(tasks.size(), 30);
    }

    @Test
    void createTaskList_returnCorrectFormat() {
        List<String> tasks = TaskFileReader.getTasks();
        assertEquals(tasks.get(0), expectedList.get(0));
    }

    @Test
    void createTaskList_returnCorrectList() {
        List<String> tasks = TaskFileReader.getTasks();
        for (int i = 0; i < 30; i++) {
            assertEquals(tasks.get(i), expectedList.get(i));
        }
    }


    @Test
    void createTaskListThreeTimes_returnSameObject() {
        List<String> tasks1 = TaskFileReader.getTasks();
        List<String> tasks2 = TaskFileReader.getTasks();
        assertSame(tasks1, tasks2);
    }

    @Test
    void getReaderInstanceTwoTimes_returnTheSameReader() {
        TaskFileReader firstTimeReader = TaskFileReader.getReaderInstance();
        TaskFileReader secondTimeReader = TaskFileReader.getReaderInstance();
        assertSame(firstTimeReader, secondTimeReader);
    }
}