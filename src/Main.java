public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Создаем две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей");
        Task task1 = new Task("Task1 name", "Task1 descr");
        Task task2 = new Task("Task2 name", "Task2 descr");
        Epic epic2 = new Epic("Epic2 name", "Epic2 descr");
        Subtask subtask1 = epic2.makeSubtask("Subtask1 name", "Subtask1 descr");
        Subtask subtask2 = epic2.makeSubtask("Subtask2 name", "Subtask2 descr");
        Epic epic1 = new Epic("Epic1 name", "Epic1 descr");
        Subtask subtask3 = epic1.makeSubtask("Subtask3 name", "Subtask3 descr");


        taskManager.putTask(task1);
        taskManager.putTask(task2);
        taskManager.putEpic(epic1);
        taskManager.putEpic(epic2);
        taskManager.putSubtask(subtask1);
        taskManager.putSubtask(subtask2);
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
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.DONE);
        epic2.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask3.setStatus(TaskStatus.DONE);

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