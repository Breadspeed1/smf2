package frc.robot.smf;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;

import frc.robot.smf.logging.ScopeLogger;

public abstract class StateMachine<S extends Enum<S>> {
    private final StateEventHandler<S>[] handlers;
    private final String name;

    private ScopeLogger logger;
    private S currentState;

    /**
     * Create a new state machine.
     * @param stateType the classtype of the State enum
     * @param name the name of the state machine
     * @param initialState the initial state of the state machine
     * @param logger the logger to log state changes and events to
     */
    @SuppressWarnings("unchecked")
    public StateMachine(Class<S> stateType, String name, S initialState, ScopeLogger logger) {
        var def = new StateEventHandler<>(initialState);
        handlers = (StateEventHandler<S>[]) Array.newInstance(def.getClass(), stateType.getEnumConstants().length);
        Arrays.setAll(handlers, (i) -> new StateEventHandler<>(stateType.getEnumConstants()[i]));

        this.name = name;
        this.currentState = initialState;
        this.logger = logger;
    }

    /**
     * Create a new state machine with a default logger.
     * @param stateType the classtype of the State enum
     * @param name the name of the state machine
     * @param initialState the initial state of the state machine
     */
    public StateMachine(Class<S> stateType, String name, S initialState) {
        this(stateType, name, initialState, new ScopeLogger(name));
    }

    /**
     * Set the event handler for all states for a specific event.
     * @param <T> the type of event
     * @param eventType the classtype of the event class
     * @param handler the handler to handle the event
     */
    protected <T> void setGlobalHandler(Class<T> eventType, EventHandler<S, T> handler) {
        for (StateEventHandler<S> seh : handlers) {
            seh.setHandler(eventType, handler);
        }
    }

    /**
     * Set the event handler for a specific state and event.
     * @param <T> the type of event
     * @param eventType the classtype of the event class
     * @param state the state to apply the handler to
     * @param handler the handler to handle the event
     */
    protected <T> void setHandler(Class<T> eventType, S state, EventHandler<S, T> handler) {
        handlers[state.ordinal()].setHandler(eventType, handler);
    }

    /**
     * Override an entire state event handler.
     * @param <T> the type of event
     * @param handler the state event handler.
     */
    protected <T> void setHandler(StateEventHandler<S> handler) {
        handlers[handler.getState().ordinal()] = handler;
    }

    /**
     * Handle an event.
     * @param <T> the type of the event being handled.
     * @param event the event to handle.
     */
    public <T> void handle(T event) {
        handlers[currentState.ordinal()]
            .trigger(event)
            .ifPresent(this::setState);
    }

    protected <T> void forward(Class<T> eventType, StateMachine<?> stateMachine) {
        setGlobalHandler(eventType, (ev) -> {
            stateMachine.handle(ev);
            return Optional.empty();
        });    
    }

    private void setState(S state) {
        currentState = state;
    }

    /**
     * Set the logger of the state machine
     * @param logger the new logger
     */
    public void setLogger(ScopeLogger logger) {
        this.logger = logger;
    }

    /**
     * Get the logger of the state machine
     * @return the logger
     */
    public ScopeLogger getLogger() {
        return logger;
    }

    /**
     * Get the name of the state machine
     * @return the name
     */
    public String getName() {
        return name;
    }
}
