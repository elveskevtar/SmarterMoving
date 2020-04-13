package net.smart.moving.player;

import api.player.server.IServerPlayerAPI;
import api.player.server.ServerPlayerAPI;
import api.player.server.ServerPlayerBase;
import net.smart.moving.SmartMoving;

public class SmartServerPlayerBase extends ServerPlayerBase {

	private SmartServer server;
	
	public SmartServerPlayerBase(ServerPlayerAPI playerAPI) {
		super(playerAPI);
		server = new SmartServer(this, player);
	}
	
	public static SmartServerPlayerBase getPlayerBase(Object player) {
		return (SmartServerPlayerBase) ((IServerPlayerAPI) player).getServerPlayerBase(SmartMoving.NAME);
	}
	
	@Override
	public void afterOnUpdate() {
		server.afterOnUpdate();
	}
	
	public SmartServer getServer() {
		return server;
	}
}
