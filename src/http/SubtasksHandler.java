package http;

import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.function.Consumer;

public class SubtasksHandler extends BaseTasksHandler<Subtask> {

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
    protected Class<? extends Task> getTaskClass() {
        return Subtask.class;
    }

    @Override
    protected Consumer<Subtask> getTaskCreator() {
        return getManager()::putSubtask;
    }

    @Override
    protected Consumer<Subtask> getTaskUpdater() {
        return getManager()::updateSubtask;
    }

    @Override
    protected void doRemove(int id) {
        getManager().removeSubtask(id);
    }

}
