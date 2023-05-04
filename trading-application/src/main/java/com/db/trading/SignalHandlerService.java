package com.db.trading;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SignalHandlerService implements SignalHandler {

    private final ObjectProvider<SignalExecutor> signalExecutorProvider;

    @Override
    public void handleSignal(int signal) {
        log.info("Processing signal {}", signal);
        signalExecutorProvider.getObject(signal).execute();
    }
}
