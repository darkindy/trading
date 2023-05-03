package com.db.trading;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Signal2Executor implements SignalExecutor {

    private final Algo algo;

    @Override
    public void execute() {
        algo.reverse();
        algo.setAlgoParam(1, 80);
        algo.submitToMarket();
        algo.doAlgo();
    }
}
