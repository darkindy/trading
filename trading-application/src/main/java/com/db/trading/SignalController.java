package com.db.trading;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequestMapping("/api/signals")
@RequiredArgsConstructor
@RestController
public class SignalController {

    private final SignalHandler signalHandler;

    @PostMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> handleSignal(@PathVariable("id") int signal) {
        return Mono.fromRunnable(() -> signalHandler.handleSignal(signal))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

}