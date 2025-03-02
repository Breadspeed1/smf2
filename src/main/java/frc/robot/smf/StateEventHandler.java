package frc.robot.smf;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class StateEventHandler<S extends Enum<S>> {
    private final HashMap<Class<?>, Function<?, Optional<S>>> handlerMap = new HashMap<>();

    public <T> void addHandler(Class<T> type, Function<T, Optional<S>> handler) {
        handlerMap.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    public <T> void trigger(T event) {
        ((Consumer<T>) handlerMap.get(event.getClass())).accept(event);
    }
}
