import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    void prepareTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

    @AfterEach
    void cleanHistory() {
        Managers.getDefaultHistory().getHistory().clear();
    }

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;

    @Test
    void putAndGetTask() {
        Task task = new Task("name", "desc", TaskStatus.NEW);
        taskManager.putTask(task);
        Task taskRetrieved = taskManager.getTask(task.getId());
        assertEquals(task, taskRetrieved);
    }

    @Test
    void putAndGetEpic() {
        Epic epic = new Epic("name", "desc");
        taskManager.putEpic(epic);
        Epic epicRetrieved = taskManager.getEpic(epic.getId());
        assertEquals(epic, epicRetrieved);
    }

    @Test
    void putAndGetSubtask() {
        Epic epic = new Epic("name", "desc");
        taskManager.putEpic(epic);
        Subtask subtask = new Subtask("name", "desc", epic.getId(), TaskStatus.NEW);
        taskManager.putSubtask(subtask);
        Subtask subtaskRetrieved = taskManager.getSubtask(subtask.getId());
        assertEquals(subtask, subtaskRetrieved);
    }

    // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    void taskMutability() {
        String name = "Task Name";
        String descr = "Task description";
        TaskStatus taskStatus = TaskStatus.NEW;
        Task task = new Task(name, descr, taskStatus);
        taskManager.putTask(task);
        assertEquals(name, task.getName());
        assertEquals(descr, task.getDescription());
        assertEquals(taskStatus, task.getStatus());
    }

    // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void taskHistoryMutability() {
        String name = "Task Name";
        String descr = "Task description";
        TaskStatus taskStatus = TaskStatus.NEW;
        Task task = new Task(name, descr, taskStatus);
        taskManager.putTask(task);
        taskManager.getTask(task.getId());
        int histIndex = taskManager.getHistory().indexOf(task);
        assertNotEquals(-1, histIndex);
        Task histTask = taskManager.getHistory().get(histIndex);
        assertEquals(name, histTask.getName());
        assertEquals(descr, histTask.getDescription());
        assertEquals(taskStatus, histTask.getStatus());
    }

}