package http;

import com.sun.net.httpserver.HttpExchange;
import model.Task;

import java.util.List;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {

    @Override
    protected void getHandler(HttpExchange exchange) {
        Optional<Integer> id = getId(exchange);
        id.ifPresentOrElse(taskId -> returnTask(exchange, taskId),
                           () -> returnTasks(exchange));
    }

    @Override
    protected void postHandler(HttpExchange exchange) {

    }

    @Override
    protected void deleteHandler(HttpExchange exchange) {
        Optional<Integer> id = getId(exchange);
        returnDeleteResult(exchange,
                           id.orElseThrow(() -> new ENotAnId("не указан идентификатор")));
    }

    private void returnDeleteResult(HttpExchange exchange, int id) {
        getManager().removeTask(id);
        sendOk(exchange);
    }

    private void returnTask(HttpExchange exchange, int id) {
        Task task = getManager().getTask(id);
        String json = getGson().toJson(task);
        sendOk(exchange, json);
    }

    private void returnTasks(HttpExchange exchange) {
        List<Task> tasks = getManager().getAllTasks();
        String json = getGson().toJson(tasks);
        sendOk(exchange, json);
    }

}
