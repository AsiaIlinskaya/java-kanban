package service;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskSerializer {

    private static final String HEADER = "id,type,name,status,description,epic,starttime,duration";
    private static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";

    public static String toCSV(Task task) {
        return toCSV(task, "");
    }

    public static String toCSV(Epic epic) {
        return toCSV(epic, "");
    }

    public static String toCSV(Subtask subtask) {
        return toCSV(subtask, Integer.toString(subtask.getEpicId()));
    }

    public static Task fromCSV(String str) {
        String[] params = str.split(",");
        int id = Integer.parseInt(params[0]);
        TaskType type = TaskType.valueOf(params[1]);
        String name = params[2];
        TaskStatus status = TaskStatus.valueOf(params[3]);
        String description = "";
        if (params.length > 4) {
            description = params[4];
        }
        String epicId = "";
        if (params.length > 5) {
            epicId = params[5];
        }
        LocalDateTime startTime = null;
        if (params.length > 6 && !params[6].isEmpty()) {
            startTime = LocalDateTime.parse(params[6], DateTimeFormatter.ofPattern(DATETIME_FORMAT));
        }
        Duration duration = null;
        if (params.length > 7 && !params[7].isEmpty()) {
            duration = Duration.ofSeconds(Long.parseLong(params[7]));
        }
        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                fillAttributes(task, id, startTime, duration);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(name, description, Integer.parseInt(epicId), status);
                fillAttributes(subtask, id, startTime, duration);
                return subtask;
            default:
                return null;
        }
    }

    public static String getHeader() {
        return HEADER;
    }

    private static String toCSV(Task task, String epicId) {
        String outTemplate = "%d,%s,%s,%s,%s,%s,%s,%s";
        return String.format(outTemplate,
            task.getId(), task.getTaskType(), task.getName(), task.getStatus(), task.getDescription(), epicId,
            task.getStartTime() == null ? "" : task.getStartTime().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)),
            task.getDuration() == null ? "" : Long.toString(task.getDuration().getSeconds()));
    }

    private static void fillAttributes(Task task, int id, LocalDateTime startTime, Duration duration) {
        task.setId(id);
        task.setStartTime(startTime);
        task.setDuration(duration);
    }

}
