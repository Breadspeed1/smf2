package frc.robot.smf;

import java.util.HashMap;
import java.util.Optional;

public class StateEventHandler<S extends Enum<S>> {
    private final HashMap<Class<?>, EventHandler<S, ?>> handlerMap = new HashMap<>();
    private final S state;

    public StateEventHandler(S state) {
        this.state = state;
    }

    /**
     * Add a handler for a specific type of message.
     * @param <T> the type of message
     * @param type the class type
     * @param handler the event handler to handle such events
     */
    public <T> void setHandler(Class<T> type, EventHandler<S, T> handler) {
        handlerMap.put(type, handler);
    }

    /**
     * Trigger the event handler for the type being sent.
     * @param <T> the type of event
     * @param event the event itself
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<S> trigger(T event) {
        if (handlerMap.containsKey(event.getClass())) {
            return ((EventHandler<S, T>) handlerMap.get(event.getClass())).handle(event);
        }

        return Optional.empty();
    }

    public void reset() {}

    public S getState() {
        return state;
    }
}
