package net.smart.moving.player.state;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.smart.moving.player.PlayerConstants;
import net.smart.moving.player.SmartBase;
import net.smart.moving.utilities.RenderUtilities;

public class StateHandlerFly extends StateHandler {

    public static void moveFlying(EntityPlayerSP player, float strafing, float forward) {
        float moveUpward = 0F;
        if (player.movementInput.sneak) {
            player.motionY += PlayerConstants.FLY_VERTICAL_VELOCITY;
            moveUpward -= PlayerConstants.FLY_MOVE_SPEED;
        }

        if (player.movementInput.jump) {
            player.motionY -= PlayerConstants.FLY_VERTICAL_VELOCITY;
            moveUpward += PlayerConstants.FLY_MOVE_SPEED;
        }

        moveAltFlying(player, moveUpward, strafing, forward);
        player.move(MoverType.SELF, player.motionX, player.motionY, player.motionZ);

        player.motionX *= PlayerConstants.HORIZONTAL_AIR_DAMPING;
        player.motionY *= PlayerConstants.HORIZONTAL_AIR_DAMPING;
        player.motionZ *= PlayerConstants.HORIZONTAL_AIR_DAMPING;
    }

    public static void moveAltFlying(EntityPlayer player, float vertical, float strafing, float forward) {
        float diffMotionXStrafing = 0, diffMotionXForward = 0, diffMotionZStrafing = 0, diffMotionZForward = 0;
        float horizontalTotal = MathHelper.sqrt(strafing * strafing + forward * forward);
        if(horizontalTotal >= 0.01F) {
            if(horizontalTotal < 1.0F)
                horizontalTotal = 1.0F;

            float moveStrafingFactor = strafing / horizontalTotal;
            float moveForwardFactor = forward / horizontalTotal;
            float sin = MathHelper.sin((float) (player.rotationYaw * Math.PI / 180F));
            float cos = MathHelper.cos((float) (player.rotationYaw * Math.PI / 180F));
            diffMotionXStrafing = moveStrafingFactor * cos;
            diffMotionXForward = -moveForwardFactor * sin;
            diffMotionZStrafing = moveStrafingFactor * sin;
            diffMotionZForward = moveForwardFactor * cos;
        }

        float rotation = player.rotationPitch / RenderUtilities.RadiantToAngle;
        float divingHorizontalFactor = MathHelper.cos(rotation);
        float divingVerticalFactor = -MathHelper.sin(rotation) * Math.signum(forward);

        float diffMotionX = diffMotionXForward * divingHorizontalFactor + diffMotionXStrafing;
        float diffMotionY = MathHelper.sqrt(diffMotionXForward * diffMotionXForward + diffMotionZForward * diffMotionZForward) * divingVerticalFactor + vertical;
        float diffMotionZ = diffMotionZForward * divingHorizontalFactor + diffMotionZStrafing;

        float total = MathHelper.sqrt(MathHelper.sqrt(diffMotionX * diffMotionX + diffMotionZ * diffMotionZ) + diffMotionY * diffMotionY);
        if(total > 0.01F) {
            float factor = PlayerConstants.FLY_SPEED_FACTOR / total;
            player.motionX = diffMotionX * factor;
            player.motionY = diffMotionY * factor;
            player.motionZ = diffMotionZ * factor;
        }
    }

    @Override
    public void afterOnUpdate(EntityPlayer player, SmartBase smartBase) {
        // TODO: Dynamically change BB to update based on horizontal velocity
    }

    @Override
    public void updatePlayerRotations(EntityPlayer player, SmartBase smartBase) {
        player.renderYawOffset = smartBase.forwardRotation;
    }

    @Override
    public void startState(EntityPlayer player, SmartBase smartBase) {
        player.height = PlayerConstants.DEFAULT_HEIGHT;
        player.eyeHeight = player.getDefaultEyeHeight();
        smartBase.bbYOffset = player.height / 2.0D;
    }

    @Override
    public void stopState(EntityPlayer player, SmartBase smartBase) {
        smartBase.bbYOffset = 0;
    }
}
