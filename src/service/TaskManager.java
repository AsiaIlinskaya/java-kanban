package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    /*
                Получение списка всех задач типа Задача
             */
    List<Task> getAllTasks();

    /*
            Получение списка всех задач типа Эпик
        */
    List<Epic> getAllEpics();

    /*
            Получение списка всех задач типа Подзадача
         */
    List<Subtask> getAllSubtasks();

    /*
            Удаление списка всех задач типа Задача
         */
    void removeAllTasks();

    /*
            Удаление списка всех задач типа Эпик
        */
    void removeAllEpics();

    /*
            Удаление списка всех задач типа Подзадача
         */
    void removeAllSubtasks();

    /*
            Получение задачи по ID
         */
    Task getTask(int id);

    /*
            Получение Эпика по ID
         */
    Epic getEpic(int id);

    /*
            Получение Подзадачи по ID
         */
    Subtask getSubtask(int id);

    /*
            Создание Задачи
         */
    void putTask(Task task);

    /*
            Создание Эпика
         */
    void putEpic(Epic epic);

    void putSubtask(Subtask subtask);

    /*
            Удаление Задачи
         */
    void removeTask(int id);

    /*
            Удаление Эпика
         */
    void removeEpic(int id);

    /*
            Удаление Подзадачи
         */
    void removeSubtask(int id);

    /*
            Обновление Задачи
         */
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    /*
            Получение списка всех подзадач определённого эпика.
         */
    List<Subtask> getSubtasks(int id);

    /*
        Возвращает историю просмотров задач
     */
    List<Task> getHistory();

}
