package taskTest;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

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

    @Test
    void getEndTime() {
        Task task = new Task("name", "descr", TaskStatus.NEW);
        assertNull(task.getStartTime());
        assertNull(task.getDuration());
        assertNull(task.getEndTime());
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 1, 13, 30);
        task.setStartTime(startTime);
        assertNotNull(task.getStartTime());
        assertNull(task.getDuration());
        assertNull(task.getEndTime());
        Duration duration = Duration.ofMinutes(45);
        task.setDuration(duration);
        assertEquals(startTime, task.getStartTime());
        assertEquals(duration, task.getDuration());
        assertEquals(LocalDateTime.of(2024, 6, 1, 14, 15), task.getEndTime());
    }

}