package frc.robot;

import java.util.Optional;

import frc.robot.smf.StateMachine;
import frc.robot.smf.events.DisableEvent;
import frc.robot.smf.events.EnableEvent;
import frc.robot.smf.events.TimeEvent;
import frc.robot.smf.logging.LogLevel;

public class ExampleSubsystem extends StateMachine<ExampleSubsystem.State> {
    private double startTime;

    public ExampleSubsystem() {
        super(State.class, "Example Subsystem", State.FLIP);
        startTime = 0;

        createHandlers();
    }

    private void createHandlers() {
        setHandler(TimeEvent.class, State.FLIP, (ev) -> {
            if (ev.time() - startTime > 1) {
                startTime = ev.time();
                getLogger().log(LogLevel.INFO, "Flop");
                return Optional.of(State.FLOP);
            }

            return Optional.empty();
        });

        setHandler(TimeEvent.class, State.FLOP, (ev) -> {
            if (ev.time() - startTime > 1) {
                startTime = ev.time();
                getLogger().log(LogLevel.INFO, "Flip");
                return Optional.of(State.FLIP);
            }

            return Optional.empty();
        });

        setHandler(EnableEvent.class, State.DISABLED, (_ev) -> Optional.of(State.FLIP));

        setHandler(TimeEvent.class, State.DISABLED, (ev) -> {
            startTime = ev.time();
            return Optional.empty();
        });
        
        setGlobalHandler(DisableEvent.class, (_ev) -> Optional.of(State.DISABLED));
    }

    enum State {
        FLIP,
        FLOP,
        DISABLED
    }
}
