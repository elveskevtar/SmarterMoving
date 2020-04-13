package net.smart.moving.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.smart.moving.SmartMoving;

public class SmartCapabilities {

	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(
				ISmartStateHandler.class, new SmartStateStorage(), new SmartStateHandler.Factory());
	}
	
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer)
			event.addCapability(
					new ResourceLocation(SmartMoving.MODID, SmartStateProvider.NAME), 
					new SmartStateProvider());
	}
}
