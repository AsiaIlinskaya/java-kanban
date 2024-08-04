package taskTest;

import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    // проверьте, что наследники класса Task.Task равны друг другу, если равен их id;
    @Test
    void epicEqualsSameId() {
        Epic epic1 = new Epic("name1", "desc1");
        Epic epic2 = new Epic("name2", "desc2");
        epic1.setId(3);
        epic2.setId(3);
        assertEquals(epic1, epic2);
    }

    @Test
    void getEndTime() {
        Epic epic = new Epic("name", "descr");
        epic.setId(1);
        assertNull(epic.getStartTime());
        assertNull(epic.getDuration());
        assertNull(epic.getEndTime());
        Subtask subtask1 = new Subtask("n1", "d1", 1, TaskStatus.NEW);
        LocalDateTime startTime1 = LocalDateTime.of(2024, 5, 1, 12, 0);
        subtask1.setStartTime(startTime1);
        Duration duration = Duration.ofMinutes(30);
        subtask1.setDuration(duration);
        epic.addSubtask(subtask1);
        epic.refreshState();
        assertEquals(startTime1, epic.getStartTime());
        assertEquals(duration, epic.getDuration());
        assertEquals(LocalDateTime.of(2024, 5, 1, 12, 30), epic.getEndTime());
        Subtask subtask2 = new Subtask("n2", "d2", 1, TaskStatus.NEW);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 6, 15, 16, 0);
        subtask2.setStartTime(startTime2);
        subtask2.setDuration(duration);
        epic.addSubtask(subtask2);
        epic.refreshState();
        assertEquals(startTime1, epic.getStartTime());
        assertEquals(Duration.ofHours(1), epic.getDuration());
        assertEquals(LocalDateTime.of(2024, 6, 15, 16, 30), epic.getEndTime());
    }

    @Test
    void refreshStateAllStatusNew() {
        Epic epic = new Epic("name", "descr");
        epic.setId(1);
        Subtask subtask1 = new Subtask("n1", "d1", 1, TaskStatus.NEW);
        subtask1.setId(2);
        Subtask subtask2 = new Subtask("n2", "d2", 1, TaskStatus.NEW);
        subtask2.setId(3);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.refreshState();
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void refreshStateAllStatusDone() {
        Epic epic = new Epic("name", "descr");
        epic.setId(1);
        Subtask subtask1 = new Subtask("n1", "d1", 1, TaskStatus.DONE);
        subtask1.setId(2);
        Subtask subtask2 = new Subtask("n2", "d2", 1, TaskStatus.DONE);
        subtask2.setId(3);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.refreshState();
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    void refreshStateStatusesNewDone() {
        Epic epic = new Epic("name", "descr");
        epic.setId(1);
        Subtask subtask1 = new Subtask("n1", "d1", 1, TaskStatus.NEW);
        subtask1.setId(2);
        Subtask subtask2 = new Subtask("n2", "d2", 1, TaskStatus.DONE);
        subtask2.setId(3);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.refreshState();
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void refreshStateAllStatusProgress() {
        Epic epic = new Epic("name", "descr");
        epic.setId(1);
        Subtask subtask1 = new Subtask("n1", "d1", 1, TaskStatus.IN_PROGRESS);
        subtask1.setId(2);
        Subtask subtask2 = new Subtask("n2", "d2", 1, TaskStatus.IN_PROGRESS);
        subtask2.setId(3);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.refreshState();
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

}