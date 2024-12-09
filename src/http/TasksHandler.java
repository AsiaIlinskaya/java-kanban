package http;

import com.sun.net.httpserver.HttpExchange;
import model.Task;

import java.util.List;
import java.util.function.Consumer;

public class TasksHandler extends BaseTasksHandler<Task> {

    @Override
    protected void getHandler(HttpExchange exchange) {
        returnTasksByIdOrAll(exchange);
    }

    @Override
    protected void postHandler(HttpExchange exchange) {
        returnPostResult(exchange);
    }

    @Override
    protected void deleteHandler(HttpExchange exchange) {
        returnDeleteResult(exchange);
    }

    @Override
    protected Task getTaskFromManager(int id) {
        return getManager().getTask(id);
    }

    protected Class<? extends Task> getTaskClass() {
        return Task.class;
    }

    protected Consumer<Task> getTaskCreator() {
        return getManager()::putTask;
    }

    protected Consumer<Task> getTaskUpdater() {
        return getManager()::updateTask;
    }

    @Override
    protected List<? extends Task> getTasksFromManager() {
        return getManager().getAllTasks();
    }

    @Override
    protected void doRemove(int id) {
        getManager().removeTask(id);
    }

}
