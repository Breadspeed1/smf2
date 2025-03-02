package frc.robot.smf;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import frc.robot.smf.logging.ScopeLogger;
import frc.robot.smf.util.Pair;

public abstract class StateMachine<S extends Enum<S>, T extends Enum<T>> implements Sendable {
    private final StateEventHandler<S, T>[] handlers;
    private final HashMap<Pair<Class<?>, T>, HashMap<StateMachine<?, ?>, Consumer<Object>>> forwards;
    private final String name;

    private final Pair<Class<?>, T> fkeybuf = new Pair<>(null, null);

    private ScopeLogger logger;
    private S currentState;

    private Object lastEvent;

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
        handlers = (StateEventHandler<S, T>[]) Array.newInstance(def.getClass(), stateType.getEnumConstants().length);
        Arrays.setAll(handlers, (i) -> new StateEventHandler<>(stateType.getEnumConstants()[i]));

        forwards = new HashMap<>();

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
     * @param <E> the type of event
     * @param topic the topic of the handler
     * @param eventType the classtype of the event class
     * @param handler the handler to handle the event
     */
    protected <E> void setGlobalHandler(Class<E> eventType, T topic, EventHandler<S, E> handler) {
        for (StateEventHandler<S, T> seh : handlers) {
            seh.setHandler(eventType, topic, handler);
        }
    }

    /**
     * Set the event handler for a specific state and event.
     * @param <E> the type of event
     * @param eventType the classtype of the event class
     * @param topic the topic for the handler
     * @param state the state to apply the handler to
     * @param handler the handler to handle the event
     */
    protected <E> void setHandler(Class<E> eventType, T topic, S state, EventHandler<S, E> handler) {
        handlers[state.ordinal()].setHandler(eventType, topic, handler);
    }

    /**
     * Override an entire state event handler.
     * @param handler the state event handler.
     */
    protected void setHandler(StateEventHandler<S, T> handler) {
        handlers[handler.getState().ordinal()] = handler;
    }

    /**
     * Handle an event.
     * @param <E> the type of the event being handled.
     * @param topic the topic of the event
     * @param event the event to handle.
     */
    public <E> void handle(T topic, E event) {
        lastEvent = event;

        fkeybuf.left = event.getClass();
        fkeybuf.right = topic;

        forwards.computeIfPresent(fkeybuf, (_k, v) -> {
            v.values().forEach((c) -> c.accept(event));
            return v;
        });

        handlers[currentState.ordinal()]
            .handle(event, topic)
            .ifPresent(this::setState);
    }

    /**
     * Forward all messages of type on topic to a state machine.
     * @param <E> Type of event
     * @param <NT> Receiver's topic type
     * @param eventType Classtype of event
     * @param oldTopic Topic to intercept form
     * @param newTopic Topic to forward to
     * @param stateMachine State machine to forward to
     */
    protected <E, NT extends Enum<NT>> void forward(Class<E> eventType, T oldTopic, NT newTopic, StateMachine<?, NT> stateMachine) {
        var key = new Pair<Class<?>, T>(eventType, oldTopic);
        forwards.computeIfAbsent(key, (_k) -> new HashMap<>());
        forwards.compute(key, (_k, v) -> {
            v.put(stateMachine, (ev) -> stateMachine.handle(newTopic, ev));
            return v;
        });
    }

    private void setState(S state) {
        currentState = state;
        handlers[currentState.ordinal()].reset();
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

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("State Machine");
        builder.publishConstBoolean("controllable", false);
        builder.addStringProperty("State", () -> currentState.toString(), null);
        builder.addStringProperty("Last Event Type", () -> lastEvent != null ? lastEvent.getClass().getSimpleName() : "No events yet.", null);
    }
}
