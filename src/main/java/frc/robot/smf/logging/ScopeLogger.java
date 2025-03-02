package frc.robot.smf.logging;

import edu.wpi.first.wpilibj.DataLogManager;

public class ScopeLogger {
    private final String scope;

    public ScopeLogger(String scope) {
        this.scope = scope;
    }

    public void log(LogLevel level, String message) {
        DataLogManager.log(String.format(
            "[%s] [%s] | %s", 
                level.toString(),
                scope,
                message
            ));
    }

    public ScopeLogger sub(String subscope) {
        return new ScopeLogger(scope + "/" + subscope);
    }
}
