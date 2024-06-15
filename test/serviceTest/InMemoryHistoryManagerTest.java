package serviceTest;

import model.TaskStatus;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private Task task;
    private Task task2;
    private Task task3;
    private HistoryManager history3item;

    @BeforeEach
    void gen3itemHistory() {
        history3item = new InMemoryHistoryManager();
        task = new Task("name1", "desc1", TaskStatus.NEW);
        task.setId(1);
        task2 = new Task("name2", "desc2", TaskStatus.NEW);
        task2.setId(2);
        task3 = new Task("name3", "desc3", TaskStatus.NEW);
        task3.setId(3);
        history3item.add(task);
        history3item.add(task2);
        history3item.add(task3);
    }

    // После добавления задачи история не пустая
    @Test
    void add1() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void add2() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task);
        historyManager.add(task2);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals(task, history.get(0));
        assertEquals(task2, history.get(1));
    }
    @Test
    void add3() {
        final List<Task> history = history3item.getHistory();
        assertNotNull(history);
        assertEquals(3, history.size());
        assertEquals(task, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }

    @Test
    void testEmpty() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(0, history.size());
    }

    @Test
    void reAdd3() {
        List<Task> history = history3item.getHistory(); // 1-2-3
        assertEquals(3, history.size());
        assertEquals(task, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
        // 3 again
        history3item.add(task3);
        history = history3item.getHistory(); // 1-2-3
        assertEquals(3, history.size());
        assertEquals(task, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
        // 1 again
        history3item.add(task);
        history = history3item.getHistory(); // 2-3-1
        assertEquals(3, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task, history.get(2));
        // 3 again
        history3item.add(task3);
        history = history3item.getHistory(); // 2-1-3
        assertEquals(3, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task, history.get(1));
        assertEquals(task3, history.get(2));
    }
    @Test
    void reAdd3newInstance() {
        List<Task> history = history3item.getHistory(); // 1-2-3
        assertEquals(3, history.size());
        assertEquals(task, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
        // new task 2
        task2 = new Task("name2-new", "desc2-new", TaskStatus.IN_PROGRESS);
        task2.setId(2);
        history3item.add(task2);
        history = history3item.getHistory(); // 1-3-2new
        assertEquals(3, history.size());
        assertEquals(task, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task2, history.get(2));
    }

    @Test
    void removeFirst() {
        history3item.remove(1);
        final List<Task> history = history3item.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    void removeLast() {
        history3item.remove(3);
        final List<Task> history = history3item.getHistory();
        assertEquals(2, history.size());
        assertEquals(task, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void removeMiddle() {
        history3item.remove(2);
        final List<Task> history = history3item.getHistory();
        assertEquals(2, history.size());
        assertEquals(task, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    void clear() {
        history3item.clear();
        final List<Task> history = history3item.getHistory();
        assertNotNull(history);
        assertEquals(0, history.size());
    }

    @Test
    void addAfterClear() {
        history3item.clear();
        List<Task> history = history3item.getHistory();
        assertNotNull(history);
        assertEquals(0, history.size());
        history3item.add(task2);
        history3item.add(task);
        history3item.add(task3);
        history = history3item.getHistory(); // 2-1-3
        assertEquals(3, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task, history.get(1));
        assertEquals(task3, history.get(2));
    }

}