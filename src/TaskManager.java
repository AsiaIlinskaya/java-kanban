import java.util.Collection;
import java.util.HashMap;

public class TaskManager {

    private final HashMap<Integer, Task> tasks; // список задач
    private final HashMap<Integer, Epic> epics; // список эпиков
    private final HashMap<Integer, Subtask> subtasks; // список подзадач

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    /*
            Получение списка всех задач типа Задача
         */
    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    /*
        Получение списка всех задач типа Эпик
    */
    public Collection<Epic> getAllEpics() {
        return epics.values();
    }

    /*
        Получение списка всех задач типа Подзадача
     */
    public Collection<Subtask> getAllSubtasks() {
        return subtasks.values();
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
        removeAllSubtasks();
    }

    /*
        Удаление списка всех задач типа Подзадача
     */
    public void removeAllSubtasks() {
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
        tasks.put(task.getId(), task);
    }

    /*
        Создание Эпика
     */
    public void putEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /*
        Создание Подзадачи
     */
    public void putSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
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
            removeSubtask(subtask.getId());
        }
        epics.remove(id);
    }

    /*
        Удаление Подзадачи
     */
    public void removeSubtask(int id) {
        subtasks.remove(id);
    }

    /*
        Обновление Задачи
     */
    public void updateTask(Task oldTask, Task newTask) {
        removeTask(oldTask.getId());
        putTask(newTask);
    }

    /*
        Обновление Эпика
     */
    public void updateEpic(Epic oldEpic, Epic newEpic) {
        removeEpic(oldEpic.getId());
        putEpic(newEpic);
    }

    /*
        Обновление Подзадачи
     */
    public void updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
        removeSubtask(oldSubtask.getId());
        putSubtask(newSubtask);
    }

    /*
        Получение списка всех подзадач определённого эпика.
     */
    public Collection<Subtask> getSubtasks(Epic epic) {
        return epic.getSubtasks();
    }
}
