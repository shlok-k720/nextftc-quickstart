package org.firstinspires.ftc.teamcode.mechanisms;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Transfer implements Subsystem {
    public static final Transfer INSTANCE = new Transfer();

    private MotorEx transferMotor;

    /* =====================
       State
       ===================== */

    private double targetPower = 0.0;
    private boolean enabled = false;

    /* =====================
       Constructor
       ===================== */

    public Transfer() {
        transferMotor = new MotorEx("transferMotor");
        transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void initialize() {
        stop();
    }

    @Override
    public void periodic() {
        if (enabled) {
            transferMotor.setPower(targetPower);
        } else {
            transferMotor.setPower(0);
        }
    }

    /* =====================
       Control API
       ===================== */

    /** Run transfer at specified power */
    public void feed(double power) {
        targetPower = Math.max(0, Math.min(1.0, power));
        enabled = true;
    }

    /** Run transfer at default power */
    public void feed() {
        feed(0.5);
    }

    /** Stop transfer */
    public void stop() {
        enabled = false;
        targetPower = 0.0;
        transferMotor.setPower(0);
    }

    public boolean isRunning() {
        return enabled;
    }

    public double getTargetPower() {
        return targetPower;
    }

    public double getPower() {
        return transferMotor.getPower();
    }

}