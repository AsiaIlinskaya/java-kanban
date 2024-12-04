package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TasksHandlerTest {

    static TaskManager manager = new InMemoryTaskManager();
    static HttpTaskServer taskServer = new HttpTaskServer(manager);
    static Gson gson = HttpTaskServer.getGson();

    @BeforeAll
    public static void prepare() {
        taskServer.start();
    }

    @BeforeEach
    public void setUp() {
        manager.removeAllTasks();
        manager.removeAllTasks();
        manager.removeAllEpics();
    }

    @AfterAll
    public static void fin() {
        taskServer.stop();
    }

    @Test
    void getHandlerAll() throws IOException, InterruptedException {
        Task task1 = new Task("Test 1", "Testing task 1", TaskStatus.NEW);
        manager.putTask(task1);
        Task task2 = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        manager.putTask(task2);
        HttpResponse<String> response = HttpTestHelper.sendRequest("tasks/");
        assertEquals(200, response.statusCode());
        String jsonTasks = response.body();
        Type typeToken = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> receivedTasks = gson.fromJson(jsonTasks, typeToken);
        List<Task> sourceTasks = Arrays.asList(task1, task2);
        assertEquals(sourceTasks, receivedTasks);
    }

    @Test
    void getHandlerByIdOK() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        manager.putTask(task);
        HttpResponse<String> response = HttpTestHelper.sendRequest("tasks/" + task.getId());
        assertEquals(200, response.statusCode());
        String jsonTask = response.body();
        Task receivedTask = gson.fromJson(jsonTask, Task.class);
        assertEquals(task, receivedTask);
    }

    @Test
    void getHandlerByIdNotFound() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        manager.putTask(task);
        int id = task.getId();
        HttpResponse<String> response = HttpTestHelper.sendRequest("tasks/" + ++id);
        assertEquals(404, response.statusCode());
    }

    @Test
    void postHandlerCreateOK() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        String taskJson = gson.toJson(task);
        int httpResult = HttpTestHelper.sendRequest("tasks", taskJson);
        assertEquals(201, httpResult);
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Test 2", tasksFromManager.get(0).getName());
    }

    @Test
    void postHandlerCreateConflict() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", TaskStatus.NEW);
        task.setDuration(Duration.ofHours(5));
        task.setStartTime(LocalDateTime.of(2024, 10, 9, 10, 0 ,0));
        manager.putTask(task);
        Task task2 = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        task2.setDuration(Duration.ofHours(3));
        task2.setStartTime(LocalDateTime.of(2024, 10, 9, 8, 0 ,0));
        String taskJson = gson.toJson(task2);
        int httpResult = HttpTestHelper.sendRequest("tasks", taskJson);
        assertEquals(406, httpResult);
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Test 1", tasksFromManager.get(0).getName());
    }

    @Test
    void postHandlerUpdateOK() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        manager.putTask(task);
        Task taskUpd = new Task("Test 3", "Testing task 3", TaskStatus.NEW);
        taskUpd.setDuration(Duration.ofHours(1));
        LocalDateTime time = LocalDateTime.of(2024, 11, 15, 15, 7, 41);
        taskUpd.setStartTime(time);
        taskUpd.setId(task.getId());
        String taskJson = gson.toJson(taskUpd);
        int httpResult = HttpTestHelper.sendRequest("tasks", taskJson);
        assertEquals(201, httpResult);
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Test 3", tasksFromManager.get(0).getName());
        assertEquals(time, tasksFromManager.get(0).getStartTime());
    }

    @Test
    void deleteHandlerByIdOK() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        manager.putTask(task);
        int httpCode = HttpTestHelper.delRequest("tasks/" + task.getId());
        assertEquals(201, httpCode);
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void deleteHandlerByIdNotFound() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        manager.putTask(task);
        int id = task.getId();
        int httpCode = HttpTestHelper.delRequest("tasks/" + ++id);
        assertEquals(404, httpCode);
        assertEquals(1, manager.getAllTasks().size());
    }

}