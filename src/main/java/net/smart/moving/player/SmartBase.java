package net.smart.moving.player;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smart.moving.SmartMovingContext;
import net.smart.moving.capabilities.ISmartStateHandler;
import net.smart.moving.capabilities.SmartStateProvider;
import net.smart.moving.config.SmartOptions;
import net.smart.moving.input.SmartInput;
import net.smart.moving.network.SmartPacketHandler;
import net.smart.moving.network.packet.StatePacket;
import net.smart.moving.player.SmartBase.State;
import net.smart.moving.utilities.RenderUtilities;

public abstract class SmartBase {
	
	public static enum State {
		INVALID(-1), IDLE(0), SNEAK(1), CRAWL(2);
		
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
	
	private static final float defaultHeight = 1.8F;
	private static final float crawlHeight = 0.65F;
	private static final float crawlEyeHeight = 0.5F;
	private static final float crawlDampingFactor = 0.3F;
	
	protected EntityPlayer player;
	
	public SmartBase(EntityPlayer player) {
		this.player = player;
	}
	
	public void afterOnUpdate() {
		State state = getState(player);
		if (state == null || state == State.INVALID)
			return;
		
		if (state == State.IDLE) {
			player.height = defaultHeight;
			player.eyeHeight = player.getDefaultEyeHeight();
		} else if (state == State.CRAWL) {
			setHorizontalDamping(crawlDampingFactor);
			player.height = crawlHeight;
			player.eyeHeight = crawlEyeHeight;
		}
		
		updateBoundingBox();
		updatePlayerRotations(state);
	}
	
	protected void updatePlayerRotations(State state) {
        double diffPosX = player.posX - player.prevPosX;
        double diffPosY = player.posZ - player.prevPosZ;
        float x2y2 = (float) (diffPosX * diffPosX + diffPosY * diffPosY);
        float yawOffset = player.renderYawOffset;

        if (x2y2 > 0) {
            float moveDir = (float) MathHelper.atan2(diffPosY, diffPosX) * (180F / (float)Math.PI) - 90.0F;
            float yawMoveDiff = MathHelper.abs(MathHelper.wrapDegrees(player.rotationYaw) - moveDir);

            if (95.0F < yawMoveDiff && yawMoveDiff < 265.0F && state != State.CRAWL)
                yawOffset = moveDir - 180.0F;
            else
                yawOffset = moveDir;
        }

        if (player.swingProgress > 0.0F)
            yawOffset = player.rotationYaw;
        
        float f = MathHelper.wrapDegrees(yawOffset - player.renderYawOffset);
        player.renderYawOffset += f * 0.3F;
        float f1 = MathHelper.wrapDegrees(player.rotationYaw - player.renderYawOffset);

        if (f1 < -75.0F)
            f1 = -75.0F;

        if (f1 >= 75.0F)
            f1 = 75.0F;

        player.renderYawOffset = player.rotationYaw - f1;

        if (f1 * f1 > 2500.0F)
        	player.renderYawOffset += f1 * 0.2F;

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
	
	protected void setHorizontalDamping(float horizontalDamping) {
		player.motionX *= horizontalDamping;
		player.motionZ *= horizontalDamping;
	}

	protected void updateBoundingBox() {
		final double d0 = player.width / 2.0D;
		final AxisAlignedBB aabb = player.getEntityBoundingBox();
		player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, aabb.minY,
				player.posZ - d0, player.posX + d0, aabb.minY + player.height, player.posZ + d0));
	}
	
	protected boolean isOpenBlockSpace(BlockPos pos) {
		IBlockState blockState = player.world.getBlockState(pos);
		IBlockState upBlockState = player.world.getBlockState(pos.up());
		return !blockState.getBlock().isNormalCube(blockState, player.world, pos)
				&& !upBlockState.getBlock().isNormalCube(upBlockState, player.world, pos.up());
	}
	
	protected boolean isHeadspaceFree(BlockPos pos, int height) {
		for (int y = 0; y < height; y++)
			if (!isOpenBlockSpace(pos.add(0, y, 0)))
				return false;
		return true;
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
