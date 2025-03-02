// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.smf.events.DisableEvent;
import frc.robot.smf.events.EnableEvent;
import frc.robot.smf.events.GameState;
import frc.robot.smf.events.TimeEvent;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  private final RobotContainer rc;
  private double t = 0;

  public Robot() {
    super(0.05);

    rc = new RobotContainer();

    SmartDashboard.putData(rc.getName(), rc);
  }

  @Override
  public void robotPeriodic() {
    if (Timer.getTimestamp() - t > 0.5) {
      t = Timer.getTimestamp();
      rc.handle(RobotContainer.Topic.TIME_EVENTS, new TimeEvent(Timer.getTimestamp(), Timer.getMatchTime()));
    }
  }

  @Override
  public void autonomousInit() {
    rc.handle(RobotContainer.Topic.MATCH_EVENTS, new EnableEvent(GameState.AUTONOMOUS));
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    rc.handle(RobotContainer.Topic.MATCH_EVENTS, new EnableEvent(GameState.TELEOP));
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void disabledInit() {
    rc.handle(RobotContainer.Topic.MATCH_EVENTS, new DisableEvent());
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {
    rc.handle(RobotContainer.Topic.MATCH_EVENTS, new EnableEvent(GameState.TEST));
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
