package net.smart.moving.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.smart.moving.SmartMovingContext;
import net.smart.moving.config.SmartOptions;
import net.smart.moving.input.SmartInput;
import net.smart.moving.network.SmartPacketHandler;
import net.smart.moving.network.packet.StatePacket;
import net.smart.moving.player.state.State;
import net.smart.moving.player.state.StateHandlerFly;

public class SmartPlayer extends SmartBase {
	
	protected static final SmartInput Input = SmartMovingContext.Input;
	protected static final SmartOptions Options = SmartMovingContext.Options;

	private SmartPlayerBase base;
	
	public SmartPlayer(SmartPlayerBase base, EntityPlayer player) {
		super(player);
		this.base = base;
	}
	
	public void moveEntityWithHeading(float strafing, float vertical, float forward) {
		State state = getState(player);
		if (state == null || state == State.INVALID)
			return;
		
		if (state == State.FLY)
			StateHandlerFly.moveFlying(player, vertical, strafing, forward, true);
		base.superMoveEntityWithHeading(strafing, vertical, forward);
	}
	
	public static void computeNewSmartState(EntityPlayer player) {
		State state = SmartBase.getState(player);
		SmartPlayer smartPlayer = SmartPlayerBase.getPlayerBase((EntityPlayerSP) player).getSmartPlayer();
		if (state == null || state == State.INVALID)
			return;
		
		boolean sneak = SmartInput.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak);
		boolean grab = SmartInput.isKeyDown(SmartOptions.grabKey);

		State newState = State.IDLE;
		
		if (sneak)
			newState = State.SNEAK;
		
		if (sneak && grab || state == State.CRAWL && sneak)
			newState = State.CRAWL;
		
		if (state == State.CRAWL && newState != State.CRAWL && !smartPlayer.isHeadspaceFree())
			newState = State.CRAWL;
		
		if (player.capabilities.isFlying)
			newState = State.FLY;
		
		if (player.isElytraFlying())
			newState = State.ELYTRA;
		
		if (newState != state) {
			SmartBase.setState(player, newState);
			StatePacket packet = new StatePacket(player.getEntityId(), newState.id);
			SmartPacketHandler.INSTANCE.sendToServer(packet);
		}
	}
	
	public static void onPlayerInput(InputUpdateEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		State state = SmartBase.getState(player);
		if (state == null || state == State.INVALID)
			return;
		
		/* fix sneaking when need to crawl */
		SmartPlayer smartPlayer = SmartPlayerBase.getPlayerBase((EntityPlayerSP) player).getSmartPlayer();
		if (state == State.CRAWL && !smartPlayer.isHeadspaceFree()
				&& !event.getMovementInput().sneak) {
			player.setSneaking(true);
			event.getMovementInput().sneak = true;
			event.getMovementInput().moveStrafe *= 0.3F;
			event.getMovementInput().moveForward *= 0.3F;
		}

		/* fix sneak use during flying */
		if (state == State.FLY && event.getMovementInput().sneak) {
			player.setSneaking(false);
			event.getMovementInput().sneak = false;
			event.getMovementInput().moveStrafe /= 0.3F;
			event.getMovementInput().moveForward /= 0.3F;
		}

		/* fix jumping while crawling */
		if (state == State.CRAWL && event.getMovementInput().jump) {
			player.setJumping(false);
			event.getMovementInput().jump = false;
		}
	}
}
