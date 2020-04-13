package net.smart.moving.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.smart.moving.SmartMoving;
import net.smart.moving.network.packet.StatePacket;

public class SmartPacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SmartMoving.MODID);
	
	public static void registerPackets() {
		INSTANCE.registerMessage(StatePacket.ClientHandler.class, StatePacket.class, StatePacket.PacketId, Side.CLIENT);
		INSTANCE.registerMessage(StatePacket.ServerHandler.class, StatePacket.class, StatePacket.PacketId, Side.SERVER);
	}
}
