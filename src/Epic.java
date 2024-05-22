import java.util.LinkedList;
import java.util.List;

public class Epic extends Task {

    private final List<Subtask> subtasks; // Каждый эпик знает, какие подзадачи в него входят

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new LinkedList<>();
    }

    /*
            Метод для перерасчета статуса.
            Завершение всех подзадач эпика считается завершением эпика.
         */
    void refreshStatus() {
        // если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW
       if (subtasks.isEmpty() || hasSubtasksSameStatus(TaskStatus.NEW)) {
           super.setStatus(TaskStatus.NEW);
       }
       // если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE
       else if (hasSubtasksSameStatus(TaskStatus.DONE)) {
           super.setStatus(TaskStatus.DONE);
        }
       // во всех остальных случаях статус должен быть IN_PROGRESS
       else {
           super.setStatus(TaskStatus.IN_PROGRESS);
       }
    }

    private boolean hasSubtasksSameStatus(TaskStatus taskStatus) {
        if (subtasks.isEmpty()) {
            return false;
        }
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != taskStatus) {
                return false;
            }
        }
        return true;
    }

    List<Subtask> getSubtasks() {
        return subtasks;
    }

    public Subtask makeSubtask(String name, String description) {
        Subtask subtask = new Subtask(name, description, this);
        subtasks.add(subtask);
        refreshStatus();
        return subtask;
    }

    public void removeSubtask(Subtask subtask) {
        if (subtasks.remove(subtask)) {
            refreshStatus();
        }
    }

    @Override
    protected void setStatus(TaskStatus taskStatus) {
        // пустой Эпик можно сразу закрыть
        if (subtasks.isEmpty() && taskStatus == TaskStatus.DONE) {
            super.setStatus(taskStatus);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
