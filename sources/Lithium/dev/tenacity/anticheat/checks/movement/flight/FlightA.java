package dev.tenacity.anticheat.checks.movement.flight;

import dev.tenacity.anticheat.Detection;
import dev.tenacity.hackerdetector.utils.MovementUtils;
import net.minecraft.entity.player.EntityPlayer;

public class FlightA extends Detection {

    public FlightA() {
        super("Flight A");
    }

    @Override
    public boolean runCheck(EntityPlayer player) {
        return !player.onGround && player.motionY == 0 && MovementUtils.isMoving(player);
    }
}
