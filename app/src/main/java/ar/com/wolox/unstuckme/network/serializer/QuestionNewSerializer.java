package ar.com.wolox.unstuckme.network.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import ar.com.wolox.unstuckme.model.QuestionNew;

/**
 * Created by agustinpagnoni on 8/2/15.
 */
public class QuestionNewSerializer implements JsonSerializer<QuestionNew> {



    public JsonElement serialize(QuestionNew src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();
        root.add("question", new Gson().toJsonTree(src, QuestionNew.class));
        return root;
    }
}