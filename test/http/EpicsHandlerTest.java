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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EpicsHandlerTest {

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
        Epic epic1 = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic1);
        Epic epic2 = new Epic("Test 2", "Testing task 2");
        manager.putEpic(epic2);
        HttpResponse<String> response = HttpTestHelper.sendRequest("epics/");
        assertEquals(200, response.statusCode());
        String jsonEpics = response.body();
        List<Epic> sourceEpics = Arrays.asList(epic1, epic2);
        String sourceJSON = gson.toJson(sourceEpics);
        assertEquals(sourceJSON, jsonEpics);
    }

    @Test
    void getHandlerByIdOK() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic1);
        HttpResponse<String> response = HttpTestHelper.sendRequest("epics/" + epic1.getId());
        assertEquals(200, response.statusCode());
        String jsonEpic = response.body();
        String sourceJson = gson.toJson(epic1);
        assertEquals(sourceJson, jsonEpic);
    }

    @Test
    void getHandlerByIdNotFound() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic1);
        int id = epic1.getId();
        HttpResponse<String> response = HttpTestHelper.sendRequest("tasks/" + ++id);
        assertEquals(404, response.statusCode());
    }

    @Test
    void getHandlerSubtasksByIdOK() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test 3", "Testing subtask 3", epic.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask2);
        HttpResponse<String> response = HttpTestHelper.sendRequest("epics/" + epic.getId() + "/subtasks");
        assertEquals(200, response.statusCode());
        String jsonSubtasks = response.body();
        List<Subtask> subtasks = Arrays.asList(subtask1, subtask2);
        String sourceJson = gson.toJson(subtasks);
        assertEquals(sourceJson, jsonSubtasks);
    }

    @Test
    void getHandlerSubtasksByIdNotFound() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epic.getId(), TaskStatus.IN_PROGRESS);
        manager.putSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test 3", "Testing subtask 3", epic.getId(), TaskStatus.DONE);
        manager.putSubtask(subtask2);
        HttpResponse<String> response = HttpTestHelper.sendRequest("epics/" + subtask1.getId() + "/subtasks");
        assertEquals(404, response.statusCode());
    }

    @Test
    void postHandlerCreateOK() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        String asJson = gson.toJson(epic);
        int httpResult = HttpTestHelper.sendRequest("epics", asJson);
        assertEquals(201, httpResult);
        List<Epic> epicsFromManager = manager.getAllEpics();
        assertNotNull(epicsFromManager);
        assertEquals(1, epicsFromManager.size());
        assertEquals("Test 1", epicsFromManager.get(0).getName());
    }

    @Test
    void deleteHandlerByIdOK() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        int httpCode = HttpTestHelper.delRequest("epics/" + epic.getId());
        assertEquals(201, httpCode);
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void deleteHandlerByIdNotFound() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        manager.putEpic(epic);
        int id = epic.getId();
        int httpCode = HttpTestHelper.delRequest("epics/" + ++id);
        assertEquals(404, httpCode);
        assertEquals(1, manager.getAllEpics().size());
    }
}