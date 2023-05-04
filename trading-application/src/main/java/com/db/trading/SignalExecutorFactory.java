package com.db.trading;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SignalExecutorFactory {

    private final Algo algo;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    private final Map<Integer, SignalSpec> signalSpecs;

    public SignalExecutorFactory(Algo algo, ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.algo = algo;
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        signalSpecs = readSignalSpecsJson();
    }

    public SignalExecutor createSignalExecutor(int signal) {
        if (signalSpecs.containsKey(signal)) {
            return new SignalSpecExecutor(signalSpecs.get(signal), algo);
        }
        /* Custom-implementation signal executors can be armed here */
        log.warn("Unknown signal: {}. Executing fallback signal.", signal);
        return new SignalFallbackExecutor(algo);
    }

    private Map<Integer, SignalSpec> readSignalSpecsJson() {
        try {
            Resource resource = resourceLoader.getResource("classpath:signal-specs.json");
            Map<Integer, SignalSpec> specsBySignalId = new HashMap<>();
            List<SignalSpec> signalSpecs = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
            });
            for (SignalSpec signalSpec : signalSpecs) {
                specsBySignalId.put(signalSpec.getId(), signalSpec);
                log.info("Configuring signal {} spec actions: {}", signalSpec.getId(), signalSpec.getActions());
            }
            return specsBySignalId;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
