package frc.robot;

import java.util.Optional;

import frc.robot.smf.StateMachine;
import frc.robot.smf.events.DisableEvent;
import frc.robot.smf.events.EnableEvent;
import frc.robot.smf.events.GameState;
import frc.robot.smf.events.TimeEvent;
import frc.robot.smf.logging.LogLevel;

public class RobotContainer extends StateMachine<RobotContainer.State, RobotContainer.Topic> {
    private final ExampleSubsystem exampleSubsystem;

    public RobotContainer() {
        super(State.class, "Robot Container", State.DISABLED);

        exampleSubsystem = new ExampleSubsystem();
        exampleSubsystem.setLogger(getLogger().sub(exampleSubsystem.getName()));

        createHandlers();
    }

    private void createHandlers() {
        setGlobalHandler(DisableEvent.class, Topic.MATCH_EVENTS, (_ev) -> Optional.of(State.DISABLED));

        setGlobalHandler(EnableEvent.class, Topic.MATCH_EVENTS, (ev) -> {
            switch (ev.state()) {
                case AUTONOMOUS:
                    exampleSubsystem.handle(ExampleSubsystem.Topic.MATCH_EVENTS, new DisableEvent());
                    return Optional.of(State.AUTON);
                case TELEOP:
                    exampleSubsystem.handle(ExampleSubsystem.Topic.MATCH_EVENTS, new EnableEvent(GameState.TELEOP));
                    return Optional.of(State.TELEOP);
                case TEST:
                    exampleSubsystem.handle(ExampleSubsystem.Topic.MATCH_EVENTS, new DisableEvent());
                    return Optional.of(State.TEST);
                default:
                    return Optional.empty();
            }
        });

        setHandler(TimeEvent.class, Topic.TIME_EVENTS, State.TEST, (ev) -> {
            getLogger().log(LogLevel.INFO, "IM TESTING!!!!!");
            return Optional.empty();
        });

        forward(DisableEvent.class, Topic.MATCH_EVENTS, ExampleSubsystem.Topic.MATCH_EVENTS, exampleSubsystem);
        forward(TimeEvent.class, Topic.TIME_EVENTS, ExampleSubsystem.Topic.TIME_EVENTS, exampleSubsystem);
    }

    enum State {
        DISABLED,
        TELEOP,
        AUTON,
        TEST
    }

    enum Topic {
        MATCH_EVENTS,
        TIME_EVENTS
    }
}
