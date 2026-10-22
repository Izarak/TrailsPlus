package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.trails.IPlayerTrailAction;

public enum PlayerTrails {
    FLAME_STEP(new FlameStepTrail()),
    HEARTS(new HeartsTrail()),
    RAIN_CLOUD(new RainCloudTrail()),
    CONTAGION(new ContagionTrail()),
    CHERRY_LEAVES_FALL(new CherryLeavesTrail()),
    FALL_LEAVES(new FallLeavesTrail()),
    MUSIC_NOTES(new MusicNotesTrail()),
    FLOWER_POWER(new FlowerPowerTrail()),
    SPITTING(new SpitTrail()),
    FIRE_FLY(new FireflyTrail()),
    OMEN_RAID(new RaidOmenTrail()),
    OMEN_TRIAL(new TrialOmenTrail()),
    OMEN_BOTH(new BothOmenTrail()),
    SOUL(new SoulTrail()),
    SCULK_SOUL(new SculkSoulTrail()),
    // October 2026
    HAUNTED_LANTERN(new HauntedLanternTrail());

    final IPlayerTrailAction trailAction;

    PlayerTrails(IPlayerTrailAction trailAction) {
        this.trailAction = trailAction;
    }

    public IPlayerTrailAction getTrailAction() {
        return trailAction;
    }
}
