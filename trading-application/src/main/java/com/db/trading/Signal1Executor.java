package com.db.trading;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Signal1Executor implements SignalExecutor {

    private final Algo algo;

    @Override
    public void execute() {
        algo.setUp();
        algo.setAlgoParam(1, 60);
        algo.performCalc();
        algo.submitToMarket();
        algo.doAlgo();
    }
}
