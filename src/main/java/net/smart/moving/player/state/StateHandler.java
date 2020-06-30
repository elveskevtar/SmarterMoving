package net.smart.moving.player.state;

import net.minecraft.entity.player.EntityPlayer;
import net.smart.moving.player.SmartBase;

public abstract class StateHandler {

    public void afterOnUpdate(EntityPlayer player) {}

    public void updatePlayerRotations(EntityPlayer player, SmartBase smartBase) {}

}
