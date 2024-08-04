package serviceTest;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import service.ManagerFileException;
import service.Managers;
import service.TaskSerializer;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    @BeforeEach
    void prepare() throws IOException {
        file = File.createTempFile("tst", null);
        setManager(new FileBackedTaskManager(file));
    }

    @AfterEach
    void cleanHistory() {
        Managers.getDefaultHistory().clear();
    }

    @Test
    @Override
    protected void getAllTasksTest() {
        super.getAllTasksTest();
    }

    @Test
    @Override
    protected void getAllEpicsTest() {
        super.getAllEpicsTest();
    }

    @Test
    @Override
    protected void getAllSubtasksTest() {
        super.getAllSubtasksTest();
    }

    @Test
    @Override
    protected void removeAllTasksTest() {
        super.removeAllTasksTest();
    }

    @Test
    @Override
    protected void removeAllEpicsTest() {
        super.removeAllEpicsTest();
    }

    @Test
    @Override
    protected void removeAllSubtasksTest() {
        super.removeAllSubtasksTest();
    }

    @Test
    @Override
    protected void putAndGetTaskTest() {
        super.putAndGetTaskTest();
    }

    @Test
    @Override
    protected void putAndGetEpicTest() {
        super.putAndGetEpicTest();
    }

    @Test
    @Override
    protected void putAndGetSubtaskTest() {
        super.putAndGetSubtaskTest();
    }

    @Test
    @Override
    protected void removeTaskTest() {
        super.removeTaskTest();
    }

    @Test
    @Override
    protected void removeEpicTest() {
        super.removeEpicTest();
    }


    @Test
    @Override
    protected void removeSubtaskTest() {
        super.removeSubtaskTest();
    }

    @Test
    @Override
    protected void updateTaskTest() {
        super.updateTaskTest();
    }

    @Test
    @Override
    protected void updateEpicTest() {
        super.updateEpicTest();
    }

    @Test
    @Override
    protected void updateSubtaskTest() {
        super.updateSubtaskTest();
    }

    @Test
    @Override
    protected void epicStateTest() {
        super.epicStateTest();
    }

    @Test
    @Override
    protected void getSubtasksTest() {
        super.getSubtasksTest();
    }

    @Test
    @Override
    protected void getHistoryTest() {
        super.getHistoryTest();
    }

    @Test
    @Override
    protected void taskHistoryMutabilityTest() {
        super.taskHistoryMutabilityTest();
    }

    @Test
    @Override
    protected void taskMutabilityTest() {
        super.taskMutabilityTest();
    }

    @Test
    @Override
    protected void taskTimeIntersectionTest() {
        super.taskTimeIntersectionTest();
    }

    @Test
    void loadFromFileEmpty() throws IOException {
        getManager().removeAllTasks();
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertNull(buffer.readLine());
        FileBackedTaskManager manager2 = new FileBackedTaskManager(file);
        assertTrue(manager2.getAllTasks().isEmpty());
    }

    @Test
    void loadFromInvalidFile() throws IOException {
        file = File.createTempFile("tst", null);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("fake header\n");
            writer.write("fake line\n");
        }
        assertThrows(ManagerFileException.class, () -> new FileBackedTaskManager(file));
    }

    @Test
    void loadFromFile() throws IOException {
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        Task task2 = new Task("Task2", "Description task2", TaskStatus.IN_PROGRESS);
        getManager().putTask(task1);
        getManager().putTask(task2);
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        getManager().putEpic(epic1);
        getManager().putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        getManager().putSubtask(subtask);
        Subtask subtask2 = new Subtask("SubTask3", "Description subtask3", epic2.getId(), TaskStatus.NEW);
        getManager().putSubtask(subtask2);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,,,", buffer.readLine());
        assertEquals("1,TASK,Task2,IN_PROGRESS,Description task2,,,", buffer.readLine());
        assertEquals("2,EPIC,epic1,DONE,Description epic1,,,", buffer.readLine());
        assertEquals("3,EPIC,epic2,NEW,Description epic2,,,", buffer.readLine());
        assertEquals("4,SUBTASK,SubTask2,DONE,Description subtask,2,,", buffer.readLine());
        assertEquals("5,SUBTASK,SubTask3,NEW,Description subtask3,3,,", buffer.readLine());
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
    void removeAllTasksFileTest() throws IOException {
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        Task task2 = new Task("Task2", "Description task2", TaskStatus.NEW);
        getManager().putTask(task1);
        getManager().putTask(task2);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,,,", buffer.readLine());
        assertEquals("1,TASK,Task2,NEW,Description task2,,,", buffer.readLine());
        getManager().removeAllTasks();
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertNull(buffer.readLine());
    }

    @Test
    void removeAllEpicsFileTest() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        getManager().putEpic(epic1);
        getManager().putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        getManager().putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,,,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,,,", buffer.readLine());
        assertEquals("2,SUBTASK,SubTask2,DONE,Description subtask,0,,", buffer.readLine());
        getManager().removeAllEpics();
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertNull(buffer.readLine());
    }

    @Test
    void removeAllSubtasksFileTest() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        getManager().putEpic(epic1);
        getManager().putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        getManager().putSubtask(subtask);
        Subtask subtask2 = new Subtask("SubTask3", "Description subtask3", epic2.getId(), TaskStatus.NEW);
        getManager().putSubtask(subtask2);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,,,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,,,", buffer.readLine());
        assertEquals("2,SUBTASK,SubTask2,DONE,Description subtask,0,,", buffer.readLine());
        assertEquals("3,SUBTASK,SubTask3,NEW,Description subtask3,1,,", buffer.readLine());
        getManager().removeAllSubtasks();
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,NEW,Description epic1,,,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,,,", buffer.readLine());
    }

    @Test
    void putTaskFileTest() throws IOException {
        Task task = new Task("Task1", "Description task1", TaskStatus.NEW);
        getManager().putTask(task);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,,,", buffer.readLine());
    }

    @Test
    void putEpicFileTest() throws IOException {
        Epic epic = new Epic("Epic1", "Description1");
        getManager().putEpic(epic);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,Epic1,NEW,Description1,,,", buffer.readLine());
    }

    @Test
    void putSubtaskFileTest() throws IOException {
        //0,SUBTASK,SubTask2,DONE,Description subtask,2
        Epic epic = new Epic("Epic1", "Description1");
        getManager().putEpic(epic);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic.getId(), TaskStatus.DONE);
        getManager().putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,Epic1,DONE,Description1,,,", buffer.readLine());
        assertEquals("1,SUBTASK,SubTask2,DONE,Description subtask,0,,", buffer.readLine());
    }

    @Test
    void removeTaskFileTest() throws IOException {
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        Task task2 = new Task("Task2", "Description task2", TaskStatus.NEW);
        getManager().putTask(task1);
        getManager().putTask(task2);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,,,", buffer.readLine());
        assertEquals("1,TASK,Task2,NEW,Description task2,,,", buffer.readLine());
        getManager().removeTask(task1.getId());
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("1,TASK,Task2,NEW,Description task2,,,", buffer.readLine());
    }

    @Test
    void removeEpicFileTest() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        getManager().putEpic(epic1);
        getManager().putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        getManager().putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,,,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,,,", buffer.readLine());
        assertEquals("2,SUBTASK,SubTask2,DONE,Description subtask,0,,", buffer.readLine());
        getManager().removeEpic(epic1.getId());
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,,,", buffer.readLine());
    }

    @Test
    void removeSubtaskFileTest() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        Epic epic2 = new Epic("epic2", "Description epic2");
        getManager().putEpic(epic1);
        getManager().putEpic(epic2);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        getManager().putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,,,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,,,", buffer.readLine());
        assertEquals("2,SUBTASK,SubTask2,DONE,Description subtask,0,,", buffer.readLine());
        getManager().removeSubtask(subtask.getId());
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,NEW,Description epic1,,,", buffer.readLine());
        assertEquals("1,EPIC,epic2,NEW,Description epic2,,,", buffer.readLine());
    }

    @Test
    void updateTaskFileTest() throws IOException {
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        getManager().putTask(task1);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task1,NEW,Description task1,,,", buffer.readLine());
        Task task2 = new Task("Task2", "Description task2", TaskStatus.IN_PROGRESS);
        task2.setId(task1.getId());
        getManager().updateTask(task2);
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,TASK,Task2,IN_PROGRESS,Description task2,,,", buffer.readLine());
    }

    @Test
    void updateEpicFileTest() throws IOException {
        Epic epic1 = new Epic("Epic1", "Description Epic1");
        getManager().putEpic(epic1);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,Epic1,NEW,Description Epic1,,,", buffer.readLine());
        Epic epic2 = new Epic("Epic2", "Description Epic2");
        epic2.setId(epic1.getId());
        getManager().updateEpic(epic2);
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,Epic2,NEW,Description Epic2,,,", buffer.readLine());
    }

    @Test
    void updateSubtaskFileTest() throws IOException {
        Epic epic1 = new Epic("epic1", "Description epic1");
        getManager().putEpic(epic1);
        Subtask subtask = new Subtask("SubTask2", "Description subtask", epic1.getId(), TaskStatus.DONE);
        getManager().putSubtask(subtask);
        BufferedReader buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,DONE,Description epic1,,,", buffer.readLine());
        assertEquals("1,SUBTASK,SubTask2,DONE,Description subtask,0,,", buffer.readLine());
        Subtask subtask2 = new Subtask("SubTask4", "Description subtask4", epic1.getId(), TaskStatus.IN_PROGRESS);
        subtask2.setId(subtask.getId());
        getManager().updateSubtask(subtask2);
        buffer = getReader();
        assertEquals(TaskSerializer.getHeader(), buffer.readLine());
        assertEquals("0,EPIC,epic1,IN_PROGRESS,Description epic1,,,", buffer.readLine());
        assertEquals("1,SUBTASK,SubTask4,IN_PROGRESS,Description subtask4,0,,", buffer.readLine());
    }



    private BufferedReader getReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }

}