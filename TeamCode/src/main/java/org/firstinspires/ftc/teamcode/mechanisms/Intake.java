package org.firstinspires.ftc.teamcode.mechanisms;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake implements Subsystem {

    public static final Intake INSTANCE = new Intake();
    private MotorEx intakeMotor;

    /* =====================
       State
       ===================== */

    private double targetPower = 0.0;
    private boolean enabled = false;

    /* =====================
       Constructor
       ===================== */

    public Intake() {
        intakeMotor = new MotorEx("intakeMotor");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void initialize() {
        stop();
    }

    @Override
    public void periodic() {
        if (enabled) {
            intakeMotor.setPower(targetPower);
        } else {
            intakeMotor.setPower(0);
        }
    }

    /* =====================
       Control API
       ===================== */

    /** Intake forward */
    public void intake(double power) {
        targetPower = clamp(power);
        enabled = true;
    }

    /** Full-speed intake */
    public void intake() {
        intake(1.0);
    }

    /** Reverse intake (outtake) */
    public void outtake(double power) {
        targetPower = -clamp(power);
        enabled = true;
    }

    /** Stop intake */
    public void stop() {
        enabled = false;
        targetPower = 0.0;
        intakeMotor.setPower(0);
    }

    public boolean isRunning() {
        return enabled;
    }

    public double getTargetPower() {
        return targetPower;
    }

    public double getPower() {
        return intakeMotor.getPower();
    }

    /* =====================
       Utility
       ===================== */

    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}