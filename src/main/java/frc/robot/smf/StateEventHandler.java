package frc.robot.smf;

import java.util.HashMap;
import java.util.Optional;

import frc.robot.smf.util.Pair;

public class StateEventHandler<S extends Enum<S>, T extends Enum<T>> {
    private final HashMap<Pair<Class<?>, T>, EventHandler<S, ?>> handlerMap = new HashMap<>();
    private final S state;
    
    private final Pair<Class<?>, T> keybuf = new Pair<Class<?>, T>(null, null);

    public StateEventHandler(S state) {
        this.state = state;
    }

    /**
     * Add a handler for a specific type of message.
     * @param <E> the type of message
     * @param type the class type
     * @param handler the event handler to handle such events
     */
    public <E> void setHandler(Class<E> type, T topic, EventHandler<S, E> handler) {
        handlerMap.put(new Pair<Class<?>, T>(type, topic), handler);
    }

    /**
     * Handle the event handler for the type being sent.
     * @param <E> the type of event
     * @param event the event itself
     */
    @SuppressWarnings("unchecked")
    public <E> Optional<S> handle(E event, T topic) {
        keybuf.left = event.getClass();
        keybuf.right = topic;
        if (handlerMap.containsKey(keybuf)) {
            return ((EventHandler<S, E>) handlerMap.get(keybuf)).handle(event);
        }

        return Optional.empty();
    }

    public void reset() {}

    public S getState() {
        return state;
    }
}
