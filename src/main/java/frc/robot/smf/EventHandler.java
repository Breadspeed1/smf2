package frc.robot.smf;

import java.util.Optional;

public interface EventHandler<S extends Enum<S>, T> {
    /**
     * Handle an event of type T
     * @param event the event to handle
     * @return the next desired state, empty optional if no transition is required.
     */
    Optional<S> handle(T event);
}
