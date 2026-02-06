/* Copyright (c) 2025 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.firstinspires.ftc.teamcode;

import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;



import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.Transfer;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.components.Component;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@TeleOp(name = "Next Robot TeleOp", group = "Robot")
public class NextRobotTeleOp extends NextFTCOpMode {
    {
        addComponents(
                new PedroComponent(Constants::createFollower)
        );
        
        // Subsystems are automatically managed by NextFTC
        addSubsystems(
                Intake.INSTANCE,
                Launcher.INSTANCE,
                Transfer.INSTANCE
        );
    }

    private DriverControlledCommand driverControlled;

    @Override
    public void onInit() {
        // Initialize all mechanisms
        Launcher.init(hardwareMap);
        
        // Initialize driver control
        driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX(),
                true
        );
        driverControlled.schedule();
        
        // Add telemetry header
        telemetry.addLine("NextRobot TeleOp Initialized");
        telemetry.addLine("Controls:");
        telemetry.addLine("- Right Trigger: Intake");
        telemetry.addLine("- Left Trigger: Outtake");
        telemetry.addLine("- Right Bumper: Launch");
        telemetry.addLine("- A Button: Feed");
        telemetry.update();
    }

    @Override
    public void onLoop() {
        // INTAKE CONTROL
        double rightTrigger = Gamepads.gamepad1().rightTrigger().get();
        double leftTrigger = Gamepads.gamepad1().leftTrigger().get();
        
        if (rightTrigger > 0.2) {
            Intake.INSTANCE.intake(rightTrigger);
        } else if (leftTrigger > 0.2) {
            Intake.INSTANCE.outtake(leftTrigger);
        } else {
            Intake.INSTANCE.stop();
        }

        // LAUNCHER CONTROL
        if (Gamepads.gamepad1().rightBumper().get()) {
            Launcher.INSTANCE.shootRPM(4500);
        } else {
            Launcher.INSTANCE.stop();
        }

        // TRANSFER / FEEDER
        if (Gamepads.gamepad1().a().get()) {
            Transfer.INSTANCE.feed();
        } else {
            Transfer.INSTANCE.stop();
        }
        
        // TELEMETRY
        updateTelemetry();
    }
    
    private void updateTelemetry() {
        telemetry.addData("Status", "Running");
        telemetry.addLine();
        
        // Intake status
        telemetry.addData("Intake Running", Intake.INSTANCE.isRunning());
        telemetry.addData("Intake Power", "%.2f", Intake.INSTANCE.getTargetPower());
        
        // Launcher status
        telemetry.addData("Launcher Running", Launcher.INSTANCE.isRunning());
        if (Launcher.INSTANCE.isRunning()) {
            telemetry.addData("Launcher RPM", "%.0f", Launcher.INSTANCE.getRPM());
        }
        
        // Transfer status
        telemetry.addData("Transfer Running", Transfer.INSTANCE.isRunning());
        if (Transfer.INSTANCE.isRunning()) {
            telemetry.addData("Transfer Power", "%.2f", Transfer.INSTANCE.getTargetPower());
        }
        
        telemetry.update();
    }

}
