package com.db.trading;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignalHandlerService implements SignalHandler {

    private final Algo algo;

    @Override
    public void handleSignal(int signal) {
        switch (signal) {
            case 1:
                algo.setUp();
                algo.setAlgoParam(1, 60);
                algo.performCalc();
                algo.submitToMarket();
                break;

            case 2:
                algo.reverse();
                algo.setAlgoParam(1, 80);
                algo.submitToMarket();
                break;

            case 3:
                algo.setAlgoParam(1, 90);
                algo.setAlgoParam(2, 15);
                algo.performCalc();
                algo.submitToMarket();
                break;

            default:
                algo.cancelTrades();
                break;
        }

        algo.doAlgo();
    }
}
