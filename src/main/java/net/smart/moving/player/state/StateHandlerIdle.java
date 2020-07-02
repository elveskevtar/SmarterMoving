package net.smart.moving.player.state;

import net.minecraft.entity.player.EntityPlayer;
import net.smart.moving.player.PlayerConstants;
import net.smart.moving.player.SmartBase;

public class StateHandlerIdle extends StateHandler {

    @Override
    public void afterOnUpdate(EntityPlayer player, SmartBase smartBase) {
        player.height = PlayerConstants.DEFAULT_HEIGHT;
        player.eyeHeight = player.getDefaultEyeHeight();
    }
}
