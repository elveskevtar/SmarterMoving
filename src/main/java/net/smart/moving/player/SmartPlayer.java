package net.smart.moving.player;

import java.util.HashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.smart.moving.SmartMoving;
import net.smart.moving.SmartMovingContext;
import net.smart.moving.capabilities.ISmartStateHandler;
import net.smart.moving.capabilities.SmartStateProvider;
import net.smart.moving.config.SmartOptions;
import net.smart.moving.input.SmartInput;
import net.smart.moving.network.SmartPacketHandler;
import net.smart.moving.network.packet.StatePacket;
import net.smart.moving.player.SMBase.State;

public class SmartPlayer extends SMBase {
	
	protected static final SmartInput Input = SmartMovingContext.Input;
	protected static final SmartOptions Options = SmartMovingContext.Options;

	private SmartPlayerBase base;
	
	public SmartPlayer(SmartPlayerBase base, EntityPlayer player) {
		super(player);
		this.base = base;
	}
	
	public static void onPlayerTick(EntityPlayer player) {
		SmartPlayer smartPlayer = SmartPlayerBase.getPlayerBase((EntityPlayerSP) player).getSmartPlayer();
		if (!player.hasCapability(SmartStateProvider.CAPABILITY_STATE, null))
			return;
		
		ISmartStateHandler handler = player.getCapability(SmartStateProvider.CAPABILITY_STATE, null);
		State state = State.getState(handler.getSmartState());
		if (state == State.INVALID)
			return;
		
		boolean grab = Input.isKeyDown(Options.grabKey);
		
		State newState = State.IDLE;
		int entHeight = Math.max(Math.round(player.height), 1);
		if (player.isSneaking() && grab)
			newState = State.CRAWL;
		if (state == State.CRAWL && newState != State.CRAWL &&
				!smartPlayer.isHeadspaceFree(player.getPosition(), entHeight))
			newState = State.CRAWL;
		
		if (newState != state) {
			handler.setSmartState(newState.id);
			StatePacket packet = new StatePacket(player.getEntityId(), newState.id);
			SmartPacketHandler.INSTANCE.sendToServer(packet);
		}
	}
}
