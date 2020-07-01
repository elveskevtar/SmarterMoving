package net.smart.moving.player.state;

public enum State {
    INVALID(-1, new StateHandler() {}),
    IDLE(0, new StateHandlerIdle()),
    SNEAK(1, new StateHandlerSneak()),
    CRAWL(2, new StateHandlerCrawl()),
    FLY(3, new StateHandlerFly()),
    ELYTRA(4, new StateHandlerElytra());

    public final byte id;
    public final StateHandler handler;

    State(int id, StateHandler handler) {
        this.id = (byte) id;
        this.handler = handler;
    }

    public static State getState(byte stateId) {
        for (State state : State.values())
            if (state.id == stateId)
                return state;
        return State.INVALID;
    }
}
