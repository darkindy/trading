package com.db.trading;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class SignalSpecExecutor implements SignalExecutor {

    public static final Pattern actionSetAlgoParamRegex = Pattern.compile("setAlgoParam\\((\\d+),\\s*(\\d+)\\)");

    private final SignalSpec signalSpec;
    private final List<Runnable> actions;

    public SignalSpecExecutor(SignalSpec signalSpec, Algo algo) {
        this.signalSpec = signalSpec;
        this.actions = new ArrayList<>();
        for (String action : signalSpec.getActions()) {
            switch (action) {
                case "doAlgo" -> actions.add(algo::doAlgo);
                case "cancelTrades" -> actions.add(algo::cancelTrades);
                case "reverse" -> actions.add(algo::reverse);
                case "submitToMarket" -> actions.add(algo::submitToMarket);
                case "performCalc" -> actions.add(algo::performCalc);
                case "setUp" -> actions.add(algo::setUp);
                default -> {
                    /* Mapping parameterized actions */
                    List<Supplier<Runnable>> parameterizedActionMapper = List.of(
                            () -> mapSetAlgoParamAction(action, algo));
                    Runnable actionRunnable = parameterizedActionMapper.stream()
                            .map(Supplier::get)
                            .filter(Objects::nonNull)
                            .findAny()
                            .orElseThrow(() -> new RuntimeException("Invalid action in signal " + signalSpec.getId() + " spec: " + action));
                    actions.add(actionRunnable);
                }
            }
        }
        actions.add(algo::doAlgo);
    }

    private Runnable mapSetAlgoParamAction(String action, Algo algo) {
        Matcher matcher = actionSetAlgoParamRegex.matcher(action);
        if (matcher.matches()) {
            String param1 = matcher.group(1);
            String param2 = matcher.group(2);
            return (() -> algo.setAlgoParam(Integer.parseInt(param1), Integer.parseInt(param2)));
        }
        return null;
    }

    @Override
    public void execute() {
        for (Runnable action : actions) {
            action.run();
        }
    }
}
