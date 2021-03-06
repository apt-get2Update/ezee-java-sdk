package com.rentmanager.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentmanager.exception.RentManagerClientException;
import com.rentmanager.exception.RentManagerException;
import com.rentmanager.exception.RentManagerServerException;

import java.beans.JavaBean;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RequestBuilder<T> {

    private static final Integer MAXPAGESIZE = 1000;
    private final Class<T> clazz;
    private final ObjectMapper objectMapper;
    private final String url;
    private final String token;
    private final String resourceName;
    private final HttpClient httpClient;

    RequestBuilder(Class<T> clazz, String url, String token, ObjectMapper objectMapper, HttpClient httpClient) {
        this.clazz = clazz;
        this.objectMapper = objectMapper;
        this.url = url;
        this.token = token;
        this.httpClient = httpClient;
        this.resourceName = clazz.getAnnotation(JavaBean.class).defaultProperty();

    }

    private Optional<String> getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }

        String resultString = result.toString();

        return resultString.length() == 0 ? Optional.empty()
                : Optional.of(resultString.substring(0, resultString.length() - 1));
    }

    public void consumeEntities(List<String> fields, List<String> embeds, List<String> ordering,
                                String filterExpression, Consumer<T> consumer) throws RentManagerException {
        int pageNumber = 1;
        AtomicInteger atomicInteger = new AtomicInteger(MAXPAGESIZE);
        while (atomicInteger.get() == MAXPAGESIZE) {
            atomicInteger.set(0);
            consumeEntities(fields, embeds, ordering, filterExpression, MAXPAGESIZE, pageNumber, entity -> {
                consumer.accept(entity);
                atomicInteger.incrementAndGet();
            });
            pageNumber++;
        }
    }

    public Optional<List<T>> getEntities(List<String> fields, List<String> embeds, List<String> ordering,
                                         String filterExpression, Integer pageSize, Integer pageNumber) throws RentManagerException {

        List<T> entities = new ArrayList<>();
        consumeEntities(fields,
                embeds,
                ordering,
                filterExpression,
                pageSize,
                pageNumber,
                entities::add);
        return Optional.ofNullable(entities);

    }

    private Map<String, String> getRequestParameter(List<String> fields, List<String> embeds, List<String> ordering, String filterExpression, Integer pageSize, Integer pageNumber) {
        Map<String, String> requestParameters = new HashMap<>();
        if (fields != null) {
            requestParameters.put("fields", fields.stream().collect(Collectors.joining(",")));
        }
        if (embeds != null) {
            requestParameters.put("embeds", embeds.stream().collect(Collectors.joining(",")));
        }
        if (ordering != null) {
            requestParameters.put("orderingOptions", ordering.stream().collect(Collectors.joining(",")));
        }
        if (filterExpression != null) {
            requestParameters.put("filters", filterExpression);
        }
        if (pageSize != null) {
            requestParameters.put("PageSize", pageSize.toString());
        }
        if (pageNumber != null) {
            requestParameters.put("PageNumber", pageNumber.toString());

        }
        return requestParameters;
    }

    private void consumeEntities(List<String> fields, List<String> embeds, List<String> ordering,
                                 String filterExpression, Integer pageSize, Integer pageNumber, Consumer<T> consumer) throws RentManagerException {


        final StringBuilder entitiesUrl = new StringBuilder(this.url + "/" + this.resourceName);

        Map<String, String> requestParameters = getRequestParameter(fields, embeds, ordering, filterExpression, pageSize, pageNumber);

        try {
            getParamsString(requestParameters).ifPresent(paramString -> {
                entitiesUrl.append("?").append(paramString);
            });

            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(entitiesUrl.toString()))
                    .setHeader("Content-Type", "application/json").setHeader("X-RM12Api-ApiToken", this.token).build();

            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int responseCode = response.statusCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {

                try (JsonParser jsonParser = objectMapper.getFactory().createParser(response.body())) {
                    if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                        throw new RentManagerException(" illicalstate of array", null);
                    }
                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        T entity = objectMapper.readValue(jsonParser, this.clazz);
                        consumer.accept(entity);
                    }
                }

            } else {
                handleException(response);
            }

        } catch (InterruptedException | IOException | NullPointerException e) {
            throw new RentManagerException("unable get entities", e);
        }
    }

    public Optional<T> getEntity(Long id, List<String> embeds) throws RentManagerException {
        Objects.requireNonNull(id);
        Optional<T> entity = null;

        final StringBuilder entitiesUrl = new StringBuilder(this.url + "/" + this.resourceName + "/" + id);

        Map<String, String> requestParameters = new HashMap<>();

        if (embeds != null) {
            requestParameters.put("embeds", embeds.stream().collect(Collectors.joining(",")));
        }
        try {
            getParamsString(requestParameters).ifPresent(paramString -> {
                entitiesUrl.append("?").append(paramString);
            });

            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(entitiesUrl.toString()))
                    .setHeader("Content-Type", "application/json").setHeader("X-RM12Api-ApiToken", this.token).build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int responseCode = response.statusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                entity = Optional.of(objectMapper.readValue(response.body(), this.clazz));
            } else if (responseCode != HttpURLConnection.HTTP_NOT_FOUND) {
                entity = Optional.empty();
            } else {
                handleException(response);
            }
        } catch (InterruptedException | IOException e) {
            throw new RentManagerException("unable get entity", e);
        }
        return entity;
    }

    private void handleException(HttpResponse<String> response) throws JsonProcessingException, RentManagerClientException, RentManagerServerException {

        Map<String, Object> errorResponse = objectMapper.readValue(response.body(), Map.class);
        int responseCode = response.statusCode();

        if (responseCode >= 400 && responseCode < 500) {

            RentManagerClientException.ModelState modelState = new RentManagerClientException.ModelState((List<String>) ((Map) errorResponse.get("ModelState")).get("filters"));
            RentManagerClientException rentManagerClientException = new RentManagerClientException(errorResponse.get("Message").toString(),
                    modelState);

            throw rentManagerClientException;

        } else {

            RentManagerServerException rentManagerServerException = new RentManagerServerException((String) errorResponse.get("UserMessage"), (String) errorResponse.get("DeveloperMessage"),
                    (Integer) errorResponse.get("ErrorCode"), (String) errorResponse.get("MoreInfoUri"),
                    (String) errorResponse.get("Exception"), (String) errorResponse.get("Details"),
                    (String) errorResponse.get("InnerException"),
                    (Map<String, Object>) errorResponse.get("AdditionalData"));

            throw rentManagerServerException;
        }
    }
}