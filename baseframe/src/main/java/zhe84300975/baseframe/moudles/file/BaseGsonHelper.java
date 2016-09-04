package zhe84300975.baseframe.moudles.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/**
 * Created by zhaowencong on 16/8/19.
 * Describe:
 */
public class BaseGsonHelper {

    static Gson buildGson(){

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new TypeAdapter<Date>() {

            @Override
            public void write(JsonWriter writer, Date value) throws IOException {
                if (value == null) {
                    writer.nullValue();
                    return;
                }

                long num = value.getTime();
                num /= 1000;
                writer.value(num);
            }

            @Override
            public Date read(JsonReader reader) throws IOException {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                }

                long value = reader.nextLong();
                return new Date(value * 1000);
            }
        });
        return builder.create();

    }
}
