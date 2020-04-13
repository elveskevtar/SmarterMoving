package net.smart.moving.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SmartStateStorage implements IStorage<ISmartStateHandler> {
	
	private static final String stateKey = "state";

	@Override
	public NBTBase writeNBT(Capability<ISmartStateHandler> capability, ISmartStateHandler instance,
			EnumFacing side) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setByte(stateKey, instance.getSmartState());
		return tag;
	}

	@Override
	public void readNBT(Capability<ISmartStateHandler> capability, ISmartStateHandler instance, EnumFacing side,
			NBTBase nbt) {
		final NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.setSmartState(tag.getByte(stateKey));
	}
}
