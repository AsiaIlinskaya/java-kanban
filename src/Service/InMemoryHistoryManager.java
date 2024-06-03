package Service;

import Model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_LIMIT = 10;
    private final LinkedList<Task> history; // история просмотров

    public InMemoryHistoryManager() {
        history = new LinkedList<>();
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (history.size() == HISTORY_LIMIT) {
            history.removeFirst();
        }
        history.addLast(task);
    }

}
