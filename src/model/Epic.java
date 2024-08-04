package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Epic extends Task {

    private final List<Subtask> subtasks; // Каждый эпик знает, какие подзадачи в него входят
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subtasks = new LinkedList<>();
    }

    /*
            Метод для перерасчета статуса.
            Завершение всех подзадач эпика считается завершением эпика.
         */
    public void refreshState() {
        int countDone = 0;
        int countNew = 0;
        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;
        Duration duration = null;
        for (Subtask subtask : subtasks) {
            switch (subtask.getStatus()) {
                case DONE:
                    countDone++;
                    break;
                case NEW:
                    countNew++;
            }
            if (subtask.getStartTime() != null &&
                    (earliestStart == null || subtask.getStartTime().isBefore(earliestStart))) {
                earliestStart = subtask.getStartTime();
            }
            if (subtask.getEndTime() != null &&
                    (latestEnd == null || subtask.getEndTime().isAfter(latestEnd))) {
                latestEnd = subtask.getEndTime();
            }
            if (subtask.getDuration() != null) {
                duration = duration == null ? subtask.getDuration() : duration.plus(subtask.getDuration());
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
        setStartTime(earliestStart);
        endTime = latestEnd;
        setDuration(duration);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeSubtask(Subtask subtask) {
        if (subtasks.remove(subtask)) {
            refreshState();
        }
    }

    public void clearSubtasks() {
        subtasks.clear();
        refreshState();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        refreshState();
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

}
