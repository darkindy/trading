package com.db.trading;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
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
    private final Map<Integer, SignalExecutor> signalExecutorSingletons = new HashMap<>();

    private final Algo algo;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final SignalExecutor fallbackExecutor;

    private final Map<Integer, SignalSpec> signalSpecs;

    public SignalExecutorFactory(@Lazy Algo algo, @Lazy @Qualifier("signalFallbackExecutor") SignalExecutor fallbackExecutor,
                                 ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.algo = algo;
        this.fallbackExecutor = fallbackExecutor;
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        signalSpecs = readSignalSpecsJson();
    }

    public SignalExecutor getSignalExecutor(int signal) {
        if (signalExecutorSingletons.containsKey(signal)) {
            return signalExecutorSingletons.get(signal);
        } else {
            SignalExecutor singletonInstance = createSignalExecutor(signal);
            signalExecutorSingletons.put(signal, singletonInstance);
            return singletonInstance;
        }
    }

    private SignalExecutor createSignalExecutor(int signal) {
        if (signalSpecs.containsKey(signal)) {
            return new SignalSpecExecutor(signalSpecs.get(signal), algo);
        }
        /* Custom-implementation signal executors can be armed here */
        log.warn("Configuring fallback executor for unknown signal: {}", signal);
        return fallbackExecutor;
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
            throw new IllegalStateException("Error occurred while loading signal-specs JSON", e);
        }
    }

}
