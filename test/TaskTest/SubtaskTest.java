package TaskTest;

import Model.Subtask;
import Model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    // проверьте, что наследники класса Task.Task равны друг другу, если равен их id;
    @Test
    public void subtaskEqualsSameId() {
        Subtask subtask1 = new Subtask("name1", "desc1", 14, TaskStatus.NEW);
        Subtask subtask2 = new Subtask("name2", "desc2", 16, TaskStatus.IN_PROGRESS);
        subtask1.setId(5);
        subtask2.setId(5);
        assertEquals(subtask1, subtask2);
    }

}