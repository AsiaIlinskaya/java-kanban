public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Создаем две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей");
        System.out.println("------------------------------------");
        Task task1 = new Task("Task1 name", "Task1 descr", TaskStatus.NEW);
        Task task2 = new Task("Task2 name", "Task2 descr", TaskStatus.DONE);
        taskManager.putTask(task1);
        taskManager.putTask(task2);

        Epic epic2 = new Epic("Epic2 name", "Epic2 descr");
        taskManager.putEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask1 name", "Subtask1 descr", epic2.getId(), TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Subtask2 name", "Subtask2 descr", epic2.getId(), TaskStatus.NEW);
        taskManager.putSubtask(subtask1);
        taskManager.putSubtask(subtask2);

        Epic epic1 = new Epic("Epic1 name", "Epic1 descr");
        taskManager.putEpic(epic1);

        Subtask subtask3 = new Subtask("Subtask3 name", "Subtask3 descr",  epic1.getId(), TaskStatus.NEW);
        taskManager.putSubtask(subtask3);

        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Изменяем статусы созданных объектов,");
        System.out.println("------------------------------------");
        Task task11 = new Task(task1.getName(), task1.getDescription(), TaskStatus.IN_PROGRESS);
        task11.setId(task1.getId());
        taskManager.updateTask(task11);

        Subtask subtask11 = new Subtask(subtask1.getName(), subtask1.getDescription(), subtask1.getEpicId(), TaskStatus.IN_PROGRESS);
        subtask11.setId(subtask1.getId());
        taskManager.updateSubtask(subtask11);

        Subtask subtask31 = new Subtask(subtask3.getName(), subtask3.getDescription(), subtask3.getEpicId(), TaskStatus.DONE);
        subtask31.setId(subtask3.getId());
        taskManager.updateSubtask(subtask31);

        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Удаляем одну из задач и один из эпиков.");
        System.out.println("------------------------------------");
        taskManager.removeTask(task1.getId());
        taskManager.removeEpic(epic1.getId());

        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }

    }
}