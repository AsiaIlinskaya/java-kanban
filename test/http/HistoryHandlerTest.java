package http;

import com.google.gson.Gson;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryHandlerTest {

    static HistoryManager historyManager = Managers.getDefaultHistory();
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
        manager.putTask(task1);
        Task task2 = new Task("Test 2", "Testing task 2", TaskStatus.NEW);
        manager.putTask(task2);
        Task task3 = new Task("Test 3", "Testing task 3", TaskStatus.NEW);
        manager.putTask(task3);
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());
        manager.getTask(task1.getId());
        String historyJson = gson.toJson(historyManager.getHistory());
        HttpResponse<String> httpResult = HttpTestHelper.sendGetRequest("history");
        assertEquals(200, httpResult.statusCode());
        String receivedHistory = httpResult.body();
        assertEquals(historyJson, receivedHistory);
    }
}