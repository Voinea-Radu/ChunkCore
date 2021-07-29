package dev.lightdream.ultraprisoncore.gangs.fight;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;

public enum FightArenaState {
    NOT_CONFIGURED,
    EMPTY,
    WAITING,
    IN_PROGRESS,
    ENDED;

    FightArenaState() {
    }

    public Lang getTranslation() {
        switch (this) {
            case EMPTY:
                return Lang.MSG_FIGHTS_ARENASTATE_EMPTY;
            case ENDED:
                return Lang.MSG_FIGHTS_ARENASTATE_ENDED;
            case IN_PROGRESS:
                return Lang.MSG_FIGHTS_ARENASTATE_INPROGRESS;
            case NOT_CONFIGURED:
                return Lang.MSG_FIGHTS_ARENASTATE_NOTCONFIGURED;
            case WAITING:
                return Lang.MSG_FIGHTS_ARENASTATE_WAITING;
            default:
                return null;
        }
    }
}
