package com.db.trading;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * This class tests the API endpoint provided by the {@link SignalController}.
 * It does not test the logic of the signal handling itself. To see tests for the signal handling logic,
 * check out the {@link com.db.trading.SignalHandlerServiceTest} class.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class SignalControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @SpyBean
    private SignalHandler signalHandler;

    @Test
    void testHandleSignal() {
        // given
        int signalId = 1;

        // when
        webTestClient.post()
                .uri("/api/signals/{id}", signalId)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class).consumeWith(result -> {
                    // then
                    assertNull(result.getResponseBody());
                    verify(signalHandler, times(1)).handleSignal(signalId);
                });
    }
}