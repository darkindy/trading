package com.db.trading;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class SignalHandlerServiceTest {

    @MockBean
    Algo algo;

    @Autowired
    private SignalHandlerService signalHandlerService;

    @Test
    public void testSignal1() {
        signalHandlerService.handleSignal(1);
        InOrder orderVerifier = Mockito.inOrder(algo);
        orderVerifier.verify(algo).setUp();
        orderVerifier.verify(algo).setAlgoParam(1, 60);
        orderVerifier.verify(algo).performCalc();
        orderVerifier.verify(algo).submitToMarket();
        orderVerifier.verify(algo).doAlgo();
        orderVerifier.verifyNoMoreInteractions();
    }

    @Test
    public void testSignal2() {
        signalHandlerService.handleSignal(2);
        InOrder orderVerifier = Mockito.inOrder(algo);
        orderVerifier.verify(algo).reverse();
        orderVerifier.verify(algo).setAlgoParam(1, 80);
        orderVerifier.verify(algo).submitToMarket();
        orderVerifier.verify(algo).doAlgo();
        orderVerifier.verifyNoMoreInteractions();
    }

    @Test
    public void testSignal3() {
        signalHandlerService.handleSignal(3);
        InOrder orderVerifier = Mockito.inOrder(algo);
        orderVerifier.verify(algo).setAlgoParam(1, 90);
        orderVerifier.verify(algo).setAlgoParam(2, 15);
        orderVerifier.verify(algo).performCalc();
        orderVerifier.verify(algo).submitToMarket();
        orderVerifier.verify(algo).doAlgo();
        orderVerifier.verifyNoMoreInteractions();
    }

    @Test
    public void testSignalDefault() {
        signalHandlerService.handleSignal(-1);
        InOrder orderVerifier = Mockito.inOrder(algo);
        orderVerifier.verify(algo).cancelTrades();
        orderVerifier.verify(algo).doAlgo();
        orderVerifier.verifyNoMoreInteractions();
    }

}
