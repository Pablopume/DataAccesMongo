package model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdAdapter extends TypeAdapter<ObjectId> {
    @Override
    public void write(JsonWriter jsonWriter, ObjectId objectId) throws IOException {
        if (objectId == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.beginObject();
            jsonWriter.name("$oid");
            jsonWriter.value(objectId.toString());
            jsonWriter.endObject();
        }
    }

    @Override
    public ObjectId read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            jsonReader.beginObject();
            String fieldname = jsonReader.nextName();
            ObjectId objectId = null;
            if ("$oid".equals(fieldname)) {
                objectId = new ObjectId(jsonReader.nextString());
            }
            jsonReader.endObject();
            return objectId;
        }
    }
}