package Service;

public class Managers {

    private static HistoryManager defaultHistoryManager;
    private static TaskManager defaultTaskManager;

    public static TaskManager getDefault() {
        if (defaultTaskManager == null) {
            defaultTaskManager = new InMemoryTaskManager();
        }
        return defaultTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        if (defaultHistoryManager == null) {
            defaultHistoryManager = new InMemoryHistoryManager();
        }
        return defaultHistoryManager;
    }

}
