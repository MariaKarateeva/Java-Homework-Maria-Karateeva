package org.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.config.AdapterConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class IotCoreRepository {

    private static final HttpClient CORE_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public CompletableFuture<HttpResponse<String>> send(String deviceId, byte[] body) {
        byte[] payload;
        try {
            var in = MAPPER.readTree(body);
            ObjectNode out = MAPPER.createObjectNode();
            out.put("deviceId", deviceId);
            out.put("time", in.path("time").asText(Instant.now().toString()));
            if (in.has("telemetryType")) {
                out.set("telemetryType", in.get("telemetryType"));
            } else {
                out.put("telemetryType", 1);          // {"temp":..} -> тип 1; свой справочник — поправь
            }
            if (in.has("value")) {
                out.set("value", in.get("value"));
            } else if (in.has("temp")) {
                out.set("value", in.get("temp"));
            } else {
                out.set("value", in);
            }
            payload = MAPPER.writeValueAsBytes(out);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }

        HttpRequest coreReq = HttpRequest.newBuilder(URI.create(AdapterConfig.CORE_URL))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("X-Device-Id", deviceId)
                .POST(HttpRequest.BodyPublishers.ofByteArray(payload))
                .build();

        return CORE_CLIENT.sendAsync(coreReq, HttpResponse.BodyHandlers.ofString());
    }
}