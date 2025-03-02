package frc.robot;

import java.util.Optional;

import frc.robot.smf.StateMachine;
import frc.robot.smf.events.DisableEvent;
import frc.robot.smf.events.EnableEvent;

public class RobotContainer extends StateMachine<RobotContainer.State> {
    public RobotContainer() {
        super(State.class, "Robot Container", State.DISABLED);
    }

    @Override
    protected void createHandlers() {
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
    }

    enum State {
        DISABLED,
        TELEOP,
        AUTON,
        TEST
    }
}
