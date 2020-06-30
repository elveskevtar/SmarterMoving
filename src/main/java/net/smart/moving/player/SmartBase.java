package net.smart.moving.player;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.smart.moving.capabilities.ISmartStateHandler;
import net.smart.moving.capabilities.SmartStateProvider;
import net.smart.moving.player.state.State;
import net.smart.moving.utilities.RenderUtilities;

public abstract class SmartBase {

	private Set<BlockPos> boundingBlocks = new HashSet<BlockPos>();

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

		state.handler.afterOnUpdate(player);
		updateBoundingBox();
		state.handler.updatePlayerRotations(player, this);
		wrapRotations();
	}
	
	protected void moveFlying(float vertical, float strafing, float forward, float speedFactor, boolean threeDimensional) {
		float diffMotionXStrafing = 0, diffMotionXForward = 0, diffMotionZStrafing = 0, diffMotionZForward = 0;
		float horizontalTotal = MathHelper.sqrt(strafing * strafing + forward * forward);
		if(horizontalTotal >= 0.01F) {
			if(horizontalTotal < 1.0F)
				horizontalTotal = 1.0F;

			float moveStrafingFactor = strafing / horizontalTotal;
			float moveForwardFactor = forward / horizontalTotal;
			float sin = MathHelper.sin((float) (player.rotationYaw * Math.PI / 180F));
			float cos = MathHelper.cos((float) (player.rotationYaw * Math.PI / 180F));
			diffMotionXStrafing = moveStrafingFactor * cos;
			diffMotionXForward = -moveForwardFactor * sin;
			diffMotionZStrafing = moveStrafingFactor * sin;
			diffMotionZForward = moveForwardFactor * cos;
		}

		float rotation = threeDimensional ? player.rotationPitch / RenderUtilities.RadiantToAngle : 0;
		float divingHorizontalFactor = MathHelper.cos(rotation);
		float divingVerticalFactor = -MathHelper.sin(rotation) * Math.signum(forward);

		float diffMotionX = diffMotionXForward * divingHorizontalFactor + diffMotionXStrafing;
		float diffMotionY = MathHelper.sqrt(diffMotionXForward * diffMotionXForward + diffMotionZForward * diffMotionZForward) * divingVerticalFactor + vertical;
		float diffMotionZ = diffMotionZForward * divingHorizontalFactor + diffMotionZStrafing;

		float total = MathHelper.sqrt(MathHelper.sqrt(diffMotionX * diffMotionX + diffMotionZ * diffMotionZ) + diffMotionY * diffMotionY);
		if(total > 0.01F) {
			float factor = speedFactor / total;
			player.motionX = diffMotionX * factor;
			player.motionY = diffMotionY * factor;
			player.motionZ = diffMotionZ * factor;
		}
	}
	
	protected void setHorizontalDamping(float horizontalDamping) {
		player.motionX *= horizontalDamping;
		player.motionZ *= horizontalDamping;
	}

	protected void updateBoundingBox() {
		final double d0 = player.width / 2.0D;
		AxisAlignedBB aabb = player.getEntityBoundingBox();
		player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, aabb.minY,
				player.posZ - d0, player.posX + d0, aabb.minY + player.height, player.posZ + d0));

		boundingBlocks.clear();
		aabb = player.getEntityBoundingBox();
		for (int x = (int)Math.floor(aabb.minX); x <= (int)Math.floor(aabb.maxX); x++)
			for (int z = (int)Math.floor(aabb.minZ); z <= (int)Math.floor(aabb.maxZ); z++)
				boundingBlocks.add(new BlockPos(x, (int)player.posY, z));
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
	
	protected BlockPos getBlockPos() {
		return new BlockPos(Math.floor(player.posX), player.posY, Math.floor(player.posZ));
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
