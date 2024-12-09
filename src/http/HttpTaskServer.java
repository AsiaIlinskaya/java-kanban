package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static HttpServer httpServer;
    private static TaskManager taskManager;
    private static final Gson gson = initGson();

    public static void main(String[] args) {
        HttpTaskServer taskServer = new HttpTaskServer(Managers.getDefault());
        taskServer.start();

        // TODO DEL DEBUG CODE
        Task tsk1 = new Task("Имя задачи 1", "Описание задачи 1", TaskStatus.NEW);
        tsk1.setDuration(Duration.ofHours(1));
        tsk1.setStartTime(LocalDateTime.of(2024, 12, 15, 13, 20));
        Managers.getDefault().putTask(tsk1);
        Task tsk2 = new Task("Имя задачи 2", "Описание задачи 2", TaskStatus.IN_PROGRESS);
        tsk2.setDuration(Duration.ofHours(3));
        tsk2.setStartTime(LocalDateTime.of(2024, 12, 11, 17, 40));
        Managers.getDefault().putTask(tsk2);
        Epic epic1 = new Epic("Имя эпика 1", "Описание эпика 1");
        Managers.getDefault().putEpic(epic1);
        Epic epic2 = new Epic("Имя эпика 2", "Описание эпика 2");
        Managers.getDefault().putEpic(epic2);
        Subtask stsk1 = new Subtask("Имя подзадачи 1", "Описание подзадачи 1", epic1.getId(), TaskStatus.IN_PROGRESS);
        stsk1.setDuration(Duration.ofHours(1));
        stsk1.setStartTime(LocalDateTime.of(2024, 12, 23, 9, 50));
        Managers.getDefault().putSubtask(stsk1);
        Subtask stsk2 = new Subtask("Имя подзадачи 2", "Описание подзадачи 1", epic2.getId(), TaskStatus.DONE);
        stsk2.setDuration(Duration.ofHours(1));
        stsk2.setStartTime(LocalDateTime.of(2024, 12, 25, 18, 30));
        Managers.getDefault().putSubtask(stsk2);
    }

    private static HttpServer initServer() {
        try {
            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TasksHandler());
            httpServer.createContext("/subtasks", new SubtasksHandler());
            httpServer.createContext("/epics", new EpicsHandler());
            httpServer.createContext("/history", new HistoryHandler());
            httpServer.createContext("/prioritized", new PrioritizedHandler());
            return httpServer;
        } catch (IOException e) {
            System.out.println("Ошибка инициализации HTTP-сервера: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Gson initGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Epic.class, new EpicDeserializer())
                .create();
    }

    protected static TaskManager getTaskManager() {
        return taskManager;
    }

    protected static Gson getGson() {
        return gson;
    }

    public HttpTaskServer(TaskManager taskManager) {
        HttpTaskServer.taskManager = taskManager;
    }

    public void start() {
        httpServer = initServer();
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

}
