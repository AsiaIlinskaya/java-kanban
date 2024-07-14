package model;

import java.util.LinkedList;
import java.util.List;

public class Epic extends Task {

    private final List<Subtask> subtasks; // Каждый эпик знает, какие подзадачи в него входят

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subtasks = new LinkedList<>();
    }

    /*
            Метод для перерасчета статуса.
            Завершение всех подзадач эпика считается завершением эпика.
         */
    public void refreshStatus() {
        int countDone = 0;
        int countNew = 0;

        for (Subtask subtask : subtasks) {
            switch (subtask.getStatus()) {
                case DONE:
                    countDone++;
                    break;
                case NEW:
                    countNew++;
            }
        }

        int countSubtasks = subtasks.size();
        if (countSubtasks == 0) {
            setStatus(TaskStatus.NEW);
        } else if (countSubtasks == countDone) {
            setStatus(TaskStatus.DONE);
        } else if (countSubtasks == countNew) {
            setStatus(TaskStatus.NEW);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeSubtask(Subtask subtask) {
        if (subtasks.remove(subtask)) {
            refreshStatus();
        }
    }

    public void clearSubtasks() {
        subtasks.clear();
        refreshStatus();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        refreshStatus();
    }

    @Override
    public String toString() {
        return "Task.Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }
}
