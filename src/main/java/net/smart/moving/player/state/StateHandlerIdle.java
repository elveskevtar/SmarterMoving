package net.smart.moving.player.state;

import net.minecraft.entity.player.EntityPlayer;
import net.smart.moving.player.PlayerConstants;

public class StateHandlerIdle extends StateHandler {

    	@Override
    public void afterOnUpdate(EntityPlayer player) {
        player.height = PlayerConstants.DEFAULT_HEIGHT;
        player.eyeHeight = player.getDefaultEyeHeight();
    }

}
