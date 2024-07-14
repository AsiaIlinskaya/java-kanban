package model;

import java.util.Objects;

/*
    Задача
 */
public class Task {
    private int id; // Уникальный идентификационный номер задачи, по которому её можно будет найти
    private String name; // Название, кратко описывающее суть задачи (например, «Переезд»)
    private String description; // Описание, в котором раскрываются детали
    private TaskStatus status; // Статус, отображающий её прогресс

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.status = taskStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    protected void setStatus(TaskStatus taskStatus) {
        status = taskStatus;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

}
