package com.db.trading;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Configuration
public class TradingApplicationConfig {

    private final SignalExecutorFactory signalExecutorFactory;

    @Bean
    public Supplier<Algo> algo() {
        return Algo::new;
    }

    @Bean
    @Primary
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SignalExecutor signalExecutor(int signal) {
        return signalExecutorFactory.getSignalExecutor(signal);
    }

}
