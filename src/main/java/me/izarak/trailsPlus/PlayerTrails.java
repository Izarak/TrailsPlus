package me.izarak.trailsPlus;

import me.izarak.trailsPlus.trails.EmptyTrailAction;
import me.izarak.trailsPlus.trails.ITrailAction;
import me.izarak.trailsPlus.trails.player.*;

public enum PlayerTrails {
    NONE(new EmptyTrailAction()),
    FLAME_STEP(new FlameStepTrail()),
    HEARTS(new HeartsTrail()),
    RAIN_CLOUD(new RainCloudTrail()),
    CONTAGION(new ContagionTrail()),
    CHERRY_LEAVES_FALL(new CherryLeavesTrail()),
    FALL_LEAVES(new FallLeavesTrail()),
    // October 2026
    HAUNTED_LANTERN(new HauntedLanternTrail());

    final ITrailAction trailAction;

    PlayerTrails(ITrailAction trailAction) {
        this.trailAction = trailAction;
    }

    public ITrailAction getTrailAction() {
        return trailAction;
    }
}
