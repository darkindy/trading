package com.db.trading;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TradingApplicationConfig {

    @Bean
    public Algo algo() {
        return new Algo();
    }

}
