package net.smart.moving.player.state;

import net.minecraft.entity.player.EntityPlayer;
import net.smart.moving.player.PlayerConstants;
import net.smart.moving.player.SmartBase;

public class StateHandlerSneak extends StateHandler {

    @Override
    public void afterOnUpdate(EntityPlayer player, SmartBase smartBase) {
        player.height = PlayerConstants.SNEAK_HEIGHT;
    }

    @Override
    public void updatePlayerRotations(EntityPlayer player, SmartBase smartBase) {
        super.updatePlayerRotations(player, smartBase);
    }
}
