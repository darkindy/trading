package com.db.trading;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignalHandlerService implements SignalHandler {

    private final SignalExecutorFactory signalExecutorFactory;

    @Override
    public void handleSignal(int signal) {
        SignalExecutor signalExecutor = signalExecutorFactory.createSignalExecutor(signal);
        signalExecutor.execute();
    }
}
