package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class HttpTestHelper {

    private static final String SERVER_URL = "http://localhost:8080/";
    private static final HttpClient client = HttpClient.newHttpClient();

    static HttpResponse<String> sendPostRequest(String endpoint, String json) throws IOException, InterruptedException {
        URI url = URI.create(SERVER_URL + endpoint);
        HttpRequest request = HttpRequest
              .newBuilder()
              .uri(url)
              .POST(HttpRequest
                      .BodyPublishers
                      .ofString(json))
              .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    static int postNgetStatus(String endpoint, String json) throws IOException, InterruptedException {
        return sendPostRequest(endpoint, json).statusCode();
    }

    static HttpResponse<String> sendGetRequest(String endpoint) throws IOException, InterruptedException {
        URI url = URI.create(SERVER_URL + endpoint);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    static int delRequest(String endpoint) throws IOException, InterruptedException {
        URI url = URI.create(SERVER_URL + endpoint);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

}
