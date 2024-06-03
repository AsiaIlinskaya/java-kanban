package Model;

public class Subtask extends Task {


    private final int epicId; // Для каждой подзадачи известно, в рамках какого эпика она выполняется

    public Subtask(String name, String description, int epicId, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Task.Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
