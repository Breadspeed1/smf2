// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.smf.events.DisableEvent;
import frc.robot.smf.events.EnableEvent;
import frc.robot.smf.events.GameState;

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

  public Robot() {
    super(0.05);

    rc = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    rc.handle(new EnableEvent(GameState.AUTONOMOUS));
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    rc.handle(new EnableEvent(GameState.TELEOP));
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void disabledInit() {
    rc.handle(new DisableEvent());
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {
    rc.handle(new EnableEvent(GameState.TEST));
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
