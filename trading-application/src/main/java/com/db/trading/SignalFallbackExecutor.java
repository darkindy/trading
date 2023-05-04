package com.db.trading;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignalFallbackExecutor implements SignalExecutor {

    private final Algo algo;

    @Override
    public void execute() {
        algo.cancelTrades();
        algo.doAlgo();
    }
}
