package service;

import model.*;

public class TaskSerializer {

    private static final String HEADER = "id,type,name,status,description,epic";

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
        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(name, description, Integer.parseInt(epicId), status);
                subtask.setId(id);
                return subtask;
            default:
                return null;
        }
    }

    public static String getHeader() {
        return HEADER;
    }

    private static String toCSV(Task task, String epicId) {
        String outTemplate = "%d,%s,%s,%s,%s,%s";
        return String.format(outTemplate,
                task.getId(), task.getTaskType(), task.getName(), task.getStatus(), task.getDescription(), epicId);
    }

}
