package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(file);
    }

    public FileBackedTaskManager(File file) {
        this.file = file;
        load();
    }

    private void load() {
        try {
            if (!Files.exists(file.toPath())) {
                return;
            }
            List<String> taskLines = Files.readAllLines(file.toPath());
            if (taskLines.size() < 2) {
                return;
            }
            if (!taskLines.get(0).equals(TaskSerializer.getHeader())) {
                throw new ManagerFileException("Incorrect file header for FileBackedTaskManager");
            }
            taskLines.remove(0);
            int maxId = 0;
            for (String taskLine : taskLines) {
                Task task = TaskSerializer.fromCSV(taskLine);
                if (task != null) {
                    putTaskGeneral(task, task.getId());
                    if (task.getId() > maxId) {
                        maxId = task.getId();
                    }
                }
            }
            setNextId(maxId + 1);
        } catch (IOException e) {
            throw new ManagerFileException("Error while file reading in FileBackedTaskManager", e);
        }
    }

    private void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(TaskSerializer.getHeader());
            for (Task task : getAllTasks()) {
                writer.write("\n" + TaskSerializer.toCSV(task));
            }
            for (Epic epic : getAllEpics()) {
                writer.write("\n" + TaskSerializer.toCSV(epic));
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write("\n" + TaskSerializer.toCSV(subtask));
            }
        } catch (IOException e) {
            throw new ManagerFileException("Error while file saving in FileBackedTaskManager", e);
        }
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void putTask(Task task) {
        super.putTask(task);
        save();
    }

    @Override
    public void putEpic(Epic epic) {
        super.putEpic(epic);
        save();
    }

    @Override
    public void putSubtask(Subtask subtask) {
        super.putSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }



}