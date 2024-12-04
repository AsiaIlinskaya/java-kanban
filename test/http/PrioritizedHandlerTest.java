package http;

import com.google.gson.Gson;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrioritizedHandlerTest {

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
    void getHandler() throws IOException, InterruptedException {
        Task task1 = new Task("Test 1", "Testing task 1", TaskStatus.NEW);
        task1.setDuration(Duration.ofHours(1));
        task1.setStartTime(LocalDateTime.of(2023, 6, 1, 11, 0, 0));
        manager.putTask(task1);
        Task task2 = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        task2.setDuration(Duration.ofHours(1));
        task2.setStartTime(LocalDateTime.of(2022, 6, 1, 11, 0, 0));
        manager.putTask(task2);
        Task task3 = new Task("Test 3", "Testing task 3", TaskStatus.NEW);
        task2.setDuration(Duration.ofHours(1));
        task2.setStartTime(LocalDateTime.of(2024, 6, 1, 11, 0, 0));
        manager.putTask(task3);
        String priorityJson = gson.toJson(manager.getPrioritizedTasks());
        HttpResponse<String> httpResult = HttpTestHelper.sendRequest("prioritized");
        assertEquals(200, httpResult.statusCode());
        String receivedPeiority = httpResult.body();
        assertEquals(priorityJson, receivedPeiority);
    }
}