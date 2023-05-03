package com.db.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignalExecutorFactory {

    @Autowired
    private Algo algo;

    public SignalExecutor createSignalExecutor(int signal) {
        switch (signal) {
            case 1:
                return () -> {
                    algo.setUp();
                    algo.setAlgoParam(1, 60);
                    algo.performCalc();
                    algo.submitToMarket();
                    algo.doAlgo();
                };

            case 2:
                return () -> {
                    algo.reverse();
                    algo.setAlgoParam(1, 80);
                    algo.submitToMarket();
                    algo.doAlgo();
                };

            case 3:
                return () -> {
                    algo.setAlgoParam(1, 90);
                    algo.setAlgoParam(2, 15);
                    algo.performCalc();
                    algo.submitToMarket();
                    algo.doAlgo();
                };

            default:
                return () -> {
                    algo.cancelTrades();
                    algo.doAlgo();
                };
        }
    }

}
