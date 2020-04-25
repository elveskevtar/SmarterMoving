package net.smart.moving.config;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.smart.properties.Properties;
import net.smart.properties.Property;

public class SmartOptions extends SmartConfig {
	
	private static final long serialVersionUID = -4494451153477722807L;
	private static final File optionsPath = Minecraft.getMinecraft().mcDataDir;
	
	private final Property<String> defaultGrabKeyName = Properties.String("move.grab.default.key.name")
			.singular().defaults("G").comment("Default key name to \"grab\" (default: \"G\")");
	
	/* https://minecraft.gamepedia.com/Key_codes */
	public final Property<Integer> defaultGrabKeyCode = defaultGrabKeyName.toKeyCode(34);
	
	public static KeyBinding grabKey;
	
	public SmartOptions() {
		loadFromOptionsFile(optionsPath);
		saveToOptionsFile(optionsPath);
		
		grabKey = new KeyBinding("key.climb", defaultGrabKeyCode.value, "key.categories.gameplay");
	}
}
