package io.whaleops.whaletunnel.benchmark.cli.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Map;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@UtilityClass
public class OkHttpUtils {

    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static String get(String url) throws IOException {
        return get(url, null, null);
    }

    public static String get(String url,
                             Map<String, String> httpHeaders,
                             Map<String, Object> requestParams) throws IOException {
        checkNotNull(url);

        String finalUrl = addUrlParams(requestParams, url);
        Request.Builder requestBuilder = new Request.Builder().url(finalUrl);
        addHeader(httpHeaders, requestBuilder);
        Request request = requestBuilder.build();
        try (Response response = CLIENT.newCall(request).execute()) {
            return getResponseBody(response);
        }
    }

    public static String post(String url,
                              Map<String, String> httpHeaders,
                              Map<String, Object> requestParamsMap,
                              Object requestBody) throws IOException {
        String finalUrl = addUrlParams(requestParamsMap, url);
        Request.Builder requestBuilder = new Request.Builder().url(finalUrl);
        if (requestBody != null) {
            requestBuilder = requestBuilder.post(RequestBody.create(MediaType.parse("application/json"), JsonUtils.toJsonString(requestBody)));
        }
        try (Response response = CLIENT.newCall(requestBuilder.build()).execute()) {
            return getResponseBody(response);
        }
    }

    private static String addUrlParams(Map<String, Object> requestParams, @NonNull String url) {
        if (requestParams == null) {
            return url;
        }

        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new IllegalArgumentException(String.format("url: %s is invalid", url));
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        return urlBuilder.toString();
    }

    private static void addHeader(Map<String, String> headers, @NonNull Request.Builder requestBuilder) {
        if (headers == null) {
            return;
        }
        headers.forEach(requestBuilder::addHeader);
    }

    private static String getResponseBody(@NonNull Response response) throws IOException {
        if (response.code() != 200 || response.body() == null) {
            throw new RuntimeException(String.format("Request execute failed, httpCode: %s, httpBody: %s",
                response.code(),
                response.body() == null ? null : response.body().string()));
        }
        return response.body().string();
    }
}
