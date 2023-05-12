package com.db.trading;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class SignalSpecExecutor implements SignalExecutor {

    public static final Pattern actionSetAlgoParamRegex = Pattern.compile("setAlgoParam\\((\\d+),\\s*(\\d+)\\)");

    private final SignalSpec signalSpec;
    private final List<Consumer<Algo>> actions;
    private final Supplier<Algo> algoSupplier;

    public SignalSpecExecutor(SignalSpec signalSpec, Supplier<Algo> algoSupplier) {
        this.signalSpec = signalSpec;
        this.algoSupplier = algoSupplier;
        this.actions = new ArrayList<>();
        for (String action : signalSpec.getActions()) {
            switch (action) {
                case "doAlgo" -> actions.add(Algo::doAlgo);
                case "cancelTrades" -> actions.add(Algo::cancelTrades);
                case "reverse" -> actions.add(Algo::reverse);
                case "submitToMarket" -> actions.add(Algo::submitToMarket);
                case "performCalc" -> actions.add(Algo::performCalc);
                case "setUp" -> actions.add(Algo::setUp);
                default -> {
                    /* Mapping parameterized actions */
                    List<Supplier<Consumer<Algo>>> parameterizedActionMapper = List.of(
                            () -> mapSetAlgoParamAction(action));
                    Consumer<Algo> actionLogic = parameterizedActionMapper.stream()
                            .map(Supplier::get)
                            .filter(Objects::nonNull)
                            .findAny()
                            .orElseThrow(() -> new IllegalStateException("Invalid action in signal " + signalSpec.getId() + " spec: " + action));
                    actions.add(actionLogic);
                }
            }
        }
        actions.add(Algo::doAlgo);
    }

    private Consumer<Algo> mapSetAlgoParamAction(String action) {
        Matcher matcher = actionSetAlgoParamRegex.matcher(action);
        if (matcher.matches()) {
            String param1 = matcher.group(1);
            String param2 = matcher.group(2);
            return (algo -> algo.setAlgoParam(Integer.parseInt(param1), Integer.parseInt(param2)));
        }
        return null;
    }

    @Override
    public void execute() {
        Algo algo = algoSupplier.get();
        for (Consumer<Algo> action : actions) {
            action.accept(algo);
        }
    }
}
