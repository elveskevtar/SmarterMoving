package net.smart.moving.render;

import java.lang.reflect.Field;

import api.player.model.IModelPlayerAPI;
import api.player.model.ModelPlayerAPI;
import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.smart.moving.SmartMoving;
import net.smart.moving.model.SmartModelPlayerBase;
import net.smart.properties.Name;
import net.smart.properties.Reflect;

public class SmartRenderPlayerBase extends RenderPlayerBase {
	
	private SmartRender smartRender;
	
	private ModelBiped[] allModelBipeds;
	private SmartModelPlayerBase[] allModelBases;
	
	public final static Name RenderLivingBase_layerRenderers = new Name("layerRenderers", "field_177097_h", "h");
	public final static Name LayerArmorBase_modelArmor = new Name("modelArmor", "field_177189_c", "c");
	private final static Field _modelArmor = Reflect.GetField(
			LayerArmorBase.class, LayerArmorBase_modelArmor);
	private final static Field _layerRenderers = Reflect.GetField(
			RenderLivingBase.class, RenderLivingBase_layerRenderers);

	public SmartRenderPlayerBase(RenderPlayerAPI renderPlayerAPI) {
		super(renderPlayerAPI);
	}
	
	private SmartRender getSmartRender() {
		if (smartRender == null)
			smartRender = new SmartRender(this);
		return smartRender;
	}
	
	public void initialize(ModelPlayer modelBipedMain, ModelBiped modelArmor) {}
	
	@Override
	public void doRender(AbstractClientPlayer entityPlayer, double d, double d1, double d2, float f, float renderPartialTicks) {
		getSmartRender().doRender(entityPlayer, d, d1, d2, f, renderPartialTicks);
	}
	
	public void superDoRender(AbstractClientPlayer entityPlayer, double d, double d1, double d2, float f, float renderPartialTicks) {
		super.doRender(entityPlayer, d, d1, d2, f, renderPartialTicks);
	}
	
	@Override
	public void rotateCorpse(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2) {
		getSmartRender().rotateCorpse(entityplayer, totalTime, actualRotation, f2);
	}

	public void superRotateCorpse(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2) {
		super.rotateCorpse(entityplayer, totalTime, actualRotation, f2);
	}
	
	@Override
	public void renderLivingAt(AbstractClientPlayer entityplayer, double d, double d1, double d2) {
		getSmartRender().renderLivingAt(entityplayer, d, d1, d2);
	}

	public void superRenderLivingAt(AbstractClientPlayer entityplayer, double d, double d1, double d2) {
		super.renderLivingAt(entityplayer, d, d1, d2);
	}
	
	@Override
	public void renderName(AbstractClientPlayer entityplayer, double d, double d1, double d2) {
		getSmartRender().renderName(entityplayer, d, d1, d2);
	}

	public void superRenderName(AbstractClientPlayer entityplayer, double d, double d1, double d2) {
		super.renderName(entityplayer, d, d1, d2);
	}
	
	public RenderManager getMovingRenderManager() {
		return renderPlayerAPI.getRenderManagerField();
	}
	
	public ModelPlayer getModelBipedMain() {
		return renderPlayer.getMainModel();
	}
	
	public ModelBiped getModelArmor() {
		for (Object layer : renderPlayerAPI.getLayerRenderersField())
			if (layer instanceof LayerArmorBase)
				return (ModelBiped) Reflect.GetField(_modelArmor, layer);
		return null;
	}
	
	public SmartModelPlayerBase createModel(ModelBiped existing, float f, boolean b) {
		return getPlayerBase(existing);
	}
	
	public boolean getSmallArms() {
		return renderPlayerAPI.getSmallArmsField();
	}
	
	public SmartModelPlayerBase[] getRenderModels() {
		ModelBiped[] modelPlayers = ModelPlayerAPI.getAllInstances();
		if (allModelBipeds != null &&
				(allModelBipeds == modelPlayers || modelPlayers.length == 0 && allModelBipeds.length == 0))
			return allModelBases;

		allModelBipeds = modelPlayers;
		allModelBases = new SmartModelPlayerBase[modelPlayers.length];
		for (int i = 0; i < allModelBases.length; i++)
			allModelBases[i] = getPlayerBase(allModelBipeds[i]);
		return allModelBases;
	}
	
	public SmartModelPlayerBase getMovingModelArmor() {
		return getPlayerBase(renderPlayer.getMainModel());
	}

	public SmartModelPlayerBase getMovingModelBipedMain() {
		for (Object layer : renderPlayerAPI.getLayerRenderersField())
			if (layer instanceof LayerArmorBase)
				return getPlayerBase((ModelBiped) Reflect.GetField(_modelArmor, layer));
		return null;
	}
	
	public static SmartModelPlayerBase getPlayerBase(ModelBiped modelPlayer) {
		return (SmartModelPlayerBase) ((IModelPlayerAPI) modelPlayer).getModelPlayerBase(SmartMoving.NAME);
	}
}
