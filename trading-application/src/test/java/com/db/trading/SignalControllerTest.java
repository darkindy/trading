package com.db.trading;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * This class tests the API endpoint provided by the {@link SignalController}.
 * It does not test the logic of the signal handling itself. To see tests for the signal handling logic,
 * check out the {@link com.db.trading.SignalHandlerServiceTest} class.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SignalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private SignalHandler signalHandler;

    @Test
    void testHandleSignal() throws Exception {
        // given
        int signalId = 1;

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/signals/{id}", signalId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        verify(signalHandler, times(1)).handleSignal(signalId);
    }
}