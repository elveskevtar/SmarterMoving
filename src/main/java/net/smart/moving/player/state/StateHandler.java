package net.smart.moving.player.state;

import net.minecraft.entity.player.EntityPlayer;
import net.smart.moving.player.SmartBase;

public abstract class StateHandler {

    public void afterOnUpdate(EntityPlayer player, SmartBase base) {}

    public void updatePlayerRotations(EntityPlayer player, SmartBase smartBase) {}

    public void startState(EntityPlayer player, SmartBase smartBase) {}

    public void stopState(EntityPlayer player, SmartBase smartBase) {}

}
