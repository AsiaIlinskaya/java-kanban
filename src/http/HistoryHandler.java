package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    protected void getHandler(HttpExchange exchange) {
        sendObjectAsJson(exchange, getManager().getHistory());
    }

}
