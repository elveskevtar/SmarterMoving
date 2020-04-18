package net.smart.moving.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.smart.moving.SmartMovingFactory;
import net.smart.moving.model.SmartModel;
import net.smart.moving.model.SmartModelPlayerBase;
import net.smart.moving.player.SmartBase;
import net.smart.moving.player.SmartBase.State;
import net.smart.moving.player.SmartPlayerBase;

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
		SmartModelPlayerBase[] playerModels = null;
		SmartBase moving = SmartMovingFactory.getInstance(entityplayer);
		if (moving != null) {
			boolean isInventory = d == 0.0F && d1 == 0.0F && d2 == 0.0F && f == 0.0F && renderPartialTicks == 1.0F;

			SmartBase.State state = SmartBase.getState(entityplayer);

			playerModels = base.getRenderModels();

			for (SmartModelPlayerBase model : playerModels) {
				SmartModel playerModel = model.getRenderModel();
				playerModel.state = state;
			}
		}
		
		CurrentMainModel = modelBipedMain;
		base.superDoRender(entityplayer, d, d1, d2, f, renderPartialTicks);
		CurrentMainModel = null;
	}
}
