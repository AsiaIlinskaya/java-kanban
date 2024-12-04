package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    protected void getHandler(HttpExchange exchange) {
        sendObjectAsJson(exchange, getManager().getPrioritizedTasks());
    }

}
