package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;


public class InMemoryTaskManager implements TaskManager {

    private static final String NOT_FOUND_MESSAGE = "Не найден объект с id ";
    private final HashMap<Integer, Task> tasks; // список задач
    private final HashMap<Integer, Epic> epics; // список эпиков
    private final HashMap<Integer, Subtask> subtasks; // список подзадач
    private int nextId; // числовое поле-счётчик для генерации идентификаторов
    private final HistoryManager history;
    private final TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        history = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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
        prioritizedTasks.removeAll(tasks.values());
        tasks.clear();
    }

    /*
        Удаление списка всех задач типа Эпик
    */
    @Override
    public void removeAllEpics() {
        removeAllHistory(epics.keySet());
        removeAllHistory(subtasks.keySet());
        prioritizedTasks.removeAll(subtasks.values());
        epics.clear();
        subtasks.clear();
    }

    /*
        Удаление списка всех задач типа Подзадача
     */
    @Override
    public void removeAllSubtasks() {
        epics.values().forEach(Epic::clearSubtasks);
        removeAllHistory(subtasks.keySet());
        prioritizedTasks.removeAll(subtasks.values());
        subtasks.clear();
    }

    /*
        Получение задачи по ID
     */
    @Override
    public Task getTask(int id) {
        validateExists(tasks, id);
        Task task = tasks.get(id);
        history.add(task);
        return task;
    }

    /*
        Получение Эпика по ID
     */
    @Override
    public Epic getEpic(int id) {
        validateExists(epics, id);
        Epic epic = epics.get(id);
        history.add(epic);
        return epic;
    }

    /*
        Получение Подзадачи по ID
     */
    @Override
    public Subtask getSubtask(int id) {
        validateExists(subtasks, id);
        Subtask subtask = subtasks.get(id);
        history.add(subtask);
        return subtask;
    }

    /*
        Создание Задачи
     */
    @Override
    public void putTask(Task task) {
        putTask(task, null);
    }

    /*
        Создание Эпика
     */
    @Override
    public void putEpic(Epic epic) {
        putEpic(epic, null);
    }

    /*
        Создание Подзадачи
     */

    @Override
    public void putSubtask(Subtask subtask) {
        putSubtask(subtask, null);
    }

    /*
        Удаление Задачи
     */
    @Override
    public void removeTask(int id) {
        validateExists(tasks, id);
        history.remove(id);
        Task task = tasks.get(id);
        if (task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
        tasks.remove(id);
    }

    /*
        Удаление Эпика
     */
    @Override
    public void removeEpic(int id) {
        validateExists(epics, id);
        epics.get(id).getSubtasks().forEach(subtask -> {
            if (subtask.getStartTime() != null) {
                prioritizedTasks.remove(subtask);
            }
            subtasks.remove(subtask.getId());
            history.remove(subtask.getId());
        });
        epics.remove(id);
        history.remove(id);
    }

    /*
        Удаление Подзадачи
     */
    @Override
    public void removeSubtask(int id) {
        validateExists(subtasks, id);
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.removeSubtask(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.remove(subtask);
        }
        subtasks.remove(id);
        history.remove(id);
    }

    /*
        Обновление Задачи
     */
    @Override
    public void updateTask(Task task) {
        validateExists(tasks, task.getId());
        validateTask(task);
        Task oldTask = tasks.get(task.getId());
        tasks.put(task.getId(), task);
        replacePrioritized(task, oldTask);
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
        validateExists(subtasks, subtaskId);
        Subtask savedSubtask = subtasks.get(subtaskId);
        int epicId = subtask.getEpicId();
        if (savedSubtask.getEpicId() == epicId) {
            validateTask(subtask);
            subtasks.put(subtaskId, subtask);
            replacePrioritized(subtask, savedSubtask);
            Epic epic = epics.get(epicId);
            int subtaskIndex = epic.getSubtasks().indexOf(savedSubtask);
            epic.getSubtasks().set(subtaskIndex, subtask);
            epic.refreshState();
        }
    }

    /*
        Получение списка всех подзадач определённого эпика.
     */
    @Override
    public List<Subtask> getSubtasks(int id) {
        validateExists(epics, id);
        return epics.get(id).getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    public Set<Task> getPrioritizedTasks() {
        return Collections.unmodifiableSortedSet(prioritizedTasks);
    }

    protected void putTaskGeneral(Task task, int id) {
        switch (task.getTaskType()) {
            case TASK:
                putTask(task, id);
                break;
            case EPIC:
                putEpic((Epic) task, id);
                break;
            case SUBTASK:
                putSubtask((Subtask) task, id);
                break;
        }
    }

    protected void setNextId(int nextId) {
        this.nextId = nextId;
    }

    private int getNextId() {
        return ++nextId;
    }

    /*
     * Для удаления истории по списку идентификаторов
     */
    private void removeAllHistory(Set<Integer> idSet) {
        idSet.forEach(history::remove);
    }

    private void putTask(Task task, Integer id) {
        validateTask(task);
        int taskId = Optional.ofNullable(id).orElse(getNextId());
        task.setId(taskId);
        tasks.put(taskId, task);
        replacePrioritized(task, null);
    }

    private void putEpic(Epic epic, Integer id) {
        validateEpic(epic);
        int epicId = Optional.ofNullable(id).orElse(getNextId());
        epic.setId(epicId);
        epics.put(epicId, epic);
    }

    private void putSubtask(Subtask subtask, Integer id) {
        int epicId = subtask.getEpicId();
        validateExists(epics, epicId);
        int subtaskId = Optional.ofNullable(id).orElse(getNextId());
        subtask.setId(subtaskId);
        validateTask(subtask);
        subtasks.put(subtaskId, subtask);
        replacePrioritized(subtask, null);
        Epic epic = epics.get(epicId);
        epic.addSubtask(subtask);
    }

    private void replacePrioritized(Task newTask, Task oldTask) {
        if (oldTask != null && oldTask.getStartTime() != null) {
            prioritizedTasks.remove(oldTask);
        }
        if (newTask.getStartTime() != null) {
            prioritizedTasks.add(newTask);
        }
    }

    private boolean hasTaskTimeConflict(Task task) {
        if (task.getStartTime() != null) {
            return prioritizedTasks.stream().anyMatch(storedTask ->
                    task.getId() != storedTask.getId() &&
                            task.getStartTime().isBefore(storedTask.getEndTime())  &&
                            task.getEndTime().isAfter(storedTask.getStartTime()));
        } else {
            return false;
        }
    }

    private void validateEpic(Epic epic) {
        if (epic.getName() == null || epic.getName().isEmpty()) {
            throw new TaskValidationException("не задано имя эпика");
        }
        if (epic.getDescription() == null || epic.getDescription().isEmpty()) {
            throw new TaskValidationException("не задано описание эпика");
        }
        if (epic.getSubtasks() == null) {
            throw new TaskValidationException("список подзадач эпика поврежден");
        }
        if (epic.getStatus() == null) {
            throw new TaskValidationException("статус эпика поврежден");
        }
    }

    private void validateTask(Task task) {
        if (task.getName() == null || task.getName().isEmpty()) {
            throw new TaskValidationException("не задано имя задачи");
        }
        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            throw new TaskValidationException("не задано описание задачи");
        }
        if (task.getStatus() == null) {
            throw new TaskValidationException("не задан статус задачи");
        }
        if (hasTaskTimeConflict(task)) {
            throw new TaskValidationException("Задача " + task.getName() + " конфликтует по времени с другой задачей");
        }
    }

    private void validateExists(HashMap<Integer, ? extends Task> taskContainer, int id) {
        if (!taskContainer.containsKey(id)) {
            throw new NotFoundException(NOT_FOUND_MESSAGE + id);
        }
    }

}
