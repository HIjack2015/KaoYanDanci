package cn.jk.kaoyandanci.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * <pre>
 *     author : jiakang
 *     e-mail : 1079153785@qq.com
 *     time   : 2018/07/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DateLongGson {
    static JsonSerializer<Date> ser = new JsonSerializer<Date>() {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                context) {
            return src == null ? null : new JsonPrimitive(src.getTime());
        }
    };

    static JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, ser)
            .registerTypeAdapter(Date.class, deser).create();
}
