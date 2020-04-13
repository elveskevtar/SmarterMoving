package net.smart.moving.player;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import api.player.client.IClientPlayerAPI;
import net.minecraft.client.entity.EntityPlayerSP;
import net.smart.moving.SmartMoving;

public class SmartPlayerBase extends ClientPlayerBase {

	private SmartPlayer SMPlayer;
	
	public SmartPlayerBase(ClientPlayerAPI playerAPI) {
		super(playerAPI);
		this.SMPlayer = new SmartPlayer(this, player);
	}
	
	public static SmartPlayerBase getPlayerBase(EntityPlayerSP player) {
		return (SmartPlayerBase) ((IClientPlayerAPI) player).getClientPlayerBase(SmartMoving.NAME);
	}
	
	@Override
	public void afterOnUpdate() {
		SMPlayer.afterOnUpdate();
	}
	
	public SmartPlayer getSmartPlayer() {
		return SMPlayer;
	}
}
