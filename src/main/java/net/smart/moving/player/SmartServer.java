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
	
	public void afterOnUpdate() {
		if (!player.hasCapability(SmartStateProvider.CAPABILITY_STATE, null))
			return;

		ISmartStateHandler handler = player.getCapability(SmartStateProvider.CAPABILITY_STATE, null);
		State state = State.getState(handler.getSmartState());
		if (state == State.INVALID)
			return;
		
		if (state == State.IDLE) {
			player.height = 1.8F;
			player.eyeHeight = player.getDefaultEyeHeight();
		} else if (state == State.CRAWL) {
			player.height = 0.65F;
			player.eyeHeight = 0.5F;
		}
		
		updateBoundingBox();
	}
}
