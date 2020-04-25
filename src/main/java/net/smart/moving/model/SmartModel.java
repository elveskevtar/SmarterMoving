// ==================================================================
// This file is part of Smart Render.
//
// Smart Render is free software: you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// Smart Render is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Smart Render. If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package net.smart.moving.model;

import static net.smart.moving.render.SmartRender.NoScaleEnd;
import static net.smart.moving.render.SmartRender.NoScaleStart;
import static net.smart.moving.render.SmartRender.Scale;
import static net.smart.moving.utilities.RenderUtilities.Eighth;
import static net.smart.moving.utilities.RenderUtilities.Half;
import static net.smart.moving.utilities.RenderUtilities.Quarter;
import static net.smart.moving.utilities.RenderUtilities.RadiantToAngle;
import static net.smart.moving.utilities.RenderUtilities.Sixteenth;
import static net.smart.moving.utilities.RenderUtilities.Sixtyfourth;
import static net.smart.moving.utilities.RenderUtilities.Thirtytwoth;
import static net.smart.moving.utilities.RenderUtilities.Whole;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.smart.moving.player.SmartBase;
import net.smart.moving.player.SmartPlayer;
import net.smart.moving.player.SmartPlayerBase;
import net.smart.moving.render.SmartModelCapeRenderer;
import net.smart.moving.render.SmartModelEarsRenderer;
import net.smart.moving.render.SmartModelRotationRenderer;
import net.smart.moving.render.SmartRender;
import net.smart.moving.render.SmartRenderData;
import net.smart.moving.utilities.RenderUtilities;
import net.smart.properties.Name;
import net.smart.properties.Reflect;

public class SmartModel {
	
	private SmartModelPlayerBase base;
	public ModelBiped modelBiped;

	private boolean isModelPlayer;
	private boolean smallArms;

	public SmartModelRotationRenderer bipedOuter;
	public SmartModelRotationRenderer bipedTorso;
	public SmartModelRotationRenderer bipedBody;
	public SmartModelRotationRenderer bipedBreast;
	public SmartModelRotationRenderer bipedNeck;
	public SmartModelRotationRenderer bipedHead;
	public SmartModelRotationRenderer bipedRightShoulder;
	public SmartModelRotationRenderer bipedRightArm;
	public SmartModelRotationRenderer bipedLeftShoulder;
	public SmartModelRotationRenderer bipedLeftArm;
	public SmartModelRotationRenderer bipedPelvic;
	public SmartModelRotationRenderer bipedRightLeg;
	public SmartModelRotationRenderer bipedLeftLeg;

	public SmartModelRotationRenderer bipedBodywear;
	public SmartModelRotationRenderer bipedHeadwear;
	public SmartModelRotationRenderer bipedRightArmwear;
	public SmartModelRotationRenderer bipedLeftArmwear;
	public SmartModelRotationRenderer bipedRightLegwear;
	public SmartModelRotationRenderer bipedLeftLegwear;

	public SmartModelEarsRenderer bipedEars;
	public SmartModelCapeRenderer bipedCloak;

	public float actualRotation;
	
	public SmartRenderData prevOuterRenderData;
	public boolean isSleeping;
	public boolean isInventory;

	private final static Name ModelRenderer_textureOffsetX = new Name("textureOffsetX", "field_78803_o", "r");
	private final static Name ModelRenderer_textureOffsetY = new Name("textureOffsetY", "field_78813_p", "s");
	private static final Field _textureOffsetX = Reflect.GetField(ModelRenderer.class, ModelRenderer_textureOffsetX);
	private static final Field _textureOffsetY = Reflect.GetField(ModelRenderer.class, ModelRenderer_textureOffsetY);
	
	public int scaleArmType;
	public int scaleLegType;
	
	public SmartBase.State state;
	public float currentHorizontalSpeedFlattened;
	public float prevMoveAngle;

	public SmartModel(boolean smallArms, ModelBiped modelBiped, SmartModelPlayerBase base, ModelRenderer originalBipedBody,
			ModelRenderer originalBipedBodywear, ModelRenderer originalBipedHead, ModelRenderer originalBipedHeadwear,
			ModelRenderer originalBipedRightArm, ModelRenderer originalBipedRightArmwear,
			ModelRenderer originalBipedLeftArm, ModelRenderer originalBipedLeftArmwear,
			ModelRenderer originalBipedRightLeg, ModelRenderer originalBipedRightLegwear,
			ModelRenderer originalBipedLeftLeg, ModelRenderer originalBipedLeftLegwear,
			ModelRenderer originalBipedCape, ModelRenderer originalBipedDeadmau5Head) {
		this.smallArms = smallArms;
		this.base = base;
		this.modelBiped = modelBiped;

		isModelPlayer = modelBiped instanceof ModelPlayer;

		modelBiped.boxList.clear();

		bipedOuter = create(null);
		bipedOuter.fadeEnabled = true;

		bipedTorso = create(bipedOuter);
		bipedBody = create(bipedTorso, originalBipedBody);
		bipedBreast = create(bipedTorso);
		bipedNeck = create(bipedBreast);
		bipedHead = create(bipedNeck, originalBipedHead);
		bipedRightShoulder = create(bipedBreast);
		bipedRightArm = create(bipedRightShoulder, originalBipedRightArm);
		bipedLeftShoulder = create(bipedBreast);
		bipedLeftShoulder.mirror = true;
		bipedLeftArm = create(bipedLeftShoulder, originalBipedLeftArm);
		bipedPelvic = create(bipedTorso);
		bipedRightLeg = create(bipedPelvic, originalBipedRightLeg);
		bipedLeftLeg = create(bipedPelvic, originalBipedLeftLeg);

		bipedBodywear = create(bipedBody, originalBipedBodywear);
		bipedHeadwear = create(bipedHead, originalBipedHeadwear);
		bipedRightArmwear = create(bipedRightArm, originalBipedRightArmwear);
		bipedLeftArmwear = create(bipedLeftArm, originalBipedLeftArmwear);
		bipedRightLegwear = create(bipedRightLeg, originalBipedRightLegwear);
		bipedLeftLegwear = create(bipedLeftLeg, originalBipedLeftLegwear);

		if (originalBipedCape != null) {
			bipedCloak = new SmartModelCapeRenderer(modelBiped, 0, 0, bipedBreast, bipedOuter);
			copy(bipedCloak, originalBipedCape);
		}

		if (originalBipedDeadmau5Head != null) {
			bipedEars = new SmartModelEarsRenderer(modelBiped, 24, 0, bipedHead);
			copy(bipedEars, originalBipedDeadmau5Head);
		}

		reset(); // set default rotation points

		base.initialize(bipedBody, bipedBodywear, bipedHead, bipedHeadwear, bipedRightArm, bipedRightArmwear,
				bipedLeftArm, bipedLeftArmwear, bipedRightLeg, bipedRightLegwear, bipedLeftLeg, bipedLeftLegwear,
				bipedCloak, bipedEars);
		
		if (SmartRender.CurrentMainModel != null) {
			isInventory = SmartRender.CurrentMainModel.isInventory;
			
			prevOuterRenderData = SmartRender.CurrentMainModel.prevOuterRenderData;
			isSleeping = SmartRender.CurrentMainModel.isSleeping;
			
			state = SmartRender.CurrentMainModel.state;
			currentHorizontalSpeedFlattened = SmartRender.CurrentMainModel.currentHorizontalSpeedFlattened;
		}
	}

	private SmartModelRotationRenderer create(SmartModelRotationRenderer base) {
		return new SmartModelRotationRenderer(modelBiped, -1, -1, base);
	}

	private SmartModelRotationRenderer create(SmartModelRotationRenderer base, ModelRenderer original) {
		if (original == null)
			return null;

		int textureOffsetX = (int) Reflect.GetField(_textureOffsetX, original);
		int textureOffsetY = (int) Reflect.GetField(_textureOffsetY, original);
		SmartModelRotationRenderer local = new SmartModelRotationRenderer(modelBiped, textureOffsetX, textureOffsetY, base);
		copy(local, original);
		return local;
	}

	private static void copy(SmartModelRotationRenderer local, ModelRenderer original) {
		if (original.childModels != null)
			for (Object childModel : original.childModels)
				local.addChild((ModelRenderer) childModel);
		if (original.cubeList != null)
			for (Object cube : original.cubeList)
				local.cubeList.add((ModelBox) cube);
		local.mirror = original.mirror;
		local.isHidden = original.isHidden;
		local.showModel = original.showModel;
	}

	public void render(Entity entity, float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngelOffset, float viewVerticalAngelOffset, float factor) {
		GL11.glPushMatrix();
		if (entity.isSneaking())
			GL11.glTranslatef(0.0F, 0.2F, 0.0F);

		bipedBody.ignoreRender = bipedHead.ignoreRender = bipedRightArm.ignoreRender = bipedLeftArm.ignoreRender = bipedRightLeg.ignoreRender = bipedLeftLeg.ignoreRender = true;
		if (isModelPlayer)
			bipedBodywear.ignoreRender = bipedHeadwear.ignoreRender = bipedRightArmwear.ignoreRender = bipedLeftArmwear.ignoreRender = bipedRightLegwear.ignoreRender = bipedLeftLegwear.ignoreRender = true;
		base.superRender(entity, totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngelOffset,
				viewVerticalAngelOffset, factor);
		if (isModelPlayer)
			bipedBodywear.ignoreRender = bipedHeadwear.ignoreRender = bipedRightArmwear.ignoreRender = bipedLeftArmwear.ignoreRender = bipedRightLegwear.ignoreRender = bipedLeftLegwear.ignoreRender = false;
		bipedBody.ignoreRender = bipedHead.ignoreRender = bipedRightArm.ignoreRender = bipedLeftArm.ignoreRender = bipedRightLeg.ignoreRender = bipedLeftLeg.ignoreRender = false;

		bipedOuter.render(factor);
		bipedOuter.renderIgnoreBase(factor);
		bipedTorso.renderIgnoreBase(factor);
		bipedBody.renderIgnoreBase(factor);
		bipedBreast.renderIgnoreBase(factor);
		bipedNeck.renderIgnoreBase(factor);
		bipedHead.renderIgnoreBase(factor);
		bipedRightShoulder.renderIgnoreBase(factor);
		bipedRightArm.renderIgnoreBase(factor);
		bipedLeftShoulder.renderIgnoreBase(factor);
		bipedLeftArm.renderIgnoreBase(factor);
		bipedPelvic.renderIgnoreBase(factor);
		bipedRightLeg.renderIgnoreBase(factor);
		bipedLeftLeg.renderIgnoreBase(factor);

		if (isModelPlayer) {
			bipedBodywear.renderIgnoreBase(factor);
			bipedHeadwear.renderIgnoreBase(factor);
			bipedRightArmwear.renderIgnoreBase(factor);
			bipedLeftArmwear.renderIgnoreBase(factor);
			bipedRightLegwear.renderIgnoreBase(factor);
			bipedLeftLegwear.renderIgnoreBase(factor);
		}

		GL11.glPopMatrix();
	}

	public void setRotationAngles(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngleOffset, float viewVerticalAngleOffset, float factor, Entity entity) {
		reset();
		
		if (state != null) {
			switch (state) {
			case IDLE:
				animateStandard(totalHorizontalDistance, currentHorizontalSpeed,
						totalTime, viewHorizontalAngleOffset, viewVerticalAngleOffset,
						factor, entity);
				break;
			case SNEAK:
				animateStandard(totalHorizontalDistance, currentHorizontalSpeed,
						totalTime, viewHorizontalAngleOffset, viewVerticalAngleOffset,
						factor, entity);
				break;
			case CRAWL:
				animateCrawling(totalHorizontalDistance, currentHorizontalSpeed,
						totalTime, viewHorizontalAngleOffset, viewVerticalAngleOffset,
						factor, entity);
				break;
			case FLY:
				animateFlying(totalHorizontalDistance, currentHorizontalSpeed,
						totalTime, viewHorizontalAngleOffset, viewVerticalAngleOffset,
						factor, entity);
				break;
			case ELYTRA:
				animateStandard(totalHorizontalDistance, currentHorizontalSpeed,
						totalTime, viewHorizontalAngleOffset, viewVerticalAngleOffset,
						factor, entity);
				break;
			default:
				break;
			}
		}
	}
	
	public void animateStandard(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngleOffset, float viewVerticalAngleOffset, float factor, Entity entity) {
		if (isInventory) {
			bipedBody.ignoreBase = true;
			bipedHead.ignoreBase = true;
			bipedRightArm.ignoreBase = true;
			bipedLeftArm.ignoreBase = true;
			bipedRightLeg.ignoreBase = true;
			bipedLeftLeg.ignoreBase = true;

			if (isModelPlayer) {
				bipedBodywear.ignoreBase = true;
				bipedHeadwear.ignoreBase = true;
				bipedRightArmwear.ignoreBase = true;
				bipedLeftArmwear.ignoreBase = true;
				bipedRightLegwear.ignoreBase = true;
				bipedLeftLegwear.ignoreBase = true;
				bipedEars.ignoreBase = true;
				bipedCloak.ignoreBase = true;
			}

			bipedBody.forceRender = false;
			bipedHead.forceRender = false;
			bipedRightArm.forceRender = false;
			bipedLeftArm.forceRender = false;
			bipedRightLeg.forceRender = false;
			bipedLeftLeg.forceRender = false;

			if (isModelPlayer) {
				bipedBodywear.forceRender = false;
				bipedHeadwear.forceRender = false;
				bipedRightArmwear.forceRender = false;
				bipedLeftArmwear.forceRender = false;
				bipedRightLegwear.forceRender = false;
				bipedLeftLegwear.forceRender = false;
				bipedEars.forceRender = false;
				bipedCloak.forceRender = false;
			}

			bipedRightArm.setRotationPoint(-5F, 2.0F, 0.0F);
			bipedLeftArm.setRotationPoint(5F, 2.0F, 0.0F);
			bipedRightLeg.setRotationPoint(-2F, 12F, 0.0F);
			bipedLeftLeg.setRotationPoint(2.0F, 12F, 0.0F);

			base.superSetRotationAngles(totalHorizontalDistance, currentHorizontalSpeed, totalTime,
					viewHorizontalAngleOffset, viewVerticalAngleOffset, factor, entity);
			return;
		}

		if (isSleeping) {
			prevOuterRenderData.rotateAngleX = 0;
			prevOuterRenderData.rotateAngleY = 0;
			prevOuterRenderData.rotateAngleZ = 0;
		}

		bipedOuter.prevData = prevOuterRenderData;

		bipedOuter.rotateAngleY = actualRotation / RadiantToAngle;
		bipedOuter.fadeRotateAngleY = !entity.isRiding();

		base.animateHeadRotation(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngleOffset,
				viewVerticalAngleOffset, factor);

		if (isSleeping)
			base.animateSleeping(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngleOffset,
					viewVerticalAngleOffset, factor);

		base.animateArmSwinging(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngleOffset,
				viewVerticalAngleOffset, factor);

		if (modelBiped.isRiding)
			base.animateRiding(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngleOffset,
					viewVerticalAngleOffset, factor);

		if (modelBiped.leftArmPose != ArmPose.EMPTY)
			base.animateLeftArmItemHolding(totalHorizontalDistance, currentHorizontalSpeed, totalTime,
					viewHorizontalAngleOffset, viewVerticalAngleOffset, factor);

		if (modelBiped.rightArmPose != ArmPose.EMPTY)
			base.animateRightArmItemHolding(totalHorizontalDistance, currentHorizontalSpeed, totalTime,
					viewHorizontalAngleOffset, viewVerticalAngleOffset, factor);

		if (modelBiped.swingProgress > -9990F) {
			base.animateWorkingBody(totalHorizontalDistance, currentHorizontalSpeed, totalTime,
					viewHorizontalAngleOffset, viewVerticalAngleOffset, factor);
			base.animateWorkingArms(totalHorizontalDistance, currentHorizontalSpeed, totalTime,
					viewHorizontalAngleOffset, viewVerticalAngleOffset, factor);
		}

		if (modelBiped.isSneak)
			base.animateSneaking(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngleOffset,
					viewVerticalAngleOffset, factor);

		base.animateArms(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngleOffset,
				viewVerticalAngleOffset, factor);

		if (modelBiped.rightArmPose == ArmPose.BOW_AND_ARROW || modelBiped.leftArmPose == ArmPose.BOW_AND_ARROW)
			base.animateBowAiming(totalHorizontalDistance, currentHorizontalSpeed, totalTime, viewHorizontalAngleOffset,
					viewVerticalAngleOffset, factor);

		if (bipedOuter.prevData != null && !bipedOuter.fadeRotateAngleX)
			bipedOuter.prevData.rotateAngleX = bipedOuter.rotateAngleX;

		if (bipedOuter.prevData != null && !bipedOuter.fadeRotateAngleY)
			bipedOuter.prevData.rotateAngleY = bipedOuter.rotateAngleY;

		bipedOuter.fadeIntermediate(totalTime);
		bipedOuter.fadeStore(totalTime);

		if (isModelPlayer) {
			bipedCloak.ignoreBase = false;
			bipedCloak.rotateAngleX = Sixtyfourth;
		}
	}
	
	public void animateCrawling(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngleOffset, float viewVerticalAngleOffset, float factor, Entity entity) {
		/* Rotate head */
		bipedHead.rotationPointZ = -2F;
		bipedHead.rotateAngleZ = -viewHorizontalAngleOffset / RadiantToAngle;
		bipedHead.rotateAngleX = -Eighth;
		
		/* Rotate biped flat on the ground */
		bipedTorso.rotationOrder = SmartModelRotationRenderer.YZX;
		bipedTorso.rotateAngleX = Quarter - Thirtytwoth;
		bipedTorso.offsetZ = -entity.height;
		
		/* Rotate torso back and forth*/
		float distance = totalHorizontalDistance * 0.5F;
		float walkFactor = Factor(currentHorizontalSpeed, 0F, 0.12951545F);
		float standFactor = Factor(currentHorizontalSpeed, 0.12951545F, 0F);
		bipedTorso.rotationPointY = 3F;
		bipedTorso.rotateAngleZ = MathHelper.cos(distance + Quarter) * Sixtyfourth * walkFactor;
		bipedBody.rotateAngleY = MathHelper.cos(distance + Half) * Sixtyfourth * walkFactor;
		
		/* Rotate legs */
		bipedRightLeg.rotateAngleX = (MathHelper.cos(distance - Quarter)
				* Sixtyfourth + Thirtytwoth) * walkFactor + Thirtytwoth * standFactor;
		bipedLeftLeg.rotateAngleX = (MathHelper.cos(distance - Half - Quarter)
				* Sixtyfourth + Thirtytwoth) * walkFactor + Thirtytwoth * standFactor;
		bipedRightLeg.rotateAngleZ = (MathHelper.cos(distance - Quarter) + 1F)
				* 0.25F * walkFactor + Thirtytwoth * standFactor;
		bipedLeftLeg.rotateAngleZ = (MathHelper.cos(distance - Quarter) - 1F)
				* 0.25F * walkFactor - Thirtytwoth * standFactor;
		if (scaleLegType != NoScaleStart)
			setLegScales(1F + (MathHelper.cos(distance) - 1F) * 0.25F * walkFactor,
					1F + (MathHelper.cos(distance - Quarter - Quarter) - 1F) * 0.25F * walkFactor);
		
		/* Rotate arms */
		bipedRightArm.rotationOrder = SmartModelRotationRenderer.YZX;
		bipedLeftArm.rotationOrder = SmartModelRotationRenderer.YZX;
		bipedRightArm.rotateAngleX = Half + Eighth;
		bipedLeftArm.rotateAngleX = Half + Eighth;
		bipedRightArm.rotateAngleZ = ((MathHelper.cos(distance + Half)) * Sixtyfourth + Thirtytwoth)
				* walkFactor + Sixteenth * standFactor;
		bipedLeftArm.rotateAngleZ = ((MathHelper.cos(distance + Half)) * Sixtyfourth - Thirtytwoth)
				* walkFactor - Sixteenth * standFactor;
		bipedRightArm.rotateAngleY = -Quarter;
		bipedLeftArm.rotateAngleY = Quarter;
		if (scaleArmType != NoScaleStart)
			setArmScales(1F + (MathHelper.cos(distance + Quarter) - 1F) * 0.15F * walkFactor,
					1F + (MathHelper.cos(distance - Quarter) - 1F) * 0.15F * walkFactor);
	}
	
	public void animateFlying(float totalHorizontalDistance, float currentHorizontalSpeed, float totalTime,
			float viewHorizontalAngleOffset, float viewVerticalAngleOffset, float factor, Entity entity) {
		SmartPlayerBase base = SmartPlayerBase.getPlayerBase((EntityPlayerSP) entity);
		SmartPlayer player = base.getSmartPlayer();
		float distance = player.totalDistance * 0.08F;
		float walkFactor = Factor(currentHorizontalSpeed, 0F, 1F);
		float standFactor = Factor(currentHorizontalSpeed, 1F, 0F);
		float time = totalTime * 0.15F;

		/* Rotate player body */
		bipedOuter.fadeRotateAngleX = true;
		bipedOuter.rotateAngleX = Quarter - player.verticalAngle;
		bipedOuter.rotateAngleY = player.horizontalAngle;
		
		/* Rotate head */
		bipedHead.rotateAngleX = -bipedOuter.rotateAngleX / 2F;

		/* Rotate arms */
		bipedRightArm.rotationOrder = SmartModelRotationRenderer.XZY;
		bipedLeftArm.rotationOrder = SmartModelRotationRenderer.XZY;
		bipedRightArm.rotateAngleY = (MathHelper.cos(time) * Sixteenth) * standFactor;
		bipedLeftArm.rotateAngleY = (MathHelper.cos(time) * Sixteenth) * standFactor;
		bipedRightArm.rotateAngleZ = (MathHelper.cos(distance + Half) * Sixtyfourth + (Half - Sixteenth)) * walkFactor + Quarter * standFactor;
		bipedLeftArm.rotateAngleZ = (MathHelper.cos(distance) * Sixtyfourth - (Half - Sixteenth)) * walkFactor - Quarter * standFactor;
		
		/* Rotate legs */
		bipedRightLeg.rotateAngleX = MathHelper.cos(distance) * Sixtyfourth * walkFactor + MathHelper.cos(time + Half) * Sixtyfourth * standFactor;
		bipedLeftLeg.rotateAngleX = MathHelper.cos(distance + Half) * Sixtyfourth * walkFactor + MathHelper.cos(time) * Sixtyfourth * standFactor;
		bipedRightLeg.rotateAngleZ = Sixtyfourth;
		bipedLeftLeg.rotateAngleZ = -Sixtyfourth;
	}

	public void animateHeadRotation(float viewHorizontalAngleOffset, float viewVerticalAngleOffset) {
		bipedNeck.ignoreBase = true;
		bipedHead.rotateAngleY = (actualRotation + viewHorizontalAngleOffset) / RenderUtilities.RadiantToAngle;
		bipedHead.rotateAngleX = viewVerticalAngleOffset / RenderUtilities.RadiantToAngle;
	}

	public void animateSleeping() {
		bipedNeck.ignoreBase = false;
		bipedHead.rotateAngleY = 0F;
		bipedHead.rotateAngleX = RenderUtilities.Eighth;
		bipedTorso.rotationPointZ = -17F;
	}

	public void animateArmSwinging(float totalHorizontalDistance, float currentHorizontalSpeed) {
		bipedRightArm.rotateAngleX = MathHelper.cos(totalHorizontalDistance * 0.6662F + RenderUtilities.Half) * 2.0F
				* currentHorizontalSpeed * 0.5F;
		bipedLeftArm.rotateAngleX = MathHelper.cos(totalHorizontalDistance * 0.6662F) * 2.0F * currentHorizontalSpeed
				* 0.5F;

		bipedRightLeg.rotateAngleX = MathHelper.cos(totalHorizontalDistance * 0.6662F) * 1.4F * currentHorizontalSpeed;
		bipedLeftLeg.rotateAngleX = MathHelper.cos(totalHorizontalDistance * 0.6662F + RenderUtilities.Half) * 1.4F
				* currentHorizontalSpeed;
	}

	public void animateRiding() {
		bipedRightArm.rotateAngleX += -0.6283185F;
		bipedLeftArm.rotateAngleX += -0.6283185F;
		bipedRightLeg.rotateAngleX = -1.256637F;
		bipedLeftLeg.rotateAngleX = -1.256637F;
		bipedRightLeg.rotateAngleY = 0.3141593F;
		bipedLeftLeg.rotateAngleY = -0.3141593F;
	}

	public void animateLeftArmItemHolding() {
		bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - 0.3141593F;
	}

	public void animateRightArmItemHolding() {
		bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - 0.3141593F;
	}

	public void animateWorkingBody() {
		float angle = MathHelper.sin(MathHelper.sqrt(modelBiped.swingProgress) * RenderUtilities.Whole) * 0.2F;
		bipedBreast.rotateAngleY = bipedBody.rotateAngleY += angle;
		bipedBreast.rotationOrder = bipedBody.rotationOrder = SmartModelRotationRenderer.YXZ;
		bipedLeftArm.rotateAngleX += angle;
	}

	public void animateWorkingArms() {
		float f6 = 1.0F - modelBiped.swingProgress;
		f6 = 1.0F - f6 * f6 * f6;
		float f7 = MathHelper.sin(f6 * RenderUtilities.Half);
		float f8 = MathHelper.sin(modelBiped.swingProgress * RenderUtilities.Half) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
		bipedRightArm.rotateAngleX -= f7 * 1.2D + f8;
		bipedRightArm.rotateAngleY += MathHelper.sin(MathHelper.sqrt(modelBiped.swingProgress) * RenderUtilities.Whole) * 0.4F;
		bipedRightArm.rotateAngleZ -= MathHelper.sin(modelBiped.swingProgress * RenderUtilities.Half) * 0.4F;
	}

	public void animateSneaking() {
		bipedTorso.rotateAngleX += 0.5F;
		bipedRightLeg.rotateAngleX += -0.5F;
		bipedLeftLeg.rotateAngleX += -0.5F;
		bipedRightArm.rotateAngleX += -0.1F;
		bipedLeftArm.rotateAngleX += -0.1F;

		bipedPelvic.offsetY = -0.13652F;
		bipedPelvic.offsetZ = -0.05652F;

		bipedBreast.offsetY = -0.01872F;
		bipedBreast.offsetZ = -0.07502F;

		bipedNeck.offsetY = 0.0621F;
	}

	public void animateArms(float totalTime) {
		bipedRightArm.rotateAngleZ += MathHelper.cos(totalTime * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(totalTime * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(totalTime * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(totalTime * 0.067F) * 0.05F;
	}

	public void animateBowAiming(float totalTime) {
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightArm.rotateAngleY = -0.1F + bipedHead.rotateAngleY - bipedOuter.rotateAngleY;
		bipedLeftArm.rotateAngleY = 0.1F + bipedHead.rotateAngleY + 0.4F - bipedOuter.rotateAngleY;
		bipedRightArm.rotateAngleX = -1.570796F + bipedHead.rotateAngleX;
		bipedLeftArm.rotateAngleX = -1.570796F + bipedHead.rotateAngleX;
		bipedRightArm.rotateAngleZ += MathHelper.cos(totalTime * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(totalTime * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(totalTime * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(totalTime * 0.067F) * 0.05F;
	}

	public void reset() {
		bipedOuter.reset();
		bipedTorso.reset();
		bipedBody.reset();
		bipedBreast.reset();
		bipedNeck.reset();
		bipedHead.reset();
		bipedRightShoulder.reset();
		bipedRightArm.reset();
		bipedLeftShoulder.reset();
		bipedLeftArm.reset();
		bipedPelvic.reset();
		bipedRightLeg.reset();
		bipedLeftLeg.reset();

		if (isModelPlayer) {
			bipedBodywear.reset();
			bipedHeadwear.reset();
			bipedRightArmwear.reset();
			bipedLeftArmwear.reset();
			bipedRightLegwear.reset();
			bipedLeftLegwear.reset();
			bipedEars.reset();
			bipedCloak.reset();
		}

		bipedRightShoulder.setRotationPoint(-5F, isModelPlayer && smallArms ? 2.5F : 2.0F, 0.0F);
		bipedLeftShoulder.setRotationPoint(5F, isModelPlayer && smallArms ? 2.5F : 2.0F, 0.0F);
		bipedPelvic.setRotationPoint(0.0F, 12.0F, 0.1F);
		bipedRightLeg.setRotationPoint(-1.9F, 0.0F, 0.0F);
		bipedLeftLeg.setRotationPoint(1.9F, 0.0F, 0.0F);

		if (isModelPlayer)
			bipedCloak.setRotationPoint(0.0F, 0.0F, 2.0F);
	}

	public void renderCloak(float f) {
		base.superRenderCloak(f);
	}

	public ModelRenderer getRandomBox(Random par1Random) {
		List<?> boxList = modelBiped.boxList;
		int size = boxList.size();
		int renderersWithBoxes = 0;

		for (int i = 0; i < size; i++) {
			ModelRenderer renderer = (ModelRenderer) boxList.get(i);
			if (canBeRandomBoxSource(renderer))
				renderersWithBoxes++;
		}

		if (renderersWithBoxes != 0) {
			int random = par1Random.nextInt(renderersWithBoxes);
			renderersWithBoxes = -1;

			for (int i = 0; i < size; i++) {
				ModelRenderer renderer = (ModelRenderer) boxList.get(i);
				if (canBeRandomBoxSource(renderer))
					renderersWithBoxes++;
				if (renderersWithBoxes == random)
					return renderer;
			}
		}

		return null;
	}
	
	private void setArmScales(float rightScale, float leftScale) {
		if (scaleArmType == Scale) {
			bipedRightArm.scaleY = rightScale;
			bipedLeftArm.scaleY = leftScale;
		} else if (scaleArmType == NoScaleEnd) {
			bipedRightArm.offsetY -= (1F - rightScale) * 0.5F;
			bipedLeftArm.offsetY -= (1F - leftScale) * 0.5F;
		}
	}
	
	private void setLegScales(float rightScale, float leftScale) {
		if (scaleLegType == Scale) {
			bipedRightLeg.scaleY = rightScale;
			bipedLeftLeg.scaleY = leftScale;
		} else if (scaleLegType == NoScaleEnd) {
			bipedRightLeg.offsetY -= (1F - rightScale) * 0.5F;
			bipedLeftLeg.offsetY -= (1F - leftScale) * 0.5F;
		}
	}

	private static boolean canBeRandomBoxSource(ModelRenderer renderer) {
		return renderer.cubeList != null && renderer.cubeList.size() > 0
				&& (!(renderer instanceof SmartModelRotationRenderer)
						|| ((SmartModelRotationRenderer) renderer).canBeRandomBoxSource());
	}
	
	private static float Factor(float x, float x0, float x1) {
		if (x0 > x1) {
			if (x <= x1)
				return 1F;
			if (x >= x0)
				return 0F;
			return (x0 - x) / (x0 - x1);
		} else {
			if (x >= x1)
				return 1F;
			if (x <= x0)
				return 0F;
			return (x - x0) / (x1 - x0);
		}
	}
	
	@SuppressWarnings("unused")
	private static float Between(float min, float max, float value) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	@SuppressWarnings("unused")
	private static float Normalize(float radiant) {
		while (radiant > Half)
			radiant -= Whole;
		while (radiant < -Half)
			radiant += Whole;
		return radiant;
	}
}
