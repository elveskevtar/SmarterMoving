package net.smart.moving.player;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import api.player.client.IClientPlayerAPI;
import net.minecraft.client.entity.EntityPlayerSP;
import net.smart.moving.SmartMoving;

public class SmartPlayerBase extends ClientPlayerBase {

	private final SmartPlayer smartPlayer;

	public SmartPlayerBase(ClientPlayerAPI playerAPI) {
		super(playerAPI);
		this.smartPlayer = new SmartPlayer(this, player);
	}
	
	@Override
	public void afterOnUpdate() {
		smartPlayer.afterOnUpdate();
	}
	
	@Override
	public void moveEntityWithHeading(float strafing, float vertical, float forward) {
		smartPlayer.moveEntityWithHeading(strafing, vertical, forward);
	}
	
	public void superMoveEntityWithHeading(float strafing, float vertical, float forward) {
		super.moveEntityWithHeading(strafing, vertical, forward);
	}
	
	public SmartPlayer getSmartPlayer() {
		return smartPlayer;
	}
	
	public static SmartPlayerBase getPlayerBase(EntityPlayerSP player) {
		return (SmartPlayerBase) ((IClientPlayerAPI) player).getClientPlayerBase(SmartMoving.NAME);
	}
}
