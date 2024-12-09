package http;

import com.sun.net.httpserver.HttpExchange;
import model.Task;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class BaseTasksHandler<T extends Task> extends BaseHttpHandler {

    protected abstract Task getTaskFromManager(int id);

    protected abstract Class<? extends Task> getTaskClass();

    protected abstract Consumer<T> getTaskCreator();

    protected abstract Consumer<T> getTaskUpdater();

    protected abstract List<? extends Task> getTasksFromManager();

    protected abstract void doRemove(int id);

    protected void returnTasksByIdOrAll(HttpExchange exchange) {
        Optional<Integer> id = getId(exchange);
        Object object  = id.isPresent() ? getTaskFromManager(id.get()) : getTasksFromManager();
        sendObjectAsJson(exchange, object);
    }

    protected void returnDeleteResult(HttpExchange exchange) {
        Optional<Integer> id = getId(exchange);
        doRemove(id.orElseThrow(() -> new ENotAnId("не указан идентификатор")));
        sendOk(exchange);
    }

    protected void returnPostResult(HttpExchange exchange) {
        String requestBody = getRequestBody(exchange);
        Optional<Integer> createdId = sendTasksToManager(requestBody);
        createdId.ifPresentOrElse(id -> sendOk(exchange, getGson().toJson(id)),
                () -> sendOk(exchange));
    }

    private Optional<Integer> sendTasksToManager(String requestBody) {
        @SuppressWarnings("unchecked") // наследники должны выдавать класс типа T
        T task = (T) getGson().fromJson(requestBody, getTaskClass());
        if (task.getId() == 0) {
            getTaskCreator().accept(task);
            return Optional.of(task.getId());
        } else {
            getTaskUpdater().accept(task);
            return Optional.empty();
        }
    }

}
