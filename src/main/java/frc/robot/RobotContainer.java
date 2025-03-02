package frc.robot;

import java.util.Optional;

import frc.robot.smf.StateMachine;
import frc.robot.smf.events.DisableEvent;
import frc.robot.smf.events.EnableEvent;
import frc.robot.smf.events.TimeEvent;

public class RobotContainer extends StateMachine<RobotContainer.State> {
    private final ExampleSubsystem exampleSubsystem;

    public RobotContainer() {
        super(State.class, "Robot Container", State.DISABLED);

        exampleSubsystem = new ExampleSubsystem();
        exampleSubsystem.setLogger(getLogger().sub(exampleSubsystem.getName()));

        createHandlers();
    }

    private void createHandlers() {
        setGlobalHandler(DisableEvent.class, (_ev) -> Optional.of(State.DISABLED));

        setGlobalHandler(EnableEvent.class, (ev) -> {
            switch (ev.state()) {
                case AUTONOMOUS:
                    return Optional.of(State.AUTON);
                case TELEOP:
                    return Optional.of(State.TELEOP);
                case TEST:
                    return Optional.of(State.TEST);
                default:
                    return Optional.empty();
            }
        });

        forward(TimeEvent.class, exampleSubsystem);
    }

    enum State {
        DISABLED,
        TELEOP,
        AUTON,
        TEST
    }
}
