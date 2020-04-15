package net.smart.moving.model;

import java.util.Random;

import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.smart.moving.render.SmartModelCapeRenderer;
import net.smart.moving.render.SmartModelEarsRenderer;
import net.smart.moving.render.SmartModelRotationRenderer;

public class SmartModelPlayerBase extends ModelPlayerBase {
	
	private SmartModel smartModel;

	public SmartModelPlayerBase(ModelPlayerAPI modelPlayerAPI) {
		super(modelPlayerAPI);
	}
	
	public void initialize(SmartModelRotationRenderer bipedBody, SmartModelRotationRenderer bipedBodywear,
			SmartModelRotationRenderer bipedHead, SmartModelRotationRenderer bipedHeadwear,
			SmartModelRotationRenderer bipedRightArm, SmartModelRotationRenderer bipedRightArmwear,
			SmartModelRotationRenderer bipedLeftArm, SmartModelRotationRenderer bipedLeftArmwear,
			SmartModelRotationRenderer bipedRightLeg, SmartModelRotationRenderer bipedRightLegwear,
			SmartModelRotationRenderer bipedLeftLeg, SmartModelRotationRenderer bipedLeftLegwear,
			SmartModelCapeRenderer bipedCloak, SmartModelEarsRenderer bipedEars) {
		modelBiped.bipedBody = bipedBody;
		modelBiped.bipedHead = bipedHead;
		modelBiped.bipedRightArm = bipedRightArm;
		modelBiped.bipedLeftArm = bipedLeftArm;
		modelBiped.bipedRightLeg = bipedRightLeg;
		modelBiped.bipedLeftLeg = bipedLeftLeg;

		if (modelPlayer != null) {
			modelPlayer.bipedBodyWear = bipedBodywear;
			modelPlayer.bipedHeadwear = bipedHeadwear;
			modelPlayer.bipedRightArmwear = bipedRightArmwear;
			modelPlayer.bipedLeftArmwear = bipedLeftArmwear;
			modelPlayer.bipedRightLegwear = bipedRightLegwear;
			modelPlayer.bipedLeftLegwear = bipedLeftLegwear;
		}
	}
	
	@Override
	public void render(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime,
				viewHorizontalAngelOffset, viewVerticalAngelOffset, factor);
	}

	public void superRender(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		super.render(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset,
				viewVerticalAngelOffset, factor);
	}

	@Override
	public void setRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity) {
		getRenderModel().setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime,
				viewHorizontalAngelOffset, viewVerticalAngelOffset, factor, entity);
	}

	public void superSetRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor, Entity entity) {
		super.setRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset,
				viewVerticalAngelOffset, factor, entity);
	}

	@Override
	public void renderCape(float f) {
		getRenderModel().renderCloak(f);
	}

	public void superRenderCloak(float factor) {
		super.renderCape(factor);
	}

	@Override
	public ModelRenderer getRandomModelBox(Random random) {
		return getRenderModel().getRandomBox(random);
	}

	public void animateHeadRotation(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateHeadRotation", new Object[] { totalHorizontalDistance, currentHorizontalSpeed,
				totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateHeadRotation(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateHeadRotation(viewHorizontalAngelOffset, viewVerticalAngelOffset);
	}

	public void animateSleeping(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateSleeping", new Object[] { totalHorizontalDistance, currentHorizontalSpeed,
				totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateSleeping(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateSleeping();
	}

	public void animateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateArmSwinging", new Object[] { totalHorizontalDistance, currentHorizontalSpeed,
				totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateArmSwinging(totalHorizontalDistance, currentHorizontalSpeed);
	}

	public void animateRiding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateRiding", new Object[] { totalHorizontalDistance, currentHorizontalSpeed,
				totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateRiding(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateRiding();
	}

	public void animateLeftArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateLeftArmItemHolding", new Object[] { totalHorizontalDistance,
				currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateLeftArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateLeftArmItemHolding();
	}

	public void animateRightArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateRightArmItemHolding", new Object[] { totalHorizontalDistance,
				currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateRightArmItemHolding(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateRightArmItemHolding();
	}

	public void animateWorkingBody(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateWorkingBody", new Object[] { totalHorizontalDistance, currentHorizontalSpeed,
				totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateWorkingBody(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateWorkingBody();
	}

	public void animateWorkingArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateWorkingArms", new Object[] { totalHorizontalDistance, currentHorizontalSpeed,
				totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateWorkingArms(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateWorkingArms();
	}

	public void animateSneaking(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateSneaking", new Object[] { totalHorizontalDistance, currentHorizontalSpeed,
				totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateSneaking(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateSneaking();
	}

	public void animateArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateArms", new Object[] { totalHorizontalDistance, currentHorizontalSpeed, totalTime,
				viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateArms(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateArms(totalTime);
	}

	public void animateBowAiming(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		modelPlayerAPI.dynamic("animateBowAiming", new Object[] { totalHorizontalDistance, currentHorizontalSpeed,
				totalTime, viewHorizontalAngelOffset, viewVerticalAngelOffset, factor });
	}

	public void dynamicVirtualAnimateBowAiming(float totalHorizontalDistance, float currentHorizontalSpeed,
			float totalTime, float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		getRenderModel().animateBowAiming(totalTime);
	}
	
	public SmartModel getRenderModel() {
		if (smartModel == null) {
			ModelRenderer bipedBodyWear = modelPlayer != null ? modelPlayer.bipedBodyWear : null;
			ModelRenderer bipedRightArmwear = modelPlayer != null ? modelPlayer.bipedRightArmwear : null;
			ModelRenderer bipedLeftArmwear = modelPlayer != null ? modelPlayer.bipedLeftArmwear : null;
			ModelRenderer bipedRightLegwear = modelPlayer != null ? modelPlayer.bipedRightLegwear : null;
			ModelRenderer bipedLeftLegwear = modelPlayer != null ? modelPlayer.bipedLeftLegwear : null;
			ModelRenderer bipedCape = modelPlayer != null ? this.modelPlayerAPI.getBipedCapeField() : null;
			ModelRenderer bipedDeadmau5Head = modelPlayer != null ? this.modelPlayerAPI.getBipedDeadmau5HeadField() : null;
	
			smartModel = new SmartModel(this.modelPlayerAPI.getSmallArmsParameter(), modelBiped, this, modelBiped.bipedBody,
					bipedBodyWear, modelBiped.bipedHead, modelBiped.bipedHeadwear, modelBiped.bipedRightArm,
					bipedRightArmwear, modelBiped.bipedLeftArm, bipedLeftArmwear, modelBiped.bipedRightLeg,
					bipedRightLegwear, modelBiped.bipedLeftLeg, bipedLeftLegwear, bipedCape, bipedDeadmau5Head);
		}
		return smartModel;
	}

	public ModelRenderer getOuter() {
		return getRenderModel().bipedOuter;
	}

	public ModelRenderer getTorso() {
		return getRenderModel().bipedTorso;
	}

	public ModelRenderer getBody() {
		return getRenderModel().bipedBody;
	}

	public ModelRenderer getBreast() {
		return getRenderModel().bipedBreast;
	}

	public ModelRenderer getNeck() {
		return getRenderModel().bipedNeck;
	}

	public ModelRenderer getHead() {
		return getRenderModel().bipedHead;
	}

	public ModelRenderer getHeadwear() {
		return getRenderModel().bipedHeadwear;
	}

	public ModelRenderer getRightShoulder() {
		return getRenderModel().bipedRightShoulder;
	}

	public ModelRenderer getRightArm() {
		return getRenderModel().bipedRightArm;
	}

	public ModelRenderer getLeftShoulder() {
		return getRenderModel().bipedLeftShoulder;
	}

	public ModelRenderer getLeftArm() {
		return getRenderModel().bipedLeftArm;
	}

	public ModelRenderer getPelvic() {
		return getRenderModel().bipedPelvic;
	}

	public ModelRenderer getRightLeg() {
		return getRenderModel().bipedRightLeg;
	}

	public ModelRenderer getLeftLeg() {
		return getRenderModel().bipedLeftLeg;
	}

	public ModelRenderer getEars() {
		return getRenderModel().bipedEars;
	}

	public ModelRenderer getCloak() {
		return getRenderModel().bipedCloak;
	}
}
