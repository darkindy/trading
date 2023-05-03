package com.db.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TradingApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TradingApplication.class, args);
        SignalHandler signalHandler = applicationContext.getBean(SignalHandler.class);
        signalHandler.handleSignal(1); /* First try calling the algorithm */
        applicationContext.close();
    }

}
