package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.NotFoundException;
import service.TaskManager;
import service.TaskValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        String methodName = exchange.getRequestMethod();
        try {
            switch (methodName) {
                case "GET":
                    getHandler(exchange);
                    break;
                case "POST":
                    postHandler(exchange);
                    break;
                case "DELETE":
                    deleteHandler(exchange);
                    break;
                default:
                    sendMethodNotAllowed(exchange);
            }
        } catch (TaskValidationException e) {
            sendResponse(exchange, 406, "Некорректные параметры запроса: " + e.getMessage());
        } catch (NotFoundException e) {
            sendResponse(exchange, 404, "Неизвестный идентификатор: " + e.getMessage());
        } catch (ENotAnId e) {
            sendResponse(exchange, 400, "Некорректный запрос: " + e.getMessage());
        } catch (Exception e) {
            sendResponse(exchange, 500, "Непредвиденная ошибка: " + e.getMessage());
        }
    }

    protected void getHandler(HttpExchange exchange) {
        sendMethodNotAllowed(exchange);
    }

    protected void postHandler(HttpExchange exchange) {
        sendMethodNotAllowed(exchange);
    }

    protected void deleteHandler(HttpExchange exchange) {
        sendMethodNotAllowed(exchange);
    }

    protected void sendObjectAsJson(HttpExchange exchange, Object obj) {
        String json = getGson().toJson(obj);
        sendOk(exchange, json);
    }

    protected void sendResponse(HttpExchange exchange, int code, String body) {
        try {
            sendHeaders(exchange, code);
            if (body != null) {
                sendBody(exchange, body);
            }
            exchange.close();
        } catch (IOException e) {
            System.out.println("Ошибка отправки http-ответа: " + e.getMessage());
        }
    }

    protected Optional<String> getPathAt(HttpExchange exchange, int position) {
        List<String> pathParts = getPathParts(exchange);
        if (pathParts.size() <= position) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(pathParts.get(position));
        }
    }

    protected Optional<Integer> getId(HttpExchange exchange) {
        Optional<String> strId = getPathAt(exchange, 2);
        if (strId.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                return Optional.of(Integer.valueOf(strId.get()));
            } catch (NumberFormatException e) {
                throw new ENotAnId(strId.get() + " не является идентификатором");
            }
        }
    }

    protected String getRequestBody(HttpExchange exchange) {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String requestText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            if (requestText.isEmpty()) {
                throw new ERequestUnreadable("отсутствует тело запроса");
            }
            return requestText;
        } catch (IOException e) {
            throw new ERequestUnreadable("нечитаемое тело запроса - " + e.getMessage());
        }
    }

    protected void sendOk(HttpExchange exchange, String text) {
        sendResponse(exchange, 200, text);
    }

    protected void sendOk(HttpExchange exchange) {
        sendResponse(exchange, 201, null);
    }

    protected TaskManager getManager() {
        return HttpTaskServer.getTaskManager();
    }

    protected Gson getGson() {
        return HttpTaskServer.getGson();
    }

    private void sendMethodNotAllowed(HttpExchange exchange) {
        sendResponse(exchange, 405, "Метод не поддерживается");
    }

    private void sendHeaders(HttpExchange exchange, int code) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, 0);
    }

    private void sendBody(HttpExchange exchange, String body) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        }
    }

    private List<String> getPathParts(HttpExchange exchange) {
        return Arrays.asList(exchange.getRequestURI().getPath().split("/"));
    }

}
