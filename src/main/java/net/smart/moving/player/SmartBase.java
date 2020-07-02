package net.smart.moving.player;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.smart.moving.capabilities.ISmartStateHandler;
import net.smart.moving.capabilities.SmartStateProvider;
import net.smart.moving.player.state.State;

public abstract class SmartBase {

	public double bbYOffset;
	private final Set<BlockPos> boundingBlocks = new HashSet<>();

	protected EntityPlayer player;
	
	public float currentSpeed;
	public float forwardRotation;
	public float verticalAngle;
	public float horizontalAngle;
	public float totalDistance;
	
	public SmartBase(EntityPlayer player) {
		this.player = player;
	}
	
	public void afterOnUpdate() {
		State state = getState(player);
		if (state == null)
			return;

		state.handler.afterOnUpdate(player, this);
		state.handler.updatePlayerRotations(player, this);
		updateBoundingBox();
		wrapRotations();
	}

	protected void updateBoundingBox() {
		final double d0 = player.width / 2.0D;
		player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, player.posY + bbYOffset,
				player.posZ - d0, player.posX + d0, player.posY + bbYOffset + player.height, player.posZ + d0));

		boundingBlocks.clear();
		AxisAlignedBB aabb = player.getEntityBoundingBox();
		for (int x = (int)Math.floor(aabb.minX); x <= (int)Math.floor(aabb.maxX); x++)
			for (int y = (int)Math.floor(aabb.minY); y <= (int)Math.floor(aabb.maxY); y++)
				for (int z = (int)Math.floor(aabb.minZ); z <= (int)Math.floor(aabb.maxZ); z++)
					boundingBlocks.add(new BlockPos(x, y, z));
	}
	
	protected boolean isOpenBlockSpace(BlockPos pos) {
		IBlockState blockState = player.world.getBlockState(pos);
		IBlockState upBlockState = player.world.getBlockState(pos.up());
		return !blockState.getBlock().isNormalCube(blockState, player.world, pos)
				&& !upBlockState.getBlock().isNormalCube(upBlockState, player.world, pos.up());
	}

	protected boolean isHeadspaceFree() {
		for (BlockPos pos : boundingBlocks)
			if (!isOpenBlockSpace(pos))
				return false;
		return true;
	}
	
	private void wrapRotations() {
        while (player.rotationYaw - player.prevRotationYaw < -180.0F)
        	player.prevRotationYaw -= 360.0F;

        while (player.rotationYaw - player.prevRotationYaw >= 180.0F)
        	player.prevRotationYaw += 360.0F;

        while (player.renderYawOffset - player.prevRenderYawOffset < -180.0F)
        	player.prevRenderYawOffset -= 360.0F;

        while (player.renderYawOffset - player.prevRenderYawOffset >= 180.0F)
        	player.prevRenderYawOffset += 360.0F;

        while (player.rotationPitch - player.prevRotationPitch < -180.0F)
        	player.prevRotationPitch -= 360.0F;

        while (player.rotationPitch - player.prevRotationPitch >= 180.0F)
        	player.prevRotationPitch += 360.0F;

        while (player.rotationYawHead - player.prevRotationYawHead < -180.0F)
        	player.prevRotationYawHead -= 360.0F;

        while (player.rotationYawHead - player.prevRotationYawHead >= 180.0F)
        	player.prevRotationYawHead += 360.0F;
	}
	
	public static State getState(Entity entity) {
		if (!entity.hasCapability(SmartStateProvider.CAPABILITY_STATE, null))
			return null;
		
		ISmartStateHandler handler = entity.getCapability(SmartStateProvider.CAPABILITY_STATE, null);
		return State.getState(handler.getSmartState());
	}
	
	public static void setState(Entity entity, State state) {
		if (!entity.hasCapability(SmartStateProvider.CAPABILITY_STATE, null))
			return;
		
		entity.getCapability(SmartStateProvider.CAPABILITY_STATE, null).setSmartState(state.id);
	}
}
