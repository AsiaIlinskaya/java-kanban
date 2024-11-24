package http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(duration.getSeconds());
        }
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        return Duration.ofSeconds((jsonReader.nextLong()));
    }

}
