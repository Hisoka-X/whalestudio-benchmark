package io.whaleops.whaletunnel.benchmark.cli.utils;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL;
import static com.fasterxml.jackson.databind.MapperFeature.REQUIRE_SETTERS_FOR_GETTERS;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
        .configure(READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
        .configure(REQUIRE_SETTERS_FOR_GETTERS, true)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .addModule(new JavaTimeModule())
        .defaultTimeZone(TimeZone.getDefault())
        .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
        .build();

    public static String toJsonString(Object object, SerializationFeature feature) {
        try {
            if (feature == null) {
                return OBJECT_MAPPER.writeValueAsString(object);
            }
            ObjectWriter writer = OBJECT_MAPPER.writer(feature);
            return writer.writeValueAsString(object);
        } catch (Exception e) {
            throw new IllegalArgumentException("Object to json string error", e);
        }
    }

    public static String toJsonString(Object object) {
        return toJsonString(object, null);
    }

    public static String toPrettyJsonString(Object object) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new IllegalArgumentException("Object to pretty json exception.", e);
        }
    }

    public static <T> T toObject(String response, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(response, typeReference);
        } catch (Exception e) {
            throw new IllegalArgumentException("Json to object exception.", e);
        }
    }
}
