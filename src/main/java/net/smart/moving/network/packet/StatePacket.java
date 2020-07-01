package net.smart.moving.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.smart.moving.player.SmartBase;
import net.smart.moving.player.state.State;

public class StatePacket implements IMessage {

	public static final byte PacketId = 0;
	
	public StatePacket() {}
	
	public int entityId;
	public byte state;
	
	public StatePacket(int entityId, byte state) {
		this.entityId = entityId;
		this.state = state;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		entityId = buf.readInt();
		state = buf.readByte();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeByte(state);
	}
	
	public static class ClientHandler implements IMessageHandler<StatePacket, IMessage> {
		
		public ClientHandler() {}

		@Override
		public IMessage onMessage(StatePacket message, MessageContext ctx) {
			return null;
		}
	}
	
	public static class ServerHandler implements IMessageHandler<StatePacket, IMessage> {
		
		public ServerHandler() {}

		@Override
		public IMessage onMessage(StatePacket message, MessageContext ctx) {
			final EntityPlayerMP sendingPlayer = ctx.getServerHandler().player;
			if (sendingPlayer == null) return null;
			
			final WorldServer playerWorldServer = sendingPlayer.getServerWorld();
			playerWorldServer.addScheduledTask(() -> processMessage(message, ctx, sendingPlayer));
			
			return null;
		}
		
		private void processMessage(StatePacket message, MessageContext ctx, EntityPlayer player) {
			SmartBase.setState(player, State.getState(message.state));
		}
	}
}
