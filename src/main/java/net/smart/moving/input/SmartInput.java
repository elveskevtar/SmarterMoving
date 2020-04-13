package net.smart.moving.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.smart.moving.SmartMovingContext;

public class SmartInput {
	
	private static boolean initialized = false;
	
	public static boolean isKeyDown(KeyBinding binding) {
		if (!Minecraft.getMinecraft().inGameHasFocus)
			return false;
		
		GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
		if (currentScreen != null && !currentScreen.allowUserInput) {
			int keyCode = binding.getKeyCode();
			if (keyCode >= 0)
				return Keyboard.isKeyDown(keyCode);
			return Mouse.isButtonDown(keyCode + 100);
		}
		
		return binding.isKeyDown();
	}
	
	public static void registerKeys() {
		if (initialized)
			return;
		
		ClientRegistry.registerKeyBinding(SmartMovingContext.Options.grabKey);
		
		initialized = true;
	}
}
