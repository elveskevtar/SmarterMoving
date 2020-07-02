package net.smart.moving.player;

import net.minecraft.entity.player.EntityPlayer;

public class SmartServer extends SmartBase {

	private final SmartServerPlayerBase base;

	public SmartServer(SmartServerPlayerBase base, EntityPlayer player) {
		super(player);
		this.base = base;
	}
}
