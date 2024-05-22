import java.util.Objects;

/*
    Задача
 */
public class Task {

    private static int nextId; // числовое поле-счётчик для генерации идентификаторов

    private final int id; // Уникальный идентификационный номер задачи, по которому её можно будет найти
    private final String name; // Название, кратко описывающее суть задачи (например, «Переезд»)
    private final String description; // Описание, в котором раскрываются детали
    private TaskStatus status; // Статус, отображающий её прогресс

    public Task(String name, String description) {
        this.id = getNextId();
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    /*
            Генерирует новый Id
         */
    private static int getNextId() {
        return nextId++;
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
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
