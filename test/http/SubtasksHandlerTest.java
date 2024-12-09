package http;

import com.google.gson.Gson;
import model.Epic;
import model.Subtask;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubtasksHandlerTest {

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
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test 3", "Testing subtask 3", epic.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask2);
        HttpResponse<String> response = HttpTestHelper.sendGetRequest("subtasks/");
        assertEquals(200, response.statusCode());
        String jsonSubtasks = response.body();
        List<Subtask> sourceSubtasks = Arrays.asList(subtask1, subtask2);
        String sourceJSON = gson.toJson(sourceSubtasks);
        assertEquals(sourceJSON, jsonSubtasks);
    }

    @Test
    void getHandlerByIdOK() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask1);
        HttpResponse<String> response = HttpTestHelper.sendGetRequest("subtasks/" + subtask1.getId());
        assertEquals(200, response.statusCode());
        String jsonSubtask = response.body();
        String sourceJson = gson.toJson(subtask1);
        assertEquals(sourceJson, jsonSubtask);
    }

    @Test
    void getHandlerByIdNotFound() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask1);
        HttpResponse<String> response = HttpTestHelper.sendGetRequest("subtasks/" + epic.getId());
        assertEquals(404, response.statusCode());
    }

    @Test
    void postHandlerCreateOK() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        String asJson = gson.toJson(subtask1);
        HttpResponse<String> response = HttpTestHelper.sendPostRequest("subtasks", asJson);
        assertEquals(200, response.statusCode());
        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertNotNull(subtasksFromManager);
        assertEquals(1, subtasksFromManager.size());
        assertEquals("Test 2", subtasksFromManager.get(0).getName());
        assertEquals(subtasksFromManager.get(0).getId(), gson.fromJson(response.body(), int.class));
    }

    @Test
    void postHandlerCreateConflict() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        subtask1.setDuration(Duration.ofHours(4));
        subtask1.setStartTime(LocalDateTime.of(2024, 11,1, 12, 0, 0));
        manager.putSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test 3", "Testing subtask 3", epic.getId(), TaskStatus.IN_PROGRESS);
        subtask2.setDuration(Duration.ofHours(8));
        subtask2.setStartTime(LocalDateTime.of(2024, 11,1, 10, 0, 0));
        String asJson = gson.toJson(subtask2);
        int httpResult = HttpTestHelper.postNgetStatus("subtasks", asJson);
        assertEquals(406, httpResult);
    }

    @Test
    void postHandlerUpdateOK() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test 3", "Testing subtask 3", epic.getId(), TaskStatus.IN_PROGRESS);
        subtask2.setId(subtask1.getId());
        String asJson = gson.toJson(subtask2);
        int httpResult = HttpTestHelper.postNgetStatus("subtasks", asJson);
        assertEquals(201, httpResult);
        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertNotNull(subtasksFromManager);
        assertEquals(1, subtasksFromManager.size());
        assertEquals("Test 3", subtasksFromManager.get(0).getName());
    }

    @Test
    void postHandlerUpdateNotFound() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test 3", "Testing subtask 3", epic.getId(), TaskStatus.IN_PROGRESS);
        subtask2.setId(epic.getId());
        String asJson = gson.toJson(subtask2);
        int httpResult = HttpTestHelper.postNgetStatus("subtasks", asJson);
        assertEquals(404, httpResult);
    }

    @Test
    void deleteHandlerByIdOK() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask1);
        int httpCode = HttpTestHelper.delRequest("subtasks/" + subtask1.getId());
        assertEquals(201, httpCode);
        assertEquals(0, manager.getAllSubtasks().size());
    }

    @Test
    void deleteHandlerByIdNotFound() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask1);
        int httpCode = HttpTestHelper.delRequest("subtasks/" + epic.getId());
        assertEquals(404, httpCode);
    }
}