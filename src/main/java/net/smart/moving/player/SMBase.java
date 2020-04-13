package net.smart.moving.player;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smart.moving.SmartMovingContext;
import net.smart.moving.capabilities.ISmartStateHandler;
import net.smart.moving.capabilities.SmartStateProvider;
import net.smart.moving.config.SmartOptions;
import net.smart.moving.input.SmartInput;
import net.smart.moving.network.SmartPacketHandler;
import net.smart.moving.network.packet.StatePacket;

public abstract class SMBase {
	
	protected static enum State {
		INVALID(-1), IDLE(0), CRAWL(1);
		
		public final byte id;
		
		State(int id) {
			this.id = (byte) id;
		}
		
		public static State getState(byte stateId) {
			for (State state : State.values())
				if (state.id == stateId)
					return state;
			return State.INVALID;
		}
	}

	protected EntityPlayer player;
	protected State state;
	
	public SMBase(EntityPlayer player) {
		this(player, State.IDLE);
	}
	
	public SMBase(EntityPlayer player, State state) {
		this.player = player;
		this.state = state;
	}
	
	protected void updateBoundingBox() {
		final double d0 = player.width / 2.0D;
		final AxisAlignedBB aabb = player.getEntityBoundingBox();
		player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, aabb.minY,
				player.posZ - d0, player.posX + d0, aabb.minY + player.height, player.posZ + d0));
	}
	
	protected boolean isOpenBlockSpace(BlockPos pos, boolean top) {
		IBlockState blockState = player.world.getBlockState(pos);
		IBlockState upBlockState = player.world.getBlockState(pos.up());
		return !blockState.getBlock().isNormalCube(blockState, player.world, pos)
				&& (!top || !upBlockState.getBlock().isNormalCube(blockState, player.world, pos.up()));
	}
	
	protected boolean isHeadspaceFree(BlockPos pos, int height, boolean top) {
		for (int y = 0; y < height; y++)
			if (isOpenBlockSpace(pos.add(0, y, 0), top))
				return false;
		return true;
	}
}
