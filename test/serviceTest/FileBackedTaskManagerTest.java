package serviceTest;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import service.TaskSerializer;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private File file;
    private FileBackedTaskManager manager;

    @BeforeEach
    void prepare() throws IOException {
        file = File.createTempFile("tst", null);
        manager = new FileBackedTaskManager(file);
    }

    @Test
    void loadFromFileEmpty() throws IOException {
        manager.removeAllTasks();
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertNull(buffer.readLine());
        FileBackedTaskManager manager2 = new FileBackedTaskManager(file);
        assertTrue(manager2.getAllTasks().isEmpty());
    }

    @Test
    void loadFromFile() throws IOException {
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        Task task2 = new Task("Task2", "Description task2", TaskStatus.IN_PROGRESS);
        manager.putTask(task1);
        manager.putTask(task2);
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask);
        Subtask subtask2 = new Subtask("SubTask3", "Description subtask3", epic2.getId(), TaskStatus.NEW);
        manager.putSubtask(subtask2);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,", buffer.readLine());
        assertEquals("1,TASK,Task2,IN_PROGRESS,Description task2,", buffer.readLine());
        assertEquals("2,EPIC,epic1,DONE,Description epic1,", buffer.readLine());
        assertEquals("3,EPIC,epic2,NEW,Description epic2,", buffer.readLine());
        assertEquals("4,SUBTASK,SubTask2,DONE,Description subtask,2", buffer.readLine());
        assertEquals("5,SUBTASK,SubTask3,NEW,Description subtask3,3", buffer.readLine());
        FileBackedTaskManager manager2 = new FileBackedTaskManager(file);
        assertEquals(2, manager2.getAllTasks().size());
        assertEquals(2, manager2.getAllEpics().size());
        assertEquals(2, manager2.getAllSubtasks().size());
        Task loadedTask1 = manager2.getTask(0);
        assertEquals("Task1", loadedTask1.getName());
        assertEquals("Description task1", loadedTask1.getDescription());
        assertEquals(TaskStatus.NEW, loadedTask1.getStatus());
        Task loadedTask2 = manager2.getTask(1);
        assertEquals("Task2", loadedTask2.getName());
        assertEquals("Description task2", loadedTask2.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, loadedTask2.getStatus());
        Epic loadedEpic1 = manager2.getEpic(2);
        assertEquals("epic1", loadedEpic1.getName());
        assertEquals("Description epic1", loadedEpic1.getDescription());
        assertEquals(TaskStatus.DONE, loadedEpic1.getStatus());
        Epic loadedEpic2 = manager2.getEpic(3);
        assertEquals("epic2", loadedEpic2.getName());
        assertEquals("Description epic2", loadedEpic2.getDescription());
        assertEquals(TaskStatus.NEW, loadedEpic2.getStatus());
        Subtask loadedSubtask2 = manager2.getSubtask(4);
        assertEquals("SubTask2", loadedSubtask2.getName());
        assertEquals("Description subtask", loadedSubtask2.getDescription());
        assertEquals(TaskStatus.DONE, loadedSubtask2.getStatus());
        assertEquals(2, loadedSubtask2.getEpicId());
        Subtask loadedSubtask3 = manager2.getSubtask(5);
        assertEquals("SubTask3", loadedSubtask3.getName());
        assertEquals("Description subtask3", loadedSubtask3.getDescription());
        assertEquals(TaskStatus.NEW, loadedSubtask3.getStatus());
        assertEquals(3, loadedSubtask3.getEpicId());
        Task task6 = new Task("Task6", "Description task6", TaskStatus.NEW);
        manager2.putTask(task6);
        assertEquals(6, task6.getId());
        List<Subtask> epic1Subtasks = epic1.getSubtasks();
        List<Subtask> epic2Subtasks = epic2.getSubtasks();
        assertEquals(1, epic1Subtasks.size());
        assertEquals(1, epic2Subtasks.size());
        assertTrue(epic1Subtasks.contains(loadedSubtask2));
        assertTrue(epic2Subtasks.contains(loadedSubtask3));
    }

    @Test
    void removeAllTasks() throws IOException {
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        Task task2 = new Task("Task2", "Description task2", TaskStatus.NEW);
        manager.putTask(task1);
        manager.putTask(task2);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,", buffer.readLine());
        assertEquals("1,TASK,Task2,NEW,Description task2,", buffer.readLine());
        manager.removeAllTasks();
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertNull(buffer.readLine());
    }

    @Test
    void removeAllEpics() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,", buffer.readLine());
        assertEquals("2,SUBTASK,SubTask2,DONE,Description subtask,0", buffer.readLine());
        manager.removeAllEpics();
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertNull(buffer.readLine());
    }

    @Test
    void removeAllSubtasks() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask);
        Subtask subtask2 = new Subtask("SubTask3", "Description subtask3", epic2.getId(), TaskStatus.NEW);
        manager.putSubtask(subtask2);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,", buffer.readLine());
        assertEquals("2,SUBTASK,SubTask2,DONE,Description subtask,0", buffer.readLine());
        assertEquals("3,SUBTASK,SubTask3,NEW,Description subtask3,1", buffer.readLine());
        manager.removeAllSubtasks();
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,NEW,Description epic1,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,", buffer.readLine());
    }

    @Test
    void putTask() throws IOException {
        Task task = new Task("Task1", "Description task1", TaskStatus.NEW);
        manager.putTask(task);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,", buffer.readLine());
    }

    @Test
    void putEpic() throws IOException {
        Epic epic = new Epic("Epic1", "Description1");
        manager.putEpic(epic);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,Epic1,NEW,Description1,", buffer.readLine());
    }

    @Test
    void putSubtask() throws IOException {
        //0,SUBTASK,SubTask2,DONE,Description subtask,2
        Epic epic = new Epic("Epic1", "Description1");
        manager.putEpic(epic);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,Epic1,DONE,Description1,", buffer.readLine());
        assertEquals("1,SUBTASK,SubTask2,DONE,Description subtask,0", buffer.readLine());
    }

    @Test
    void removeTask() throws IOException {
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        Task task2 = new Task("Task2", "Description task2", TaskStatus.NEW);
        manager.putTask(task1);
        manager.putTask(task2);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,", buffer.readLine());
        assertEquals("1,TASK,Task2,NEW,Description task2,", buffer.readLine());
        manager.removeTask(task1.getId());
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("1,TASK,Task2,NEW,Description task2,", buffer.readLine());
    }

    @Test
    void removeEpic() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,", buffer.readLine());
        assertEquals("2,SUBTASK,SubTask2,DONE,Description subtask,0", buffer.readLine());
        manager.removeEpic(epic1.getId());
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,", buffer.readLine());
    }

    @Test
    void removeSubtask() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,", buffer.readLine());
        assertEquals("2,SUBTASK,SubTask2,DONE,Description subtask,0", buffer.readLine());
        manager.removeSubtask(subtask.getId());
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,NEW,Description epic1,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,", buffer.readLine());
    }

    @Test
    void updateTask() throws IOException {
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        manager.putTask(task1);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,", buffer.readLine());
        Task task2 = new Task("Task2", "Description task2", TaskStatus.IN_PROGRESS);
        task2.setId(task1.getId());
        manager.updateTask(task2);
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task2,IN_PROGRESS,Description task2,", buffer.readLine());
    }

    @Test
    void updateEpic() throws IOException {
        Epic epic1 = new Epic("Epic1", "Description Epic1");
        manager.putEpic(epic1);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,Epic1,NEW,Description Epic1,", buffer.readLine());
        Epic epic2 = new Epic("Epic2", "Description Epic2");
        epic2.setId(epic1.getId());
        manager.updateEpic(epic2);
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,Epic2,NEW,Description Epic2,", buffer.readLine());
    }

    @Test
    void updateSubtask() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        manager.putEpic(epic1);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,", buffer.readLine());
        assertEquals("1,SUBTASK,SubTask2,DONE,Description subtask,0", buffer.readLine());
        Subtask subtask2 = new Subtask("SubTask4", "Description subtask4", epic1.getId(), TaskStatus.IN_PROGRESS);
        subtask2.setId(subtask.getId());
        manager.updateSubtask(subtask2);
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,IN_PROGRESS,Description epic1,", buffer.readLine());
        assertEquals("1,SUBTASK,SubTask4,IN_PROGRESS,Description subtask4,0", buffer.readLine());
    }

    private BufferedReader getReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }

}