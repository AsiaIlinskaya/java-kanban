package serviceTest;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.TaskManager;
import service.TaskValidationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    private T manager;

    protected T getManager() {
        return manager;
    }

    protected void setManager(T manager) {
        this.manager = manager;
    }

    protected void getAllTasksTest() {
        Task task1 = new Task("n1", "d1", TaskStatus.NEW);
        Task task2 = new Task("n2", "d2", TaskStatus.NEW);
        Task task3 = new Task("n3", "d3", TaskStatus.NEW);
        manager.putTask(task1);
        manager.putTask(task2);
        manager.putTask(task3);
        List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks);
        assertEquals(3, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
        assertTrue(tasks.contains(task3));
    }

    protected void getAllEpicsTest() {
        Epic epic1 = new Epic("n1", "d1");
        Epic epic2 = new Epic("n2", "d2");
        Epic epic3 = new Epic("n3", "d3");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        manager.putEpic(epic3);
        List<Epic> epics = manager.getAllEpics();
        assertNotNull(epics);
        assertEquals(3, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
        assertTrue(epics.contains(epic3));
    }

    protected void getAllSubtasksTest() {
        Epic epic1 = new Epic("n1", "d1");
        Epic epic2 = new Epic("n2", "d2");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        Subtask subtask1 = new Subtask("n3", "n3", epic1.getId(), TaskStatus.NEW);
        Subtask subtask2 = new Subtask("n4", "n4", epic1.getId(), TaskStatus.NEW);
        Subtask subtask3 = new Subtask("n5", "n5", epic2.getId(), TaskStatus.NEW);
        Subtask subtask4 = new Subtask("n6", "n6", epic2.getId(), TaskStatus.NEW);
        manager.putSubtask(subtask1);
        manager.putSubtask(subtask2);
        manager.putSubtask(subtask3);
        manager.putSubtask(subtask4);
        List<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks);
        assertEquals(4, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
        assertTrue(subtasks.contains(subtask3));
        assertTrue(subtasks.contains(subtask4));
    }

    protected void removeAllTasksTest() {
        Task task1 = new Task("n1", "d1", TaskStatus.NEW);
        Task task2 = new Task("n2", "d2", TaskStatus.NEW);
        task2.setStartTime(LocalDateTime.now());
        task2.setDuration(Duration.ofMinutes(5));
        Task task3 = new Task("n3", "d3", TaskStatus.NEW);
        manager.putTask(task1);
        manager.putTask(task2);
        manager.putTask(task3);
        assertEquals(3, manager.getAllTasks().size());
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        assertEquals(2, manager.getHistory().size());
        assertEquals(1, manager.getPrioritizedTasks().size());
        manager.removeAllTasks();
        assertEquals(0, manager.getAllTasks().size());
        assertEquals(0, manager.getHistory().size());
        assertEquals(0, manager.getPrioritizedTasks().size());
    }

    protected void removeAllEpicsTest() {
        Epic epic1 = new Epic("n1", "d1");
        Epic epic2 = new Epic("n2", "d2");
        Epic epic3 = new Epic("n3", "d3");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        manager.putEpic(epic3);
        assertEquals(3, manager.getAllEpics().size());
        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        assertEquals(2, manager.getHistory().size());
        manager.removeAllEpics();
        assertEquals(0, manager.getAllEpics().size());
        assertEquals(0, manager.getHistory().size());
        assertEquals(0, manager.getPrioritizedTasks().size());
    }

    protected void removeAllSubtasksTest() {
        Epic epic1 = new Epic("n1", "d1");
        Epic epic2 = new Epic("n2", "d2");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        Subtask subtask1 = new Subtask("n3", "n3", epic1.getId(), TaskStatus.NEW);
        Subtask subtask2 = new Subtask("n4", "n4", epic1.getId(), TaskStatus.IN_PROGRESS);
        subtask2.setStartTime(LocalDateTime.now().minusHours(3));
        subtask2.setDuration(Duration.ofHours(1));
        Subtask subtask3 = new Subtask("n5", "n5", epic2.getId(), TaskStatus.DONE);
        subtask3.setStartTime(LocalDateTime.now());
        subtask3.setDuration(Duration.ofHours(2));
        Subtask subtask4 = new Subtask("n6", "n6", epic2.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask1);
        manager.putSubtask(subtask2);
        manager.putSubtask(subtask3);
        manager.putSubtask(subtask4);
        assertEquals(2, manager.getAllEpics().size());
        assertEquals(4, manager.getAllSubtasks().size());
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        assertEquals(TaskStatus.DONE, epic2.getStatus());
        manager.getEpic(epic2.getId());
        manager.getSubtask(subtask2.getId());
        assertEquals(2, manager.getHistory().size());
        assertEquals(2, manager.getPrioritizedTasks().size());
        manager.removeAllSubtasks();
        assertEquals(2, manager.getAllEpics().size());
        assertEquals(0, manager.getAllSubtasks().size());
        assertEquals(1, manager.getHistory().size());
        assertEquals(0, manager.getPrioritizedTasks().size());
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        assertEquals(TaskStatus.NEW, epic2.getStatus());
    }

    protected void putAndGetTaskTest() {
        Task task = new Task("name", "desc", TaskStatus.NEW);
        manager.putTask(task);
        assertEquals(0, manager.getHistory().size());
        Task taskRetrieved = manager.getTask(task.getId());
        assertEquals(task, taskRetrieved);
        assertEquals(1, manager.getHistory().size());
    }

    protected void putAndGetEpicTest() {
        Epic epic = new Epic("name", "desc");
        manager.putEpic(epic);
        assertEquals(0, manager.getHistory().size());
        Epic epicRetrieved = manager.getEpic(epic.getId());
        assertEquals(epic, epicRetrieved);
        assertEquals(1, manager.getHistory().size());
    }

    protected void putAndGetSubtaskTest() {
        Epic epic = new Epic("name", "desc");
        manager.putEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus());
        Subtask subtask = new Subtask("name", "desc", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
        assertEquals(0, manager.getHistory().size());
        Subtask subtaskRetrieved = manager.getSubtask(subtask.getId());
        assertEquals(subtask, subtaskRetrieved);
        assertEquals(1, manager.getHistory().size());
    }

    protected void removeTaskTest() {
        Task task1 = new Task("n1", "d1", TaskStatus.NEW);
        Task task2 = new Task("n2", "d2", TaskStatus.NEW);
        task2.setStartTime(LocalDateTime.now());
        task2.setDuration(Duration.ofMinutes(5));
        Task task3 = new Task("n3", "d3", TaskStatus.NEW);
        manager.putTask(task1);
        manager.putTask(task2);
        manager.putTask(task3);
        manager.getTask(task2.getId());
        assertEquals(3, manager.getAllTasks().size());
        assertEquals(1, manager.getHistory().size());
        assertEquals(1, manager.getPrioritizedTasks().size());
        manager.removeTask(task2.getId());
        assertEquals(2, manager.getAllTasks().size());
        assertEquals(0, manager.getHistory().size());
        assertEquals(0, manager.getPrioritizedTasks().size());
    }

    protected void removeEpicTest() {
        Epic epic1 = new Epic("n1", "d1");
        Epic epic2 = new Epic("n2", "d2");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        Subtask subtask1 = new Subtask("n3", "n3", epic1.getId(), TaskStatus.NEW);
        Subtask subtask2 = new Subtask("n4", "n4", epic1.getId(), TaskStatus.NEW);
        subtask2.setStartTime(LocalDateTime.now().minusHours(3));
        subtask2.setDuration(Duration.ofHours(1));
        Subtask subtask3 = new Subtask("n5", "n5", epic2.getId(), TaskStatus.NEW);
        subtask3.setStartTime(LocalDateTime.now());
        subtask3.setDuration(Duration.ofHours(2));
        Subtask subtask4 = new Subtask("n6", "n6", epic2.getId(), TaskStatus.NEW);
        manager.putSubtask(subtask1);
        manager.putSubtask(subtask2);
        manager.putSubtask(subtask3);
        manager.putSubtask(subtask4);
        assertEquals(2, manager.getAllEpics().size());
        assertEquals(4, manager.getAllSubtasks().size());
        manager.getEpic(epic2.getId());
        manager.getSubtask(subtask2.getId());
        assertEquals(2, manager.getHistory().size());
        assertEquals(2, manager.getPrioritizedTasks().size());
        manager.removeEpic(epic1.getId());
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(2, manager.getAllSubtasks().size());
        assertEquals(1, manager.getHistory().size());
        assertEquals(1, manager.getPrioritizedTasks().size());
    }

    protected void removeSubtaskTest() {
        Epic epic1 = new Epic("n1", "d1");
        Epic epic2 = new Epic("n2", "d2");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        Subtask subtask1 = new Subtask("n3", "n3", epic1.getId(), TaskStatus.NEW);
        Subtask subtask2 = new Subtask("n4", "n4", epic1.getId(), TaskStatus.NEW);
        subtask2.setStartTime(LocalDateTime.now().minusHours(3));
        subtask2.setDuration(Duration.ofHours(1));
        Subtask subtask3 = new Subtask("n5", "n5", epic2.getId(), TaskStatus.IN_PROGRESS);
        subtask3.setStartTime(LocalDateTime.now());
        subtask3.setDuration(Duration.ofHours(2));
        Subtask subtask4 = new Subtask("n6", "n6", epic2.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask1);
        manager.putSubtask(subtask2);
        manager.putSubtask(subtask3);
        manager.putSubtask(subtask4);
        assertEquals(TaskStatus.IN_PROGRESS, epic2.getStatus());
        assertEquals(2, manager.getAllEpics().size());
        assertEquals(4, manager.getAllSubtasks().size());
        manager.getEpic(epic2.getId());
        manager.getSubtask(subtask3.getId());
        assertEquals(2, manager.getHistory().size());
        assertEquals(2, manager.getPrioritizedTasks().size());
        manager.removeSubtask(subtask3.getId());
        assertEquals(TaskStatus.DONE, epic2.getStatus());
        assertEquals(2, manager.getAllEpics().size());
        assertEquals(3, manager.getAllSubtasks().size());
        assertEquals(1, manager.getHistory().size());
        assertEquals(1, manager.getPrioritizedTasks().size());
    }

    protected void updateTaskTest() {
        Task task1 = new Task("n1", "d1", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(5));
        manager.putTask(task1);
        assertTrue(manager.getPrioritizedTasks().contains(task1));
        assertTrue(manager.getAllTasks().contains(task1));
        assertEquals(1, manager.getAllTasks().size());
        assertEquals(1, manager.getPrioritizedTasks().size());
        Task task2 = new Task("n2", "d2", TaskStatus.NEW);
        task2.setId(task1.getId());
        manager.updateTask(task2);
        assertEquals(0, manager.getPrioritizedTasks().size());
        assertEquals(1, manager.getAllTasks().size());
        assertTrue(manager.getAllTasks().contains(task2));
    }

    protected void updateEpicTest() {
        Epic epic1 = new Epic("n1", "d1");
        manager.putEpic(epic1);
        assertTrue(manager.getPrioritizedTasks().isEmpty());
        assertTrue(manager.getAllEpics().contains(epic1));
        Subtask subtask1 = new Subtask("n3", "n3", epic1.getId(), TaskStatus.NEW);
        Subtask subtask2 = new Subtask("n4", "n4", epic1.getId(), TaskStatus.NEW);
        manager.putSubtask(subtask1);
        manager.putSubtask(subtask2);
        assertEquals(2, epic1.getSubtasks().size());
        assertEquals(1, manager.getAllEpics().size());
        Epic epic2 = new Epic("n2", "d2");
        epic2.setId(epic1.getId());
        manager.updateEpic(epic2);
        assertTrue(manager.getPrioritizedTasks().isEmpty());
        assertEquals(1, manager.getAllEpics().size());
        assertTrue(manager.getAllEpics().contains(epic2));
        assertTrue(manager.getAllEpics().contains(epic1));
        Epic storedEpic = manager.getEpic(epic1.getId());
        assertTrue(storedEpic.getSubtasks().contains(subtask1));
        assertTrue(storedEpic.getSubtasks().contains(subtask2));
    }

    protected void updateSubtaskTest() {
        Epic epic1 = new Epic("n1", "d1");
        manager.putEpic(epic1);
        Subtask subtask1 = new Subtask("n3", "n3", epic1.getId(), TaskStatus.NEW);
        Subtask subtask2 = new Subtask("n4", "n4", epic1.getId(), TaskStatus.NEW);
        subtask2.setStartTime(LocalDateTime.now().minusHours(3));
        subtask2.setDuration(Duration.ofHours(1));
        manager.putSubtask(subtask1);
        manager.putSubtask(subtask2);
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(2, manager.getAllSubtasks().size());
        assertEquals(1, manager.getPrioritizedTasks().size());
        assertTrue(manager.getAllEpics().contains(epic1));
        assertTrue(manager.getAllSubtasks().contains(subtask1));
        assertTrue(manager.getAllSubtasks().contains(subtask2));
        assertTrue(manager.getPrioritizedTasks().contains(subtask2));
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        Subtask subtask3 = new Subtask("n5", "n5", epic1.getId(), TaskStatus.IN_PROGRESS);
        subtask3.setId(subtask2.getId());
        subtask3.setStartTime(LocalDateTime.now());
        subtask3.setDuration(Duration.ofHours(2));
        manager.updateSubtask(subtask3);
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(2, manager.getAllSubtasks().size());
        assertEquals(1, manager.getPrioritizedTasks().size());
        assertTrue(manager.getAllEpics().contains(epic1));
        assertTrue(manager.getAllSubtasks().contains(subtask1));
        assertTrue(manager.getAllSubtasks().contains(subtask3));
        assertTrue(manager.getPrioritizedTasks().contains(subtask3));
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
    }

    protected void epicStateTest() {
        Epic epic1 = new Epic("n1", "d1");
        manager.putEpic(epic1);
        Subtask subtask1 = new Subtask("n3", "n3", epic1.getId(), TaskStatus.NEW);
        LocalDateTime initTime = LocalDateTime.of(2024, 2, 10, 13, 0);
        subtask1.setStartTime(initTime);
        Duration initDuration = Duration.ofHours(1);
        subtask1.setDuration(initDuration);
        manager.putSubtask(subtask1);
        assertEquals(initTime, epic1.getStartTime());
        assertEquals(initDuration, epic1.getDuration());
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        Subtask subtask2 = new Subtask("n4", "n4", epic1.getId(), TaskStatus.IN_PROGRESS);
        LocalDateTime updTime = LocalDateTime.of(2024, 4, 11, 16, 20);
        subtask2.setStartTime(updTime);
        Duration updDuration = Duration.ofMinutes(15);
        subtask2.setDuration(updDuration);
        subtask2.setId(subtask1.getId());
        manager.updateSubtask(subtask2);
        assertEquals(updTime, epic1.getStartTime());
        assertEquals(updDuration, epic1.getDuration());
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
    }

    protected void getSubtasksTest() {
        Epic epic1 = new Epic("n1", "d1");
        manager.putEpic(epic1);
        Subtask subtask1 = new Subtask("n3", "n3", epic1.getId(), TaskStatus.NEW);
        Subtask subtask2 = new Subtask("n4", "n4", epic1.getId(), TaskStatus.NEW);
        manager.putSubtask(subtask1);
        manager.putSubtask(subtask2);
        assertEquals(2, manager.getSubtasks(epic1.getId()).size());
        assertTrue(manager.getSubtasks(epic1.getId()).contains(subtask1));
        assertTrue(manager.getSubtasks(epic1.getId()).contains(subtask2));
    }

    protected void getHistoryTest() {
        assertTrue(manager.getHistory().isEmpty());
        Task task1 = new Task("n1", "d1", TaskStatus.NEW);
        manager.putTask(task1);
        Epic epic1 = new Epic("n1", "d1");
        manager.putEpic(epic1);
        Subtask subtask1 = new Subtask("n3", "n3", epic1.getId(), TaskStatus.NEW);
        manager.putSubtask(subtask1);
        assertTrue(manager.getHistory().isEmpty());
        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());
        assertEquals(3, manager.getHistory().size());
        assertEquals(subtask1, manager.getHistory().get(2));
        assertEquals(epic1, manager.getHistory().get(1));
        assertEquals(task1, manager.getHistory().get(0));
    }

    protected void taskMutabilityTest() {
        String name = "Task.Task Name";
        String descr = "Task.Task description";
        TaskStatus taskStatus = TaskStatus.NEW;
        Task task = new Task(name, descr, taskStatus);
        manager.putTask(task);
        assertEquals(name, task.getName());
        assertEquals(descr, task.getDescription());
        assertEquals(taskStatus, task.getStatus());
    }

    protected void taskHistoryMutabilityTest() {
        String name = "Task.Task Name";
        String descr = "Task.Task description";
        TaskStatus taskStatus = TaskStatus.NEW;
        Task task = new Task(name, descr, taskStatus);
        manager.putTask(task);
        manager.getTask(task.getId());
        int histIndex = manager.getHistory().indexOf(task);
        assertNotEquals(-1, histIndex);
        Task histTask = manager.getHistory().get(histIndex);
        assertEquals(name, histTask.getName());
        assertEquals(descr, histTask.getDescription());
        assertEquals(taskStatus, histTask.getStatus());
    }

    protected void taskTimeIntersectionTest() {
        Task task1 = new Task("n1", "d1", TaskStatus.NEW);
        Task task2 = new Task("n2", "d2", TaskStatus.NEW);
        Task task3 = new Task("n3", "d3", TaskStatus.NEW);
        Task task4 = new Task("n4", "d4", TaskStatus.NEW);
        Task task5 = new Task("n5", "d5", TaskStatus.NEW);
        Task task6 = new Task("n6", "d6", TaskStatus.NEW);
        Task task7 = new Task("n7", "d7", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 1, 10, 8, 0));
        task1.setDuration(Duration.ofHours(1));
        manager.putTask(task1);
        task3.setStartTime(LocalDateTime.of(2024, 1, 10, 6, 0));
        task3.setDuration(Duration.ofHours(1));
        manager.putTask(task3);
        task2.setStartTime(LocalDateTime.of(2024, 1, 10, 7, 30));
        task2.setDuration(Duration.ofMinutes(10));
        manager.putTask(task2);
        task4.setStartTime(LocalDateTime.of(2024, 1, 10, 8, 30));
        task4.setDuration(Duration.ofHours(1));
        assertThrows(TaskValidationException.class, () -> manager.putTask(task4));
        task5.setStartTime(LocalDateTime.of(2024, 1, 10, 5, 30));
        task5.setDuration(Duration.ofHours(1));
        assertThrows(TaskValidationException.class, () -> manager.putTask(task5));
        task6.setStartTime(LocalDateTime.of(2024, 1, 10, 7, 20));
        task6.setDuration(Duration.ofMinutes(30));
        assertThrows(TaskValidationException.class, () -> manager.putTask(task6));
        task7.setStartTime(LocalDateTime.of(2024, 1, 10, 6, 10));
        task7.setDuration(Duration.ofMinutes(10));
        assertThrows(TaskValidationException.class, () -> manager.putTask(task7));
    }

}
