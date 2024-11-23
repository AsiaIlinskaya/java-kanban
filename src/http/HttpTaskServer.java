package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final HttpServer httpServer = initServer();
    private static TaskManager taskManager;
    private static final HistoryManager historyManager = Managers.getDefaultHistory();
    private static final Gson gson = initGson();

    public static void main(String[] args) {
        HttpTaskServer taskServer = new HttpTaskServer(Managers.getDefault());
        taskServer.start();
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
        Gson gson = new Gson();

        return gson;
    }

    protected static TaskManager getTaskManager() {
        return taskManager;
    }

    protected static HistoryManager getHistoryManager() {
        return historyManager;
    }

    protected static Gson getGson() {
        return gson;
    }

    public HttpTaskServer(TaskManager taskManager) {
        HttpTaskServer.taskManager = taskManager;
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

}
