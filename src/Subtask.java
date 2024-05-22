public class Subtask extends Task {

    private final Epic epic; // Для каждой подзадачи известно, в рамках какого эпика она выполняется

    Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    @Override
    protected void setStatus(TaskStatus taskStatus) {
        super.setStatus(taskStatus);
        epic.refreshStatus();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epic.getId() +
                '}';
    }
}
