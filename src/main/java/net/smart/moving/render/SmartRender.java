package net.smart.moving.render;

import static net.smart.moving.utilities.RenderUtilities.Half;
import static net.smart.moving.utilities.RenderUtilities.Quarter;
import static net.smart.moving.utilities.RenderUtilities.RadiantToAngle;
import static net.smart.moving.utilities.RenderUtilities.Whole;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.math.MathHelper;
import net.smart.moving.SmartMovingFactory;
import net.smart.moving.model.SmartModel;
import net.smart.moving.model.SmartModelPlayerBase;
import net.smart.moving.player.SmartBase;

public class SmartRender {
	
	public static SmartModel CurrentMainModel;
	public static final int Scale = 0;
	public static final int NoScaleStart = 1;
	public static final int NoScaleEnd = 2;
	
	public SmartRenderPlayerBase base;
	public final SmartModel modelBipedMain;
	
	public SmartRender(SmartRenderPlayerBase base) {
		this.base = base;
		
		modelBipedMain = base.getMovingModelBipedMain().getRenderModel();
		SmartModel modelArmor = base.getMovingModelArmor().getRenderModel();
		
		modelBipedMain.scaleArmType = Scale;
		modelBipedMain.scaleLegType = Scale;
		modelArmor.scaleArmType = NoScaleStart;
		modelArmor.scaleLegType = Scale;
	}
	
	public void doRender(AbstractClientPlayer entityplayer,
			double d, double d1, double d2, float f, float renderPartialTicks) {
		SmartModelPlayerBase[] playerModels = null;
		SmartBase moving = SmartMovingFactory.getInstance(entityplayer);
		if (moving != null) {
			boolean isInventory = d == 0.0F && d1 == 0.0F && d2 == 0.0F && f == 0.0F && renderPartialTicks == 1.0F;
			if (!isInventory) {
				double diffX = entityplayer.posX - entityplayer.prevPosX;
				double diffY = entityplayer.posY - entityplayer.prevPosY;
				double diffZ = entityplayer.posZ - entityplayer.prevPosZ;
				float horizontalMove = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
				float verticalAngle = (float) Math.atan2(diffY, horizontalMove);
				if (Float.isNaN(verticalAngle) || horizontalMove <= 0.08)
					verticalAngle = diffY < 0 ? -Quarter : Quarter;
				float forwardRotation = moving.forwardRotation / RadiantToAngle;
				float horizontalAngle = 0;
				if (horizontalMove >= 0.08) {
					horizontalAngle = (float) Math.atan2(diffZ, diffX) - forwardRotation - Quarter;
					if (Float.isNaN(horizontalAngle) || diffX == 0 || diffZ == 0)
						horizontalAngle = moving.horizontalAngle;				
					while (horizontalAngle - moving.horizontalAngle > Half)
						horizontalAngle -= Whole;
					while (horizontalAngle - moving.horizontalAngle < -Half)
						horizontalAngle += Whole;
				}
				
				moving.currentSpeed = MathHelper.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2) + Math.pow(diffZ, 2));
				moving.forwardRotation = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * renderPartialTicks;
				moving.verticalAngle += (verticalAngle - moving.verticalAngle) * 0.1F;
				moving.horizontalAngle += (horizontalAngle - moving.horizontalAngle) * 0.1F;
				moving.totalDistance += moving.currentSpeed * renderPartialTicks;
			}
			
			playerModels = base.getRenderModels();
			for (SmartModelPlayerBase model : playerModels) {
				SmartModel playerModel = model.getRenderModel();
				playerModel.state = SmartBase.getState(entityplayer);
			}
		}
		
		CurrentMainModel = modelBipedMain;
		base.superDoRender(entityplayer, d, d1, d2, f, renderPartialTicks);
		CurrentMainModel = null;
	}
}
