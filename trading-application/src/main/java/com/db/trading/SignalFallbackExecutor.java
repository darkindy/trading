package com.db.trading;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class SignalFallbackExecutor implements SignalExecutor {

    private final Supplier<Algo> algoSupplier;

    @Override
    public void execute() {
        Algo algo = algoSupplier.get();
        algo.cancelTrades();
        algo.doAlgo();
    }
}
