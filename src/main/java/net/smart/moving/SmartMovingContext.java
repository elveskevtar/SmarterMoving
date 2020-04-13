package net.smart.moving;

import net.smart.moving.config.SmartConfig;
import net.smart.moving.config.SmartOptions;
import net.smart.moving.input.SmartInput;

public class SmartMovingContext {
	
	public static final SmartOptions Options = new SmartOptions();
	public static final SmartInput Input = new SmartInput();
	
	private static boolean initialized = false;
	
	public static void initialize() {
		if (initialized)
			return;
		
		Input.registerKeys();
		
		initialized = true;
	}
}
