package net.smart.moving.capabilities;

import java.util.concurrent.Callable;

public class SmartStateHandler implements ISmartStateHandler {
	
	private byte state;

	@Override
	public byte getSmartState() {
		return state;
	}

	@Override
	public void setSmartState(byte state) {
		this.state = state;
	}
	
	public static class Factory implements Callable<ISmartStateHandler> {

		@Override
		public ISmartStateHandler call() throws Exception {
			return new SmartStateHandler();
		}
	}
}
