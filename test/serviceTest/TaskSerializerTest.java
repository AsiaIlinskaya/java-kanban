package serviceTest;

import model.*;
import org.junit.jupiter.api.Test;
import service.TaskSerializer;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskSerializerTest {

    @Test
    void taskToCSV() {
        int id = 1;
        String name = "Task1";
        String description = "Description task1";
        Task task = new Task(name, description, TaskStatus.NEW);
        task.setId(id);
        String result = TaskSerializer.toCSV(task);
        String expectedResult = "1,TASK,Task1,NEW,Description task1,,,";
        assertEquals(expectedResult, result);
    }

    @Test
    void taskToCSVwithTime() {
        int id = 1;
        String name = "Task1";
        String description = "Description task1";
        Task task = new Task(name, description, TaskStatus.NEW);
        task.setId(id);
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 31, 23, 0);
        Duration duration = Duration.ofHours(7);
        task.setStartTime(startTime);
        task.setDuration(duration);
        String result = TaskSerializer.toCSV(task);
        String expectedResult = "1,TASK,Task1,NEW,Description task1,,31.12.2024 23:00:00,25200";
        assertEquals(expectedResult, result);
    }

    @Test
    void epicToCSV() {
        int id = 2;
        String name = "Epic2";
        String description = "Description epic2";
        Task epic = new Epic(name, description);
        epic.setId(id);
        String result = TaskSerializer.toCSV(epic);
        String expectedResult = "2,EPIC,Epic2,NEW,Description epic2,,,";
        assertEquals(expectedResult, result);
    }

    @Test
    void subtaskToCSV() {
        int id = 3;
        int epicId = 2;
        String name = "Sub Task2";
        String description = "Description sub task3";
        Subtask subtask = new Subtask(name, description, epicId, TaskStatus.DONE);
        subtask.setId(id);
        String result = TaskSerializer.toCSV(subtask);
        String expectedResult = "3,SUBTASK,Sub Task2,DONE,Description sub task3,2,,";
        assertEquals(expectedResult, result);
    }

    @Test
    void subtaskToCSVwithTime() {
        int id = 3;
        int epicId = 2;
        String name = "Sub Task2";
        String description = "Description sub task3";
        Subtask subtask = new Subtask(name, description, epicId, TaskStatus.DONE);
        subtask.setId(id);
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 31, 23, 0);
        Duration duration = Duration.ofMinutes(10);
        subtask.setStartTime(startTime);
        subtask.setDuration(duration);
        String result = TaskSerializer.toCSV(subtask);
        String expectedResult = "3,SUBTASK,Sub Task2,DONE,Description sub task3,2,31.12.2024 23:00:00,600";
        assertEquals(expectedResult, result);
    }

    @Test
    void taskFromCSV() {
        String csv = "1,TASK,Task1,NEW,Description task1,,,";
        Task task = TaskSerializer.fromCSV(csv);
        assertEquals(1, task.getId());
        assertEquals(TaskType.TASK, task.getTaskType());
        assertEquals("Task1", task.getName());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals("Description task1", task.getDescription());
    }

    @Test
    void taskFromCSVwithTime() {
        String csv = "1,TASK,Task1,NEW,Description task1,,31.12.2024 23:00:00,25200";
        Task task = TaskSerializer.fromCSV(csv);
        assertEquals(1, task.getId());
        assertEquals(TaskType.TASK, task.getTaskType());
        assertEquals("Task1", task.getName());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals("Description task1", task.getDescription());
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 31, 23, 0);
        Duration duration = Duration.ofHours(7);
        assertEquals(startTime, task.getStartTime());
        assertEquals(duration, task.getDuration());
    }

    @Test
    void epicFromCSV() {
        String csv = "2,EPIC,Epic2,NEW,,,,";
        Epic epic = (Epic) TaskSerializer.fromCSV(csv);
        assertEquals(2, epic.getId());
        assertEquals(TaskType.EPIC, epic.getTaskType());
        assertEquals("Epic2", epic.getName());
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void subTaskFromCSV() {
        String csv = "3,SUBTASK,Sub Task2,DONE,Description sub task3,2,,";
        Subtask subtask = (Subtask) TaskSerializer.fromCSV(csv);
        assertEquals(3, subtask.getId());
        assertEquals(TaskType.SUBTASK, subtask.getTaskType());
        assertEquals("Sub Task2", subtask.getName());
        assertEquals(TaskStatus.DONE, subtask.getStatus());
        assertEquals("Description sub task3", subtask.getDescription());
        assertEquals(2,subtask.getEpicId());
    }

    @Test
    void subTaskFromCSVwithTime() {
        String csv = "3,SUBTASK,Sub Task2,DONE,Description sub task3,2,31.12.2024 23:00:00,600";
        Subtask subtask = (Subtask) TaskSerializer.fromCSV(csv);
        assertEquals(3, subtask.getId());
        assertEquals(TaskType.SUBTASK, subtask.getTaskType());
        assertEquals("Sub Task2", subtask.getName());
        assertEquals(TaskStatus.DONE, subtask.getStatus());
        assertEquals("Description sub task3", subtask.getDescription());
        assertEquals(2,subtask.getEpicId());
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 31, 23, 0);
        Duration duration = Duration.ofMinutes(10);
        assertEquals(startTime, subtask.getStartTime());
        assertEquals(duration, subtask.getDuration());
    }

    @Test
    void getHeader() {
        String expected = "id,type,name,status,description,epic,starttime,duration";
        assertEquals(expected, TaskSerializer.getHeader());
    }
}