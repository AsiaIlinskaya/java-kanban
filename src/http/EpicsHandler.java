package http;

import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import model.Task;
import service.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EpicsHandler extends BaseTasksHandler<Epic> {

    @Override
    protected void getHandler(HttpExchange exchange) {
        Optional<String> pathLevel3 = getPathAt(exchange, 3);
        if (pathLevel3.isEmpty()) {
            returnTasksByIdOrAll(exchange);
        } else if (pathLevel3.get().equals("subtasks")) {
            Optional<Integer> id = getId(exchange);
            @SuppressWarnings("OptionalGetWithoutIsPresent") // есть pathLevel3, значит id на pathLevel2 тоже
            List<Subtask> subtasks = getManager().getSubtasks(id.get());
            sendObjectAsJson(exchange, subtasks);
        } else {
            throw  new NotFoundException(pathLevel3.get() + " - не поддерживается");
        }
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
        return getManager().getEpic(id);
    }

    @Override
    protected List<? extends Task> getTasksFromManager() {
        return getManager().getAllEpics();
    }

    @Override
    protected Class<? extends Task> getTaskClass() {
        return Epic.class;
    }

    @Override
    protected Consumer<Epic> getTaskCreator() {
        return getManager()::putEpic;
    }

    @Override
    protected Consumer<Epic> getTaskUpdater() {
        return getManager()::updateEpic;
    }

    @Override
    protected void doRemove(int id) {
        getManager().removeEpic(id);
    }

}
