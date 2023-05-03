package com.db.trading;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SignalExecutorFactory {

    private final Map<Integer, SignalExecutor> signalExecutors;
    private final SignalFallbackExecutor signalFallbackExecutor;

    private final Algo algo;

    public SignalExecutorFactory(Algo algo) {
        this.algo = algo;
        /* Set up signal executors */
        signalFallbackExecutor = new SignalFallbackExecutor(algo);
        signalExecutors = new HashMap<>();
        signalExecutors.put(1, new Signal1Executor(algo));
        signalExecutors.put(2, new Signal2Executor(algo));
        signalExecutors.put(3, new Signal3Executor(algo));
    }

    public SignalExecutor createSignalExecutor(int signal) {
        return signalExecutors.getOrDefault(signal, signalFallbackExecutor);
    }

}
