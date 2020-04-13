package net.smart.moving.player;

import net.minecraft.entity.player.EntityPlayer;
import net.smart.moving.SmartMoving;
import net.smart.moving.capabilities.ISmartStateHandler;
import net.smart.moving.capabilities.SmartStateProvider;

public class SmartServer extends SMBase {
	
	private SmartServerPlayerBase base;

	public SmartServer(SmartServerPlayerBase base, EntityPlayer player) {
		super(player);
		this.base = base;
	}
}
