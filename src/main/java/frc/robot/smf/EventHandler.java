package frc.robot.smf;

import java.util.Optional;

public interface EventHandler<S extends Enum<S>, T> {
    /**
     * Handle a
     * @param event
     * @return
     */
    Optional<S> handle(T event);
}
