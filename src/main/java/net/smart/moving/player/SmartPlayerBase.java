package net.smart.moving.player;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import api.player.client.IClientPlayerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.smart.moving.SmartMoving;

public class SmartPlayerBase extends ClientPlayerBase {

	private SmartPlayer smartPlayer;
	
	public SmartPlayerBase(ClientPlayerAPI playerAPI) {
		super(playerAPI);
		this.smartPlayer = new SmartPlayer(this, player);
	}
	
	@Override
	public void afterOnUpdate() {
		smartPlayer.afterOnUpdate();
	}
	
	public Minecraft getMCField() {
		return playerAPI.getMcField();
	}
	
	public SmartPlayer getSmartPlayer() {
		return smartPlayer;
	}
	
	public static SmartPlayerBase getPlayerBase(EntityPlayerSP player) {
		return (SmartPlayerBase) ((IClientPlayerAPI) player).getClientPlayerBase(SmartMoving.NAME);
	}
}
