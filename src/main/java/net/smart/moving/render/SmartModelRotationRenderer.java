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

package net.smart.moving.render;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.smart.moving.utilities.RenderUtilities;
import net.smart.properties.Name;
import net.smart.properties.Reflect;

public class SmartModelRotationRenderer extends ModelRenderer {
	
	protected final static float RadiantToAngle = RenderUtilities.RadiantToAngle;
	protected final static float Whole = RenderUtilities.Whole;
	protected final static float Half = RenderUtilities.Half;

	private static FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
	private static float[] array = new float[16];

	private SmartModelRotationRenderer base;

	public boolean ignoreRender;
	public boolean forceRender;

	private boolean compiled;
	private int displayList;
	public int rotationOrder;

	public float scaleX;
	public float scaleY;
	public float scaleZ;

	public boolean ignoreBase;
	public boolean ignoreSuperRotation;

	public static int XYZ = 0;
	public static int XZY = 1;
	public static int YXZ = 2;
	public static int YZX = 3;
	public static int ZXY = 4;
	public static int ZYX = 5;

	public boolean fadeEnabled;

	private boolean fadeOffsetX;
	private boolean fadeOffsetY;
	private boolean fadeOffsetZ;
	public boolean fadeRotateAngleX;
	public boolean fadeRotateAngleY;
	public boolean fadeRotateAngleZ;
	private boolean fadeRotationPointX;
	private boolean fadeRotationPointY;
	private boolean fadeRotationPointZ;

	public SmartRenderData prevData;

	private final static Name ModelRenderer_compiled = new Name("compiled", "field_78812_q", "t");
	private final static Name ModelRenderer_compileDisplayList = new Name("compileDisplayList", "func_78788_d", "d");
	private final static Name ModelRenderer_displayList = new Name("displayList", "field_78811_r", "u");
	private static Field _compiled = Reflect.GetField(ModelRenderer.class, ModelRenderer_compiled);
	private static Method _compileDisplayList = Reflect.GetMethod(
			ModelRenderer.class, ModelRenderer_compileDisplayList, float.class);
	private static Field _displayList = Reflect.GetField(ModelRenderer.class, ModelRenderer_displayList);

	public SmartModelRotationRenderer(ModelBase modelBase, int i, int j, SmartModelRotationRenderer base) {
		super(modelBase, i, j);
		rotationOrder = XYZ;
		compiled = false;

		this.base = base;
		if (base != null)
			base.addChild(this);

		scaleX = 1.0F;
		scaleY = 1.0F;
		scaleZ = 1.0F;

		fadeEnabled = false;
	}

	@Override
	public void render(float f) {
		if ((!ignoreRender && !ignoreBase) || forceRender)
			doRender(f, ignoreBase);
	}

	public void renderIgnoreBase(float f) {
		if (ignoreBase)
			doRender(f, false);
	}

	public void doRender(float f, boolean useParentTransformations) {
		if (!preRender(f))
			return;
		preTransforms(f, true, useParentTransformations);
		GL11.glCallList(displayList);
		if (childModels != null)
			for (int i = 0; i < childModels.size(); i++)
				childModels.get(i).render(f);
		postTransforms(f, true, useParentTransformations);
	}

	public boolean preRender(float f) {
		if (isHidden)
			return false;

		if (!showModel)
			return false;

		if (!compiled)
			UpdateCompiled();

		if (!compiled) {
			Reflect.Invoke(_compileDisplayList, this, f);
			UpdateDisplayList();
			compiled = true;
		}

		return true;
	}

	public void preTransforms(float f, boolean push, boolean useParentTransformations) {
		if (base != null && !ignoreBase && useParentTransformations)
			base.preTransforms(f, push, true);
		preTransform(f, push);
	}

	public void preTransform(float f, boolean push) {
		if (rotateAngleX != 0.0F || rotateAngleY != 0.0F || rotateAngleZ != 0.0F || ignoreSuperRotation) {
			if (push)
				GL11.glPushMatrix();

			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);

			if (ignoreSuperRotation) {
				buffer.rewind();
				GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
				buffer.get(array);

				GL11.glLoadIdentity();
				GL11.glTranslatef(array[12] / array[15], array[13] / array[15], array[14] / array[15]);
			}

			rotate(rotationOrder, rotateAngleX, rotateAngleY, rotateAngleZ);

			GL11.glScalef(scaleX, scaleY, scaleZ);
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
		} else if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F || scaleX != 1.0F
				|| scaleY != 1.0F || scaleZ != 1.0F || offsetX != 0.0F || offsetY != 0.0F || offsetZ != 0.0F) {
			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
			GL11.glScalef(scaleX, scaleY, scaleZ);
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
		}
	}

	private static void rotate(int rotationOrder, float rotateAngleX, float rotateAngleY, float rotateAngleZ) {
		if ((rotationOrder == ZXY) && rotateAngleY != 0.0F)
			GL11.glRotatef(rotateAngleY * RadiantToAngle, 0.0F, 1.0F, 0.0F);

		if ((rotationOrder == YXZ) && rotateAngleZ != 0.0F)
			GL11.glRotatef(rotateAngleZ * RadiantToAngle, 0.0F, 0.0F, 1.0F);

		if ((rotationOrder == YZX || rotationOrder == YXZ || rotationOrder == ZXY || rotationOrder == ZYX)
				&& rotateAngleX != 0.0F)
			GL11.glRotatef(rotateAngleX * RadiantToAngle, 1.0F, 0.0F, 0.0F);

		if ((rotationOrder == XZY || rotationOrder == ZYX) && rotateAngleY != 0.0F)
			GL11.glRotatef(rotateAngleY * RadiantToAngle, 0.0F, 1.0F, 0.0F);

		if ((rotationOrder == XYZ || rotationOrder == XZY || rotationOrder == YZX || rotationOrder == ZXY
				|| rotationOrder == ZYX) && rotateAngleZ != 0.0F)
			GL11.glRotatef(rotateAngleZ * RadiantToAngle, 0.0F, 0.0F, 1.0F);

		if ((rotationOrder == XYZ || rotationOrder == YXZ || rotationOrder == YZX) && rotateAngleY != 0.0F)
			GL11.glRotatef(rotateAngleY * RadiantToAngle, 0.0F, 1.0F, 0.0F);

		if ((rotationOrder == XYZ || rotationOrder == XZY) && rotateAngleX != 0.0F)
			GL11.glRotatef(rotateAngleX * RadiantToAngle, 1.0F, 0.0F, 0.0F);
	}

	public void postTransform(float f, boolean pop) {
		if (rotateAngleX != 0.0F || rotateAngleY != 0.0F || rotateAngleZ != 0.0F || ignoreSuperRotation) {
			if (pop)
				GL11.glPopMatrix();
		} else if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F || scaleX != 1.0F
				|| scaleY != 1.0F || scaleZ != 1.0F || offsetX != 0.0F || offsetY != 0.0F || offsetZ != 0.0F) {
			GL11.glTranslatef(-offsetX, -offsetY, -offsetZ);
			GL11.glScalef(1F / scaleX, 1F / scaleY, 1F / scaleZ);
			GL11.glTranslatef(-rotationPointX * f, -rotationPointY * f, -rotationPointZ * f);
		}
	}

	public void postTransforms(float f, boolean pop, boolean useParentTransformations) {
		postTransform(f, pop);
		if (base != null && !ignoreBase && useParentTransformations)
			base.postTransforms(f, pop, true);
	}

	public void reset() {
		rotationOrder = XYZ;

		scaleX = 1.0F;
		scaleY = 1.0F;
		scaleZ = 1.0F;

		rotationPointX = 0F;
		rotationPointY = 0F;
		rotationPointZ = 0F;

		rotateAngleX = 0F;
		rotateAngleY = 0F;
		rotateAngleZ = 0F;

		ignoreBase = false;
		ignoreSuperRotation = false;
		forceRender = false;

		offsetX = 0;
		offsetY = 0;
		offsetZ = 0;

		fadeOffsetX = false;
		fadeOffsetY = false;
		fadeOffsetZ = false;
		fadeRotateAngleX = false;
		fadeRotateAngleY = false;
		fadeRotateAngleZ = false;
		fadeRotationPointX = false;
		fadeRotationPointY = false;
		fadeRotationPointZ = false;

		prevData = null;
	}

	@Override
	public void renderWithRotation(float f) {
		boolean update = !compiled;
		super.renderWithRotation(f);
		if (update)
			UpdateLocals();
	}

	@Override
	public void postRender(float f) {
		boolean update = !compiled;
		if (!preRender(f))
			return;
		if (update)
			UpdateLocals();
		preTransforms(f, false, true);
	}

	private void UpdateLocals() {
		UpdateCompiled();
		if (compiled)
			UpdateDisplayList();
	}

	private void UpdateCompiled() {
		compiled = (Boolean) Reflect.GetField(_compiled, this);
	}

	private void UpdateDisplayList() {
		displayList = (Integer) Reflect.GetField(_displayList, this);
	}

	public void fadeStore(float totalTime) {
		if (prevData != null) {
			prevData.offsetX = offsetX;
			prevData.offsetY = offsetY;
			prevData.offsetZ = offsetZ;
			prevData.rotateAngleX = rotateAngleX;
			prevData.rotateAngleY = rotateAngleY;
			prevData.rotateAngleZ = rotateAngleZ;
			prevData.rotationPointX = rotationPointX;
			prevData.rotationPointY = rotationPointY;
			prevData.rotationPointZ = rotationPointZ;
			prevData.totalTime = totalTime;
		}
	}

	public void fadeIntermediate(float totalTime) {
		if (prevData != null && totalTime - prevData.totalTime <= 2F) {
			offsetX = GetIntermediatePosition(prevData.offsetX, offsetX, fadeOffsetX, prevData.totalTime, totalTime);
			offsetY = GetIntermediatePosition(prevData.offsetY, offsetY, fadeOffsetY, prevData.totalTime, totalTime);
			offsetZ = GetIntermediatePosition(prevData.offsetZ, offsetZ, fadeOffsetZ, prevData.totalTime, totalTime);

			rotateAngleX = GetIntermediateAngle(prevData.rotateAngleX, rotateAngleX, fadeRotateAngleX,
					prevData.totalTime, totalTime);
			rotateAngleY = GetIntermediateAngle(prevData.rotateAngleY, rotateAngleY, fadeRotateAngleY,
					prevData.totalTime, totalTime);
			rotateAngleZ = GetIntermediateAngle(prevData.rotateAngleZ, rotateAngleZ, fadeRotateAngleZ,
					prevData.totalTime, totalTime);

			rotationPointX = GetIntermediatePosition(prevData.rotationPointX, rotationPointX, fadeRotationPointX,
					prevData.totalTime, totalTime);
			rotationPointY = GetIntermediatePosition(prevData.rotationPointY, rotationPointY, fadeRotationPointY,
					prevData.totalTime, totalTime);
			rotationPointZ = GetIntermediatePosition(prevData.rotationPointZ, rotationPointZ, fadeRotationPointZ,
					prevData.totalTime, totalTime);
		}
	}

	public boolean canBeRandomBoxSource() {
		return true;
	}

	private static float GetIntermediatePosition(float prevPosition, float shouldPosition, boolean fade,
			float lastTotalTime, float totalTime) {
		if (!fade || shouldPosition == prevPosition)
			return shouldPosition;

		return prevPosition + (shouldPosition - prevPosition) * (totalTime - lastTotalTime) * 0.2F;
	}

	private static float GetIntermediateAngle(float prevAngle, float shouldAngle, boolean fade, float lastTotalTime,
			float totalTime) {
		if (!fade || shouldAngle == prevAngle)
			return shouldAngle;

		while (prevAngle >= Whole)
			prevAngle -= Whole;
		while (prevAngle < 0F)
			prevAngle += Whole;

		while (shouldAngle >= Whole)
			shouldAngle -= Whole;
		while (shouldAngle < 0F)
			shouldAngle += Whole;

		if (shouldAngle > prevAngle && (shouldAngle - prevAngle) > Half)
			prevAngle += Whole;

		if (shouldAngle < prevAngle && (prevAngle - shouldAngle) > Half)
			shouldAngle += Whole;

		return prevAngle + (shouldAngle - prevAngle) * (totalTime - lastTotalTime) * 0.2F;
	}
}
