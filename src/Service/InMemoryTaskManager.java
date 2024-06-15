package Service;

import Model.Epic;
import Model.Subtask;
import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks; // список задач
    private final HashMap<Integer, Epic> epics; // список эпиков
    private final HashMap<Integer, Subtask> subtasks; // список подзадач
    private int nextId; // числовое поле-счётчик для генерации идентификаторов
    private final HistoryManager history;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        history = Managers.getDefaultHistory();
    }
    /*
            Получение списка всех задач типа Задача
         */
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    /*
        Получение списка всех задач типа Эпик
    */
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    /*
        Получение списка всех задач типа Подзадача
     */
    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    /*
        Удаление списка всех задач типа Задача
     */
    @Override
    public void removeAllTasks() {
        removeAllHistory(tasks.keySet());
        tasks.clear();
    }

    /*
        Удаление списка всех задач типа Эпик
    */
    @Override
    public void removeAllEpics() {
        removeAllHistory(epics.keySet());
        removeAllHistory(subtasks.keySet());

        epics.clear();
        subtasks.clear();

    }

    /*
        Удаление списка всех задач типа Подзадача
     */
    @Override
    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
        }
        removeAllHistory(subtasks.keySet());
        subtasks.clear();
    }

    /*
        Получение задачи по ID
     */
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        history.add(task);
        return task;
    }

    /*
        Получение Эпика по ID
     */
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        history.add(epic);
        return epic;
    }

    /*
        Получение Подзадачи по ID
     */
    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        history.add(subtask);
        return subtask;
    }

    /*
        Создание Задачи
     */
    @Override
    public void putTask(Task task) {
        int id = getNextId();
        task.setId(id);
        tasks.put(id, task);
    }

    /*
        Создание Эпика
     */
    @Override
    public void putEpic(Epic epic) {
        int id = getNextId();
        epic.setId(id);
        epics.put(id, epic);
    }

    /*
        Создание Подзадачи
     */

    @Override
    public void putSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId)) {
            int subtaskId = getNextId();
            subtask.setId(subtaskId);
            subtasks.put(subtaskId, subtask);
            Epic epic = epics.get(epicId);
            epic.addSubtask(subtask);
        }

    }

    /*
        Удаление Задачи
     */
    @Override
    public void removeTask(int id) {
        history.remove(id);
        tasks.remove(id);

    }

    /*
        Удаление Эпика
     */
    @Override
    public void removeEpic(int id) {

        for (Subtask subtask : epics.get(id).getSubtasks()) {
            subtasks.remove(subtask.getId());
            history.remove(subtask.getId());
        }
        epics.remove(id);
        history.remove(id);

    }

    /*
        Удаление Подзадачи
     */
    @Override
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null ) {
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            epic.removeSubtask(subtask);
            subtasks.remove(id);
            history.remove(id);
        }
    }

    /*
        Обновление Задачи
     */
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    /*
        Обновление Эпика
     */
    @Override
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
    @Override
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
    @Override
    public List<Subtask> getSubtasks(int id){
        if (epics.containsKey(id)) {
            return epics.get(id).getSubtasks();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    private int getNextId() {
        return nextId++;
    }

    /*
     * Для удаления истории по списку идентификаторов
     */
    private void removeAllHistory (Set<Integer> idSet) {
        for (Integer id : idSet) {
            history.remove(id);
        }
    }

}
