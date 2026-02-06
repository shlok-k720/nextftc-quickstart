package org.firstinspires.ftc.teamcode.mechanisms;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Launcher implements Subsystem {
    public static Launcher INSTANCE;
    private DcMotorEx launcherMotor;

    /* =====================
       PIDF CONSTANTS
       ===================== */

    // STARTING VALUES — TUNE THESE
    private static final double kP = 300;
    private static final double kI = 0.0;
    private static final double kD = 5.0;
    private static final double kF = 10;

    private static final PIDFCoefficients PIDF =
            new PIDFCoefficients(kP, kI, kD, kF);

    /* =====================
       State
       ===================== */

    private double targetVelocityDegPerSec = 0.0;
    private boolean enabled = false;

    public Launcher(HardwareMap hardwareMap) {
        launcherMotor = hardwareMap.get(DcMotorEx.class, "launcher"); // or whatever your launcher name is

        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        // APPLY PIDF HERE
        launcherMotor.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER,
                PIDF
        );
    }

    /** Call ONCE in Robot init */
    public static void init(HardwareMap hardwareMap) {
        if (INSTANCE == null) {
            INSTANCE = new Launcher(hardwareMap);
        }
    }

    @Override
    public void initialize() {
        stop();
    }

    @Override
    public void periodic() {
        if (enabled) {
            launcherMotor.setVelocity(targetVelocityDegPerSec, AngleUnit.DEGREES);
        } else {
            launcherMotor.setVelocity(0);
        }
    }

    /* =====================
       Control API
       ===================== */

    /** Shoot at target RPM */
    public void shootRPM(double rpm) {
        targetVelocityDegPerSec = rpmToDegPerSec(rpm);
        enabled = true;
    }

    /** Update RPM while running */
    public void setRPM(double rpm) {
        targetVelocityDegPerSec = rpmToDegPerSec(rpm);
    }

    /** Stop launcher */
    public void stop() {
        enabled = false;
        targetVelocityDegPerSec = 0;
        launcherMotor.setVelocity(0);
    }

    public boolean isRunning() {
        return enabled;
    }

    /** Measured RPM */
    public double getRPM() {
        double degPerSec = launcherMotor.getVelocity(AngleUnit.DEGREES);
        return degPerSec / 6.0;
    }

    /* =====================
       Conversions
       ===================== */

    private double rpmToDegPerSec(double rpm) {
        return rpm * 6.0; // 360 / 60
    }
}