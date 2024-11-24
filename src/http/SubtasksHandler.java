package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {

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
        return getManager().getSubtask(id);
    }

    @Override
    protected List<? extends Task> getTasksFromManager() {
        return getManager().getAllSubtasks();
    }

    @Override
    protected void sendTasksToManager(String requestBody) {
        Subtask subtask = getGson().fromJson(requestBody, Subtask.class);
        if (subtask.getId() == 0) {
            getManager().putSubtask(subtask);
        } else {
            getManager().updateSubtask(subtask);
        }
    }

    @Override
    protected void doRemove(int id) {
        getManager().removeSubtask(id);
    }

}
