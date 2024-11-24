package http;

import com.sun.net.httpserver.HttpExchange;
import model.Task;

import java.util.List;

public class TasksHandler extends BaseHttpHandler {

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

    @Override
    protected List<? extends Task> getTasksFromManager() {
        return getManager().getAllTasks();
    }

    @Override
    protected void sendTasksToManager(String requestBody) {
        Task task = getGson().fromJson(requestBody, Task.class);
        if (task.getId() == 0) {
            getManager().putTask(task);
        } else {
            getManager().updateTask(task);
        }
    }

    @Override
    protected void doRemove(int id) {
        getManager().removeTask(id);
    }

}
