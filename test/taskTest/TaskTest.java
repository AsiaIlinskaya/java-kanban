package taskTest;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    // проверьте, что экземпляры класса Task.Task равны друг другу, если равен их id;
    @Test
    void taskEqualsSameId() {
        Task task1 = new Task("name1", "desc1", TaskStatus.NEW);
        Task task2 = new Task("name2", "desc2", TaskStatus.IN_PROGRESS);
        task1.setId(2);
        task2.setId(2);
        assertEquals(task1, task2);
    }

}