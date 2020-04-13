package net.smart.moving.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SmartStateProvider implements ICapabilitySerializable<NBTTagCompound> {
	
	@CapabilityInject(ISmartStateHandler.class)
	public static final Capability<ISmartStateHandler> CAPABILITY_STATE = null;
	
	public static final String NAME = "smartmoving";
	
	private ISmartStateHandler instance = CAPABILITY_STATE.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CAPABILITY_STATE;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return hasCapability(capability, facing) ? CAPABILITY_STATE.<T>cast(instance) : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) CAPABILITY_STATE.getStorage().writeNBT(CAPABILITY_STATE, instance, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		CAPABILITY_STATE.getStorage().readNBT(CAPABILITY_STATE, instance, null, nbt);
	}
}
