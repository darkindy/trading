package com.db.trading;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Signal3Executor implements SignalExecutor {

    private final Algo algo;

    @Override
    public void execute() {
        algo.setAlgoParam(1, 90);
        algo.setAlgoParam(2, 15);
        algo.performCalc();
        algo.submitToMarket();
        algo.doAlgo();
    }
}
