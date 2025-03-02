package frc.robot;

import java.util.Optional;

import frc.robot.smf.StateMachine;
import frc.robot.smf.events.DisableEvent;
import frc.robot.smf.events.EnableEvent;
import frc.robot.smf.events.TimeEvent;
import frc.robot.smf.logging.LogLevel;

public class ExampleSubsystem extends StateMachine<ExampleSubsystem.State, ExampleSubsystem.Topic> {
    private double startTime;

    public ExampleSubsystem() {
        super(State.class, "Example Subsystem", State.FLIP);
        startTime = 0;

        createHandlers();
    }

    private void createHandlers() {
        setGlobalHandler(Object.class, Topic.TIME_EVENTS, (ev) -> {
            getLogger().log(LogLevel.DEBUG, ev.toString());
            return Optional.empty();
        });

        setHandler(TimeEvent.class, Topic.TIME_EVENTS, State.FLIP, (ev) -> {
            if (ev.time() - startTime > 1) {
                startTime = ev.time();
                return Optional.of(State.FLOP);
            }

            return Optional.empty();
        });

        setHandler(TimeEvent.class, Topic.TIME_EVENTS, State.FLOP, (ev) -> {
            if (ev.time() - startTime > 1) {
                startTime = ev.time();
                return Optional.of(State.FLIP);
            }

            return Optional.empty();
        });

        setHandler(EnableEvent.class, Topic.MATCH_EVENTS, State.DISABLED, (_ev) -> Optional.of(State.FLIP));

        setHandler(TimeEvent.class, Topic.TIME_EVENTS, State.DISABLED, (ev) -> {
            startTime = ev.time();
            return Optional.empty();
        });
        
        setGlobalHandler(DisableEvent.class, Topic.MATCH_EVENTS, (_ev) -> Optional.of(State.DISABLED));
    }

    enum State {
        FLIP,
        FLOP,
        DISABLED
    }

    enum Topic {
        MATCH_EVENTS,
        TIME_EVENTS
    }
}
