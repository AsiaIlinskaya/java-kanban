import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

    private final HashMap<Integer, Task> tasks; // список задач
    private final HashMap<Integer, Epic> epics; // список эпиков
    private final HashMap<Integer, Subtask> subtasks; // список подзадач
    private int nextId; // числовое поле-счётчик для генерации идентификаторов

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    /*
            Получение списка всех задач типа Задача
         */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    /*
        Получение списка всех задач типа Эпик
    */
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    /*
        Получение списка всех задач типа Подзадача
     */
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    /*
        Удаление списка всех задач типа Задача
     */
    public void removeAllTasks() {
        tasks.clear();
    }

    /*
        Удаление списка всех задач типа Эпик
    */
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    /*
        Удаление списка всех задач типа Подзадача
     */
    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.refreshStatus();
        }
        subtasks.clear();
    }

    /*
        Получение задачи по ID
     */
    public Task getTask(int id) {
        return tasks.get(id);
    }

    /*
        Получение Эпика по ID
     */
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    /*
        Получение Подзадачи по ID
     */
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    /*
        Создание Задачи
     */
    public void putTask(Task task) {
        int id = getNextId();
        task.setId(id);
        tasks.put(id, task);
    }

    /*
        Создание Эпика
     */
    public void putEpic(Epic epic) {
        int id = getNextId();
        epic.setId(id);
        epics.put(id, epic);
    }

    /*
        Создание Подзадачи
     */

    public void putSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId)) {
            int subtaskId = getNextId();
            subtask.setId(subtaskId);
            subtasks.put(subtaskId, subtask);
            Epic epic = epics.get(epicId);
            epic.getSubtasks().add(subtask);
            epic.refreshStatus();
        }

    }

    /*
        Удаление Задачи
     */
    public void removeTask(int id) {
        tasks.remove(id);
    }

    /*
        Удаление Эпика
     */
    public void removeEpic(int id) {
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }

    /*
        Удаление Подзадачи
     */
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.getSubtasks().remove(subtask);
        epic.refreshStatus();
        subtasks.remove(id);

    }

    /*
        Обновление Задачи
     */
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    /*
        Обновление Эпика
     */

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
        }

    }

    /*
        Обновление Подзадачи
     */

    public void updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        if (subtasks.containsKey(subtaskId)) {
            Subtask savedSubtask = subtasks.get(subtaskId);
            int epicId = subtask.getEpicId();
            if (savedSubtask.getEpicId() == epicId) {
                subtasks.put(subtaskId, subtask);
                Epic epic = epics.get(epicId);
                int subtaskIndex = epic.getSubtasks().indexOf(savedSubtask);
                epic.getSubtasks().set(subtaskIndex, subtask);
                epic.refreshStatus();
            }
        }
    }

    /*
        Получение списка всех подзадач определённого эпика.
     */
    public List<Subtask> getSubtasks (int id){
        if (epics.containsKey(id)) {
            return epics.get(id).getSubtasks();
        }
        else {
            return null;
        }
    }

    private int getNextId() {
        return nextId++;
    }
}
