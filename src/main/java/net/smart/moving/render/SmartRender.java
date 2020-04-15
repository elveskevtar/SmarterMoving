package net.smart.moving.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.smart.moving.SmartMovingFactory;
import net.smart.moving.model.SmartModel;
import net.smart.moving.model.SmartModelPlayerBase;
import net.smart.moving.player.SMBase;
import net.smart.moving.player.SMBase.State;

public class SmartRender {
	
	public static SmartModel CurrentMainModel;
	public static final int Scale = 0;
	public static final int NoScaleStart = 1;
	public static final int NoScaleEnd = 2;
	
	public SmartRenderPlayerBase base;
	public final SmartModel modelBipedMain;
	
	private static int _iOffset, _jOffset;
	private static Minecraft _minecraft;

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
		SmartModelPlayerBase[] modelPlayers = null;
		SMBase moving = SmartMovingFactory.getInstance(entityplayer);
		if (moving != null) {
			boolean isInventory = d == 0.0F && d1 == 0.0F && d2 == 0.0F && f == 0.0F && renderPartialTicks == 1.0F;

			SMBase.State state = moving.getState();

			modelPlayers = base.getRenderModels();

			for (SmartModelPlayerBase baseRef : modelPlayers) {
				SmartModel modelPlayer = baseRef.getRenderModel();
				modelPlayer.state = state;
			}

			if (!isInventory && entityplayer.isSneaking() && !(entityplayer instanceof EntityPlayerSP) && state == State.CRAWL)
				d1 += 0.125D;
		}

		CurrentMainModel = modelBipedMain;
		base.superDoRender(entityplayer, d, d1, d2, f, renderPartialTicks);
		CurrentMainModel = null;
	}
	
	public void rotateCorpse(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2) {
		base.superRotateCorpse(entityplayer, totalTime, actualRotation, f2);
	}

	public void renderLivingAt(AbstractClientPlayer entityplayer, double d, double d1, double d2) {
		base.superRenderLivingAt(entityplayer, d, d1, d2);
	}

	public void renderName(AbstractClientPlayer entityPlayer, double d, double d1, double d2) {
		boolean changedIsSneaking = false, originalIsSneaking = false;
		if (Minecraft.isGuiEnabled() && entityPlayer != base.getMovingRenderManager().pointedEntity) {
			SMBase moving = SmartMovingFactory.getInstance(entityPlayer);
			if (moving != null) {
				originalIsSneaking = entityPlayer.isSneaking();
				boolean temporaryIsSneaking = originalIsSneaking;
				if (moving.getState() == State.CRAWL) {
					temporaryIsSneaking = true;
					d1 -= 0.2F;
				} else if (originalIsSneaking && !temporaryIsSneaking) {
					d1 -= 0.05F;
				}

				changedIsSneaking = temporaryIsSneaking != originalIsSneaking;
				if (changedIsSneaking)
					entityPlayer.setSneaking(temporaryIsSneaking);
			}
		}

		base.superRenderName(entityPlayer, d, d1, d2);

		if (changedIsSneaking)
			entityPlayer.setSneaking(originalIsSneaking);
	}
}
