package com.db.trading;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class SignalHandlerServiceTest {

    @MockBean
    Algo algo;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private SignalHandlerService signalHandlerService;

    @ParameterizedTest
    @MethodSource("signalSpecs")
    void testHandleSignalAllSpecs(int signalId, List<String> actions) throws InvocationTargetException, IllegalAccessException {
        signalHandlerService.handleSignal(signalId);
        InOrder orderVerifier = Mockito.inOrder(algo);
        for (String action : actions) {
            verifyMethod(orderVerifier, algo, action);
        }
        orderVerifier.verify(algo).doAlgo();
        orderVerifier.verifyNoMoreInteractions();
    }

    private Stream<Arguments> signalSpecs() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:signal-specs.json");
        List<SignalSpec> signalSpecs = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });
        return signalSpecs.stream().map(spec -> Arguments.of(spec.getId(), spec.getActions()));
    }

    private void verifyMethod(InOrder orderVerifier, Object obj, String methodName) throws InvocationTargetException, IllegalAccessException {
        Collection<Invocation> invocations = Mockito.mockingDetails(obj).getInvocations();
        for (Invocation invocation : invocations) {
            if (invocation.getMethod().getName().equals(methodName)) {
                Method method = invocation.getMethod();
                Object[] arguments = invocation.getArguments();
                method.invoke(orderVerifier.verify(invocation.getMock()), arguments);
                break;
            }
        }
    }

    /* Older per-signal tests */

    @Disabled
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

    @Disabled
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

    @Disabled
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
